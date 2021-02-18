// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cryptopro;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class GOST28147Parameters extends ASN1Encodable
{
    ASN1OctetString iv;
    DERObjectIdentifier paramSet;
    
    public static GOST28147Parameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static GOST28147Parameters getInstance(final Object o) {
        if (o == null || o instanceof GOST28147Parameters) {
            return (GOST28147Parameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GOST28147Parameters((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid GOST3410Parameter: " + o.getClass().getName());
    }
    
    public GOST28147Parameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.iv = objects.nextElement();
        this.paramSet = (DERObjectIdentifier)objects.nextElement();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.iv);
        asn1EncodableVector.add(this.paramSet);
        return new DERSequence(asn1EncodableVector);
    }
}
