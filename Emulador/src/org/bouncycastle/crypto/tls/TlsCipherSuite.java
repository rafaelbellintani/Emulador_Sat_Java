// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;

public abstract class TlsCipherSuite
{
    protected static final short KE_RSA = 1;
    protected static final short KE_RSA_EXPORT = 2;
    protected static final short KE_DHE_DSS = 3;
    protected static final short KE_DHE_DSS_EXPORT = 4;
    protected static final short KE_DHE_RSA = 5;
    protected static final short KE_DHE_RSA_EXPORT = 6;
    protected static final short KE_DH_DSS = 7;
    protected static final short KE_DH_RSA = 8;
    protected static final short KE_DH_anon = 9;
    protected static final short KE_SRP = 10;
    protected static final short KE_SRP_RSA = 11;
    protected static final short KE_SRP_DSS = 12;
    
    protected abstract void init(final byte[] p0, final byte[] p1, final byte[] p2);
    
    protected abstract byte[] encodePlaintext(final short p0, final byte[] p1, final int p2, final int p3);
    
    protected abstract byte[] decodeCiphertext(final short p0, final byte[] p1, final int p2, final int p3, final TlsProtocolHandler p4) throws IOException;
    
    protected abstract short getKeyExchangeAlgorithm();
}
