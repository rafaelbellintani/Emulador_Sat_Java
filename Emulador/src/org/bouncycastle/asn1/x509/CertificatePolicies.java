// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import java.util.Vector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificatePolicies extends ASN1Encodable
{
    static final DERObjectIdentifier anyPolicy;
    Vector policies;
    
    @Deprecated
    public static CertificatePolicies getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    @Deprecated
    public static CertificatePolicies getInstance(final Object o) {
        if (o instanceof CertificatePolicies) {
            return (CertificatePolicies)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertificatePolicies((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    @Deprecated
    public CertificatePolicies(final ASN1Sequence asn1Sequence) {
        this.policies = new Vector();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            this.policies.addElement(ASN1Sequence.getInstance(objects.nextElement()).getObjectAt(0));
        }
    }
    
    @Deprecated
    public CertificatePolicies(final DERObjectIdentifier obj) {
        (this.policies = new Vector()).addElement(obj);
    }
    
    @Deprecated
    public CertificatePolicies(final String s) {
        this(new DERObjectIdentifier(s));
    }
    
    public void addPolicy(final String s) {
        this.policies.addElement(new DERObjectIdentifier(s));
    }
    
    public String getPolicy(final int index) {
        if (this.policies.size() > index) {
            return this.policies.elementAt(index).getId();
        }
        return null;
    }
    
    @Override
    @Deprecated
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < this.policies.size(); ++i) {
            asn1EncodableVector.add(new DERSequence((DEREncodable)this.policies.elementAt(i)));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        String str = null;
        for (int i = 0; i < this.policies.size(); ++i) {
            if (str != null) {
                str += ", ";
            }
            str += ((DERObjectIdentifier)this.policies.elementAt(i)).getId();
        }
        return "CertificatePolicies: " + str;
    }
    
    static {
        anyPolicy = new DERObjectIdentifier("2.5.29.32.0");
    }
}
