// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.digests.SHA1Digest;
import java.io.IOException;
import java.io.OutputStream;

public class TlsCipherSuiteManager
{
    private static final int TLS_RSA_WITH_3DES_EDE_CBC_SHA = 10;
    private static final int TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = 19;
    private static final int TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = 22;
    private static final int TLS_RSA_WITH_AES_128_CBC_SHA = 47;
    private static final int TLS_DHE_DSS_WITH_AES_128_CBC_SHA = 50;
    private static final int TLS_DHE_RSA_WITH_AES_128_CBC_SHA = 51;
    private static final int TLS_RSA_WITH_AES_256_CBC_SHA = 53;
    private static final int TLS_DHE_DSS_WITH_AES_256_CBC_SHA = 56;
    private static final int TLS_DHE_RSA_WITH_AES_256_CBC_SHA = 57;
    
    protected static void writeCipherSuites(final OutputStream outputStream) throws IOException {
        final int[] array = { 57, 56, 51, 50, 22, 19, 53, 47, 10 };
        TlsUtils.writeUint16(2 * array.length, outputStream);
        for (int i = 0; i < array.length; ++i) {
            TlsUtils.writeUint16(array[i], outputStream);
        }
    }
    
    protected static TlsCipherSuite getCipherSuite(final int n, final TlsProtocolHandler tlsProtocolHandler) throws IOException {
        switch (n) {
            case 10: {
                return createDESedeCipherSuite(24, (short)1);
            }
            case 19: {
                return createDESedeCipherSuite(24, (short)3);
            }
            case 22: {
                return createDESedeCipherSuite(24, (short)5);
            }
            case 47: {
                return createAESCipherSuite(16, (short)1);
            }
            case 50: {
                return createAESCipherSuite(16, (short)3);
            }
            case 51: {
                return createAESCipherSuite(16, (short)5);
            }
            case 53: {
                return createAESCipherSuite(32, (short)1);
            }
            case 56: {
                return createAESCipherSuite(32, (short)3);
            }
            case 57: {
                return createAESCipherSuite(32, (short)5);
            }
            default: {
                tlsProtocolHandler.failWithError((short)2, (short)40);
                return null;
            }
        }
    }
    
    private static TlsCipherSuite createAESCipherSuite(final int n, final short n2) {
        return new TlsBlockCipherCipherSuite(createAESCipher(), createAESCipher(), new SHA1Digest(), new SHA1Digest(), n, n2);
    }
    
    private static TlsCipherSuite createDESedeCipherSuite(final int n, final short n2) {
        return new TlsBlockCipherCipherSuite(createDESedeCipher(), createDESedeCipher(), new SHA1Digest(), new SHA1Digest(), n, n2);
    }
    
    private static CBCBlockCipher createAESCipher() {
        return new CBCBlockCipher(new AESFastEngine());
    }
    
    private static CBCBlockCipher createDESedeCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }
}
