// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.util;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.InputStream;
import java.io.IOException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class PrivateKeyFactory
{
    public static AsymmetricKeyParameter createKey(final byte[] array) throws IOException {
        return createKey(PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(array)));
    }
    
    public static AsymmetricKeyParameter createKey(final InputStream inputStream) throws IOException {
        return createKey(PrivateKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    public static AsymmetricKeyParameter createKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final AlgorithmIdentifier algorithmId = privateKeyInfo.getAlgorithmId();
        if (algorithmId.getObjectId().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            final RSAPrivateKeyStructure rsaPrivateKeyStructure = new RSAPrivateKeyStructure((ASN1Sequence)privateKeyInfo.getPrivateKey());
            return new RSAPrivateCrtKeyParameters(rsaPrivateKeyStructure.getModulus(), rsaPrivateKeyStructure.getPublicExponent(), rsaPrivateKeyStructure.getPrivateExponent(), rsaPrivateKeyStructure.getPrime1(), rsaPrivateKeyStructure.getPrime2(), rsaPrivateKeyStructure.getExponent1(), rsaPrivateKeyStructure.getExponent2(), rsaPrivateKeyStructure.getCoefficient());
        }
        if (algorithmId.getObjectId().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            final DHParameter dhParameter = new DHParameter((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
            final DERInteger derInteger = (DERInteger)privateKeyInfo.getPrivateKey();
            final BigInteger l = dhParameter.getL();
            return new DHPrivateKeyParameters(derInteger.getValue(), new DHParameters(dhParameter.getP(), dhParameter.getG(), null, (l == null) ? 0 : l.intValue()));
        }
        if (algorithmId.getObjectId().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            final ElGamalParameter elGamalParameter = new ElGamalParameter((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
            return new ElGamalPrivateKeyParameters(((DERInteger)privateKeyInfo.getPrivateKey()).getValue(), new ElGamalParameters(elGamalParameter.getP(), elGamalParameter.getG()));
        }
        if (algorithmId.getObjectId().equals(X9ObjectIdentifiers.id_dsa)) {
            final DERInteger derInteger2 = (DERInteger)privateKeyInfo.getPrivateKey();
            final DEREncodable parameters = privateKeyInfo.getAlgorithmId().getParameters();
            DSAParameters dsaParameters = null;
            if (parameters != null) {
                final DSAParameter instance = DSAParameter.getInstance(parameters.getDERObject());
                dsaParameters = new DSAParameters(instance.getP(), instance.getQ(), instance.getG());
            }
            return new DSAPrivateKeyParameters(derInteger2.getValue(), dsaParameters);
        }
        if (algorithmId.getObjectId().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            final X962Parameters x962Parameters = new X962Parameters((DERObject)privateKeyInfo.getAlgorithmId().getParameters());
            ECDomainParameters ecDomainParameters;
            if (x962Parameters.isNamedCurve()) {
                final DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)x962Parameters.getParameters();
                X9ECParameters x9ECParameters = X962NamedCurves.getByOID(derObjectIdentifier);
                if (x9ECParameters == null) {
                    x9ECParameters = SECNamedCurves.getByOID(derObjectIdentifier);
                    if (x9ECParameters == null) {
                        x9ECParameters = NISTNamedCurves.getByOID(derObjectIdentifier);
                        if (x9ECParameters == null) {
                            x9ECParameters = TeleTrusTNamedCurves.getByOID(derObjectIdentifier);
                        }
                    }
                }
                ecDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
            }
            else {
                final X9ECParameters x9ECParameters2 = new X9ECParameters((ASN1Sequence)x962Parameters.getParameters());
                ecDomainParameters = new ECDomainParameters(x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH(), x9ECParameters2.getSeed());
            }
            return new ECPrivateKeyParameters(new ECPrivateKeyStructure((ASN1Sequence)privateKeyInfo.getPrivateKey()).getKey(), ecDomainParameters);
        }
        throw new RuntimeException("algorithm identifier in key not recognised");
    }
}
