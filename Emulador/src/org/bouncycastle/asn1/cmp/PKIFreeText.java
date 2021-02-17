// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIFreeText extends ASN1Encodable
{
    ASN1Sequence strings;
    
    public static PKIFreeText getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static PKIFreeText getInstance(final Object o) {
        if (o instanceof PKIFreeText) {
            return (PKIFreeText)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIFreeText((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Unknown object in factory: " + o.getClass().getName());
    }
    
    public PKIFreeText(final ASN1Sequence strings) {
        final Enumeration objects = strings.getObjects();
        while (objects.hasMoreElements()) {
            if (!(objects.nextElement() instanceof DERUTF8String)) {
                throw new IllegalArgumentException("attempt to insert non UTF8 STRING into PKIFreeText");
            }
        }
        this.strings = strings;
    }
    
    public PKIFreeText(final DERUTF8String derutf8String) {
        this.strings = new DERSequence(derutf8String);
    }
    
    public int size() {
        return this.strings.size();
    }
    
    public DERUTF8String getStringAt(final int n) {
        return (DERUTF8String)this.strings.getObjectAt(n);
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.strings;
    }
}
