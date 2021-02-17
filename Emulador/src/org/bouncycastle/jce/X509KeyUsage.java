// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Encodable;

public class X509KeyUsage extends ASN1Encodable
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
    private int usage;
    
    public X509KeyUsage(final int usage) {
        this.usage = 0;
        this.usage = usage;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new KeyUsage(this.usage);
    }
}
