// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREnumerated;

public class OCSPResponseStatus extends DEREnumerated
{
    public static final int SUCCESSFUL = 0;
    public static final int MALFORMED_REQUEST = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int TRY_LATER = 3;
    public static final int SIG_REQUIRED = 5;
    public static final int UNAUTHORIZED = 6;
    
    public OCSPResponseStatus(final int n) {
        super(n);
    }
    
    public OCSPResponseStatus(final DEREnumerated derEnumerated) {
        super(derEnumerated.getValue().intValue());
    }
}
