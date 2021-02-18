// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.ocsp.ResponseBytes;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import java.io.InputStream;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ocsp.OCSPResponse;

public class OCSPResp
{
    private OCSPResponse resp;
    
    public OCSPResp(final OCSPResponse resp) {
        this.resp = resp;
    }
    
    public OCSPResp(final byte[] array) throws IOException {
        this(new ASN1InputStream(array));
    }
    
    public OCSPResp(final InputStream inputStream) throws IOException {
        this(new ASN1InputStream(inputStream));
    }
    
    private OCSPResp(final ASN1InputStream asn1InputStream) throws IOException {
        try {
            this.resp = OCSPResponse.getInstance(asn1InputStream.readObject());
        }
        catch (IllegalArgumentException ex) {
            throw new IOException("malformed response: " + ex.getMessage());
        }
        catch (ClassCastException ex2) {
            throw new IOException("malformed response: " + ex2.getMessage());
        }
    }
    
    public int getStatus() {
        return this.resp.getResponseStatus().getValue().intValue();
    }
    
    public Object getResponseObject() throws OCSPException {
        final ResponseBytes responseBytes = this.resp.getResponseBytes();
        if (responseBytes == null) {
            return null;
        }
        if (responseBytes.getResponseType().equals(OCSPObjectIdentifiers.id_pkix_ocsp_basic)) {
            try {
                return new BasicOCSPResp(BasicOCSPResponse.getInstance(new ASN1InputStream(responseBytes.getResponse().getOctets()).readObject()));
            }
            catch (Exception obj) {
                throw new OCSPException("problem decoding object: " + obj, obj);
            }
        }
        return responseBytes.getResponse();
    }
    
    public byte[] getEncoded() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this.resp);
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof OCSPResp && this.resp.equals(((OCSPResp)o).resp));
    }
    
    @Override
    public int hashCode() {
        return this.resp.hashCode();
    }
}
