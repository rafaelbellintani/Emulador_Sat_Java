// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ocsp.SingleResponse;
import java.text.ParseException;
import java.util.Date;
import org.bouncycastle.asn1.ocsp.ResponseData;
import java.security.cert.X509Extension;

public class RespData implements X509Extension
{
    ResponseData data;
    
    public RespData(final ResponseData data) {
        this.data = data;
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
}
