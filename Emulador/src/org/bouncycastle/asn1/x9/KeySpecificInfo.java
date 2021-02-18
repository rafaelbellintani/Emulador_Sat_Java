// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class KeySpecificInfo extends ASN1Encodable
{
    private DERObjectIdentifier algorithm;
    private ASN1OctetString counter;
    
    public KeySpecificInfo(final DERObjectIdentifier algorithm, final ASN1OctetString counter) {
        this.algorithm = algorithm;
        this.counter = counter;
    }
    
    public KeySpecificInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.algorithm = objects.nextElement();
        this.counter = (ASN1OctetString)objects.nextElement();
    }
    
    public DERObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }
    
    public ASN1OctetString getCounter() {
        return this.counter;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algorithm);
        asn1EncodableVector.add(this.counter);
        return new DERSequence(asn1EncodableVector);
    }
}
