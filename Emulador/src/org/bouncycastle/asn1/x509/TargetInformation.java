// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class TargetInformation extends ASN1Encodable
{
    private ASN1Sequence targets;
    
    public static TargetInformation getInstance(final Object o) {
        if (o instanceof TargetInformation) {
            return (TargetInformation)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TargetInformation((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass());
    }
    
    private TargetInformation(final ASN1Sequence targets) {
        this.targets = targets;
    }
    
    public Targets[] getTargetsObjects() {
        final Targets[] array = new Targets[this.targets.size()];
        int n = 0;
        final Enumeration objects = this.targets.getObjects();
        while (objects.hasMoreElements()) {
            array[n++] = Targets.getInstance(objects.nextElement());
        }
        return array;
    }
    
    public TargetInformation(final Targets targets) {
        this.targets = new DERSequence(targets);
    }
    
    public TargetInformation(final Target[] array) {
        this(new Targets(array));
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.targets;
    }
}
