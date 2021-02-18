// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.ASN1Encodable;

public class BasicConstraints extends ASN1Encodable
{
    DERBoolean cA;
    DERInteger pathLenConstraint;
    
    public static BasicConstraints getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static BasicConstraints getInstance(final Object o) {
        if (o == null || o instanceof BasicConstraints) {
            return (BasicConstraints)o;
        }
        if (o instanceof ASN1Sequence) {
            return new BasicConstraints((ASN1Sequence)o);
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public BasicConstraints(final ASN1Sequence asn1Sequence) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (asn1Sequence.size() == 0) {
            this.cA = null;
            this.pathLenConstraint = null;
        }
        else {
            if (asn1Sequence.getObjectAt(0) instanceof DERBoolean) {
                this.cA = DERBoolean.getInstance(asn1Sequence.getObjectAt(0));
            }
            else {
                this.cA = null;
                this.pathLenConstraint = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
            }
            if (asn1Sequence.size() > 1) {
                if (this.cA == null) {
                    throw new IllegalArgumentException("wrong sequence in constructor");
                }
                this.pathLenConstraint = DERInteger.getInstance(asn1Sequence.getObjectAt(1));
            }
        }
    }
    
    @Deprecated
    public BasicConstraints(final boolean b, final int n) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (b) {
            this.cA = new DERBoolean(b);
            this.pathLenConstraint = new DERInteger(n);
        }
        else {
            this.cA = null;
            this.pathLenConstraint = null;
        }
    }
    
    public BasicConstraints(final boolean b) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (b) {
            this.cA = new DERBoolean(true);
        }
        else {
            this.cA = null;
        }
        this.pathLenConstraint = null;
    }
    
    public BasicConstraints(final int n) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        this.cA = new DERBoolean(true);
        this.pathLenConstraint = new DERInteger(n);
    }
    
    public boolean isCA() {
        return this.cA != null && this.cA.isTrue();
    }
    
    public BigInteger getPathLenConstraint() {
        if (this.pathLenConstraint != null) {
            return this.pathLenConstraint.getValue();
        }
        return null;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.cA != null) {
            asn1EncodableVector.add(this.cA);
        }
        if (this.pathLenConstraint != null) {
            asn1EncodableVector.add(this.pathLenConstraint);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        if (this.pathLenConstraint != null) {
            return "BasicConstraints: isCa(" + this.isCA() + "), pathLenConstraint = " + this.pathLenConstraint.getValue();
        }
        if (this.cA == null) {
            return "BasicConstraints: isCa(false)";
        }
        return "BasicConstraints: isCa(" + this.isCA() + ")";
    }
}
