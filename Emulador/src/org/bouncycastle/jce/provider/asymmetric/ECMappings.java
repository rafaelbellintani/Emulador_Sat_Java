// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import java.util.HashMap;

public class ECMappings extends HashMap
{
    public ECMappings() {
        this.put("KeyAgreement.ECDH", "org.bouncycastle.jce.provider.asymmetric.ec.KeyAgreement$DH");
        this.put("KeyAgreement.ECDHC", "org.bouncycastle.jce.provider.asymmetric.ec.KeyAgreement$DHC");
        this.put("KeyAgreement.ECMQV", "org.bouncycastle.jce.provider.asymmetric.ec.KeyAgreement$MQV");
        this.put("KeyAgreement." + X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "org.bouncycastle.jce.provider.asymmetric.ec.KeyAgreement$DHwithSHA1KDF");
        this.put("KeyAgreement." + X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "org.bouncycastle.jce.provider.asymmetric.ec.KeyAgreement$MQVwithSHA1KDF");
        this.put("KeyFactory.EC", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$EC");
        this.put("KeyFactory.ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$ECDSA");
        this.put("KeyFactory.ECDH", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$ECDH");
        this.put("KeyFactory.ECDHC", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$ECDHC");
        this.put("KeyFactory.ECMQV", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$ECMQV");
        this.put("Alg.Alias.KeyFactory." + X9ObjectIdentifiers.id_ecPublicKey, "EC");
        this.put("Alg.Alias.KeyFactory." + X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "EC");
        this.put("Alg.Alias.KeyFactory." + X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "ECMQV");
        this.put("KeyFactory.ECGOST3410", "org.bouncycastle.jce.provider.asymmetric.ec.KeyFactory$ECGOST3410");
        this.put("Alg.Alias.KeyFactory.GOST-3410-2001", "ECGOST3410");
        this.put("Alg.Alias.KeyFactory.ECGOST-3410", "ECGOST3410");
        this.put("Alg.Alias.KeyFactory." + CryptoProObjectIdentifiers.gostR3410_2001, "ECGOST3410");
        this.put("KeyPairGenerator.EC", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$EC");
        this.put("KeyPairGenerator.ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECDSA");
        this.put("KeyPairGenerator.ECDH", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECDH");
        this.put("KeyPairGenerator.ECDHC", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECDHC");
        this.put("KeyPairGenerator.ECIES", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECDH");
        this.put("KeyPairGenerator.ECMQV", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECMQV");
        this.put("Alg.Alias.KeyPairGenerator." + X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "EC");
        this.put("Alg.Alias.KeyPairGenerator." + X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "ECMQV");
        this.put("KeyPairGenerator.ECGOST3410", "org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator$ECGOST3410");
        this.put("Alg.Alias.KeyPairGenerator.ECGOST-3410", "ECGOST3410");
        this.put("Alg.Alias.KeyPairGenerator.GOST-3410-2001", "ECGOST3410");
        this.put("Signature.ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSA");
        this.put("Signature.NONEwithECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSAnone");
        this.put("Alg.Alias.Signature.SHA1withECDSA", "ECDSA");
        this.put("Alg.Alias.Signature.ECDSAwithSHA1", "ECDSA");
        this.put("Alg.Alias.Signature.SHA1WITHECDSA", "ECDSA");
        this.put("Alg.Alias.Signature.ECDSAWITHSHA1", "ECDSA");
        this.put("Alg.Alias.Signature.SHA1WithECDSA", "ECDSA");
        this.put("Alg.Alias.Signature.ECDSAWithSHA1", "ECDSA");
        this.put("Alg.Alias.Signature.1.2.840.10045.4.1", "ECDSA");
        this.put("Alg.Alias.Signature." + TeleTrusTObjectIdentifiers.ecSignWithSha1, "ECDSA");
        this.addSignatureAlgorithm("SHA224", "ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSA224", X9ObjectIdentifiers.ecdsa_with_SHA224);
        this.addSignatureAlgorithm("SHA256", "ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSA256", X9ObjectIdentifiers.ecdsa_with_SHA256);
        this.addSignatureAlgorithm("SHA384", "ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSA384", X9ObjectIdentifiers.ecdsa_with_SHA384);
        this.addSignatureAlgorithm("SHA512", "ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSA512", X9ObjectIdentifiers.ecdsa_with_SHA512);
        this.addSignatureAlgorithm("RIPEMD160", "ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecDSARipeMD160", TeleTrusTObjectIdentifiers.ecSignWithRipemd160);
        this.put("Signature.SHA1WITHECNR", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecNR");
        this.put("Signature.SHA224WITHECNR", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecNR224");
        this.put("Signature.SHA256WITHECNR", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecNR256");
        this.put("Signature.SHA384WITHECNR", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecNR384");
        this.put("Signature.SHA512WITHECNR", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecNR512");
        this.addSignatureAlgorithm("SHA1", "CVC-ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecCVCDSA", EACObjectIdentifiers.id_TA_ECDSA_SHA_1);
        this.addSignatureAlgorithm("SHA224", "CVC-ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecCVCDSA224", EACObjectIdentifiers.id_TA_ECDSA_SHA_224);
        this.addSignatureAlgorithm("SHA256", "CVC-ECDSA", "org.bouncycastle.jce.provider.asymmetric.ec.Signature$ecCVCDSA256", EACObjectIdentifiers.id_TA_ECDSA_SHA_256);
    }
    
    private void addSignatureAlgorithm(final String s, final String s2, final String value, final DERObjectIdentifier derObjectIdentifier) {
        final String string = s + "WITH" + s2;
        final String string2 = s + "with" + s2;
        final String string3 = s + "With" + s2;
        final String string4 = s + "/" + s2;
        this.put("Signature." + string, value);
        this.put("Alg.Alias.Signature." + string2, string);
        this.put("Alg.Alias.Signature." + string3, string);
        this.put("Alg.Alias.Signature." + string4, string);
        this.put("Alg.Alias.Signature." + derObjectIdentifier, string);
        this.put("Alg.Alias.Signature.OID." + derObjectIdentifier, string);
    }
}
