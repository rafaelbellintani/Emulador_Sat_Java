// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class AlgorithmIdentifier extends ASN1Encodable
{
    private DERObjectIdentifier objectId;
    private DEREncodable parameters;
    private boolean parametersDefined;
    
    public static AlgorithmIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static AlgorithmIdentifier getInstance(final Object o) {
        if (o == null || o instanceof AlgorithmIdentifier) {
            return (AlgorithmIdentifier)o;
        }
        if (o instanceof DERObjectIdentifier) {
            return new AlgorithmIdentifier((DERObjectIdentifier)o);
        }
        if (o instanceof String) {
            return new AlgorithmIdentifier((String)o);
        }
        if (o instanceof ASN1Sequence) {
            return new AlgorithmIdentifier((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier(final DERObjectIdentifier objectId) {
        this.parametersDefined = false;
        this.objectId = objectId;
    }
    
    public AlgorithmIdentifier(final String s) {
        this.parametersDefined = false;
        this.objectId = new DERObjectIdentifier(s);
    }
    
    public AlgorithmIdentifier(final DERObjectIdentifier objectId, final DEREncodable parameters) {
        this.parametersDefined = false;
        this.parametersDefined = true;
        this.objectId = objectId;
        this.parameters = parameters;
    }
    
    public AlgorithmIdentifier(final ASN1Sequence asn1Sequence) {
        this.parametersDefined = false;
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.objectId = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.parametersDefined = true;
            this.parameters = asn1Sequence.getObjectAt(1);
        }
        else {
            this.parameters = null;
        }
    }
    
    public DERObjectIdentifier getObjectId() {
        return this.objectId;
    }
    
    public DEREncodable getParameters() {
        return this.parameters;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.objectId);
        if (this.parametersDefined) {
            asn1EncodableVector.add(this.parameters);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
