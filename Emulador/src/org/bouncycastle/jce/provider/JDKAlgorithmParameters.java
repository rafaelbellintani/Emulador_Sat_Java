// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle.asn1.pkcs.RC2CBCParameter;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import java.security.spec.PSSParameterSpec;
import org.bouncycastle.asn1.pkcs.PKCS12PBEParams;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.RSAESOAEPparams;
import javax.crypto.spec.PSource;
import java.security.spec.MGF1ParameterSpec;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.asn1.x509.DSAParameter;
import java.security.spec.DSAParameterSpec;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import java.io.IOException;
import org.bouncycastle.asn1.pkcs.DHParameter;
import javax.crypto.spec.DHParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.AlgorithmParametersSpi;

public abstract class JDKAlgorithmParameters extends AlgorithmParametersSpi
{
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    @Override
    protected AlgorithmParameterSpec engineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz == null) {
            throw new NullPointerException("argument to getParameterSpec must not be null");
        }
        return this.localEngineGetParameterSpec(clazz);
    }
    
    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(final Class p0) throws InvalidParameterSpecException;
    
    public static class DH extends JDKAlgorithmParameters
    {
        DHParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final DHParameter dhParameter = new DHParameter(this.currentSpec.getP(), this.currentSpec.getG(), this.currentSpec.getL());
            try {
                return dhParameter.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding DHParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s)) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == DHParameterSpec.class) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to DH parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidParameterSpecException("DHParameterSpec required to initialise a Diffie-Hellman algorithm parameters object");
            }
            this.currentSpec = (DHParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final DHParameter dhParameter = new DHParameter((ASN1Sequence)ASN1Object.fromByteArray(array));
                if (dhParameter.getL() != null) {
                    this.currentSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG(), dhParameter.getL().intValue());
                }
                else {
                    this.currentSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG());
                }
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid DH Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid DH Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "Diffie-Hellman Parameters";
        }
    }
    
    public static class DSA extends JDKAlgorithmParameters
    {
        DSAParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final DSAParameter dsaParameter = new DSAParameter(this.currentSpec.getP(), this.currentSpec.getQ(), this.currentSpec.getG());
            try {
                return dsaParameter.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding DSAParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s)) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == DSAParameterSpec.class) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to DSA parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
                throw new InvalidParameterSpecException("DSAParameterSpec required to initialise a DSA algorithm parameters object");
            }
            this.currentSpec = (DSAParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final DSAParameter dsaParameter = new DSAParameter((ASN1Sequence)ASN1Object.fromByteArray(array));
                this.currentSpec = new DSAParameterSpec(dsaParameter.getP(), dsaParameter.getQ(), dsaParameter.getG());
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid DSA Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid DSA Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str) || str.equalsIgnoreCase("X.509")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "DSA Parameters";
        }
    }
    
    public static class ElGamal extends JDKAlgorithmParameters
    {
        ElGamalParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final ElGamalParameter elGamalParameter = new ElGamalParameter(this.currentSpec.getP(), this.currentSpec.getG());
            try {
                return elGamalParameter.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding ElGamalParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s) || s.equalsIgnoreCase("X.509")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == ElGamalParameterSpec.class) {
                return this.currentSpec;
            }
            if (clazz == DHParameterSpec.class) {
                return new DHParameterSpec(this.currentSpec.getP(), this.currentSpec.getG());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof ElGamalParameterSpec) && !(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidParameterSpecException("DHParameterSpec required to initialise a ElGamal algorithm parameters object");
            }
            if (algorithmParameterSpec instanceof ElGamalParameterSpec) {
                this.currentSpec = (ElGamalParameterSpec)algorithmParameterSpec;
            }
            else {
                final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
                this.currentSpec = new ElGamalParameterSpec(dhParameterSpec.getP(), dhParameterSpec.getG());
            }
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final ElGamalParameter elGamalParameter = new ElGamalParameter((ASN1Sequence)ASN1Object.fromByteArray(array));
                this.currentSpec = new ElGamalParameterSpec(elGamalParameter.getP(), elGamalParameter.getG());
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid ElGamal Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid ElGamal Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str) || str.equalsIgnoreCase("X.509")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "ElGamal Parameters";
        }
    }
    
    public static class GOST3410 extends JDKAlgorithmParameters
    {
        GOST3410ParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.currentSpec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.currentSpec.getDigestParamSetOID()), new DERObjectIdentifier(this.currentSpec.getEncryptionParamSetOID()));
            try {
                return gost3410PublicKeyAlgParameters.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding GOST3410Parameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s) || s.equalsIgnoreCase("X.509")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == GOST3410PublicKeyParameterSetSpec.class) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to GOST3410 parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof GOST3410ParameterSpec)) {
                throw new InvalidParameterSpecException("GOST3410ParameterSpec required to initialise a GOST3410 algorithm parameters object");
            }
            this.currentSpec = (GOST3410ParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                this.currentSpec = GOST3410ParameterSpec.fromPublicKeyAlg(new GOST3410PublicKeyAlgParameters((ASN1Sequence)ASN1Object.fromByteArray(array)));
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid GOST3410 Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid GOST3410 Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str) || str.equalsIgnoreCase("X.509")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "GOST3410 Parameters";
        }
    }
    
    public static class IES extends JDKAlgorithmParameters
    {
        IESParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            try {
                final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                asn1EncodableVector.add(new DEROctetString(this.currentSpec.getDerivationV()));
                asn1EncodableVector.add(new DEROctetString(this.currentSpec.getEncodingV()));
                asn1EncodableVector.add(new DERInteger(this.currentSpec.getMacKeySize()));
                return new DERSequence(asn1EncodableVector).getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding IESParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s) || s.equalsIgnoreCase("X.509")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == IESParameterSpec.class) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
                throw new InvalidParameterSpecException("IESParameterSpec required to initialise a IES algorithm parameters object");
            }
            this.currentSpec = (IESParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(array);
                this.currentSpec = new IESParameterSpec(((ASN1OctetString)asn1Sequence.getObjectAt(0)).getOctets(), ((ASN1OctetString)asn1Sequence.getObjectAt(0)).getOctets(), ((DERInteger)asn1Sequence.getObjectAt(0)).getValue().intValue());
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid IES Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid IES Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str) || str.equalsIgnoreCase("X.509")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "IES Parameters";
        }
    }
    
    public static class IVAlgorithmParameters extends JDKAlgorithmParameters
    {
        private byte[] iv;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            return this.engineGetEncoded("ASN.1");
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                return new DEROctetString(this.engineGetEncoded("RAW")).getEncoded();
            }
            if (s.equals("RAW")) {
                return Arrays.clone(this.iv);
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == IvParameterSpec.class) {
                return new IvParameterSpec(this.iv);
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
                throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
            }
            this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
        }
        
        @Override
        protected void engineInit(byte[] octets) throws IOException {
            if (octets.length % 8 != 0 && octets[0] == 4 && octets[1] == octets.length - 2) {
                octets = ((ASN1OctetString)ASN1Object.fromByteArray(octets)).getOctets();
            }
            this.iv = Arrays.clone(octets);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                try {
                    this.engineInit(((ASN1OctetString)ASN1Object.fromByteArray(array)).getOctets());
                }
                catch (Exception obj) {
                    throw new IOException("Exception decoding: " + obj);
                }
                return;
            }
            if (s.equals("RAW")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in IV parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "IV Parameters";
        }
    }
    
    public static class OAEP extends JDKAlgorithmParameters
    {
        OAEPParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final RSAESOAEPparams rsaesoaePparams = new RSAESOAEPparams(new AlgorithmIdentifier(JCEDigestUtil.getOID(this.currentSpec.getDigestAlgorithm()), new DERNull()), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(JCEDigestUtil.getOID(((MGF1ParameterSpec)this.currentSpec.getMGFParameters()).getDigestAlgorithm()), new DERNull())), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(((PSource.PSpecified)this.currentSpec.getPSource()).getValue())));
            try {
                return rsaesoaePparams.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding OAEPParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s) || s.equalsIgnoreCase("X.509")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == OAEPParameterSpec.class && this.currentSpec != null) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to OAEP parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof OAEPParameterSpec)) {
                throw new InvalidParameterSpecException("OAEPParameterSpec required to initialise an OAEP algorithm parameters object");
            }
            this.currentSpec = (OAEPParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final RSAESOAEPparams rsaesoaePparams = new RSAESOAEPparams((ASN1Sequence)ASN1Object.fromByteArray(array));
                this.currentSpec = new OAEPParameterSpec(rsaesoaePparams.getHashAlgorithm().getObjectId().getId(), rsaesoaePparams.getMaskGenAlgorithm().getObjectId().getId(), new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(rsaesoaePparams.getMaskGenAlgorithm().getParameters()).getObjectId().getId()), new PSource.PSpecified(ASN1OctetString.getInstance(rsaesoaePparams.getPSourceAlgorithm().getParameters()).getOctets()));
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (str.equalsIgnoreCase("X.509") || str.equalsIgnoreCase("ASN.1")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "OAEP Parameters";
        }
    }
    
    public static class PBKDF2 extends JDKAlgorithmParameters
    {
        PBKDF2Params params;
        
        @Override
        protected byte[] engineGetEncoded() {
            try {
                return this.params.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Oooops! " + ex.toString());
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s)) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getSalt(), this.params.getIterationCount().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
            }
            final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            this.params = new PBKDF2Params(pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.params = PBKDF2Params.getInstance(ASN1Object.fromByteArray(array));
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in PWRIKEK parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "PBKDF2 Parameters";
        }
    }
    
    public static class PKCS12PBE extends JDKAlgorithmParameters
    {
        PKCS12PBEParams params;
        
        @Override
        protected byte[] engineGetEncoded() {
            try {
                return this.params.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Oooops! " + ex.toString());
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s)) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getIV(), this.params.getIterations().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
            }
            final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            this.params = new PKCS12PBEParams(pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.params = PKCS12PBEParams.getInstance(ASN1Object.fromByteArray(array));
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in PKCS12 PBE parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "PKCS12 PBE Parameters";
        }
    }
    
    public static class PSS extends JDKAlgorithmParameters
    {
        PSSParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            final PSSParameterSpec currentSpec = this.currentSpec;
            return new RSASSAPSSparams(new AlgorithmIdentifier(JCEDigestUtil.getOID(currentSpec.getDigestAlgorithm()), new DERNull()), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(JCEDigestUtil.getOID(((MGF1ParameterSpec)currentSpec.getMGFParameters()).getDigestAlgorithm()), new DERNull())), new DERInteger(currentSpec.getSaltLength()), new DERInteger(currentSpec.getTrailerField())).getEncoded("DER");
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (s.equalsIgnoreCase("X.509") || s.equalsIgnoreCase("ASN.1")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PSSParameterSpec.class && this.currentSpec != null) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PSS parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
                throw new InvalidParameterSpecException("PSSParameterSpec required to initialise an PSS algorithm parameters object");
            }
            this.currentSpec = (PSSParameterSpec)algorithmParameterSpec;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final RSASSAPSSparams rsassapsSparams = new RSASSAPSSparams((ASN1Sequence)ASN1Object.fromByteArray(array));
                this.currentSpec = new PSSParameterSpec(rsassapsSparams.getHashAlgorithm().getObjectId().getId(), rsassapsSparams.getMaskGenAlgorithm().getObjectId().getId(), new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(rsassapsSparams.getMaskGenAlgorithm().getParameters()).getObjectId().getId()), rsassapsSparams.getSaltLength().getValue().intValue(), rsassapsSparams.getTrailerField().getValue().intValue());
            }
            catch (ClassCastException ex) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String str) throws IOException {
            if (this.isASN1FormatString(str) || str.equalsIgnoreCase("X.509")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameter format " + str);
        }
        
        @Override
        protected String engineToString() {
            return "PSS Parameters";
        }
    }
    
    public static class RC2AlgorithmParameters extends JDKAlgorithmParameters
    {
        private static final short[] table;
        private static final short[] ekb;
        private byte[] iv;
        private int parameterVersion;
        
        public RC2AlgorithmParameters() {
            this.parameterVersion = 58;
        }
        
        @Override
        protected byte[] engineGetEncoded() {
            return Arrays.clone(this.iv);
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                if (this.parameterVersion == -1) {
                    return new RC2CBCParameter(this.engineGetEncoded()).getEncoded();
                }
                return new RC2CBCParameter(this.parameterVersion, this.engineGetEncoded()).getEncoded();
            }
            else {
                if (s.equals("RAW")) {
                    return this.engineGetEncoded();
                }
                return null;
            }
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == RC2ParameterSpec.class && this.parameterVersion != -1) {
                if (this.parameterVersion < 256) {
                    return new RC2ParameterSpec(RC2AlgorithmParameters.ekb[this.parameterVersion], this.iv);
                }
                return new RC2ParameterSpec(this.parameterVersion, this.iv);
            }
            else {
                if (clazz == IvParameterSpec.class) {
                    return new IvParameterSpec(this.iv);
                }
                throw new InvalidParameterSpecException("unknown parameter spec passed to RC2 parameters object.");
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
            }
            else {
                if (!(algorithmParameterSpec instanceof RC2ParameterSpec)) {
                    throw new InvalidParameterSpecException("IvParameterSpec or RC2ParameterSpec required to initialise a RC2 parameters algorithm parameters object");
                }
                final int effectiveKeyBits = ((RC2ParameterSpec)algorithmParameterSpec).getEffectiveKeyBits();
                if (effectiveKeyBits != -1) {
                    if (effectiveKeyBits < 256) {
                        this.parameterVersion = RC2AlgorithmParameters.table[effectiveKeyBits];
                    }
                    else {
                        this.parameterVersion = effectiveKeyBits;
                    }
                }
                this.iv = ((RC2ParameterSpec)algorithmParameterSpec).getIV();
            }
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.iv = Arrays.clone(array);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                final RC2CBCParameter instance = RC2CBCParameter.getInstance(ASN1Object.fromByteArray(array));
                if (instance.getRC2ParameterVersion() != null) {
                    this.parameterVersion = instance.getRC2ParameterVersion().intValue();
                }
                this.iv = instance.getIV();
                return;
            }
            if (s.equals("RAW")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in IV parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "RC2 Parameters";
        }
        
        static {
            table = new short[] { 189, 86, 234, 242, 162, 241, 172, 42, 176, 147, 209, 156, 27, 51, 253, 208, 48, 4, 182, 220, 125, 223, 50, 75, 247, 203, 69, 155, 49, 187, 33, 90, 65, 159, 225, 217, 74, 77, 158, 218, 160, 104, 44, 195, 39, 95, 128, 54, 62, 238, 251, 149, 26, 254, 206, 168, 52, 169, 19, 240, 166, 63, 216, 12, 120, 36, 175, 35, 82, 193, 103, 23, 245, 102, 144, 231, 232, 7, 184, 96, 72, 230, 30, 83, 243, 146, 164, 114, 140, 8, 21, 110, 134, 0, 132, 250, 244, 127, 138, 66, 25, 246, 219, 205, 20, 141, 80, 18, 186, 60, 6, 78, 236, 179, 53, 17, 161, 136, 142, 43, 148, 153, 183, 113, 116, 211, 228, 191, 58, 222, 150, 14, 188, 10, 237, 119, 252, 55, 107, 3, 121, 137, 98, 198, 215, 192, 210, 124, 106, 139, 34, 163, 91, 5, 93, 2, 117, 213, 97, 227, 24, 143, 85, 81, 173, 31, 11, 94, 133, 229, 194, 87, 99, 202, 61, 108, 180, 197, 204, 112, 178, 145, 89, 13, 71, 32, 200, 79, 88, 224, 1, 226, 22, 56, 196, 111, 59, 15, 101, 70, 190, 126, 45, 123, 130, 249, 64, 181, 29, 115, 248, 235, 38, 199, 135, 151, 37, 84, 177, 40, 170, 152, 157, 165, 100, 109, 122, 212, 16, 129, 68, 239, 73, 214, 174, 46, 221, 118, 92, 47, 167, 28, 201, 9, 105, 154, 131, 207, 41, 57, 185, 233, 76, 255, 67, 171 };
            ekb = new short[] { 93, 190, 155, 139, 17, 153, 110, 77, 89, 243, 133, 166, 63, 183, 131, 197, 228, 115, 107, 58, 104, 90, 192, 71, 160, 100, 52, 12, 241, 208, 82, 165, 185, 30, 150, 67, 65, 216, 212, 44, 219, 248, 7, 119, 42, 202, 235, 239, 16, 28, 22, 13, 56, 114, 47, 137, 193, 249, 128, 196, 109, 174, 48, 61, 206, 32, 99, 254, 230, 26, 199, 184, 80, 232, 36, 23, 252, 37, 111, 187, 106, 163, 68, 83, 217, 162, 1, 171, 188, 182, 31, 152, 238, 154, 167, 45, 79, 158, 142, 172, 224, 198, 73, 70, 41, 244, 148, 138, 175, 225, 91, 195, 179, 123, 87, 209, 124, 156, 237, 135, 64, 140, 226, 203, 147, 20, 201, 97, 46, 229, 204, 246, 94, 168, 92, 214, 117, 141, 98, 149, 88, 105, 118, 161, 74, 181, 85, 9, 120, 51, 130, 215, 221, 121, 245, 27, 11, 222, 38, 33, 40, 116, 4, 151, 86, 223, 60, 240, 55, 57, 220, 255, 6, 164, 234, 66, 8, 218, 180, 113, 176, 207, 18, 122, 78, 250, 108, 29, 132, 0, 200, 127, 145, 69, 170, 43, 194, 177, 143, 213, 186, 242, 173, 25, 178, 103, 54, 247, 15, 10, 146, 125, 227, 157, 233, 144, 62, 35, 39, 102, 19, 236, 129, 21, 189, 34, 191, 159, 126, 169, 81, 75, 76, 251, 2, 211, 112, 134, 49, 231, 59, 5, 3, 84, 96, 72, 101, 24, 210, 205, 95, 50, 136, 14, 53, 253 };
        }
    }
}
