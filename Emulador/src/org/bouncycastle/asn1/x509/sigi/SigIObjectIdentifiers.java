// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.sigi;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface SigIObjectIdentifiers
{
    public static final DERObjectIdentifier id_sigi = new DERObjectIdentifier("1.3.36.8");
    public static final DERObjectIdentifier id_sigi_kp = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi + ".2");
    public static final DERObjectIdentifier id_sigi_cp = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi + ".1");
    public static final DERObjectIdentifier id_sigi_on = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi + ".4");
    public static final DERObjectIdentifier id_sigi_kp_directoryService = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi_kp + ".1");
    public static final DERObjectIdentifier id_sigi_on_personalData = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi_on + ".1");
    public static final DERObjectIdentifier id_sigi_cp_sigconform = new DERObjectIdentifier(SigIObjectIdentifiers.id_sigi_cp + ".1");
}
