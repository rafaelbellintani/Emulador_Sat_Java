// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIHeader extends ASN1Encodable
{
    private DERInteger pvno;
    private GeneralName sender;
    private GeneralName recipient;
    private DERGeneralizedTime messageTime;
    private AlgorithmIdentifier protectionAlg;
    private ASN1OctetString senderKID;
    private ASN1OctetString recipKID;
    private ASN1OctetString transactionID;
    private ASN1OctetString senderNonce;
    private ASN1OctetString recipNonce;
    private PKIFreeText freeText;
    private ASN1Sequence generalInfo;
    
    private PKIHeader(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pvno = DERInteger.getInstance(objects.nextElement());
        this.sender = GeneralName.getInstance(objects.nextElement());
        this.recipient = GeneralName.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            switch (asn1TaggedObject.getTagNo()) {
                case 0: {
                    this.messageTime = DERGeneralizedTime.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 1: {
                    this.protectionAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 2: {
                    this.senderKID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 3: {
                    this.recipKID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 4: {
                    this.transactionID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 5: {
                    this.senderNonce = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 6: {
                    this.recipNonce = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 7: {
                    this.freeText = PKIFreeText.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 8: {
                    this.generalInfo = ASN1Sequence.getInstance(asn1TaggedObject, true);
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag number: " + asn1TaggedObject.getTagNo());
                }
            }
        }
    }
    
    public static PKIHeader getInstance(final Object o) {
        if (o instanceof PKIHeader) {
            return (PKIHeader)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIHeader((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getPvno() {
        return this.pvno;
    }
    
    public GeneralName getSender() {
        return this.sender;
    }
    
    public GeneralName getRecipient() {
        return this.recipient;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pvno);
        asn1EncodableVector.add(this.sender);
        asn1EncodableVector.add(this.recipient);
        this.addOptional(asn1EncodableVector, 0, this.messageTime);
        this.addOptional(asn1EncodableVector, 1, this.protectionAlg);
        this.addOptional(asn1EncodableVector, 2, this.senderKID);
        this.addOptional(asn1EncodableVector, 3, this.recipKID);
        this.addOptional(asn1EncodableVector, 4, this.transactionID);
        this.addOptional(asn1EncodableVector, 5, this.senderNonce);
        this.addOptional(asn1EncodableVector, 6, this.recipNonce);
        this.addOptional(asn1EncodableVector, 7, this.freeText);
        this.addOptional(asn1EncodableVector, 8, this.generalInfo);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
}
