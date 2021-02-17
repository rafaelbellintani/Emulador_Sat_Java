// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class QCStatement extends ASN1Encodable implements ETSIQCObjectIdentifiers, RFC3739QCObjectIdentifiers
{
    DERObjectIdentifier qcStatementId;
    ASN1Encodable qcStatementInfo;
    
    public static QCStatement getInstance(final Object o) {
        if (o == null || o instanceof QCStatement) {
            return (QCStatement)o;
        }
        if (o instanceof ASN1Sequence) {
            return new QCStatement(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public QCStatement(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.qcStatementId = DERObjectIdentifier.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.qcStatementInfo = objects.nextElement();
        }
    }
    
    public QCStatement(final DERObjectIdentifier qcStatementId) {
        this.qcStatementId = qcStatementId;
        this.qcStatementInfo = null;
    }
    
    public QCStatement(final DERObjectIdentifier qcStatementId, final ASN1Encodable qcStatementInfo) {
        this.qcStatementId = qcStatementId;
        this.qcStatementInfo = qcStatementInfo;
    }
    
    public DERObjectIdentifier getStatementId() {
        return this.qcStatementId;
    }
    
    public ASN1Encodable getStatementInfo() {
        return this.qcStatementInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.qcStatementId);
        if (this.qcStatementInfo != null) {
            asn1EncodableVector.add(this.qcStatementInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
