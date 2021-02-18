// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.PBEParametersGenerator;

public class PKCS5S2ParametersGenerator extends PBEParametersGenerator
{
    private Mac hMac;
    
    public PKCS5S2ParametersGenerator() {
        this.hMac = new HMac(new SHA1Digest());
    }
    
    private void F(final byte[] array, final byte[] array2, final int n, final byte[] array3, final byte[] array4, final int n2) {
        final byte[] array5 = new byte[this.hMac.getMacSize()];
        final KeyParameter keyParameter = new KeyParameter(array);
        this.hMac.init(keyParameter);
        if (array2 != null) {
            this.hMac.update(array2, 0, array2.length);
        }
        this.hMac.update(array3, 0, array3.length);
        this.hMac.doFinal(array5, 0);
        System.arraycopy(array5, 0, array4, n2, array5.length);
        if (n == 0) {
            throw new IllegalArgumentException("iteration count must be at least 1.");
        }
        for (int i = 1; i < n; ++i) {
            this.hMac.init(keyParameter);
            this.hMac.update(array5, 0, array5.length);
            this.hMac.doFinal(array5, 0);
            for (int j = 0; j != array5.length; ++j) {
                final int n3 = n2 + j;
                array4[n3] ^= array5[j];
            }
        }
    }
    
    private void intToOctet(final byte[] array, final int n) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)n;
    }
    
    private byte[] generateDerivedKey(final int n) {
        final int macSize = this.hMac.getMacSize();
        final int n2 = (n + macSize - 1) / macSize;
        final byte[] array = new byte[4];
        final byte[] array2 = new byte[n2 * macSize];
        for (int i = 1; i <= n2; ++i) {
            this.intToOctet(array, i);
            this.F(this.password, this.salt, this.iterationCount, array, array2, (i - 1) * macSize);
        }
        return array2;
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n) {
        n /= 8;
        return new KeyParameter(this.generateDerivedKey(n), 0, n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n, int n2) {
        n /= 8;
        n2 /= 8;
        final byte[] generateDerivedKey = this.generateDerivedKey(n + n2);
        return new ParametersWithIV(new KeyParameter(generateDerivedKey, 0, n), generateDerivedKey, n, n2);
    }
    
    @Override
    public CipherParameters generateDerivedMacParameters(final int n) {
        return this.generateDerivedParameters(n);
    }
}
