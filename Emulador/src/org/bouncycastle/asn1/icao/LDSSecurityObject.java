// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class LDSSecurityObject extends ASN1Encodable implements ICAOObjectIdentifiers
{
    public static final int ub_DataGroups = 16;
    DERInteger version;
    AlgorithmIdentifier digestAlgorithmIdentifier;
    DataGroupHash[] datagroupHash;
    
    public static LDSSecurityObject getInstance(final Object o) {
        if (o == null || o instanceof LDSSecurityObject) {
            return (LDSSecurityObject)o;
        }
        if (o instanceof ASN1Sequence) {
            return new LDSSecurityObject(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + o.getClass().getName());
    }
    
    public LDSSecurityObject(final ASN1Sequence asn1Sequence) {
        this.version = new DERInteger(0);
        if (asn1Sequence == null || asn1Sequence.size() == 0) {
            throw new IllegalArgumentException("null or empty sequence passed.");
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = DERInteger.getInstance(objects.nextElement());
        this.digestAlgorithmIdentifier = AlgorithmIdentifier.getInstance(objects.nextElement());
        final ASN1Sequence instance = ASN1Sequence.getInstance(objects.nextElement());
        this.checkDatagroupHashSeqSize(instance.size());
        this.datagroupHash = new DataGroupHash[instance.size()];
        for (int i = 0; i < instance.size(); ++i) {
            this.datagroupHash[i] = DataGroupHash.getInstance(instance.getObjectAt(i));
        }
    }
    
    public LDSSecurityObject(final AlgorithmIdentifier digestAlgorithmIdentifier, final DataGroupHash[] datagroupHash) {
        this.version = new DERInteger(0);
        this.digestAlgorithmIdentifier = digestAlgorithmIdentifier;
        this.datagroupHash = datagroupHash;
        this.checkDatagroupHashSeqSize(datagroupHash.length);
    }
    
    private void checkDatagroupHashSeqSize(final int n) {
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("wrong size in DataGroupHashValues : not in (2..16)");
        }
    }
    
    public AlgorithmIdentifier getDigestAlgorithmIdentifier() {
        return this.digestAlgorithmIdentifier;
    }
    
    public DataGroupHash[] getDatagroupHash() {
        return this.datagroupHash;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.digestAlgorithmIdentifier);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < this.datagroupHash.length; ++i) {
            asn1EncodableVector2.add(this.datagroupHash[i]);
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        return new DERSequence(asn1EncodableVector);
    }
}
