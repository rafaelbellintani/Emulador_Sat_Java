// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

public class Base64Encoder implements Encoder
{
    protected final byte[] encodingTable;
    protected byte padding;
    protected final byte[] decodingTable;
    
    protected void initialiseDecodingTable() {
        for (int i = 0; i < this.encodingTable.length; ++i) {
            this.decodingTable[this.encodingTable[i]] = (byte)i;
        }
    }
    
    public Base64Encoder() {
        this.encodingTable = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        this.padding = 61;
        this.decodingTable = new byte[128];
        this.initialiseDecodingTable();
    }
    
    @Override
    public int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        final int n3 = n2 % 3;
        final int n4 = n2 - n3;
        for (int i = n; i < n + n4; i += 3) {
            final int n5 = array[i] & 0xFF;
            final int n6 = array[i + 1] & 0xFF;
            final int n7 = array[i + 2] & 0xFF;
            outputStream.write(this.encodingTable[n5 >>> 2 & 0x3F]);
            outputStream.write(this.encodingTable[(n5 << 4 | n6 >>> 4) & 0x3F]);
            outputStream.write(this.encodingTable[(n6 << 2 | n7 >>> 6) & 0x3F]);
            outputStream.write(this.encodingTable[n7 & 0x3F]);
        }
        switch (n3) {
            case 1: {
                final int n8 = array[n + n4] & 0xFF;
                final int n9 = n8 >>> 2 & 0x3F;
                final int n10 = n8 << 4 & 0x3F;
                outputStream.write(this.encodingTable[n9]);
                outputStream.write(this.encodingTable[n10]);
                outputStream.write(this.padding);
                outputStream.write(this.padding);
                break;
            }
            case 2: {
                final int n11 = array[n + n4] & 0xFF;
                final int n12 = array[n + n4 + 1] & 0xFF;
                final int n13 = n11 >>> 2 & 0x3F;
                final int n14 = (n11 << 4 | n12 >>> 4) & 0x3F;
                final int n15 = n12 << 2 & 0x3F;
                outputStream.write(this.encodingTable[n13]);
                outputStream.write(this.encodingTable[n14]);
                outputStream.write(this.encodingTable[n15]);
                outputStream.write(this.padding);
                break;
            }
        }
        return n4 / 3 * 4 + ((n3 == 0) ? 0 : 4);
    }
    
    private boolean ignore(final char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }
    
    @Override
    public int decode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        int n3 = 0;
        int n4;
        for (n4 = n + n2; n4 > n && this.ignore((char)array[n4 - 1]); --n4) {}
        int nextI3;
        for (int n5 = n4 - 4, i = this.nextI(array, n, n5); i < n5; i = this.nextI(array, nextI3, n5)) {
            final byte b = this.decodingTable[array[i++]];
            int nextI = this.nextI(array, i, n5);
            final byte b2 = this.decodingTable[array[nextI++]];
            int nextI2 = this.nextI(array, nextI, n5);
            final byte b3 = this.decodingTable[array[nextI2++]];
            nextI3 = this.nextI(array, nextI2, n5);
            final byte b4 = this.decodingTable[array[nextI3++]];
            outputStream.write(b << 2 | b2 >> 4);
            outputStream.write(b2 << 4 | b3 >> 2);
            outputStream.write(b3 << 6 | b4);
            n3 += 3;
        }
        return n3 + this.decodeLastBlock(outputStream, (char)array[n4 - 4], (char)array[n4 - 3], (char)array[n4 - 2], (char)array[n4 - 1]);
    }
    
    private int nextI(final byte[] array, int n, final int n2) {
        while (n < n2 && this.ignore((char)array[n])) {
            ++n;
        }
        return n;
    }
    
    @Override
    public int decode(final String s, final OutputStream outputStream) throws IOException {
        int n = 0;
        int length;
        for (length = s.length(); length > 0 && this.ignore(s.charAt(length - 1)); --length) {}
        final int n2 = 0;
        int nextI3;
        for (int n3 = length - 4, i = this.nextI(s, n2, n3); i < n3; i = this.nextI(s, nextI3, n3)) {
            final byte b = this.decodingTable[s.charAt(i++)];
            int nextI = this.nextI(s, i, n3);
            final byte b2 = this.decodingTable[s.charAt(nextI++)];
            int nextI2 = this.nextI(s, nextI, n3);
            final byte b3 = this.decodingTable[s.charAt(nextI2++)];
            nextI3 = this.nextI(s, nextI2, n3);
            final byte b4 = this.decodingTable[s.charAt(nextI3++)];
            outputStream.write(b << 2 | b2 >> 4);
            outputStream.write(b2 << 4 | b3 >> 2);
            outputStream.write(b3 << 6 | b4);
            n += 3;
        }
        return n + this.decodeLastBlock(outputStream, s.charAt(length - 4), s.charAt(length - 3), s.charAt(length - 2), s.charAt(length - 1));
    }
    
    private int decodeLastBlock(final OutputStream outputStream, final char c, final char c2, final char c3, final char c4) throws IOException {
        if (c3 == this.padding) {
            outputStream.write(this.decodingTable[c] << 2 | this.decodingTable[c2] >> 4);
            return 1;
        }
        if (c4 == this.padding) {
            final byte b = this.decodingTable[c];
            final byte b2 = this.decodingTable[c2];
            final byte b3 = this.decodingTable[c3];
            outputStream.write(b << 2 | b2 >> 4);
            outputStream.write(b2 << 4 | b3 >> 2);
            return 2;
        }
        final byte b4 = this.decodingTable[c];
        final byte b5 = this.decodingTable[c2];
        final byte b6 = this.decodingTable[c3];
        final byte b7 = this.decodingTable[c4];
        outputStream.write(b4 << 2 | b5 >> 4);
        outputStream.write(b5 << 4 | b6 >> 2);
        outputStream.write(b6 << 6 | b7);
        return 3;
    }
    
    private int nextI(final String s, int index, final int n) {
        while (index < n && this.ignore(s.charAt(index))) {
            ++index;
        }
        return index;
    }
}
