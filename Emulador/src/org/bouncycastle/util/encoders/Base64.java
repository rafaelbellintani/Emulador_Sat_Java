// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class Base64
{
    private static final Encoder encoder;
    
    public static byte[] encode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((array.length + 2) / 3 * 4);
        try {
            Base64.encoder.encode(array, 0, array.length, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception encoding base64 string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int encode(final byte[] array, final OutputStream outputStream) throws IOException {
        return Base64.encoder.encode(array, 0, array.length, outputStream);
    }
    
    public static int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        return Base64.encoder.encode(array, n, n2, outputStream);
    }
    
    public static byte[] decode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(array.length / 4 * 3);
        try {
            Base64.encoder.decode(array, 0, array.length, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception decoding base64 string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] decode(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.length() / 4 * 3);
        try {
            Base64.encoder.decode(s, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception decoding base64 string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int decode(final String s, final OutputStream outputStream) throws IOException {
        return Base64.encoder.decode(s, outputStream);
    }
    
    static {
        encoder = new Base64Encoder();
    }
}
