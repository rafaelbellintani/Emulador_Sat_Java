// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObjectIdentifier;

public class PolicyQualifierId extends DERObjectIdentifier
{
    private static final String id_qt = "1.3.6.1.5.5.7.2";
    public static final PolicyQualifierId id_qt_cps;
    public static final PolicyQualifierId id_qt_unotice;
    
    private PolicyQualifierId(final String s) {
        super(s);
    }
    
    static {
        id_qt_cps = new PolicyQualifierId("1.3.6.1.5.5.7.2.1");
        id_qt_unotice = new PolicyQualifierId("1.3.6.1.5.5.7.2.2");
    }
}
