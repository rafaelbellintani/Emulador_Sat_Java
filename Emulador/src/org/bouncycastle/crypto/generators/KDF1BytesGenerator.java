// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;

public class KDF1BytesGenerator extends BaseKDFBytesGenerator
{
    public KDF1BytesGenerator(final Digest digest) {
        super(0, digest);
    }
}
