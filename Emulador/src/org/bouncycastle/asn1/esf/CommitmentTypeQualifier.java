// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class CommitmentTypeQualifier extends ASN1Encodable
{
    private DERObjectIdentifier commitmentTypeIdentifier;
    private DEREncodable qualifier;
    
    public CommitmentTypeQualifier(final DERObjectIdentifier derObjectIdentifier) {
        this(derObjectIdentifier, null);
    }
    
    public CommitmentTypeQualifier(final DERObjectIdentifier commitmentTypeIdentifier, final DEREncodable qualifier) {
        this.commitmentTypeIdentifier = commitmentTypeIdentifier;
        this.qualifier = qualifier;
    }
    
    public CommitmentTypeQualifier(final ASN1Sequence asn1Sequence) {
        this.commitmentTypeIdentifier = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() > 1) {
            this.qualifier = asn1Sequence.getObjectAt(1);
        }
    }
    
    public static CommitmentTypeQualifier getInstance(final Object o) {
        if (o instanceof CommitmentTypeQualifier || o == null) {
            return (CommitmentTypeQualifier)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CommitmentTypeQualifier((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in getInstance.");
    }
    
    public DERObjectIdentifier getCommitmentTypeIdentifier() {
        return this.commitmentTypeIdentifier;
    }
    
    public DEREncodable getQualifier() {
        return this.qualifier;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.commitmentTypeIdentifier);
        if (this.qualifier != null) {
            asn1EncodableVector.add(this.qualifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
