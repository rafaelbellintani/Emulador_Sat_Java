// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import java.security.spec.PKCS8EncodedKeySpec;
import java.io.UnsupportedEncodingException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import java.security.KeyFactory;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.PublicKey;

public class ECKeyUtil
{
    public static PublicKey publicToExplicitParameters(final PublicKey publicKey, final String s) throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException {
        final Provider provider = Security.getProvider(s);
        if (provider == null) {
            throw new NoSuchProviderException("cannot find provider: " + s);
        }
        return publicToExplicitParameters(publicKey, provider);
    }
    
    public static PublicKey publicToExplicitParameters(final PublicKey publicKey, final Provider provider) throws IllegalArgumentException, NoSuchAlgorithmException {
        try {
            final SubjectPublicKeyInfo instance = SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(publicKey.getEncoded()));
            if (instance.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                throw new IllegalArgumentException("cannot convert GOST key to explicit parameters.");
            }
            final X962Parameters x962Parameters = new X962Parameters((DERObject)instance.getAlgorithmId().getParameters());
            X9ECParameters x9ECParameters;
            if (x962Parameters.isNamedCurve()) {
                final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid((DERObjectIdentifier)x962Parameters.getParameters());
                x9ECParameters = new X9ECParameters(namedCurveByOid.getCurve(), namedCurveByOid.getG(), namedCurveByOid.getN(), namedCurveByOid.getH());
            }
            else {
                if (!x962Parameters.isImplicitlyCA()) {
                    return publicKey;
                }
                x9ECParameters = new X9ECParameters(ProviderUtil.getEcImplicitlyCa().getCurve(), ProviderUtil.getEcImplicitlyCa().getG(), ProviderUtil.getEcImplicitlyCa().getN(), ProviderUtil.getEcImplicitlyCa().getH());
            }
            return KeyFactory.getInstance(publicKey.getAlgorithm(), provider).generatePublic(new X509EncodedKeySpec(new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(x9ECParameters).getDERObject()), instance.getPublicKeyData().getBytes()).getEncoded()));
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
        catch (NoSuchAlgorithmException ex2) {
            throw ex2;
        }
        catch (Exception ex3) {
            throw new UnexpectedException(ex3);
        }
    }
    
    public static PrivateKey privateToExplicitParameters(final PrivateKey privateKey, final String s) throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException {
        final Provider provider = Security.getProvider(s);
        if (provider == null) {
            throw new NoSuchProviderException("cannot find provider: " + s);
        }
        return privateToExplicitParameters(privateKey, provider);
    }
    
    public static PrivateKey privateToExplicitParameters(final PrivateKey privateKey, final Provider provider) throws IllegalArgumentException, NoSuchAlgorithmException {
        try {
            final PrivateKeyInfo instance = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(privateKey.getEncoded()));
            if (instance.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                throw new UnsupportedEncodingException("cannot convert GOST key to explicit parameters.");
            }
            final X962Parameters x962Parameters = new X962Parameters((DERObject)instance.getAlgorithmId().getParameters());
            X9ECParameters x9ECParameters;
            if (x962Parameters.isNamedCurve()) {
                final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid((DERObjectIdentifier)x962Parameters.getParameters());
                x9ECParameters = new X9ECParameters(namedCurveByOid.getCurve(), namedCurveByOid.getG(), namedCurveByOid.getN(), namedCurveByOid.getH());
            }
            else {
                if (!x962Parameters.isImplicitlyCA()) {
                    return privateKey;
                }
                x9ECParameters = new X9ECParameters(ProviderUtil.getEcImplicitlyCa().getCurve(), ProviderUtil.getEcImplicitlyCa().getG(), ProviderUtil.getEcImplicitlyCa().getN(), ProviderUtil.getEcImplicitlyCa().getH());
            }
            return KeyFactory.getInstance(privateKey.getAlgorithm(), provider).generatePrivate(new PKCS8EncodedKeySpec(new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(x9ECParameters).getDERObject()), instance.getPrivateKey()).getEncoded()));
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
        catch (NoSuchAlgorithmException ex2) {
            throw ex2;
        }
        catch (Exception ex3) {
            throw new UnexpectedException(ex3);
        }
    }
    
    private static class UnexpectedException extends RuntimeException
    {
        private Throwable cause;
        
        UnexpectedException(final Throwable cause) {
            super(cause.toString());
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
