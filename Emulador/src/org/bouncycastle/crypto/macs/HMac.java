// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ExtendedDigest;
import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;

public class HMac implements Mac
{
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private int blockLength;
    private byte[] inputPad;
    private byte[] outputPad;
    private static Hashtable blockLengths;
    
    private static int getByteLength(final Digest digest) {
        if (digest instanceof ExtendedDigest) {
            return ((ExtendedDigest)digest).getByteLength();
        }
        final Integer n = HMac.blockLengths.get(digest.getAlgorithmName());
        if (n == null) {
            throw new IllegalArgumentException("unknown digest passed: " + digest.getAlgorithmName());
        }
        return n;
    }
    
    public HMac(final Digest digest) {
        this(digest, getByteLength(digest));
    }
    
    private HMac(final Digest digest, final int blockLength) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
        this.blockLength = blockLength;
        this.inputPad = new byte[this.blockLength];
        this.outputPad = new byte[this.blockLength];
    }
    
    @Override
    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/HMAC";
    }
    
    public Digest getUnderlyingDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.digest.reset();
        final byte[] key = ((KeyParameter)cipherParameters).getKey();
        if (key.length > this.blockLength) {
            this.digest.update(key, 0, key.length);
            this.digest.doFinal(this.inputPad, 0);
            for (int i = this.digestSize; i < this.inputPad.length; ++i) {
                this.inputPad[i] = 0;
            }
        }
        else {
            System.arraycopy(key, 0, this.inputPad, 0, key.length);
            for (int j = key.length; j < this.inputPad.length; ++j) {
                this.inputPad[j] = 0;
            }
        }
        this.outputPad = new byte[this.inputPad.length];
        System.arraycopy(this.inputPad, 0, this.outputPad, 0, this.inputPad.length);
        for (int k = 0; k < this.inputPad.length; ++k) {
            final byte[] inputPad = this.inputPad;
            final int n = k;
            inputPad[n] ^= 0x36;
        }
        for (int l = 0; l < this.outputPad.length; ++l) {
            final byte[] outputPad = this.outputPad;
            final int n2 = l;
            outputPad[n2] ^= 0x5C;
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }
    
    @Override
    public int getMacSize() {
        return this.digestSize;
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] array2 = new byte[this.digestSize];
        this.digest.doFinal(array2, 0);
        this.digest.update(this.outputPad, 0, this.outputPad.length);
        this.digest.update(array2, 0, array2.length);
        final int doFinal = this.digest.doFinal(array, n);
        this.reset();
        return doFinal;
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }
    
    static {
        (HMac.blockLengths = new Hashtable()).put("GOST3411", new Integer(32));
        HMac.blockLengths.put("MD2", new Integer(16));
        HMac.blockLengths.put("MD4", new Integer(64));
        HMac.blockLengths.put("MD5", new Integer(64));
        HMac.blockLengths.put("RIPEMD128", new Integer(64));
        HMac.blockLengths.put("RIPEMD160", new Integer(64));
        HMac.blockLengths.put("SHA-1", new Integer(64));
        HMac.blockLengths.put("SHA-224", new Integer(64));
        HMac.blockLengths.put("SHA-256", new Integer(64));
        HMac.blockLengths.put("SHA-384", new Integer(128));
        HMac.blockLengths.put("SHA-512", new Integer(128));
        HMac.blockLengths.put("Tiger", new Integer(64));
        HMac.blockLengths.put("Whirlpool", new Integer(64));
    }
}
