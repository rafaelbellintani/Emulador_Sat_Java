// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.DigestInfo;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class MacData extends ASN1Encodable
{
    private static final BigInteger ONE;
    DigestInfo digInfo;
    byte[] salt;
    BigInteger iterationCount;
    
    public static MacData getInstance(final Object o) {
        if (o instanceof MacData) {
            return (MacData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MacData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public MacData(final ASN1Sequence asn1Sequence) {
        this.digInfo = DigestInfo.getInstance(asn1Sequence.getObjectAt(0));
        this.salt = ((ASN1OctetString)asn1Sequence.getObjectAt(1)).getOctets();
        if (asn1Sequence.size() == 3) {
            this.iterationCount = ((DERInteger)asn1Sequence.getObjectAt(2)).getValue();
        }
        else {
            this.iterationCount = MacData.ONE;
        }
    }
    
    public MacData(final DigestInfo digInfo, final byte[] salt, final int n) {
        this.digInfo = digInfo;
        this.salt = salt;
        this.iterationCount = BigInteger.valueOf(n);
    }
    
    public DigestInfo getMac() {
        return this.digInfo;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }
    
    public BigInteger getIterationCount() {
        return this.iterationCount;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.digInfo);
        asn1EncodableVector.add(new DEROctetString(this.salt));
        if (!this.iterationCount.equals(MacData.ONE)) {
            asn1EncodableVector.add(new DERInteger(this.iterationCount));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
}
