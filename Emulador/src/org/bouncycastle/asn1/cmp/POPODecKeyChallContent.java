// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class POPODecKeyChallContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private POPODecKeyChallContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static POPODecKeyChallContent getInstance(final Object o) {
        if (o instanceof POPODecKeyChallContent) {
            return (POPODecKeyChallContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new POPODecKeyChallContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public Challenge[] toChallengeArray() {
        final Challenge[] array = new Challenge[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = Challenge.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
