// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class Hex
{
    private static final Encoder encoder;
    
    public static byte[] encode(final byte[] array) {
        return encode(array, 0, array.length);
    }
    
    public static byte[] encode(final byte[] array, final int n, final int n2) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.encode(array, n, n2, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception encoding Hex string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int encode(final byte[] array, final OutputStream outputStream) throws IOException {
        return Hex.encoder.encode(array, 0, array.length, outputStream);
    }
    
    public static int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        return Hex.encoder.encode(array, n, n2, outputStream);
    }
    
    public static byte[] decode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.decode(array, 0, array.length, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception decoding Hex string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] decode(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.decode(s, byteArrayOutputStream);
        }
        catch (IOException obj) {
            throw new RuntimeException("exception decoding Hex string: " + obj);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int decode(final String s, final OutputStream outputStream) throws IOException {
        return Hex.encoder.decode(s, outputStream);
    }
    
    static {
        encoder = new HexEncoder();
    }
}
