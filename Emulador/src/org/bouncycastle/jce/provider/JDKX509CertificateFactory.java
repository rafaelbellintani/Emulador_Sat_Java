// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.X509Certificate;
import java.util.List;
import java.security.cert.CertPath;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.security.cert.CertificateException;
import java.io.PushbackInputStream;
import java.security.cert.CRLException;
import java.security.cert.CRL;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.DEREncodable;
import java.security.cert.CertificateParsingException;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.cert.Certificate;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Set;
import java.security.cert.CertificateFactorySpi;

public class JDKX509CertificateFactory extends CertificateFactorySpi
{
    private static final PEMUtil PEM_CERT_PARSER;
    private static final PEMUtil PEM_CRL_PARSER;
    private ASN1Set sData;
    private int sDataObjectCount;
    private InputStream currentStream;
    private ASN1Set sCrlData;
    private int sCrlDataObjectCount;
    private InputStream currentCrlStream;
    
    public JDKX509CertificateFactory() {
        this.sData = null;
        this.sDataObjectCount = 0;
        this.currentStream = null;
        this.sCrlData = null;
        this.sCrlDataObjectCount = 0;
        this.currentCrlStream = null;
    }
    
    private Certificate readDERCertificate(final ASN1InputStream asn1InputStream) throws IOException, CertificateParsingException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)asn1InputStream.readObject();
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof DERObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCertificates();
            return this.getCertificate();
        }
        return new X509CertificateObject(X509CertificateStructure.getInstance(asn1Sequence));
    }
    
    private Certificate getCertificate() throws CertificateParsingException {
        if (this.sData != null) {
            while (this.sDataObjectCount < this.sData.size()) {
                final DEREncodable object = this.sData.getObjectAt(this.sDataObjectCount++);
                if (object instanceof ASN1Sequence) {
                    return new X509CertificateObject(X509CertificateStructure.getInstance(object));
                }
            }
        }
        return null;
    }
    
    private Certificate readPEMCertificate(final InputStream inputStream) throws IOException, CertificateParsingException {
        final ASN1Sequence pemObject = JDKX509CertificateFactory.PEM_CERT_PARSER.readPEMObject(inputStream);
        if (pemObject != null) {
            return new X509CertificateObject(X509CertificateStructure.getInstance(pemObject));
        }
        return null;
    }
    
    protected CRL createCRL(final CertificateList list) throws CRLException {
        return new X509CRLObject(list);
    }
    
    private CRL readPEMCRL(final InputStream inputStream) throws IOException, CRLException {
        final ASN1Sequence pemObject = JDKX509CertificateFactory.PEM_CRL_PARSER.readPEMObject(inputStream);
        if (pemObject != null) {
            return this.createCRL(CertificateList.getInstance(pemObject));
        }
        return null;
    }
    
    private CRL readDERCRL(final ASN1InputStream asn1InputStream) throws IOException, CRLException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)asn1InputStream.readObject();
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof DERObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sCrlData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCRLs();
            return this.getCRL();
        }
        return this.createCRL(CertificateList.getInstance(asn1Sequence));
    }
    
    private CRL getCRL() throws CRLException {
        if (this.sCrlData == null || this.sCrlDataObjectCount >= this.sCrlData.size()) {
            return null;
        }
        return this.createCRL(CertificateList.getInstance(this.sCrlData.getObjectAt(this.sCrlDataObjectCount++)));
    }
    
    @Override
    public Certificate engineGenerateCertificate(final InputStream in) throws CertificateException {
        if (this.currentStream == null) {
            this.currentStream = in;
            this.sData = null;
            this.sDataObjectCount = 0;
        }
        else if (this.currentStream != in) {
            this.currentStream = in;
            this.sData = null;
            this.sDataObjectCount = 0;
        }
        try {
            if (this.sData != null) {
                if (this.sDataObjectCount != this.sData.size()) {
                    return this.getCertificate();
                }
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
            else {
                final int readLimit = ProviderUtil.getReadLimit(in);
                final PushbackInputStream pushbackInputStream = new PushbackInputStream(in);
                final int read = pushbackInputStream.read();
                if (read == -1) {
                    return null;
                }
                pushbackInputStream.unread(read);
                if (read != 48) {
                    return this.readPEMCertificate(pushbackInputStream);
                }
                return this.readDERCertificate(new ASN1InputStream(pushbackInputStream, readLimit));
            }
        }
        catch (Exception cause) {
            throw new CertificateException(cause);
        }
    }
    
    @Override
    public Collection engineGenerateCertificates(final InputStream inputStream) throws CertificateException {
        final ArrayList<Certificate> list = new ArrayList<Certificate>();
        Certificate engineGenerateCertificate;
        while ((engineGenerateCertificate = this.engineGenerateCertificate(inputStream)) != null) {
            list.add(engineGenerateCertificate);
        }
        return list;
    }
    
    @Override
    public CRL engineGenerateCRL(final InputStream in) throws CRLException {
        if (this.currentCrlStream == null) {
            this.currentCrlStream = in;
            this.sCrlData = null;
            this.sCrlDataObjectCount = 0;
        }
        else if (this.currentCrlStream != in) {
            this.currentCrlStream = in;
            this.sCrlData = null;
            this.sCrlDataObjectCount = 0;
        }
        try {
            if (this.sCrlData != null) {
                if (this.sCrlDataObjectCount != this.sCrlData.size()) {
                    return this.getCRL();
                }
                this.sCrlData = null;
                this.sCrlDataObjectCount = 0;
                return null;
            }
            else {
                final int readLimit = ProviderUtil.getReadLimit(in);
                final PushbackInputStream pushbackInputStream = new PushbackInputStream(in);
                final int read = pushbackInputStream.read();
                if (read == -1) {
                    return null;
                }
                pushbackInputStream.unread(read);
                if (read != 48) {
                    return this.readPEMCRL(pushbackInputStream);
                }
                return this.readDERCRL(new ASN1InputStream(pushbackInputStream, readLimit, true));
            }
        }
        catch (CRLException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new CRLException(ex2.toString());
        }
    }
    
    @Override
    public Collection engineGenerateCRLs(final InputStream inputStream) throws CRLException {
        final ArrayList<CRL> list = new ArrayList<CRL>();
        CRL engineGenerateCRL;
        while ((engineGenerateCRL = this.engineGenerateCRL(inputStream)) != null) {
            list.add(engineGenerateCRL);
        }
        return list;
    }
    
    @Override
    public Iterator engineGetCertPathEncodings() {
        return PKIXCertPath.certPathEncodings.iterator();
    }
    
    @Override
    public CertPath engineGenerateCertPath(final InputStream inputStream) throws CertificateException {
        return this.engineGenerateCertPath(inputStream, "PkiPath");
    }
    
    @Override
    public CertPath engineGenerateCertPath(final InputStream inputStream, final String s) throws CertificateException {
        return new PKIXCertPath(inputStream, s);
    }
    
    @Override
    public CertPath engineGenerateCertPath(final List list) throws CertificateException {
        for (final Object next : list) {
            if (next != null && !(next instanceof X509Certificate)) {
                throw new CertificateException("list contains non X509Certificate object while creating CertPath\n" + next.toString());
            }
        }
        return new PKIXCertPath(list);
    }
    
    static {
        PEM_CERT_PARSER = new PEMUtil("CERTIFICATE");
        PEM_CRL_PARSER = new PEMUtil("CRL");
    }
}
