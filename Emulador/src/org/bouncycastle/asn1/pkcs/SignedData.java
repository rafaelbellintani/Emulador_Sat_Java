// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignedData extends ASN1Encodable implements PKCSObjectIdentifiers
{
    private DERInteger version;
    private ASN1Set digestAlgorithms;
    private ContentInfo contentInfo;
    private ASN1Set certificates;
    private ASN1Set crls;
    private ASN1Set signerInfos;
    
    public static SignedData getInstance(final Object obj) {
        if (obj instanceof SignedData) {
            return (SignedData)obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new SignedData((ASN1Sequence)obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj);
    }
    
    public SignedData(final DERInteger version, final ASN1Set digestAlgorithms, final ContentInfo contentInfo, final ASN1Set certificates, final ASN1Set crls, final ASN1Set signerInfos) {
        this.version = version;
        this.digestAlgorithms = digestAlgorithms;
        this.contentInfo = contentInfo;
        this.certificates = certificates;
        this.crls = crls;
        this.signerInfos = signerInfos;
    }
    
    public SignedData(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.digestAlgorithms = (ASN1Set)objects.nextElement();
        this.contentInfo = ContentInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final DERInteger derInteger = objects.nextElement();
            if (derInteger instanceof DERTaggedObject) {
                final DERTaggedObject derTaggedObject = (DERTaggedObject)derInteger;
                switch (derTaggedObject.getTagNo()) {
                    case 0: {
                        this.certificates = ASN1Set.getInstance(derTaggedObject, false);
                        continue;
                    }
                    case 1: {
                        this.crls = ASN1Set.getInstance(derTaggedObject, false);
                        continue;
                    }
                    default: {
                        throw new IllegalArgumentException("unknown tag value " + derTaggedObject.getTagNo());
                    }
                }
            }
            else {
                this.signerInfos = (ASN1Set)derInteger;
            }
        }
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public ASN1Set getDigestAlgorithms() {
        return this.digestAlgorithms;
    }
    
    public ContentInfo getContentInfo() {
        return this.contentInfo;
    }
    
    public ASN1Set getCertificates() {
        return this.certificates;
    }
    
    public ASN1Set getCRLs() {
        return this.crls;
    }
    
    public ASN1Set getSignerInfos() {
        return this.signerInfos;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.digestAlgorithms);
        asn1EncodableVector.add(this.contentInfo);
        if (this.certificates != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
        }
        if (this.crls != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
        }
        asn1EncodableVector.add(this.signerInfos);
        return new BERSequence(asn1EncodableVector);
    }
}
