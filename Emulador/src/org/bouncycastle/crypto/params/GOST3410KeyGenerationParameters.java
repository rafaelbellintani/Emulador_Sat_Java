// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class GOST3410KeyGenerationParameters extends KeyGenerationParameters
{
    private GOST3410Parameters params;
    
    public GOST3410KeyGenerationParameters(final SecureRandom secureRandom, final GOST3410Parameters params) {
        super(secureRandom, params.getP().bitLength() - 1);
        this.params = params;
    }
    
    public GOST3410Parameters getParameters() {
        return this.params;
    }
}
