// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.Digest;

public class CombinedHash implements Digest
{
    private Digest md5;
    private Digest sha1;
    
    public CombinedHash() {
        this.md5 = new MD5Digest();
        this.sha1 = new SHA1Digest();
    }
    
    @Override
    public String getAlgorithmName() {
        return this.md5.getAlgorithmName() + " and " + this.sha1.getAlgorithmName() + " for TLS 1.0";
    }
    
    @Override
    public int getDigestSize() {
        return 36;
    }
    
    @Override
    public void update(final byte b) {
        this.md5.update(b);
        this.sha1.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.md5.update(array, n, n2);
        this.sha1.update(array, n, n2);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        return this.md5.doFinal(array, n) + this.sha1.doFinal(array, n + 16);
    }
    
    @Override
    public void reset() {
        this.md5.reset();
        this.sha1.reset();
    }
}
