// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class TimeStampReq extends ASN1Encodable
{
    DERInteger version;
    MessageImprint messageImprint;
    DERObjectIdentifier tsaPolicy;
    DERInteger nonce;
    DERBoolean certReq;
    X509Extensions extensions;
    
    public static TimeStampReq getInstance(final Object o) {
        if (o == null || o instanceof TimeStampReq) {
            return (TimeStampReq)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TimeStampReq((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Unknown object in 'TimeStampReq' factory : " + o.getClass().getName() + ".");
    }
    
    public TimeStampReq(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        int n = 0;
        this.version = DERInteger.getInstance(asn1Sequence.getObjectAt(n));
        ++n;
        this.messageImprint = MessageImprint.getInstance(asn1Sequence.getObjectAt(n));
        for (int i = ++n; i < size; ++i) {
            if (asn1Sequence.getObjectAt(i) instanceof DERObjectIdentifier) {
                this.tsaPolicy = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof DERInteger) {
                this.nonce = DERInteger.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof DERBoolean) {
                this.certReq = DERBoolean.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
                if (asn1TaggedObject.getTagNo() == 0) {
                    this.extensions = X509Extensions.getInstance(asn1TaggedObject, false);
                }
            }
        }
    }
    
    public TimeStampReq(final MessageImprint messageImprint, final DERObjectIdentifier tsaPolicy, final DERInteger nonce, final DERBoolean certReq, final X509Extensions extensions) {
        this.version = new DERInteger(1);
        this.messageImprint = messageImprint;
        this.tsaPolicy = tsaPolicy;
        this.nonce = nonce;
        this.certReq = certReq;
        this.extensions = extensions;
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }
    
    public DERObjectIdentifier getReqPolicy() {
        return this.tsaPolicy;
    }
    
    public DERInteger getNonce() {
        return this.nonce;
    }
    
    public DERBoolean getCertReq() {
        return this.certReq;
    }
    
    public X509Extensions getExtensions() {
        return this.extensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.messageImprint);
        if (this.tsaPolicy != null) {
            asn1EncodableVector.add(this.tsaPolicy);
        }
        if (this.nonce != null) {
            asn1EncodableVector.add(this.nonce);
        }
        if (this.certReq != null && this.certReq.isTrue()) {
            asn1EncodableVector.add(this.certReq);
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.extensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
