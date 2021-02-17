// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.util.Random;
import java.math.BigInteger;

public abstract class ECFieldElement implements ECConstants
{
    public abstract BigInteger toBigInteger();
    
    public abstract String getFieldName();
    
    public abstract int getFieldSize();
    
    public abstract ECFieldElement add(final ECFieldElement p0);
    
    public abstract ECFieldElement subtract(final ECFieldElement p0);
    
    public abstract ECFieldElement multiply(final ECFieldElement p0);
    
    public abstract ECFieldElement divide(final ECFieldElement p0);
    
    public abstract ECFieldElement negate();
    
    public abstract ECFieldElement square();
    
    public abstract ECFieldElement invert();
    
    public abstract ECFieldElement sqrt();
    
    @Override
    public String toString() {
        return this.toBigInteger().toString(2);
    }
    
    public static class F2m extends ECFieldElement
    {
        public static final int GNB = 1;
        public static final int TPB = 2;
        public static final int PPB = 3;
        private int representation;
        private int m;
        private int k1;
        private int k2;
        private int k3;
        private IntArray x;
        private int t;
        
        public F2m(final int m, final int k1, final int k2, final int k3, final BigInteger bigInteger) {
            this.t = m + 31 >> 5;
            this.x = new IntArray(bigInteger, this.t);
            if (k2 == 0 && k3 == 0) {
                this.representation = 2;
            }
            else {
                if (k2 >= k3) {
                    throw new IllegalArgumentException("k2 must be smaller than k3");
                }
                if (k2 <= 0) {
                    throw new IllegalArgumentException("k2 must be larger than 0");
                }
                this.representation = 3;
            }
            if (bigInteger.signum() < 0) {
                throw new IllegalArgumentException("x value cannot be negative");
            }
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
        }
        
        public F2m(final int n, final int n2, final BigInteger bigInteger) {
            this(n, n2, 0, 0, bigInteger);
        }
        
        private F2m(final int m, final int k1, final int k2, final int k3, final IntArray x) {
            this.t = m + 31 >> 5;
            this.x = x;
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            if (k2 == 0 && k3 == 0) {
                this.representation = 2;
            }
            else {
                this.representation = 3;
            }
        }
        
        @Override
        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }
        
        @Override
        public String getFieldName() {
            return "F2m";
        }
        
        @Override
        public int getFieldSize() {
            return this.m;
        }
        
        public static void checkFieldElements(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            if (!(ecFieldElement instanceof F2m) || !(ecFieldElement2 instanceof F2m)) {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
            final F2m f2m = (F2m)ecFieldElement;
            final F2m f2m2 = (F2m)ecFieldElement2;
            if (f2m.m != f2m2.m || f2m.k1 != f2m2.k1 || f2m.k2 != f2m2.k2 || f2m.k3 != f2m2.k3) {
                throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
            }
            if (f2m.representation != f2m2.representation) {
                throw new IllegalArgumentException("One of the field elements are not elements has incorrect representation");
            }
        }
        
        @Override
        public ECFieldElement add(final ECFieldElement ecFieldElement) {
            final IntArray intArray = (IntArray)this.x.clone();
            intArray.addShifted(((F2m)ecFieldElement).x, 0);
            return new F2m(this.m, this.k1, this.k2, this.k3, intArray);
        }
        
        @Override
        public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
            return this.add(ecFieldElement);
        }
        
        @Override
        public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
            final IntArray multiply = this.x.multiply(((F2m)ecFieldElement).x, this.m);
            multiply.reduce(this.m, new int[] { this.k1, this.k2, this.k3 });
            return new F2m(this.m, this.k1, this.k2, this.k3, multiply);
        }
        
        @Override
        public ECFieldElement divide(final ECFieldElement ecFieldElement) {
            return this.multiply(ecFieldElement.invert());
        }
        
        @Override
        public ECFieldElement negate() {
            return this;
        }
        
        @Override
        public ECFieldElement square() {
            final IntArray square = this.x.square(this.m);
            square.reduce(this.m, new int[] { this.k1, this.k2, this.k3 });
            return new F2m(this.m, this.k1, this.k2, this.k3, square);
        }
        
        @Override
        public ECFieldElement invert() {
            IntArray intArray = (IntArray)this.x.clone();
            IntArray intArray2 = new IntArray(this.t);
            intArray2.setBit(this.m);
            intArray2.setBit(0);
            intArray2.setBit(this.k1);
            if (this.representation == 3) {
                intArray2.setBit(this.k2);
                intArray2.setBit(this.k3);
            }
            IntArray intArray3 = new IntArray(this.t);
            intArray3.setBit(0);
            IntArray intArray4 = new IntArray(this.t);
            while (!intArray.isZero()) {
                int n = intArray.bitLength() - intArray2.bitLength();
                if (n < 0) {
                    final IntArray intArray5 = intArray;
                    intArray = intArray2;
                    intArray2 = intArray5;
                    final IntArray intArray6 = intArray3;
                    intArray3 = intArray4;
                    intArray4 = intArray6;
                    n = -n;
                }
                final int n2 = n >> 5;
                final int n3 = n & 0x1F;
                intArray.addShifted(intArray2.shiftLeft(n3), n2);
                intArray3.addShifted(intArray4.shiftLeft(n3), n2);
            }
            return new F2m(this.m, this.k1, this.k2, this.k3, intArray4);
        }
        
        @Override
        public ECFieldElement sqrt() {
            throw new RuntimeException("Not implemented");
        }
        
        public int getRepresentation() {
            return this.representation;
        }
        
        public int getM() {
            return this.m;
        }
        
        public int getK1() {
            return this.k1;
        }
        
        public int getK2() {
            return this.k2;
        }
        
        public int getK3() {
            return this.k3;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof F2m)) {
                return false;
            }
            final F2m f2m = (F2m)o;
            return this.m == f2m.m && this.k1 == f2m.k1 && this.k2 == f2m.k2 && this.k3 == f2m.k3 && this.representation == f2m.representation && this.x.equals(f2m.x);
        }
        
        @Override
        public int hashCode() {
            return this.x.hashCode() ^ this.m ^ this.k1 ^ this.k2 ^ this.k3;
        }
    }
    
    public static class Fp extends ECFieldElement
    {
        BigInteger x;
        BigInteger q;
        
        public Fp(final BigInteger bigInteger, final BigInteger x) {
            this.x = x;
            if (x.compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("x value too large in field element");
            }
            this.q = bigInteger;
        }
        
        @Override
        public BigInteger toBigInteger() {
            return this.x;
        }
        
        @Override
        public String getFieldName() {
            return "Fp";
        }
        
        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }
        
        public BigInteger getQ() {
            return this.q;
        }
        
        @Override
        public ECFieldElement add(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.x.add(ecFieldElement.toBigInteger()).mod(this.q));
        }
        
        @Override
        public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.x.subtract(ecFieldElement.toBigInteger()).mod(this.q));
        }
        
        @Override
        public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.x.multiply(ecFieldElement.toBigInteger()).mod(this.q));
        }
        
        @Override
        public ECFieldElement divide(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.x.multiply(ecFieldElement.toBigInteger().modInverse(this.q)).mod(this.q));
        }
        
        @Override
        public ECFieldElement negate() {
            return new Fp(this.q, this.x.negate().mod(this.q));
        }
        
        @Override
        public ECFieldElement square() {
            return new Fp(this.q, this.x.multiply(this.x).mod(this.q));
        }
        
        @Override
        public ECFieldElement invert() {
            return new Fp(this.q, this.x.modInverse(this.q));
        }
        
        @Override
        public ECFieldElement sqrt() {
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            }
            if (this.q.testBit(1)) {
                final Fp fp = new Fp(this.q, this.x.modPow(this.q.shiftRight(2).add(ECConstants.ONE), this.q));
                return fp.square().equals(this) ? fp : null;
            }
            final BigInteger subtract = this.q.subtract(ECConstants.ONE);
            final BigInteger shiftRight = subtract.shiftRight(1);
            if (!this.x.modPow(shiftRight, this.q).equals(ECConstants.ONE)) {
                return null;
            }
            final BigInteger add = subtract.shiftRight(2).shiftLeft(1).add(ECConstants.ONE);
            final BigInteger x = this.x;
            final BigInteger mod = x.shiftLeft(2).mod(this.q);
            final Random rnd = new Random();
            while (true) {
                final BigInteger val = new BigInteger(this.q.bitLength(), rnd);
                if (val.compareTo(this.q) < 0 && val.multiply(val).subtract(mod).modPow(shiftRight, this.q).equals(subtract)) {
                    final BigInteger[] lucasSequence = lucasSequence(this.q, val, x, add);
                    final BigInteger bigInteger = lucasSequence[0];
                    BigInteger add2 = lucasSequence[1];
                    if (add2.multiply(add2).mod(this.q).equals(mod)) {
                        if (add2.testBit(0)) {
                            add2 = add2.add(this.q);
                        }
                        return new Fp(this.q, add2.shiftRight(1));
                    }
                    if (!bigInteger.equals(ECConstants.ONE) && !bigInteger.equals(subtract)) {
                        return null;
                    }
                    continue;
                }
            }
        }
        
        private static BigInteger[] lucasSequence(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
            final int bitLength = bigInteger4.bitLength();
            final int lowestSetBit = bigInteger4.getLowestSetBit();
            BigInteger bigInteger5 = ECConstants.ONE;
            BigInteger bigInteger6 = ECConstants.TWO;
            BigInteger bigInteger7 = bigInteger2;
            BigInteger val = ECConstants.ONE;
            BigInteger bigInteger8 = ECConstants.ONE;
            for (int i = bitLength - 1; i >= lowestSetBit + 1; --i) {
                val = val.multiply(bigInteger8).mod(bigInteger);
                if (bigInteger4.testBit(i)) {
                    bigInteger8 = val.multiply(bigInteger3).mod(bigInteger);
                    bigInteger5 = bigInteger5.multiply(bigInteger7).mod(bigInteger);
                    bigInteger6 = bigInteger7.multiply(bigInteger6).subtract(bigInteger2.multiply(val)).mod(bigInteger);
                    bigInteger7 = bigInteger7.multiply(bigInteger7).subtract(bigInteger8.shiftLeft(1)).mod(bigInteger);
                }
                else {
                    bigInteger8 = val;
                    bigInteger5 = bigInteger5.multiply(bigInteger6).subtract(val).mod(bigInteger);
                    bigInteger7 = bigInteger7.multiply(bigInteger6).subtract(bigInteger2.multiply(val)).mod(bigInteger);
                    bigInteger6 = bigInteger6.multiply(bigInteger6).subtract(val.shiftLeft(1)).mod(bigInteger);
                }
            }
            final BigInteger mod = val.multiply(bigInteger8).mod(bigInteger);
            final BigInteger mod2 = mod.multiply(bigInteger3).mod(bigInteger);
            BigInteger bigInteger9 = bigInteger5.multiply(bigInteger6).subtract(mod).mod(bigInteger);
            BigInteger bigInteger10 = bigInteger7.multiply(bigInteger6).subtract(bigInteger2.multiply(mod)).mod(bigInteger);
            BigInteger val2 = mod.multiply(mod2).mod(bigInteger);
            for (int j = 1; j <= lowestSetBit; ++j) {
                bigInteger9 = bigInteger9.multiply(bigInteger10).mod(bigInteger);
                bigInteger10 = bigInteger10.multiply(bigInteger10).subtract(val2.shiftLeft(1)).mod(bigInteger);
                val2 = val2.multiply(val2).mod(bigInteger);
            }
            return new BigInteger[] { bigInteger9, bigInteger10 };
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Fp)) {
                return false;
            }
            final Fp fp = (Fp)o;
            return this.q.equals(fp.q) && this.x.equals(fp.x);
        }
        
        @Override
        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }
    }
}
