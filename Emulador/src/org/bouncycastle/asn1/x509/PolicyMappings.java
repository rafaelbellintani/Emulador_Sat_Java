// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PolicyMappings extends ASN1Encodable
{
    ASN1Sequence seq;
    
    public PolicyMappings(final ASN1Sequence seq) {
        this.seq = null;
        this.seq = seq;
    }
    
    public PolicyMappings(final Hashtable hashtable) {
        this.seq = null;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final String s = (String)hashtable.get(key);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(new DERObjectIdentifier(key));
            asn1EncodableVector2.add(new DERObjectIdentifier(s));
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
}
