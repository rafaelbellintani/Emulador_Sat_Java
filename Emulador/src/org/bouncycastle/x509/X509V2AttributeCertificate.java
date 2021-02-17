// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import org.bouncycastle.util.Arrays;
import java.util.ArrayList;
import org.bouncycastle.asn1.ASN1Encodable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.PublicKey;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Sequence;
import java.math.BigInteger;
import java.text.ParseException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.InputStream;
import java.util.Date;
import org.bouncycastle.asn1.x509.AttributeCertificate;

public class X509V2AttributeCertificate implements X509AttributeCertificate
{
    private AttributeCertificate cert;
    private Date notBefore;
    private Date notAfter;
    
    public X509V2AttributeCertificate(final InputStream inputStream) throws IOException {
        this(AttributeCertificate.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    public X509V2AttributeCertificate(final byte[] buf) throws IOException {
        this(new ByteArrayInputStream(buf));
    }
    
    X509V2AttributeCertificate(final AttributeCertificate cert) throws IOException {
        this.cert = cert;
        try {
            this.notAfter = cert.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime().getDate();
            this.notBefore = cert.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime().getDate();
        }
        catch (ParseException ex) {
            throw new IOException("invalid data structure in certificate!");
        }
    }
    
    @Override
    public int getVersion() {
        return this.cert.getAcinfo().getVersion().getValue().intValue() + 1;
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.cert.getAcinfo().getSerialNumber().getValue();
    }
    
    @Override
    public AttributeCertificateHolder getHolder() {
        return new AttributeCertificateHolder((ASN1Sequence)this.cert.getAcinfo().getHolder().toASN1Object());
    }
    
    @Override
    public AttributeCertificateIssuer getIssuer() {
        return new AttributeCertificateIssuer(this.cert.getAcinfo().getIssuer());
    }
    
    @Override
    public Date getNotBefore() {
        return this.notBefore;
    }
    
    @Override
    public Date getNotAfter() {
        return this.notAfter;
    }
    
    @Override
    public boolean[] getIssuerUniqueID() {
        final DERBitString issuerUniqueID = this.cert.getAcinfo().getIssuerUniqueID();
        if (issuerUniqueID != null) {
            final byte[] bytes = issuerUniqueID.getBytes();
            final boolean[] array = new boolean[bytes.length * 8 - issuerUniqueID.getPadBits()];
            for (int i = 0; i != array.length; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.checkValidity(new Date());
    }
    
    @Override
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        if (date.after(this.getNotAfter())) {
            throw new CertificateExpiredException("certificate expired on " + this.getNotAfter());
        }
        if (date.before(this.getNotBefore())) {
            throw new CertificateNotYetValidException("certificate not valid till " + this.getNotBefore());
        }
    }
    
    @Override
    public byte[] getSignature() {
        return this.cert.getSignatureValue().getBytes();
    }
    
    @Override
    public final void verify(final PublicKey publicKey, final String provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (!this.cert.getSignatureAlgorithm().equals(this.cert.getAcinfo().getSignature())) {
            throw new CertificateException("Signature algorithm in certificate info not same as outer certificate");
        }
        final Signature instance = Signature.getInstance(this.cert.getSignatureAlgorithm().getObjectId().getId(), provider);
        instance.initVerify(publicKey);
        try {
            instance.update(this.cert.getAcinfo().getEncoded());
        }
        catch (IOException ex) {
            throw new SignatureException("Exception encoding certificate info object");
        }
        if (!instance.verify(this.getSignature())) {
            throw new InvalidKeyException("Public key presented not for certificate signature");
        }
    }
    
    @Override
    public byte[] getEncoded() throws IOException {
        return this.cert.getEncoded();
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final X509Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = extensions.getExtension(new DERObjectIdentifier(s));
            if (extension != null) {
                try {
                    return extension.getValue().getEncoded("DER");
                }
                catch (Exception ex) {
                    throw new RuntimeException("error encoding " + ex.toString());
                }
            }
        }
        return null;
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final X509Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            final HashSet<String> set = new HashSet<String>();
            final Enumeration oids = extensions.oids();
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                if (extensions.getExtension(derObjectIdentifier).isCritical() == b) {
                    set.add(derObjectIdentifier.getId());
                }
            }
            return set;
        }
        return null;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    @Override
    public X509Attribute[] getAttributes() {
        final ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        final X509Attribute[] array = new X509Attribute[attributes.size()];
        for (int i = 0; i != attributes.size(); ++i) {
            array[i] = new X509Attribute((ASN1Encodable)attributes.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public X509Attribute[] getAttributes(final String anObject) {
        final ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        final ArrayList<X509Attribute> list = new ArrayList<X509Attribute>();
        for (int i = 0; i != attributes.size(); ++i) {
            final X509Attribute x509Attribute = new X509Attribute((ASN1Encodable)attributes.getObjectAt(i));
            if (x509Attribute.getOID().equals(anObject)) {
                list.add(x509Attribute);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return list.toArray(new X509Attribute[list.size()]);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509AttributeCertificate)) {
            return false;
        }
        final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)o;
        try {
            return Arrays.areEqual(this.getEncoded(), x509AttributeCertificate.getEncoded());
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        try {
            return Arrays.hashCode(this.getEncoded());
        }
        catch (IOException ex) {
            return 0;
        }
    }
}
