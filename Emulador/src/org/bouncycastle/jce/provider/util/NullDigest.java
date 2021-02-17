// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.util;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.Digest;

public class NullDigest implements Digest
{
    private ByteArrayOutputStream bOut;
    
    public NullDigest() {
        this.bOut = new ByteArrayOutputStream();
    }
    
    @Override
    public String getAlgorithmName() {
        return "NULL";
    }
    
    @Override
    public int getDigestSize() {
        return this.bOut.size();
    }
    
    @Override
    public void update(final byte b) {
        this.bOut.write(b);
    }
    
    @Override
    public void update(final byte[] b, final int off, final int len) {
        this.bOut.write(b, off, len);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] byteArray = this.bOut.toByteArray();
        System.arraycopy(byteArray, 0, array, n, byteArray.length);
        this.reset();
        return byteArray.length;
    }
    
    @Override
    public void reset() {
        this.bOut.reset();
    }
}
