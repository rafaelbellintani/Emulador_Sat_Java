// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class AttCertIssuer extends ASN1Encodable implements ASN1Choice
{
    ASN1Encodable obj;
    DERObject choiceObj;
    
    public static AttCertIssuer getInstance(final Object o) {
        if (o instanceof AttCertIssuer) {
            return (AttCertIssuer)o;
        }
        if (o instanceof V2Form) {
            return new AttCertIssuer(V2Form.getInstance(o));
        }
        if (o instanceof GeneralNames) {
            return new AttCertIssuer((GeneralNames)o);
        }
        if (o instanceof ASN1TaggedObject) {
            return new AttCertIssuer(V2Form.getInstance((ASN1TaggedObject)o, false));
        }
        if (o instanceof ASN1Sequence) {
            return new AttCertIssuer(GeneralNames.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public static AttCertIssuer getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public AttCertIssuer(final GeneralNames obj) {
        this.obj = obj;
        this.choiceObj = this.obj.getDERObject();
    }
    
    public AttCertIssuer(final V2Form obj) {
        this.obj = obj;
        this.choiceObj = new DERTaggedObject(false, 0, this.obj);
    }
    
    public ASN1Encodable getIssuer() {
        return this.obj;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.choiceObj;
    }
}
