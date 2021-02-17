// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.util.Collections;
import java.util.ListIterator;
import java.io.Writer;
import org.bouncycastle.openssl.PEMWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.security.cert.CertificateEncodingException;
import java.util.Iterator;
import java.security.cert.Certificate;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObject;
import java.security.NoSuchProviderException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.InputStream;
import javax.security.auth.x500.X500Principal;
import java.util.Collection;
import java.util.ArrayList;
import java.security.cert.X509Certificate;
import java.util.List;
import java.security.cert.CertPath;

public class PKIXCertPath extends CertPath
{
    static final List certPathEncodings;
    private List certificates;
    
    private List sortCerts(final List c) {
        if (c.size() < 2) {
            return c;
        }
        X500Principal x500Principal = c.get(0).getIssuerX500Principal();
        boolean b = true;
        for (int i = 1; i != c.size(); ++i) {
            if (!x500Principal.equals(c.get(i).getSubjectX500Principal())) {
                b = false;
                break;
            }
            x500Principal = c.get(i).getIssuerX500Principal();
        }
        if (b) {
            return c;
        }
        final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>(c.size());
        final ArrayList list2 = new ArrayList(c);
        for (int j = 0; j < c.size(); ++j) {
            final X509Certificate x509Certificate = c.get(j);
            boolean b2 = false;
            final X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            for (int k = 0; k != c.size(); ++k) {
                if (c.get(k).getIssuerX500Principal().equals(subjectX500Principal)) {
                    b2 = true;
                    break;
                }
            }
            if (!b2) {
                list.add(x509Certificate);
                c.remove(j);
            }
        }
        if (list.size() > 1) {
            return list2;
        }
        for (int l = 0; l != list.size(); ++l) {
            final X500Principal issuerX500Principal = list.get(l).getIssuerX500Principal();
            for (int n = 0; n < c.size(); ++n) {
                final X509Certificate x509Certificate2 = c.get(n);
                if (issuerX500Principal.equals(x509Certificate2.getSubjectX500Principal())) {
                    list.add(x509Certificate2);
                    c.remove(n);
                    break;
                }
            }
        }
        if (c.size() > 0) {
            return list2;
        }
        return list;
    }
    
    PKIXCertPath(final List c) {
        super("X.509");
        this.certificates = this.sortCerts(new ArrayList(c));
    }
    
    PKIXCertPath(final InputStream in, final String str) throws CertificateException {
        super("X.509");
        try {
            if (str.equalsIgnoreCase("PkiPath")) {
                final DERObject object = new ASN1InputStream(in).readObject();
                if (!(object instanceof ASN1Sequence)) {
                    throw new CertificateException("input stream does not contain a ASN1 SEQUENCE while reading PkiPath encoded data to load CertPath");
                }
                final Enumeration objects = ((ASN1Sequence)object).getObjects();
                this.certificates = new ArrayList();
                final CertificateFactory instance = CertificateFactory.getInstance("X.509", "BC");
                while (objects.hasMoreElements()) {
                    this.certificates.add(0, instance.generateCertificate(new ByteArrayInputStream(objects.nextElement().getEncoded("DER"))));
                }
            }
            else {
                if (!str.equalsIgnoreCase("PKCS7") && !str.equalsIgnoreCase("PEM")) {
                    throw new CertificateException("unsupported encoding: " + str);
                }
                final BufferedInputStream inStream = new BufferedInputStream(in);
                this.certificates = new ArrayList();
                Certificate generateCertificate;
                while ((generateCertificate = CertificateFactory.getInstance("X.509", "BC").generateCertificate(inStream)) != null) {
                    this.certificates.add(generateCertificate);
                }
            }
        }
        catch (IOException ex) {
            throw new CertificateException("IOException throw while decoding CertPath:\n" + ex.toString());
        }
        catch (NoSuchProviderException ex2) {
            throw new CertificateException("BouncyCastle provider not found while trying to get a CertificateFactory:\n" + ex2.toString());
        }
        this.certificates = this.sortCerts(this.certificates);
    }
    
    @Override
    public Iterator getEncodings() {
        return PKIXCertPath.certPathEncodings.iterator();
    }
    
    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        final Iterator encodings = this.getEncodings();
        if (encodings.hasNext()) {
            final String next = encodings.next();
            if (next instanceof String) {
                return this.getEncoded(next);
            }
        }
        return null;
    }
    
    @Override
    public byte[] getEncoded(final String str) throws CertificateEncodingException {
        if (str.equalsIgnoreCase("PkiPath")) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            final ListIterator<X509Certificate> listIterator = this.certificates.listIterator(this.certificates.size());
            while (listIterator.hasPrevious()) {
                asn1EncodableVector.add(this.toASN1Object(listIterator.previous()));
            }
            return this.toDEREncoded(new DERSequence(asn1EncodableVector));
        }
        if (str.equalsIgnoreCase("PKCS7")) {
            final ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, null);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int i = 0; i != this.certificates.size(); ++i) {
                asn1EncodableVector2.add(this.toASN1Object((X509Certificate)this.certificates.get(i)));
            }
            return this.toDEREncoded(new ContentInfo(PKCSObjectIdentifiers.signedData, new SignedData(new DERInteger(1), new DERSet(), contentInfo, new DERSet(asn1EncodableVector2), null, new DERSet())));
        }
        if (str.equalsIgnoreCase("PEM")) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final PEMWriter pemWriter = new PEMWriter(new OutputStreamWriter(out));
            try {
                for (int j = 0; j != this.certificates.size(); ++j) {
                    pemWriter.writeObject(this.certificates.get(j));
                }
                pemWriter.close();
            }
            catch (Exception ex) {
                throw new CertificateEncodingException("can't encode certificate for PEM encoded path");
            }
            return out.toByteArray();
        }
        throw new CertificateEncodingException("unsupported encoding: " + str);
    }
    
    @Override
    public List getCertificates() {
        return Collections.unmodifiableList((List<?>)new ArrayList<Object>(this.certificates));
    }
    
    private DERObject toASN1Object(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new ASN1InputStream(x509Certificate.getEncoded()).readObject();
        }
        catch (Exception ex) {
            throw new CertificateEncodingException("Exception while encoding certificate: " + ex.toString());
        }
    }
    
    private byte[] toDEREncoded(final ASN1Encodable asn1Encodable) throws CertificateEncodingException {
        try {
            return asn1Encodable.getEncoded("DER");
        }
        catch (IOException obj) {
            throw new CertificateEncodingException("Exception thrown: " + obj);
        }
    }
    
    static {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("PkiPath");
        list.add("PEM");
        list.add("PKCS7");
        certPathEncodings = Collections.unmodifiableList((List<?>)list);
    }
}
