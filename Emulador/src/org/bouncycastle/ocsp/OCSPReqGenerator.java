// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import org.bouncycastle.asn1.ocsp.Request;
import java.util.Iterator;
import org.bouncycastle.asn1.ocsp.OCSPRequest;
import org.bouncycastle.asn1.ocsp.Signature;
import java.security.cert.CertificateEncodingException;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERBitString;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchProviderException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ocsp.TBSRequest;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.jce.X509Principal;
import javax.security.auth.x500.X500Principal;
import java.util.ArrayList;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import java.util.List;

public class OCSPReqGenerator
{
    private List list;
    private GeneralName requestorName;
    private X509Extensions requestExtensions;
    
    public OCSPReqGenerator() {
        this.list = new ArrayList();
        this.requestorName = null;
        this.requestExtensions = null;
    }
    
    public void addRequest(final CertificateID certificateID) {
        this.list.add(new RequestObject(certificateID, null));
    }
    
    public void addRequest(final CertificateID certificateID, final X509Extensions x509Extensions) {
        this.list.add(new RequestObject(certificateID, x509Extensions));
    }
    
    public void setRequestorName(final X500Principal x500Principal) {
        try {
            this.requestorName = new GeneralName(4, new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("cannot encode principal: " + obj);
        }
    }
    
    public void setRequestorName(final GeneralName requestorName) {
        this.requestorName = requestorName;
    }
    
    public void setRequestExtensions(final X509Extensions requestExtensions) {
        this.requestExtensions = requestExtensions;
    }
    
    private OCSPReq generateRequest(final DERObjectIdentifier derObjectIdentifier, final PrivateKey privateKey, final X509Certificate[] array, final String s, final SecureRandom random) throws OCSPException, NoSuchProviderException {
        final Iterator<RequestObject> iterator = this.list.iterator();
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        while (iterator.hasNext()) {
            try {
                asn1EncodableVector.add(iterator.next().toRequest());
                continue;
            }
            catch (Exception ex) {
                throw new OCSPException("exception creating Request", ex);
            }
            break;
        }
        final TBSRequest tbsRequest = new TBSRequest(this.requestorName, new DERSequence(asn1EncodableVector), this.requestExtensions);
        Signature signature = null;
        if (derObjectIdentifier != null) {
            if (this.requestorName == null) {
                throw new OCSPException("requestorName must be specified if request is signed.");
            }
            java.security.Signature signatureInstance;
            try {
                signatureInstance = OCSPUtil.createSignatureInstance(derObjectIdentifier.getId(), s);
                if (random != null) {
                    signatureInstance.initSign(privateKey, random);
                }
                else {
                    signatureInstance.initSign(privateKey);
                }
            }
            catch (NoSuchProviderException ex2) {
                throw ex2;
            }
            catch (GeneralSecurityException obj) {
                throw new OCSPException("exception creating signature: " + obj, obj);
            }
            DERBitString derBitString;
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                new ASN1OutputStream(byteArrayOutputStream).writeObject(tbsRequest);
                signatureInstance.update(byteArrayOutputStream.toByteArray());
                derBitString = new DERBitString(signatureInstance.sign());
            }
            catch (Exception obj2) {
                throw new OCSPException("exception processing TBSRequest: " + obj2, obj2);
            }
            final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(derObjectIdentifier, new DERNull());
            if (array != null && array.length > 0) {
                final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
                try {
                    for (int i = 0; i != array.length; ++i) {
                        asn1EncodableVector2.add(new X509CertificateStructure((ASN1Sequence)ASN1Object.fromByteArray(array[i].getEncoded())));
                    }
                }
                catch (IOException ex3) {
                    throw new OCSPException("error processing certs", ex3);
                }
                catch (CertificateEncodingException ex4) {
                    throw new OCSPException("error encoding certs", ex4);
                }
                signature = new Signature(algorithmIdentifier, derBitString, new DERSequence(asn1EncodableVector2));
            }
            else {
                signature = new Signature(algorithmIdentifier, derBitString);
            }
        }
        return new OCSPReq(new OCSPRequest(tbsRequest, signature));
    }
    
    public OCSPReq generate() throws OCSPException {
        try {
            return this.generateRequest(null, null, null, null, null);
        }
        catch (NoSuchProviderException obj) {
            throw new OCSPException("no provider! - " + obj, obj);
        }
    }
    
    public OCSPReq generate(final String s, final PrivateKey privateKey, final X509Certificate[] array, final String s2) throws OCSPException, NoSuchProviderException, IllegalArgumentException {
        return this.generate(s, privateKey, array, s2, null);
    }
    
    public OCSPReq generate(final String str, final PrivateKey privateKey, final X509Certificate[] array, final String s, final SecureRandom secureRandom) throws OCSPException, NoSuchProviderException, IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("no signing algorithm specified");
        }
        try {
            return this.generateRequest(OCSPUtil.getAlgorithmOID(str), privateKey, array, s, secureRandom);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("unknown signing algorithm specified: " + str);
        }
    }
    
    public Iterator getSignatureAlgNames() {
        return OCSPUtil.getAlgNames();
    }
    
    private class RequestObject
    {
        CertificateID certId;
        X509Extensions extensions;
        
        public RequestObject(final CertificateID certId, final X509Extensions extensions) {
            this.certId = certId;
            this.extensions = extensions;
        }
        
        public Request toRequest() throws Exception {
            return new Request(this.certId.toASN1Object(), this.extensions);
        }
    }
}
