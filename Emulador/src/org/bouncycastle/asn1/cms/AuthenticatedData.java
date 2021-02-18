// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class AuthenticatedData extends ASN1Encodable
{
    private DERInteger version;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private AlgorithmIdentifier macAlgorithm;
    private AlgorithmIdentifier digestAlgorithm;
    private ContentInfo encapsulatedContentInfo;
    private ASN1Set authAttrs;
    private ASN1OctetString mac;
    private ASN1Set unauthAttrs;
    
    public AuthenticatedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final AlgorithmIdentifier macAlgorithm, final AlgorithmIdentifier digestAlgorithm, final ContentInfo encapsulatedContentInfo, final ASN1Set authAttrs, final ASN1OctetString mac, final ASN1Set unauthAttrs) {
        if ((digestAlgorithm != null || authAttrs != null) && (digestAlgorithm == null || authAttrs == null)) {
            throw new IllegalArgumentException("digestAlgorithm and authAttrs must be set together");
        }
        this.version = new DERInteger(calculateVersion(originatorInfo));
        this.originatorInfo = originatorInfo;
        this.macAlgorithm = macAlgorithm;
        this.digestAlgorithm = digestAlgorithm;
        this.recipientInfos = recipientInfos;
        this.encapsulatedContentInfo = encapsulatedContentInfo;
        this.authAttrs = authAttrs;
        this.mac = mac;
        this.unauthAttrs = unauthAttrs;
    }
    
    public AuthenticatedData(final ASN1Sequence asn1Sequence) {
        int n = 0;
        this.version = (DERInteger)asn1Sequence.getObjectAt(n++);
        DEREncodable derEncodable = asn1Sequence.getObjectAt(n++);
        if (derEncodable instanceof ASN1TaggedObject) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)derEncodable, false);
            derEncodable = asn1Sequence.getObjectAt(n++);
        }
        this.recipientInfos = ASN1Set.getInstance(derEncodable);
        this.macAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n++));
        DEREncodable derEncodable2 = asn1Sequence.getObjectAt(n++);
        if (derEncodable2 instanceof ASN1TaggedObject) {
            this.digestAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)derEncodable2, false);
            derEncodable2 = asn1Sequence.getObjectAt(n++);
        }
        this.encapsulatedContentInfo = ContentInfo.getInstance(derEncodable2);
        DEREncodable derEncodable3 = asn1Sequence.getObjectAt(n++);
        if (derEncodable3 instanceof ASN1TaggedObject) {
            this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)derEncodable3, false);
            derEncodable3 = asn1Sequence.getObjectAt(n++);
        }
        this.mac = ASN1OctetString.getInstance(derEncodable3);
        if (asn1Sequence.size() > n) {
            this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n), false);
        }
    }
    
    public static AuthenticatedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static AuthenticatedData getInstance(final Object o) {
        if (o == null || o instanceof AuthenticatedData) {
            return (AuthenticatedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AuthenticatedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid AuthenticatedData: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }
    
    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }
    
    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }
    
    public ContentInfo getEncapsulatedContentInfo() {
        return this.encapsulatedContentInfo;
    }
    
    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }
    
    public ASN1OctetString getMac() {
        return this.mac;
    }
    
    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        asn1EncodableVector.add(this.recipientInfos);
        asn1EncodableVector.add(this.macAlgorithm);
        if (this.digestAlgorithm != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.digestAlgorithm));
        }
        asn1EncodableVector.add(this.encapsulatedContentInfo);
        if (this.authAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.authAttrs));
        }
        asn1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, this.unauthAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
    
    public static int calculateVersion(final OriginatorInfo originatorInfo) {
        if (originatorInfo == null) {
            return 0;
        }
        int n = 0;
        final Enumeration objects = originatorInfo.getCertificates().getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject nextElement = objects.nextElement();
            if (nextElement instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = nextElement;
                if (asn1TaggedObject.getTagNo() == 2) {
                    n = 1;
                }
                else {
                    if (asn1TaggedObject.getTagNo() == 3) {
                        n = 3;
                        break;
                    }
                    continue;
                }
            }
        }
        final Enumeration objects2 = originatorInfo.getCRLs().getObjects();
        while (objects2.hasMoreElements()) {
            final ASN1TaggedObject nextElement2 = objects2.nextElement();
            if (nextElement2 instanceof ASN1TaggedObject && nextElement2.getTagNo() == 1) {
                n = 3;
                break;
            }
        }
        return n;
    }
}
