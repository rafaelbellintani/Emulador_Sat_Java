// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.net.io.SocketInputStream;
import org.apache.commons.net.io.FromNetASCIIInputStream;
import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import org.apache.commons.net.io.SocketOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.io.ToNetASCIIOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;

public class FTPClient extends FTP implements Configurable
{
    public static final int ACTIVE_LOCAL_DATA_CONNECTION_MODE = 0;
    public static final int ACTIVE_REMOTE_DATA_CONNECTION_MODE = 1;
    public static final int PASSIVE_LOCAL_DATA_CONNECTION_MODE = 2;
    public static final int PASSIVE_REMOTE_DATA_CONNECTION_MODE = 3;
    private int __dataConnectionMode;
    private int __dataTimeout;
    private int __passivePort;
    private String __passiveHost;
    private int __fileType;
    private int __fileFormat;
    private int __fileStructure;
    private int __fileTransferMode;
    private boolean __remoteVerificationEnabled;
    private long __restartOffset;
    private FTPFileEntryParserFactory __parserFactory;
    private int __bufferSize;
    private boolean __listHiddenFiles;
    private String __systemName;
    private FTPFileEntryParser __entryParser;
    private FTPClientConfig __configuration;
    private static String __parms;
    private static Pattern __parms_pat;
    
    public FTPClient() {
        this.__initDefaults();
        this.__dataTimeout = -1;
        this.__remoteVerificationEnabled = true;
        this.__parserFactory = new DefaultFTPFileEntryParserFactory();
        this.__configuration = null;
        this.__listHiddenFiles = false;
    }
    
    private void __initDefaults() {
        this.__dataConnectionMode = 0;
        this.__passiveHost = null;
        this.__passivePort = -1;
        this.__fileType = 0;
        this.__fileStructure = 7;
        this.__fileFormat = 4;
        this.__fileTransferMode = 10;
        this.__restartOffset = 0L;
        this.__systemName = null;
        this.__entryParser = null;
        this.__bufferSize = 1024;
    }
    
    private String __parsePathname(final String reply) {
        final int begin = reply.indexOf(34) + 1;
        final int end = reply.indexOf(34, begin);
        return reply.substring(begin, end);
    }
    
    private void __parsePassiveModeReply(String reply) throws MalformedServerReplyException {
        final Matcher m = FTPClient.__parms_pat.matcher(reply);
        if (!m.find()) {
            throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
        }
        reply = m.group();
        final String[] parts = m.group().split(",");
        this.__passiveHost = parts[0] + '.' + parts[1] + '.' + parts[2] + '.' + parts[3];
        try {
            final int oct1 = Integer.parseInt(parts[4]);
            final int oct2 = Integer.parseInt(parts[5]);
            this.__passivePort = (oct1 << 8 | oct2);
        }
        catch (NumberFormatException e) {
            throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
        }
    }
    
    private boolean __storeFile(final int command, final String remote, final InputStream local) throws IOException {
        final Socket socket;
        if ((socket = this._openDataConnection_(command, remote)) == null) {
            return false;
        }
        OutputStream output = new BufferedOutputStream(socket.getOutputStream(), this.getBufferSize());
        if (this.__fileType == 0) {
            output = new ToNetASCIIOutputStream(output);
        }
        try {
            Util.copyStream(local, output, this.getBufferSize(), -1L, null, false);
        }
        catch (IOException e) {
            try {
                socket.close();
            }
            catch (IOException ex) {}
            throw e;
        }
        output.close();
        socket.close();
        return this.completePendingCommand();
    }
    
    private OutputStream __storeFileStream(final int command, final String remote) throws IOException {
        final Socket socket;
        if ((socket = this._openDataConnection_(command, remote)) == null) {
            return null;
        }
        OutputStream output = socket.getOutputStream();
        if (this.__fileType == 0) {
            output = new BufferedOutputStream(output, this.getBufferSize());
            output = new ToNetASCIIOutputStream(output);
        }
        return new SocketOutputStream(socket, output);
    }
    
    protected Socket _openDataConnection_(final int command, final String arg) throws IOException {
        if (this.__dataConnectionMode != 0 && this.__dataConnectionMode != 2) {
            return null;
        }
        Socket socket;
        if (this.__dataConnectionMode == 0) {
            final ServerSocket server = this._serverSocketFactory_.createServerSocket(0, 1, this.getLocalAddress());
            if (!FTPReply.isPositiveCompletion(this.port(this.getLocalAddress(), server.getLocalPort()))) {
                server.close();
                return null;
            }
            if (this.__restartOffset > 0L && !this.restart(this.__restartOffset)) {
                server.close();
                return null;
            }
            if (!FTPReply.isPositivePreliminary(this.sendCommand(command, arg))) {
                server.close();
                return null;
            }
            if (this.__dataTimeout >= 0) {
                server.setSoTimeout(this.__dataTimeout);
            }
            try {
                socket = server.accept();
            }
            finally {
                server.close();
            }
        }
        else {
            if (this.pasv() != 227) {
                return null;
            }
            this.__parsePassiveModeReply(this._replyLines.get(this._replyLines.size() - 1));
            socket = this._socketFactory_.createSocket(this.__passiveHost, this.__passivePort);
            if (this.__restartOffset > 0L && !this.restart(this.__restartOffset)) {
                socket.close();
                return null;
            }
            if (!FTPReply.isPositivePreliminary(this.sendCommand(command, arg))) {
                socket.close();
                return null;
            }
        }
        if (this.__remoteVerificationEnabled && !this.verifyRemote(socket)) {
            final InetAddress host1 = socket.getInetAddress();
            final InetAddress host2 = this.getRemoteAddress();
            socket.close();
            throw new IOException("Host attempting data connection " + host1.getHostAddress() + " is not same as server " + host2.getHostAddress());
        }
        if (this.__dataTimeout >= 0) {
            socket.setSoTimeout(this.__dataTimeout);
        }
        return socket;
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this.__initDefaults();
    }
    
    public void setDataTimeout(final int timeout) {
        this.__dataTimeout = timeout;
    }
    
    public void setParserFactory(final FTPFileEntryParserFactory parserFactory) {
        this.__parserFactory = parserFactory;
    }
    
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        this.__initDefaults();
    }
    
    public void setRemoteVerificationEnabled(final boolean enable) {
        this.__remoteVerificationEnabled = enable;
    }
    
    public boolean isRemoteVerificationEnabled() {
        return this.__remoteVerificationEnabled;
    }
    
    public boolean login(final String username, final String password) throws IOException {
        this.user(username);
        return FTPReply.isPositiveCompletion(this._replyCode) || (FTPReply.isPositiveIntermediate(this._replyCode) && FTPReply.isPositiveCompletion(this.pass(password)));
    }
    
    public boolean login(final String username, final String password, final String account) throws IOException {
        this.user(username);
        if (FTPReply.isPositiveCompletion(this._replyCode)) {
            return true;
        }
        if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
            return false;
        }
        this.pass(password);
        return FTPReply.isPositiveCompletion(this._replyCode) || (FTPReply.isPositiveIntermediate(this._replyCode) && FTPReply.isPositiveCompletion(this.acct(account)));
    }
    
    public boolean logout() throws IOException {
        return FTPReply.isPositiveCompletion(this.quit());
    }
    
    public boolean changeWorkingDirectory(final String pathname) throws IOException {
        return FTPReply.isPositiveCompletion(this.cwd(pathname));
    }
    
    public boolean changeToParentDirectory() throws IOException {
        return FTPReply.isPositiveCompletion(this.cdup());
    }
    
    public boolean structureMount(final String pathname) throws IOException {
        return FTPReply.isPositiveCompletion(this.smnt(pathname));
    }
    
    boolean reinitialize() throws IOException {
        this.rein();
        if (FTPReply.isPositiveCompletion(this._replyCode) || (FTPReply.isPositivePreliminary(this._replyCode) && FTPReply.isPositiveCompletion(this.getReply()))) {
            this.__initDefaults();
            return true;
        }
        return false;
    }
    
    public void enterLocalActiveMode() {
        this.__dataConnectionMode = 0;
        this.__passiveHost = null;
        this.__passivePort = -1;
    }
    
    public void enterLocalPassiveMode() {
        this.__dataConnectionMode = 2;
        this.__passiveHost = null;
        this.__passivePort = -1;
    }
    
    public boolean enterRemoteActiveMode(final InetAddress host, final int port) throws IOException {
        if (FTPReply.isPositiveCompletion(this.port(host, port))) {
            this.__dataConnectionMode = 1;
            this.__passiveHost = null;
            this.__passivePort = -1;
            return true;
        }
        return false;
    }
    
    public boolean enterRemotePassiveMode() throws IOException {
        if (this.pasv() != 227) {
            return false;
        }
        this.__dataConnectionMode = 3;
        this.__parsePassiveModeReply(this._replyLines.get(0));
        return true;
    }
    
    public String getPassiveHost() {
        return this.__passiveHost;
    }
    
    public int getPassivePort() {
        return this.__passivePort;
    }
    
    public int getDataConnectionMode() {
        return this.__dataConnectionMode;
    }
    
    public boolean setFileType(final int fileType) throws IOException {
        if (FTPReply.isPositiveCompletion(this.type(fileType))) {
            this.__fileType = fileType;
            this.__fileFormat = 4;
            return true;
        }
        return false;
    }
    
    public boolean setFileType(final int fileType, final int formatOrByteSize) throws IOException {
        if (FTPReply.isPositiveCompletion(this.type(fileType, formatOrByteSize))) {
            this.__fileType = fileType;
            this.__fileFormat = formatOrByteSize;
            return true;
        }
        return false;
    }
    
    public boolean setFileStructure(final int structure) throws IOException {
        if (FTPReply.isPositiveCompletion(this.stru(structure))) {
            this.__fileStructure = structure;
            return true;
        }
        return false;
    }
    
    public boolean setFileTransferMode(final int mode) throws IOException {
        if (FTPReply.isPositiveCompletion(this.mode(mode))) {
            this.__fileTransferMode = mode;
            return true;
        }
        return false;
    }
    
    public boolean remoteRetrieve(final String filename) throws IOException {
        return (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3) && FTPReply.isPositivePreliminary(this.retr(filename));
    }
    
    public boolean remoteStore(final String filename) throws IOException {
        return (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3) && FTPReply.isPositivePreliminary(this.stor(filename));
    }
    
    public boolean remoteStoreUnique(final String filename) throws IOException {
        return (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3) && FTPReply.isPositivePreliminary(this.stou(filename));
    }
    
    public boolean remoteStoreUnique() throws IOException {
        return (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3) && FTPReply.isPositivePreliminary(this.stou());
    }
    
    public boolean remoteAppend(final String filename) throws IOException {
        return (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3) && FTPReply.isPositivePreliminary(this.stor(filename));
    }
    
    public boolean completePendingCommand() throws IOException {
        return FTPReply.isPositiveCompletion(this.getReply());
    }
    
    public boolean retrieveFile(final String remote, final OutputStream local) throws IOException {
        final Socket socket;
        if ((socket = this._openDataConnection_(13, remote)) == null) {
            return false;
        }
        InputStream input = new BufferedInputStream(socket.getInputStream(), this.getBufferSize());
        if (this.__fileType == 0) {
            input = new FromNetASCIIInputStream(input);
        }
        try {
            Util.copyStream(input, local, this.getBufferSize(), -1L, null, false);
        }
        catch (IOException e) {
            try {
                socket.close();
            }
            catch (IOException ex) {}
            throw e;
        }
        socket.close();
        return this.completePendingCommand();
    }
    
    public InputStream retrieveFileStream(final String remote) throws IOException {
        final Socket socket;
        if ((socket = this._openDataConnection_(13, remote)) == null) {
            return null;
        }
        InputStream input = socket.getInputStream();
        if (this.__fileType == 0) {
            input = new BufferedInputStream(input, this.getBufferSize());
            input = new FromNetASCIIInputStream(input);
        }
        return new SocketInputStream(socket, input);
    }
    
    public boolean storeFile(final String remote, final InputStream local) throws IOException {
        return this.__storeFile(14, remote, local);
    }
    
    public OutputStream storeFileStream(final String remote) throws IOException {
        return this.__storeFileStream(14, remote);
    }
    
    public boolean appendFile(final String remote, final InputStream local) throws IOException {
        return this.__storeFile(16, remote, local);
    }
    
    public OutputStream appendFileStream(final String remote) throws IOException {
        return this.__storeFileStream(16, remote);
    }
    
    public boolean storeUniqueFile(final String remote, final InputStream local) throws IOException {
        return this.__storeFile(15, remote, local);
    }
    
    public OutputStream storeUniqueFileStream(final String remote) throws IOException {
        return this.__storeFileStream(15, remote);
    }
    
    public boolean storeUniqueFile(final InputStream local) throws IOException {
        return this.__storeFile(15, null, local);
    }
    
    public OutputStream storeUniqueFileStream() throws IOException {
        return this.__storeFileStream(15, null);
    }
    
    public boolean allocate(final int bytes) throws IOException {
        return FTPReply.isPositiveCompletion(this.allo(bytes));
    }
    
    public boolean allocate(final int bytes, final int recordSize) throws IOException {
        return FTPReply.isPositiveCompletion(this.allo(bytes, recordSize));
    }
    
    private boolean restart(final long offset) throws IOException {
        this.__restartOffset = 0L;
        return FTPReply.isPositiveIntermediate(this.rest(Long.toString(offset)));
    }
    
    public void setRestartOffset(final long offset) {
        if (offset >= 0L) {
            this.__restartOffset = offset;
        }
    }
    
    public long getRestartOffset() {
        return this.__restartOffset;
    }
    
    public boolean rename(final String from, final String to) throws IOException {
        return FTPReply.isPositiveIntermediate(this.rnfr(from)) && FTPReply.isPositiveCompletion(this.rnto(to));
    }
    
    public boolean abort() throws IOException {
        return FTPReply.isPositiveCompletion(this.abor());
    }
    
    public boolean deleteFile(final String pathname) throws IOException {
        return FTPReply.isPositiveCompletion(this.dele(pathname));
    }
    
    public boolean removeDirectory(final String pathname) throws IOException {
        return FTPReply.isPositiveCompletion(this.rmd(pathname));
    }
    
    public boolean makeDirectory(final String pathname) throws IOException {
        return FTPReply.isPositiveCompletion(this.mkd(pathname));
    }
    
    public String printWorkingDirectory() throws IOException {
        if (this.pwd() != 257) {
            return null;
        }
        return this.__parsePathname(this._replyLines.get(this._replyLines.size() - 1));
    }
    
    public boolean sendSiteCommand(final String arguments) throws IOException {
        return FTPReply.isPositiveCompletion(this.site(arguments));
    }
    
    public String getSystemName() throws IOException {
        if (this.__systemName == null && FTPReply.isPositiveCompletion(this.syst())) {
            this.__systemName = this._replyLines.get(this._replyLines.size() - 1).substring(4);
        }
        return this.__systemName;
    }
    
    public String listHelp() throws IOException {
        if (FTPReply.isPositiveCompletion(this.help())) {
            return this.getReplyString();
        }
        return null;
    }
    
    public String listHelp(final String command) throws IOException {
        if (FTPReply.isPositiveCompletion(this.help(command))) {
            return this.getReplyString();
        }
        return null;
    }
    
    public boolean sendNoOp() throws IOException {
        return FTPReply.isPositiveCompletion(this.noop());
    }
    
    public String[] listNames(final String pathname) throws IOException {
        final Socket socket;
        if ((socket = this._openDataConnection_(27, pathname)) == null) {
            return null;
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), this.getControlEncoding()));
        final ArrayList<String> results = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            results.add(line);
        }
        reader.close();
        socket.close();
        if (this.completePendingCommand()) {
            final String[] names = new String[results.size()];
            return results.toArray(names);
        }
        return null;
    }
    
    public String[] listNames() throws IOException {
        return this.listNames(null);
    }
    
    public FTPFile[] listFiles(final String pathname) throws IOException {
        final String key = null;
        final FTPListParseEngine engine = this.initiateListParsing(key, pathname);
        return engine.getFiles();
    }
    
    public FTPFile[] listFiles() throws IOException {
        return this.listFiles(null);
    }
    
    public FTPListParseEngine initiateListParsing() throws IOException {
        return this.initiateListParsing(null);
    }
    
    public FTPListParseEngine initiateListParsing(final String pathname) throws IOException {
        final String key = null;
        return this.initiateListParsing(key, pathname);
    }
    
    public FTPListParseEngine initiateListParsing(final String parserKey, final String pathname) throws IOException {
        if (this.__entryParser == null) {
            if (null != parserKey) {
                this.__entryParser = this.__parserFactory.createFileEntryParser(parserKey);
            }
            else if (null != this.__configuration) {
                this.__entryParser = this.__parserFactory.createFileEntryParser(this.__configuration);
            }
            else {
                this.__entryParser = this.__parserFactory.createFileEntryParser(this.getSystemName());
            }
        }
        return this.initiateListParsing(this.__entryParser, pathname);
    }
    
    private FTPListParseEngine initiateListParsing(final FTPFileEntryParser parser, final String pathname) throws IOException {
        final FTPListParseEngine engine = new FTPListParseEngine(parser);
        final Socket socket;
        if ((socket = this._openDataConnection_(26, this.getListArguments(pathname))) == null) {
            return engine;
        }
        try {
            engine.readServerList(socket.getInputStream(), this.getControlEncoding());
        }
        finally {
            socket.close();
        }
        this.completePendingCommand();
        return engine;
    }
    
    protected String getListArguments(final String pathname) {
        if (this.getListHiddenFiles()) {
            final StringBuffer sb = new StringBuffer(pathname.length() + 3);
            sb.append("-a ");
            sb.append(pathname);
            return sb.toString();
        }
        return pathname;
    }
    
    public String getStatus() throws IOException {
        if (FTPReply.isPositiveCompletion(this.stat())) {
            return this.getReplyString();
        }
        return null;
    }
    
    public String getStatus(final String pathname) throws IOException {
        if (FTPReply.isPositiveCompletion(this.stat(pathname))) {
            return this.getReplyString();
        }
        return null;
    }
    
    public String getModificationTime(final String pathname) throws IOException {
        if (FTPReply.isPositiveCompletion(this.mdtm(pathname))) {
            return this.getReplyString();
        }
        return null;
    }
    
    public void setBufferSize(final int bufSize) {
        this.__bufferSize = bufSize;
    }
    
    public int getBufferSize() {
        return this.__bufferSize;
    }
    
    public void configure(final FTPClientConfig config) {
        this.__configuration = config;
    }
    
    public void setListHiddenFiles(final boolean listHiddenFiles) {
        this.__listHiddenFiles = listHiddenFiles;
    }
    
    public boolean getListHiddenFiles() {
        return this.__listHiddenFiles;
    }
    
    static {
        FTPClient.__parms = "\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}";
        FTPClient.__parms_pat = Pattern.compile(FTPClient.__parms);
    }
}
