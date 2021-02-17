// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DERObjectIdentifier;

public interface SMIMEAttributes
{
    public static final DERObjectIdentifier smimeCapabilities = PKCSObjectIdentifiers.pkcs_9_at_smimeCapabilities;
    public static final DERObjectIdentifier encrypKeyPref = PKCSObjectIdentifiers.id_aa_encrypKeyPref;
}
