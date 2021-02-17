// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREnumerated;

public class CRLReason extends DEREnumerated
{
    @Deprecated
    public static final int UNSPECIFIED = 0;
    @Deprecated
    public static final int KEY_COMPROMISE = 1;
    @Deprecated
    public static final int CA_COMPROMISE = 2;
    @Deprecated
    public static final int AFFILIATION_CHANGED = 3;
    @Deprecated
    public static final int SUPERSEDED = 4;
    @Deprecated
    public static final int CESSATION_OF_OPERATION = 5;
    @Deprecated
    public static final int CERTIFICATE_HOLD = 6;
    @Deprecated
    public static final int REMOVE_FROM_CRL = 8;
    @Deprecated
    public static final int PRIVILEGE_WITHDRAWN = 9;
    @Deprecated
    public static final int AA_COMPROMISE = 10;
    public static final int unspecified = 0;
    public static final int keyCompromise = 1;
    public static final int cACompromise = 2;
    public static final int affiliationChanged = 3;
    public static final int superseded = 4;
    public static final int cessationOfOperation = 5;
    public static final int certificateHold = 6;
    public static final int removeFromCRL = 8;
    public static final int privilegeWithdrawn = 9;
    public static final int aACompromise = 10;
    private static final String[] reasonString;
    
    public CRLReason(final int n) {
        super(n);
    }
    
    public CRLReason(final DEREnumerated derEnumerated) {
        super(derEnumerated.getValue().intValue());
    }
    
    @Override
    public String toString() {
        final int intValue = this.getValue().intValue();
        String str;
        if (intValue < 0 || intValue > 10) {
            str = "invalid";
        }
        else {
            str = CRLReason.reasonString[intValue];
        }
        return "CRLReason: " + str;
    }
    
    static {
        reasonString = new String[] { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
    }
}
