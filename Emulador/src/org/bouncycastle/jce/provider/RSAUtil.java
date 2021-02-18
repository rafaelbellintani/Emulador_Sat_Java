// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.security.interfaces.RSAPublicKey;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DERObjectIdentifier;

class RSAUtil
{
    static boolean isRsaOid(final DERObjectIdentifier derObjectIdentifier) {
        return derObjectIdentifier.equals(PKCSObjectIdentifiers.rsaEncryption) || derObjectIdentifier.equals(X509ObjectIdentifiers.id_ea_rsa) || derObjectIdentifier.equals(PKCSObjectIdentifiers.id_RSASSA_PSS) || derObjectIdentifier.equals(PKCSObjectIdentifiers.id_RSAES_OAEP);
    }
    
    static RSAKeyParameters generatePublicKeyParameter(final RSAPublicKey rsaPublicKey) {
        return new RSAKeyParameters(false, rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
    }
    
    static RSAKeyParameters generatePrivateKeyParameter(final RSAPrivateKey rsaPrivateKey) {
        if (rsaPrivateKey instanceof RSAPrivateCrtKey) {
            final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)rsaPrivateKey;
            return new RSAPrivateCrtKeyParameters(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient());
        }
        return new RSAKeyParameters(true, rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
    }
}
