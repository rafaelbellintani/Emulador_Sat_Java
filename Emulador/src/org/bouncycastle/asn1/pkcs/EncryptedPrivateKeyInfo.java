// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class EncryptedPrivateKeyInfo extends ASN1Encodable
{
    private AlgorithmIdentifier algId;
    private ASN1OctetString data;
    
    public EncryptedPrivateKeyInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
        this.data = objects.nextElement();
    }
    
    public EncryptedPrivateKeyInfo(final AlgorithmIdentifier algId, final byte[] array) {
        this.algId = algId;
        this.data = new DEROctetString(array);
    }
    
    public static EncryptedPrivateKeyInfo getInstance(final Object o) {
        if (o instanceof EncryptedData) {
            return (EncryptedPrivateKeyInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EncryptedPrivateKeyInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getEncryptionAlgorithm() {
        return this.algId;
    }
    
    public byte[] getEncryptedData() {
        return this.data.getOctets();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(this.data);
        return new DERSequence(asn1EncodableVector);
    }
}
