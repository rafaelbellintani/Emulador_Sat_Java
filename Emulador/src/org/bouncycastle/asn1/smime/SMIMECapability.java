// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SMIMECapability extends ASN1Encodable
{
    public static final DERObjectIdentifier preferSignedData;
    public static final DERObjectIdentifier canNotDecryptAny;
    public static final DERObjectIdentifier sMIMECapabilitiesVersions;
    public static final DERObjectIdentifier dES_CBC;
    public static final DERObjectIdentifier dES_EDE3_CBC;
    public static final DERObjectIdentifier rC2_CBC;
    public static final DERObjectIdentifier aES128_CBC;
    public static final DERObjectIdentifier aES192_CBC;
    public static final DERObjectIdentifier aES256_CBC;
    private DERObjectIdentifier capabilityID;
    private DEREncodable parameters;
    
    public SMIMECapability(final ASN1Sequence asn1Sequence) {
        this.capabilityID = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() > 1) {
            this.parameters = asn1Sequence.getObjectAt(1);
        }
    }
    
    public SMIMECapability(final DERObjectIdentifier capabilityID, final DEREncodable parameters) {
        this.capabilityID = capabilityID;
        this.parameters = parameters;
    }
    
    public static SMIMECapability getInstance(final Object o) {
        if (o == null || o instanceof SMIMECapability) {
            return (SMIMECapability)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SMIMECapability((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid SMIMECapability");
    }
    
    public DERObjectIdentifier getCapabilityID() {
        return this.capabilityID;
    }
    
    public DEREncodable getParameters() {
        return this.parameters;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.capabilityID);
        if (this.parameters != null) {
            asn1EncodableVector.add(this.parameters);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        preferSignedData = PKCSObjectIdentifiers.preferSignedData;
        canNotDecryptAny = PKCSObjectIdentifiers.canNotDecryptAny;
        sMIMECapabilitiesVersions = PKCSObjectIdentifiers.sMIMECapabilitiesVersions;
        dES_CBC = new DERObjectIdentifier("1.3.14.3.2.7");
        dES_EDE3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC;
        rC2_CBC = PKCSObjectIdentifiers.RC2_CBC;
        aES128_CBC = NISTObjectIdentifiers.id_aes128_CBC;
        aES192_CBC = NISTObjectIdentifiers.id_aes192_CBC;
        aES256_CBC = NISTObjectIdentifiers.id_aes256_CBC;
    }
}
