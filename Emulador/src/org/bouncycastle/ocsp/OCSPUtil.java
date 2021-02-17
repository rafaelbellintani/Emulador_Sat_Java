// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.util.HashSet;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.Signature;
import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.Set;
import java.util.Hashtable;

class OCSPUtil
{
    private static Hashtable algorithms;
    private static Hashtable oids;
    private static Set noParams;
    
    static DERObjectIdentifier getAlgorithmOID(String upperCase) {
        upperCase = Strings.toUpperCase(upperCase);
        if (OCSPUtil.algorithms.containsKey(upperCase)) {
            return OCSPUtil.algorithms.get(upperCase);
        }
        return new DERObjectIdentifier(upperCase);
    }
    
    static String getAlgorithmName(final DERObjectIdentifier derObjectIdentifier) {
        if (OCSPUtil.oids.containsKey(derObjectIdentifier)) {
            return OCSPUtil.oids.get(derObjectIdentifier);
        }
        return derObjectIdentifier.getId();
    }
    
    static AlgorithmIdentifier getSigAlgID(final DERObjectIdentifier derObjectIdentifier) {
        if (OCSPUtil.noParams.contains(derObjectIdentifier)) {
            return new AlgorithmIdentifier(derObjectIdentifier);
        }
        return new AlgorithmIdentifier(derObjectIdentifier, new DERNull());
    }
    
    static Iterator getAlgNames() {
        final Enumeration<Object> keys = OCSPUtil.algorithms.keys();
        final ArrayList<Object> list = new ArrayList<Object>();
        while (keys.hasMoreElements()) {
            list.add(keys.nextElement());
        }
        return list.iterator();
    }
    
    static CertStore createCertStoreInstance(final String s, final CertStoreParameters certStoreParameters, final String provider) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        if (provider == null) {
            return CertStore.getInstance(s, certStoreParameters);
        }
        return CertStore.getInstance(s, certStoreParameters, provider);
    }
    
    static MessageDigest createDigestInstance(final String s, final String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider == null) {
            return MessageDigest.getInstance(s);
        }
        return MessageDigest.getInstance(s, provider);
    }
    
    static Signature createSignatureInstance(final String s, final String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider == null) {
            return Signature.getInstance(s);
        }
        return Signature.getInstance(s, provider);
    }
    
    static CertificateFactory createX509CertificateFactory(final String provider) throws CertificateException, NoSuchProviderException {
        if (provider == null) {
            return CertificateFactory.getInstance("X.509");
        }
        return CertificateFactory.getInstance("X.509", provider);
    }
    
    static {
        OCSPUtil.algorithms = new Hashtable();
        OCSPUtil.oids = new Hashtable();
        OCSPUtil.noParams = new HashSet();
        OCSPUtil.algorithms.put("MD2WITHRSAENCRYPTION", PKCSObjectIdentifiers.md2WithRSAEncryption);
        OCSPUtil.algorithms.put("MD2WITHRSA", PKCSObjectIdentifiers.md2WithRSAEncryption);
        OCSPUtil.algorithms.put("MD5WITHRSAENCRYPTION", PKCSObjectIdentifiers.md5WithRSAEncryption);
        OCSPUtil.algorithms.put("MD5WITHRSA", PKCSObjectIdentifiers.md5WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA1WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha1WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA1WITHRSA", PKCSObjectIdentifiers.sha1WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA224WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha224WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA224WITHRSA", PKCSObjectIdentifiers.sha224WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA256WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA256WITHRSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA384WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha384WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA384WITHRSA", PKCSObjectIdentifiers.sha384WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA512WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        OCSPUtil.algorithms.put("SHA512WITHRSA", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        OCSPUtil.algorithms.put("RIPEMD160WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        OCSPUtil.algorithms.put("RIPEMD160WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        OCSPUtil.algorithms.put("RIPEMD128WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        OCSPUtil.algorithms.put("RIPEMD128WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        OCSPUtil.algorithms.put("RIPEMD256WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        OCSPUtil.algorithms.put("RIPEMD256WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        OCSPUtil.algorithms.put("SHA1WITHDSA", X9ObjectIdentifiers.id_dsa_with_sha1);
        OCSPUtil.algorithms.put("DSAWITHSHA1", X9ObjectIdentifiers.id_dsa_with_sha1);
        OCSPUtil.algorithms.put("SHA224WITHDSA", NISTObjectIdentifiers.dsa_with_sha224);
        OCSPUtil.algorithms.put("SHA256WITHDSA", NISTObjectIdentifiers.dsa_with_sha256);
        OCSPUtil.algorithms.put("SHA1WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA1);
        OCSPUtil.algorithms.put("ECDSAWITHSHA1", X9ObjectIdentifiers.ecdsa_with_SHA1);
        OCSPUtil.algorithms.put("SHA224WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA224);
        OCSPUtil.algorithms.put("SHA256WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA256);
        OCSPUtil.algorithms.put("SHA384WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA384);
        OCSPUtil.algorithms.put("SHA512WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA512);
        OCSPUtil.algorithms.put("GOST3411WITHGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        OCSPUtil.algorithms.put("GOST3411WITHGOST3410-94", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        OCSPUtil.oids.put(PKCSObjectIdentifiers.md2WithRSAEncryption, "MD2WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.md5WithRSAEncryption, "MD5WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, "SHA1WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384WITHRSA");
        OCSPUtil.oids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512WITHRSA");
        OCSPUtil.oids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160, "RIPEMD160WITHRSA");
        OCSPUtil.oids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128, "RIPEMD128WITHRSA");
        OCSPUtil.oids.put(TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256, "RIPEMD256WITHRSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.id_dsa_with_sha1, "SHA1WITHDSA");
        OCSPUtil.oids.put(NISTObjectIdentifiers.dsa_with_sha224, "SHA224WITHDSA");
        OCSPUtil.oids.put(NISTObjectIdentifiers.dsa_with_sha256, "SHA256WITHDSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1WITHECDSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224WITHECDSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256WITHECDSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384WITHECDSA");
        OCSPUtil.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512WITHECDSA");
        OCSPUtil.oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3411WITHGOST3410");
        OCSPUtil.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA1);
        OCSPUtil.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA224);
        OCSPUtil.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA256);
        OCSPUtil.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA384);
        OCSPUtil.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA512);
        OCSPUtil.noParams.add(X9ObjectIdentifiers.id_dsa_with_sha1);
        OCSPUtil.noParams.add(NISTObjectIdentifiers.dsa_with_sha224);
        OCSPUtil.noParams.add(NISTObjectIdentifiers.dsa_with_sha256);
    }
}
