// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.telnet;

import java.io.InterruptedIOException;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;

final class TelnetInputStream extends BufferedInputStream implements Runnable
{
    static final int _STATE_DATA = 0;
    static final int _STATE_IAC = 1;
    static final int _STATE_WILL = 2;
    static final int _STATE_WONT = 3;
    static final int _STATE_DO = 4;
    static final int _STATE_DONT = 5;
    static final int _STATE_SB = 6;
    static final int _STATE_SE = 7;
    static final int _STATE_CR = 8;
    static final int _STATE_IAC_SB = 9;
    private boolean __hasReachedEOF;
    private boolean __isClosed;
    private boolean __readIsWaiting;
    private int __receiveState;
    private int __queueHead;
    private int __queueTail;
    private int __bytesAvailable;
    private int[] __queue;
    private TelnetClient __client;
    private Thread __thread;
    private IOException __ioException;
    private int[] __suboption;
    private int __suboption_count;
    private boolean __threaded;
    
    TelnetInputStream(final InputStream input, final TelnetClient client, final boolean readerThread) {
        super(input);
        this.__suboption = new int[256];
        this.__suboption_count = 0;
        this.__client = client;
        this.__receiveState = 0;
        this.__isClosed = true;
        this.__hasReachedEOF = false;
        this.__queue = new int[2049];
        this.__queueHead = 0;
        this.__queueTail = 0;
        this.__bytesAvailable = 0;
        this.__ioException = null;
        this.__readIsWaiting = false;
        this.__threaded = false;
        if (readerThread) {
            this.__thread = new Thread(this);
        }
        else {
            this.__thread = null;
        }
    }
    
    TelnetInputStream(final InputStream input, final TelnetClient client) {
        this(input, client, true);
    }
    
    void _start() {
        if (this.__thread == null) {
            return;
        }
        this.__isClosed = false;
        int priority = Thread.currentThread().getPriority() + 1;
        if (priority > 10) {
            priority = 10;
        }
        this.__thread.setPriority(priority);
        this.__thread.setDaemon(true);
        this.__thread.start();
        this.__threaded = true;
    }
    
    private int __read(final boolean mayBlock) throws IOException {
        while (mayBlock || super.available() != 0) {
            int ch;
            if ((ch = super.read()) < 0) {
                return -1;
            }
            ch &= 0xFF;
            synchronized (this.__client) {
                this.__client._processAYTResponse();
            }
            this.__client._spyRead(ch);
            switch (this.__receiveState) {
                case 8: {
                    if (ch == 0) {
                        continue;
                    }
                }
                case 0: {
                    if (ch == 255) {
                        this.__receiveState = 1;
                        continue;
                    }
                    if (ch == 13) {
                        synchronized (this.__client) {
                            if (this.__client._requestedDont(0)) {
                                this.__receiveState = 8;
                            }
                            else {
                                this.__receiveState = 0;
                            }
                        }
                        break;
                    }
                    this.__receiveState = 0;
                    break;
                }
                case 1: {
                    switch (ch) {
                        case 251: {
                            this.__receiveState = 2;
                            continue;
                        }
                        case 252: {
                            this.__receiveState = 3;
                            continue;
                        }
                        case 253: {
                            this.__receiveState = 4;
                            continue;
                        }
                        case 254: {
                            this.__receiveState = 5;
                            continue;
                        }
                        case 250: {
                            this.__suboption_count = 0;
                            this.__receiveState = 6;
                            continue;
                        }
                        case 255: {
                            this.__receiveState = 0;
                            break;
                        }
                    }
                    this.__receiveState = 0;
                    continue;
                }
                case 2: {
                    synchronized (this.__client) {
                        this.__client._processWill(ch);
                        this.__client._flushOutputStream();
                    }
                    this.__receiveState = 0;
                    continue;
                }
                case 3: {
                    synchronized (this.__client) {
                        this.__client._processWont(ch);
                        this.__client._flushOutputStream();
                    }
                    this.__receiveState = 0;
                    continue;
                }
                case 4: {
                    synchronized (this.__client) {
                        this.__client._processDo(ch);
                        this.__client._flushOutputStream();
                    }
                    this.__receiveState = 0;
                    continue;
                }
                case 5: {
                    synchronized (this.__client) {
                        this.__client._processDont(ch);
                        this.__client._flushOutputStream();
                    }
                    this.__receiveState = 0;
                    continue;
                }
                case 6: {
                    switch (ch) {
                        case 255: {
                            this.__receiveState = 9;
                            continue;
                        }
                        default: {
                            this.__suboption[this.__suboption_count++] = ch;
                            this.__receiveState = 6;
                            continue;
                        }
                    }
                    break;
                }
                case 9: {
                    switch (ch) {
                        case 240: {
                            synchronized (this.__client) {
                                this.__client._processSuboption(this.__suboption, this.__suboption_count);
                                this.__client._flushOutputStream();
                            }
                            this.__receiveState = 0;
                            continue;
                        }
                        default: {
                            this.__receiveState = 6;
                            this.__receiveState = 0;
                            continue;
                        }
                    }
                    break;
                }
            }
            return ch;
        }
        return -2;
    }
    
    private void __processChar(final int ch) throws InterruptedException {
        synchronized (this.__queue) {
            while (this.__bytesAvailable >= this.__queue.length - 1) {
                if (this.__threaded) {
                    this.__queue.notify();
                    try {
                        this.__queue.wait();
                        continue;
                    }
                    catch (InterruptedException e) {
                        throw e;
                    }
                }
                throw new IllegalStateException("Queue is full! Cannot process another character.");
            }
            if (this.__readIsWaiting && this.__threaded) {
                this.__queue.notify();
            }
            this.__queue[this.__queueTail] = ch;
            ++this.__bytesAvailable;
            if (++this.__queueTail >= this.__queue.length) {
                this.__queueTail = 0;
            }
        }
    }
    
    @Override
    public int read() throws IOException {
        synchronized (this.__queue) {
            while (this.__ioException == null) {
                if (this.__bytesAvailable != 0) {
                    final int ch = this.__queue[this.__queueHead];
                    if (++this.__queueHead >= this.__queue.length) {
                        this.__queueHead = 0;
                    }
                    --this.__bytesAvailable;
                    if (this.__bytesAvailable == 0 && this.__threaded) {
                        this.__queue.notify();
                    }
                    return ch;
                }
                if (this.__hasReachedEOF) {
                    return -1;
                }
                if (this.__threaded) {
                    this.__queue.notify();
                    try {
                        this.__readIsWaiting = true;
                        this.__queue.wait();
                        this.__readIsWaiting = false;
                        continue;
                    }
                    catch (InterruptedException e3) {
                        throw new InterruptedIOException("Fatal thread interruption during read.");
                    }
                }
                this.__readIsWaiting = true;
                boolean mayBlock = true;
                do {
                    int ch;
                    try {
                        if ((ch = this.__read(mayBlock)) < 0 && ch != -2) {
                            return ch;
                        }
                    }
                    catch (InterruptedIOException e) {
                        synchronized (this.__queue) {
                            this.__ioException = e;
                            this.__queue.notifyAll();
                            try {
                                this.__queue.wait(100L);
                            }
                            catch (InterruptedException ex) {}
                        }
                        return -1;
                    }
                    try {
                        if (ch != -2) {
                            this.__processChar(ch);
                        }
                    }
                    catch (InterruptedException e4) {
                        if (this.__isClosed) {
                            return -1;
                        }
                    }
                    mayBlock = false;
                } while (super.available() > 0 && this.__bytesAvailable < this.__queue.length - 1);
                this.__readIsWaiting = false;
            }
            final IOException e2 = this.__ioException;
            this.__ioException = null;
            throw e2;
        }
    }
    
    @Override
    public int read(final byte[] buffer) throws IOException {
        return this.read(buffer, 0, buffer.length);
    }
    
    @Override
    public int read(final byte[] buffer, int offset, int length) throws IOException {
        if (length < 1) {
            return 0;
        }
        synchronized (this.__queue) {
            if (length > this.__bytesAvailable) {
                length = this.__bytesAvailable;
            }
        }
        int ch;
        if ((ch = this.read()) == -1) {
            return -1;
        }
        final int off = offset;
        do {
            buffer[offset++] = (byte)ch;
        } while (--length > 0 && (ch = this.read()) != -1);
        return offset - off;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int available() throws IOException {
        synchronized (this.__queue) {
            return this.__bytesAvailable;
        }
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        synchronized (this.__queue) {
            this.__hasReachedEOF = true;
            this.__isClosed = true;
            if (this.__thread != null && this.__thread.isAlive()) {
                this.__thread.interrupt();
            }
            this.__queue.notifyAll();
        }
        this.__threaded = false;
    }
    
    public void run() {
        try {
            while (!this.__isClosed) {
                int ch;
                try {
                    if ((ch = this.__read(true)) < 0) {
                        break;
                    }
                }
                catch (InterruptedIOException e) {
                    synchronized (this.__queue) {
                        this.__ioException = e;
                        this.__queue.notifyAll();
                        try {
                            this.__queue.wait(100L);
                        }
                        catch (InterruptedException interrupted) {
                            if (this.__isClosed) {
                                break;
                            }
                        }
                    }
                }
                catch (RuntimeException re) {
                    super.close();
                    break;
                }
                try {
                    this.__processChar(ch);
                }
                catch (InterruptedException e2) {
                    if (this.__isClosed) {
                        break;
                    }
                    continue;
                }
            }
        }
        catch (IOException ioe) {
            synchronized (this.__queue) {
                this.__ioException = ioe;
            }
        }
        synchronized (this.__queue) {
            this.__isClosed = true;
            this.__hasReachedEOF = true;
            this.__queue.notify();
        }
        this.__threaded = false;
    }
}
