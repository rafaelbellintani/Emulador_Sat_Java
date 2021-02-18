// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class CommitmentTypeIndication extends ASN1Encodable
{
    private DERObjectIdentifier commitmentTypeId;
    private ASN1Sequence commitmentTypeQualifier;
    
    public CommitmentTypeIndication(final ASN1Sequence asn1Sequence) {
        this.commitmentTypeId = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() > 1) {
            this.commitmentTypeQualifier = (ASN1Sequence)asn1Sequence.getObjectAt(1);
        }
    }
    
    public CommitmentTypeIndication(final DERObjectIdentifier commitmentTypeId) {
        this.commitmentTypeId = commitmentTypeId;
    }
    
    public CommitmentTypeIndication(final DERObjectIdentifier commitmentTypeId, final ASN1Sequence commitmentTypeQualifier) {
        this.commitmentTypeId = commitmentTypeId;
        this.commitmentTypeQualifier = commitmentTypeQualifier;
    }
    
    public static CommitmentTypeIndication getInstance(final Object o) {
        if (o == null || o instanceof CommitmentTypeIndication) {
            return (CommitmentTypeIndication)o;
        }
        return new CommitmentTypeIndication(ASN1Sequence.getInstance(o));
    }
    
    public DERObjectIdentifier getCommitmentTypeId() {
        return this.commitmentTypeId;
    }
    
    public ASN1Sequence getCommitmentTypeQualifier() {
        return this.commitmentTypeQualifier;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.commitmentTypeId);
        if (this.commitmentTypeQualifier != null) {
            asn1EncodableVector.add(this.commitmentTypeQualifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
