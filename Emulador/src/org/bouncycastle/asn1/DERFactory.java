// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

class DERFactory
{
    static final DERSequence EMPTY_SEQUENCE;
    static final DERSet EMPTY_SET;
    
    static DERSequence createSequence(final ASN1EncodableVector asn1EncodableVector) {
        return (asn1EncodableVector.size() < 1) ? DERFactory.EMPTY_SEQUENCE : new DERSequence(asn1EncodableVector);
    }
    
    static DERSet createSet(final ASN1EncodableVector asn1EncodableVector) {
        return (asn1EncodableVector.size() < 1) ? DERFactory.EMPTY_SET : new DERSet(asn1EncodableVector);
    }
    
    static DERSet createSet(final ASN1EncodableVector asn1EncodableVector, final boolean b) {
        return (asn1EncodableVector.size() < 1) ? DERFactory.EMPTY_SET : new DERSet(asn1EncodableVector, b);
    }
    
    static {
        EMPTY_SEQUENCE = new DERSequence();
        EMPTY_SET = new DERSet();
    }
}
