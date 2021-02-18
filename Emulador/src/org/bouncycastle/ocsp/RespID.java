// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.security.MessageDigest;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.PublicKey;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ocsp.ResponderID;

public class RespID
{
    ResponderID id;
    
    public RespID(final ResponderID id) {
        this.id = id;
    }
    
    public RespID(final X500Principal x500Principal) {
        try {
            this.id = new ResponderID(new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("can't decode name.");
        }
    }
    
    public RespID(final PublicKey publicKey) throws OCSPException {
        try {
            final MessageDigest digestInstance = OCSPUtil.createDigestInstance("SHA1", null);
            digestInstance.update(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getPublicKeyData().getBytes());
            this.id = new ResponderID(new DEROctetString(digestInstance.digest()));
        }
        catch (Exception obj) {
            throw new OCSPException("problem creating ID: " + obj, obj);
        }
    }
    
    public ResponderID toASN1Object() {
        return this.id;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof RespID && this.id.equals(((RespID)o).id);
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
