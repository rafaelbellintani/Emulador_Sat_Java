// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.encoders;

public class HexTranslator implements Translator
{
    private static final byte[] hexTable;
    
    @Override
    public int getEncodedBlockSize() {
        return 2;
    }
    
    @Override
    public int encode(final byte[] array, int n, final int n2, final byte[] array2, final int n3) {
        for (int i = 0, n4 = 0; i < n2; ++i, n4 += 2) {
            array2[n3 + n4] = HexTranslator.hexTable[array[n] >> 4 & 0xF];
            array2[n3 + n4 + 1] = HexTranslator.hexTable[array[n] & 0xF];
            ++n;
        }
        return n2 * 2;
    }
    
    @Override
    public int getDecodedBlockSize() {
        return 1;
    }
    
    @Override
    public int decode(final byte[] array, final int n, final int n2, final byte[] array2, int n3) {
        final int n4 = n2 / 2;
        for (int i = 0; i < n4; ++i) {
            final byte b = array[n + i * 2];
            final byte b2 = array[n + i * 2 + 1];
            if (b < 97) {
                array2[n3] = (byte)(b - 48 << 4);
            }
            else {
                array2[n3] = (byte)(b - 97 + 10 << 4);
            }
            if (b2 < 97) {
                final int n5 = n3;
                array2[n5] += (byte)(b2 - 48);
            }
            else {
                final int n6 = n3;
                array2[n6] += (byte)(b2 - 97 + 10);
            }
            ++n3;
        }
        return n4;
    }
    
    static {
        hexTable = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
    }
}
