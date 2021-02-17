// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.InputStream;

public class ASN1ObjectParser
{
    ASN1StreamParser _aIn;
    
    protected ASN1ObjectParser(final int n, final int n2, final InputStream inputStream) {
        this._aIn = new ASN1StreamParser(inputStream);
    }
}
