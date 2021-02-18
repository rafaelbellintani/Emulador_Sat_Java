// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface X509ObjectIdentifiers
{
    public static final String id = "2.5.4";
    public static final DERObjectIdentifier commonName = new DERObjectIdentifier("2.5.4.3");
    public static final DERObjectIdentifier countryName = new DERObjectIdentifier("2.5.4.6");
    public static final DERObjectIdentifier localityName = new DERObjectIdentifier("2.5.4.7");
    public static final DERObjectIdentifier stateOrProvinceName = new DERObjectIdentifier("2.5.4.8");
    public static final DERObjectIdentifier organization = new DERObjectIdentifier("2.5.4.10");
    public static final DERObjectIdentifier organizationalUnitName = new DERObjectIdentifier("2.5.4.11");
    public static final DERObjectIdentifier id_at_telephoneNumber = new DERObjectIdentifier("2.5.4.20");
    public static final DERObjectIdentifier id_at_name = new DERObjectIdentifier("2.5.4.41");
    public static final DERObjectIdentifier id_SHA1 = new DERObjectIdentifier("1.3.14.3.2.26");
    public static final DERObjectIdentifier ripemd160 = new DERObjectIdentifier("1.3.36.3.2.1");
    public static final DERObjectIdentifier ripemd160WithRSAEncryption = new DERObjectIdentifier("1.3.36.3.3.1.2");
    public static final DERObjectIdentifier id_ea_rsa = new DERObjectIdentifier("2.5.8.1.1");
    public static final DERObjectIdentifier id_pkix = new DERObjectIdentifier("1.3.6.1.5.5.7");
    public static final DERObjectIdentifier id_pe = new DERObjectIdentifier(X509ObjectIdentifiers.id_pkix + ".1");
    public static final DERObjectIdentifier id_ad = new DERObjectIdentifier(X509ObjectIdentifiers.id_pkix + ".48");
    public static final DERObjectIdentifier id_ad_caIssuers = new DERObjectIdentifier(X509ObjectIdentifiers.id_ad + ".2");
    public static final DERObjectIdentifier id_ad_ocsp = new DERObjectIdentifier(X509ObjectIdentifiers.id_ad + ".1");
    public static final DERObjectIdentifier ocspAccessMethod = X509ObjectIdentifiers.id_ad_ocsp;
    public static final DERObjectIdentifier crlAccessMethod = X509ObjectIdentifiers.id_ad_caIssuers;
}
