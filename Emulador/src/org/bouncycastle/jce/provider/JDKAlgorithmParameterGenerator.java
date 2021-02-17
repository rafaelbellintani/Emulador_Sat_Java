// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.crypto.generators.GOST3410ParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.DSAParameters;
import java.security.spec.DSAParameterSpec;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.params.DHParameters;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.SecureRandom;
import java.security.AlgorithmParameterGeneratorSpi;

public abstract class JDKAlgorithmParameterGenerator extends AlgorithmParameterGeneratorSpi
{
    protected SecureRandom random;
    protected int strength;
    
    public JDKAlgorithmParameterGenerator() {
        this.strength = 1024;
    }
    
    @Override
    protected void engineInit(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
    
    public static class DES extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DES parameter generation.");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[8];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("DES", "BC");
                instance.init(new IvParameterSpec(array));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class DH extends JDKAlgorithmParameterGenerator
    {
        private int l;
        
        public DH() {
            this.l = 0;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
            }
            final DHGenParameterSpec dhGenParameterSpec = (DHGenParameterSpec)algorithmParameterSpec;
            this.strength = dhGenParameterSpec.getPrimeSize();
            this.l = dhGenParameterSpec.getExponentSize();
            this.random = random;
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final DHParametersGenerator dhParametersGenerator = new DHParametersGenerator();
            if (this.random != null) {
                dhParametersGenerator.init(this.strength, 20, this.random);
            }
            else {
                dhParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            final DHParameters generateParameters = dhParametersGenerator.generateParameters();
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("DH", "BC");
                instance.init(new DHParameterSpec(generateParameters.getP(), generateParameters.getG(), this.l));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class DSA extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final int strength, final SecureRandom random) {
            if (strength < 512 || strength > 1024 || strength % 64 != 0) {
                throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
            }
            this.strength = strength;
            this.random = random;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final DSAParametersGenerator dsaParametersGenerator = new DSAParametersGenerator();
            if (this.random != null) {
                dsaParametersGenerator.init(this.strength, 20, this.random);
            }
            else {
                dsaParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            final DSAParameters generateParameters = dsaParametersGenerator.generateParameters();
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("DSA", "BC");
                instance.init(new DSAParameterSpec(generateParameters.getP(), generateParameters.getQ(), generateParameters.getG()));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class ElGamal extends JDKAlgorithmParameterGenerator
    {
        private int l;
        
        public ElGamal() {
            this.l = 0;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
            }
            final DHGenParameterSpec dhGenParameterSpec = (DHGenParameterSpec)algorithmParameterSpec;
            this.strength = dhGenParameterSpec.getPrimeSize();
            this.l = dhGenParameterSpec.getExponentSize();
            this.random = random;
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
            if (this.random != null) {
                elGamalParametersGenerator.init(this.strength, 20, this.random);
            }
            else {
                elGamalParametersGenerator.init(this.strength, 20, new SecureRandom());
            }
            final ElGamalParameters generateParameters = elGamalParametersGenerator.generateParameters();
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("ElGamal", "BC");
                instance.init(new DHParameterSpec(generateParameters.getP(), generateParameters.getG(), this.l));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class GOST3410 extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for GOST3410 parameter generation.");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final GOST3410ParametersGenerator gost3410ParametersGenerator = new GOST3410ParametersGenerator();
            if (this.random != null) {
                gost3410ParametersGenerator.init(this.strength, 2, this.random);
            }
            else {
                gost3410ParametersGenerator.init(this.strength, 2, new SecureRandom());
            }
            final GOST3410Parameters generateParameters = gost3410ParametersGenerator.generateParameters();
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("GOST3410", "BC");
                instance.init(new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(generateParameters.getP(), generateParameters.getQ(), generateParameters.getA())));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class RC2 extends JDKAlgorithmParameterGenerator
    {
        RC2ParameterSpec spec;
        
        public RC2() {
            this.spec = null;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                this.spec = (RC2ParameterSpec)algorithmParameterSpec;
                return;
            }
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC2 parameter generation.");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            AlgorithmParameters algorithmParameters;
            if (this.spec == null) {
                final byte[] array = new byte[8];
                if (this.random == null) {
                    this.random = new SecureRandom();
                }
                this.random.nextBytes(array);
                try {
                    algorithmParameters = AlgorithmParameters.getInstance("RC2", "BC");
                    algorithmParameters.init(new IvParameterSpec(array));
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
            else {
                try {
                    algorithmParameters = AlgorithmParameters.getInstance("RC2", "BC");
                    algorithmParameters.init(this.spec);
                }
                catch (Exception ex2) {
                    throw new RuntimeException(ex2.getMessage());
                }
            }
            return algorithmParameters;
        }
    }
}
