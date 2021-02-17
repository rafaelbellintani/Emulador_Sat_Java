// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import java.util.Vector;
import java.io.InputStream;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

public class Certificate
{
    protected X509CertificateStructure[] certs;
    
    protected static Certificate parse(final InputStream inputStream) throws IOException {
        int i = TlsUtils.readUint24(inputStream);
        final Vector<X509CertificateStructure> vector = new Vector<X509CertificateStructure>();
        while (i > 0) {
            final int uint24 = TlsUtils.readUint24(inputStream);
            i -= 3 + uint24;
            final byte[] buf = new byte[uint24];
            TlsUtils.readFully(buf, inputStream);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            vector.addElement(X509CertificateStructure.getInstance(new ASN1InputStream(byteArrayInputStream).readObject()));
            if (byteArrayInputStream.available() > 0) {
                throw new IllegalArgumentException("Sorry, there is garbage data left after the certificate");
            }
        }
        final X509CertificateStructure[] array = new X509CertificateStructure[vector.size()];
        for (int j = 0; j < vector.size(); ++j) {
            array[j] = vector.elementAt(j);
        }
        return new Certificate(array);
    }
    
    private Certificate(final X509CertificateStructure[] certs) {
        this.certs = certs;
    }
    
    public X509CertificateStructure[] getCerts() {
        final X509CertificateStructure[] array = new X509CertificateStructure[this.certs.length];
        System.arraycopy(this.certs, 0, array, 0, this.certs.length);
        return array;
    }
}
