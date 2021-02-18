// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class Accuracy extends ASN1Encodable
{
    DERInteger seconds;
    DERInteger millis;
    DERInteger micros;
    protected static final int MIN_MILLIS = 1;
    protected static final int MAX_MILLIS = 999;
    protected static final int MIN_MICROS = 1;
    protected static final int MAX_MICROS = 999;
    
    protected Accuracy() {
    }
    
    public Accuracy(final DERInteger seconds, final DERInteger millis, final DERInteger micros) {
        this.seconds = seconds;
        if (millis != null && (millis.getValue().intValue() < 1 || millis.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
        }
        this.millis = millis;
        if (micros != null && (micros.getValue().intValue() < 1 || micros.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
        }
        this.micros = micros;
    }
    
    public Accuracy(final ASN1Sequence asn1Sequence) {
        this.seconds = null;
        this.millis = null;
        this.micros = null;
        for (int i = 0; i < asn1Sequence.size(); ++i) {
            if (asn1Sequence.getObjectAt(i) instanceof DERInteger) {
                this.seconds = (DERInteger)asn1Sequence.getObjectAt(i);
            }
            else if (asn1Sequence.getObjectAt(i) instanceof DERTaggedObject) {
                final DERTaggedObject derTaggedObject = (DERTaggedObject)asn1Sequence.getObjectAt(i);
                switch (derTaggedObject.getTagNo()) {
                    case 0: {
                        this.millis = DERInteger.getInstance(derTaggedObject, false);
                        if (this.millis.getValue().intValue() < 1 || this.millis.getValue().intValue() > 999) {
                            throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
                        }
                        break;
                    }
                    case 1: {
                        this.micros = DERInteger.getInstance(derTaggedObject, false);
                        if (this.micros.getValue().intValue() < 1 || this.micros.getValue().intValue() > 999) {
                            throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
                        }
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Invalig tag number");
                    }
                }
            }
        }
    }
    
    public static Accuracy getInstance(final Object o) {
        if (o == null || o instanceof Accuracy) {
            return (Accuracy)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Accuracy((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Unknown object in 'Accuracy' factory : " + o.getClass().getName() + ".");
    }
    
    public DERInteger getSeconds() {
        return this.seconds;
    }
    
    public DERInteger getMillis() {
        return this.millis;
    }
    
    public DERInteger getMicros() {
        return this.micros;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.seconds != null) {
            asn1EncodableVector.add(this.seconds);
        }
        if (this.millis != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
        }
        if (this.micros != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.micros));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
