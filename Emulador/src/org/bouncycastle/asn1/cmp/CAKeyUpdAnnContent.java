// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CAKeyUpdAnnContent extends ASN1Encodable
{
    private CMPCertificate oldWithNew;
    private CMPCertificate newWithOld;
    private CMPCertificate newWithNew;
    
    private CAKeyUpdAnnContent(final ASN1Sequence asn1Sequence) {
        this.oldWithNew = CMPCertificate.getInstance(asn1Sequence.getObjectAt(0));
        this.newWithOld = CMPCertificate.getInstance(asn1Sequence.getObjectAt(1));
        this.newWithNew = CMPCertificate.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public static CAKeyUpdAnnContent getInstance(final Object o) {
        if (o instanceof CAKeyUpdAnnContent) {
            return (CAKeyUpdAnnContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CAKeyUpdAnnContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CMPCertificate getOldWithNew() {
        return this.oldWithNew;
    }
    
    public CMPCertificate getNewWithOld() {
        return this.newWithOld;
    }
    
    public CMPCertificate getNewWithNew() {
        return this.newWithNew;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.oldWithNew);
        asn1EncodableVector.add(this.newWithOld);
        asn1EncodableVector.add(this.newWithNew);
        return new DERSequence(asn1EncodableVector);
    }
}
