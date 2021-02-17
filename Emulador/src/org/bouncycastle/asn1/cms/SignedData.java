// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.BERTaggedObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignedData extends ASN1Encodable
{
    private DERInteger version;
    private ASN1Set digestAlgorithms;
    private ContentInfo contentInfo;
    private ASN1Set certificates;
    private ASN1Set crls;
    private ASN1Set signerInfos;
    private boolean certsBer;
    private boolean crlsBer;
    
    public static SignedData getInstance(final Object o) {
        if (o instanceof SignedData) {
            return (SignedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public SignedData(final ASN1Set digestAlgorithms, final ContentInfo contentInfo, final ASN1Set certificates, final ASN1Set crls, final ASN1Set signerInfos) {
        this.version = this.calculateVersion(contentInfo.getContentType(), certificates, crls, signerInfos);
        this.digestAlgorithms = digestAlgorithms;
        this.contentInfo = contentInfo;
        this.certificates = certificates;
        this.crls = crls;
        this.signerInfos = signerInfos;
        this.crlsBer = (crls instanceof BERSet);
        this.certsBer = (certificates instanceof BERSet);
    }
    
    private DERInteger calculateVersion(final DERObjectIdentifier derObjectIdentifier, final ASN1Set set, final ASN1Set set2, final ASN1Set set3) {
        boolean b = false;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 = false;
        if (set != null) {
            final Enumeration objects = set.getObjects();
            while (objects.hasMoreElements()) {
                final ASN1TaggedObject nextElement = objects.nextElement();
                if (nextElement instanceof ASN1TaggedObject) {
                    final ASN1TaggedObject asn1TaggedObject = nextElement;
                    if (asn1TaggedObject.getTagNo() == 1) {
                        b3 = true;
                    }
                    else if (asn1TaggedObject.getTagNo() == 2) {
                        b4 = true;
                    }
                    else {
                        if (asn1TaggedObject.getTagNo() != 3) {
                            continue;
                        }
                        b = true;
                    }
                }
            }
        }
        if (b) {
            return new DERInteger(5);
        }
        if (set2 != null) {
            final Enumeration objects2 = set2.getObjects();
            while (objects2.hasMoreElements()) {
                if (objects2.nextElement() instanceof ASN1TaggedObject) {
                    b2 = true;
                }
            }
        }
        if (b2) {
            return new DERInteger(5);
        }
        if (b4) {
            return new DERInteger(4);
        }
        if (b3) {
            return new DERInteger(3);
        }
        if (!derObjectIdentifier.equals(CMSObjectIdentifiers.data)) {
            return new DERInteger(3);
        }
        if (this.checkForVersion3(set3)) {
            return new DERInteger(3);
        }
        return new DERInteger(1);
    }
    
    private boolean checkForVersion3(final ASN1Set set) {
        final Enumeration objects = set.getObjects();
        while (objects.hasMoreElements()) {
            if (SignerInfo.getInstance(objects.nextElement()).getVersion().getValue().intValue() == 3) {
                return true;
            }
        }
        return false;
    }
    
    public SignedData(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.digestAlgorithms = (ASN1Set)objects.nextElement();
        this.contentInfo = ContentInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final DERInteger derInteger = objects.nextElement();
            if (derInteger instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)derInteger;
                switch (asn1TaggedObject.getTagNo()) {
                    case 0: {
                        this.certsBer = (asn1TaggedObject instanceof BERTaggedObject);
                        this.certificates = ASN1Set.getInstance(asn1TaggedObject, false);
                        continue;
                    }
                    case 1: {
                        this.crlsBer = (asn1TaggedObject instanceof BERTaggedObject);
                        this.crls = ASN1Set.getInstance(asn1TaggedObject, false);
                        continue;
                    }
                    default: {
                        throw new IllegalArgumentException("unknown tag value " + asn1TaggedObject.getTagNo());
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
    
    public ContentInfo getEncapContentInfo() {
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
            if (this.certsBer) {
                asn1EncodableVector.add(new BERTaggedObject(false, 0, this.certificates));
            }
            else {
                asn1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
            }
        }
        if (this.crls != null) {
            if (this.crlsBer) {
                asn1EncodableVector.add(new BERTaggedObject(false, 1, this.crls));
            }
            else {
                asn1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
            }
        }
        asn1EncodableVector.add(this.signerInfos);
        return new BERSequence(asn1EncodableVector);
    }
}
