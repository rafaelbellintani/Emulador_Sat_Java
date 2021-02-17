// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import java.security.Signature;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStoreParameters;
import java.util.Collection;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.CertStore;
import java.security.cert.X509Certificate;
import java.security.NoSuchProviderException;
import java.util.Enumeration;
import java.security.cert.CertificateFactory;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ocsp.Request;
import org.bouncycastle.asn1.x509.GeneralName;
import java.io.InputStream;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ocsp.OCSPRequest;
import java.security.cert.X509Extension;

public class OCSPReq implements X509Extension
{
    private OCSPRequest req;
    
    public OCSPReq(final OCSPRequest req) {
        this.req = req;
    }
    
    public OCSPReq(final byte[] array) throws IOException {
        this(new ASN1InputStream(array));
    }
    
    public OCSPReq(final InputStream inputStream) throws IOException {
        this(new ASN1InputStream(inputStream));
    }
    
    private OCSPReq(final ASN1InputStream asn1InputStream) throws IOException {
        try {
            this.req = OCSPRequest.getInstance(asn1InputStream.readObject());
        }
        catch (IllegalArgumentException ex) {
            throw new IOException("malformed request: " + ex.getMessage());
        }
        catch (ClassCastException ex2) {
            throw new IOException("malformed request: " + ex2.getMessage());
        }
    }
    
    public byte[] getTBSRequest() throws OCSPException {
        try {
            return this.req.getTbsRequest().getEncoded();
        }
        catch (IOException ex) {
            throw new OCSPException("problem encoding tbsRequest", ex);
        }
    }
    
    public int getVersion() {
        return this.req.getTbsRequest().getVersion().getValue().intValue() + 1;
    }
    
    public GeneralName getRequestorName() {
        return GeneralName.getInstance(this.req.getTbsRequest().getRequestorName());
    }
    
    public Req[] getRequestList() {
        final ASN1Sequence requestList = this.req.getTbsRequest().getRequestList();
        final Req[] array = new Req[requestList.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = new Req(Request.getInstance(requestList.getObjectAt(i)));
        }
        return array;
    }
    
    public X509Extensions getRequestExtensions() {
        return X509Extensions.getInstance(this.req.getTbsRequest().getRequestExtensions());
    }
    
    public String getSignatureAlgOID() {
        if (!this.isSigned()) {
            return null;
        }
        return this.req.getOptionalSignature().getSignatureAlgorithm().getObjectId().getId();
    }
    
    public byte[] getSignature() {
        if (!this.isSigned()) {
            return null;
        }
        return this.req.getOptionalSignature().getSignature().getBytes();
    }
    
    private List getCertList(final String s) throws OCSPException, NoSuchProviderException {
        final ArrayList<Certificate> list = new ArrayList<Certificate>();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        CertificateFactory x509CertificateFactory;
        try {
            x509CertificateFactory = OCSPUtil.createX509CertificateFactory(s);
        }
        catch (CertificateException ex) {
            throw new OCSPException("can't get certificate factory.", ex);
        }
        final ASN1Sequence certs = this.req.getOptionalSignature().getCerts();
        if (certs != null) {
            final Enumeration objects = certs.getObjects();
            while (objects.hasMoreElements()) {
                try {
                    asn1OutputStream.writeObject(objects.nextElement());
                    list.add(x509CertificateFactory.generateCertificate(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
                }
                catch (IOException ex2) {
                    throw new OCSPException("can't re-encode certificate!", ex2);
                }
                catch (CertificateException ex3) {
                    throw new OCSPException("can't re-encode certificate!", ex3);
                }
                byteArrayOutputStream.reset();
            }
        }
        return list;
    }
    
    public X509Certificate[] getCerts(final String s) throws OCSPException, NoSuchProviderException {
        if (!this.isSigned()) {
            return null;
        }
        final List certList = this.getCertList(s);
        return certList.toArray(new X509Certificate[certList.size()]);
    }
    
    public CertStore getCertificates(final String s, final String s2) throws NoSuchAlgorithmException, NoSuchProviderException, OCSPException {
        if (!this.isSigned()) {
            return null;
        }
        try {
            return OCSPUtil.createCertStoreInstance(s, new CollectionCertStoreParameters(this.getCertList(s2)), s2);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new OCSPException("can't setup the CertStore", ex);
        }
    }
    
    public boolean isSigned() {
        return this.req.getOptionalSignature() != null;
    }
    
    public boolean verify(final PublicKey publicKey, final String s) throws OCSPException, NoSuchProviderException {
        if (!this.isSigned()) {
            throw new OCSPException("attempt to verify signature on unsigned object");
        }
        try {
            final Signature signatureInstance = OCSPUtil.createSignatureInstance(this.getSignatureAlgOID(), s);
            signatureInstance.initVerify(publicKey);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.req.getTbsRequest());
            signatureInstance.update(byteArrayOutputStream.toByteArray());
            return signatureInstance.verify(this.getSignature());
        }
        catch (NoSuchProviderException ex) {
            throw ex;
        }
        catch (Exception obj) {
            throw new OCSPException("exception processing sig: " + obj, obj);
        }
    }
    
    public byte[] getEncoded() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this.req);
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final HashSet<String> set = new HashSet<String>();
        final X509Extensions requestExtensions = this.getRequestExtensions();
        if (requestExtensions != null) {
            final Enumeration oids = requestExtensions.oids();
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                if (b == requestExtensions.getExtension(derObjectIdentifier).isCritical()) {
                    set.add(derObjectIdentifier.getId());
                }
            }
        }
        return set;
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
        final X509Extensions requestExtensions = this.getRequestExtensions();
        if (requestExtensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = requestExtensions.getExtension(new DERObjectIdentifier(s));
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
}
