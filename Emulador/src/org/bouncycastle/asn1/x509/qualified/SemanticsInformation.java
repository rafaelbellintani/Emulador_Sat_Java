// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SemanticsInformation extends ASN1Encodable
{
    DERObjectIdentifier semanticsIdentifier;
    GeneralName[] nameRegistrationAuthorities;
    
    public static SemanticsInformation getInstance(final Object o) {
        if (o == null || o instanceof SemanticsInformation) {
            return (SemanticsInformation)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SemanticsInformation(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public SemanticsInformation(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        if (asn1Sequence.size() < 1) {
            throw new IllegalArgumentException("no objects in SemanticsInformation");
        }
        Object o = objects.nextElement();
        if (o instanceof DERObjectIdentifier) {
            this.semanticsIdentifier = DERObjectIdentifier.getInstance(o);
            if (objects.hasMoreElements()) {
                o = objects.nextElement();
            }
            else {
                o = null;
            }
        }
        if (o != null) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(o);
            this.nameRegistrationAuthorities = new GeneralName[instance.size()];
            for (int i = 0; i < instance.size(); ++i) {
                this.nameRegistrationAuthorities[i] = GeneralName.getInstance(instance.getObjectAt(i));
            }
        }
    }
    
    public SemanticsInformation(final DERObjectIdentifier semanticsIdentifier, final GeneralName[] nameRegistrationAuthorities) {
        this.semanticsIdentifier = semanticsIdentifier;
        this.nameRegistrationAuthorities = nameRegistrationAuthorities;
    }
    
    public SemanticsInformation(final DERObjectIdentifier semanticsIdentifier) {
        this.semanticsIdentifier = semanticsIdentifier;
        this.nameRegistrationAuthorities = null;
    }
    
    public SemanticsInformation(final GeneralName[] nameRegistrationAuthorities) {
        this.semanticsIdentifier = null;
        this.nameRegistrationAuthorities = nameRegistrationAuthorities;
    }
    
    public DERObjectIdentifier getSemanticsIdentifier() {
        return this.semanticsIdentifier;
    }
    
    public GeneralName[] getNameRegistrationAuthorities() {
        return this.nameRegistrationAuthorities;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.semanticsIdentifier != null) {
            asn1EncodableVector.add(this.semanticsIdentifier);
        }
        if (this.nameRegistrationAuthorities != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int i = 0; i < this.nameRegistrationAuthorities.length; ++i) {
                asn1EncodableVector2.add(this.nameRegistrationAuthorities[i]);
            }
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
