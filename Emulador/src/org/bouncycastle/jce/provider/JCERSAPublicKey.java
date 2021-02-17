// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

public class JCERSAPublicKey implements RSAPublicKey
{
    static final long serialVersionUID = 2675817738516720772L;
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    JCERSAPublicKey(final RSAKeyParameters rsaKeyParameters) {
        this.modulus = rsaKeyParameters.getModulus();
        this.publicExponent = rsaKeyParameters.getExponent();
    }
    
    JCERSAPublicKey(final RSAPublicKeySpec rsaPublicKeySpec) {
        this.modulus = rsaPublicKeySpec.getModulus();
        this.publicExponent = rsaPublicKeySpec.getPublicExponent();
    }
    
    JCERSAPublicKey(final RSAPublicKey rsaPublicKey) {
        this.modulus = rsaPublicKey.getModulus();
        this.publicExponent = rsaPublicKey.getPublicExponent();
    }
    
    JCERSAPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            final RSAPublicKeyStructure rsaPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)subjectPublicKeyInfo.getPublicKey());
            this.modulus = rsaPublicKeyStructure.getModulus();
            this.publicExponent = rsaPublicKeyStructure.getPublicExponent();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }
    
    @Override
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    @Override
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public String getAlgorithm() {
        return "RSA";
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded() {
        return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPublicKeyStructure(this.getModulus(), this.getPublicExponent()).getDERObject()).getDEREncoded();
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RSAPublicKey)) {
            return false;
        }
        final RSAPublicKey rsaPublicKey = (RSAPublicKey)o;
        return this.getModulus().equals(rsaPublicKey.getModulus()) && this.getPublicExponent().equals(rsaPublicKey.getPublicExponent());
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("RSA Public Key").append(property);
        sb.append("            modulus: ").append(this.getModulus().toString(16)).append(property);
        sb.append("    public exponent: ").append(this.getPublicExponent().toString(16)).append(property);
        return sb.toString();
    }
}
