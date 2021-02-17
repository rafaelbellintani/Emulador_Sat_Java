// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.signers.DSASigner;
import org.bouncycastle.crypto.signers.DSADigestSigner;

class TlsDSSSigner extends DSADigestSigner
{
    TlsDSSSigner() {
        super(new DSASigner(), new SHA1Digest());
    }
}
