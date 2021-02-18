// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.util.Random;
import java.math.BigInteger;

public abstract class ECCurve
{
    ECFieldElement a;
    ECFieldElement b;
    
    public abstract int getFieldSize();
    
    public abstract ECFieldElement fromBigInteger(final BigInteger p0);
    
    public abstract ECPoint createPoint(final BigInteger p0, final BigInteger p1, final boolean p2);
    
    public abstract ECPoint decodePoint(final byte[] p0);
    
    public abstract ECPoint getInfinity();
    
    public ECFieldElement getA() {
        return this.a;
    }
    
    public ECFieldElement getB() {
        return this.b;
    }
    
    public static class F2m extends ECCurve
    {
        private int m;
        private int k1;
        private int k2;
        private int k3;
        private BigInteger n;
        private BigInteger h;
        private ECPoint.F2m infinity;
        private byte mu;
        private BigInteger[] si;
        
        public F2m(final int n, final int n2, final BigInteger bigInteger, final BigInteger bigInteger2) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, null, null);
        }
        
        public F2m(final int n, final int n2, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }
        
        public F2m(final int n, final int n2, final int n3, final int n4, final BigInteger bigInteger, final BigInteger bigInteger2) {
            this(n, n2, n3, n4, bigInteger, bigInteger2, null, null);
        }
        
        public F2m(final int m, final int k1, final int k2, final int k3, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger n, final BigInteger h) {
            this.mu = 0;
            this.si = null;
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            this.n = n;
            this.h = h;
            if (k1 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            }
            if (k2 == 0) {
                if (k3 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                }
            }
            else {
                if (k2 <= k1) {
                    throw new IllegalArgumentException("k2 must be > k1");
                }
                if (k3 <= k2) {
                    throw new IllegalArgumentException("k3 must be > k2");
                }
            }
            this.a = this.fromBigInteger(bigInteger);
            this.b = this.fromBigInteger(bigInteger2);
            this.infinity = new ECPoint.F2m(this, null, null);
        }
        
        @Override
        public int getFieldSize() {
            return this.m;
        }
        
        @Override
        public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
            return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, bigInteger);
        }
        
        @Override
        public ECPoint createPoint(final BigInteger bigInteger, final BigInteger bigInteger2, final boolean b) {
            return new ECPoint.F2m(this, this.fromBigInteger(bigInteger), this.fromBigInteger(bigInteger2), b);
        }
        
        @Override
        public ECPoint decodePoint(final byte[] array) {
            ECPoint ecPoint = null;
            switch (array[0]) {
                case 0: {
                    ecPoint = this.getInfinity();
                    break;
                }
                case 2:
                case 3: {
                    final byte[] array2 = new byte[array.length - 1];
                    System.arraycopy(array, 1, array2, 0, array2.length);
                    if (array[0] == 2) {
                        ecPoint = this.decompressPoint(array2, 0);
                        break;
                    }
                    ecPoint = this.decompressPoint(array2, 1);
                    break;
                }
                case 4:
                case 6:
                case 7: {
                    final byte[] magnitude = new byte[(array.length - 1) / 2];
                    final byte[] magnitude2 = new byte[(array.length - 1) / 2];
                    System.arraycopy(array, 1, magnitude, 0, magnitude.length);
                    System.arraycopy(array, magnitude.length + 1, magnitude2, 0, magnitude2.length);
                    ecPoint = new ECPoint.F2m(this, new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, magnitude)), new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, magnitude2)), false);
                    break;
                }
                default: {
                    throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(array[0], 16));
                }
            }
            return ecPoint;
        }
        
        @Override
        public ECPoint getInfinity() {
            return this.infinity;
        }
        
        public boolean isKoblitz() {
            return this.n != null && this.h != null && (this.a.toBigInteger().equals(ECConstants.ZERO) || this.a.toBigInteger().equals(ECConstants.ONE)) && this.b.toBigInteger().equals(ECConstants.ONE);
        }
        
        synchronized byte getMu() {
            if (this.mu == 0) {
                this.mu = Tnaf.getMu(this);
            }
            return this.mu;
        }
        
        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }
            return this.si;
        }
        
        private ECPoint decompressPoint(final byte[] magnitude, final int n) {
            final ECFieldElement.F2m f2m = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, magnitude));
            ECFieldElement ecFieldElement;
            if (f2m.toBigInteger().equals(ECConstants.ZERO)) {
                ecFieldElement = this.b;
                for (int i = 0; i < this.m - 1; ++i) {
                    ecFieldElement = ecFieldElement.square();
                }
            }
            else {
                ECFieldElement ecFieldElement2 = this.solveQuadradicEquation(f2m.add(this.a).add(this.b.multiply(f2m.square().invert())));
                if (ecFieldElement2 == null) {
                    throw new RuntimeException("Invalid point compression");
                }
                int n2 = 0;
                if (ecFieldElement2.toBigInteger().testBit(0)) {
                    n2 = 1;
                }
                if (n2 != n) {
                    ecFieldElement2 = ecFieldElement2.add(new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ONE));
                }
                ecFieldElement = f2m.multiply(ecFieldElement2);
            }
            return new ECPoint.F2m(this, f2m, ecFieldElement);
        }
        
        private ECFieldElement solveQuadradicEquation(final ECFieldElement ecFieldElement) {
            final ECFieldElement.F2m f2m = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ZERO);
            if (ecFieldElement.toBigInteger().equals(ECConstants.ZERO)) {
                return f2m;
            }
            final Random rnd = new Random();
            ECFieldElement add;
            do {
                final ECFieldElement.F2m f2m2 = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(this.m, rnd));
                add = f2m;
                ECFieldElement add2 = ecFieldElement;
                for (int i = 1; i <= this.m - 1; ++i) {
                    final ECFieldElement square = add2.square();
                    add = add.square().add(square.multiply(f2m2));
                    add2 = square.add(ecFieldElement);
                }
                if (!add2.toBigInteger().equals(ECConstants.ZERO)) {
                    return null;
                }
            } while (add.square().add(add).toBigInteger().equals(ECConstants.ZERO));
            return add;
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
            return this.m == f2m.m && this.k1 == f2m.k1 && this.k2 == f2m.k2 && this.k3 == f2m.k3 && this.a.equals(f2m.a) && this.b.equals(f2m.b);
        }
        
        @Override
        public int hashCode() {
            return this.a.hashCode() ^ this.b.hashCode() ^ this.m ^ this.k1 ^ this.k2 ^ this.k3;
        }
        
        public int getM() {
            return this.m;
        }
        
        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
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
        
        public BigInteger getN() {
            return this.n;
        }
        
        public BigInteger getH() {
            return this.h;
        }
    }
    
    public static class Fp extends ECCurve
    {
        BigInteger q;
        ECPoint.Fp infinity;
        
        public Fp(final BigInteger q, final BigInteger bigInteger, final BigInteger bigInteger2) {
            this.q = q;
            this.a = this.fromBigInteger(bigInteger);
            this.b = this.fromBigInteger(bigInteger2);
            this.infinity = new ECPoint.Fp(this, null, null);
        }
        
        public BigInteger getQ() {
            return this.q;
        }
        
        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }
        
        @Override
        public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
            return new ECFieldElement.Fp(this.q, bigInteger);
        }
        
        @Override
        public ECPoint createPoint(final BigInteger bigInteger, final BigInteger bigInteger2, final boolean b) {
            return new ECPoint.Fp(this, this.fromBigInteger(bigInteger), this.fromBigInteger(bigInteger2), b);
        }
        
        @Override
        public ECPoint decodePoint(final byte[] array) {
            ECPoint infinity = null;
            switch (array[0]) {
                case 0: {
                    infinity = this.getInfinity();
                    break;
                }
                case 2:
                case 3: {
                    final int n = array[0] & 0x1;
                    final byte[] magnitude = new byte[array.length - 1];
                    System.arraycopy(array, 1, magnitude, 0, magnitude.length);
                    final ECFieldElement.Fp fp = new ECFieldElement.Fp(this.q, new BigInteger(1, magnitude));
                    final ECFieldElement sqrt = fp.multiply(fp.square().add(this.a)).add(this.b).sqrt();
                    if (sqrt == null) {
                        throw new RuntimeException("Invalid point compression");
                    }
                    if ((sqrt.toBigInteger().testBit(0) ? 1 : 0) == n) {
                        infinity = new ECPoint.Fp(this, fp, sqrt, true);
                        break;
                    }
                    infinity = new ECPoint.Fp(this, fp, new ECFieldElement.Fp(this.q, this.q.subtract(sqrt.toBigInteger())), true);
                    break;
                }
                case 4:
                case 6:
                case 7: {
                    final byte[] magnitude2 = new byte[(array.length - 1) / 2];
                    final byte[] magnitude3 = new byte[(array.length - 1) / 2];
                    System.arraycopy(array, 1, magnitude2, 0, magnitude2.length);
                    System.arraycopy(array, magnitude2.length + 1, magnitude3, 0, magnitude3.length);
                    infinity = new ECPoint.Fp(this, new ECFieldElement.Fp(this.q, new BigInteger(1, magnitude2)), new ECFieldElement.Fp(this.q, new BigInteger(1, magnitude3)));
                    break;
                }
                default: {
                    throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(array[0], 16));
                }
            }
            return infinity;
        }
        
        @Override
        public ECPoint getInfinity() {
            return this.infinity;
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
            return this.q.equals(fp.q) && this.a.equals(fp.a) && this.b.equals(fp.b);
        }
        
        @Override
        public int hashCode() {
            return this.a.hashCode() ^ this.b.hashCode() ^ this.q.hashCode();
        }
    }
}
