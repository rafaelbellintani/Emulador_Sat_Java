// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertTemplate extends ASN1Encodable
{
    private DERInteger version;
    private DERInteger serialNumber;
    private AlgorithmIdentifier signingAlg;
    private X509Name issuer;
    private OptionalValidity validity;
    private X509Name subject;
    private SubjectPublicKeyInfo publicKey;
    private DERBitString issuerUID;
    private DERBitString subjectUID;
    private X509Extensions extensions;
    
    private CertTemplate(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            switch (asn1TaggedObject.getTagNo()) {
                case 0: {
                    this.version = DERInteger.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 1: {
                    this.serialNumber = DERInteger.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 2: {
                    this.signingAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 3: {
                    this.issuer = X509Name.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 4: {
                    this.validity = OptionalValidity.getInstance(ASN1Sequence.getInstance(asn1TaggedObject, false));
                    continue;
                }
                case 5: {
                    this.subject = X509Name.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 6: {
                    this.publicKey = SubjectPublicKeyInfo.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 7: {
                    this.issuerUID = DERBitString.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 8: {
                    this.subjectUID = DERBitString.getInstance(asn1TaggedObject, false);
                    continue;
                }
                case 9: {
                    this.extensions = X509Extensions.getInstance(asn1TaggedObject, false);
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag: " + asn1TaggedObject.getTagNo());
                }
            }
        }
    }
    
    public static CertTemplate getInstance(final Object o) {
        if (o instanceof CertTemplate) {
            return (CertTemplate)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertTemplate((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, 0, false, this.version);
        this.addOptional(asn1EncodableVector, 1, false, this.serialNumber);
        this.addOptional(asn1EncodableVector, 2, false, this.signingAlg);
        this.addOptional(asn1EncodableVector, 3, true, this.issuer);
        this.addOptional(asn1EncodableVector, 4, false, this.validity);
        this.addOptional(asn1EncodableVector, 5, true, this.subject);
        this.addOptional(asn1EncodableVector, 6, false, this.publicKey);
        this.addOptional(asn1EncodableVector, 7, false, this.issuerUID);
        this.addOptional(asn1EncodableVector, 8, false, this.subjectUID);
        this.addOptional(asn1EncodableVector, 9, false, this.extensions);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final boolean b, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(b, n, asn1Encodable));
        }
    }
}
