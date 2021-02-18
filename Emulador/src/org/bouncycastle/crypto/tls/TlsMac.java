// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;

public class TlsMac
{
    private long seqNo;
    private HMac mac;
    
    protected TlsMac(final Digest digest, final byte[] array, final int n, final int n2) {
        (this.mac = new HMac(digest)).init(new KeyParameter(array, n, n2));
        this.seqNo = 0L;
    }
    
    protected int getSize() {
        return this.mac.getMacSize();
    }
    
    protected byte[] calculateMac(final short n, final byte[] b, final int off, final int len) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            TlsUtils.writeUint64(this.seqNo++, byteArrayOutputStream);
            TlsUtils.writeUint8(n, byteArrayOutputStream);
            TlsUtils.writeVersion(byteArrayOutputStream);
            TlsUtils.writeUint16(len, byteArrayOutputStream);
            byteArrayOutputStream.write(b, off, len);
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            this.mac.update(byteArray, 0, byteArray.length);
            final byte[] array = new byte[this.mac.getMacSize()];
            this.mac.doFinal(array, 0);
            this.mac.reset();
            return array;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Internal error during mac calculation");
        }
    }
}
