// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Enumeration;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Encodable;

public class PrivateKeyInfo extends ASN1Encodable
{
    private DERObject privKey;
    private AlgorithmIdentifier algId;
    private ASN1Set attributes;
    
    public static PrivateKeyInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static PrivateKeyInfo getInstance(final Object o) {
        if (o instanceof PrivateKeyInfo) {
            return (PrivateKeyInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PrivateKeyInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public PrivateKeyInfo(final AlgorithmIdentifier algorithmIdentifier, final DERObject derObject) {
        this(algorithmIdentifier, derObject, null);
    }
    
    public PrivateKeyInfo(final AlgorithmIdentifier algId, final DERObject privKey, final ASN1Set attributes) {
        this.privKey = privKey;
        this.algId = algId;
        this.attributes = attributes;
    }
    
    public PrivateKeyInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        if (objects.nextElement().getValue().intValue() != 0) {
            throw new IllegalArgumentException("wrong version for private key info");
        }
        this.algId = new AlgorithmIdentifier((ASN1Sequence)objects.nextElement());
        try {
            this.privKey = new ASN1InputStream(((ASN1OctetString)objects.nextElement()).getOctets()).readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Error recoverying private key from sequence");
        }
        if (objects.hasMoreElements()) {
            this.attributes = ASN1Set.getInstance((ASN1TaggedObject)objects.nextElement(), false);
        }
    }
    
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }
    
    public DERObject getPrivateKey() {
        return this.privKey;
    }
    
    public ASN1Set getAttributes() {
        return this.attributes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(0));
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(new DEROctetString(this.privKey));
        if (this.attributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.attributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
