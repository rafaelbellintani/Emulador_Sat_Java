// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class NoticeReference extends ASN1Encodable
{
    private DisplayText organization;
    private ASN1Sequence noticeNumbers;
    
    public NoticeReference(final String s, final Vector vector) {
        this.organization = new DisplayText(s);
        final Integer element = vector.elementAt(0);
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (element instanceof Integer) {
            final Enumeration<Integer> elements = vector.elements();
            while (elements.hasMoreElements()) {
                asn1EncodableVector.add(new DERInteger(elements.nextElement()));
            }
        }
        this.noticeNumbers = new DERSequence(asn1EncodableVector);
    }
    
    public NoticeReference(final String s, final ASN1Sequence noticeNumbers) {
        this.organization = new DisplayText(s);
        this.noticeNumbers = noticeNumbers;
    }
    
    public NoticeReference(final int n, final String s, final ASN1Sequence noticeNumbers) {
        this.organization = new DisplayText(n, s);
        this.noticeNumbers = noticeNumbers;
    }
    
    public NoticeReference(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.organization = DisplayText.getInstance(asn1Sequence.getObjectAt(0));
        this.noticeNumbers = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static NoticeReference getInstance(final Object o) {
        if (o instanceof NoticeReference) {
            return (NoticeReference)o;
        }
        if (o instanceof ASN1Sequence) {
            return new NoticeReference((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in getInstance.");
    }
    
    public DisplayText getOrganization() {
        return this.organization;
    }
    
    public ASN1Sequence getNoticeNumbers() {
        return this.noticeNumbers;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.organization);
        asn1EncodableVector.add(this.noticeNumbers);
        return new DERSequence(asn1EncodableVector);
    }
}
