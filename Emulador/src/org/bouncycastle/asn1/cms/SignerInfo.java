// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignerInfo extends ASN1Encodable
{
    private DERInteger version;
    private SignerIdentifier sid;
    private AlgorithmIdentifier digAlgorithm;
    private ASN1Set authenticatedAttributes;
    private AlgorithmIdentifier digEncryptionAlgorithm;
    private ASN1OctetString encryptedDigest;
    private ASN1Set unauthenticatedAttributes;
    
    public static SignerInfo getInstance(final Object o) throws IllegalArgumentException {
        if (o == null || o instanceof SignerInfo) {
            return (SignerInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignerInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public SignerInfo(final SignerIdentifier sid, final AlgorithmIdentifier digAlgorithm, final ASN1Set authenticatedAttributes, final AlgorithmIdentifier digEncryptionAlgorithm, final ASN1OctetString encryptedDigest, final ASN1Set unauthenticatedAttributes) {
        if (sid.isTagged()) {
            this.version = new DERInteger(3);
        }
        else {
            this.version = new DERInteger(1);
        }
        this.sid = sid;
        this.digAlgorithm = digAlgorithm;
        this.authenticatedAttributes = authenticatedAttributes;
        this.digEncryptionAlgorithm = digEncryptionAlgorithm;
        this.encryptedDigest = encryptedDigest;
        this.unauthenticatedAttributes = unauthenticatedAttributes;
    }
    
    public SignerInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.sid = SignerIdentifier.getInstance(objects.nextElement());
        this.digAlgorithm = AlgorithmIdentifier.getInstance(objects.nextElement());
        final DERInteger nextElement = objects.nextElement();
        if (nextElement instanceof ASN1TaggedObject) {
            this.authenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)nextElement, false);
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(objects.nextElement());
        }
        else {
            this.authenticatedAttributes = null;
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(nextElement);
        }
        this.encryptedDigest = ASN1OctetString.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.unauthenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)objects.nextElement(), false);
        }
        else {
            this.unauthenticatedAttributes = null;
        }
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public SignerIdentifier getSID() {
        return this.sid;
    }
    
    public ASN1Set getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digAlgorithm;
    }
    
    public ASN1OctetString getEncryptedDigest() {
        return this.encryptedDigest;
    }
    
    public AlgorithmIdentifier getDigestEncryptionAlgorithm() {
        return this.digEncryptionAlgorithm;
    }
    
    public ASN1Set getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.sid);
        asn1EncodableVector.add(this.digAlgorithm);
        if (this.authenticatedAttributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.authenticatedAttributes));
        }
        asn1EncodableVector.add(this.digEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedDigest);
        if (this.unauthenticatedAttributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.unauthenticatedAttributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
