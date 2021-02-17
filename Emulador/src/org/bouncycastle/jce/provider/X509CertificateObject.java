// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.Signature;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.misc.VerisignCzagExtension;
import org.bouncycastle.asn1.misc.NetscapeRevocationURL;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.util.Arrays;
import java.security.cert.Certificate;
import java.security.PublicKey;
import org.bouncycastle.asn1.x509.X509Extension;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.ArrayList;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.List;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.X509Principal;
import java.security.Principal;
import java.math.BigInteger;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import java.util.Date;
import org.bouncycastle.asn1.DERBitString;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import java.security.cert.X509Certificate;

public class X509CertificateObject extends X509Certificate implements PKCS12BagAttributeCarrier
{
    private X509CertificateStructure c;
    private BasicConstraints basicConstraints;
    private boolean[] keyUsage;
    private boolean hashValueSet;
    private int hashValue;
    private PKCS12BagAttributeCarrier attrCarrier;
    
    public X509CertificateObject(final X509CertificateStructure c) throws CertificateParsingException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.c = c;
        try {
            final byte[] extensionBytes = this.getExtensionBytes("2.5.29.19");
            if (extensionBytes != null) {
                this.basicConstraints = BasicConstraints.getInstance(ASN1Object.fromByteArray(extensionBytes));
            }
        }
        catch (Exception obj) {
            throw new CertificateParsingException("cannot construct BasicConstraints: " + obj);
        }
        try {
            final byte[] extensionBytes2 = this.getExtensionBytes("2.5.29.15");
            if (extensionBytes2 != null) {
                final DERBitString instance = DERBitString.getInstance(ASN1Object.fromByteArray(extensionBytes2));
                final byte[] bytes = instance.getBytes();
                final int n = bytes.length * 8 - instance.getPadBits();
                this.keyUsage = new boolean[(n < 9) ? 9 : n];
                for (int i = 0; i != n; ++i) {
                    this.keyUsage[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
                }
            }
            else {
                this.keyUsage = null;
            }
        }
        catch (Exception obj2) {
            throw new CertificateParsingException("cannot construct KeyUsage: " + obj2);
        }
    }
    
    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.checkValidity(new Date());
    }
    
    @Override
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        if (date.getTime() > this.getNotAfter().getTime()) {
            throw new CertificateExpiredException("certificate expired on " + this.c.getEndDate().getTime());
        }
        if (date.getTime() < this.getNotBefore().getTime()) {
            throw new CertificateNotYetValidException("certificate not valid till " + this.c.getStartDate().getTime());
        }
    }
    
    @Override
    public int getVersion() {
        return this.c.getVersion();
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.c.getSerialNumber().getValue();
    }
    
    @Override
    public Principal getIssuerDN() {
        return new X509Principal(this.c.getIssuer());
    }
    
    @Override
    public X500Principal getIssuerX500Principal() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.c.getIssuer());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }
    
    @Override
    public Principal getSubjectDN() {
        return new X509Principal(this.c.getSubject());
    }
    
    @Override
    public X500Principal getSubjectX500Principal() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.c.getSubject());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }
    
    @Override
    public Date getNotBefore() {
        return this.c.getStartDate().getDate();
    }
    
    @Override
    public Date getNotAfter() {
        return this.c.getEndDate().getDate();
    }
    
    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        try {
            return this.c.getTBSCertificate().getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    @Override
    public byte[] getSignature() {
        return this.c.getSignature().getBytes();
    }
    
    @Override
    public String getSigAlgName() {
        final Provider provider = Security.getProvider("BC");
        if (provider != null) {
            final String property = provider.getProperty("Alg.Alias.Signature." + this.getSigAlgOID());
            if (property != null) {
                return property;
            }
        }
        final Provider[] providers = Security.getProviders();
        for (int i = 0; i != providers.length; ++i) {
            final String property2 = providers[i].getProperty("Alg.Alias.Signature." + this.getSigAlgOID());
            if (property2 != null) {
                return property2;
            }
        }
        return this.getSigAlgOID();
    }
    
    @Override
    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getObjectId().getId();
    }
    
    @Override
    public byte[] getSigAlgParams() {
        if (this.c.getSignatureAlgorithm().getParameters() != null) {
            return this.c.getSignatureAlgorithm().getParameters().getDERObject().getDEREncoded();
        }
        return null;
    }
    
    @Override
    public boolean[] getIssuerUniqueID() {
        final DERBitString issuerUniqueId = this.c.getTBSCertificate().getIssuerUniqueId();
        if (issuerUniqueId != null) {
            final byte[] bytes = issuerUniqueId.getBytes();
            final boolean[] array = new boolean[bytes.length * 8 - issuerUniqueId.getPadBits()];
            for (int i = 0; i != array.length; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public boolean[] getSubjectUniqueID() {
        final DERBitString subjectUniqueId = this.c.getTBSCertificate().getSubjectUniqueId();
        if (subjectUniqueId != null) {
            final byte[] bytes = subjectUniqueId.getBytes();
            final boolean[] array = new boolean[bytes.length * 8 - subjectUniqueId.getPadBits()];
            for (int i = 0; i != array.length; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public boolean[] getKeyUsage() {
        return this.keyUsage;
    }
    
    @Override
    public List getExtendedKeyUsage() throws CertificateParsingException {
        final byte[] extensionBytes = this.getExtensionBytes("2.5.29.37");
        if (extensionBytes != null) {
            try {
                final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(extensionBytes).readObject();
                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i != asn1Sequence.size(); ++i) {
                    list.add(((DERObjectIdentifier)asn1Sequence.getObjectAt(i)).getId());
                }
                return Collections.unmodifiableList((List<?>)list);
            }
            catch (Exception ex) {
                throw new CertificateParsingException("error processing extended key usage extension");
            }
        }
        return null;
    }
    
    @Override
    public int getBasicConstraints() {
        if (this.basicConstraints == null) {
            return -1;
        }
        if (!this.basicConstraints.isCA()) {
            return -1;
        }
        if (this.basicConstraints.getPathLenConstraint() == null) {
            return Integer.MAX_VALUE;
        }
        return this.basicConstraints.getPathLenConstraint().intValue();
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            final HashSet<String> set = new HashSet<String>();
            final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                    if (extensions.getExtension(derObjectIdentifier).isCritical()) {
                        set.add(derObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    private byte[] getExtensionBytes(final String s) {
        final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(new DERObjectIdentifier(s));
            if (extension != null) {
                return extension.getValue().getOctets();
            }
        }
        return null;
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(new DERObjectIdentifier(s));
            if (extension != null) {
                try {
                    return extension.getValue().getEncoded();
                }
                catch (Exception ex) {
                    throw new IllegalStateException("error parsing " + ex.toString());
                }
            }
        }
        return null;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            final HashSet<String> set = new HashSet<String>();
            final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                    if (!extensions.getExtension(derObjectIdentifier).isCritical()) {
                        set.add(derObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        if (this.getVersion() == 3) {
            final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                    final String id = derObjectIdentifier.getId();
                    if (!id.equals(RFC3280CertPathUtilities.KEY_USAGE) && !id.equals(RFC3280CertPathUtilities.CERTIFICATE_POLICIES) && !id.equals(RFC3280CertPathUtilities.POLICY_MAPPINGS) && !id.equals(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY) && !id.equals(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS) && !id.equals(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT) && !id.equals(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR) && !id.equals(RFC3280CertPathUtilities.POLICY_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.BASIC_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME)) {
                        if (id.equals(RFC3280CertPathUtilities.NAME_CONSTRAINTS)) {
                            continue;
                        }
                        if (extensions.getExtension(derObjectIdentifier).isCritical()) {
                            return true;
                        }
                        continue;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public PublicKey getPublicKey() {
        return JDKKeyFactory.createPublicKeyFromPublicKeyInfo(this.c.getSubjectPublicKeyInfo());
    }
    
    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        try {
            return this.c.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Certificate)) {
            return false;
        }
        final Certificate certificate = (Certificate)o;
        try {
            return Arrays.areEqual(this.getEncoded(), certificate.getEncoded());
        }
        catch (CertificateEncodingException ex) {
            return false;
        }
    }
    
    @Override
    public synchronized int hashCode() {
        if (!this.hashValueSet) {
            this.hashValue = this.calculateHashCode();
            this.hashValueSet = true;
        }
        return this.hashValue;
    }
    
    private int calculateHashCode() {
        try {
            return Arrays.hashCode(this.getEncoded());
        }
        catch (CertificateEncodingException ex) {
            return 0;
        }
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
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("  [0]         Version: ").append(this.getVersion()).append(property);
        sb.append("         SerialNumber: ").append(this.getSerialNumber()).append(property);
        sb.append("             IssuerDN: ").append(this.getIssuerDN()).append(property);
        sb.append("           Start Date: ").append(this.getNotBefore()).append(property);
        sb.append("           Final Date: ").append(this.getNotAfter()).append(property);
        sb.append("            SubjectDN: ").append(this.getSubjectDN()).append(property);
        sb.append("           Public Key: ").append(this.getPublicKey()).append(property);
        sb.append("  Signature Algorithm: ").append(this.getSigAlgName()).append(property);
        final byte[] signature = this.getSignature();
        sb.append("            Signature: ").append(new String(Hex.encode(signature, 0, 20))).append(property);
        for (int i = 20; i < signature.length; i += 20) {
            if (i < signature.length - 20) {
                sb.append("                       ").append(new String(Hex.encode(signature, i, 20))).append(property);
            }
            else {
                sb.append("                       ").append(new String(Hex.encode(signature, i, signature.length - i))).append(property);
            }
        }
        final X509Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                sb.append("       Extensions: \n");
            }
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(derObjectIdentifier);
                if (extension.getValue() != null) {
                    final ASN1InputStream asn1InputStream = new ASN1InputStream(extension.getValue().getOctets());
                    sb.append("                       critical(").append(extension.isCritical()).append(") ");
                    try {
                        if (derObjectIdentifier.equals(X509Extensions.BasicConstraints)) {
                            sb.append(new BasicConstraints((ASN1Sequence)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(X509Extensions.KeyUsage)) {
                            sb.append(new KeyUsage((DERBitString)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType)) {
                            sb.append(new NetscapeCertType((DERBitString)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL)) {
                            sb.append(new NetscapeRevocationURL((DERIA5String)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension)) {
                            sb.append(new VerisignCzagExtension((DERIA5String)asn1InputStream.readObject())).append(property);
                        }
                        else {
                            sb.append(derObjectIdentifier.getId());
                            sb.append(" value = ").append(ASN1Dump.dumpAsString(asn1InputStream.readObject())).append(property);
                        }
                    }
                    catch (Exception ex) {
                        sb.append(derObjectIdentifier.getId());
                        sb.append(" value = ").append("*****").append(property);
                    }
                }
                else {
                    sb.append(property);
                }
            }
        }
        return sb.toString();
    }
    
    @Override
    public final void verify(final PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        final String signatureName = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
        Signature signature;
        try {
            signature = Signature.getInstance(signatureName, "BC");
        }
        catch (Exception ex) {
            signature = Signature.getInstance(signatureName);
        }
        this.checkSignature(publicKey, signature);
    }
    
    @Override
    public final void verify(final PublicKey publicKey, final String provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.checkSignature(publicKey, Signature.getInstance(X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm()), provider));
    }
    
    private void checkSignature(final PublicKey publicKey, final Signature signature) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (!this.c.getSignatureAlgorithm().equals(this.c.getTBSCertificate().getSignature())) {
            throw new CertificateException("signature algorithm in TBS cert not same as outer cert");
        }
        X509SignatureUtil.setSignatureParameters(signature, this.c.getSignatureAlgorithm().getParameters());
        signature.initVerify(publicKey);
        signature.update(this.getTBSCertificate());
        if (!signature.verify(this.getSignature())) {
            throw new InvalidKeyException("Public key presented not for certificate signature");
        }
    }
}
