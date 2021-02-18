// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.text.ParseException;
import java.util.Date;
import org.bouncycastle.asn1.ocsp.CertStatus;
import org.bouncycastle.asn1.ocsp.RevokedInfo;
import org.bouncycastle.asn1.ocsp.SingleResponse;
import java.security.cert.X509Extension;

public class SingleResp implements X509Extension
{
    SingleResponse resp;
    
    public SingleResp(final SingleResponse resp) {
        this.resp = resp;
    }
    
    public CertificateID getCertID() {
        return new CertificateID(this.resp.getCertID());
    }
    
    public Object getCertStatus() {
        final CertStatus certStatus = this.resp.getCertStatus();
        if (certStatus.getTagNo() == 0) {
            return null;
        }
        if (certStatus.getTagNo() == 1) {
            return new RevokedStatus(RevokedInfo.getInstance(certStatus.getStatus()));
        }
        return new UnknownStatus();
    }
    
    public Date getThisUpdate() {
        try {
            return this.resp.getThisUpdate().getDate();
        }
        catch (ParseException ex) {
            throw new IllegalStateException("ParseException: " + ex.getMessage());
        }
    }
    
    public Date getNextUpdate() {
        if (this.resp.getNextUpdate() == null) {
            return null;
        }
        try {
            return this.resp.getNextUpdate().getDate();
        }
        catch (ParseException ex) {
            throw new IllegalStateException("ParseException: " + ex.getMessage());
        }
    }
    
    public X509Extensions getSingleExtensions() {
        return this.resp.getSingleExtensions();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final HashSet<String> set = new HashSet<String>();
        final X509Extensions singleExtensions = this.getSingleExtensions();
        if (singleExtensions != null) {
            final Enumeration oids = singleExtensions.oids();
            while (oids.hasMoreElements()) {
                final DERObjectIdentifier derObjectIdentifier = oids.nextElement();
                if (b == singleExtensions.getExtension(derObjectIdentifier).isCritical()) {
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
        final X509Extensions singleExtensions = this.getSingleExtensions();
        if (singleExtensions != null) {
            final org.bouncycastle.asn1.x509.X509Extension extension = singleExtensions.getExtension(new DERObjectIdentifier(s));
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
