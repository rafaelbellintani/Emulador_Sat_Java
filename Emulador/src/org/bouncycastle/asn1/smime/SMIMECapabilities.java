// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SMIMECapabilities extends ASN1Encodable
{
    public static final DERObjectIdentifier preferSignedData;
    public static final DERObjectIdentifier canNotDecryptAny;
    public static final DERObjectIdentifier sMIMECapabilitesVersions;
    public static final DERObjectIdentifier dES_CBC;
    public static final DERObjectIdentifier dES_EDE3_CBC;
    public static final DERObjectIdentifier rC2_CBC;
    private ASN1Sequence capabilities;
    
    public static SMIMECapabilities getInstance(final Object o) {
        if (o == null || o instanceof SMIMECapabilities) {
            return (SMIMECapabilities)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SMIMECapabilities((ASN1Sequence)o);
        }
        if (o instanceof Attribute) {
            return new SMIMECapabilities((ASN1Sequence)((Attribute)o).getAttrValues().getObjectAt(0));
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public SMIMECapabilities(final ASN1Sequence capabilities) {
        this.capabilities = capabilities;
    }
    
    public Vector getCapabilities(final DERObjectIdentifier derObjectIdentifier) {
        final Enumeration objects = this.capabilities.getObjects();
        final Vector<SMIMECapability> vector = new Vector<SMIMECapability>();
        if (derObjectIdentifier == null) {
            while (objects.hasMoreElements()) {
                vector.addElement(SMIMECapability.getInstance(objects.nextElement()));
            }
        }
        else {
            while (objects.hasMoreElements()) {
                final SMIMECapability instance = SMIMECapability.getInstance(objects.nextElement());
                if (derObjectIdentifier.equals(instance.getCapabilityID())) {
                    vector.addElement(instance);
                }
            }
        }
        return vector;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.capabilities;
    }
    
    static {
        preferSignedData = PKCSObjectIdentifiers.preferSignedData;
        canNotDecryptAny = PKCSObjectIdentifiers.canNotDecryptAny;
        sMIMECapabilitesVersions = PKCSObjectIdentifiers.sMIMECapabilitiesVersions;
        dES_CBC = new DERObjectIdentifier("1.3.14.3.2.7");
        dES_EDE3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC;
        rC2_CBC = PKCSObjectIdentifiers.RC2_CBC;
    }
}
