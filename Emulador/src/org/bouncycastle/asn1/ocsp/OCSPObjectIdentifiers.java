// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface OCSPObjectIdentifiers
{
    public static final String pkix_ocsp = "1.3.6.1.5.5.7.48.1";
    public static final DERObjectIdentifier id_pkix_ocsp = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1");
    public static final DERObjectIdentifier id_pkix_ocsp_basic = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.1");
    public static final DERObjectIdentifier id_pkix_ocsp_nonce = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.2");
    public static final DERObjectIdentifier id_pkix_ocsp_crl = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.3");
    public static final DERObjectIdentifier id_pkix_ocsp_response = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.4");
    public static final DERObjectIdentifier id_pkix_ocsp_nocheck = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.5");
    public static final DERObjectIdentifier id_pkix_ocsp_archive_cutoff = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.6");
    public static final DERObjectIdentifier id_pkix_ocsp_service_locator = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1.7");
}
