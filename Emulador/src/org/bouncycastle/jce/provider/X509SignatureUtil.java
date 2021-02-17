// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.io.IOException;
import java.security.SignatureException;
import java.security.AlgorithmParameters;
import org.bouncycastle.asn1.DEREncodable;
import java.security.Signature;
import org.bouncycastle.asn1.ASN1Null;

class X509SignatureUtil
{
    private static final ASN1Null derNull;
    
    static void setSignatureParameters(final Signature signature, final DEREncodable derEncodable) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (derEncodable != null && !X509SignatureUtil.derNull.equals(derEncodable)) {
            final AlgorithmParameters instance = AlgorithmParameters.getInstance(signature.getAlgorithm(), signature.getProvider());
            try {
                instance.init(derEncodable.getDERObject().getDEREncoded());
            }
            catch (IOException ex) {
                throw new SignatureException("IOException decoding parameters: " + ex.getMessage());
            }
            if (signature.getAlgorithm().endsWith("MGF1")) {
                try {
                    signature.setParameter(instance.getParameterSpec(PSSParameterSpec.class));
                }
                catch (GeneralSecurityException ex2) {
                    throw new SignatureException("Exception extracting parameters: " + ex2.getMessage());
                }
            }
        }
    }
    
    static String getSignatureName(final AlgorithmIdentifier algorithmIdentifier) {
        final DEREncodable parameters = algorithmIdentifier.getParameters();
        if (parameters != null && !X509SignatureUtil.derNull.equals(parameters)) {
            if (algorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
                return getDigestAlgName(RSASSAPSSparams.getInstance(parameters).getHashAlgorithm().getObjectId()) + "withRSAandMGF1";
            }
            if (algorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.ecdsa_with_SHA2)) {
                return getDigestAlgName((DERObjectIdentifier)ASN1Sequence.getInstance(parameters).getObjectAt(0)) + "withECDSA";
            }
        }
        return algorithmIdentifier.getObjectId().getId();
    }
    
    private static String getDigestAlgName(final DERObjectIdentifier derObjectIdentifier) {
        if (PKCSObjectIdentifiers.md5.equals(derObjectIdentifier)) {
            return "MD5";
        }
        if (OIWObjectIdentifiers.idSHA1.equals(derObjectIdentifier)) {
            return "SHA1";
        }
        if (NISTObjectIdentifiers.id_sha224.equals(derObjectIdentifier)) {
            return "SHA224";
        }
        if (NISTObjectIdentifiers.id_sha256.equals(derObjectIdentifier)) {
            return "SHA256";
        }
        if (NISTObjectIdentifiers.id_sha384.equals(derObjectIdentifier)) {
            return "SHA384";
        }
        if (NISTObjectIdentifiers.id_sha512.equals(derObjectIdentifier)) {
            return "SHA512";
        }
        if (TeleTrusTObjectIdentifiers.ripemd128.equals(derObjectIdentifier)) {
            return "RIPEMD128";
        }
        if (TeleTrusTObjectIdentifiers.ripemd160.equals(derObjectIdentifier)) {
            return "RIPEMD160";
        }
        if (TeleTrusTObjectIdentifiers.ripemd256.equals(derObjectIdentifier)) {
            return "RIPEMD256";
        }
        if (CryptoProObjectIdentifiers.gostR3411.equals(derObjectIdentifier)) {
            return "GOST3411";
        }
        return derObjectIdentifier.getId();
    }
    
    static {
        derNull = new DERNull();
    }
}
