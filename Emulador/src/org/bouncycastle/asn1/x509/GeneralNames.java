// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class GeneralNames extends ASN1Encodable
{
    private final GeneralName[] names;
    
    public static GeneralNames getInstance(final Object o) {
        if (o == null || o instanceof GeneralNames) {
            return (GeneralNames)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GeneralNames((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static GeneralNames getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralNames(final GeneralName generalName) {
        this.names = new GeneralName[] { generalName };
    }
    
    public GeneralNames(final ASN1Sequence asn1Sequence) {
        this.names = new GeneralName[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            this.names[i] = GeneralName.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public GeneralName[] getNames() {
        final GeneralName[] array = new GeneralName[this.names.length];
        System.arraycopy(this.names, 0, array, 0, this.names.length);
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DERSequence(this.names);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("GeneralNames:");
        sb.append(property);
        for (int i = 0; i != this.names.length; ++i) {
            sb.append("    ");
            sb.append(this.names[i]);
            sb.append(property);
        }
        return sb.toString();
    }
}
