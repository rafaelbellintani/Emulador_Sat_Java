// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class AuthenticatedSafe extends ASN1Encodable
{
    ContentInfo[] info;
    
    public AuthenticatedSafe(final ASN1Sequence asn1Sequence) {
        this.info = new ContentInfo[asn1Sequence.size()];
        for (int i = 0; i != this.info.length; ++i) {
            this.info[i] = ContentInfo.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public AuthenticatedSafe(final ContentInfo[] info) {
        this.info = info;
    }
    
    public ContentInfo[] getContentInfo() {
        return this.info;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != this.info.length; ++i) {
            asn1EncodableVector.add(this.info[i]);
        }
        return new BERSequence(asn1EncodableVector);
    }
}
