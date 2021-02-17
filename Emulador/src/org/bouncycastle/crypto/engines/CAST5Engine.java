// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class CAST5Engine implements BlockCipher
{
    protected static final int M32 = -1;
    protected static final int[] S1;
    protected static final int[] S2;
    protected static final int[] S3;
    protected static final int[] S4;
    protected static final int[] S5;
    protected static final int[] S6;
    protected static final int[] S7;
    protected static final int[] S8;
    protected static final int MAX_ROUNDS = 16;
    protected static final int RED_ROUNDS = 12;
    protected static final int BLOCK_SIZE = 8;
    protected int[] _Kr;
    protected int[] _Km;
    private boolean _encrypting;
    private byte[] _workingKey;
    private int _rounds;
    
    public CAST5Engine() {
        this._Kr = new int[17];
        this._Km = new int[17];
        this._encrypting = false;
        this._workingKey = null;
        this._rounds = 16;
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this._encrypting = encrypting;
            this.setKey(this._workingKey = ((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("Invalid parameter passed to " + this.getAlgorithmName() + " init - " + cipherParameters.getClass().getName());
    }
    
    @Override
    public String getAlgorithmName() {
        return "CAST5";
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this._workingKey == null) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        final int blockSize = this.getBlockSize();
        if (n + blockSize > array.length) {
            throw new DataLengthException("Input buffer too short");
        }
        if (n2 + blockSize > array2.length) {
            throw new DataLengthException("Output buffer too short");
        }
        if (this._encrypting) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    protected void setKey(final byte[] array) {
        if (array.length < 11) {
            this._rounds = 12;
        }
        final int[] array2 = new int[16];
        final int[] array3 = new int[16];
        for (int i = 0; i < array.length; ++i) {
            array3[i] = (array[i] & 0xFF);
        }
        final int intsTo32bits = this.IntsTo32bits(array3, 0);
        final int intsTo32bits2 = this.IntsTo32bits(array3, 4);
        final int intsTo32bits3 = this.IntsTo32bits(array3, 8);
        final int intsTo32bits4 = this.IntsTo32bits(array3, 12);
        this.Bits32ToInts(intsTo32bits ^ CAST5Engine.S5[array3[13]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[14]] ^ CAST5Engine.S7[array3[8]], array2, 0);
        this.Bits32ToInts(intsTo32bits3 ^ CAST5Engine.S5[array2[0]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[3]] ^ CAST5Engine.S8[array3[10]], array2, 4);
        this.Bits32ToInts(intsTo32bits4 ^ CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S5[array3[9]], array2, 8);
        this.Bits32ToInts(intsTo32bits2 ^ CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[11]] ^ CAST5Engine.S8[array2[8]] ^ CAST5Engine.S6[array3[11]], array2, 12);
        this._Km[1] = (CAST5Engine.S5[array2[8]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[7]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S5[array2[2]]);
        this._Km[2] = (CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[11]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S6[array2[6]]);
        this._Km[3] = (CAST5Engine.S5[array2[12]] ^ CAST5Engine.S6[array2[13]] ^ CAST5Engine.S7[array2[3]] ^ CAST5Engine.S8[array2[2]] ^ CAST5Engine.S7[array2[9]]);
        this._Km[4] = (CAST5Engine.S5[array2[14]] ^ CAST5Engine.S6[array2[15]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[0]] ^ CAST5Engine.S8[array2[12]]);
        final int intsTo32bits5 = this.IntsTo32bits(array2, 0);
        final int intsTo32bits6 = this.IntsTo32bits(array2, 4);
        final int intsTo32bits7 = this.IntsTo32bits(array2, 8);
        final int intsTo32bits8 = this.IntsTo32bits(array2, 12);
        this.Bits32ToInts(intsTo32bits7 ^ CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[7]] ^ CAST5Engine.S7[array2[4]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S7[array2[0]], array3, 0);
        this.Bits32ToInts(intsTo32bits5 ^ CAST5Engine.S5[array3[0]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[3]] ^ CAST5Engine.S8[array2[2]], array3, 4);
        this.Bits32ToInts(intsTo32bits6 ^ CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S5[array2[1]], array3, 8);
        this.Bits32ToInts(intsTo32bits8 ^ CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[11]] ^ CAST5Engine.S8[array3[8]] ^ CAST5Engine.S6[array2[3]], array3, 12);
        this._Km[5] = (CAST5Engine.S5[array3[3]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[13]] ^ CAST5Engine.S5[array3[8]]);
        this._Km[6] = (CAST5Engine.S5[array3[1]] ^ CAST5Engine.S6[array3[0]] ^ CAST5Engine.S7[array3[14]] ^ CAST5Engine.S8[array3[15]] ^ CAST5Engine.S6[array3[13]]);
        this._Km[7] = (CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[8]] ^ CAST5Engine.S8[array3[9]] ^ CAST5Engine.S7[array3[3]]);
        this._Km[8] = (CAST5Engine.S5[array3[5]] ^ CAST5Engine.S6[array3[4]] ^ CAST5Engine.S7[array3[10]] ^ CAST5Engine.S8[array3[11]] ^ CAST5Engine.S8[array3[7]]);
        final int intsTo32bits9 = this.IntsTo32bits(array3, 0);
        final int intsTo32bits10 = this.IntsTo32bits(array3, 4);
        final int intsTo32bits11 = this.IntsTo32bits(array3, 8);
        final int intsTo32bits12 = this.IntsTo32bits(array3, 12);
        this.Bits32ToInts(intsTo32bits9 ^ CAST5Engine.S5[array3[13]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[14]] ^ CAST5Engine.S7[array3[8]], array2, 0);
        this.Bits32ToInts(intsTo32bits11 ^ CAST5Engine.S5[array2[0]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[3]] ^ CAST5Engine.S8[array3[10]], array2, 4);
        this.Bits32ToInts(intsTo32bits12 ^ CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S5[array3[9]], array2, 8);
        this.Bits32ToInts(intsTo32bits10 ^ CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[11]] ^ CAST5Engine.S8[array2[8]] ^ CAST5Engine.S6[array3[11]], array2, 12);
        this._Km[9] = (CAST5Engine.S5[array2[3]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[12]] ^ CAST5Engine.S8[array2[13]] ^ CAST5Engine.S5[array2[9]]);
        this._Km[10] = (CAST5Engine.S5[array2[1]] ^ CAST5Engine.S6[array2[0]] ^ CAST5Engine.S7[array2[14]] ^ CAST5Engine.S8[array2[15]] ^ CAST5Engine.S6[array2[12]]);
        this._Km[11] = (CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[8]] ^ CAST5Engine.S8[array2[9]] ^ CAST5Engine.S7[array2[2]]);
        this._Km[12] = (CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[4]] ^ CAST5Engine.S7[array2[10]] ^ CAST5Engine.S8[array2[11]] ^ CAST5Engine.S8[array2[6]]);
        final int intsTo32bits13 = this.IntsTo32bits(array2, 0);
        final int intsTo32bits14 = this.IntsTo32bits(array2, 4);
        final int intsTo32bits15 = this.IntsTo32bits(array2, 8);
        final int intsTo32bits16 = this.IntsTo32bits(array2, 12);
        this.Bits32ToInts(intsTo32bits15 ^ CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[7]] ^ CAST5Engine.S7[array2[4]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S7[array2[0]], array3, 0);
        this.Bits32ToInts(intsTo32bits13 ^ CAST5Engine.S5[array3[0]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[3]] ^ CAST5Engine.S8[array2[2]], array3, 4);
        this.Bits32ToInts(intsTo32bits14 ^ CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S5[array2[1]], array3, 8);
        this.Bits32ToInts(intsTo32bits16 ^ CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[11]] ^ CAST5Engine.S8[array3[8]] ^ CAST5Engine.S6[array2[3]], array3, 12);
        this._Km[13] = (CAST5Engine.S5[array3[8]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[7]] ^ CAST5Engine.S8[array3[6]] ^ CAST5Engine.S5[array3[3]]);
        this._Km[14] = (CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[11]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S6[array3[7]]);
        this._Km[15] = (CAST5Engine.S5[array3[12]] ^ CAST5Engine.S6[array3[13]] ^ CAST5Engine.S7[array3[3]] ^ CAST5Engine.S8[array3[2]] ^ CAST5Engine.S7[array3[8]]);
        this._Km[16] = (CAST5Engine.S5[array3[14]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[0]] ^ CAST5Engine.S8[array3[13]]);
        final int intsTo32bits17 = this.IntsTo32bits(array3, 0);
        final int intsTo32bits18 = this.IntsTo32bits(array3, 4);
        final int intsTo32bits19 = this.IntsTo32bits(array3, 8);
        final int intsTo32bits20 = this.IntsTo32bits(array3, 12);
        this.Bits32ToInts(intsTo32bits17 ^ CAST5Engine.S5[array3[13]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[14]] ^ CAST5Engine.S7[array3[8]], array2, 0);
        this.Bits32ToInts(intsTo32bits19 ^ CAST5Engine.S5[array2[0]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[3]] ^ CAST5Engine.S8[array3[10]], array2, 4);
        this.Bits32ToInts(intsTo32bits20 ^ CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S5[array3[9]], array2, 8);
        this.Bits32ToInts(intsTo32bits18 ^ CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[11]] ^ CAST5Engine.S8[array2[8]] ^ CAST5Engine.S6[array3[11]], array2, 12);
        this._Kr[1] = ((CAST5Engine.S5[array2[8]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[7]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S5[array2[2]]) & 0x1F);
        this._Kr[2] = ((CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[11]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S6[array2[6]]) & 0x1F);
        this._Kr[3] = ((CAST5Engine.S5[array2[12]] ^ CAST5Engine.S6[array2[13]] ^ CAST5Engine.S7[array2[3]] ^ CAST5Engine.S8[array2[2]] ^ CAST5Engine.S7[array2[9]]) & 0x1F);
        this._Kr[4] = ((CAST5Engine.S5[array2[14]] ^ CAST5Engine.S6[array2[15]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[0]] ^ CAST5Engine.S8[array2[12]]) & 0x1F);
        final int intsTo32bits21 = this.IntsTo32bits(array2, 0);
        final int intsTo32bits22 = this.IntsTo32bits(array2, 4);
        final int intsTo32bits23 = this.IntsTo32bits(array2, 8);
        final int intsTo32bits24 = this.IntsTo32bits(array2, 12);
        this.Bits32ToInts(intsTo32bits23 ^ CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[7]] ^ CAST5Engine.S7[array2[4]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S7[array2[0]], array3, 0);
        this.Bits32ToInts(intsTo32bits21 ^ CAST5Engine.S5[array3[0]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[3]] ^ CAST5Engine.S8[array2[2]], array3, 4);
        this.Bits32ToInts(intsTo32bits22 ^ CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S5[array2[1]], array3, 8);
        this.Bits32ToInts(intsTo32bits24 ^ CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[11]] ^ CAST5Engine.S8[array3[8]] ^ CAST5Engine.S6[array2[3]], array3, 12);
        this._Kr[5] = ((CAST5Engine.S5[array3[3]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[13]] ^ CAST5Engine.S5[array3[8]]) & 0x1F);
        this._Kr[6] = ((CAST5Engine.S5[array3[1]] ^ CAST5Engine.S6[array3[0]] ^ CAST5Engine.S7[array3[14]] ^ CAST5Engine.S8[array3[15]] ^ CAST5Engine.S6[array3[13]]) & 0x1F);
        this._Kr[7] = ((CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[8]] ^ CAST5Engine.S8[array3[9]] ^ CAST5Engine.S7[array3[3]]) & 0x1F);
        this._Kr[8] = ((CAST5Engine.S5[array3[5]] ^ CAST5Engine.S6[array3[4]] ^ CAST5Engine.S7[array3[10]] ^ CAST5Engine.S8[array3[11]] ^ CAST5Engine.S8[array3[7]]) & 0x1F);
        final int intsTo32bits25 = this.IntsTo32bits(array3, 0);
        final int intsTo32bits26 = this.IntsTo32bits(array3, 4);
        final int intsTo32bits27 = this.IntsTo32bits(array3, 8);
        final int intsTo32bits28 = this.IntsTo32bits(array3, 12);
        this.Bits32ToInts(intsTo32bits25 ^ CAST5Engine.S5[array3[13]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[12]] ^ CAST5Engine.S8[array3[14]] ^ CAST5Engine.S7[array3[8]], array2, 0);
        this.Bits32ToInts(intsTo32bits27 ^ CAST5Engine.S5[array2[0]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[1]] ^ CAST5Engine.S8[array2[3]] ^ CAST5Engine.S8[array3[10]], array2, 4);
        this.Bits32ToInts(intsTo32bits28 ^ CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[5]] ^ CAST5Engine.S8[array2[4]] ^ CAST5Engine.S5[array3[9]], array2, 8);
        this.Bits32ToInts(intsTo32bits26 ^ CAST5Engine.S5[array2[10]] ^ CAST5Engine.S6[array2[9]] ^ CAST5Engine.S7[array2[11]] ^ CAST5Engine.S8[array2[8]] ^ CAST5Engine.S6[array3[11]], array2, 12);
        this._Kr[9] = ((CAST5Engine.S5[array2[3]] ^ CAST5Engine.S6[array2[2]] ^ CAST5Engine.S7[array2[12]] ^ CAST5Engine.S8[array2[13]] ^ CAST5Engine.S5[array2[9]]) & 0x1F);
        this._Kr[10] = ((CAST5Engine.S5[array2[1]] ^ CAST5Engine.S6[array2[0]] ^ CAST5Engine.S7[array2[14]] ^ CAST5Engine.S8[array2[15]] ^ CAST5Engine.S6[array2[12]]) & 0x1F);
        this._Kr[11] = ((CAST5Engine.S5[array2[7]] ^ CAST5Engine.S6[array2[6]] ^ CAST5Engine.S7[array2[8]] ^ CAST5Engine.S8[array2[9]] ^ CAST5Engine.S7[array2[2]]) & 0x1F);
        this._Kr[12] = ((CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[4]] ^ CAST5Engine.S7[array2[10]] ^ CAST5Engine.S8[array2[11]] ^ CAST5Engine.S8[array2[6]]) & 0x1F);
        final int intsTo32bits29 = this.IntsTo32bits(array2, 0);
        final int intsTo32bits30 = this.IntsTo32bits(array2, 4);
        final int intsTo32bits31 = this.IntsTo32bits(array2, 8);
        final int intsTo32bits32 = this.IntsTo32bits(array2, 12);
        this.Bits32ToInts(intsTo32bits31 ^ CAST5Engine.S5[array2[5]] ^ CAST5Engine.S6[array2[7]] ^ CAST5Engine.S7[array2[4]] ^ CAST5Engine.S8[array2[6]] ^ CAST5Engine.S7[array2[0]], array3, 0);
        this.Bits32ToInts(intsTo32bits29 ^ CAST5Engine.S5[array3[0]] ^ CAST5Engine.S6[array3[2]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[3]] ^ CAST5Engine.S8[array2[2]], array3, 4);
        this.Bits32ToInts(intsTo32bits30 ^ CAST5Engine.S5[array3[7]] ^ CAST5Engine.S6[array3[6]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S5[array2[1]], array3, 8);
        this.Bits32ToInts(intsTo32bits32 ^ CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[11]] ^ CAST5Engine.S8[array3[8]] ^ CAST5Engine.S6[array2[3]], array3, 12);
        this._Kr[13] = ((CAST5Engine.S5[array3[8]] ^ CAST5Engine.S6[array3[9]] ^ CAST5Engine.S7[array3[7]] ^ CAST5Engine.S8[array3[6]] ^ CAST5Engine.S5[array3[3]]) & 0x1F);
        this._Kr[14] = ((CAST5Engine.S5[array3[10]] ^ CAST5Engine.S6[array3[11]] ^ CAST5Engine.S7[array3[5]] ^ CAST5Engine.S8[array3[4]] ^ CAST5Engine.S6[array3[7]]) & 0x1F);
        this._Kr[15] = ((CAST5Engine.S5[array3[12]] ^ CAST5Engine.S6[array3[13]] ^ CAST5Engine.S7[array3[3]] ^ CAST5Engine.S8[array3[2]] ^ CAST5Engine.S7[array3[8]]) & 0x1F);
        this._Kr[16] = ((CAST5Engine.S5[array3[14]] ^ CAST5Engine.S6[array3[15]] ^ CAST5Engine.S7[array3[1]] ^ CAST5Engine.S8[array3[0]] ^ CAST5Engine.S8[array3[13]]) & 0x1F);
    }
    
    protected int encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] array3 = new int[2];
        this.CAST_Encipher(this.BytesTo32bits(array, n), this.BytesTo32bits(array, n + 4), array3);
        this.Bits32ToBytes(array3[0], array2, n2);
        this.Bits32ToBytes(array3[1], array2, n2 + 4);
        return 8;
    }
    
    protected int decryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] array3 = new int[2];
        this.CAST_Decipher(this.BytesTo32bits(array, n), this.BytesTo32bits(array, n + 4), array3);
        this.Bits32ToBytes(array3[0], array2, n2);
        this.Bits32ToBytes(array3[1], array2, n2 + 4);
        return 8;
    }
    
    protected final int F1(final int n, final int n2, final int n3) {
        final int n4 = n2 + n;
        final int n5 = n4 << n3 | n4 >>> 32 - n3;
        return (CAST5Engine.S1[n5 >>> 24 & 0xFF] ^ CAST5Engine.S2[n5 >>> 16 & 0xFF]) - CAST5Engine.S3[n5 >>> 8 & 0xFF] + CAST5Engine.S4[n5 & 0xFF];
    }
    
    protected final int F2(final int n, final int n2, final int n3) {
        final int n4 = n2 ^ n;
        final int n5 = n4 << n3 | n4 >>> 32 - n3;
        return CAST5Engine.S1[n5 >>> 24 & 0xFF] - CAST5Engine.S2[n5 >>> 16 & 0xFF] + CAST5Engine.S3[n5 >>> 8 & 0xFF] ^ CAST5Engine.S4[n5 & 0xFF];
    }
    
    protected final int F3(final int n, final int n2, final int n3) {
        final int n4 = n2 - n;
        final int n5 = n4 << n3 | n4 >>> 32 - n3;
        return (CAST5Engine.S1[n5 >>> 24 & 0xFF] + CAST5Engine.S2[n5 >>> 16 & 0xFF] ^ CAST5Engine.S3[n5 >>> 8 & 0xFF]) - CAST5Engine.S4[n5 & 0xFF];
    }
    
    protected final void CAST_Encipher(final int n, final int n2, final int[] array) {
        int n3 = n;
        int n4 = n2;
        for (int i = 1; i <= this._rounds; ++i) {
            final int n5 = n3;
            final int n6 = n3 = n4;
            switch (i) {
                case 1:
                case 4:
                case 7:
                case 10:
                case 13:
                case 16: {
                    n4 = (n5 ^ this.F1(n6, this._Km[i], this._Kr[i]));
                    break;
                }
                case 2:
                case 5:
                case 8:
                case 11:
                case 14: {
                    n4 = (n5 ^ this.F2(n6, this._Km[i], this._Kr[i]));
                    break;
                }
                case 3:
                case 6:
                case 9:
                case 12:
                case 15: {
                    n4 = (n5 ^ this.F3(n6, this._Km[i], this._Kr[i]));
                    break;
                }
            }
        }
        array[0] = n4;
        array[1] = n3;
    }
    
    protected final void CAST_Decipher(final int n, final int n2, final int[] array) {
        int n3 = n;
        int n4 = n2;
        for (int i = this._rounds; i > 0; --i) {
            final int n5 = n3;
            final int n6 = n3 = n4;
            switch (i) {
                case 1:
                case 4:
                case 7:
                case 10:
                case 13:
                case 16: {
                    n4 = (n5 ^ this.F1(n6, this._Km[i], this._Kr[i]));
                    break;
                }
                case 2:
                case 5:
                case 8:
                case 11:
                case 14: {
                    n4 = (n5 ^ this.F2(n6, this._Km[i], this._Kr[i]));
                    break;
                }
                case 3:
                case 6:
                case 9:
                case 12:
                case 15: {
                    n4 = (n5 ^ this.F3(n6, this._Km[i], this._Kr[i]));
                    break;
                }
            }
        }
        array[0] = n4;
        array[1] = n3;
    }
    
    protected final void Bits32ToInts(final int n, final int[] array, final int n2) {
        array[n2 + 3] = (n & 0xFF);
        array[n2 + 2] = (n >>> 8 & 0xFF);
        array[n2 + 1] = (n >>> 16 & 0xFF);
        array[n2] = (n >>> 24 & 0xFF);
    }
    
    protected final int IntsTo32bits(final int[] array, final int n) {
        return (array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    protected final void Bits32ToBytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)n;
        array[n2 + 2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 24);
    }
    
    protected final int BytesTo32bits(final byte[] array, final int n) {
        return (array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    static {
        S1 = new int[] { 821772500, -1616838901, 1810681135, 1059425402, 505495343, -1677701677, 1610868032, -811611831, -1076580569, -2000962123, -503103344, -1731160459, 1852023008, 365126098, -1025022435, 584384398, 677919599, -1065365415, -14452280, 2002735330, 1136869587, -550533546, -2005097446, -1563247315, -1580605226, 879511577, 1639411079, 575934255, 717107937, -1437329813, 576097850, -1563213360, 1725645000, -1484506833, 5111599, 767152862, -1751892052, 1251459544, 1383482551, -1242286169, -1205028113, -682503847, 1878520045, 1510570527, -2105841456, -1863518930, 582008916, -1131521739, 1265446783, 1354458274, -765048560, -1092255443, -1221385584, -382003809, -1265703919, 1275016285, -45759936, -1389258945, -990457810, 1442611557, -709768531, -1582551634, -1563117715, -1046803376, -2011021070, 208555832, -1528512553, 1331405426, 1447828783, -979610855, -1186340012, -1337562626, -1313428598, -955033379, 1669711173, 286233437, 1465092821, 1782121619, -432195616, 710211251, 980974943, 1651941557, 430374111, 2051154026, 704238805, -165996399, -1150146722, -1437564569, 948965521, -961214997, -2067281012, 718756367, -2025188313, -1563323541, 718440111, -1437150575, -678870176, 1113355533, -1816945114, 410092745, 1811985197, 1944238868, -1598112708, 1415722873, 1682284203, 1060277122, 1998114690, 1503841958, 82706478, -1979811610, 1068173648, 845149890, -2127020283, 1768146376, 1993038550, -728140599, -904393265, 940016341, -939893514, -1966926575, 904371731, 1205506512, -200306554, -1478344290, 825647681, 85914773, -1437123836, 1249926541, 1417871568, 3287612, -1083912737, -1168660850, 1975924523, 1353700161, -1480510859, -1856369675, 1800716203, 722146342, -1421030953, 1151126914, -134483355, -1417296397, 458611604, -1428888796, -811287233, 770352098, -1642050302, -927128148, -354462285, -708993384, -485346894, 718646636, -1790760482, -1380039384, -663679127, -1437480689, -1434948618, 575749918, -1437489253, 718488780, 2069512688, -746783827, 453416197, 1106044049, -1262275866, 52586708, -916452660, -835158419, -1083461268, 1785789304, 218356169, -723568162, -535796774, 1194783844, 1523787992, -1287140202, 1975193539, -1739514885, 1341901877, -1249128598, -518059332, -1077543350, -1492456432, -1405528310, 1057244207, 1636348243, -533104082, 1462225785, -1662303857, 481089165, 718503062, 24497053, -962724087, -950311440, -639942440, -334596231, 1195698900, -1323552140, -584791138, 2115785917, -267303687, -769388879, -1770671107, -1548994731, -730060881, 1372086093, 1452307862, -1514465818, 1476592880, -905696015, 18495466, -1916818725, 901398090, 891748256, -1015329527, -1137676583, -1734007194, 1447622437, -10594659, 216884176, 2086908623, 1879786977, -706064143, -2052511630, -1356874329, -735885200, -1484321805, 758861177, 1121993112, 215018983, 642190776, -125730484, 1196255959, 2081185372, -786228903, 941322904, -170724133, -1417443757, 1848581667, -2089706338, -1114513338, -1705622162, -600236020, 550028657, -1775511012, -504981761, -1321096440, 2093648313, 443148163, 46942275, -1560820359, 1117713533, 1115362972, 1523183689, -577827072, 1551984063 };
        S2 = new int[] { 522195092, -284448933, 1776537470, 960447360, -27144326, -289070982, 1435016340, 1929119313, -1381503111, 1310552629, -715496498, -570149190, -1715195665, 1594623892, 417127293, -1579749389, -1598738565, 1508390405, -300568428, -369108727, -599523194, -275495847, -1165767501, -524038661, -774225535, 990456497, -107482687, -1511600261, 21106139, -454561957, 631373633, -511641594, 532942976, 396095098, -746928471, -27774812, -1730245761, 2011709262, 2039648873, 620404603, -518797221, -1396440957, -682609371, -135634593, 1645490516, 223693667, 1567101217, -932789415, 1029951347, -824036160, -724009337, 1550265121, 119497089, 972513919, 907948164, -454338757, 1613718692, -700789348, 465323573, -1635712211, 654439692, -1719371084, -1595678855, -1167264884, 277098644, 624404830, -194023426, -1577108705, 546110314, -1891267468, -639589849, 1321679412, -58175639, 1045293279, -284295032, 895050893, -1975175028, 494945126, 1914543101, -1517910853, -400202957, -2075229678, 311263384, -19710028, -836236575, 669096869, -710491566, -459844419, -975809059, -345608092, 2005142349, -1581864959, -2066012503, -524982508, 569394103, -439330720, 1425027204, 108000370, -1558535853, -623098027, -1251844673, 1750473702, -2083886188, 762237499, -321977893, -1496067910, -1233109668, -1351112951, 867476300, 964413654, 1591880597, 1594774276, -2115145887, 552026980, -1268903048, -568826981, -2011389662, -1184422191, -2142656536, 582474363, 1582640421, 1383256631, 2043843868, -972191412, 1217180674, 463797851, -1531928725, 480777679, -1576259579, -2005803165, -1176621109, 214354409, 200212307, -484358889, -1269553099, -1620891332, -297670871, 1847405948, 1342460550, 510035443, -214695482, 815934613, 833030224, 1620250387, 1945732119, -1591306151, -328967100, 1388869545, -838913114, -1607788735, 2092620194, 562037615, 1356438536, -885045151, -1033119899, 1688467115, -2144065930, 631725691, -454635012, 549916902, -839862656, 394546491, 837744717, 2114462948, 751520235, -2073412690, -1879607160, -295870218, 2063029875, 803036379, -1592380991, 821456707, -1275401132, 360699898, -276465204, -783098280, -617611938, -1892495847, 812317050, 49299192, -1724802347, -1035798001, -1478235216, -963753722, -1193663732, -2138951640, -589368376, -748703375, 143268808, -1094662816, 1638124008, -1129777843, -953159686, 578956953, -2100989772, -656847223, -1961085764, 807278310, 658237817, -1325405530, 1641658566, 11683945, -1207972289, 148645947, 1138423386, -136210536, 1981396783, -1893950556, -595183712, 380097457, -1614572617, -1491898645, -960707010, 441530178, -278386500, 1375954390, 761952171, 891809099, -2111843818, 157052462, -611126533, 1592404427, 341349109, -1856483457, 1417898363, 644327628, -2061934520, -1941197590, -2093457196, 220455161, 1815641738, 182899273, -1299947508, -667585763, -592329145, -1404283158, 1052606899, 588164016, 1681439879, -256527878, -1889623373, -65518014, 167996282, 1336969661, 1688053129, -1555742370, 1543734051, 1046297529, 1138201970, 2121126012, 115334942, 1819067631, 1902159161, 1941945968, -2088274427, 1159982321 };
        S3 = new int[] { -1913667008, 637164959, -342868545, -401553145, 1197506559, 916448331, -1944074684, -1362179440, -1095632449, -285488406, -389080752, 1373570990, -1844541434, -257096376, -516125309, -1838149419, 286293407, 124026297, -1293687596, 1028597854, -1179670496, -86080800, -1603852661, -2106427090, 1430237888, 1218109995, -722495596, 308166588, 570424558, -2107958275, -1839872531, 307733056, 1310360322, -1159692289, 1384269543, -1906895858, 863238079, -1935703672, -1493414168, -914180699, -1463804489, 1470087780, 1728663345, -222478497, 1090516929, 532123132, -1905536319, 1132193179, -1716503105, -1243888053, 1670234342, 1434557849, -1583888356, 1241591150, -980923864, -859607183, -1203518957, 1812415473, -2096527044, 267246943, 796911696, -675250306, 38830015, 1526438404, -1488465200, 374413614, -1351565506, 1489179520, 1603809326, 1920779204, 168801282, 260042626, -1936261715, 1563175598, -1897293239, 1356499128, -2077756256, 514611088, 2037363785, -2108498923, -272794213, -1502455427, -1381482280, 1173701892, -94538749, -398540027, 1334932762, -1839830590, 602925377, -1459359442, 1613172210, 41346230, -1795332748, -1837529678, -2106139701, 41386358, -122711667, 1313404830, -1889440289, -492993522, -2077262461, 873260488, -1766082942, -1816874680, -282051413, -1739608280, 2006953883, -1831053811, 575479328, -2076726648, 2099895446, 660001756, -1953465106, -1256205760, -406815517, -446253919, -1008115362, 1022894237, 1620365795, -845372607, 1551255054, 15374395, -724141951, -45656276, -143856167, -1113054564, 310226346, 1133119310, 530038928, 136043402, -1818198338, -1187460587, -1750057729, 1036173560, -1927630100, 1681395281, 1758231547, -653318264, 306774401, 1575354324, -578881430, 1990386196, -1180433560, -1839360625, 1262092282, -1170624791, -1526738165, -84438213, 1833535011, 423410938, 660763973, -2107837318, 1639812000, -786545967, -827521804, 310289298, 272797111, -2106414734, -1838103384, 310240523, 677093832, 1013118031, 901835429, -402271695, 1116285435, -1258496126, 1337354835, 243122523, 520626091, 277223598, -50526099, -100718455, 1766575121, 594173102, 316590669, 742362309, -758108674, -118531946, -456174886, -1793762457, 1229605004, -1179211764, 1552908988, -1982633147, 979407927, -335492695, 1148277331, 176638793, -680281024, 2083809052, 40992502, 1340822838, -1563414529, -759209788, -734067776, 1354035053, 122129617, 7215240, -1562034347, -1176054596, -1576763370, -1755891661, -685736601, -569405635, 1928887091, -1412673741, 1988674909, 2063640240, -1803878399, 1459647954, -105150216, -1992162914, 1113892351, -2057108768, 1927010603, -292086935, 1856122846, 1594404395, -1350934163, -439777433, -819991598, 1643104450, -240376463, -863880766, 1730235576, -1310358575, -1210302878, 2131803598, -116761544, 267404349, 1617849798, 1616132681, 1462223176, 736725533, -1967909064, 551665188, -1349068273, 1749386277, -1719452699, 1611482493, 674206544, -2093698206, -652406496, 728599968, 1680547377, -1674552832, 1388111496, 453204106, -138743851, 1094905244, -1540269039, -2093859131, -537967050, -1590442751, -372026596, -298502269 };
        S4 = new int[] { -1649212384, 532081118, -1480688657, -764173672, 1246723035, 1689095255, -2058288061, -100528431, 2116582143, -435177885, 157234593, 2045505824, -49963709, 1687664561, -211542173, 605965023, 672431967, 1336064205, -918355904, 214114848, -36500688, -1062914225, 489488601, 605322005, -296939238, 264917351, 1912574028, 756637694, 436560991, 202637054, 135989450, 85393697, -2142043904, -398565634, -1399130888, 2145855233, -759632289, 115294817, -1147233398, 1922296357, -830144545, -177108991, 1037454084, -1569774021, 2127856640, 1417604070, 1148013728, 1827919605, 642362335, -1365194763, 909348033, 1346338451, -747167647, 297154785, 1917849091, -133254469, -1411362770, -326273058, 1469521537, -514889914, -919383040, 1763717519, 136166297, -3996507, 1295325189, 2134727907, -1496815930, 1566297257, -622039062, -1617793135, -1622793681, 965822077, -1514181234, 289653839, 1133871874, -803123477, 35685304, 1068898316, 418943774, 672553190, 642281022, -1948808592, 1954014401, -1257840516, -215152091, 2030668546, -454378623, 672283427, 1776201016, 359975446, -544793758, 555499703, -1524982023, 1324923, 69110472, 152125443, -1118182190, -472820011, 1340634837, 798073664, 1434183902, 15393959, 216384236, 1303690150, -413745665, -583833172, -333991883, 106373927, -1716533072, 1455997841, 1801814300, 1578393881, 1854262133, -1106788350, -1036888713, -1992297236, 1539295533, -789824731, -1216341321, -1922221276, 549938159, -1016683012, -1674041216, 181285381, -1429646198, -324937785, 68876850, 488006234, 1728155692, -1686799788, 836007927, -1859735503, 919367643, -955544762, -639210936, 1457871481, 40520939, 1380155135, 797931188, 234455205, -2039165469, -304478997, 397000196, 739833055, -1217101923, -1423247436, -272413408, 772369276, 390177364, -441016267, 557662966, 740064294, 1640166671, 1699928825, -759025160, 622006121, -669614174, 68743880, 1742502, 219489963, 1664179233, 1577743084, 1236991741, 410585305, -1928479354, 823226535, 1050371084, -868347689, -708127818, 212779912, -147848735, 1819446015, 1911218849, 530248558, -808726225, -1042381801, -1408778645, -884694568, -1952772266, 20547779, -1312477238, -1262603827, -663214074, 312714466, 1870521650, 1493008054, -803280640, 615382978, -191295547, -1760449851, 1932181, -2098862126, 278426614, 6369430, -1020422879, -1381948929, 697336853, 2143000447, -1348553765, 701099306, 1558357093, -1489964244, -794148888, -1973632879, -727831321, 216290473, -703935098, 23009561, 1996984579, -559924490, 2024298078, -555526433, 569400510, -1955208313, -1278933423, -1197095953, -655444270, -450642313, -1038793431, 795471839, -1343849733, -193936206, -203363493, -691234698, 971261452, 534414648, 428311343, -905940121, -1450097416, 694888862, 1227866773, -1838760277, -1251512727, -1680613926, -545389265, -618303460, 459166190, -162323226, 1794958188, 51825668, -2042355394, -1210295856, 2036672799, -858325693, 1099053433, -1825845770, -1235762355, 1323291266, 2061838604, 1018778475, -2061623042, -1741466242, 334295216, -738217102, 1065731521, 183467730 };
        S5 = new int[] { 2127105028, 745436345, -1693554977, -1506576111, -1200979969, 500390133, 1155374404, 389092991, 150729210, -403369524, -771417344, 1935325696, 716645080, 946045387, -1393155014, 1774124410, -425531521, -255385395, -1001830378, -856309376, 948246080, 363898952, -427091765, 1286266623, 1598556673, 68334250, 630723836, 1104211938, 1312863373, 613332731, -1917182722, 1101634306, 441780740, -1165007413, 1917973735, -1784342747, -1056510761, -1750755318, -986072662, 1299840618, -218892445, 1756332096, -317940138, 297047435, -504669560, -2029394256, -673156778, 1311375015, 1667687725, 47300608, -995324411, -1820854927, 201668394, 1468347890, 576830978, -700276535, -552361344, 1958042578, 1747032512, -735975956, 1408974056, -928125517, 682131401, 1033214337, 1545599232, -29830247, 206503691, 103024618, -1439739983, 1337551222, -1865968379, -1331124364, -279600641, -442719550, -1498010329, -429243805, -547028961, 247794022, -539142724, 702416469, -1860275302, 397379957, 851939612, -1980197784, 218229120, 1380406772, 62274761, 214451378, -1124863830, -2018756887, -449154010, 28563499, 446592073, 1693330814, -841240102, 29968656, -1201094784, 220656637, -1824330265, 77972100, 1667708854, 1358280214, -230201629, -1899350335, 325977563, -17726575, -74941897, -689440812, -939819575, 811859167, -1225422370, -332840486, 652502677, -1219075047, -162205755, -796043081, 1217549313, -1044722817, -436251377, -1240977335, 1538642152, -2015941030, -1419088159, 574252750, -970198067, -1643608583, 1758150215, 141295887, -1575098336, -779392546, -201959561, -100482058, 1082055363, -877406896, 395511885, -1328083270, 179534037, -648938740, -556279210, 1092926436, -1798698154, 257381841, -522066578, 1636087230, 1477059743, -1795732544, -483948402, -1619307167, -1008991616, 90732309, 1684827095, 1150307763, 1723134115, -1057921910, 1769919919, 1240018934, 815675215, 750138730, -2055174797, 1234303040, 1995484674, 138143821, 675421338, 1145607174, 1936608440, -1056364272, -1949737018, 2105974004, 323969391, 779555213, -1290064927, -1433357198, 1017501463, 2098600890, -1666346992, -1354355806, -1612424750, 1171473753, -638395885, -607759225, -203097778, 393037935, 159126506, 1662887367, 1147106178, 391545844, -842634601, 1891500680, -1278357646, 1851642611, 546529401, 1167818917, -1100946725, -1446891263, -341495460, 575554290, 475796850, -160294100, 450035699, -1943715762, 844027695, 1080539133, 86184846, 1554234488, -602941842, 1972511363, 2018339607, 1491841390, 1141460869, 1061690759, -50418053, 2008416118, -1943862593, -1426819754, 1598468138, 722020353, 1027143159, 212344630, 1387219594, 1725294528, -549779340, -1794813680, 458938280, -165751379, 1828119673, 544571780, -791741851, -1997029800, 1241802790, 267843827, -1600356496, 1397140384, 1558801448, -512299613, 1806446719, 929573330, -2060054615, 400817706, 616011623, -173446368, -691198571, 1761550015, 1968522284, -241236290, -102734438, -289847011, 872482584, -1154430280, -400359915, -2007561853, 1963876937, -631079339, 1584857000, -1319942842, 1833426440, -269883436 };
        S6 = new int[] { -151351395, 749497569, 1285769319, -499941508, -1780807449, 23610292, -319988548, 844452780, -1080096416, -543038739, -2081400931, 1676510905, 448177848, -564216263, -208668878, -1987464904, 871450977, -1072089155, -184105254, -463315330, -1559696743, 1310974780, 2043402188, 1218528103, -1558931943, -20362283, -1592518838, -358606746, -1601905875, 162023535, -1467457206, 687910808, 23484817, -510056349, -923595680, 779677500, -791340750, -821040108, -137754670, -794288014, -46065282, -1828346192, -395582502, 1958663117, 925738300, 1283408968, -625617856, 1840910019, 137959847, -1615139111, 1239142320, 1315376211, 1547541505, 1690155329, 739140458, -1166157363, -361794680, -418658462, 905091803, 1548541325, -254505588, -1199483934, 144808038, 451078856, 676114313, -1433239005, -1825259949, 993665471, 373509091, -1695926010, -269958290, -124727847, -2145227346, -1019173725, -545350647, -1500207097, 1534877388, 572371878, -1704353745, 1753320020, -827184785, 1405125690, -24562091, 633333386, -1268610372, -819843393, 632057672, -1448504441, 1404951397, -412091417, -379060872, 195638627, -1909183551, -392094743, 1233155085, -938967556, -1914388583, -1592720992, 2144565621, -631626048, -400582321, -1792488055, -46948371, -1200081729, 1594115437, 572884632, -909850565, 767645374, 1331858858, 1475698373, -501085506, -762220865, 1321687957, 619889600, 1121017241, -854753376, 2070816767, -1461941520, 1933951238, -199351505, 890643334, -420837082, 859025556, 360630002, 925594799, 1764062180, -374745016, -216661367, 979562269, -1484266952, -207227274, 1949714515, 546639971, 1165388173, -1225075705, 1495988560, 922170659, 1291546247, 2107952832, 1813327274, -888957272, -988938659, -53016661, 153207855, -1981812549, 1608695416, 1150242611, 1967526857, 721801357, 1220138373, -603679679, -938897509, 2112743302, -1013304461, 1111556101, 1778980689, 250857638, -1996459306, 673216130, -1448478786, -1087215715, -732210315, -1286341376, -877599912, -2096160246, 529510932, -747450616, -868464109, -1930022554, 102533054, -2000056440, 1617093527, 1204784762, -1228385661, 1019391227, 1069574518, 1317995090, 1691889997, -633835293, 510022745, -1056372496, 1362108837, 1817929911, -2110813536, 805817662, 1953603311, -595122559, 120799444, 2118332377, 207536705, -2012665748, -174925679, 145305846, -1786842363, -1208221763, -1033442961, 1877257368, -1317802816, -1134513110, -1791715110, -73290222, 759945014, 254147243, -1527513877, -493448925, 629083197, -1823953079, 907280572, -394170550, 940896768, -1543946173, -1669704510, -1133490345, -633214983, -1034235078, 1425318020, -1317055227, 1496677566, -306375224, 2140652971, -1168455755, -1225335121, 977771578, 1392695845, 1698528874, 1411812681, 1369733098, 1343739227, -674079352, 1142123638, 67414216, -1192910559, -1206218102, 1626167401, -1748673642, -353593061, 697522451, 33404913, 143560186, -1699285259, 994885535, 1247667115, -435872459, -1595811755, -747942671, -180032021, -1326893788, -1095004227, -1562942769, 1237921620, 951448369, 1898488916, 1211705605, -1503978056, -2061723715, -696922321 };
        S7 = new int[] { -2048901095, 858518887, 1714274303, -809085293, 713916271, -1415853806, -564131679, 539548191, 36158695, 1298409750, 419087104, 1358007170, 749914897, -1305286820, 1261868530, -1299773474, -1604338442, -851344919, -514842356, -498142787, -1318534271, -35330167, 1551479000, 512490819, 1296650241, 951993153, -1858277859, -1834509249, 144139966, -1158763020, 310820559, -1226126567, 643875328, 1969602020, 1680088954, -2109154135, -1011634842, 672358534, 198762408, 896343282, 276269502, -1280120370, 84060815, 197145886, 376173866, -351076478, -481793775, -749898474, 1316698879, 1598252827, -1661542345, 1233235075, 859989710, -1936506441, -791128896, -885363576, 1203513385, 1193654839, -1502948821, 2060853022, 207403770, 1144516871, -1226335902, 1121114134, 177607304, -509230994, 326409831, 1929119770, -1311688201, -111659195, -820388008, -1094453418, -1066485200, 119610148, 1170376745, -916573825, -1131494127, 951863017, -957941228, -1159178166, -1387348922, 1183797387, 2015970143, -249292741, -2111980897, -1342828556, -366195091, 384012900, -1839969653, 10178499, -1415148307, -1698074760, 111523738, -1299878290, 451689641, -1098676600, 235406569, 1441906262, -404408773, -1281232291, -136397947, 1644036924, 376726067, 1006849064, -630387596, 2041234796, 1021632941, 1374734338, -1728515238, 371631263, -287823063, 490221539, 206551450, -1154328712, 1053219195, 1853335209, -882537636, -732811065, 735133835, 1623211703, -1190752904, -1556654860, -198129539, -928574718, -1184003022, -338368578, -1098146515, 2038037254, -417180920, -1955213449, 300912036, -528234408, -1922336657, 1516443558, -94570592, 1574567987, -225525840, -172375280, -1595227520, 146372218, -1546005840, 2043888151, 35287437, -1698286742, 655490400, 1132482787, 110692520, 1031794116, -2106774545, 1324057718, 1217253157, 919197030, 686247489, -1033827638, 1028237775, -1159480865, -1235251738, -1834045596, 986174950, -1633155831, -232062595, -1541980304, -585230653, 367056889, 1353824391, 731860949, 1650113154, 1778481506, 784341916, 357075625, -686364864, 1074092588, -1814914526, -483541094, 92751289, 877911070, -694605458, 1231880047, 480201094, -538776313, -1200471343, 434011822, 87971354, 363687820, 1717726236, 1901380172, -368563414, -1813305031, 400339184, 1490350766, -1633512197, 1389319756, -1736180122, 784598401, 1983468483, 30828846, -744439544, -1578691058, -453845082, 1765724805, 1955612312, 1277890269, 1333098070, 1564029816, -1590549681, 1026694237, -1007296108, 1260819201, -945880529, 1016692350, 1582273796, 1073413053, 1995943182, 694588404, 1025494639, -971094594, -743068876, -148112969, 453260480, 1316140391, 1435673405, -1256025343, -808277889, 1622062951, 403978347, 817677117, 950059133, -48888078, -1016901221, 1486738320, 1417279718, 481875527, -1745002071, -361276940, 760697757, 1452955855, -397515859, 1177426808, 1702951038, -209618668, -1847962124, 1084371187, -778531019, -1226630958, 1073369276, 1027665953, -1010778706, 1230553676, 1368340146, -2068720784, 267243139, -2020746534, -224233017, -1797252120, -1871614133, -1790211421 };
        S8 = new int[] { -501862387, -1143078916, -1477715267, 895778965, 2005530807, -423554533, 237245952, 86829237, 296341424, -443207919, -320366326, -1819881100, 709006108, 1994621201, -1322389702, 937287164, -560275791, 168608556, -1105629143, -2069886656, -1155253745, -1261357105, -1269925392, 77524477, 185966941, 1208824168, -1950622118, 1721625922, -940775375, 1066374631, 1927223579, 1971335949, -1811463599, 1551748602, -1413583517, -1438637724, -1291725814, 48746954, 1398218158, 2050065058, 313056748, -39177379, 393167848, 1912293076, 940740642, -829121836, -1203279443, -1772365726, -2097950635, 1727764327, 364383054, 492521376, 1291706479, -1030830920, 1474851438, 1685747964, -1719247548, 1619776915, 1814040067, 970743798, 1561002147, -1369198606, 2123093554, 1880132620, -1143779255, 697884420, -1743981526, -1687292783, -1635852973, 110200136, 1489731079, 997519150, 1378877361, -767096628, 478029773, -1528094373, 1022481122, 431258168, 1112503832, 897933369, -1659379993, 669726182, -911214981, 918222264, 163866573, -1047981903, -518144133, 114105080, 1903216136, 761148244, -723629734, 1690750982, -1128217044, 1037045171, 1888456500, 2010454850, 642736655, 616092351, 365016990, 1185228132, -120068786, 1043824992, 2023083429, -2053368411, -431646840, -1015298209, -620250612, 108438443, 2132974366, 830746235, 606445527, -121703310, -2090861384, 1844756978, -1762283115, -49614596, -1325526196, -498045635, 1335562986, -233442779, -1574734993, -1615543256, 634407289, 885462008, -1000242809, -361075048, 2094100220, 339117932, -246136569, -1092686316, 1458155303, -1605721023, 1022871705, -1829979418, -580451987, 353796843, -1472008481, -38117196, -242189451, 551748367, 618185374, -516331717, -274317384, 1904685140, -1225601221, -1624087486, -887774004, -1340455676, -236683891, -2075517979, -1159208996, 1120655984, -847401462, 1474845562, -717268234, 550456716, -828058584, 2043752612, 881257467, 869518812, 2005220179, 938474677, -989427848, -444550170, 1315485940, -976702594, 226533026, 965733244, 321539988, 1136104718, 804158748, 573969341, -586757470, 937399083, -1004240247, -1393300541, 1461057207, -281773859, -228105873, -1052193820, -1873641122, 1581322155, -1266015131, 786071460, -394575644, -376528764, 1485433313, -271347460, -586689701, -616016236, 953673138, 1467089153, 1930354364, 1533292819, -1802404273, 1346121658, 1685000834, 1965281866, -529033579, -104760689, 2052792609, -779634538, 690371149, -1169093409, -2114683745, -1391369235, -361014939, 436236910, 289419410, 14314871, 1242357089, -1390459389, 1616633776, -1628585116, 585885352, -823668086, -1595459936, 1432659641, 277164553, -940863689, 770115018, -1991158001, -553024981, -1117185428, -1441602318, -2025513969, -520707462, 987383833, 1290892879, 225909803, 1741533526, 890078084, 1496906255, 1111072499, 916028167, 243534141, 1252605537, -2090805125, 531204876, 290011180, -378133083, 102027703, 237315147, 209093447, 1486785922, 220223953, -1536771298, -119928190, 82940208, -1167176000, -1725542044, 518464269, 1353887104, -353474559, -1917672829, -359926370 };
    }
}
