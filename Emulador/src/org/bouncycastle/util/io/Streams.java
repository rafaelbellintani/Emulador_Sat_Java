// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.io;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Streams
{
    private static int BUFFER_SIZE;
    
    public static void drain(final InputStream inputStream) throws IOException {
        final byte[] b = new byte[Streams.BUFFER_SIZE];
        while (inputStream.read(b, 0, b.length) >= 0) {}
    }
    
    public static byte[] readAll(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAll(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] readAllLimited(final InputStream inputStream, final int n) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAllLimited(inputStream, n, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int readFully(final InputStream inputStream, final byte[] array) throws IOException {
        return readFully(inputStream, array, 0, array.length);
    }
    
    public static int readFully(final InputStream inputStream, final byte[] b, final int n, final int n2) throws IOException {
        int i;
        int read;
        for (i = 0; i < n2; i += read) {
            read = inputStream.read(b, n + i, n2 - i);
            if (read < 0) {
                break;
            }
        }
        return i;
    }
    
    public static void pipeAll(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[Streams.BUFFER_SIZE];
        int read;
        while ((read = inputStream.read(array, 0, array.length)) >= 0) {
            outputStream.write(array, 0, read);
        }
    }
    
    public static long pipeAllLimited(final InputStream inputStream, final long n, final OutputStream outputStream) throws IOException {
        long n2 = 0L;
        final byte[] array = new byte[Streams.BUFFER_SIZE];
        int read;
        while ((read = inputStream.read(array, 0, array.length)) >= 0) {
            n2 += read;
            if (n2 > n) {
                throw new StreamOverflowException("Data Overflow");
            }
            outputStream.write(array, 0, read);
        }
        return n2;
    }
    
    static {
        Streams.BUFFER_SIZE = 512;
    }
}
