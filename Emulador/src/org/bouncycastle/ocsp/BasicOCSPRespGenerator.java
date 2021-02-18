// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import org.bouncycastle.asn1.ocsp.SingleResponse;
import org.bouncycastle.asn1.ocsp.RevokedInfo;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.ocsp.CertStatus;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import java.security.Signature;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.Iterator;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import java.security.cert.CertificateEncodingException;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.DERBitString;
import java.security.GeneralSecurityException;
import java.security.NoSuchProviderException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.util.Date;
import java.security.PublicKey;
import java.util.ArrayList;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.util.List;

public class BasicOCSPRespGenerator
{
    private List list;
    private X509Extensions responseExtensions;
    private RespID responderID;
    
    public BasicOCSPRespGenerator(final RespID responderID) {
        this.list = new ArrayList();
        this.responseExtensions = null;
        this.responderID = responderID;
    }
    
    public BasicOCSPRespGenerator(final PublicKey publicKey) throws OCSPException {
        this.list = new ArrayList();
        this.responseExtensions = null;
        this.responderID = new RespID(publicKey);
    }
    
    public void addResponse(final CertificateID certificateID, final CertificateStatus certificateStatus) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), null, null));
    }
    
    public void addResponse(final CertificateID certificateID, final CertificateStatus certificateStatus, final X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), null, x509Extensions));
    }
    
    public void addResponse(final CertificateID certificateID, final CertificateStatus certificateStatus, final Date date, final X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, new Date(), date, x509Extensions));
    }
    
    public void addResponse(final CertificateID certificateID, final CertificateStatus certificateStatus, final Date date, final Date date2, final X509Extensions x509Extensions) {
        this.list.add(new ResponseObject(certificateID, certificateStatus, date, date2, x509Extensions));
    }
    
    public void setResponseExtensions(final X509Extensions responseExtensions) {
        this.responseExtensions = responseExtensions;
    }
    
    private BasicOCSPResp generateResponse(final String s, final PrivateKey privateKey, final X509Certificate[] array, final Date date, final String s2, final SecureRandom random) throws OCSPException, NoSuchProviderException {
        final Iterator<ResponseObject> iterator = this.list.iterator();
        DERObjectIdentifier algorithmOID;
        try {
            algorithmOID = OCSPUtil.getAlgorithmOID(s);
        }
        catch (Exception ex5) {
            throw new IllegalArgumentException("unknown signing algorithm specified");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        while (iterator.hasNext()) {
            try {
                asn1EncodableVector.add(iterator.next().toResponse());
                continue;
            }
            catch (Exception ex) {
                throw new OCSPException("exception creating Request", ex);
            }
            break;
        }
        final ResponseData responseData = new ResponseData(this.responderID.toASN1Object(), new DERGeneralizedTime(date), new DERSequence(asn1EncodableVector), this.responseExtensions);
        Signature signatureInstance;
        try {
            signatureInstance = OCSPUtil.createSignatureInstance(s, s2);
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
            signatureInstance.update(responseData.getEncoded("DER"));
            derBitString = new DERBitString(signatureInstance.sign());
        }
        catch (Exception obj2) {
            throw new OCSPException("exception processing TBSRequest: " + obj2, obj2);
        }
        final AlgorithmIdentifier sigAlgID = OCSPUtil.getSigAlgID(algorithmOID);
        ASN1Sequence asn1Sequence = null;
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
            asn1Sequence = new DERSequence(asn1EncodableVector2);
        }
        return new BasicOCSPResp(new BasicOCSPResponse(responseData, sigAlgID, derBitString, asn1Sequence));
    }
    
    public BasicOCSPResp generate(final String s, final PrivateKey privateKey, final X509Certificate[] array, final Date date, final String s2) throws OCSPException, NoSuchProviderException, IllegalArgumentException {
        return this.generate(s, privateKey, array, date, s2, null);
    }
    
    public BasicOCSPResp generate(final String s, final PrivateKey privateKey, final X509Certificate[] array, final Date date, final String s2, final SecureRandom secureRandom) throws OCSPException, NoSuchProviderException, IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("no signing algorithm specified");
        }
        return this.generateResponse(s, privateKey, array, date, s2, secureRandom);
    }
    
    public Iterator getSignatureAlgNames() {
        return OCSPUtil.getAlgNames();
    }
    
    private class ResponseObject
    {
        CertificateID certId;
        CertStatus certStatus;
        DERGeneralizedTime thisUpdate;
        DERGeneralizedTime nextUpdate;
        X509Extensions extensions;
        
        public ResponseObject(final CertificateID certId, final CertificateStatus certificateStatus, final Date date, final Date date2, final X509Extensions extensions) {
            this.certId = certId;
            if (certificateStatus == null) {
                this.certStatus = new CertStatus();
            }
            else if (certificateStatus instanceof UnknownStatus) {
                this.certStatus = new CertStatus(2, new DERNull());
            }
            else {
                final RevokedStatus revokedStatus = (RevokedStatus)certificateStatus;
                if (revokedStatus.hasRevocationReason()) {
                    this.certStatus = new CertStatus(new RevokedInfo(new DERGeneralizedTime(revokedStatus.getRevocationTime()), new CRLReason(revokedStatus.getRevocationReason())));
                }
                else {
                    this.certStatus = new CertStatus(new RevokedInfo(new DERGeneralizedTime(revokedStatus.getRevocationTime()), null));
                }
            }
            this.thisUpdate = new DERGeneralizedTime(date);
            if (date2 != null) {
                this.nextUpdate = new DERGeneralizedTime(date2);
            }
            else {
                this.nextUpdate = null;
            }
            this.extensions = extensions;
        }
        
        public SingleResponse toResponse() throws Exception {
            return new SingleResponse(this.certId.toASN1Object(), this.certStatus, this.thisUpdate, this.nextUpdate, this.extensions);
        }
    }
}
