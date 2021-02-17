// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.Attribute;

public class SMIMECapabilitiesAttribute extends Attribute
{
    public SMIMECapabilitiesAttribute(final SMIMECapabilityVector smimeCapabilityVector) {
        super(SMIMEAttributes.smimeCapabilities, new DERSet(new DERSequence(smimeCapabilityVector.toDEREncodableVector())));
    }
}
