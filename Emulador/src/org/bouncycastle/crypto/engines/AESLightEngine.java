// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class AESLightEngine implements BlockCipher
{
    private static final byte[] S;
    private static final byte[] Si;
    private static final int[] rcon;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private int ROUNDS;
    private int[][] WorkingKey;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private boolean forEncryption;
    private static final int BLOCK_SIZE = 16;
    
    private int shift(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private int FFmulX(final int n) {
        return (n & 0x7F7F7F7F) << 1 ^ ((n & 0x80808080) >>> 7) * 27;
    }
    
    private int mcol(final int n) {
        final int fFmulX = this.FFmulX(n);
        return fFmulX ^ this.shift(n ^ fFmulX, 8) ^ this.shift(n, 16) ^ this.shift(n, 24);
    }
    
    private int inv_mcol(final int n) {
        final int fFmulX = this.FFmulX(n);
        final int fFmulX2 = this.FFmulX(fFmulX);
        final int fFmulX3 = this.FFmulX(fFmulX2);
        final int n2 = n ^ fFmulX3;
        return fFmulX ^ fFmulX2 ^ fFmulX3 ^ this.shift(fFmulX ^ n2, 8) ^ this.shift(fFmulX2 ^ n2, 16) ^ this.shift(n2, 24);
    }
    
    private int subWord(final int n) {
        return (AESLightEngine.S[n & 0xFF] & 0xFF) | (AESLightEngine.S[n >> 8 & 0xFF] & 0xFF) << 8 | (AESLightEngine.S[n >> 16 & 0xFF] & 0xFF) << 16 | AESLightEngine.S[n >> 24 & 0xFF] << 24;
    }
    
    private int[][] generateWorkingKey(final byte[] array, final boolean b) {
        final int n = array.length / 4;
        if ((n != 4 && n != 6 && n != 8) || n * 4 != array.length) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        this.ROUNDS = n + 6;
        final int[][] array2 = new int[this.ROUNDS + 1][4];
        for (int n2 = 0, i = 0; i < array.length; i += 4, ++n2) {
            array2[n2 >> 2][n2 & 0x3] = ((array[i] & 0xFF) | (array[i + 1] & 0xFF) << 8 | (array[i + 2] & 0xFF) << 16 | array[i + 3] << 24);
        }
        for (int n3 = this.ROUNDS + 1 << 2, j = n; j < n3; ++j) {
            int subWord = array2[j - 1 >> 2][j - 1 & 0x3];
            if (j % n == 0) {
                subWord = (this.subWord(this.shift(subWord, 8)) ^ AESLightEngine.rcon[j / n - 1]);
            }
            else if (n > 6 && j % n == 4) {
                subWord = this.subWord(subWord);
            }
            array2[j >> 2][j & 0x3] = (array2[j - n >> 2][j - n & 0x3] ^ subWord);
        }
        if (!b) {
            for (int k = 1; k < this.ROUNDS; ++k) {
                for (int l = 0; l < 4; ++l) {
                    array2[k][l] = this.inv_mcol(array2[k][l]);
                }
            }
        }
        return array2;
    }
    
    public AESLightEngine() {
        this.WorkingKey = null;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), forEncryption);
            this.forEncryption = forEncryption;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }
    
    @Override
    public String getAlgorithmName() {
        return "AES";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 > array2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.unpackBlock(array, n);
            this.encryptBlock(this.WorkingKey);
            this.packBlock(array2, n2);
        }
        else {
            this.unpackBlock(array, n);
            this.decryptBlock(this.WorkingKey);
            this.packBlock(array2, n2);
        }
        return 16;
    }
    
    @Override
    public void reset() {
    }
    
    private void unpackBlock(final byte[] array, final int n) {
        int n2 = n;
        this.C0 = (array[n2++] & 0xFF);
        this.C0 |= (array[n2++] & 0xFF) << 8;
        this.C0 |= (array[n2++] & 0xFF) << 16;
        this.C0 |= array[n2++] << 24;
        this.C1 = (array[n2++] & 0xFF);
        this.C1 |= (array[n2++] & 0xFF) << 8;
        this.C1 |= (array[n2++] & 0xFF) << 16;
        this.C1 |= array[n2++] << 24;
        this.C2 = (array[n2++] & 0xFF);
        this.C2 |= (array[n2++] & 0xFF) << 8;
        this.C2 |= (array[n2++] & 0xFF) << 16;
        this.C2 |= array[n2++] << 24;
        this.C3 = (array[n2++] & 0xFF);
        this.C3 |= (array[n2++] & 0xFF) << 8;
        this.C3 |= (array[n2++] & 0xFF) << 16;
        this.C3 |= array[n2++] << 24;
    }
    
    private void packBlock(final byte[] array, final int n) {
        int n2 = n;
        array[n2++] = (byte)this.C0;
        array[n2++] = (byte)(this.C0 >> 8);
        array[n2++] = (byte)(this.C0 >> 16);
        array[n2++] = (byte)(this.C0 >> 24);
        array[n2++] = (byte)this.C1;
        array[n2++] = (byte)(this.C1 >> 8);
        array[n2++] = (byte)(this.C1 >> 16);
        array[n2++] = (byte)(this.C1 >> 24);
        array[n2++] = (byte)this.C2;
        array[n2++] = (byte)(this.C2 >> 8);
        array[n2++] = (byte)(this.C2 >> 16);
        array[n2++] = (byte)(this.C2 >> 24);
        array[n2++] = (byte)this.C3;
        array[n2++] = (byte)(this.C3 >> 8);
        array[n2++] = (byte)(this.C3 >> 16);
        array[n2++] = (byte)(this.C3 >> 24);
    }
    
    private void encryptBlock(final int[][] array) {
        this.C0 ^= array[0][0];
        this.C1 ^= array[0][1];
        this.C2 ^= array[0][2];
        this.C3 ^= array[0][3];
        int i;
        int n;
        int n2;
        int n3;
        int n4;
        for (i = 1; i < this.ROUNDS - 1; n4 = (this.mcol((AESLightEngine.S[this.C3 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C0 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C1 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C2 >> 24 & 0xFF] << 24) ^ array[i++][3]), this.C0 = (this.mcol((AESLightEngine.S[n & 0xFF] & 0xFF) ^ (AESLightEngine.S[n2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n4 >> 24 & 0xFF] << 24) ^ array[i][0]), this.C1 = (this.mcol((AESLightEngine.S[n2 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n >> 24 & 0xFF] << 24) ^ array[i][1]), this.C2 = (this.mcol((AESLightEngine.S[n3 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n2 >> 24 & 0xFF] << 24) ^ array[i][2]), this.C3 = (this.mcol((AESLightEngine.S[n4 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n3 >> 24 & 0xFF] << 24) ^ array[i++][3])) {
            n = (this.mcol((AESLightEngine.S[this.C0 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C1 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C3 >> 24 & 0xFF] << 24) ^ array[i][0]);
            n2 = (this.mcol((AESLightEngine.S[this.C1 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C0 >> 24 & 0xFF] << 24) ^ array[i][1]);
            n3 = (this.mcol((AESLightEngine.S[this.C2 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C0 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C1 >> 24 & 0xFF] << 24) ^ array[i][2]);
        }
        final int n5 = this.mcol((AESLightEngine.S[this.C0 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C1 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C3 >> 24 & 0xFF] << 24) ^ array[i][0];
        final int n6 = this.mcol((AESLightEngine.S[this.C1 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C0 >> 24 & 0xFF] << 24) ^ array[i][1];
        final int n7 = this.mcol((AESLightEngine.S[this.C2 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C0 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C1 >> 24 & 0xFF] << 24) ^ array[i][2];
        final int n8 = this.mcol((AESLightEngine.S[this.C3 & 0xFF] & 0xFF) ^ (AESLightEngine.S[this.C0 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[this.C1 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[this.C2 >> 24 & 0xFF] << 24) ^ array[i++][3];
        this.C0 = ((AESLightEngine.S[n5 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n7 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n8 >> 24 & 0xFF] << 24 ^ array[i][0]);
        this.C1 = ((AESLightEngine.S[n6 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n8 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n5 >> 24 & 0xFF] << 24 ^ array[i][1]);
        this.C2 = ((AESLightEngine.S[n7 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n8 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n6 >> 24 & 0xFF] << 24 ^ array[i][2]);
        this.C3 = ((AESLightEngine.S[n8 & 0xFF] & 0xFF) ^ (AESLightEngine.S[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.S[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.S[n7 >> 24 & 0xFF] << 24 ^ array[i][3]);
    }
    
    private void decryptBlock(final int[][] array) {
        this.C0 ^= array[this.ROUNDS][0];
        this.C1 ^= array[this.ROUNDS][1];
        this.C2 ^= array[this.ROUNDS][2];
        this.C3 ^= array[this.ROUNDS][3];
        int i;
        int n;
        int n2;
        int n3;
        int n4;
        for (i = this.ROUNDS - 1; i > 1; n4 = (this.inv_mcol((AESLightEngine.Si[this.C3 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C1 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C0 >> 24 & 0xFF] << 24) ^ array[i--][3]), this.C0 = (this.inv_mcol((AESLightEngine.Si[n & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n2 >> 24 & 0xFF] << 24) ^ array[i][0]), this.C1 = (this.inv_mcol((AESLightEngine.Si[n2 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n4 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n3 >> 24 & 0xFF] << 24) ^ array[i][1]), this.C2 = (this.inv_mcol((AESLightEngine.Si[n3 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n4 >> 24 & 0xFF] << 24) ^ array[i][2]), this.C3 = (this.inv_mcol((AESLightEngine.Si[n4 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n >> 24 & 0xFF] << 24) ^ array[i--][3])) {
            n = (this.inv_mcol((AESLightEngine.Si[this.C0 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C1 >> 24 & 0xFF] << 24) ^ array[i][0]);
            n2 = (this.inv_mcol((AESLightEngine.Si[this.C1 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C0 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C2 >> 24 & 0xFF] << 24) ^ array[i][1]);
            n3 = (this.inv_mcol((AESLightEngine.Si[this.C2 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C1 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C0 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C3 >> 24 & 0xFF] << 24) ^ array[i][2]);
        }
        final int n5 = this.inv_mcol((AESLightEngine.Si[this.C0 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C3 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C2 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C1 >> 24 & 0xFF] << 24) ^ array[i][0];
        final int n6 = this.inv_mcol((AESLightEngine.Si[this.C1 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C0 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C3 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C2 >> 24 & 0xFF] << 24) ^ array[i][1];
        final int n7 = this.inv_mcol((AESLightEngine.Si[this.C2 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C1 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C0 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C3 >> 24 & 0xFF] << 24) ^ array[i][2];
        final int n8 = this.inv_mcol((AESLightEngine.Si[this.C3 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[this.C2 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[this.C1 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[this.C0 >> 24 & 0xFF] << 24) ^ array[i][3];
        this.C0 = ((AESLightEngine.Si[n5 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n8 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n7 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n6 >> 24 & 0xFF] << 24 ^ array[0][0]);
        this.C1 = ((AESLightEngine.Si[n6 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n8 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n7 >> 24 & 0xFF] << 24 ^ array[0][1]);
        this.C2 = ((AESLightEngine.Si[n7 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n8 >> 24 & 0xFF] << 24 ^ array[0][2]);
        this.C3 = ((AESLightEngine.Si[n8 & 0xFF] & 0xFF) ^ (AESLightEngine.Si[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESLightEngine.Si[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ AESLightEngine.Si[n5 >> 24 & 0xFF] << 24 ^ array[0][3]);
    }
    
    static {
        S = new byte[] { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22 };
        Si = new byte[] { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125 };
        rcon = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
    }
}
