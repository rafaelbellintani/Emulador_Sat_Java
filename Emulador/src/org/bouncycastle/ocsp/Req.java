// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ocsp.Request;
import java.security.cert.X509Extension;

public class Req implements X509Extension
{
    private Request req;
    
    public Req(final Request req) {
        this.req = req;
    }
    
    public CertificateID getCertID() {
        return new CertificateID(this.req.getReqCert());
    }
    
    public X509Extensions getSingleRequestExtensions() {
        return this.req.getSingleRequestExtensions();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final HashSet<String> set = new HashSet<String>();
        final X509Extensions singleRequestExtensions = this.getSingleRequestExtensions();
        if (singleRequestExtensions != null) {
            final Enumeration oids = singleRequestExtensions.oids();
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                if (b == singleRequestExtensions.getExtension(derObjectIdentifier).isCritical()) {
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
        final X509Extensions singleRequestExtensions = this.getSingleRequestExtensions();
        if (singleRequestExtensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = singleRequestExtensions.getExtension(new DERObjectIdentifier(s));
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
