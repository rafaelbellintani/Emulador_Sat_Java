// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class ErrorMsgContent extends ASN1Encodable
{
    private PKIStatusInfo pKIStatusInfo;
    private DERInteger errorCode;
    private PKIFreeText errorDetails;
    
    private ErrorMsgContent(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pKIStatusInfo = PKIStatusInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            if (nextElement instanceof DERInteger) {
                this.errorCode = DERInteger.getInstance(nextElement);
            }
            else {
                this.errorDetails = PKIFreeText.getInstance(nextElement);
            }
        }
    }
    
    public static ErrorMsgContent getInstance(final Object o) {
        if (o instanceof ErrorMsgContent) {
            return (ErrorMsgContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ErrorMsgContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public PKIStatusInfo getPKIStatusInfo() {
        return this.pKIStatusInfo;
    }
    
    public DERInteger getErrorCode() {
        return this.errorCode;
    }
    
    public PKIFreeText getErrorDetails() {
        return this.errorDetails;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pKIStatusInfo);
        this.addOptional(asn1EncodableVector, this.errorCode);
        this.addOptional(asn1EncodableVector, this.errorDetails);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
}
