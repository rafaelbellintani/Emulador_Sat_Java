// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import java.security.interfaces.DSAPrivateKey;
import java.security.PrivateKey;
import java.security.InvalidKeyException;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import java.security.interfaces.DSAPublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import java.security.PublicKey;

public class DSAUtil
{
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof DSAPublicKey) {
            final DSAPublicKey dsaPublicKey = (DSAPublicKey)publicKey;
            return new DSAPublicKeyParameters(dsaPublicKey.getY(), new DSAParameters(dsaPublicKey.getParams().getP(), dsaPublicKey.getParams().getQ(), dsaPublicKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify DSA public key: " + publicKey.getClass().getName());
    }
    
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof DSAPrivateKey) {
            final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)privateKey;
            return new DSAPrivateKeyParameters(dsaPrivateKey.getX(), new DSAParameters(dsaPrivateKey.getParams().getP(), dsaPrivateKey.getParams().getQ(), dsaPrivateKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify DSA private key.");
    }
}
