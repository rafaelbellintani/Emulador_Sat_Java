// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Encodable;

public class SubjectKeyIdentifier extends ASN1Encodable
{
    private byte[] keyidentifier;
    
    public static SubjectKeyIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1OctetString.getInstance(asn1TaggedObject, b));
    }
    
    public static SubjectKeyIdentifier getInstance(final Object o) {
        if (o instanceof SubjectKeyIdentifier) {
            return (SubjectKeyIdentifier)o;
        }
        if (o instanceof SubjectPublicKeyInfo) {
            return new SubjectKeyIdentifier((SubjectPublicKeyInfo)o);
        }
        if (o instanceof ASN1OctetString) {
            return new SubjectKeyIdentifier((ASN1OctetString)o);
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        throw new IllegalArgumentException("Invalid SubjectKeyIdentifier: " + o.getClass().getName());
    }
    
    public SubjectKeyIdentifier(final byte[] keyidentifier) {
        this.keyidentifier = keyidentifier;
    }
    
    public SubjectKeyIdentifier(final ASN1OctetString asn1OctetString) {
        this.keyidentifier = asn1OctetString.getOctets();
    }
    
    public SubjectKeyIdentifier(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.keyidentifier = getDigest(subjectPublicKeyInfo);
    }
    
    public byte[] getKeyIdentifier() {
        return this.keyidentifier;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DEROctetString(this.keyidentifier);
    }
    
    public static SubjectKeyIdentifier createSHA1KeyIdentifier(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        return new SubjectKeyIdentifier(subjectPublicKeyInfo);
    }
    
    public static SubjectKeyIdentifier createTruncatedSHA1KeyIdentifier(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final byte[] digest = getDigest(subjectPublicKeyInfo);
        final byte[] array = new byte[8];
        System.arraycopy(digest, digest.length - 8, array, 0, array.length);
        final byte[] array2 = array;
        final int n = 0;
        array2[n] &= 0xF;
        final byte[] array3 = array;
        final int n2 = 0;
        array3[n2] |= 0x40;
        return new SubjectKeyIdentifier(array);
    }
    
    private static byte[] getDigest(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final SHA1Digest sha1Digest = new SHA1Digest();
        final byte[] array = new byte[sha1Digest.getDigestSize()];
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sha1Digest.update(bytes, 0, bytes.length);
        sha1Digest.doFinal(array, 0);
        return array;
    }
}
