// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import org.bouncycastle.util.Arrays;
import java.math.BigInteger;

class IntArray
{
    private int[] m_ints;
    
    public IntArray(final int n) {
        this.m_ints = new int[n];
    }
    
    public IntArray(final int[] ints) {
        this.m_ints = ints;
    }
    
    public IntArray(final BigInteger bigInteger) {
        this(bigInteger, 0);
    }
    
    public IntArray(final BigInteger bigInteger, final int n) {
        if (bigInteger.signum() == -1) {
            throw new IllegalArgumentException("Only positive Integers allowed");
        }
        if (bigInteger.equals(ECConstants.ZERO)) {
            this.m_ints = new int[] { 0 };
            return;
        }
        final byte[] byteArray = bigInteger.toByteArray();
        int length = byteArray.length;
        int n2 = 0;
        if (byteArray[0] == 0) {
            --length;
            n2 = 1;
        }
        final int n3 = (length + 3) / 4;
        if (n3 < n) {
            this.m_ints = new int[n];
        }
        else {
            this.m_ints = new int[n3];
        }
        int i = n3 - 1;
        final int n4 = length % 4 + n2;
        int n5 = 0;
        int j = n2;
        if (n2 < n4) {
            while (j < n4) {
                final int n6 = n5 << 8;
                int n7 = byteArray[j];
                if (n7 < 0) {
                    n7 += 256;
                }
                n5 = (n6 | n7);
                ++j;
            }
            this.m_ints[i--] = n5;
        }
        while (i >= 0) {
            int n8 = 0;
            for (int k = 0; k < 4; ++k) {
                final int n9 = n8 << 8;
                int n10 = byteArray[j++];
                if (n10 < 0) {
                    n10 += 256;
                }
                n8 = (n9 | n10);
            }
            this.m_ints[i] = n8;
            --i;
        }
    }
    
    public boolean isZero() {
        return this.m_ints.length == 0 || (this.m_ints[0] == 0 && this.getUsedLength() == 0);
    }
    
    public int getUsedLength() {
        int length = this.m_ints.length;
        if (length < 1) {
            return 0;
        }
        if (this.m_ints[0] != 0) {
            while (this.m_ints[--length] == 0) {}
            return length + 1;
        }
        while (this.m_ints[--length] == 0) {
            if (length <= 0) {
                return 0;
            }
        }
        return length + 1;
    }
    
    public int bitLength() {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return 0;
        }
        final int n = usedLength - 1;
        int i = this.m_ints[n];
        int n2 = (n << 5) + 1;
        if ((i & 0xFFFF0000) != 0x0) {
            if ((i & 0xFF000000) != 0x0) {
                n2 += 24;
                i >>>= 24;
            }
            else {
                n2 += 16;
                i >>>= 16;
            }
        }
        else if (i > 255) {
            n2 += 8;
            i >>>= 8;
        }
        while (i != 1) {
            ++n2;
            i >>>= 1;
        }
        return n2;
    }
    
    private int[] resizedInts(final int n) {
        final int[] array = new int[n];
        final int length = this.m_ints.length;
        System.arraycopy(this.m_ints, 0, array, 0, (length < n) ? length : n);
        return array;
    }
    
    public BigInteger toBigInteger() {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return ECConstants.ZERO;
        }
        final int n = this.m_ints[usedLength - 1];
        final byte[] array = new byte[4];
        int n2 = 0;
        int n3 = 0;
        for (int i = 3; i >= 0; --i) {
            final byte b = (byte)(n >>> 8 * i);
            if (n3 != 0 || b != 0) {
                n3 = 1;
                array[n2++] = b;
            }
        }
        final byte[] magnitude = new byte[4 * (usedLength - 1) + n2];
        for (int j = 0; j < n2; ++j) {
            magnitude[j] = array[j];
        }
        for (int k = usedLength - 2; k >= 0; --k) {
            for (int l = 3; l >= 0; --l) {
                magnitude[n2++] = (byte)(this.m_ints[k] >>> 8 * l);
            }
        }
        return new BigInteger(1, magnitude);
    }
    
    public void shiftLeft() {
        int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return;
        }
        if (this.m_ints[usedLength - 1] < 0 && ++usedLength > this.m_ints.length) {
            this.m_ints = this.resizedInts(this.m_ints.length + 1);
        }
        int n = 0;
        for (int i = 0; i < usedLength; ++i) {
            final boolean b = this.m_ints[i] < 0;
            final int[] ints = this.m_ints;
            final int n2 = i;
            ints[n2] <<= 1;
            if (n != 0) {
                final int[] ints2 = this.m_ints;
                final int n3 = i;
                ints2[n3] |= 0x1;
            }
            n = (b ? 1 : 0);
        }
    }
    
    public IntArray shiftLeft(final int i) {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return this;
        }
        if (i == 0) {
            return this;
        }
        if (i > 31) {
            throw new IllegalArgumentException("shiftLeft() for max 31 bits , " + i + "bit shift is not possible");
        }
        final int[] array = new int[usedLength + 1];
        final int n = 32 - i;
        array[0] = this.m_ints[0] << i;
        for (int j = 1; j < usedLength; ++j) {
            array[j] = (this.m_ints[j] << i | this.m_ints[j - 1] >>> n);
        }
        array[usedLength] = this.m_ints[usedLength - 1] >>> n;
        return new IntArray(array);
    }
    
    public void addShifted(final IntArray intArray, final int n) {
        final int usedLength = intArray.getUsedLength();
        final int n2 = usedLength + n;
        if (n2 > this.m_ints.length) {
            this.m_ints = this.resizedInts(n2);
        }
        for (int i = 0; i < usedLength; ++i) {
            final int[] ints = this.m_ints;
            final int n3 = i + n;
            ints[n3] ^= intArray.m_ints[i];
        }
    }
    
    public int getLength() {
        return this.m_ints.length;
    }
    
    public boolean testBit(final int n) {
        return (this.m_ints[n >> 5] & 1 << (n & 0x1F)) != 0x0;
    }
    
    public void flipBit(final int n) {
        final int n2 = n >> 5;
        final int n3 = 1 << (n & 0x1F);
        final int[] ints = this.m_ints;
        final int n4 = n2;
        ints[n4] ^= n3;
    }
    
    public void setBit(final int n) {
        final int n2 = n >> 5;
        final int n3 = 1 << (n & 0x1F);
        final int[] ints = this.m_ints;
        final int n4 = n2;
        ints[n4] |= n3;
    }
    
    public IntArray multiply(final IntArray intArray, final int n) {
        final int n2 = n + 31 >> 5;
        if (this.m_ints.length < n2) {
            this.m_ints = this.resizedInts(n2);
        }
        final IntArray intArray2 = new IntArray(intArray.resizedInts(intArray.getLength() + 1));
        final IntArray intArray3 = new IntArray(n + n + 31 >> 5);
        int n3 = 1;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < n2; ++j) {
                if ((this.m_ints[j] & n3) != 0x0) {
                    intArray3.addShifted(intArray2, j);
                }
            }
            n3 <<= 1;
            intArray2.shiftLeft();
        }
        return intArray3;
    }
    
    public void reduce(final int n, final int[] array) {
        for (int i = n + n - 2; i >= n; --i) {
            if (this.testBit(i)) {
                final int n2 = i - n;
                this.flipBit(n2);
                this.flipBit(i);
                int length = array.length;
                while (--length >= 0) {
                    this.flipBit(array[length] + n2);
                }
            }
        }
        this.m_ints = this.resizedInts(n + 31 >> 5);
    }
    
    public IntArray square(final int n) {
        final int[] array = { 0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85 };
        final int n2 = n + 31 >> 5;
        if (this.m_ints.length < n2) {
            this.m_ints = this.resizedInts(n2);
        }
        final IntArray intArray = new IntArray(n2 + n2);
        for (int i = 0; i < n2; ++i) {
            int n3 = 0;
            for (int j = 0; j < 4; ++j) {
                n3 = (n3 >>> 8 | array[this.m_ints[i] >>> j * 4 & 0xF] << 24);
            }
            intArray.m_ints[i + i] = n3;
            int n4 = 0;
            final int n5 = this.m_ints[i] >>> 16;
            for (int k = 0; k < 4; ++k) {
                n4 = (n4 >>> 8 | array[n5 >>> k * 4 & 0xF] << 24);
            }
            intArray.m_ints[i + i + 1] = n4;
        }
        return intArray;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof IntArray)) {
            return false;
        }
        final IntArray intArray = (IntArray)o;
        final int usedLength = this.getUsedLength();
        if (intArray.getUsedLength() != usedLength) {
            return false;
        }
        for (int i = 0; i < usedLength; ++i) {
            if (this.m_ints[i] != intArray.m_ints[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int usedLength = this.getUsedLength();
        int n = 1;
        for (int i = 0; i < usedLength; ++i) {
            n = n * 31 + this.m_ints[i];
        }
        return n;
    }
    
    public Object clone() {
        return new IntArray(Arrays.clone(this.m_ints));
    }
    
    @Override
    public String toString() {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return "0";
        }
        final StringBuffer sb = new StringBuffer(Integer.toBinaryString(this.m_ints[usedLength - 1]));
        for (int i = usedLength - 2; i >= 0; --i) {
            String s = Integer.toBinaryString(this.m_ints[i]);
            for (int j = s.length(); j < 8; ++j) {
                s = "0" + s;
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
