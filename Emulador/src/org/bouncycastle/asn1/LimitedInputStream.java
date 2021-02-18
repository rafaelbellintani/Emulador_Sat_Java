// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.InputStream;

abstract class LimitedInputStream extends InputStream
{
    protected final InputStream _in;
    
    LimitedInputStream(final InputStream in) {
        this._in = in;
    }
    
    protected void setParentEofDetect(final boolean eofOn00) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)this._in).setEofOn00(eofOn00);
        }
    }
}
