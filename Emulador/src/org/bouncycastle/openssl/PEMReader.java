// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.openssl;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import java.security.spec.RSAPrivateCrtKeySpec;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.util.encoders.Hex;
import java.util.StringTokenizer;
import java.security.KeyPair;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import java.security.cert.X509CRL;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.security.NoSuchProviderException;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.PublicKey;
import org.bouncycastle.util.encoders.Base64;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;

public class PEMReader extends BufferedReader
{
    private final PasswordFinder pFinder;
    private final String provider;
    
    public PEMReader(final Reader reader) {
        this(reader, null, "BC");
    }
    
    public PEMReader(final Reader reader, final PasswordFinder passwordFinder) {
        this(reader, passwordFinder, "BC");
    }
    
    public PEMReader(final Reader in, final PasswordFinder pFinder, final String provider) {
        super(in);
        this.pFinder = pFinder;
        this.provider = provider;
    }
    
    public Object readObject() throws IOException {
        String line;
        while ((line = this.readLine()) != null) {
            if (line.indexOf("-----BEGIN PUBLIC KEY") != -1) {
                return this.readPublicKey("-----END PUBLIC KEY");
            }
            if (line.indexOf("-----BEGIN RSA PUBLIC KEY") != -1) {
                return this.readRSAPublicKey("-----END RSA PUBLIC KEY");
            }
            if (line.indexOf("-----BEGIN CERTIFICATE REQUEST") != -1) {
                return this.readCertificateRequest("-----END CERTIFICATE REQUEST");
            }
            if (line.indexOf("-----BEGIN NEW CERTIFICATE REQUEST") != -1) {
                return this.readCertificateRequest("-----END NEW CERTIFICATE REQUEST");
            }
            if (line.indexOf("-----BEGIN CERTIFICATE") != -1) {
                return this.readCertificate("-----END CERTIFICATE");
            }
            if (line.indexOf("-----BEGIN PKCS7") != -1) {
                return this.readPKCS7("-----END PKCS7");
            }
            if (line.indexOf("-----BEGIN X509 CERTIFICATE") != -1) {
                return this.readCertificate("-----END X509 CERTIFICATE");
            }
            if (line.indexOf("-----BEGIN X509 CRL") != -1) {
                return this.readCRL("-----END X509 CRL");
            }
            if (line.indexOf("-----BEGIN ATTRIBUTE CERTIFICATE") != -1) {
                return this.readAttributeCertificate("-----END ATTRIBUTE CERTIFICATE");
            }
            if (line.indexOf("-----BEGIN RSA PRIVATE KEY") != -1) {
                try {
                    return this.readKeyPair("RSA", "-----END RSA PRIVATE KEY");
                }
                catch (IOException ex) {
                    throw ex;
                }
                catch (Exception ex2) {
                    throw new PEMException("problem creating RSA private key: " + ex2.toString(), ex2);
                }
            }
            if (line.indexOf("-----BEGIN DSA PRIVATE KEY") != -1) {
                try {
                    return this.readKeyPair("DSA", "-----END DSA PRIVATE KEY");
                }
                catch (IOException ex3) {
                    throw ex3;
                }
                catch (Exception ex4) {
                    throw new PEMException("problem creating DSA private key: " + ex4.toString(), ex4);
                }
            }
            if (line.indexOf("-----BEGIN EC PARAMETERS-----") != -1) {
                return this.readECParameters("-----END EC PARAMETERS-----");
            }
            if (line.indexOf("-----BEGIN EC PRIVATE KEY-----") != -1) {
                try {
                    return this.readKeyPair("ECDSA", "-----END EC PRIVATE KEY-----");
                }
                catch (IOException ex5) {
                    throw ex5;
                }
                catch (Exception ex6) {
                    throw new PEMException("problem creating ECDSA private key: " + ex6.toString(), ex6);
                }
                break;
            }
        }
        return null;
    }
    
    private byte[] readBytes(final String s) throws IOException {
        final StringBuffer sb = new StringBuffer();
        String line;
        while ((line = this.readLine()) != null && line.indexOf(s) == -1) {
            sb.append(line.trim());
        }
        if (line == null) {
            throw new IOException(s + " not found");
        }
        return Base64.decode(sb.toString());
    }
    
    private PublicKey readRSAPublicKey(final String s) throws IOException {
        final RSAPublicKeyStructure rsaPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)new ASN1InputStream(this.readBytes(s)).readObject());
        final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(rsaPublicKeyStructure.getModulus(), rsaPublicKeyStructure.getPublicExponent());
        try {
            return KeyFactory.getInstance("RSA", this.provider).generatePublic(keySpec);
        }
        catch (NoSuchProviderException ex2) {
            throw new IOException("can't find provider " + this.provider);
        }
        catch (Exception ex) {
            throw new PEMException("problem extracting key: " + ex.toString(), ex);
        }
    }
    
    private PublicKey readPublicKey(final String s) throws IOException {
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(this.readBytes(s));
        final String[] array = { "DSA", "RSA" };
        for (int i = 0; i < array.length; ++i) {
            try {
                return KeyFactory.getInstance(array[i], this.provider).generatePublic(keySpec);
            }
            catch (NoSuchAlgorithmException ex) {}
            catch (InvalidKeySpecException ex2) {}
            catch (NoSuchProviderException ex3) {
                throw new RuntimeException("can't find provider " + this.provider);
            }
        }
        return null;
    }
    
    private X509Certificate readCertificate(final String s) throws IOException {
        final ByteArrayInputStream inStream = new ByteArrayInputStream(this.readBytes(s));
        try {
            return (X509Certificate)CertificateFactory.getInstance("X.509", this.provider).generateCertificate(inStream);
        }
        catch (Exception ex) {
            throw new PEMException("problem parsing cert: " + ex.toString(), ex);
        }
    }
    
    private X509CRL readCRL(final String s) throws IOException {
        final ByteArrayInputStream inStream = new ByteArrayInputStream(this.readBytes(s));
        try {
            return (X509CRL)CertificateFactory.getInstance("X.509", this.provider).generateCRL(inStream);
        }
        catch (Exception ex) {
            throw new PEMException("problem parsing cert: " + ex.toString(), ex);
        }
    }
    
    private PKCS10CertificationRequest readCertificateRequest(final String s) throws IOException {
        try {
            return new PKCS10CertificationRequest(this.readBytes(s));
        }
        catch (Exception ex) {
            throw new PEMException("problem parsing certrequest: " + ex.toString(), ex);
        }
    }
    
    private X509AttributeCertificate readAttributeCertificate(final String s) throws IOException {
        return new X509V2AttributeCertificate(this.readBytes(s));
    }
    
    private ContentInfo readPKCS7(final String s) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String line;
        while ((line = this.readLine()) != null && line.indexOf(s) == -1) {
            sb.append(line.trim().trim());
            Base64.decode(sb.substring(0, sb.length() / 4 * 4), byteArrayOutputStream);
            sb.delete(0, sb.length() / 4 * 4);
        }
        if (sb.length() != 0) {
            throw new IOException("base64 data appears to be truncated");
        }
        if (line == null) {
            throw new IOException(s + " not found");
        }
        try {
            return ContentInfo.getInstance(new ASN1InputStream(byteArrayOutputStream.toByteArray()).readObject());
        }
        catch (Exception ex) {
            throw new PEMException("problem parsing PKCS7 object: " + ex.toString(), ex);
        }
    }
    
    private KeyPair readKeyPair(final String algorithm, final String str) throws Exception {
        boolean b = false;
        String substring = null;
        final StringBuffer sb = new StringBuffer();
        String line;
        while ((line = this.readLine()) != null) {
            if (line.startsWith("Proc-Type: 4,ENCRYPTED")) {
                b = true;
            }
            else if (line.startsWith("DEK-Info:")) {
                substring = line.substring(10);
            }
            else {
                if (line.indexOf(str) != -1) {
                    break;
                }
                sb.append(line.trim());
            }
        }
        byte[] array = Base64.decode(sb.toString());
        if (b) {
            if (this.pFinder == null) {
                throw new PasswordException("No password finder specified, but a password is required");
            }
            final char[] password = this.pFinder.getPassword();
            if (password == null) {
                throw new PasswordException("Password is null, but a password is required");
            }
            final StringTokenizer stringTokenizer = new StringTokenizer(substring, ",");
            array = PEMUtilities.crypt(false, this.provider, array, password, stringTokenizer.nextToken(), Hex.decode(stringTokenizer.nextToken()));
        }
        final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(array);
        KeySpec keySpec;
        KeySpec keySpec2;
        if (algorithm.equals("RSA")) {
            final DERInteger derInteger = (DERInteger)asn1Sequence.getObjectAt(1);
            final DERInteger derInteger2 = (DERInteger)asn1Sequence.getObjectAt(2);
            final DERInteger derInteger3 = (DERInteger)asn1Sequence.getObjectAt(3);
            final DERInteger derInteger4 = (DERInteger)asn1Sequence.getObjectAt(4);
            final DERInteger derInteger5 = (DERInteger)asn1Sequence.getObjectAt(5);
            final DERInteger derInteger6 = (DERInteger)asn1Sequence.getObjectAt(6);
            final DERInteger derInteger7 = (DERInteger)asn1Sequence.getObjectAt(7);
            final DERInteger derInteger8 = (DERInteger)asn1Sequence.getObjectAt(8);
            keySpec = new RSAPublicKeySpec(derInteger.getValue(), derInteger2.getValue());
            keySpec2 = new RSAPrivateCrtKeySpec(derInteger.getValue(), derInteger2.getValue(), derInteger3.getValue(), derInteger4.getValue(), derInteger5.getValue(), derInteger6.getValue(), derInteger7.getValue(), derInteger8.getValue());
        }
        else if (algorithm.equals("ECDSA")) {
            final ECPrivateKeyStructure ecPrivateKeyStructure = new ECPrivateKeyStructure(asn1Sequence);
            final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, ecPrivateKeyStructure.getParameters());
            final PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, ecPrivateKeyStructure.getDERObject());
            final SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, ecPrivateKeyStructure.getPublicKey().getBytes());
            keySpec2 = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
            keySpec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
        }
        else {
            final DERInteger derInteger9 = (DERInteger)asn1Sequence.getObjectAt(1);
            final DERInteger derInteger10 = (DERInteger)asn1Sequence.getObjectAt(2);
            final DERInteger derInteger11 = (DERInteger)asn1Sequence.getObjectAt(3);
            final DERInteger derInteger12 = (DERInteger)asn1Sequence.getObjectAt(4);
            keySpec2 = new DSAPrivateKeySpec(((DERInteger)asn1Sequence.getObjectAt(5)).getValue(), derInteger9.getValue(), derInteger10.getValue(), derInteger11.getValue());
            keySpec = new DSAPublicKeySpec(derInteger12.getValue(), derInteger9.getValue(), derInteger10.getValue(), derInteger11.getValue());
        }
        final KeyFactory instance = KeyFactory.getInstance(algorithm, this.provider);
        return new KeyPair(instance.generatePublic(keySpec), instance.generatePrivate(keySpec2));
    }
    
    private ECNamedCurveParameterSpec readECParameters(final String s) throws IOException {
        return ECNamedCurveTable.getParameterSpec(((DERObjectIdentifier)ASN1Object.fromByteArray(this.readBytes(s))).getId());
    }
}
