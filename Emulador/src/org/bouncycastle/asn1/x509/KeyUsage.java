// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERBitString;

public class KeyUsage extends DERBitString
{
    public static final int digitalSignature = 128;
    public static final int nonRepudiation = 64;
    public static final int keyEncipherment = 32;
    public static final int dataEncipherment = 16;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int cRLSign = 2;
    public static final int encipherOnly = 1;
    public static final int decipherOnly = 32768;
    
    public static DERBitString getInstance(final Object o) {
        if (o instanceof KeyUsage) {
            return (KeyUsage)o;
        }
        if (o instanceof X509Extension) {
            return new KeyUsage(DERBitString.getInstance(X509Extension.convertValueToObject((X509Extension)o)));
        }
        return new KeyUsage(DERBitString.getInstance(o));
    }
    
    public KeyUsage(final int n) {
        super(DERBitString.getBytes(n), DERBitString.getPadBits(n));
    }
    
    public KeyUsage(final DERBitString derBitString) {
        super(derBitString.getBytes(), derBitString.getPadBits());
    }
    
    @Override
    public String toString() {
        if (this.data.length == 1) {
            return "KeyUsage: 0x" + Integer.toHexString(this.data[0] & 0xFF);
        }
        return "KeyUsage: 0x" + Integer.toHexString((this.data[1] & 0xFF) << 8 | (this.data[0] & 0xFF));
    }
}
