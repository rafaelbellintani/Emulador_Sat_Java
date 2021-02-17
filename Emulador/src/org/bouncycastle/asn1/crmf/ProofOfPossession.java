// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class ProofOfPossession extends ASN1Encodable implements ASN1Choice
{
    private int tagNo;
    private ASN1Encodable obj;
    
    private ProofOfPossession(final ASN1TaggedObject asn1TaggedObject) {
        switch (this.tagNo = asn1TaggedObject.getTagNo()) {
            case 0: {
                this.obj = DERNull.INSTANCE;
                break;
            }
            case 1: {
                this.obj = POPOSigningKey.getInstance(asn1TaggedObject, false);
                break;
            }
            case 2:
            case 3: {
                this.obj = POPOPrivKey.getInstance(asn1TaggedObject, false);
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown tag: " + this.tagNo);
            }
        }
    }
    
    public static ProofOfPossession getInstance(final Object o) {
        if (o instanceof ProofOfPossession) {
            return (ProofOfPossession)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new ProofOfPossession((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public int getType() {
        return this.tagNo;
    }
    
    public ASN1Encodable getObject() {
        return this.obj;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DERTaggedObject(false, this.tagNo, this.obj);
    }
}
