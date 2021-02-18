// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class POPOPrivKey extends ASN1Encodable implements ASN1Choice
{
    private DERObject obj;
    
    private POPOPrivKey(final DERObject obj) {
        this.obj = obj;
    }
    
    public static ASN1Encodable getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return new POPOPrivKey(asn1TaggedObject.getObject());
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.obj;
    }
}
