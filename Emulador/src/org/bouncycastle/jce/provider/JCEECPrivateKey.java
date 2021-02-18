// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import java.util.Enumeration;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.params.ECDomainParameters;
import java.security.spec.ECPoint;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jce.provider.asymmetric.ec.EC5Util;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.asn1.DERBitString;
import java.security.spec.ECParameterSpec;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import java.security.interfaces.ECPrivateKey;

public class JCEECPrivateKey implements ECPrivateKey, org.bouncycastle.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier, ECPointEncoder
{
    private String algorithm;
    private BigInteger d;
    private ECParameterSpec ecSpec;
    private boolean withCompression;
    private DERBitString publicKey;
    private PKCS12BagAttributeCarrierImpl attrCarrier;
    
    protected JCEECPrivateKey() {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    public JCEECPrivateKey(final ECPrivateKey ecPrivateKey) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKey.getS();
        this.algorithm = ecPrivateKey.getAlgorithm();
        this.ecSpec = ecPrivateKey.getParams();
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeySpec.getD();
        if (ecPrivateKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecPrivateKeySpec.getParams().getCurve(), ecPrivateKeySpec.getParams().getSeed()), ecPrivateKeySpec.getParams());
        }
        else {
            this.ecSpec = null;
        }
    }
    
    public JCEECPrivateKey(final String algorithm, final java.security.spec.ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeySpec.getS();
        this.ecSpec = ecPrivateKeySpec.getParams();
    }
    
    public JCEECPrivateKey(final String algorithm, final JCEECPrivateKey jceecPrivateKey) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = jceecPrivateKey.d;
        this.ecSpec = jceecPrivateKey.ecSpec;
        this.withCompression = jceecPrivateKey.withCompression;
        this.attrCarrier = jceecPrivateKey.attrCarrier;
        this.publicKey = jceecPrivateKey.publicKey;
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final JCEECPublicKey jceecPublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        if (ecSpec == null) {
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getX().toBigInteger(), parameters.getG().getY().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            this.ecSpec = ecSpec;
        }
        this.publicKey = this.getPublicKeyDetails(jceecPublicKey);
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final JCEECPublicKey jceecPublicKey, final org.bouncycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        if (ecParameterSpec == null) {
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getX().toBigInteger(), parameters.getG().getY().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), new ECPoint(ecParameterSpec.getG().getX().toBigInteger(), ecParameterSpec.getG().getY().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH().intValue());
        }
        this.publicKey = this.getPublicKeyDetails(jceecPublicKey);
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.ecSpec = null;
    }
    
    JCEECPrivateKey(final PrivateKeyInfo privateKeyInfo) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }
    
    private void populateFromPrivKeyInfo(final PrivateKeyInfo privateKeyInfo) {
        final X962Parameters x962Parameters = new X962Parameters((DERObject)privateKeyInfo.getAlgorithmId().getParameters());
        if (x962Parameters.isNamedCurve()) {
            final DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)x962Parameters.getParameters();
            final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(derObjectIdentifier);
            if (namedCurveByOid == null) {
                final ECDomainParameters byOID = ECGOST3410NamedCurves.getByOID(derObjectIdentifier);
                this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(derObjectIdentifier), EC5Util.convertCurve(byOID.getCurve(), byOID.getSeed()), new ECPoint(byOID.getG().getX().toBigInteger(), byOID.getG().getY().toBigInteger()), byOID.getN(), byOID.getH());
            }
            else {
                this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(derObjectIdentifier), EC5Util.convertCurve(namedCurveByOid.getCurve(), namedCurveByOid.getSeed()), new ECPoint(namedCurveByOid.getG().getX().toBigInteger(), namedCurveByOid.getG().getY().toBigInteger()), namedCurveByOid.getN(), namedCurveByOid.getH());
            }
        }
        else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
        }
        else {
            final X9ECParameters x9ECParameters = new X9ECParameters((ASN1Sequence)x962Parameters.getParameters());
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(x9ECParameters.getCurve(), x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getX().toBigInteger(), x9ECParameters.getG().getY().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
        }
        if (privateKeyInfo.getPrivateKey() instanceof DERInteger) {
            this.d = ((DERInteger)privateKeyInfo.getPrivateKey()).getValue();
        }
        else {
            final ECPrivateKeyStructure ecPrivateKeyStructure = new ECPrivateKeyStructure((ASN1Sequence)privateKeyInfo.getPrivateKey());
            this.d = ecPrivateKeyStructure.getKey();
            this.publicKey = ecPrivateKeyStructure.getPublicKey();
        }
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        X962Parameters x962Parameters;
        if (this.ecSpec instanceof ECNamedCurveSpec) {
            x962Parameters = new X962Parameters(ECUtil.getNamedCurveOid(((ECNamedCurveSpec)this.ecSpec).getName()));
        }
        else if (this.ecSpec == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
        }
        else {
            final ECCurve convertCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        ECPrivateKeyStructure ecPrivateKeyStructure;
        if (this.publicKey != null) {
            ecPrivateKeyStructure = new ECPrivateKeyStructure(this.getS(), this.publicKey, x962Parameters);
        }
        else {
            ecPrivateKeyStructure = new ECPrivateKeyStructure(this.getS(), x962Parameters);
        }
        PrivateKeyInfo privateKeyInfo;
        if (this.algorithm.equals("ECGOST3410")) {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, x962Parameters.getDERObject()), ecPrivateKeyStructure.getDERObject());
        }
        else {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.getDERObject()), ecPrivateKeyStructure.getDERObject());
        }
        return privateKeyInfo.getDEREncoded();
    }
    
    @Override
    public ECParameterSpec getParams() {
        return this.ecSpec;
    }
    
    @Override
    public org.bouncycastle.jce.spec.ECParameterSpec getParameters() {
        if (this.ecSpec == null) {
            return null;
        }
        return EC5Util.convertSpec(this.ecSpec, this.withCompression);
    }
    
    org.bouncycastle.jce.spec.ECParameterSpec engineGetSpec() {
        if (this.ecSpec != null) {
            return EC5Util.convertSpec(this.ecSpec, this.withCompression);
        }
        return ProviderUtil.getEcImplicitlyCa();
    }
    
    @Override
    public BigInteger getS() {
        return this.d;
    }
    
    @Override
    public BigInteger getD() {
        return this.d;
    }
    
    @Override
    public void setBagAttribute(final DERObjectIdentifier derObjectIdentifier, final DEREncodable derEncodable) {
        this.attrCarrier.setBagAttribute(derObjectIdentifier, derEncodable);
    }
    
    @Override
    public DEREncodable getBagAttribute(final DERObjectIdentifier derObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(derObjectIdentifier);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }
    
    @Override
    public void setPointFormat(final String anotherString) {
        this.withCompression = !"UNCOMPRESSED".equalsIgnoreCase(anotherString);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof JCEECPrivateKey)) {
            return false;
        }
        final JCEECPrivateKey jceecPrivateKey = (JCEECPrivateKey)o;
        return this.getD().equals(jceecPrivateKey.getD()) && this.engineGetSpec().equals(jceecPrivateKey.engineGetSpec());
    }
    
    @Override
    public int hashCode() {
        return this.getD().hashCode() ^ this.engineGetSpec().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("EC Private Key").append(property);
        sb.append("             S: ").append(this.d.toString(16)).append(property);
        return sb.toString();
    }
    
    private DERBitString getPublicKeyDetails(final JCEECPublicKey jceecPublicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(jceecPublicKey.getEncoded())).getPublicKeyData();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Object.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getEncoded());
        objectOutputStream.writeObject(this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
        this.attrCarrier.writeObject(objectOutputStream);
    }
}
