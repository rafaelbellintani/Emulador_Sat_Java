// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms.ecc;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.cms.OriginatorPublicKey;
import org.bouncycastle.asn1.ASN1Encodable;

public class MQVuserKeyingMaterial extends ASN1Encodable
{
    private OriginatorPublicKey ephemeralPublicKey;
    private ASN1OctetString addedukm;
    
    public MQVuserKeyingMaterial(final OriginatorPublicKey ephemeralPublicKey, final ASN1OctetString addedukm) {
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.addedukm = addedukm;
    }
    
    private MQVuserKeyingMaterial(final ASN1Sequence asn1Sequence) {
        this.ephemeralPublicKey = OriginatorPublicKey.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.addedukm = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public static MQVuserKeyingMaterial getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static MQVuserKeyingMaterial getInstance(final Object o) {
        if (o == null || o instanceof MQVuserKeyingMaterial) {
            return (MQVuserKeyingMaterial)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MQVuserKeyingMaterial((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid MQVuserKeyingMaterial: " + o.getClass().getName());
    }
    
    public OriginatorPublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    public ASN1OctetString getAddedukm() {
        return this.addedukm;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.ephemeralPublicKey);
        if (this.addedukm != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.addedukm));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
