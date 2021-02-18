// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface ICAOObjectIdentifiers
{
    public static final String id_icao = "2.23.136";
    public static final DERObjectIdentifier id_icao_mrtd = new DERObjectIdentifier("2.23.136.1");
    public static final DERObjectIdentifier id_icao_mrtd_security = new DERObjectIdentifier(ICAOObjectIdentifiers.id_icao_mrtd + ".1");
    public static final DERObjectIdentifier id_icao_ldsSecurityObject = new DERObjectIdentifier(ICAOObjectIdentifiers.id_icao_mrtd_security + ".1");
}
