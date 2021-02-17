// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.tftp;

import org.apache.commons.net.io.ToNetASCIIInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.io.IOException;
import org.apache.commons.net.io.FromNetASCIIOutputStream;
import java.net.InetAddress;
import java.io.OutputStream;

public class TFTPClient extends TFTP
{
    public static final int DEFAULT_MAX_TIMEOUTS = 5;
    private int __maxTimeouts;
    
    public TFTPClient() {
        this.__maxTimeouts = 5;
    }
    
    public void setMaxTimeouts(final int numTimeouts) {
        if (numTimeouts < 1) {
            this.__maxTimeouts = 1;
        }
        else {
            this.__maxTimeouts = numTimeouts;
        }
    }
    
    public int getMaxTimeouts() {
        return this.__maxTimeouts;
    }
    
    public int receiveFile(final String filename, final int mode, OutputStream output, InetAddress host, final int port) throws IOException {
        TFTPPacket received = null;
        final TFTPAckPacket ack = new TFTPAckPacket(host, port, 0);
        this.beginBufferedOps();
        int bytesRead;
        int hostPort;
        int dataLength;
        int lastBlock = dataLength = (hostPort = (bytesRead = 0));
        int block = 1;
        if (mode == 0) {
            output = new FromNetASCIIOutputStream(output);
        }
        TFTPPacket sent = new TFTPReadRequestPacket(host, port, filename, mode);
    Label_0534:
        while (true) {
            do {
                this.bufferedSend(sent);
                do {
                    int timeouts = 0;
                    while (timeouts < this.__maxTimeouts) {
                        try {
                            received = this.bufferedReceive();
                        }
                        catch (SocketException e3) {
                            if (++timeouts >= this.__maxTimeouts) {
                                this.endBufferedOps();
                                throw new IOException("Connection timed out.");
                            }
                            continue;
                        }
                        catch (InterruptedIOException e4) {
                            if (++timeouts >= this.__maxTimeouts) {
                                this.endBufferedOps();
                                throw new IOException("Connection timed out.");
                            }
                            continue;
                        }
                        catch (TFTPPacketException e) {
                            this.endBufferedOps();
                            throw new IOException("Bad packet: " + e.getMessage());
                        }
                        break;
                    }
                    if (lastBlock == 0) {
                        hostPort = received.getPort();
                        ack.setPort(hostPort);
                        if (!host.equals(received.getAddress())) {
                            host = received.getAddress();
                            ack.setAddress(host);
                            sent.setAddress(host);
                        }
                    }
                    if (!host.equals(received.getAddress()) || received.getPort() != hostPort) {
                        final TFTPErrorPacket error = new TFTPErrorPacket(received.getAddress(), received.getPort(), 5, "Unexpected host or port.");
                        this.bufferedSend(error);
                        break;
                    }
                    switch (received.getType()) {
                        case 5: {
                            final TFTPErrorPacket error = (TFTPErrorPacket)received;
                            this.endBufferedOps();
                            throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
                        }
                        case 3: {
                            final TFTPDataPacket data = (TFTPDataPacket)received;
                            dataLength = data.getDataLength();
                            lastBlock = data.getBlockNumber();
                            if (lastBlock == block) {
                                try {
                                    output.write(data.getData(), data.getDataOffset(), dataLength);
                                }
                                catch (IOException e2) {
                                    final TFTPErrorPacket error = new TFTPErrorPacket(host, hostPort, 3, "File write failed.");
                                    this.bufferedSend(error);
                                    this.endBufferedOps();
                                    throw e2;
                                }
                                if (++block > 65535) {
                                    block = 0;
                                }
                                ack.setBlockNumber(lastBlock);
                                sent = ack;
                                bytesRead += dataLength;
                                continue Label_0534;
                            }
                            this.discardPackets();
                            continue;
                        }
                        default: {
                            this.endBufferedOps();
                            throw new IOException("Received unexpected packet type.");
                        }
                    }
                } while (lastBlock != ((block == 0) ? 65535 : (block - 1)));
            } while (dataLength == 512);
            break;
        }
        this.bufferedSend(sent);
        this.endBufferedOps();
        return bytesRead;
    }
    
    public int receiveFile(final String filename, final int mode, final OutputStream output, final String hostname, final int port) throws UnknownHostException, IOException {
        return this.receiveFile(filename, mode, output, InetAddress.getByName(hostname), port);
    }
    
    public int receiveFile(final String filename, final int mode, final OutputStream output, final InetAddress host) throws IOException {
        return this.receiveFile(filename, mode, output, host, 69);
    }
    
    public int receiveFile(final String filename, final int mode, final OutputStream output, final String hostname) throws UnknownHostException, IOException {
        return this.receiveFile(filename, mode, output, InetAddress.getByName(hostname), 69);
    }
    
    public void sendFile(final String filename, final int mode, InputStream input, InetAddress host, final int port) throws IOException {
        TFTPPacket received = null;
        final TFTPDataPacket data = new TFTPDataPacket(host, port, 0, this._sendBuffer, 4, 0);
        boolean justStarted = true;
        this.beginBufferedOps();
        int totalThisPacket;
        int bytesRead;
        int hostPort;
        int dataLength;
        int lastBlock = dataLength = (hostPort = (bytesRead = (totalThisPacket = 0)));
        int block = 0;
        boolean lastAckWait = false;
        if (mode == 0) {
            input = new ToNetASCIIInputStream(input);
        }
        TFTPPacket sent = new TFTPWriteRequestPacket(host, port, filename, mode);
    Label_0576:
        while (true) {
            do {
                this.bufferedSend(sent);
                do {
                    int timeouts = 0;
                    while (timeouts < this.__maxTimeouts) {
                        try {
                            received = this.bufferedReceive();
                        }
                        catch (SocketException e2) {
                            if (++timeouts >= this.__maxTimeouts) {
                                this.endBufferedOps();
                                throw new IOException("Connection timed out.");
                            }
                            continue;
                        }
                        catch (InterruptedIOException e3) {
                            if (++timeouts >= this.__maxTimeouts) {
                                this.endBufferedOps();
                                throw new IOException("Connection timed out.");
                            }
                            continue;
                        }
                        catch (TFTPPacketException e) {
                            this.endBufferedOps();
                            throw new IOException("Bad packet: " + e.getMessage());
                        }
                        break;
                    }
                    if (justStarted) {
                        justStarted = false;
                        hostPort = received.getPort();
                        data.setPort(hostPort);
                        if (!host.equals(received.getAddress())) {
                            host = received.getAddress();
                            data.setAddress(host);
                            sent.setAddress(host);
                        }
                    }
                    if (!host.equals(received.getAddress()) || received.getPort() != hostPort) {
                        final TFTPErrorPacket error = new TFTPErrorPacket(received.getAddress(), received.getPort(), 5, "Unexpected host or port.");
                        this.bufferedSend(error);
                        break;
                    }
                    switch (received.getType()) {
                        case 5: {
                            final TFTPErrorPacket error = (TFTPErrorPacket)received;
                            this.endBufferedOps();
                            throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
                        }
                        case 4: {
                            final TFTPAckPacket ack = (TFTPAckPacket)received;
                            lastBlock = ack.getBlockNumber();
                            if (lastBlock != block) {
                                this.discardPackets();
                                continue;
                            }
                            if (++block > 65535) {
                                block = 0;
                            }
                            if (lastAckWait) {
                                break Label_0576;
                            }
                            int offset;
                            for (dataLength = 512, offset = 4, totalThisPacket = 0; dataLength > 0 && (bytesRead = input.read(this._sendBuffer, offset, dataLength)) > 0; offset += bytesRead, dataLength -= bytesRead, totalThisPacket += bytesRead) {}
                            if (totalThisPacket < 512) {
                                lastAckWait = true;
                            }
                            data.setBlockNumber(block);
                            data.setData(this._sendBuffer, 4, totalThisPacket);
                            sent = data;
                            continue Label_0576;
                        }
                        default: {
                            this.endBufferedOps();
                            throw new IOException("Received unexpected packet type.");
                        }
                    }
                } while (lastBlock != ((block == 0) ? 65535 : (block - 1)));
            } while (totalThisPacket > 0 || lastAckWait);
            break;
        }
        this.endBufferedOps();
    }
    
    public void sendFile(final String filename, final int mode, final InputStream input, final String hostname, final int port) throws UnknownHostException, IOException {
        this.sendFile(filename, mode, input, InetAddress.getByName(hostname), port);
    }
    
    public void sendFile(final String filename, final int mode, final InputStream input, final InetAddress host) throws IOException {
        this.sendFile(filename, mode, input, host, 69);
    }
    
    public void sendFile(final String filename, final int mode, final InputStream input, final String hostname) throws UnknownHostException, IOException {
        this.sendFile(filename, mode, input, InetAddress.getByName(hostname), 69);
    }
}
