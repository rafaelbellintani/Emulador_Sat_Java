// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.Configurable;
import java.util.Locale;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.FTPClientConfig;

public class DefaultFTPFileEntryParserFactory implements FTPFileEntryParserFactory
{
    private FTPClientConfig config;
    
    public DefaultFTPFileEntryParserFactory() {
        this.config = null;
    }
    
    public FTPFileEntryParser createFileEntryParser(final String key) {
        if (key == null) {
            throw new ParserInitializationException("Parser key cannot be null");
        }
        Class<?> parserClass = null;
        FTPFileEntryParser parser = null;
        try {
            parserClass = Class.forName(key);
            parser = (FTPFileEntryParser)parserClass.newInstance();
        }
        catch (ClassNotFoundException e4) {
            try {
                String ukey = null;
                if (null != key) {
                    ukey = key.toUpperCase(Locale.ENGLISH);
                }
                if (ukey.indexOf("UNIX") >= 0 || ukey.indexOf("TYPE: L8") >= 0) {
                    parser = this.createUnixFTPEntryParser();
                }
                else if (ukey.indexOf("VMS") >= 0) {
                    parser = this.createVMSVersioningFTPEntryParser();
                }
                else if (ukey.indexOf("WINDOWS") >= 0) {
                    parser = this.createNTFTPEntryParser();
                }
                else if (ukey.indexOf("OS/2") >= 0) {
                    parser = this.createOS2FTPEntryParser();
                }
                else if (ukey.indexOf("OS/400") >= 0 || ukey.indexOf("AS/400") >= 0) {
                    parser = this.createOS400FTPEntryParser();
                }
                else if (ukey.indexOf("MVS") >= 0) {
                    parser = this.createMVSEntryParser();
                }
                else {
                    if (ukey.indexOf("NETWARE") < 0) {
                        throw new ParserInitializationException("Unknown parser type: " + key);
                    }
                    parser = this.createNetwareFTPEntryParser();
                }
            }
            catch (NoClassDefFoundError nf) {
                throw new ParserInitializationException("Error initializing parser", nf);
            }
        }
        catch (NoClassDefFoundError e) {
            throw new ParserInitializationException("Error initializing parser", e);
        }
        catch (ClassCastException e2) {
            throw new ParserInitializationException(parserClass.getName() + " does not implement the interface " + "org.apache.commons.net.ftp.FTPFileEntryParser.", e2);
        }
        catch (Throwable e3) {
            throw new ParserInitializationException("Error initializing parser", e3);
        }
        if (parser instanceof Configurable) {
            ((Configurable)parser).configure(this.config);
        }
        return parser;
    }
    
    public FTPFileEntryParser createFileEntryParser(final FTPClientConfig config) throws ParserInitializationException {
        this.config = config;
        final String key = config.getServerSystemKey();
        return this.createFileEntryParser(key);
    }
    
    public FTPFileEntryParser createUnixFTPEntryParser() {
        return new UnixFTPEntryParser();
    }
    
    public FTPFileEntryParser createVMSVersioningFTPEntryParser() {
        return new VMSVersioningFTPEntryParser();
    }
    
    public FTPFileEntryParser createNetwareFTPEntryParser() {
        return new NetwareFTPEntryParser();
    }
    
    public FTPFileEntryParser createNTFTPEntryParser() {
        if (this.config != null && "WINDOWS".equals(this.config.getServerSystemKey())) {
            return new NTFTPEntryParser();
        }
        return new CompositeFileEntryParser(new FTPFileEntryParser[] { new NTFTPEntryParser(), new UnixFTPEntryParser() });
    }
    
    public FTPFileEntryParser createOS2FTPEntryParser() {
        return new OS2FTPEntryParser();
    }
    
    public FTPFileEntryParser createOS400FTPEntryParser() {
        if (this.config != null && "OS/400".equals(this.config.getServerSystemKey())) {
            return new OS400FTPEntryParser();
        }
        return new CompositeFileEntryParser(new FTPFileEntryParser[] { new OS400FTPEntryParser(), new UnixFTPEntryParser() });
    }
    
    public FTPFileEntryParser createMVSEntryParser() {
        return new MVSFTPEntryParser();
    }
}
