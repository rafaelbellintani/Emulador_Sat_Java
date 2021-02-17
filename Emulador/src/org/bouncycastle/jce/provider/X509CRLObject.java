// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.x509.extension.X509ExtensionUtil;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.util.Iterator;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.util.encoders.Hex;
import java.util.Collections;
import java.security.cert.X509CRLEntry;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.TBSCertList;
import java.util.Date;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.X509Principal;
import java.security.Principal;
import java.security.Signature;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509Extension;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import java.security.cert.CRLException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.CertificateList;
import java.security.cert.X509CRL;

public class X509CRLObject extends X509CRL
{
    private CertificateList c;
    private String sigAlgName;
    private byte[] sigAlgParams;
    private boolean isIndirect;
    
    public X509CRLObject(final CertificateList c) throws CRLException {
        this.c = c;
        try {
            this.sigAlgName = X509SignatureUtil.getSignatureName(c.getSignatureAlgorithm());
            if (c.getSignatureAlgorithm().getParameters() != null) {
                this.sigAlgParams = ((ASN1Encodable)c.getSignatureAlgorithm().getParameters()).getDEREncoded();
            }
            else {
                this.sigAlgParams = null;
            }
            this.isIndirect = this.isIndirectCRL();
        }
        catch (Exception obj) {
            throw new CRLException("CRL contents invalid: " + obj);
        }
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs == null) {
            return false;
        }
        criticalExtensionOIDs.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
        criticalExtensionOIDs.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
        return !criticalExtensionOIDs.isEmpty();
    }
    
    private Set getExtensionOIDs(final boolean b) {
        if (this.getVersion() == 2) {
            final X509Extensions extensions = this.c.getTBSCertList().getExtensions();
            if (extensions != null) {
                final HashSet<String> set = new HashSet<String>();
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                    if (b == extensions.getExtension(derObjectIdentifier).isCritical()) {
                        set.add(derObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final X509Extensions extensions = this.c.getTBSCertList().getExtensions();
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
    public byte[] getEncoded() throws CRLException {
        try {
            return this.c.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    @Override
    public void verify(final PublicKey publicKey) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        this.verify(publicKey, "BC");
    }
    
    @Override
    public void verify(final PublicKey publicKey, final String provider) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (!this.c.getSignatureAlgorithm().equals(this.c.getTBSCertList().getSignature())) {
            throw new CRLException("Signature algorithm on CertificateList does not match TBSCertList.");
        }
        final Signature instance = Signature.getInstance(this.getSigAlgName(), provider);
        instance.initVerify(publicKey);
        instance.update(this.getTBSCertList());
        if (!instance.verify(this.getSignature())) {
            throw new SignatureException("CRL does not verify with supplied public key.");
        }
    }
    
    @Override
    public int getVersion() {
        return this.c.getVersion();
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
    public Date getThisUpdate() {
        return this.c.getThisUpdate().getDate();
    }
    
    @Override
    public Date getNextUpdate() {
        if (this.c.getNextUpdate() != null) {
            return this.c.getNextUpdate().getDate();
        }
        return null;
    }
    
    private Set loadCRLEntries() {
        final HashSet<X509CRLEntryObject> set = new HashSet<X509CRLEntryObject>();
        final Enumeration revokedCertificateEnumeration = this.c.getRevokedCertificateEnumeration();
        X500Principal x500Principal = this.getIssuerX500Principal();
        while (revokedCertificateEnumeration.hasMoreElements()) {
            final X509CRLEntryObject x509CRLEntryObject = new X509CRLEntryObject(revokedCertificateEnumeration.nextElement(), this.isIndirect, x500Principal);
            set.add(x509CRLEntryObject);
            x500Principal = x509CRLEntryObject.getCertificateIssuer();
        }
        return set;
    }
    
    @Override
    public X509CRLEntry getRevokedCertificate(final BigInteger bigInteger) {
        final Enumeration revokedCertificateEnumeration = this.c.getRevokedCertificateEnumeration();
        X500Principal x500Principal = this.getIssuerX500Principal();
        while (revokedCertificateEnumeration.hasMoreElements()) {
            final TBSCertList.CRLEntry crlEntry = revokedCertificateEnumeration.nextElement();
            final X509CRLEntryObject x509CRLEntryObject = new X509CRLEntryObject(crlEntry, this.isIndirect, x500Principal);
            if (bigInteger.equals(crlEntry.getUserCertificate().getValue())) {
                return x509CRLEntryObject;
            }
            x500Principal = x509CRLEntryObject.getCertificateIssuer();
        }
        return null;
    }
    
    @Override
    public Set getRevokedCertificates() {
        final Set loadCRLEntries = this.loadCRLEntries();
        if (!loadCRLEntries.isEmpty()) {
            return Collections.unmodifiableSet((Set<?>)loadCRLEntries);
        }
        return null;
    }
    
    @Override
    public byte[] getTBSCertList() throws CRLException {
        try {
            return this.c.getTBSCertList().getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    @Override
    public byte[] getSignature() {
        return this.c.getSignature().getBytes();
    }
    
    @Override
    public String getSigAlgName() {
        return this.sigAlgName;
    }
    
    @Override
    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getObjectId().getId();
    }
    
    @Override
    public byte[] getSigAlgParams() {
        if (this.sigAlgParams != null) {
            final byte[] array = new byte[this.sigAlgParams.length];
            System.arraycopy(this.sigAlgParams, 0, array, 0, array.length);
            return array;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("              Version: ").append(this.getVersion()).append(property);
        sb.append("             IssuerDN: ").append(this.getIssuerDN()).append(property);
        sb.append("          This update: ").append(this.getThisUpdate()).append(property);
        sb.append("          Next update: ").append(this.getNextUpdate()).append(property);
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
        final X509Extensions extensions = this.c.getTBSCertList().getExtensions();
        if (extensions != null) {
            final Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                sb.append("           Extensions: ").append(property);
            }
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(derObjectIdentifier);
                if (extension.getValue() != null) {
                    final ASN1InputStream asn1InputStream = new ASN1InputStream(extension.getValue().getOctets());
                    sb.append("                       critical(").append(extension.isCritical()).append(") ");
                    try {
                        if (derObjectIdentifier.equals(X509Extensions.CRLNumber)) {
                            sb.append(new CRLNumber(DERInteger.getInstance(asn1InputStream.readObject()).getPositiveValue())).append(property);
                        }
                        else if (derObjectIdentifier.equals(X509Extensions.DeltaCRLIndicator)) {
                            sb.append("Base CRL: " + new CRLNumber(DERInteger.getInstance(asn1InputStream.readObject()).getPositiveValue())).append(property);
                        }
                        else if (derObjectIdentifier.equals(X509Extensions.IssuingDistributionPoint)) {
                            sb.append(new IssuingDistributionPoint((ASN1Sequence)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(X509Extensions.CRLDistributionPoints)) {
                            sb.append(new CRLDistPoint((ASN1Sequence)asn1InputStream.readObject())).append(property);
                        }
                        else if (derObjectIdentifier.equals(X509Extensions.FreshestCRL)) {
                            sb.append(new CRLDistPoint((ASN1Sequence)asn1InputStream.readObject())).append(property);
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
        final Set revokedCertificates = this.getRevokedCertificates();
        if (revokedCertificates != null) {
            final Iterator<Object> iterator = revokedCertificates.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                sb.append(property);
            }
        }
        return sb.toString();
    }
    
    @Override
    public boolean isRevoked(final Certificate certificate) {
        if (!certificate.getType().equals("X.509")) {
            throw new RuntimeException("X.509 CRL used with non X.509 Cert");
        }
        final TBSCertList.CRLEntry[] revokedCertificates = this.c.getRevokedCertificates();
        if (revokedCertificates != null) {
            final BigInteger serialNumber = ((X509Certificate)certificate).getSerialNumber();
            for (int i = 0; i < revokedCertificates.length; ++i) {
                if (revokedCertificates[i].getUserCertificate().getValue().equals(serialNumber)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isIndirectCRL() throws CRLException {
        final byte[] extensionValue = this.getExtensionValue(X509Extensions.IssuingDistributionPoint.getId());
        boolean indirectCRL = false;
        try {
            if (extensionValue != null) {
                indirectCRL = IssuingDistributionPoint.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue)).isIndirectCRL();
            }
        }
        catch (Exception ex) {
            throw new ExtCRLException("Exception reading IssuingDistributionPoint", ex);
        }
        return indirectCRL;
    }
}
