// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.security.spec.RSAPrivateCrtKeySpec;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;

public class JCERSAPrivateCrtKey extends JCERSAPrivateKey implements RSAPrivateCrtKey
{
    static final long serialVersionUID = 7834723820638524718L;
    private BigInteger publicExponent;
    private BigInteger primeP;
    private BigInteger primeQ;
    private BigInteger primeExponentP;
    private BigInteger primeExponentQ;
    private BigInteger crtCoefficient;
    
    JCERSAPrivateCrtKey(final RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters) {
        super(rsaPrivateCrtKeyParameters);
        this.publicExponent = rsaPrivateCrtKeyParameters.getPublicExponent();
        this.primeP = rsaPrivateCrtKeyParameters.getP();
        this.primeQ = rsaPrivateCrtKeyParameters.getQ();
        this.primeExponentP = rsaPrivateCrtKeyParameters.getDP();
        this.primeExponentQ = rsaPrivateCrtKeyParameters.getDQ();
        this.crtCoefficient = rsaPrivateCrtKeyParameters.getQInv();
    }
    
    JCERSAPrivateCrtKey(final RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec) {
        this.modulus = rsaPrivateCrtKeySpec.getModulus();
        this.publicExponent = rsaPrivateCrtKeySpec.getPublicExponent();
        this.privateExponent = rsaPrivateCrtKeySpec.getPrivateExponent();
        this.primeP = rsaPrivateCrtKeySpec.getPrimeP();
        this.primeQ = rsaPrivateCrtKeySpec.getPrimeQ();
        this.primeExponentP = rsaPrivateCrtKeySpec.getPrimeExponentP();
        this.primeExponentQ = rsaPrivateCrtKeySpec.getPrimeExponentQ();
        this.crtCoefficient = rsaPrivateCrtKeySpec.getCrtCoefficient();
    }
    
    JCERSAPrivateCrtKey(final RSAPrivateCrtKey rsaPrivateCrtKey) {
        this.modulus = rsaPrivateCrtKey.getModulus();
        this.publicExponent = rsaPrivateCrtKey.getPublicExponent();
        this.privateExponent = rsaPrivateCrtKey.getPrivateExponent();
        this.primeP = rsaPrivateCrtKey.getPrimeP();
        this.primeQ = rsaPrivateCrtKey.getPrimeQ();
        this.primeExponentP = rsaPrivateCrtKey.getPrimeExponentP();
        this.primeExponentQ = rsaPrivateCrtKey.getPrimeExponentQ();
        this.crtCoefficient = rsaPrivateCrtKey.getCrtCoefficient();
    }
    
    JCERSAPrivateCrtKey(final PrivateKeyInfo privateKeyInfo) {
        this(new RSAPrivateKeyStructure((ASN1Sequence)privateKeyInfo.getPrivateKey()));
    }
    
    JCERSAPrivateCrtKey(final RSAPrivateKeyStructure rsaPrivateKeyStructure) {
        this.modulus = rsaPrivateKeyStructure.getModulus();
        this.publicExponent = rsaPrivateKeyStructure.getPublicExponent();
        this.privateExponent = rsaPrivateKeyStructure.getPrivateExponent();
        this.primeP = rsaPrivateKeyStructure.getPrime1();
        this.primeQ = rsaPrivateKeyStructure.getPrime2();
        this.primeExponentP = rsaPrivateKeyStructure.getExponent1();
        this.primeExponentQ = rsaPrivateKeyStructure.getExponent2();
        this.crtCoefficient = rsaPrivateKeyStructure.getCoefficient();
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPrivateKeyStructure(this.getModulus(), this.getPublicExponent(), this.getPrivateExponent(), this.getPrimeP(), this.getPrimeQ(), this.getPrimeExponentP(), this.getPrimeExponentQ(), this.getCrtCoefficient()).getDERObject()).getDEREncoded();
    }
    
    @Override
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public BigInteger getPrimeP() {
        return this.primeP;
    }
    
    @Override
    public BigInteger getPrimeQ() {
        return this.primeQ;
    }
    
    @Override
    public BigInteger getPrimeExponentP() {
        return this.primeExponentP;
    }
    
    @Override
    public BigInteger getPrimeExponentQ() {
        return this.primeExponentQ;
    }
    
    @Override
    public BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode() ^ this.getPrivateExponent().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RSAPrivateCrtKey)) {
            return false;
        }
        final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)o;
        return this.getModulus().equals(rsaPrivateCrtKey.getModulus()) && this.getPublicExponent().equals(rsaPrivateCrtKey.getPublicExponent()) && this.getPrivateExponent().equals(rsaPrivateCrtKey.getPrivateExponent()) && this.getPrimeP().equals(rsaPrivateCrtKey.getPrimeP()) && this.getPrimeQ().equals(rsaPrivateCrtKey.getPrimeQ()) && this.getPrimeExponentP().equals(rsaPrivateCrtKey.getPrimeExponentP()) && this.getPrimeExponentQ().equals(rsaPrivateCrtKey.getPrimeExponentQ()) && this.getCrtCoefficient().equals(rsaPrivateCrtKey.getCrtCoefficient());
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("RSA Private CRT Key").append(property);
        sb.append("            modulus: ").append(this.getModulus().toString(16)).append(property);
        sb.append("    public exponent: ").append(this.getPublicExponent().toString(16)).append(property);
        sb.append("   private exponent: ").append(this.getPrivateExponent().toString(16)).append(property);
        sb.append("             primeP: ").append(this.getPrimeP().toString(16)).append(property);
        sb.append("             primeQ: ").append(this.getPrimeQ().toString(16)).append(property);
        sb.append("     primeExponentP: ").append(this.getPrimeExponentP().toString(16)).append(property);
        sb.append("     primeExponentQ: ").append(this.getPrimeExponentQ().toString(16)).append(property);
        sb.append("     crtCoefficient: ").append(this.getCrtCoefficient().toString(16)).append(property);
        return sb.toString();
    }
}
