// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.ASN1InputStream;
import java.util.Date;
import java.math.BigInteger;
import java.security.cert.CRLException;
import org.bouncycastle.asn1.x509.X509Extension;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import org.bouncycastle.asn1.x509.GeneralName;
import java.io.IOException;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.TBSCertList;
import java.security.cert.X509CRLEntry;

public class X509CRLEntryObject extends X509CRLEntry
{
    private TBSCertList.CRLEntry c;
    private boolean isIndirect;
    private X500Principal previousCertificateIssuer;
    private X500Principal certificateIssuer;
    private int hashValue;
    private boolean isHashValueSet;
    
    public X509CRLEntryObject(final TBSCertList.CRLEntry c) {
        this.c = c;
        this.certificateIssuer = this.loadCertificateIssuer();
    }
    
    public X509CRLEntryObject(final TBSCertList.CRLEntry c, final boolean isIndirect, final X500Principal previousCertificateIssuer) {
        this.c = c;
        this.isIndirect = isIndirect;
        this.previousCertificateIssuer = previousCertificateIssuer;
        this.certificateIssuer = this.loadCertificateIssuer();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    private X500Principal loadCertificateIssuer() {
        if (!this.isIndirect) {
            return null;
        }
        final byte[] extensionValue = this.getExtensionValue(X509Extensions.CertificateIssuer.getId());
        if (extensionValue == null) {
            return this.previousCertificateIssuer;
        }
        try {
            final GeneralName[] names = GeneralNames.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue)).getNames();
            for (int i = 0; i < names.length; ++i) {
                if (names[i].getTagNo() == 4) {
                    return new X500Principal(names[i].getName().getDERObject().getDEREncoded());
                }
            }
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public X500Principal getCertificateIssuer() {
        return this.certificateIssuer;
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final X509Extensions extensions = this.c.getExtensions();
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
        final X509Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(new DERObjectIdentifier(s));
            if (extension != null) {
                try {
                    return extension.getValue().getEncoded();
                }
                catch (Exception ex) {
                    throw new RuntimeException("error encoding " + ex.toString());
                }
            }
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        if (!this.isHashValueSet) {
            this.hashValue = super.hashCode();
            this.isHashValueSet = true;
        }
        return this.hashValue;
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
    public BigInteger getSerialNumber() {
        return this.c.getUserCertificate().getValue();
    }
    
    @Override
    public Date getRevocationDate() {
        return this.c.getRevocationDate().getDate();
    }
    
    @Override
    public boolean hasExtensions() {
        return this.c.getExtensions() != null;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("      userCertificate: ").append(this.getSerialNumber()).append(property);
        sb.append("       revocationDate: ").append(this.getRevocationDate()).append(property);
        sb.append("       certificateIssuer: ").append(this.getCertificateIssuer()).append(property);
        final X509Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            final Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                sb.append("   crlEntryExtensions:").append(property);
                while (oids.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                    final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(derObjectIdentifier);
                    if (extension.getValue() != null) {
                        final ASN1InputStream asn1InputStream = new ASN1InputStream(extension.getValue().getOctets());
                        sb.append("                       critical(").append(extension.isCritical()).append(") ");
                        try {
                            if (derObjectIdentifier.equals(X509Extensions.ReasonCode)) {
                                sb.append(new CRLReason(DEREnumerated.getInstance(asn1InputStream.readObject()))).append(property);
                            }
                            else if (derObjectIdentifier.equals(X509Extensions.CertificateIssuer)) {
                                sb.append("Certificate issuer: ").append(new GeneralNames((ASN1Sequence)asn1InputStream.readObject())).append(property);
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
        }
        return sb.toString();
    }
}
