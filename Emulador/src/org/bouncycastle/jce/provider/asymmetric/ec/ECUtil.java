// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import java.security.PrivateKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import java.security.InvalidKeyException;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import java.security.PublicKey;

public class ECUtil
{
    static int[] convertMidTerms(final int[] array) {
        final int[] array2 = new int[3];
        if (array.length == 1) {
            array2[0] = array[0];
        }
        else {
            if (array.length != 3) {
                throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
            }
            if (array[0] < array[1] && array[0] < array[2]) {
                array2[0] = array[0];
                if (array[1] < array[2]) {
                    array2[1] = array[1];
                    array2[2] = array[2];
                }
                else {
                    array2[1] = array[2];
                    array2[2] = array[1];
                }
            }
            else if (array[1] < array[2]) {
                array2[0] = array[1];
                if (array[0] < array[2]) {
                    array2[1] = array[0];
                    array2[2] = array[2];
                }
                else {
                    array2[1] = array[2];
                    array2[2] = array[0];
                }
            }
            else {
                array2[0] = array[2];
                if (array[0] < array[1]) {
                    array2[1] = array[0];
                    array2[2] = array[1];
                }
                else {
                    array2[1] = array[1];
                    array2[2] = array[0];
                }
            }
        }
        return array2;
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof ECPublicKey) {
            final ECPublicKey ecPublicKey = (ECPublicKey)publicKey;
            final ECParameterSpec parameters = ecPublicKey.getParameters();
            if (parameters == null) {
                final ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
                return new ECPublicKeyParameters(((JCEECPublicKey)ecPublicKey).engineGetQ(), new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed()));
            }
            return new ECPublicKeyParameters(ecPublicKey.getQ(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
        }
        else {
            if (publicKey instanceof java.security.interfaces.ECPublicKey) {
                final java.security.interfaces.ECPublicKey ecPublicKey2 = (java.security.interfaces.ECPublicKey)publicKey;
                final ECParameterSpec convertSpec = EC5Util.convertSpec(ecPublicKey2.getParams(), false);
                return new ECPublicKeyParameters(EC5Util.convertPoint(ecPublicKey2.getParams(), ecPublicKey2.getW(), false), new ECDomainParameters(convertSpec.getCurve(), convertSpec.getG(), convertSpec.getN(), convertSpec.getH(), convertSpec.getSeed()));
            }
            throw new InvalidKeyException("cannot identify EC public key.");
        }
    }
    
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof ECPrivateKey) {
            final ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
            ECParameterSpec ecParameterSpec = ecPrivateKey.getParameters();
            if (ecParameterSpec == null) {
                ecParameterSpec = ProviderUtil.getEcImplicitlyCa();
            }
            return new ECPrivateKeyParameters(ecPrivateKey.getD(), new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN(), ecParameterSpec.getH(), ecParameterSpec.getSeed()));
        }
        throw new InvalidKeyException("can't identify EC private key.");
    }
    
    public static DERObjectIdentifier getNamedCurveOid(final String s) {
        DERObjectIdentifier derObjectIdentifier = X962NamedCurves.getOID(s);
        if (derObjectIdentifier == null) {
            derObjectIdentifier = SECNamedCurves.getOID(s);
            if (derObjectIdentifier == null) {
                derObjectIdentifier = NISTNamedCurves.getOID(s);
            }
            if (derObjectIdentifier == null) {
                derObjectIdentifier = TeleTrusTNamedCurves.getOID(s);
            }
            if (derObjectIdentifier == null) {
                derObjectIdentifier = ECGOST3410NamedCurves.getOID(s);
            }
        }
        return derObjectIdentifier;
    }
    
    public static X9ECParameters getNamedCurveByOid(final DERObjectIdentifier derObjectIdentifier) {
        X9ECParameters x9ECParameters = X962NamedCurves.getByOID(derObjectIdentifier);
        if (x9ECParameters == null) {
            x9ECParameters = SECNamedCurves.getByOID(derObjectIdentifier);
            if (x9ECParameters == null) {
                x9ECParameters = NISTNamedCurves.getByOID(derObjectIdentifier);
            }
            if (x9ECParameters == null) {
                x9ECParameters = TeleTrusTNamedCurves.getByOID(derObjectIdentifier);
            }
        }
        return x9ECParameters;
    }
    
    public static String getCurveName(final DERObjectIdentifier derObjectIdentifier) {
        String s = X962NamedCurves.getName(derObjectIdentifier);
        if (s == null) {
            s = SECNamedCurves.getName(derObjectIdentifier);
            if (s == null) {
                s = NISTNamedCurves.getName(derObjectIdentifier);
            }
            if (s == null) {
                s = TeleTrusTNamedCurves.getName(derObjectIdentifier);
            }
            if (s == null) {
                s = ECGOST3410NamedCurves.getName(derObjectIdentifier);
            }
        }
        return s;
    }
}
