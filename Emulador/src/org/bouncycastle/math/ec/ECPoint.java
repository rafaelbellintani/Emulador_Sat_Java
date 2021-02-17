// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.asn1.x9.X9IntegerConverter;

public abstract class ECPoint
{
    ECCurve curve;
    ECFieldElement x;
    ECFieldElement y;
    protected boolean withCompression;
    protected ECMultiplier multiplier;
    protected PreCompInfo preCompInfo;
    private static X9IntegerConverter converter;
    
    protected ECPoint(final ECCurve curve, final ECFieldElement x, final ECFieldElement y) {
        this.multiplier = null;
        this.preCompInfo = null;
        this.curve = curve;
        this.x = x;
        this.y = y;
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public ECFieldElement getX() {
        return this.x;
    }
    
    public ECFieldElement getY() {
        return this.y;
    }
    
    public boolean isInfinity() {
        return this.x == null && this.y == null;
    }
    
    public boolean isCompressed() {
        return this.withCompression;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ECPoint)) {
            return false;
        }
        final ECPoint ecPoint = (ECPoint)o;
        if (this.isInfinity()) {
            return ecPoint.isInfinity();
        }
        return this.x.equals(ecPoint.x) && this.y.equals(ecPoint.y);
    }
    
    @Override
    public int hashCode() {
        if (this.isInfinity()) {
            return 0;
        }
        return this.x.hashCode() ^ this.y.hashCode();
    }
    
    void setPreCompInfo(final PreCompInfo preCompInfo) {
        this.preCompInfo = preCompInfo;
    }
    
    public abstract byte[] getEncoded();
    
    public abstract ECPoint add(final ECPoint p0);
    
    public abstract ECPoint subtract(final ECPoint p0);
    
    public abstract ECPoint negate();
    
    public abstract ECPoint twice();
    
    synchronized void assertECMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = new FpNafMultiplier();
        }
    }
    
    public ECPoint multiply(final BigInteger bigInteger) {
        if (this.isInfinity()) {
            return this;
        }
        if (bigInteger.signum() == 0) {
            return this.curve.getInfinity();
        }
        this.assertECMultiplier();
        return this.multiplier.multiply(this, bigInteger, this.preCompInfo);
    }
    
    static {
        ECPoint.converter = new X9IntegerConverter();
    }
    
    public static class F2m extends ECPoint
    {
        public F2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            this(ecCurve, ecFieldElement, ecFieldElement2, false);
        }
        
        public F2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
            if ((ecFieldElement != null && ecFieldElement2 == null) || (ecFieldElement == null && ecFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (ecFieldElement != null) {
                ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (ecCurve != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = withCompression;
        }
        
        @Deprecated
        public F2m(final ECCurve ecCurve) {
            super(ecCurve, null, null);
        }
        
        @Override
        public byte[] getEncoded() {
            if (this.isInfinity()) {
                return new byte[1];
            }
            final int byteLength = ECPoint.converter.getByteLength(this.x);
            final byte[] integerToBytes = ECPoint.converter.integerToBytes(this.getX().toBigInteger(), byteLength);
            byte[] array;
            if (this.withCompression) {
                array = new byte[byteLength + 1];
                array[0] = 2;
                if (!this.getX().toBigInteger().equals(ECConstants.ZERO) && this.getY().multiply(this.getX().invert()).toBigInteger().testBit(0)) {
                    array[0] = 3;
                }
                System.arraycopy(integerToBytes, 0, array, 1, byteLength);
            }
            else {
                final byte[] integerToBytes2 = ECPoint.converter.integerToBytes(this.getY().toBigInteger(), byteLength);
                array = new byte[byteLength + byteLength + 1];
                array[0] = 4;
                System.arraycopy(integerToBytes, 0, array, 1, byteLength);
                System.arraycopy(integerToBytes2, 0, array, byteLength + 1, byteLength);
            }
            return array;
        }
        
        private static void checkPoints(final ECPoint ecPoint, final ECPoint ecPoint2) {
            if (!ecPoint.curve.equals(ecPoint2.curve)) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }
        
        @Override
        public ECPoint add(final ECPoint ecPoint) {
            checkPoints(this, ecPoint);
            return this.addSimple((F2m)ecPoint);
        }
        
        public F2m addSimple(final F2m f2m) {
            if (this.isInfinity()) {
                return f2m;
            }
            if (f2m.isInfinity()) {
                return this;
            }
            final ECFieldElement.F2m obj = (ECFieldElement.F2m)f2m.getX();
            final ECFieldElement.F2m obj2 = (ECFieldElement.F2m)f2m.getY();
            if (!this.x.equals(obj)) {
                final ECFieldElement.F2m f2m2 = (ECFieldElement.F2m)this.y.add(obj2).divide(this.x.add(obj));
                final ECFieldElement.F2m f2m3 = (ECFieldElement.F2m)f2m2.square().add(f2m2).add(this.x).add(obj).add(this.curve.getA());
                return new F2m(this.curve, f2m3, f2m2.multiply(this.x.add(f2m3)).add(f2m3).add(this.y), this.withCompression);
            }
            if (this.y.equals(obj2)) {
                return (F2m)this.twice();
            }
            return (F2m)this.curve.getInfinity();
        }
        
        @Override
        public ECPoint subtract(final ECPoint ecPoint) {
            checkPoints(this, ecPoint);
            return this.subtractSimple((F2m)ecPoint);
        }
        
        public F2m subtractSimple(final F2m f2m) {
            if (f2m.isInfinity()) {
                return this;
            }
            return this.addSimple((F2m)f2m.negate());
        }
        
        @Override
        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            }
            if (this.x.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            final ECFieldElement.F2m f2m = (ECFieldElement.F2m)this.x.add(this.y.divide(this.x));
            final ECFieldElement.F2m f2m2 = (ECFieldElement.F2m)f2m.square().add(f2m).add(this.curve.getA());
            return new F2m(this.curve, f2m2, this.x.square().add(f2m2.multiply(f2m.add(this.curve.fromBigInteger(ECConstants.ONE)))), this.withCompression);
        }
        
        @Override
        public ECPoint negate() {
            return new F2m(this.curve, this.getX(), this.getY().add(this.getX()), this.withCompression);
        }
    }
    
    public static class Fp extends ECPoint
    {
        public Fp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            this(ecCurve, ecFieldElement, ecFieldElement2, false);
        }
        
        public Fp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
            if ((ecFieldElement != null && ecFieldElement2 == null) || (ecFieldElement == null && ecFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = withCompression;
        }
        
        @Override
        public byte[] getEncoded() {
            if (this.isInfinity()) {
                return new byte[1];
            }
            final int byteLength = ECPoint.converter.getByteLength(this.x);
            if (this.withCompression) {
                byte b;
                if (this.getY().toBigInteger().testBit(0)) {
                    b = 3;
                }
                else {
                    b = 2;
                }
                final byte[] integerToBytes = ECPoint.converter.integerToBytes(this.getX().toBigInteger(), byteLength);
                final byte[] array = new byte[integerToBytes.length + 1];
                array[0] = b;
                System.arraycopy(integerToBytes, 0, array, 1, integerToBytes.length);
                return array;
            }
            final byte[] integerToBytes2 = ECPoint.converter.integerToBytes(this.getX().toBigInteger(), byteLength);
            final byte[] integerToBytes3 = ECPoint.converter.integerToBytes(this.getY().toBigInteger(), byteLength);
            final byte[] array2 = new byte[integerToBytes2.length + integerToBytes3.length + 1];
            array2[0] = 4;
            System.arraycopy(integerToBytes2, 0, array2, 1, integerToBytes2.length);
            System.arraycopy(integerToBytes3, 0, array2, integerToBytes2.length + 1, integerToBytes3.length);
            return array2;
        }
        
        @Override
        public ECPoint add(final ECPoint ecPoint) {
            if (this.isInfinity()) {
                return ecPoint;
            }
            if (ecPoint.isInfinity()) {
                return this;
            }
            if (!this.x.equals(ecPoint.x)) {
                final ECFieldElement divide = ecPoint.y.subtract(this.y).divide(ecPoint.x.subtract(this.x));
                final ECFieldElement subtract = divide.square().subtract(this.x).subtract(ecPoint.x);
                return new Fp(this.curve, subtract, divide.multiply(this.x.subtract(subtract)).subtract(this.y));
            }
            if (this.y.equals(ecPoint.y)) {
                return this.twice();
            }
            return this.curve.getInfinity();
        }
        
        @Override
        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            }
            if (this.y.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            final ECFieldElement fromBigInteger = this.curve.fromBigInteger(BigInteger.valueOf(2L));
            final ECFieldElement divide = this.x.square().multiply(this.curve.fromBigInteger(BigInteger.valueOf(3L))).add(this.curve.a).divide(this.y.multiply(fromBigInteger));
            final ECFieldElement subtract = divide.square().subtract(this.x.multiply(fromBigInteger));
            return new Fp(this.curve, subtract, divide.multiply(this.x.subtract(subtract)).subtract(this.y), this.withCompression);
        }
        
        @Override
        public ECPoint subtract(final ECPoint ecPoint) {
            if (ecPoint.isInfinity()) {
                return this;
            }
            return this.add(ecPoint.negate());
        }
        
        @Override
        public ECPoint negate() {
            return new Fp(this.curve, this.x, this.y.negate(), this.withCompression);
        }
    }
}
