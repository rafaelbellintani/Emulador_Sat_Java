// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.text.ParseException;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.DERGeneralizedTime;
import java.util.Date;
import org.bouncycastle.asn1.ocsp.RevokedInfo;

public class RevokedStatus implements CertificateStatus
{
    RevokedInfo info;
    
    public RevokedStatus(final RevokedInfo info) {
        this.info = info;
    }
    
    public RevokedStatus(final Date date, final int n) {
        this.info = new RevokedInfo(new DERGeneralizedTime(date), new CRLReason(n));
    }
    
    public Date getRevocationTime() {
        try {
            return this.info.getRevocationTime().getDate();
        }
        catch (ParseException ex) {
            throw new IllegalStateException("ParseException:" + ex.getMessage());
        }
    }
    
    public boolean hasRevocationReason() {
        return this.info.getRevocationReason() != null;
    }
    
    public int getRevocationReason() {
        if (this.info.getRevocationReason() == null) {
            throw new IllegalStateException("attempt to get a reason where none is available");
        }
        return this.info.getRevocationReason().getValue().intValue();
    }
}
