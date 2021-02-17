// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class TSTInfo extends ASN1Encodable
{
    DERInteger version;
    DERObjectIdentifier tsaPolicyId;
    MessageImprint messageImprint;
    DERInteger serialNumber;
    DERGeneralizedTime genTime;
    Accuracy accuracy;
    DERBoolean ordering;
    DERInteger nonce;
    GeneralName tsa;
    X509Extensions extensions;
    
    public static TSTInfo getInstance(final Object o) {
        if (o == null || o instanceof TSTInfo) {
            return (TSTInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TSTInfo((ASN1Sequence)o);
        }
        if (o instanceof ASN1OctetString) {
            try {
                return getInstance(new ASN1InputStream(((ASN1OctetString)o).getOctets()).readObject());
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Bad object format in 'TSTInfo' factory.");
            }
        }
        throw new IllegalArgumentException("Unknown object in 'TSTInfo' factory : " + o.getClass().getName() + ".");
    }
    
    public TSTInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = DERInteger.getInstance(objects.nextElement());
        this.tsaPolicyId = DERObjectIdentifier.getInstance(objects.nextElement());
        this.messageImprint = MessageImprint.getInstance(objects.nextElement());
        this.serialNumber = DERInteger.getInstance(objects.nextElement());
        this.genTime = DERGeneralizedTime.getInstance(objects.nextElement());
        this.ordering = new DERBoolean(false);
        while (objects.hasMoreElements()) {
            final DERObject derObject = objects.nextElement();
            if (derObject instanceof ASN1TaggedObject) {
                final DERTaggedObject derTaggedObject = (DERTaggedObject)derObject;
                switch (derTaggedObject.getTagNo()) {
                    case 0: {
                        this.tsa = GeneralName.getInstance(derTaggedObject, true);
                        continue;
                    }
                    case 1: {
                        this.extensions = X509Extensions.getInstance(derTaggedObject, false);
                        continue;
                    }
                    default: {
                        throw new IllegalArgumentException("Unknown tag value " + derTaggedObject.getTagNo());
                    }
                }
            }
            else if (derObject instanceof DERSequence) {
                this.accuracy = Accuracy.getInstance(derObject);
            }
            else if (derObject instanceof DERBoolean) {
                this.ordering = DERBoolean.getInstance(derObject);
            }
            else {
                if (!(derObject instanceof DERInteger)) {
                    continue;
                }
                this.nonce = DERInteger.getInstance(derObject);
            }
        }
    }
    
    public TSTInfo(final DERObjectIdentifier tsaPolicyId, final MessageImprint messageImprint, final DERInteger serialNumber, final DERGeneralizedTime genTime, final Accuracy accuracy, final DERBoolean ordering, final DERInteger nonce, final GeneralName tsa, final X509Extensions extensions) {
        this.version = new DERInteger(1);
        this.tsaPolicyId = tsaPolicyId;
        this.messageImprint = messageImprint;
        this.serialNumber = serialNumber;
        this.genTime = genTime;
        this.accuracy = accuracy;
        this.ordering = ordering;
        this.nonce = nonce;
        this.tsa = tsa;
        this.extensions = extensions;
    }
    
    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }
    
    public DERObjectIdentifier getPolicy() {
        return this.tsaPolicyId;
    }
    
    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    public Accuracy getAccuracy() {
        return this.accuracy;
    }
    
    public DERGeneralizedTime getGenTime() {
        return this.genTime;
    }
    
    public DERBoolean getOrdering() {
        return this.ordering;
    }
    
    public DERInteger getNonce() {
        return this.nonce;
    }
    
    public GeneralName getTsa() {
        return this.tsa;
    }
    
    public X509Extensions getExtensions() {
        return this.extensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.tsaPolicyId);
        asn1EncodableVector.add(this.messageImprint);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.genTime);
        if (this.accuracy != null) {
            asn1EncodableVector.add(this.accuracy);
        }
        if (this.ordering != null && this.ordering.isTrue()) {
            asn1EncodableVector.add(this.ordering);
        }
        if (this.nonce != null) {
            asn1EncodableVector.add(this.nonce);
        }
        if (this.tsa != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.tsa));
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.extensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
