// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class DataGroupHash extends ASN1Encodable
{
    DERInteger dataGroupNumber;
    ASN1OctetString dataGroupHashValue;
    
    public static DataGroupHash getInstance(final Object o) {
        if (o == null || o instanceof DataGroupHash) {
            return (DataGroupHash)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DataGroupHash(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + o.getClass().getName());
    }
    
    public DataGroupHash(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.dataGroupNumber = DERInteger.getInstance(objects.nextElement());
        this.dataGroupHashValue = ASN1OctetString.getInstance(objects.nextElement());
    }
    
    public DataGroupHash(final int n, final ASN1OctetString dataGroupHashValue) {
        this.dataGroupNumber = new DERInteger(n);
        this.dataGroupHashValue = dataGroupHashValue;
    }
    
    public int getDataGroupNumber() {
        return this.dataGroupNumber.getValue().intValue();
    }
    
    public ASN1OctetString getDataGroupHashValue() {
        return this.dataGroupHashValue;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.dataGroupNumber);
        asn1EncodableVector.add(this.dataGroupHashValue);
        return new DERSequence(asn1EncodableVector);
    }
}
