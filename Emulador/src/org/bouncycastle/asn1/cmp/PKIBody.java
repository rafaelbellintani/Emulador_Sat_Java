// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.crmf.CertReqMessages;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIBody extends ASN1Encodable implements ASN1Choice
{
    private int tagNo;
    private ASN1Encodable body;
    
    public static PKIBody getInstance(final Object o) {
        if (o instanceof PKIBody) {
            return (PKIBody)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new PKIBody((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    private PKIBody(final ASN1TaggedObject asn1TaggedObject) {
        this.tagNo = asn1TaggedObject.getTagNo();
        switch (asn1TaggedObject.getTagNo()) {
            case 0: {
                this.body = CertReqMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 1: {
                this.body = CertRepMessage.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 2: {
                this.body = CertReqMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 3: {
                this.body = CertRepMessage.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 4: {
                this.body = CertificationRequest.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 5: {
                this.body = POPODecKeyChallContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 6: {
                this.body = POPODecKeyRespContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 7: {
                this.body = CertReqMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 8: {
                this.body = CertRepMessage.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 9: {
                this.body = CertReqMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 10: {
                this.body = KeyRecRepContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 11: {
                this.body = RevReqContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 12: {
                this.body = RevRepContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 13: {
                this.body = CertReqMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 14: {
                this.body = CertRepMessage.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 15: {
                this.body = CAKeyUpdAnnContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 16: {
                this.body = CMPCertificate.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 17: {
                this.body = RevAnnContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 18: {
                this.body = CRLAnnContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 19: {
                this.body = PKIConfirmContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 20: {
                this.body = PKIMessages.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 21: {
                this.body = GenMsgContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 22: {
                this.body = GenRepContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 23: {
                this.body = ErrorMsgContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 24: {
                this.body = CertConfirmContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 25: {
                this.body = PollReqContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            case 26: {
                this.body = PollRepContent.getInstance(asn1TaggedObject.getObject());
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown tag number: " + asn1TaggedObject.getTagNo());
            }
        }
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DERTaggedObject(true, this.tagNo, this.body);
    }
}
