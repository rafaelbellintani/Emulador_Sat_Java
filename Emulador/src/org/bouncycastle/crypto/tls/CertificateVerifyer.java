// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import org.bouncycastle.asn1.x509.X509CertificateStructure;

public interface CertificateVerifyer
{
    boolean isValid(final X509CertificateStructure[] p0);
}
