// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class RecipientKeyIdentifier extends ASN1Encodable
{
    private ASN1OctetString subjectKeyIdentifier;
    private DERGeneralizedTime date;
    private OtherKeyAttribute other;
    
    public RecipientKeyIdentifier(final ASN1OctetString subjectKeyIdentifier, final DERGeneralizedTime date, final OtherKeyAttribute other) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.date = date;
        this.other = other;
    }
    
    public RecipientKeyIdentifier(final ASN1Sequence asn1Sequence) {
        this.subjectKeyIdentifier = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        switch (asn1Sequence.size()) {
            case 1: {
                break;
            }
            case 2: {
                if (asn1Sequence.getObjectAt(1) instanceof DERGeneralizedTime) {
                    this.date = (DERGeneralizedTime)asn1Sequence.getObjectAt(1);
                    break;
                }
                this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(2));
                break;
            }
            case 3: {
                this.date = (DERGeneralizedTime)asn1Sequence.getObjectAt(1);
                this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(2));
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid RecipientKeyIdentifier");
            }
        }
    }
    
    public static RecipientKeyIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static RecipientKeyIdentifier getInstance(final Object o) {
        if (o == null || o instanceof RecipientKeyIdentifier) {
            return (RecipientKeyIdentifier)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RecipientKeyIdentifier((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid RecipientKeyIdentifier: " + o.getClass().getName());
    }
    
    public ASN1OctetString getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }
    
    public DERGeneralizedTime getDate() {
        return this.date;
    }
    
    public OtherKeyAttribute getOtherKeyAttribute() {
        return this.other;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.subjectKeyIdentifier);
        if (this.date != null) {
            asn1EncodableVector.add(this.date);
        }
        if (this.other != null) {
            asn1EncodableVector.add(this.other);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
