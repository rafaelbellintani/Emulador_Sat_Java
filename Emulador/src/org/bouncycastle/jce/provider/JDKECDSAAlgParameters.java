// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.spec.InvalidParameterSpecException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.DEROctetString;
import java.io.IOException;
import java.security.AlgorithmParametersSpi;

public abstract class JDKECDSAAlgParameters extends AlgorithmParametersSpi
{
    public static class SigAlgParameters extends JDKAlgorithmParameters
    {
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            return this.engineGetEncoded("ASN.1");
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (s == null) {
                return this.engineGetEncoded("ASN.1");
            }
            if (s.equals("ASN.1")) {
                return new DEROctetString(this.engineGetEncoded("RAW")).getEncoded();
            }
            return null;
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            throw new IOException("Unknown parameters format in IV parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "ECDSA Parameters";
        }
    }
}
