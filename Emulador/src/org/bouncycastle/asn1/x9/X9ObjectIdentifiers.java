// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface X9ObjectIdentifiers
{
    public static final String ansi_X9_62 = "1.2.840.10045";
    public static final String id_fieldType = "1.2.840.10045.1";
    public static final DERObjectIdentifier prime_field = new DERObjectIdentifier("1.2.840.10045.1.1");
    public static final DERObjectIdentifier characteristic_two_field = new DERObjectIdentifier("1.2.840.10045.1.2");
    public static final DERObjectIdentifier gnBasis = new DERObjectIdentifier("1.2.840.10045.1.2.3.1");
    public static final DERObjectIdentifier tpBasis = new DERObjectIdentifier("1.2.840.10045.1.2.3.2");
    public static final DERObjectIdentifier ppBasis = new DERObjectIdentifier("1.2.840.10045.1.2.3.3");
    public static final String id_ecSigType = "1.2.840.10045.4";
    public static final DERObjectIdentifier ecdsa_with_SHA1 = new DERObjectIdentifier("1.2.840.10045.4.1");
    public static final String id_publicKeyType = "1.2.840.10045.2";
    public static final DERObjectIdentifier id_ecPublicKey = new DERObjectIdentifier("1.2.840.10045.2.1");
    public static final DERObjectIdentifier ecdsa_with_SHA2 = new DERObjectIdentifier("1.2.840.10045.4.3");
    public static final DERObjectIdentifier ecdsa_with_SHA224 = new DERObjectIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA2 + ".1");
    public static final DERObjectIdentifier ecdsa_with_SHA256 = new DERObjectIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA2 + ".2");
    public static final DERObjectIdentifier ecdsa_with_SHA384 = new DERObjectIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA2 + ".3");
    public static final DERObjectIdentifier ecdsa_with_SHA512 = new DERObjectIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA2 + ".4");
    public static final String ellipticCurve = "1.2.840.10045.3";
    public static final String cTwoCurve = "1.2.840.10045.3.0";
    public static final DERObjectIdentifier c2pnb163v1 = new DERObjectIdentifier("1.2.840.10045.3.0.1");
    public static final DERObjectIdentifier c2pnb163v2 = new DERObjectIdentifier("1.2.840.10045.3.0.2");
    public static final DERObjectIdentifier c2pnb163v3 = new DERObjectIdentifier("1.2.840.10045.3.0.3");
    public static final DERObjectIdentifier c2pnb176w1 = new DERObjectIdentifier("1.2.840.10045.3.0.4");
    public static final DERObjectIdentifier c2tnb191v1 = new DERObjectIdentifier("1.2.840.10045.3.0.5");
    public static final DERObjectIdentifier c2tnb191v2 = new DERObjectIdentifier("1.2.840.10045.3.0.6");
    public static final DERObjectIdentifier c2tnb191v3 = new DERObjectIdentifier("1.2.840.10045.3.0.7");
    public static final DERObjectIdentifier c2onb191v4 = new DERObjectIdentifier("1.2.840.10045.3.0.8");
    public static final DERObjectIdentifier c2onb191v5 = new DERObjectIdentifier("1.2.840.10045.3.0.9");
    public static final DERObjectIdentifier c2pnb208w1 = new DERObjectIdentifier("1.2.840.10045.3.0.10");
    public static final DERObjectIdentifier c2tnb239v1 = new DERObjectIdentifier("1.2.840.10045.3.0.11");
    public static final DERObjectIdentifier c2tnb239v2 = new DERObjectIdentifier("1.2.840.10045.3.0.12");
    public static final DERObjectIdentifier c2tnb239v3 = new DERObjectIdentifier("1.2.840.10045.3.0.13");
    public static final DERObjectIdentifier c2onb239v4 = new DERObjectIdentifier("1.2.840.10045.3.0.14");
    public static final DERObjectIdentifier c2onb239v5 = new DERObjectIdentifier("1.2.840.10045.3.0.15");
    public static final DERObjectIdentifier c2pnb272w1 = new DERObjectIdentifier("1.2.840.10045.3.0.16");
    public static final DERObjectIdentifier c2pnb304w1 = new DERObjectIdentifier("1.2.840.10045.3.0.17");
    public static final DERObjectIdentifier c2tnb359v1 = new DERObjectIdentifier("1.2.840.10045.3.0.18");
    public static final DERObjectIdentifier c2pnb368w1 = new DERObjectIdentifier("1.2.840.10045.3.0.19");
    public static final DERObjectIdentifier c2tnb431r1 = new DERObjectIdentifier("1.2.840.10045.3.0.20");
    public static final String primeCurve = "1.2.840.10045.3.1";
    public static final DERObjectIdentifier prime192v1 = new DERObjectIdentifier("1.2.840.10045.3.1.1");
    public static final DERObjectIdentifier prime192v2 = new DERObjectIdentifier("1.2.840.10045.3.1.2");
    public static final DERObjectIdentifier prime192v3 = new DERObjectIdentifier("1.2.840.10045.3.1.3");
    public static final DERObjectIdentifier prime239v1 = new DERObjectIdentifier("1.2.840.10045.3.1.4");
    public static final DERObjectIdentifier prime239v2 = new DERObjectIdentifier("1.2.840.10045.3.1.5");
    public static final DERObjectIdentifier prime239v3 = new DERObjectIdentifier("1.2.840.10045.3.1.6");
    public static final DERObjectIdentifier prime256v1 = new DERObjectIdentifier("1.2.840.10045.3.1.7");
    public static final DERObjectIdentifier dhpublicnumber = new DERObjectIdentifier("1.2.840.10046.2.1");
    public static final DERObjectIdentifier id_dsa = new DERObjectIdentifier("1.2.840.10040.4.1");
    public static final DERObjectIdentifier id_dsa_with_sha1 = new DERObjectIdentifier("1.2.840.10040.4.3");
    public static final DERObjectIdentifier x9_63_scheme = new DERObjectIdentifier("1.3.133.16.840.63.0");
    public static final DERObjectIdentifier dhSinglePass_stdDH_sha1kdf_scheme = new DERObjectIdentifier(X9ObjectIdentifiers.x9_63_scheme + ".2");
    public static final DERObjectIdentifier dhSinglePass_cofactorDH_sha1kdf_scheme = new DERObjectIdentifier(X9ObjectIdentifiers.x9_63_scheme + ".3");
    public static final DERObjectIdentifier mqvSinglePass_sha1kdf_scheme = new DERObjectIdentifier(X9ObjectIdentifiers.x9_63_scheme + ".16");
    public static final DERObjectIdentifier x9_42_schemes = new DERObjectIdentifier("1.2.840.10046.3");
    public static final DERObjectIdentifier dhStatic = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".1");
    public static final DERObjectIdentifier dhEphem = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".2");
    public static final DERObjectIdentifier dhOneFlow = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".3");
    public static final DERObjectIdentifier dhHybrid1 = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".4");
    public static final DERObjectIdentifier dhHybrid2 = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".5");
    public static final DERObjectIdentifier dhHybridOneFlow = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".6");
    public static final DERObjectIdentifier mqv2 = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".7");
    public static final DERObjectIdentifier mqv1 = new DERObjectIdentifier(X9ObjectIdentifiers.x9_42_schemes + ".8");
}
