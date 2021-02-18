// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.asn1.x509.X509CertificateStructure;

public class AlwaysValidVerifyer implements CertificateVerifyer
{
    @Override
    public boolean isValid(final X509CertificateStructure[] array) {
        return true;
    }
}
