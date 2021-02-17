// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class RecipientEncryptedKey extends ASN1Encodable
{
    private KeyAgreeRecipientIdentifier identifier;
    private ASN1OctetString encryptedKey;
    
    private RecipientEncryptedKey(final ASN1Sequence asn1Sequence) {
        this.identifier = KeyAgreeRecipientIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(1);
    }
    
    public static RecipientEncryptedKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static RecipientEncryptedKey getInstance(final Object o) {
        if (o == null || o instanceof RecipientEncryptedKey) {
            return (RecipientEncryptedKey)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RecipientEncryptedKey((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid RecipientEncryptedKey: " + o.getClass().getName());
    }
    
    public RecipientEncryptedKey(final KeyAgreeRecipientIdentifier identifier, final ASN1OctetString encryptedKey) {
        this.identifier = identifier;
        this.encryptedKey = encryptedKey;
    }
    
    public KeyAgreeRecipientIdentifier getIdentifier() {
        return this.identifier;
    }
    
    public ASN1OctetString getEncryptedKey() {
        return this.encryptedKey;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.identifier);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
