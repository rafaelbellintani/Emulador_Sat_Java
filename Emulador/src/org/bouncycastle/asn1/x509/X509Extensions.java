// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import java.util.Vector;
import java.util.Hashtable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class X509Extensions extends ASN1Encodable
{
    public static final DERObjectIdentifier SubjectDirectoryAttributes;
    public static final DERObjectIdentifier SubjectKeyIdentifier;
    public static final DERObjectIdentifier KeyUsage;
    public static final DERObjectIdentifier PrivateKeyUsagePeriod;
    public static final DERObjectIdentifier SubjectAlternativeName;
    public static final DERObjectIdentifier IssuerAlternativeName;
    public static final DERObjectIdentifier BasicConstraints;
    public static final DERObjectIdentifier CRLNumber;
    public static final DERObjectIdentifier ReasonCode;
    public static final DERObjectIdentifier InstructionCode;
    public static final DERObjectIdentifier InvalidityDate;
    public static final DERObjectIdentifier DeltaCRLIndicator;
    public static final DERObjectIdentifier IssuingDistributionPoint;
    public static final DERObjectIdentifier CertificateIssuer;
    public static final DERObjectIdentifier NameConstraints;
    public static final DERObjectIdentifier CRLDistributionPoints;
    public static final DERObjectIdentifier CertificatePolicies;
    public static final DERObjectIdentifier PolicyMappings;
    public static final DERObjectIdentifier AuthorityKeyIdentifier;
    public static final DERObjectIdentifier PolicyConstraints;
    public static final DERObjectIdentifier ExtendedKeyUsage;
    public static final DERObjectIdentifier FreshestCRL;
    public static final DERObjectIdentifier InhibitAnyPolicy;
    public static final DERObjectIdentifier AuthorityInfoAccess;
    public static final DERObjectIdentifier SubjectInfoAccess;
    public static final DERObjectIdentifier LogoType;
    public static final DERObjectIdentifier BiometricInfo;
    public static final DERObjectIdentifier QCStatements;
    public static final DERObjectIdentifier AuditIdentity;
    public static final DERObjectIdentifier NoRevAvail;
    public static final DERObjectIdentifier TargetInformation;
    private Hashtable extensions;
    private Vector ordering;
    
    public static X509Extensions getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static X509Extensions getInstance(final Object o) {
        if (o == null || o instanceof X509Extensions) {
            return (X509Extensions)o;
        }
        if (o instanceof ASN1Sequence) {
            return new X509Extensions((ASN1Sequence)o);
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public X509Extensions(final ASN1Sequence asn1Sequence) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(objects.nextElement());
            if (instance.size() == 3) {
                this.extensions.put(instance.getObjectAt(0), new X509Extension(DERBoolean.getInstance(instance.getObjectAt(1)), ASN1OctetString.getInstance(instance.getObjectAt(2))));
            }
            else {
                if (instance.size() != 2) {
                    throw new IllegalArgumentException("Bad sequence size: " + instance.size());
                }
                this.extensions.put(instance.getObjectAt(0), new X509Extension(false, ASN1OctetString.getInstance(instance.getObjectAt(1))));
            }
            this.ordering.addElement(instance.getObjectAt(0));
        }
    }
    
    public X509Extensions(final Hashtable hashtable) {
        this(null, hashtable);
    }
    
    public X509Extensions(final Vector vector, final Hashtable hashtable) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration<Object> enumeration;
        if (vector == null) {
            enumeration = hashtable.keys();
        }
        else {
            enumeration = vector.elements();
        }
        while (enumeration.hasMoreElements()) {
            this.ordering.addElement(enumeration.nextElement());
        }
        final Enumeration<DERObjectIdentifier> elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            final DERObjectIdentifier derObjectIdentifier = elements.nextElement();
            this.extensions.put(derObjectIdentifier, hashtable.get(derObjectIdentifier));
        }
    }
    
    public X509Extensions(final Vector vector, final Vector vector2) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        final Enumeration<Object> elements = vector.elements();
        while (elements.hasMoreElements()) {
            this.ordering.addElement(elements.nextElement());
        }
        int index = 0;
        final Enumeration<DERObjectIdentifier> elements2 = this.ordering.elements();
        while (elements2.hasMoreElements()) {
            this.extensions.put(elements2.nextElement(), vector2.elementAt(index));
            ++index;
        }
    }
    
    public Enumeration oids() {
        return this.ordering.elements();
    }
    
    public X509Extension getExtension(final DERObjectIdentifier key) {
        return this.extensions.get(key);
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<DERObjectIdentifier> elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            final DERObjectIdentifier key = elements.nextElement();
            final X509Extension x509Extension = this.extensions.get(key);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(key);
            if (x509Extension.isCritical()) {
                asn1EncodableVector2.add(new DERBoolean(true));
            }
            asn1EncodableVector2.add(x509Extension.getValue());
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public boolean equivalent(final X509Extensions x509Extensions) {
        if (this.extensions.size() != x509Extensions.extensions.size()) {
            return false;
        }
        final Enumeration<Object> keys = this.extensions.keys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            if (!this.extensions.get(nextElement).equals(x509Extensions.extensions.get(nextElement))) {
                return false;
            }
        }
        return true;
    }
    
    static {
        SubjectDirectoryAttributes = new DERObjectIdentifier("2.5.29.9");
        SubjectKeyIdentifier = new DERObjectIdentifier("2.5.29.14");
        KeyUsage = new DERObjectIdentifier("2.5.29.15");
        PrivateKeyUsagePeriod = new DERObjectIdentifier("2.5.29.16");
        SubjectAlternativeName = new DERObjectIdentifier("2.5.29.17");
        IssuerAlternativeName = new DERObjectIdentifier("2.5.29.18");
        BasicConstraints = new DERObjectIdentifier("2.5.29.19");
        CRLNumber = new DERObjectIdentifier("2.5.29.20");
        ReasonCode = new DERObjectIdentifier("2.5.29.21");
        InstructionCode = new DERObjectIdentifier("2.5.29.23");
        InvalidityDate = new DERObjectIdentifier("2.5.29.24");
        DeltaCRLIndicator = new DERObjectIdentifier("2.5.29.27");
        IssuingDistributionPoint = new DERObjectIdentifier("2.5.29.28");
        CertificateIssuer = new DERObjectIdentifier("2.5.29.29");
        NameConstraints = new DERObjectIdentifier("2.5.29.30");
        CRLDistributionPoints = new DERObjectIdentifier("2.5.29.31");
        CertificatePolicies = new DERObjectIdentifier("2.5.29.32");
        PolicyMappings = new DERObjectIdentifier("2.5.29.33");
        AuthorityKeyIdentifier = new DERObjectIdentifier("2.5.29.35");
        PolicyConstraints = new DERObjectIdentifier("2.5.29.36");
        ExtendedKeyUsage = new DERObjectIdentifier("2.5.29.37");
        FreshestCRL = new DERObjectIdentifier("2.5.29.46");
        InhibitAnyPolicy = new DERObjectIdentifier("2.5.29.54");
        AuthorityInfoAccess = new DERObjectIdentifier("1.3.6.1.5.5.7.1.1");
        SubjectInfoAccess = new DERObjectIdentifier("1.3.6.1.5.5.7.1.11");
        LogoType = new DERObjectIdentifier("1.3.6.1.5.5.7.1.12");
        BiometricInfo = new DERObjectIdentifier("1.3.6.1.5.5.7.1.2");
        QCStatements = new DERObjectIdentifier("1.3.6.1.5.5.7.1.3");
        AuditIdentity = new DERObjectIdentifier("1.3.6.1.5.5.7.1.4");
        NoRevAvail = new DERObjectIdentifier("2.5.29.56");
        TargetInformation = new DERObjectIdentifier("2.5.29.55");
    }
}
