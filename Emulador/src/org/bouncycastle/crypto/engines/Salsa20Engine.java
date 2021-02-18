// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Strings;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MaxBytesExceededException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;

public class Salsa20Engine implements StreamCipher
{
    private static final int stateSize = 16;
    private static final byte[] sigma;
    private static final byte[] tau;
    private int index;
    private int[] engineState;
    private int[] x;
    private byte[] keyStream;
    private byte[] workingKey;
    private byte[] workingIV;
    private boolean initialised;
    private int cW0;
    private int cW1;
    private int cW2;
    
    public Salsa20Engine() {
        this.index = 0;
        this.engineState = new int[16];
        this.x = new int[16];
        this.keyStream = new byte[64];
        this.workingKey = null;
        this.workingIV = null;
        this.initialised = false;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Salsa20 Init parameters must include an IV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        final byte[] iv = parametersWithIV.getIV();
        if (iv == null || iv.length != 8) {
            throw new IllegalArgumentException("Salsa20 requires exactly 8 bytes of IV");
        }
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("Salsa20 Init parameters must include a key");
        }
        this.workingKey = ((KeyParameter)parametersWithIV.getParameters()).getKey();
        this.workingIV = iv;
        this.setKey(this.workingKey, this.workingIV);
    }
    
    @Override
    public String getAlgorithmName() {
        return "Salsa20";
    }
    
    @Override
    public byte returnByte(final byte b) {
        if (this.limitExceeded()) {
            throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
        }
        if (this.index == 0) {
            this.salsa20WordToByte(this.engineState, this.keyStream);
            final int[] engineState = this.engineState;
            final int n = 8;
            ++engineState[n];
            if (this.engineState[8] == 0) {
                final int[] engineState2 = this.engineState;
                final int n2 = 9;
                ++engineState2[n2];
            }
        }
        final byte b2 = (byte)(this.keyStream[this.index] ^ b);
        this.index = (this.index + 1 & 0x3F);
        return b2;
    }
    
    @Override
    public void processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 > array2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.limitExceeded(n2)) {
            throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
        }
        for (int i = 0; i < n2; ++i) {
            if (this.index == 0) {
                this.salsa20WordToByte(this.engineState, this.keyStream);
                final int[] engineState = this.engineState;
                final int n4 = 8;
                ++engineState[n4];
                if (this.engineState[8] == 0) {
                    final int[] engineState2 = this.engineState;
                    final int n5 = 9;
                    ++engineState2[n5];
                }
            }
            array2[i + n3] = (byte)(this.keyStream[this.index] ^ array[i + n]);
            this.index = (this.index + 1 & 0x3F);
        }
    }
    
    @Override
    public void reset() {
        this.setKey(this.workingKey, this.workingIV);
    }
    
    private void setKey(final byte[] workingKey, final byte[] workingIV) {
        this.workingKey = workingKey;
        this.workingIV = workingIV;
        this.index = 0;
        this.resetCounter();
        int n = 0;
        this.engineState[1] = this.byteToIntLittle(this.workingKey, 0);
        this.engineState[2] = this.byteToIntLittle(this.workingKey, 4);
        this.engineState[3] = this.byteToIntLittle(this.workingKey, 8);
        this.engineState[4] = this.byteToIntLittle(this.workingKey, 12);
        byte[] array;
        if (this.workingKey.length == 32) {
            array = Salsa20Engine.sigma;
            n = 16;
        }
        else {
            array = Salsa20Engine.tau;
        }
        this.engineState[11] = this.byteToIntLittle(this.workingKey, n);
        this.engineState[12] = this.byteToIntLittle(this.workingKey, n + 4);
        this.engineState[13] = this.byteToIntLittle(this.workingKey, n + 8);
        this.engineState[14] = this.byteToIntLittle(this.workingKey, n + 12);
        this.engineState[0] = this.byteToIntLittle(array, 0);
        this.engineState[5] = this.byteToIntLittle(array, 4);
        this.engineState[10] = this.byteToIntLittle(array, 8);
        this.engineState[15] = this.byteToIntLittle(array, 12);
        this.engineState[6] = this.byteToIntLittle(this.workingIV, 0);
        this.engineState[7] = this.byteToIntLittle(this.workingIV, 4);
        this.engineState[8] = (this.engineState[9] = 0);
        this.initialised = true;
    }
    
    private void salsa20WordToByte(final int[] array, final byte[] array2) {
        System.arraycopy(array, 0, this.x, 0, array.length);
        for (int i = 0; i < 10; ++i) {
            final int[] x = this.x;
            final int n = 4;
            x[n] ^= this.rotl(this.x[0] + this.x[12], 7);
            final int[] x2 = this.x;
            final int n2 = 8;
            x2[n2] ^= this.rotl(this.x[4] + this.x[0], 9);
            final int[] x3 = this.x;
            final int n3 = 12;
            x3[n3] ^= this.rotl(this.x[8] + this.x[4], 13);
            final int[] x4 = this.x;
            final int n4 = 0;
            x4[n4] ^= this.rotl(this.x[12] + this.x[8], 18);
            final int[] x5 = this.x;
            final int n5 = 9;
            x5[n5] ^= this.rotl(this.x[5] + this.x[1], 7);
            final int[] x6 = this.x;
            final int n6 = 13;
            x6[n6] ^= this.rotl(this.x[9] + this.x[5], 9);
            final int[] x7 = this.x;
            final int n7 = 1;
            x7[n7] ^= this.rotl(this.x[13] + this.x[9], 13);
            final int[] x8 = this.x;
            final int n8 = 5;
            x8[n8] ^= this.rotl(this.x[1] + this.x[13], 18);
            final int[] x9 = this.x;
            final int n9 = 14;
            x9[n9] ^= this.rotl(this.x[10] + this.x[6], 7);
            final int[] x10 = this.x;
            final int n10 = 2;
            x10[n10] ^= this.rotl(this.x[14] + this.x[10], 9);
            final int[] x11 = this.x;
            final int n11 = 6;
            x11[n11] ^= this.rotl(this.x[2] + this.x[14], 13);
            final int[] x12 = this.x;
            final int n12 = 10;
            x12[n12] ^= this.rotl(this.x[6] + this.x[2], 18);
            final int[] x13 = this.x;
            final int n13 = 3;
            x13[n13] ^= this.rotl(this.x[15] + this.x[11], 7);
            final int[] x14 = this.x;
            final int n14 = 7;
            x14[n14] ^= this.rotl(this.x[3] + this.x[15], 9);
            final int[] x15 = this.x;
            final int n15 = 11;
            x15[n15] ^= this.rotl(this.x[7] + this.x[3], 13);
            final int[] x16 = this.x;
            final int n16 = 15;
            x16[n16] ^= this.rotl(this.x[11] + this.x[7], 18);
            final int[] x17 = this.x;
            final int n17 = 1;
            x17[n17] ^= this.rotl(this.x[0] + this.x[3], 7);
            final int[] x18 = this.x;
            final int n18 = 2;
            x18[n18] ^= this.rotl(this.x[1] + this.x[0], 9);
            final int[] x19 = this.x;
            final int n19 = 3;
            x19[n19] ^= this.rotl(this.x[2] + this.x[1], 13);
            final int[] x20 = this.x;
            final int n20 = 0;
            x20[n20] ^= this.rotl(this.x[3] + this.x[2], 18);
            final int[] x21 = this.x;
            final int n21 = 6;
            x21[n21] ^= this.rotl(this.x[5] + this.x[4], 7);
            final int[] x22 = this.x;
            final int n22 = 7;
            x22[n22] ^= this.rotl(this.x[6] + this.x[5], 9);
            final int[] x23 = this.x;
            final int n23 = 4;
            x23[n23] ^= this.rotl(this.x[7] + this.x[6], 13);
            final int[] x24 = this.x;
            final int n24 = 5;
            x24[n24] ^= this.rotl(this.x[4] + this.x[7], 18);
            final int[] x25 = this.x;
            final int n25 = 11;
            x25[n25] ^= this.rotl(this.x[10] + this.x[9], 7);
            final int[] x26 = this.x;
            final int n26 = 8;
            x26[n26] ^= this.rotl(this.x[11] + this.x[10], 9);
            final int[] x27 = this.x;
            final int n27 = 9;
            x27[n27] ^= this.rotl(this.x[8] + this.x[11], 13);
            final int[] x28 = this.x;
            final int n28 = 10;
            x28[n28] ^= this.rotl(this.x[9] + this.x[8], 18);
            final int[] x29 = this.x;
            final int n29 = 12;
            x29[n29] ^= this.rotl(this.x[15] + this.x[14], 7);
            final int[] x30 = this.x;
            final int n30 = 13;
            x30[n30] ^= this.rotl(this.x[12] + this.x[15], 9);
            final int[] x31 = this.x;
            final int n31 = 14;
            x31[n31] ^= this.rotl(this.x[13] + this.x[12], 13);
            final int[] x32 = this.x;
            final int n32 = 15;
            x32[n32] ^= this.rotl(this.x[14] + this.x[13], 18);
        }
        int n33 = 0;
        for (int j = 0; j < 16; ++j) {
            this.intToByteLittle(this.x[j] + array[j], array2, n33);
            n33 += 4;
        }
        for (int k = 16; k < this.x.length; ++k) {
            this.intToByteLittle(this.x[k], array2, n33);
            n33 += 4;
        }
    }
    
    private byte[] intToByteLittle(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 3] = (byte)(n >>> 24);
        return array;
    }
    
    private int rotl(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    private int byteToIntLittle(final byte[] array, final int n) {
        return (array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16 | array[n + 3] << 24;
    }
    
    private void resetCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }
    
    private boolean limitExceeded() {
        ++this.cW0;
        if (this.cW0 == 0) {
            ++this.cW1;
            if (this.cW1 == 0) {
                ++this.cW2;
                return (this.cW2 & 0x20) != 0x0;
            }
        }
        return false;
    }
    
    private boolean limitExceeded(final int n) {
        if (this.cW0 >= 0) {
            this.cW0 += n;
        }
        else {
            this.cW0 += n;
            if (this.cW0 >= 0) {
                ++this.cW1;
                if (this.cW1 == 0) {
                    ++this.cW2;
                    return (this.cW2 & 0x20) != 0x0;
                }
            }
        }
        return false;
    }
    
    static {
        sigma = Strings.toByteArray("expand 32-byte k");
        tau = Strings.toByteArray("expand 16-byte k");
    }
}
