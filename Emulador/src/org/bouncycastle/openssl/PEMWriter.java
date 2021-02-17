// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.openssl;

import java.security.interfaces.DSAParams;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.SecureRandom;
import java.math.BigInteger;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import org.bouncycastle.x509.X509AttributeCertificate;
import java.security.PublicKey;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.DSAParameter;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import java.security.Key;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.util.encoders.Base64;
import java.io.IOException;
import org.bouncycastle.util.encoders.Hex;
import java.io.Writer;
import java.io.BufferedWriter;

public class PEMWriter extends BufferedWriter
{
    private String provider;
    
    public PEMWriter(final Writer writer) {
        this(writer, "BC");
    }
    
    public PEMWriter(final Writer out, final String provider) {
        super(out);
        this.provider = provider;
    }
    
    private void writeHexEncoded(byte[] encode) throws IOException {
        encode = Hex.encode(encode);
        for (int i = 0; i != encode.length; ++i) {
            this.write((char)encode[i]);
        }
    }
    
    private void writeEncoded(byte[] encode) throws IOException {
        final char[] cbuf = new char[64];
        encode = Base64.encode(encode);
        for (int i = 0; i < encode.length; i += cbuf.length) {
            int len;
            for (len = 0; len != cbuf.length && i + len < encode.length; ++len) {
                cbuf[len] = (char)encode[i + len];
            }
            this.write(cbuf, 0, len);
            this.newLine();
        }
    }
    
    public void writeObject(final Object o) throws IOException {
        String s = null;
        byte[] array = null;
        Label_0489: {
            if (o instanceof X509Certificate) {
                s = "CERTIFICATE";
                try {
                    array = ((X509Certificate)o).getEncoded();
                    break Label_0489;
                }
                catch (CertificateEncodingException ex) {
                    throw new IOException("Cannot encode object: " + ex.toString());
                }
            }
            if (o instanceof X509CRL) {
                s = "X509 CRL";
                try {
                    array = ((X509CRL)o).getEncoded();
                    break Label_0489;
                }
                catch (CRLException ex2) {
                    throw new IOException("Cannot encode object: " + ex2.toString());
                }
            }
            if (o instanceof KeyPair) {
                this.writeObject(((KeyPair)o).getPrivate());
                return;
            }
            if (o instanceof PrivateKey) {
                final PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(((Key)o).getEncoded()));
                if (o instanceof RSAPrivateKey) {
                    s = "RSA PRIVATE KEY";
                    array = privateKeyInfo.getPrivateKey().getEncoded();
                }
                else if (o instanceof DSAPrivateKey) {
                    s = "DSA PRIVATE KEY";
                    final DSAParameter instance = DSAParameter.getInstance(privateKeyInfo.getAlgorithmId().getParameters());
                    final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                    asn1EncodableVector.add(new DERInteger(0));
                    asn1EncodableVector.add(new DERInteger(instance.getP()));
                    asn1EncodableVector.add(new DERInteger(instance.getQ()));
                    asn1EncodableVector.add(new DERInteger(instance.getG()));
                    final BigInteger x = ((DSAPrivateKey)o).getX();
                    asn1EncodableVector.add(new DERInteger(instance.getG().modPow(x, instance.getP())));
                    asn1EncodableVector.add(new DERInteger(x));
                    array = new DERSequence(asn1EncodableVector).getEncoded();
                }
                else {
                    if (!((PrivateKey)o).getAlgorithm().equals("ECDSA")) {
                        throw new IOException("Cannot identify private key");
                    }
                    s = "EC PRIVATE KEY";
                    array = privateKeyInfo.getPrivateKey().getEncoded();
                }
            }
            else if (o instanceof PublicKey) {
                s = "PUBLIC KEY";
                array = ((PublicKey)o).getEncoded();
            }
            else if (o instanceof X509AttributeCertificate) {
                s = "ATTRIBUTE CERTIFICATE";
                array = ((X509V2AttributeCertificate)o).getEncoded();
            }
            else if (o instanceof PKCS10CertificationRequest) {
                s = "CERTIFICATE REQUEST";
                array = ((PKCS10CertificationRequest)o).getEncoded();
            }
            else {
                if (!(o instanceof ContentInfo)) {
                    throw new IOException("unknown object passed - can't encode.");
                }
                s = "PKCS7";
                array = ((ContentInfo)o).getEncoded();
            }
        }
        this.writeHeader(s);
        this.writeEncoded(array);
        this.writeFooter(s);
    }
    
    public void writeObject(final Object o, final String s, final char[] array, final SecureRandom secureRandom) throws IOException {
        if (o instanceof KeyPair) {
            this.writeObject(((KeyPair)o).getPrivate());
            return;
        }
        String s2 = null;
        byte[] array2 = null;
        if (o instanceof RSAPrivateCrtKey) {
            s2 = "RSA PRIVATE KEY";
            final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)o;
            array2 = new RSAPrivateKeyStructure(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient()).getEncoded();
        }
        else if (o instanceof DSAPrivateKey) {
            s2 = "DSA PRIVATE KEY";
            final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)o;
            final DSAParams params = dsaPrivateKey.getParams();
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(new DERInteger(0));
            asn1EncodableVector.add(new DERInteger(params.getP()));
            asn1EncodableVector.add(new DERInteger(params.getQ()));
            asn1EncodableVector.add(new DERInteger(params.getG()));
            final BigInteger x = dsaPrivateKey.getX();
            asn1EncodableVector.add(new DERInteger(params.getG().modPow(x, params.getP())));
            asn1EncodableVector.add(new DERInteger(x));
            array2 = new DERSequence(asn1EncodableVector).getEncoded();
        }
        else if (o instanceof PrivateKey && "ECDSA".equals(((PrivateKey)o).getAlgorithm())) {
            s2 = "EC PRIVATE KEY";
            array2 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(((PrivateKey)o).getEncoded())).getPrivateKey().getEncoded();
        }
        if (s2 == null || array2 == null) {
            throw new IllegalArgumentException("Object type not supported: " + o.getClass().getName());
        }
        String upperCase = Strings.toUpperCase(s);
        if (upperCase.equals("DESEDE")) {
            upperCase = "DES-EDE3-CBC";
        }
        final byte[] bytes = new byte[upperCase.startsWith("AES-") ? 16 : 8];
        secureRandom.nextBytes(bytes);
        final byte[] crypt = PEMUtilities.crypt(true, this.provider, array2, array, upperCase, bytes);
        this.writeHeader(s2);
        this.write("Proc-Type: 4,ENCRYPTED");
        this.newLine();
        this.write("DEK-Info: " + upperCase + ",");
        this.writeHexEncoded(bytes);
        this.newLine();
        this.newLine();
        this.writeEncoded(crypt);
        this.writeFooter(s2);
    }
    
    private void writeHeader(final String str) throws IOException {
        this.write("-----BEGIN " + str + "-----");
        this.newLine();
    }
    
    private void writeFooter(final String str) throws IOException {
        this.write("-----END " + str + "-----");
        this.newLine();
    }
}
