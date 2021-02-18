// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.io;

import java.io.Writer;
import java.io.Reader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class Util
{
    public static final int DEFAULT_COPY_BUFFER_SIZE = 1024;
    
    private Util() {
    }
    
    public static final long copyStream(final InputStream source, final OutputStream dest, final int bufferSize, final long streamSize, final CopyStreamListener listener, final boolean flush) throws CopyStreamException {
        final byte[] buffer = new byte[bufferSize];
        long total = 0L;
        try {
            int bytes;
            while ((bytes = source.read(buffer)) != -1) {
                if (bytes == 0) {
                    bytes = source.read();
                    if (bytes < 0) {
                        break;
                    }
                    dest.write(bytes);
                    if (flush) {
                        dest.flush();
                    }
                    ++total;
                    if (listener == null) {
                        continue;
                    }
                    listener.bytesTransferred(total, 1, streamSize);
                }
                else {
                    dest.write(buffer, 0, bytes);
                    if (flush) {
                        dest.flush();
                    }
                    total += bytes;
                    if (listener == null) {
                        continue;
                    }
                    listener.bytesTransferred(total, bytes, streamSize);
                }
            }
        }
        catch (IOException e) {
            throw new CopyStreamException("IOException caught while copying.", total, e);
        }
        return total;
    }
    
    public static final long copyStream(final InputStream source, final OutputStream dest, final int bufferSize, final long streamSize, final CopyStreamListener listener) throws CopyStreamException {
        return copyStream(source, dest, bufferSize, streamSize, listener, true);
    }
    
    public static final long copyStream(final InputStream source, final OutputStream dest, final int bufferSize) throws CopyStreamException {
        return copyStream(source, dest, bufferSize, -1L, null);
    }
    
    public static final long copyStream(final InputStream source, final OutputStream dest) throws CopyStreamException {
        return copyStream(source, dest, 1024);
    }
    
    public static final long copyReader(final Reader source, final Writer dest, final int bufferSize, final long streamSize, final CopyStreamListener listener) throws CopyStreamException {
        final char[] buffer = new char[bufferSize];
        long total = 0L;
        try {
            int chars;
            while ((chars = source.read(buffer)) != -1) {
                if (chars == 0) {
                    chars = source.read();
                    if (chars < 0) {
                        break;
                    }
                    dest.write(chars);
                    dest.flush();
                    ++total;
                    if (listener == null) {
                        continue;
                    }
                    listener.bytesTransferred(total, chars, streamSize);
                }
                else {
                    dest.write(buffer, 0, chars);
                    dest.flush();
                    total += chars;
                    if (listener == null) {
                        continue;
                    }
                    listener.bytesTransferred(total, chars, streamSize);
                }
            }
        }
        catch (IOException e) {
            throw new CopyStreamException("IOException caught while copying.", total, e);
        }
        return total;
    }
    
    public static final long copyReader(final Reader source, final Writer dest, final int bufferSize) throws CopyStreamException {
        return copyReader(source, dest, bufferSize, -1L, null);
    }
    
    public static final long copyReader(final Reader source, final Writer dest) throws CopyStreamException {
        return copyReader(source, dest, 1024);
    }
}
