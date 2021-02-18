// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class Holder extends ASN1Encodable
{
    IssuerSerial baseCertificateID;
    GeneralNames entityName;
    ObjectDigestInfo objectDigestInfo;
    private int version;
    
    public static Holder getInstance(final Object o) {
        if (o instanceof Holder) {
            return (Holder)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Holder((ASN1Sequence)o);
        }
        if (o instanceof ASN1TaggedObject) {
            return new Holder((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public Holder(final ASN1TaggedObject asn1TaggedObject) {
        this.version = 1;
        switch (asn1TaggedObject.getTagNo()) {
            case 0: {
                this.baseCertificateID = IssuerSerial.getInstance(asn1TaggedObject, false);
                break;
            }
            case 1: {
                this.entityName = GeneralNames.getInstance(asn1TaggedObject, false);
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown tag in Holder");
            }
        }
        this.version = 0;
    }
    
    public Holder(final ASN1Sequence asn1Sequence) {
        this.version = 1;
        if (asn1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
            switch (instance.getTagNo()) {
                case 0: {
                    this.baseCertificateID = IssuerSerial.getInstance(instance, false);
                    break;
                }
                case 1: {
                    this.entityName = GeneralNames.getInstance(instance, false);
                    break;
                }
                case 2: {
                    this.objectDigestInfo = ObjectDigestInfo.getInstance(instance, false);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag in Holder");
                }
            }
        }
        this.version = 1;
    }
    
    public Holder(final IssuerSerial baseCertificateID) {
        this.version = 1;
        this.baseCertificateID = baseCertificateID;
    }
    
    public Holder(final IssuerSerial baseCertificateID, final int version) {
        this.version = 1;
        this.baseCertificateID = baseCertificateID;
        this.version = version;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public Holder(final GeneralNames entityName) {
        this.version = 1;
        this.entityName = entityName;
    }
    
    public Holder(final GeneralNames entityName, final int version) {
        this.version = 1;
        this.entityName = entityName;
        this.version = version;
    }
    
    public Holder(final ObjectDigestInfo objectDigestInfo) {
        this.version = 1;
        this.objectDigestInfo = objectDigestInfo;
    }
    
    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }
    
    public GeneralNames getEntityName() {
        return this.entityName;
    }
    
    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.version == 1) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            if (this.baseCertificateID != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
            }
            if (this.entityName != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 1, this.entityName));
            }
            if (this.objectDigestInfo != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
            }
            return new DERSequence(asn1EncodableVector);
        }
        if (this.entityName != null) {
            return new DERTaggedObject(false, 1, this.entityName);
        }
        return new DERTaggedObject(false, 0, this.baseCertificateID);
    }
}
