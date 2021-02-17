// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class Targets extends ASN1Encodable
{
    private ASN1Sequence targets;
    
    public static Targets getInstance(final Object o) {
        if (o instanceof Targets) {
            return (Targets)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Targets((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass());
    }
    
    private Targets(final ASN1Sequence targets) {
        this.targets = targets;
    }
    
    public Targets(final Target[] array) {
        this.targets = new DERSequence(array);
    }
    
    public Target[] getTargets() {
        final Target[] array = new Target[this.targets.size()];
        int n = 0;
        final Enumeration objects = this.targets.getObjects();
        while (objects.hasMoreElements()) {
            array[n++] = Target.getInstance(objects.nextElement());
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.targets;
    }
}
