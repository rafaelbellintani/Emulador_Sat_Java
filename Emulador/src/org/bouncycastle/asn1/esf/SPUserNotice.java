// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.asn1.x509.NoticeReference;

public class SPUserNotice
{
    private NoticeReference noticeRef;
    private DisplayText explicitText;
    
    public static SPUserNotice getInstance(final Object o) {
        if (o == null || o instanceof SPUserNotice) {
            return (SPUserNotice)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SPUserNotice((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'SPUserNotice' factory : " + o.getClass().getName() + ".");
    }
    
    public SPUserNotice(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final DEREncodable derEncodable = objects.nextElement();
            if (derEncodable instanceof NoticeReference) {
                this.noticeRef = NoticeReference.getInstance(derEncodable);
            }
            else {
                if (!(derEncodable instanceof DisplayText)) {
                    throw new IllegalArgumentException("Invalid element in 'SPUserNotice'.");
                }
                this.explicitText = DisplayText.getInstance(derEncodable);
            }
        }
    }
    
    public SPUserNotice(final NoticeReference noticeRef, final DisplayText explicitText) {
        this.noticeRef = noticeRef;
        this.explicitText = explicitText;
    }
    
    public NoticeReference getNoticeRef() {
        return this.noticeRef;
    }
    
    public DisplayText getExplicitText() {
        return this.explicitText;
    }
    
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.noticeRef != null) {
            asn1EncodableVector.add(this.noticeRef);
        }
        if (this.explicitText != null) {
            asn1EncodableVector.add(this.explicitText);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
