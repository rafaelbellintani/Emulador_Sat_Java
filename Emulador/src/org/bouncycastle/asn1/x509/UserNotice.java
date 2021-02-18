// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class UserNotice extends ASN1Encodable
{
    private NoticeReference noticeRef;
    private DisplayText explicitText;
    
    public UserNotice(final NoticeReference noticeRef, final DisplayText explicitText) {
        this.noticeRef = noticeRef;
        this.explicitText = explicitText;
    }
    
    public UserNotice(final NoticeReference noticeRef, final String s) {
        this.noticeRef = noticeRef;
        this.explicitText = new DisplayText(s);
    }
    
    public UserNotice(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.noticeRef = NoticeReference.getInstance(asn1Sequence.getObjectAt(0));
            this.explicitText = DisplayText.getInstance(asn1Sequence.getObjectAt(1));
        }
        else {
            if (asn1Sequence.size() != 1) {
                throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
            }
            if (asn1Sequence.getObjectAt(0).getDERObject() instanceof ASN1Sequence) {
                this.noticeRef = NoticeReference.getInstance(asn1Sequence.getObjectAt(0));
            }
            else {
                this.explicitText = DisplayText.getInstance(asn1Sequence.getObjectAt(0));
            }
        }
    }
    
    public NoticeReference getNoticeRef() {
        return this.noticeRef;
    }
    
    public DisplayText getExplicitText() {
        return this.explicitText;
    }
    
    @Override
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
