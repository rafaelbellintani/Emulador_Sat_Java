// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends DERTaggedObject
{
    public BERTaggedObject(final int n, final DEREncodable derEncodable) {
        super(n, derEncodable);
    }
    
    public BERTaggedObject(final boolean b, final int n, final DEREncodable derEncodable) {
        super(b, n, derEncodable);
    }
    
    public BERTaggedObject(final int n) {
        super(false, n, new BERSequence());
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        if (derOutputStream instanceof ASN1OutputStream || derOutputStream instanceof BEROutputStream) {
            derOutputStream.writeTag(160, this.tagNo);
            derOutputStream.write(128);
            if (!this.empty) {
                if (!this.explicit) {
                    Enumeration enumeration;
                    if (this.obj instanceof ASN1OctetString) {
                        if (this.obj instanceof BERConstructedOctetString) {
                            enumeration = ((BERConstructedOctetString)this.obj).getObjects();
                        }
                        else {
                            enumeration = new BERConstructedOctetString(((ASN1OctetString)this.obj).getOctets()).getObjects();
                        }
                    }
                    else if (this.obj instanceof ASN1Sequence) {
                        enumeration = ((ASN1Sequence)this.obj).getObjects();
                    }
                    else {
                        if (!(this.obj instanceof ASN1Set)) {
                            throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
                        }
                        enumeration = ((ASN1Set)this.obj).getObjects();
                    }
                    while (enumeration.hasMoreElements()) {
                        derOutputStream.writeObject(enumeration.nextElement());
                    }
                }
                else {
                    derOutputStream.writeObject(this.obj);
                }
            }
            derOutputStream.write(0);
            derOutputStream.write(0);
        }
        else {
            super.encode(derOutputStream);
        }
    }
}
