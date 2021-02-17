// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.util;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.InputStream;
import java.io.IOException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class PublicKeyFactory
{
    public static AsymmetricKeyParameter createKey(final byte[] array) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(array)));
    }
    
    public static AsymmetricKeyParameter createKey(final InputStream inputStream) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    public static AsymmetricKeyParameter createKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final AlgorithmIdentifier algorithmId = subjectPublicKeyInfo.getAlgorithmId();
        if (algorithmId.getObjectId().equals(PKCSObjectIdentifiers.rsaEncryption) || algorithmId.getObjectId().equals(X509ObjectIdentifiers.id_ea_rsa)) {
            final RSAPublicKeyStructure rsaPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)subjectPublicKeyInfo.getPublicKey());
            return new RSAKeyParameters(false, rsaPublicKeyStructure.getModulus(), rsaPublicKeyStructure.getPublicExponent());
        }
        if (algorithmId.getObjectId().equals(PKCSObjectIdentifiers.dhKeyAgreement) || algorithmId.getObjectId().equals(X9ObjectIdentifiers.dhpublicnumber)) {
            final DHParameter dhParameter = new DHParameter((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
            final DERInteger derInteger = (DERInteger)subjectPublicKeyInfo.getPublicKey();
            final BigInteger l = dhParameter.getL();
            return new DHPublicKeyParameters(derInteger.getValue(), new DHParameters(dhParameter.getP(), dhParameter.getG(), null, (l == null) ? 0 : l.intValue()));
        }
        if (algorithmId.getObjectId().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            final ElGamalParameter elGamalParameter = new ElGamalParameter((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
            return new ElGamalPublicKeyParameters(((DERInteger)subjectPublicKeyInfo.getPublicKey()).getValue(), new ElGamalParameters(elGamalParameter.getP(), elGamalParameter.getG()));
        }
        if (algorithmId.getObjectId().equals(X9ObjectIdentifiers.id_dsa) || algorithmId.getObjectId().equals(OIWObjectIdentifiers.dsaWithSHA1)) {
            final DERInteger derInteger2 = (DERInteger)subjectPublicKeyInfo.getPublicKey();
            final DEREncodable parameters = subjectPublicKeyInfo.getAlgorithmId().getParameters();
            DSAParameters dsaParameters = null;
            if (parameters != null) {
                final DSAParameter instance = DSAParameter.getInstance(parameters.getDERObject());
                dsaParameters = new DSAParameters(instance.getP(), instance.getQ(), instance.getG());
            }
            return new DSAPublicKeyParameters(derInteger2.getValue(), dsaParameters);
        }
        if (algorithmId.getObjectId().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            final X962Parameters x962Parameters = new X962Parameters((DERObject)subjectPublicKeyInfo.getAlgorithmId().getParameters());
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
            return new ECPublicKeyParameters(new X9ECPoint(ecDomainParameters.getCurve(), new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes())).getPoint(), ecDomainParameters);
        }
        throw new RuntimeException("algorithm identifier in key not recognised");
    }
}
