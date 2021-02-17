// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class SerpentEngine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    static final int ROUNDS = 32;
    static final int PHI = -1640531527;
    private boolean encrypting;
    private int[] wKey;
    private int X0;
    private int X1;
    private int X2;
    private int X3;
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = encrypting;
            this.wKey = this.makeWorkingKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Serpent init - " + cipherParameters.getClass().getName());
    }
    
    @Override
    public String getAlgorithmName() {
        return "Serpent";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public final int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.wKey == null) {
            throw new IllegalStateException("Serpent not initialised");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 > array2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.encrypting) {
            this.encryptBlock(array, n, array2, n2);
        }
        else {
            this.decryptBlock(array, n, array2, n2);
        }
        return 16;
    }
    
    @Override
    public void reset() {
    }
    
    private int[] makeWorkingKey(final byte[] array) throws IllegalArgumentException {
        final int[] array2 = new int[16];
        int n = 0;
        int i;
        for (i = array.length - 4; i > 0; i -= 4) {
            array2[n++] = this.bytesToWord(array, i);
        }
        if (i == 0) {
            array2[n++] = this.bytesToWord(array, 0);
            if (n < 8) {
                array2[n] = 1;
            }
            final int n2 = 132;
            final int[] array3 = new int[n2];
            for (int j = 8; j < 16; ++j) {
                array2[j] = this.rotateLeft(array2[j - 8] ^ array2[j - 5] ^ array2[j - 3] ^ array2[j - 1] ^ 0x9E3779B9 ^ j - 8, 11);
            }
            System.arraycopy(array2, 8, array3, 0, 8);
            for (int k = 8; k < n2; ++k) {
                array3[k] = this.rotateLeft(array3[k - 8] ^ array3[k - 5] ^ array3[k - 3] ^ array3[k - 1] ^ 0x9E3779B9 ^ k, 11);
            }
            this.sb3(array3[0], array3[1], array3[2], array3[3]);
            array3[0] = this.X0;
            array3[1] = this.X1;
            array3[2] = this.X2;
            array3[3] = this.X3;
            this.sb2(array3[4], array3[5], array3[6], array3[7]);
            array3[4] = this.X0;
            array3[5] = this.X1;
            array3[6] = this.X2;
            array3[7] = this.X3;
            this.sb1(array3[8], array3[9], array3[10], array3[11]);
            array3[8] = this.X0;
            array3[9] = this.X1;
            array3[10] = this.X2;
            array3[11] = this.X3;
            this.sb0(array3[12], array3[13], array3[14], array3[15]);
            array3[12] = this.X0;
            array3[13] = this.X1;
            array3[14] = this.X2;
            array3[15] = this.X3;
            this.sb7(array3[16], array3[17], array3[18], array3[19]);
            array3[16] = this.X0;
            array3[17] = this.X1;
            array3[18] = this.X2;
            array3[19] = this.X3;
            this.sb6(array3[20], array3[21], array3[22], array3[23]);
            array3[20] = this.X0;
            array3[21] = this.X1;
            array3[22] = this.X2;
            array3[23] = this.X3;
            this.sb5(array3[24], array3[25], array3[26], array3[27]);
            array3[24] = this.X0;
            array3[25] = this.X1;
            array3[26] = this.X2;
            array3[27] = this.X3;
            this.sb4(array3[28], array3[29], array3[30], array3[31]);
            array3[28] = this.X0;
            array3[29] = this.X1;
            array3[30] = this.X2;
            array3[31] = this.X3;
            this.sb3(array3[32], array3[33], array3[34], array3[35]);
            array3[32] = this.X0;
            array3[33] = this.X1;
            array3[34] = this.X2;
            array3[35] = this.X3;
            this.sb2(array3[36], array3[37], array3[38], array3[39]);
            array3[36] = this.X0;
            array3[37] = this.X1;
            array3[38] = this.X2;
            array3[39] = this.X3;
            this.sb1(array3[40], array3[41], array3[42], array3[43]);
            array3[40] = this.X0;
            array3[41] = this.X1;
            array3[42] = this.X2;
            array3[43] = this.X3;
            this.sb0(array3[44], array3[45], array3[46], array3[47]);
            array3[44] = this.X0;
            array3[45] = this.X1;
            array3[46] = this.X2;
            array3[47] = this.X3;
            this.sb7(array3[48], array3[49], array3[50], array3[51]);
            array3[48] = this.X0;
            array3[49] = this.X1;
            array3[50] = this.X2;
            array3[51] = this.X3;
            this.sb6(array3[52], array3[53], array3[54], array3[55]);
            array3[52] = this.X0;
            array3[53] = this.X1;
            array3[54] = this.X2;
            array3[55] = this.X3;
            this.sb5(array3[56], array3[57], array3[58], array3[59]);
            array3[56] = this.X0;
            array3[57] = this.X1;
            array3[58] = this.X2;
            array3[59] = this.X3;
            this.sb4(array3[60], array3[61], array3[62], array3[63]);
            array3[60] = this.X0;
            array3[61] = this.X1;
            array3[62] = this.X2;
            array3[63] = this.X3;
            this.sb3(array3[64], array3[65], array3[66], array3[67]);
            array3[64] = this.X0;
            array3[65] = this.X1;
            array3[66] = this.X2;
            array3[67] = this.X3;
            this.sb2(array3[68], array3[69], array3[70], array3[71]);
            array3[68] = this.X0;
            array3[69] = this.X1;
            array3[70] = this.X2;
            array3[71] = this.X3;
            this.sb1(array3[72], array3[73], array3[74], array3[75]);
            array3[72] = this.X0;
            array3[73] = this.X1;
            array3[74] = this.X2;
            array3[75] = this.X3;
            this.sb0(array3[76], array3[77], array3[78], array3[79]);
            array3[76] = this.X0;
            array3[77] = this.X1;
            array3[78] = this.X2;
            array3[79] = this.X3;
            this.sb7(array3[80], array3[81], array3[82], array3[83]);
            array3[80] = this.X0;
            array3[81] = this.X1;
            array3[82] = this.X2;
            array3[83] = this.X3;
            this.sb6(array3[84], array3[85], array3[86], array3[87]);
            array3[84] = this.X0;
            array3[85] = this.X1;
            array3[86] = this.X2;
            array3[87] = this.X3;
            this.sb5(array3[88], array3[89], array3[90], array3[91]);
            array3[88] = this.X0;
            array3[89] = this.X1;
            array3[90] = this.X2;
            array3[91] = this.X3;
            this.sb4(array3[92], array3[93], array3[94], array3[95]);
            array3[92] = this.X0;
            array3[93] = this.X1;
            array3[94] = this.X2;
            array3[95] = this.X3;
            this.sb3(array3[96], array3[97], array3[98], array3[99]);
            array3[96] = this.X0;
            array3[97] = this.X1;
            array3[98] = this.X2;
            array3[99] = this.X3;
            this.sb2(array3[100], array3[101], array3[102], array3[103]);
            array3[100] = this.X0;
            array3[101] = this.X1;
            array3[102] = this.X2;
            array3[103] = this.X3;
            this.sb1(array3[104], array3[105], array3[106], array3[107]);
            array3[104] = this.X0;
            array3[105] = this.X1;
            array3[106] = this.X2;
            array3[107] = this.X3;
            this.sb0(array3[108], array3[109], array3[110], array3[111]);
            array3[108] = this.X0;
            array3[109] = this.X1;
            array3[110] = this.X2;
            array3[111] = this.X3;
            this.sb7(array3[112], array3[113], array3[114], array3[115]);
            array3[112] = this.X0;
            array3[113] = this.X1;
            array3[114] = this.X2;
            array3[115] = this.X3;
            this.sb6(array3[116], array3[117], array3[118], array3[119]);
            array3[116] = this.X0;
            array3[117] = this.X1;
            array3[118] = this.X2;
            array3[119] = this.X3;
            this.sb5(array3[120], array3[121], array3[122], array3[123]);
            array3[120] = this.X0;
            array3[121] = this.X1;
            array3[122] = this.X2;
            array3[123] = this.X3;
            this.sb4(array3[124], array3[125], array3[126], array3[127]);
            array3[124] = this.X0;
            array3[125] = this.X1;
            array3[126] = this.X2;
            array3[127] = this.X3;
            this.sb3(array3[128], array3[129], array3[130], array3[131]);
            array3[128] = this.X0;
            array3[129] = this.X1;
            array3[130] = this.X2;
            array3[131] = this.X3;
            return array3;
        }
        throw new IllegalArgumentException("key must be a multiple of 4 bytes");
    }
    
    private int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    private int rotateRight(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private int bytesToWord(final byte[] array, final int n) {
        return (array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    private void wordToBytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)n;
        array[n2 + 2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 24);
    }
    
    private void encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        this.X3 = this.bytesToWord(array, n);
        this.X2 = this.bytesToWord(array, n + 4);
        this.X1 = this.bytesToWord(array, n + 8);
        this.X0 = this.bytesToWord(array, n + 12);
        this.sb0(this.wKey[0] ^ this.X0, this.wKey[1] ^ this.X1, this.wKey[2] ^ this.X2, this.wKey[3] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[4] ^ this.X0, this.wKey[5] ^ this.X1, this.wKey[6] ^ this.X2, this.wKey[7] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[8] ^ this.X0, this.wKey[9] ^ this.X1, this.wKey[10] ^ this.X2, this.wKey[11] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[12] ^ this.X0, this.wKey[13] ^ this.X1, this.wKey[14] ^ this.X2, this.wKey[15] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[16] ^ this.X0, this.wKey[17] ^ this.X1, this.wKey[18] ^ this.X2, this.wKey[19] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[20] ^ this.X0, this.wKey[21] ^ this.X1, this.wKey[22] ^ this.X2, this.wKey[23] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[24] ^ this.X0, this.wKey[25] ^ this.X1, this.wKey[26] ^ this.X2, this.wKey[27] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[28] ^ this.X0, this.wKey[29] ^ this.X1, this.wKey[30] ^ this.X2, this.wKey[31] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[32] ^ this.X0, this.wKey[33] ^ this.X1, this.wKey[34] ^ this.X2, this.wKey[35] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[36] ^ this.X0, this.wKey[37] ^ this.X1, this.wKey[38] ^ this.X2, this.wKey[39] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[40] ^ this.X0, this.wKey[41] ^ this.X1, this.wKey[42] ^ this.X2, this.wKey[43] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[44] ^ this.X0, this.wKey[45] ^ this.X1, this.wKey[46] ^ this.X2, this.wKey[47] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[48] ^ this.X0, this.wKey[49] ^ this.X1, this.wKey[50] ^ this.X2, this.wKey[51] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[52] ^ this.X0, this.wKey[53] ^ this.X1, this.wKey[54] ^ this.X2, this.wKey[55] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[56] ^ this.X0, this.wKey[57] ^ this.X1, this.wKey[58] ^ this.X2, this.wKey[59] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[60] ^ this.X0, this.wKey[61] ^ this.X1, this.wKey[62] ^ this.X2, this.wKey[63] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[64] ^ this.X0, this.wKey[65] ^ this.X1, this.wKey[66] ^ this.X2, this.wKey[67] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[68] ^ this.X0, this.wKey[69] ^ this.X1, this.wKey[70] ^ this.X2, this.wKey[71] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[72] ^ this.X0, this.wKey[73] ^ this.X1, this.wKey[74] ^ this.X2, this.wKey[75] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[76] ^ this.X0, this.wKey[77] ^ this.X1, this.wKey[78] ^ this.X2, this.wKey[79] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[80] ^ this.X0, this.wKey[81] ^ this.X1, this.wKey[82] ^ this.X2, this.wKey[83] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[84] ^ this.X0, this.wKey[85] ^ this.X1, this.wKey[86] ^ this.X2, this.wKey[87] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[88] ^ this.X0, this.wKey[89] ^ this.X1, this.wKey[90] ^ this.X2, this.wKey[91] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[92] ^ this.X0, this.wKey[93] ^ this.X1, this.wKey[94] ^ this.X2, this.wKey[95] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[96] ^ this.X0, this.wKey[97] ^ this.X1, this.wKey[98] ^ this.X2, this.wKey[99] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[100] ^ this.X0, this.wKey[101] ^ this.X1, this.wKey[102] ^ this.X2, this.wKey[103] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[104] ^ this.X0, this.wKey[105] ^ this.X1, this.wKey[106] ^ this.X2, this.wKey[107] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[108] ^ this.X0, this.wKey[109] ^ this.X1, this.wKey[110] ^ this.X2, this.wKey[111] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[112] ^ this.X0, this.wKey[113] ^ this.X1, this.wKey[114] ^ this.X2, this.wKey[115] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[116] ^ this.X0, this.wKey[117] ^ this.X1, this.wKey[118] ^ this.X2, this.wKey[119] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[120] ^ this.X0, this.wKey[121] ^ this.X1, this.wKey[122] ^ this.X2, this.wKey[123] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[124] ^ this.X0, this.wKey[125] ^ this.X1, this.wKey[126] ^ this.X2, this.wKey[127] ^ this.X3);
        this.wordToBytes(this.wKey[131] ^ this.X3, array2, n2);
        this.wordToBytes(this.wKey[130] ^ this.X2, array2, n2 + 4);
        this.wordToBytes(this.wKey[129] ^ this.X1, array2, n2 + 8);
        this.wordToBytes(this.wKey[128] ^ this.X0, array2, n2 + 12);
    }
    
    private void decryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        this.X3 = (this.wKey[131] ^ this.bytesToWord(array, n));
        this.X2 = (this.wKey[130] ^ this.bytesToWord(array, n + 4));
        this.X1 = (this.wKey[129] ^ this.bytesToWord(array, n + 8));
        this.ib7(this.X0 = (this.wKey[128] ^ this.bytesToWord(array, n + 12)), this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[124];
        this.X1 ^= this.wKey[125];
        this.X2 ^= this.wKey[126];
        this.X3 ^= this.wKey[127];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[120];
        this.X1 ^= this.wKey[121];
        this.X2 ^= this.wKey[122];
        this.X3 ^= this.wKey[123];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[116];
        this.X1 ^= this.wKey[117];
        this.X2 ^= this.wKey[118];
        this.X3 ^= this.wKey[119];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[112];
        this.X1 ^= this.wKey[113];
        this.X2 ^= this.wKey[114];
        this.X3 ^= this.wKey[115];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[108];
        this.X1 ^= this.wKey[109];
        this.X2 ^= this.wKey[110];
        this.X3 ^= this.wKey[111];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[104];
        this.X1 ^= this.wKey[105];
        this.X2 ^= this.wKey[106];
        this.X3 ^= this.wKey[107];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[100];
        this.X1 ^= this.wKey[101];
        this.X2 ^= this.wKey[102];
        this.X3 ^= this.wKey[103];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[96];
        this.X1 ^= this.wKey[97];
        this.X2 ^= this.wKey[98];
        this.X3 ^= this.wKey[99];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[92];
        this.X1 ^= this.wKey[93];
        this.X2 ^= this.wKey[94];
        this.X3 ^= this.wKey[95];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[88];
        this.X1 ^= this.wKey[89];
        this.X2 ^= this.wKey[90];
        this.X3 ^= this.wKey[91];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[84];
        this.X1 ^= this.wKey[85];
        this.X2 ^= this.wKey[86];
        this.X3 ^= this.wKey[87];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[80];
        this.X1 ^= this.wKey[81];
        this.X2 ^= this.wKey[82];
        this.X3 ^= this.wKey[83];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[76];
        this.X1 ^= this.wKey[77];
        this.X2 ^= this.wKey[78];
        this.X3 ^= this.wKey[79];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[72];
        this.X1 ^= this.wKey[73];
        this.X2 ^= this.wKey[74];
        this.X3 ^= this.wKey[75];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[68];
        this.X1 ^= this.wKey[69];
        this.X2 ^= this.wKey[70];
        this.X3 ^= this.wKey[71];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[64];
        this.X1 ^= this.wKey[65];
        this.X2 ^= this.wKey[66];
        this.X3 ^= this.wKey[67];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[60];
        this.X1 ^= this.wKey[61];
        this.X2 ^= this.wKey[62];
        this.X3 ^= this.wKey[63];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[56];
        this.X1 ^= this.wKey[57];
        this.X2 ^= this.wKey[58];
        this.X3 ^= this.wKey[59];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[52];
        this.X1 ^= this.wKey[53];
        this.X2 ^= this.wKey[54];
        this.X3 ^= this.wKey[55];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[48];
        this.X1 ^= this.wKey[49];
        this.X2 ^= this.wKey[50];
        this.X3 ^= this.wKey[51];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[44];
        this.X1 ^= this.wKey[45];
        this.X2 ^= this.wKey[46];
        this.X3 ^= this.wKey[47];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[40];
        this.X1 ^= this.wKey[41];
        this.X2 ^= this.wKey[42];
        this.X3 ^= this.wKey[43];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[36];
        this.X1 ^= this.wKey[37];
        this.X2 ^= this.wKey[38];
        this.X3 ^= this.wKey[39];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[32];
        this.X1 ^= this.wKey[33];
        this.X2 ^= this.wKey[34];
        this.X3 ^= this.wKey[35];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[28];
        this.X1 ^= this.wKey[29];
        this.X2 ^= this.wKey[30];
        this.X3 ^= this.wKey[31];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[24];
        this.X1 ^= this.wKey[25];
        this.X2 ^= this.wKey[26];
        this.X3 ^= this.wKey[27];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[20];
        this.X1 ^= this.wKey[21];
        this.X2 ^= this.wKey[22];
        this.X3 ^= this.wKey[23];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[16];
        this.X1 ^= this.wKey[17];
        this.X2 ^= this.wKey[18];
        this.X3 ^= this.wKey[19];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[12];
        this.X1 ^= this.wKey[13];
        this.X2 ^= this.wKey[14];
        this.X3 ^= this.wKey[15];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[8];
        this.X1 ^= this.wKey[9];
        this.X2 ^= this.wKey[10];
        this.X3 ^= this.wKey[11];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[4];
        this.X1 ^= this.wKey[5];
        this.X2 ^= this.wKey[6];
        this.X3 ^= this.wKey[7];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.wordToBytes(this.X3 ^ this.wKey[3], array2, n2);
        this.wordToBytes(this.X2 ^ this.wKey[2], array2, n2 + 4);
        this.wordToBytes(this.X1 ^ this.wKey[1], array2, n2 + 8);
        this.wordToBytes(this.X0 ^ this.wKey[0], array2, n2 + 12);
    }
    
    private void sb0(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n ^ n4;
        final int n6 = n3 ^ n5;
        final int n7 = n2 ^ n6;
        this.X3 = ((n & n4) ^ n7);
        final int n8 = n ^ (n2 & n5);
        this.X2 = (n7 ^ (n3 | n8));
        final int n9 = this.X3 & (n6 ^ n8);
        this.X1 = (~n6 ^ n9);
        this.X0 = (n9 ^ ~n8);
    }
    
    private void ib0(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n ^ n2;
        final int n7 = n4 ^ (n5 | n6);
        final int n8 = n3 ^ n7;
        this.X2 = (n6 ^ n8);
        final int n9 = n5 ^ (n4 & n6);
        this.X1 = (n7 ^ (this.X2 & n9));
        this.X3 = ((n & n7) ^ (n8 | this.X1));
        this.X0 = (this.X3 ^ (n8 ^ n9));
    }
    
    private void sb1(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n2 ^ ~n;
        final int n6 = n3 ^ (n | n5);
        this.X2 = (n4 ^ n6);
        final int n7 = n2 ^ (n4 | n5);
        final int n8 = n5 ^ this.X2;
        this.X3 = (n8 ^ (n6 & n7));
        final int n9 = n6 ^ n7;
        this.X1 = (this.X3 ^ n9);
        this.X0 = (n6 ^ (n8 & n9));
    }
    
    private void ib1(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n2 ^ n4;
        final int n6 = n ^ (n2 & n5);
        final int n7 = n5 ^ n6;
        this.X3 = (n3 ^ n7);
        final int n8 = n2 ^ (n5 & n6);
        this.X1 = (n6 ^ (this.X3 | n8));
        final int n9 = ~this.X1;
        final int n10 = this.X3 ^ n8;
        this.X0 = (n9 ^ n10);
        this.X2 = (n7 ^ (n9 | n10));
    }
    
    private void sb2(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n2 ^ n4;
        this.X0 = (n6 ^ (n3 & n5));
        final int n7 = n3 ^ n5;
        final int n8 = n2 & (n3 ^ this.X0);
        this.X3 = (n7 ^ n8);
        this.X2 = (n ^ ((n4 | n8) & (this.X0 | n7)));
        this.X1 = (n6 ^ this.X3 ^ (this.X2 ^ (n4 | n5)));
    }
    
    private void ib2(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n2 ^ n4;
        final int n6 = ~n5;
        final int n7 = n ^ n3;
        final int n8 = n3 ^ n5;
        this.X0 = (n7 ^ (n2 & n8));
        this.X3 = (n5 ^ (n7 | (n4 ^ (n | n6))));
        final int n9 = ~n8;
        final int n10 = this.X0 | this.X3;
        this.X1 = (n9 ^ n10);
        this.X2 = ((n4 & n9) ^ (n7 ^ n10));
    }
    
    private void sb3(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n ^ n2;
        final int n6 = n & n3;
        final int n7 = n | n4;
        final int n8 = n3 ^ n4;
        final int n9 = n6 | (n5 & n7);
        this.X2 = (n8 ^ n9);
        final int n10 = n9 ^ (n2 ^ n7);
        this.X0 = (n5 ^ (n8 & n10));
        final int n11 = this.X2 & this.X0;
        this.X1 = (n10 ^ n11);
        this.X3 = ((n2 | n4) ^ (n8 ^ n11));
    }
    
    private void ib3(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n | n2;
        final int n6 = n2 ^ n3;
        final int n7 = n ^ (n2 & n6);
        final int n8 = n3 ^ n7;
        final int n9 = n4 | n7;
        this.X0 = (n6 ^ n9);
        final int n10 = n4 ^ (n6 | n9);
        this.X2 = (n8 ^ n10);
        final int n11 = n5 ^ n10;
        this.X3 = (n7 ^ (this.X0 & n11));
        this.X1 = (this.X3 ^ (this.X0 ^ n11));
    }
    
    private void sb4(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n ^ n4;
        final int n6 = n3 ^ (n4 & n5);
        final int n7 = n2 | n6;
        this.X3 = (n5 ^ n7);
        final int n8 = ~n2;
        this.X0 = (n6 ^ (n5 | n8));
        final int n9 = n & this.X0;
        final int n10 = n5 ^ n8;
        this.X2 = (n9 ^ (n7 & n10));
        this.X1 = (n ^ n6 ^ (n10 & this.X2));
    }
    
    private void ib4(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n2 ^ (n & (n3 | n4));
        final int n6 = n3 ^ (n & n5);
        this.X1 = (n4 ^ n6);
        final int n7 = ~n;
        this.X3 = (n5 ^ (n6 & this.X1));
        final int n8 = n4 ^ (this.X1 | n7);
        this.X0 = (this.X3 ^ n8);
        this.X2 = ((n5 & n8) ^ (this.X1 ^ n7));
    }
    
    private void sb5(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n ^ n2;
        final int n7 = n ^ n4;
        this.X0 = (n3 ^ n5 ^ (n6 | n7));
        final int n8 = n4 & this.X0;
        this.X1 = (n8 ^ (n6 ^ this.X0));
        final int n9 = n5 | this.X0;
        final int n10 = n6 | n8;
        final int n11 = n7 ^ n9;
        this.X2 = (n10 ^ n11);
        this.X3 = (n2 ^ n8 ^ (this.X1 & n11));
    }
    
    private void ib5(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n3;
        final int n6 = n4 ^ (n2 & n5);
        final int n7 = n & n6;
        this.X3 = (n7 ^ (n2 ^ n5));
        final int n8 = n2 | this.X3;
        this.X1 = (n6 ^ (n & n8));
        final int n9 = n | n4;
        this.X0 = (n9 ^ (n5 ^ n8));
        this.X2 = ((n2 & n9) ^ (n7 | (n ^ n3)));
    }
    
    private void sb6(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n ^ n4;
        final int n7 = n2 ^ n6;
        final int n8 = n3 ^ (n5 | n6);
        this.X1 = (n2 ^ n8);
        final int n9 = n4 ^ (n6 | this.X1);
        this.X2 = (n7 ^ (n8 & n9));
        final int n10 = n8 ^ n9;
        this.X0 = (this.X2 ^ n10);
        this.X3 = (~n8 ^ (n7 & n10));
    }
    
    private void ib6(final int n, final int n2, final int n3, final int n4) {
        final int n5 = ~n;
        final int n6 = n ^ n2;
        final int n7 = n3 ^ n6;
        final int n8 = n4 ^ (n3 | n5);
        this.X1 = (n7 ^ n8);
        final int n9 = n6 ^ (n7 & n8);
        this.X3 = (n8 ^ (n2 | n9));
        final int n10 = n2 | this.X3;
        this.X0 = (n9 ^ n10);
        this.X2 = ((n4 & n5) ^ (n7 ^ n10));
    }
    
    private void sb7(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n2 ^ n3;
        final int n6 = n4 ^ (n3 & n5);
        final int n7 = n ^ n6;
        this.X1 = (n2 ^ (n7 & (n4 | n5)));
        final int n8 = n6 | this.X1;
        this.X3 = (n5 ^ (n & n7));
        final int n9 = n7 ^ n8;
        this.X2 = (n6 ^ (this.X3 & n9));
        this.X0 = (~n9 ^ (this.X3 & this.X2));
    }
    
    private void ib7(final int n, final int n2, final int n3, final int n4) {
        final int n5 = n3 | (n & n2);
        final int n6 = n4 & (n | n2);
        this.X3 = (n5 ^ n6);
        final int n7 = ~n4;
        final int n8 = n2 ^ n6;
        this.X1 = (n ^ (n8 | (this.X3 ^ n7)));
        this.X0 = (n3 ^ n8 ^ (n4 | this.X1));
        this.X2 = (n5 ^ this.X1 ^ (this.X0 ^ (n & this.X3)));
    }
    
    private void LT() {
        final int rotateLeft = this.rotateLeft(this.X0, 13);
        final int rotateLeft2 = this.rotateLeft(this.X2, 3);
        final int n = this.X1 ^ rotateLeft ^ rotateLeft2;
        final int n2 = this.X3 ^ rotateLeft2 ^ rotateLeft << 3;
        this.X1 = this.rotateLeft(n, 1);
        this.X3 = this.rotateLeft(n2, 7);
        this.X0 = this.rotateLeft(rotateLeft ^ this.X1 ^ this.X3, 5);
        this.X2 = this.rotateLeft(rotateLeft2 ^ this.X3 ^ this.X1 << 7, 22);
    }
    
    private void inverseLT() {
        final int n = this.rotateRight(this.X2, 22) ^ this.X3 ^ this.X1 << 7;
        final int n2 = this.rotateRight(this.X0, 5) ^ this.X1 ^ this.X3;
        final int rotateRight = this.rotateRight(this.X3, 7);
        final int rotateRight2 = this.rotateRight(this.X1, 1);
        this.X3 = (rotateRight ^ n ^ n2 << 3);
        this.X1 = (rotateRight2 ^ n2 ^ n);
        this.X2 = this.rotateRight(n, 3);
        this.X0 = this.rotateRight(n2, 13);
    }
}
