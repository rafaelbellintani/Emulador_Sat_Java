// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.security.Signature;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStoreParameters;
import java.util.Collection;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.CertStore;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateFactory;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ocsp.SingleResponse;
import java.text.ParseException;
import java.util.Date;
import java.io.IOException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import java.security.cert.X509Extension;

public class BasicOCSPResp implements X509Extension
{
    BasicOCSPResponse resp;
    ResponseData data;
    X509Certificate[] chain;
    
    public BasicOCSPResp(final BasicOCSPResponse resp) {
        this.chain = null;
        this.resp = resp;
        this.data = resp.getTbsResponseData();
    }
    
    public byte[] getTBSResponseData() throws OCSPException {
        try {
            return this.resp.getTbsResponseData().getEncoded();
        }
        catch (IOException ex) {
            throw new OCSPException("problem encoding tbsResponseData", ex);
        }
    }
    
    public int getVersion() {
        return this.data.getVersion().getValue().intValue() + 1;
    }
    
    public RespID getResponderId() {
        return new RespID(this.data.getResponderID());
    }
    
    public Date getProducedAt() {
        try {
            return this.data.getProducedAt().getDate();
        }
        catch (ParseException ex) {
            throw new IllegalStateException("ParseException:" + ex.getMessage());
        }
    }
    
    public SingleResp[] getResponses() {
        final ASN1Sequence responses = this.data.getResponses();
        final SingleResp[] array = new SingleResp[responses.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = new SingleResp(SingleResponse.getInstance(responses.getObjectAt(i)));
        }
        return array;
    }
    
    public X509Extensions getResponseExtensions() {
        return this.data.getResponseExtensions();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final HashSet<String> set = new HashSet<String>();
        final X509Extensions responseExtensions = this.getResponseExtensions();
        if (responseExtensions != null) {
            final Enumeration oids = responseExtensions.oids();
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                if (b == responseExtensions.getExtension(derObjectIdentifier).isCritical()) {
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
        final X509Extensions responseExtensions = this.getResponseExtensions();
        if (responseExtensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = responseExtensions.getExtension(new DERObjectIdentifier(s));
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
    
    public String getSignatureAlgName() {
        return OCSPUtil.getAlgorithmName(this.resp.getSignatureAlgorithm().getObjectId());
    }
    
    public String getSignatureAlgOID() {
        return this.resp.getSignatureAlgorithm().getObjectId().getId();
    }
    
    @Deprecated
    public RespData getResponseData() {
        return new RespData(this.resp.getTbsResponseData());
    }
    
    public byte[] getSignature() {
        return this.resp.getSignature().getBytes();
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
        final ASN1Sequence certs = this.resp.getCerts();
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
        final List certList = this.getCertList(s);
        return certList.toArray(new X509Certificate[certList.size()]);
    }
    
    public CertStore getCertificates(final String s, final String s2) throws NoSuchAlgorithmException, NoSuchProviderException, OCSPException {
        try {
            return OCSPUtil.createCertStoreInstance(s, new CollectionCertStoreParameters(this.getCertList(s2)), s2);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new OCSPException("can't setup the CertStore", ex);
        }
    }
    
    public boolean verify(final PublicKey publicKey, final String s) throws OCSPException, NoSuchProviderException {
        try {
            final Signature signatureInstance = OCSPUtil.createSignatureInstance(this.getSignatureAlgName(), s);
            signatureInstance.initVerify(publicKey);
            signatureInstance.update(this.resp.getTbsResponseData().getEncoded("DER"));
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
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this.resp);
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof BasicOCSPResp && this.resp.equals(((BasicOCSPResp)o).resp));
    }
    
    @Override
    public int hashCode() {
        return this.resp.hashCode();
    }
}
