// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CompressedData extends ASN1Encodable
{
    private DERInteger version;
    private AlgorithmIdentifier compressionAlgorithm;
    private ContentInfo encapContentInfo;
    
    public CompressedData(final AlgorithmIdentifier compressionAlgorithm, final ContentInfo encapContentInfo) {
        this.version = new DERInteger(0);
        this.compressionAlgorithm = compressionAlgorithm;
        this.encapContentInfo = encapContentInfo;
    }
    
    public CompressedData(final ASN1Sequence asn1Sequence) {
        this.version = (DERInteger)asn1Sequence.getObjectAt(0);
        this.compressionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.encapContentInfo = ContentInfo.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public static CompressedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static CompressedData getInstance(final Object o) {
        if (o == null || o instanceof CompressedData) {
            return (CompressedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CompressedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid CompressedData: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public AlgorithmIdentifier getCompressionAlgorithmIdentifier() {
        return this.compressionAlgorithm;
    }
    
    public ContentInfo getEncapContentInfo() {
        return this.encapContentInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.compressionAlgorithm);
        asn1EncodableVector.add(this.encapContentInfo);
        return new BERSequence(asn1EncodableVector);
    }
}
