// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

public class TlsNullCipherSuite extends TlsCipherSuite
{
    @Override
    protected void init(final byte[] array, final byte[] array2, final byte[] array3) {
        throw new TlsRuntimeException("Sorry, init of TLS_NULL_WITH_NULL_NULL is forbidden");
    }
    
    @Override
    protected byte[] encodePlaintext(final short n, final byte[] array, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        System.arraycopy(array, n2, array2, 0, n3);
        return array2;
    }
    
    @Override
    protected byte[] decodeCiphertext(final short n, final byte[] array, final int n2, final int n3, final TlsProtocolHandler tlsProtocolHandler) {
        final byte[] array2 = new byte[n3];
        System.arraycopy(array, n2, array2, 0, n3);
        return array2;
    }
    
    @Override
    protected short getKeyExchangeAlgorithm() {
        return 0;
    }
}
