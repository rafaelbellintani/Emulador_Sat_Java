// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.Digest;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TlsUtils
{
    static byte[] toByteArray(final String s) {
        final char[] charArray = s.toCharArray();
        final byte[] array = new byte[charArray.length];
        for (int i = 0; i != array.length; ++i) {
            array[i] = (byte)charArray[i];
        }
        return array;
    }
    
    protected static void writeUint8(final short n, final OutputStream outputStream) throws IOException {
        outputStream.write(n);
    }
    
    protected static void writeUint8(final short n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
    }
    
    protected static void writeUint16(final int n, final OutputStream outputStream) throws IOException {
        outputStream.write(n >> 8);
        outputStream.write(n);
    }
    
    protected static void writeUint16(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >> 8);
        array[n2 + 1] = (byte)n;
    }
    
    protected static void writeUint24(final int n, final OutputStream outputStream) throws IOException {
        outputStream.write(n >> 16);
        outputStream.write(n >> 8);
        outputStream.write(n);
    }
    
    protected static void writeUint24(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >> 16);
        array[n2 + 1] = (byte)(n >> 8);
        array[n2 + 2] = (byte)n;
    }
    
    protected static void writeUint32(final long n, final OutputStream outputStream) throws IOException {
        outputStream.write((int)(n >> 24));
        outputStream.write((int)(n >> 16));
        outputStream.write((int)(n >> 8));
        outputStream.write((int)n);
    }
    
    protected static void writeUint32(final long n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >> 24);
        array[n2 + 1] = (byte)(n >> 16);
        array[n2 + 2] = (byte)(n >> 8);
        array[n2 + 3] = (byte)n;
    }
    
    protected static void writeUint64(final long n, final OutputStream outputStream) throws IOException {
        outputStream.write((int)(n >> 56));
        outputStream.write((int)(n >> 48));
        outputStream.write((int)(n >> 40));
        outputStream.write((int)(n >> 32));
        outputStream.write((int)(n >> 24));
        outputStream.write((int)(n >> 16));
        outputStream.write((int)(n >> 8));
        outputStream.write((int)n);
    }
    
    protected static void writeUint64(final long n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >> 56);
        array[n2 + 1] = (byte)(n >> 48);
        array[n2 + 2] = (byte)(n >> 40);
        array[n2 + 3] = (byte)(n >> 32);
        array[n2 + 4] = (byte)(n >> 24);
        array[n2 + 5] = (byte)(n >> 16);
        array[n2 + 6] = (byte)(n >> 8);
        array[n2 + 7] = (byte)n;
    }
    
    protected static void writeOpaque8(final byte[] b, final OutputStream outputStream) throws IOException {
        writeUint8((short)b.length, outputStream);
        outputStream.write(b);
    }
    
    protected static void writeOpaque16(final byte[] b, final OutputStream outputStream) throws IOException {
        writeUint16(b.length, outputStream);
        outputStream.write(b);
    }
    
    protected static short readUint8(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        if (read == -1) {
            throw new EOFException();
        }
        return (short)read;
    }
    
    protected static int readUint16(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if ((read | read2) < 0) {
            throw new EOFException();
        }
        return read << 8 | read2;
    }
    
    protected static int readUint24(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        if ((read | read2 | read3) < 0) {
            throw new EOFException();
        }
        return read << 16 | read2 << 8 | read3;
    }
    
    protected static long readUint32(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        final int read4 = inputStream.read();
        if ((read | read2 | read3 | read4) < 0) {
            throw new EOFException();
        }
        return (long)read << 24 | (long)read2 << 16 | (long)read3 << 8 | (long)read4;
    }
    
    protected static void readFully(final byte[] b, final InputStream inputStream) throws IOException {
        int read;
        for (int i = 0; i != b.length; i += read) {
            read = inputStream.read(b, i, b.length - i);
            if (read == -1) {
                throw new EOFException();
            }
        }
    }
    
    protected static byte[] readOpaque8(final InputStream inputStream) throws IOException {
        final byte[] array = new byte[readUint8(inputStream)];
        readFully(array, inputStream);
        return array;
    }
    
    protected static byte[] readOpaque16(final InputStream inputStream) throws IOException {
        final byte[] array = new byte[readUint16(inputStream)];
        readFully(array, inputStream);
        return array;
    }
    
    protected static void checkVersion(final byte[] array, final TlsProtocolHandler tlsProtocolHandler) throws IOException {
        if (array[0] != 3 || array[1] != 1) {
            tlsProtocolHandler.failWithError((short)2, (short)70);
        }
    }
    
    protected static void checkVersion(final InputStream inputStream, final TlsProtocolHandler tlsProtocolHandler) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if (read != 3 || read2 != 1) {
            tlsProtocolHandler.failWithError((short)2, (short)70);
        }
    }
    
    protected static void writeVersion(final OutputStream outputStream) throws IOException {
        outputStream.write(3);
        outputStream.write(1);
    }
    
    private static void hmac_hash(final Digest digest, final byte[] array, final byte[] array2, final byte[] array3) {
        final HMac hMac = new HMac(digest);
        final KeyParameter keyParameter = new KeyParameter(array);
        byte[] array4 = array2;
        final int digestSize = digest.getDigestSize();
        final int n = (array3.length + digestSize - 1) / digestSize;
        final byte[] array5 = new byte[hMac.getMacSize()];
        final byte[] array6 = new byte[hMac.getMacSize()];
        for (int i = 0; i < n; ++i) {
            hMac.init(keyParameter);
            hMac.update(array4, 0, array4.length);
            hMac.doFinal(array5, 0);
            array4 = array5;
            hMac.init(keyParameter);
            hMac.update(array4, 0, array4.length);
            hMac.update(array2, 0, array2.length);
            hMac.doFinal(array6, 0);
            System.arraycopy(array6, 0, array3, digestSize * i, Math.min(digestSize, array3.length - digestSize * i));
        }
    }
    
    protected static void PRF(final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4) {
        final int n = (array.length + 1) / 2;
        final byte[] array5 = new byte[n];
        final byte[] array6 = new byte[n];
        System.arraycopy(array, 0, array5, 0, n);
        System.arraycopy(array, array.length - n, array6, 0, n);
        final byte[] array7 = new byte[array2.length + array3.length];
        System.arraycopy(array2, 0, array7, 0, array2.length);
        System.arraycopy(array3, 0, array7, array2.length, array3.length);
        final byte[] array8 = new byte[array4.length];
        hmac_hash(new MD5Digest(), array5, array7, array8);
        hmac_hash(new SHA1Digest(), array6, array7, array4);
        for (int i = 0; i < array4.length; ++i) {
            final int n2 = i;
            array4[n2] ^= array8[i];
        }
    }
}
