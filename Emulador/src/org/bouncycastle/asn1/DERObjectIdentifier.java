// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class DERObjectIdentifier extends ASN1Object
{
    String identifier;
    
    public static DERObjectIdentifier getInstance(final Object o) {
        if (o == null || o instanceof DERObjectIdentifier) {
            return (DERObjectIdentifier)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERObjectIdentifier(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERObjectIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    DERObjectIdentifier(final byte[] array) {
        final StringBuffer sb = new StringBuffer();
        long n = 0L;
        BigInteger obj = null;
        int n2 = 1;
        for (int i = 0; i != array.length; ++i) {
            final int n3 = array[i] & 0xFF;
            if (n < 36028797018963968L) {
                n = n * 128L + (n3 & 0x7F);
                if ((n3 & 0x80) == 0x0) {
                    if (n2 != 0) {
                        switch ((int)n / 40) {
                            case 0: {
                                sb.append('0');
                                break;
                            }
                            case 1: {
                                sb.append('1');
                                n -= 40L;
                                break;
                            }
                            default: {
                                sb.append('2');
                                n -= 80L;
                                break;
                            }
                        }
                        n2 = 0;
                    }
                    sb.append('.');
                    sb.append(n);
                    n = 0L;
                }
            }
            else {
                if (obj == null) {
                    obj = BigInteger.valueOf(n);
                }
                obj = obj.shiftLeft(7).or(BigInteger.valueOf(n3 & 0x7F));
                if ((n3 & 0x80) == 0x0) {
                    sb.append('.');
                    sb.append(obj);
                    obj = null;
                    n = 0L;
                }
            }
        }
        this.identifier = sb.toString();
    }
    
    public DERObjectIdentifier(final String s) {
        if (!isValidIdentifier(s)) {
            throw new IllegalArgumentException("string " + s + " not an OID");
        }
        this.identifier = s;
    }
    
    public String getId() {
        return this.identifier;
    }
    
    private void writeField(final OutputStream outputStream, final long n) throws IOException {
        if (n >= 128L) {
            if (n >= 16384L) {
                if (n >= 2097152L) {
                    if (n >= 268435456L) {
                        if (n >= 34359738368L) {
                            if (n >= 4398046511104L) {
                                if (n >= 562949953421312L) {
                                    if (n >= 72057594037927936L) {
                                        outputStream.write((int)(n >> 56) | 0x80);
                                    }
                                    outputStream.write((int)(n >> 49) | 0x80);
                                }
                                outputStream.write((int)(n >> 42) | 0x80);
                            }
                            outputStream.write((int)(n >> 35) | 0x80);
                        }
                        outputStream.write((int)(n >> 28) | 0x80);
                    }
                    outputStream.write((int)(n >> 21) | 0x80);
                }
                outputStream.write((int)(n >> 14) | 0x80);
            }
            outputStream.write((int)(n >> 7) | 0x80);
        }
        outputStream.write((int)n & 0x7F);
    }
    
    private void writeField(final OutputStream outputStream, final BigInteger bigInteger) throws IOException {
        final int n = (bigInteger.bitLength() + 6) / 7;
        if (n == 0) {
            outputStream.write(0);
        }
        else {
            BigInteger shiftRight = bigInteger;
            final byte[] b = new byte[n];
            for (int i = n - 1; i >= 0; --i) {
                b[i] = (byte)((shiftRight.intValue() & 0x7F) | 0x80);
                shiftRight = shiftRight.shiftRight(7);
            }
            final byte[] array = b;
            final int n2 = n - 1;
            array[n2] &= 0x7F;
            outputStream.write(b);
        }
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        final OIDTokenizer oidTokenizer = new OIDTokenizer(this.identifier);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DEROutputStream derOutputStream2 = new DEROutputStream(byteArrayOutputStream);
        this.writeField(byteArrayOutputStream, Integer.parseInt(oidTokenizer.nextToken()) * 40 + Integer.parseInt(oidTokenizer.nextToken()));
        while (oidTokenizer.hasMoreTokens()) {
            final String nextToken = oidTokenizer.nextToken();
            if (nextToken.length() < 18) {
                this.writeField(byteArrayOutputStream, Long.parseLong(nextToken));
            }
            else {
                this.writeField(byteArrayOutputStream, new BigInteger(nextToken));
            }
        }
        derOutputStream2.close();
        derOutputStream.writeEncoded(6, byteArrayOutputStream.toByteArray());
    }
    
    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERObjectIdentifier && this.identifier.equals(((DERObjectIdentifier)derObject).identifier);
    }
    
    @Override
    public String toString() {
        return this.getId();
    }
    
    private static boolean isValidIdentifier(final String s) {
        if (s.length() < 3 || s.charAt(1) != '.') {
            return false;
        }
        final char char1 = s.charAt(0);
        if (char1 < '0' || char1 > '2') {
            return false;
        }
        boolean b = false;
        for (int i = s.length() - 1; i >= 2; --i) {
            final char char2 = s.charAt(i);
            if ('0' <= char2 && char2 <= '9') {
                b = true;
            }
            else {
                if (char2 != '.') {
                    return false;
                }
                if (!b) {
                    return false;
                }
                b = false;
            }
        }
        return b;
    }
}
