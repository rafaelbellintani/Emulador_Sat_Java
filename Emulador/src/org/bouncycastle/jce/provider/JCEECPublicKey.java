// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import java.math.BigInteger;
import org.bouncycastle.jce.ECGOST3410NamedCurveTable;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.ASN1Sequence;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import java.security.spec.EllipticCurve;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.asymmetric.ec.EC5Util;
import java.security.spec.ECPublicKeySpec;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import java.security.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import java.security.interfaces.ECPublicKey;

public class JCEECPublicKey implements ECPublicKey, org.bouncycastle.jce.interfaces.ECPublicKey, ECPointEncoder
{
    private String algorithm;
    private ECPoint q;
    private ECParameterSpec ecSpec;
    private boolean withCompression;
    private GOST3410PublicKeyAlgParameters gostParams;
    
    public JCEECPublicKey(final String algorithm, final JCEECPublicKey jceecPublicKey) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = jceecPublicKey.q;
        this.ecSpec = jceecPublicKey.ecSpec;
        this.withCompression = jceecPublicKey.withCompression;
        this.gostParams = jceecPublicKey.gostParams;
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.ecSpec = ecPublicKeySpec.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, ecPublicKeySpec.getW(), false);
    }
    
    public JCEECPublicKey(final String algorithm, final org.bouncycastle.jce.spec.ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = ecPublicKeySpec.getQ();
        if (ecPublicKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecPublicKeySpec.getParams().getCurve(), ecPublicKeySpec.getParams().getSeed()), ecPublicKeySpec.getParams());
        }
        else {
            if (this.q.getCurve() == null) {
                this.q = ProviderUtil.getEcImplicitlyCa().getCurve().createPoint(this.q.getX().toBigInteger(), this.q.getY().toBigInteger(), false);
            }
            this.ecSpec = null;
        }
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters, final ECParameterSpec ecSpec) {
        this.algorithm = "EC";
        final ECDomainParameters parameters = ecPublicKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        if (ecSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            this.ecSpec = ecSpec;
        }
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters, final org.bouncycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "EC";
        final ECDomainParameters parameters = ecPublicKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        if (ecParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), ecParameterSpec);
        }
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        this.ecSpec = null;
    }
    
    private ECParameterSpec createSpec(final EllipticCurve curve, final ECDomainParameters ecDomainParameters) {
        return new ECParameterSpec(curve, new java.security.spec.ECPoint(ecDomainParameters.getG().getX().toBigInteger(), ecDomainParameters.getG().getY().toBigInteger()), ecDomainParameters.getN(), ecDomainParameters.getH().intValue());
    }
    
    public JCEECPublicKey(final ECPublicKey ecPublicKey) {
        this.algorithm = "EC";
        this.algorithm = ecPublicKey.getAlgorithm();
        this.ecSpec = ecPublicKey.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, ecPublicKey.getW(), false);
    }
    
    JCEECPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.algorithm = "EC";
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }
    
    private void populateFromPubKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        if (subjectPublicKeyInfo.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
            final DERBitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
            this.algorithm = "ECGOST3410";
            ASN1OctetString asn1OctetString;
            try {
                asn1OctetString = (ASN1OctetString)ASN1Object.fromByteArray(publicKeyData.getBytes());
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("error recovering public key");
            }
            final byte[] octets = asn1OctetString.getOctets();
            final byte[] magnitude = new byte[32];
            final byte[] magnitude2 = new byte[32];
            for (int i = 0; i != magnitude.length; ++i) {
                magnitude[i] = octets[31 - i];
            }
            for (int j = 0; j != magnitude2.length; ++j) {
                magnitude2[j] = octets[63 - j];
            }
            this.gostParams = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
            final ECNamedCurveParameterSpec parameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()));
            final ECCurve curve = parameterSpec.getCurve();
            final EllipticCurve convertCurve = EC5Util.convertCurve(curve, parameterSpec.getSeed());
            this.q = curve.createPoint(new BigInteger(1, magnitude), new BigInteger(1, magnitude2), false);
            this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), convertCurve, new java.security.spec.ECPoint(parameterSpec.getG().getX().toBigInteger(), parameterSpec.getG().getY().toBigInteger()), parameterSpec.getN(), parameterSpec.getH());
        }
        else {
            final X962Parameters x962Parameters = new X962Parameters((DERObject)subjectPublicKeyInfo.getAlgorithmId().getParameters());
            ECCurve ecCurve;
            if (x962Parameters.isNamedCurve()) {
                final DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)x962Parameters.getParameters();
                final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(derObjectIdentifier);
                ecCurve = namedCurveByOid.getCurve();
                this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(derObjectIdentifier), EC5Util.convertCurve(ecCurve, namedCurveByOid.getSeed()), new java.security.spec.ECPoint(namedCurveByOid.getG().getX().toBigInteger(), namedCurveByOid.getG().getY().toBigInteger()), namedCurveByOid.getN(), namedCurveByOid.getH());
            }
            else if (x962Parameters.isImplicitlyCA()) {
                this.ecSpec = null;
                ecCurve = ProviderUtil.getEcImplicitlyCa().getCurve();
            }
            else {
                final X9ECParameters x9ECParameters = new X9ECParameters((ASN1Sequence)x962Parameters.getParameters());
                ecCurve = x9ECParameters.getCurve();
                this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(ecCurve, x9ECParameters.getSeed()), new java.security.spec.ECPoint(x9ECParameters.getG().getX().toBigInteger(), x9ECParameters.getG().getY().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
            }
            final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
            ASN1OctetString asn1OctetString2 = new DEROctetString(bytes);
            if (bytes[0] == 4 && bytes[1] == bytes.length - 2 && (bytes[2] == 2 || bytes[2] == 3) && new X9IntegerConverter().getByteLength(ecCurve) >= bytes.length - 3) {
                try {
                    asn1OctetString2 = (ASN1OctetString)ASN1Object.fromByteArray(bytes);
                }
                catch (IOException ex2) {
                    throw new IllegalArgumentException("error recovering public key");
                }
            }
            this.q = new X9ECPoint(ecCurve, asn1OctetString2).getPoint();
        }
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded() {
        SubjectPublicKeyInfo subjectPublicKeyInfo;
        if (this.algorithm.equals("ECGOST3410")) {
            ASN1Encodable gostParams;
            if (this.gostParams != null) {
                gostParams = this.gostParams;
            }
            else if (this.ecSpec instanceof ECNamedCurveSpec) {
                gostParams = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet);
            }
            else {
                final ECCurve convertCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
                gostParams = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
            final BigInteger bigInteger = this.q.getX().toBigInteger();
            final BigInteger bigInteger2 = this.q.getY().toBigInteger();
            final byte[] array = new byte[64];
            this.extractBytes(array, 0, bigInteger);
            this.extractBytes(array, 32, bigInteger2);
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, gostParams.getDERObject()), new DEROctetString(array));
        }
        else {
            X962Parameters x962Parameters;
            if (this.ecSpec instanceof ECNamedCurveSpec) {
                x962Parameters = new X962Parameters(ECUtil.getNamedCurveOid(((ECNamedCurveSpec)this.ecSpec).getName()));
            }
            else if (this.ecSpec == null) {
                x962Parameters = new X962Parameters(DERNull.INSTANCE);
            }
            else {
                final ECCurve convertCurve2 = EC5Util.convertCurve(this.ecSpec.getCurve());
                x962Parameters = new X962Parameters(new X9ECParameters(convertCurve2, EC5Util.convertPoint(convertCurve2, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.getDERObject()), ((ASN1OctetString)new X9ECPoint(this.engineGetQ().getCurve().createPoint(this.getQ().getX().toBigInteger(), this.getQ().getY().toBigInteger(), this.withCompression)).getDERObject()).getOctets());
        }
        return subjectPublicKeyInfo.getDEREncoded();
    }
    
    private void extractBytes(final byte[] array, final int n, final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length < 32) {
            final byte[] array2 = new byte[32];
            System.arraycopy(byteArray, 0, array2, array2.length - byteArray.length, byteArray.length);
        }
        for (int i = 0; i != 32; ++i) {
            array[n + i] = byteArray[byteArray.length - 1 - i];
        }
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
    
    @Override
    public java.security.spec.ECPoint getW() {
        return new java.security.spec.ECPoint(this.q.getX().toBigInteger(), this.q.getY().toBigInteger());
    }
    
    @Override
    public ECPoint getQ() {
        if (this.ecSpec != null) {
            return this.q;
        }
        if (this.q instanceof ECPoint.Fp) {
            return new ECPoint.Fp(null, this.q.getX(), this.q.getY());
        }
        return new ECPoint.F2m(null, this.q.getX(), this.q.getY());
    }
    
    public ECPoint engineGetQ() {
        return this.q;
    }
    
    org.bouncycastle.jce.spec.ECParameterSpec engineGetSpec() {
        if (this.ecSpec != null) {
            return EC5Util.convertSpec(this.ecSpec, this.withCompression);
        }
        return ProviderUtil.getEcImplicitlyCa();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("EC Public Key").append(property);
        sb.append("            X: ").append(this.q.getX().toBigInteger().toString(16)).append(property);
        sb.append("            Y: ").append(this.q.getY().toBigInteger().toString(16)).append(property);
        return sb.toString();
    }
    
    @Override
    public void setPointFormat(final String anotherString) {
        this.withCompression = !"UNCOMPRESSED".equalsIgnoreCase(anotherString);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof JCEECPublicKey)) {
            return false;
        }
        final JCEECPublicKey jceecPublicKey = (JCEECPublicKey)o;
        return this.engineGetQ().equals(jceecPublicKey.engineGetQ()) && this.engineGetSpec().equals(jceecPublicKey.engineGetSpec());
    }
    
    @Override
    public int hashCode() {
        return this.engineGetQ().hashCode() ^ this.engineGetSpec().hashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getEncoded());
        objectOutputStream.writeObject(this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
    }
}
