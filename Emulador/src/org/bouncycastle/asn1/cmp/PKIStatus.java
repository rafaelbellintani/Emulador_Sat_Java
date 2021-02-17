// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIStatus extends ASN1Encodable
{
    public static final int GRANTED = 0;
    public static final int GRANTED_WITH_MODS = 1;
    public static final int REJECTION = 2;
    public static final int WAITING = 3;
    public static final int REVOCATION_WARNING = 4;
    public static final int REVOCATION_NOTIFICATION = 5;
    public static final int KEY_UPDATE_WARNING = 6;
    public static final PKIStatus granted;
    public static final PKIStatus grantedWithMods;
    public static final PKIStatus rejection;
    public static final PKIStatus waiting;
    public static final PKIStatus revocationWarning;
    public static final PKIStatus revocationNotification;
    public static final PKIStatus keyUpdateWaiting;
    private DERInteger value;
    
    private PKIStatus(final int n) {
        this(new DERInteger(n));
    }
    
    private PKIStatus(final DERInteger value) {
        this.value = value;
    }
    
    public static PKIStatus getInstance(final Object o) {
        if (o instanceof PKIStatus) {
            return (PKIStatus)o;
        }
        if (o instanceof DERInteger) {
            return new PKIStatus((DERInteger)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.value;
    }
    
    static {
        granted = new PKIStatus(0);
        grantedWithMods = new PKIStatus(1);
        rejection = new PKIStatus(2);
        waiting = new PKIStatus(3);
        revocationWarning = new PKIStatus(4);
        revocationNotification = new PKIStatus(5);
        keyUpdateWaiting = new PKIStatus(6);
    }
}
