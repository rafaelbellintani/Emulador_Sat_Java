// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.params.GOST3410ValidationParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import java.util.Random;
import java.math.BigInteger;
import java.security.SecureRandom;

public class GOST3410ParametersGenerator
{
    private int size;
    private int typeproc;
    private SecureRandom init_random;
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    public void init(final int size, final int typeproc, final SecureRandom init_random) {
        this.size = size;
        this.typeproc = typeproc;
        this.init_random = init_random;
    }
    
    private int procedure_A(int i, int j, final BigInteger[] array, final int n) {
        while (i < 0 || i > 65536) {
            i = this.init_random.nextInt() / 32768;
        }
        while (j < 0 || j > 65536 || j / 2 == 0) {
            j = this.init_random.nextInt() / 32768 + 1;
        }
        final BigInteger val = new BigInteger(Integer.toString(j));
        final BigInteger val2 = new BigInteger("19381");
        BigInteger[] array2 = { new BigInteger(Integer.toString(i)) };
        int[] array3 = { n };
        int n2 = 0;
        for (int n3 = 0; array3[n3] >= 17; ++n3) {
            final int[] array4 = new int[array3.length + 1];
            System.arraycopy(array3, 0, array4, 0, array3.length);
            array3 = new int[array4.length];
            System.arraycopy(array4, 0, array3, 0, array4.length);
            array3[n3 + 1] = array3[n3] / 2;
            n2 = n3 + 1;
        }
        final BigInteger[] array5 = new BigInteger[n2 + 1];
        array5[n2] = new BigInteger("8003", 16);
        int n4 = n2 - 1;
        for (int k = 0; k < n2; ++k) {
            final int n5 = array3[n4] / 16;
        Block_10:
            while (true) {
                final BigInteger[] array6 = new BigInteger[array2.length];
                System.arraycopy(array2, 0, array6, 0, array2.length);
                array2 = new BigInteger[n5 + 1];
                System.arraycopy(array6, 0, array2, 0, array6.length);
                for (int l = 0; l < n5; ++l) {
                    array2[l + 1] = array2[l].multiply(val2).add(val).mod(GOST3410ParametersGenerator.TWO.pow(16));
                }
                BigInteger add = new BigInteger("0");
                for (int n6 = 0; n6 < n5; ++n6) {
                    add = add.add(array2[n6].multiply(GOST3410ParametersGenerator.TWO.pow(16 * n6)));
                }
                array2[0] = array2[n5];
                BigInteger bigInteger = GOST3410ParametersGenerator.TWO.pow(array3[n4] - 1).divide(array5[n4 + 1]).add(GOST3410ParametersGenerator.TWO.pow(array3[n4] - 1).multiply(add).divide(array5[n4 + 1].multiply(GOST3410ParametersGenerator.TWO.pow(16 * n5))));
                if (bigInteger.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                    bigInteger = bigInteger.add(GOST3410ParametersGenerator.ONE);
                }
                int n7 = 0;
                while (true) {
                    array5[n4] = array5[n4 + 1].multiply(bigInteger.add(BigInteger.valueOf(n7))).add(GOST3410ParametersGenerator.ONE);
                    if (array5[n4].compareTo(GOST3410ParametersGenerator.TWO.pow(array3[n4])) == 1) {
                        break;
                    }
                    if (GOST3410ParametersGenerator.TWO.modPow(array5[n4 + 1].multiply(bigInteger.add(BigInteger.valueOf(n7))), array5[n4]).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger.add(BigInteger.valueOf(n7)), array5[n4]).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                        break Block_10;
                    }
                    n7 += 2;
                }
            }
            --n4;
            if (n4 < 0) {
                array[0] = array5[0];
                array[1] = array5[1];
                return array2[0].intValue();
            }
        }
        return array2[0].intValue();
    }
    
    private long procedure_Aa(long i, long j, final BigInteger[] array, final int n) {
        while (i < 0L || i > 4294967296L) {
            i = this.init_random.nextInt() * 2;
        }
        while (j < 0L || j > 4294967296L || j / 2L == 0L) {
            j = this.init_random.nextInt() * 2 + 1;
        }
        final BigInteger val = new BigInteger(Long.toString(j));
        final BigInteger val2 = new BigInteger("97781173");
        BigInteger[] array2 = { new BigInteger(Long.toString(i)) };
        int[] array3 = { n };
        int n2 = 0;
        for (int n3 = 0; array3[n3] >= 33; ++n3) {
            final int[] array4 = new int[array3.length + 1];
            System.arraycopy(array3, 0, array4, 0, array3.length);
            array3 = new int[array4.length];
            System.arraycopy(array4, 0, array3, 0, array4.length);
            array3[n3 + 1] = array3[n3] / 2;
            n2 = n3 + 1;
        }
        final BigInteger[] array5 = new BigInteger[n2 + 1];
        array5[n2] = new BigInteger("8000000B", 16);
        int n4 = n2 - 1;
        for (int k = 0; k < n2; ++k) {
            final int n5 = array3[n4] / 32;
        Block_10:
            while (true) {
                final BigInteger[] array6 = new BigInteger[array2.length];
                System.arraycopy(array2, 0, array6, 0, array2.length);
                array2 = new BigInteger[n5 + 1];
                System.arraycopy(array6, 0, array2, 0, array6.length);
                for (int l = 0; l < n5; ++l) {
                    array2[l + 1] = array2[l].multiply(val2).add(val).mod(GOST3410ParametersGenerator.TWO.pow(32));
                }
                BigInteger add = new BigInteger("0");
                for (int n6 = 0; n6 < n5; ++n6) {
                    add = add.add(array2[n6].multiply(GOST3410ParametersGenerator.TWO.pow(32 * n6)));
                }
                array2[0] = array2[n5];
                BigInteger bigInteger = GOST3410ParametersGenerator.TWO.pow(array3[n4] - 1).divide(array5[n4 + 1]).add(GOST3410ParametersGenerator.TWO.pow(array3[n4] - 1).multiply(add).divide(array5[n4 + 1].multiply(GOST3410ParametersGenerator.TWO.pow(32 * n5))));
                if (bigInteger.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                    bigInteger = bigInteger.add(GOST3410ParametersGenerator.ONE);
                }
                int n7 = 0;
                while (true) {
                    array5[n4] = array5[n4 + 1].multiply(bigInteger.add(BigInteger.valueOf(n7))).add(GOST3410ParametersGenerator.ONE);
                    if (array5[n4].compareTo(GOST3410ParametersGenerator.TWO.pow(array3[n4])) == 1) {
                        break;
                    }
                    if (GOST3410ParametersGenerator.TWO.modPow(array5[n4 + 1].multiply(bigInteger.add(BigInteger.valueOf(n7))), array5[n4]).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger.add(BigInteger.valueOf(n7)), array5[n4]).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                        break Block_10;
                    }
                    n7 += 2;
                }
            }
            --n4;
            if (n4 < 0) {
                array[0] = array5[0];
                array[1] = array5[1];
                return array2[0].longValue();
            }
        }
        return array2[0].longValue();
    }
    
    private void procedure_B(int i, int j, final BigInteger[] array) {
        while (i < 0 || i > 65536) {
            i = this.init_random.nextInt() / 32768;
        }
        while (j < 0 || j > 65536 || j / 2 == 0) {
            j = this.init_random.nextInt() / 32768 + 1;
        }
        final BigInteger[] array2 = new BigInteger[2];
        final BigInteger val = new BigInteger(Integer.toString(j));
        final BigInteger val2 = new BigInteger("19381");
        i = this.procedure_A(i, j, array2, 256);
        final BigInteger bigInteger = array2[0];
        i = this.procedure_A(i, j, array2, 512);
        final BigInteger bigInteger2 = array2[0];
        final BigInteger[] array3 = new BigInteger[65];
        array3[0] = new BigInteger(Integer.toString(i));
        final int exponent = 1024;
        BigInteger add2 = null;
    Block_8:
        while (true) {
            for (int k = 0; k < 64; ++k) {
                array3[k + 1] = array3[k].multiply(val2).add(val).mod(GOST3410ParametersGenerator.TWO.pow(16));
            }
            BigInteger add = new BigInteger("0");
            for (int l = 0; l < 64; ++l) {
                add = add.add(array3[l].multiply(GOST3410ParametersGenerator.TWO.pow(16 * l)));
            }
            array3[0] = array3[64];
            BigInteger bigInteger3 = GOST3410ParametersGenerator.TWO.pow(exponent - 1).divide(bigInteger.multiply(bigInteger2)).add(GOST3410ParametersGenerator.TWO.pow(exponent - 1).multiply(add).divide(bigInteger.multiply(bigInteger2).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (bigInteger3.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                bigInteger3 = bigInteger3.add(GOST3410ParametersGenerator.ONE);
            }
            int n = 0;
            while (true) {
                add2 = bigInteger.multiply(bigInteger2).multiply(bigInteger3.add(BigInteger.valueOf(n))).add(GOST3410ParametersGenerator.ONE);
                if (add2.compareTo(GOST3410ParametersGenerator.TWO.pow(exponent)) == 1) {
                    break;
                }
                if (GOST3410ParametersGenerator.TWO.modPow(bigInteger.multiply(bigInteger2).multiply(bigInteger3.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger.multiply(bigInteger3.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break Block_8;
                }
                n += 2;
            }
        }
        array[0] = add2;
        array[1] = bigInteger;
    }
    
    private void procedure_Bb(long i, long j, final BigInteger[] array) {
        while (i < 0L || i > 4294967296L) {
            i = this.init_random.nextInt() * 2;
        }
        while (j < 0L || j > 4294967296L || j / 2L == 0L) {
            j = this.init_random.nextInt() * 2 + 1;
        }
        final BigInteger[] array2 = new BigInteger[2];
        final BigInteger val = new BigInteger(Long.toString(j));
        final BigInteger val2 = new BigInteger("97781173");
        i = this.procedure_Aa(i, j, array2, 256);
        final BigInteger bigInteger = array2[0];
        i = this.procedure_Aa(i, j, array2, 512);
        final BigInteger bigInteger2 = array2[0];
        final BigInteger[] array3 = new BigInteger[33];
        array3[0] = new BigInteger(Long.toString(i));
        final int exponent = 1024;
        BigInteger add2 = null;
    Block_8:
        while (true) {
            for (int k = 0; k < 32; ++k) {
                array3[k + 1] = array3[k].multiply(val2).add(val).mod(GOST3410ParametersGenerator.TWO.pow(32));
            }
            BigInteger add = new BigInteger("0");
            for (int l = 0; l < 32; ++l) {
                add = add.add(array3[l].multiply(GOST3410ParametersGenerator.TWO.pow(32 * l)));
            }
            array3[0] = array3[32];
            BigInteger bigInteger3 = GOST3410ParametersGenerator.TWO.pow(exponent - 1).divide(bigInteger.multiply(bigInteger2)).add(GOST3410ParametersGenerator.TWO.pow(exponent - 1).multiply(add).divide(bigInteger.multiply(bigInteger2).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (bigInteger3.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                bigInteger3 = bigInteger3.add(GOST3410ParametersGenerator.ONE);
            }
            int n = 0;
            while (true) {
                add2 = bigInteger.multiply(bigInteger2).multiply(bigInteger3.add(BigInteger.valueOf(n))).add(GOST3410ParametersGenerator.ONE);
                if (add2.compareTo(GOST3410ParametersGenerator.TWO.pow(exponent)) == 1) {
                    break;
                }
                if (GOST3410ParametersGenerator.TWO.modPow(bigInteger.multiply(bigInteger2).multiply(bigInteger3.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger.multiply(bigInteger3.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break Block_8;
                }
                n += 2;
            }
        }
        array[0] = add2;
        array[1] = bigInteger;
    }
    
    private BigInteger procedure_C(final BigInteger m, final BigInteger val) {
        final BigInteger subtract = m.subtract(GOST3410ParametersGenerator.ONE);
        final BigInteger divide = subtract.divide(val);
        final int bitLength = m.bitLength();
        BigInteger modPow;
        while (true) {
            final BigInteger bigInteger = new BigInteger(bitLength, this.init_random);
            if (bigInteger.compareTo(GOST3410ParametersGenerator.ONE) > 0 && bigInteger.compareTo(subtract) < 0) {
                modPow = bigInteger.modPow(divide, m);
                if (modPow.compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break;
                }
                continue;
            }
        }
        return modPow;
    }
    
    public GOST3410Parameters generateParameters() {
        final BigInteger[] array = new BigInteger[2];
        if (this.typeproc == 1) {
            final int nextInt = this.init_random.nextInt();
            final int nextInt2 = this.init_random.nextInt();
            switch (this.size) {
                case 512: {
                    this.procedure_A(nextInt, nextInt2, array, 512);
                    break;
                }
                case 1024: {
                    this.procedure_B(nextInt, nextInt2, array);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Ooops! key size 512 or 1024 bit.");
                }
            }
            final BigInteger bigInteger = array[0];
            final BigInteger bigInteger2 = array[1];
            return new GOST3410Parameters(bigInteger, bigInteger2, this.procedure_C(bigInteger, bigInteger2), new GOST3410ValidationParameters(nextInt, nextInt2));
        }
        final long nextLong = this.init_random.nextLong();
        final long nextLong2 = this.init_random.nextLong();
        switch (this.size) {
            case 512: {
                this.procedure_Aa(nextLong, nextLong2, array, 512);
                break;
            }
            case 1024: {
                this.procedure_Bb(nextLong, nextLong2, array);
                break;
            }
            default: {
                throw new IllegalStateException("Ooops! key size 512 or 1024 bit.");
            }
        }
        final BigInteger bigInteger3 = array[0];
        final BigInteger bigInteger4 = array[1];
        return new GOST3410Parameters(bigInteger3, bigInteger4, this.procedure_C(bigInteger3, bigInteger4), new GOST3410ValidationParameters(nextLong, nextLong2));
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
}
