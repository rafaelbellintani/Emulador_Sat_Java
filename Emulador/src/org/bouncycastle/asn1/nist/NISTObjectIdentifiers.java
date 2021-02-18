// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.nist;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface NISTObjectIdentifiers
{
    public static final String nistAlgorithm = "2.16.840.1.101.3.4";
    public static final DERObjectIdentifier id_sha256 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.1");
    public static final DERObjectIdentifier id_sha384 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.2");
    public static final DERObjectIdentifier id_sha512 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.3");
    public static final DERObjectIdentifier id_sha224 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.4");
    public static final String aes = "2.16.840.1.101.3.4.1";
    public static final DERObjectIdentifier id_aes128_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.1");
    public static final DERObjectIdentifier id_aes128_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.2");
    public static final DERObjectIdentifier id_aes128_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.3");
    public static final DERObjectIdentifier id_aes128_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.4");
    public static final DERObjectIdentifier id_aes128_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.5");
    public static final DERObjectIdentifier id_aes128_GCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.6");
    public static final DERObjectIdentifier id_aes128_CCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.7");
    public static final DERObjectIdentifier id_aes192_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.21");
    public static final DERObjectIdentifier id_aes192_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.22");
    public static final DERObjectIdentifier id_aes192_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.23");
    public static final DERObjectIdentifier id_aes192_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.24");
    public static final DERObjectIdentifier id_aes192_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.25");
    public static final DERObjectIdentifier id_aes192_GCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.26");
    public static final DERObjectIdentifier id_aes192_CCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.27");
    public static final DERObjectIdentifier id_aes256_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.41");
    public static final DERObjectIdentifier id_aes256_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.42");
    public static final DERObjectIdentifier id_aes256_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.43");
    public static final DERObjectIdentifier id_aes256_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.44");
    public static final DERObjectIdentifier id_aes256_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.45");
    public static final DERObjectIdentifier id_aes256_GCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.46");
    public static final DERObjectIdentifier id_aes256_CCM = new DERObjectIdentifier("2.16.840.1.101.3.4.1.47");
    public static final DERObjectIdentifier id_dsa_with_sha2 = new DERObjectIdentifier("2.16.840.1.101.3.4.3");
    public static final DERObjectIdentifier dsa_with_sha224 = new DERObjectIdentifier(NISTObjectIdentifiers.id_dsa_with_sha2 + ".1");
    public static final DERObjectIdentifier dsa_with_sha256 = new DERObjectIdentifier(NISTObjectIdentifiers.id_dsa_with_sha2 + ".2");
    public static final DERObjectIdentifier dsa_with_sha384 = new DERObjectIdentifier(NISTObjectIdentifiers.id_dsa_with_sha2 + ".3");
    public static final DERObjectIdentifier dsa_with_sha512 = new DERObjectIdentifier(NISTObjectIdentifiers.id_dsa_with_sha2 + ".4");
}
