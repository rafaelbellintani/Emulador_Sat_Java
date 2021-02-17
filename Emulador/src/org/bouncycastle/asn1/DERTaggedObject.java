// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject
{
    private static final byte[] ZERO_BYTES;
    
    public DERTaggedObject(final int n, final DEREncodable derEncodable) {
        super(n, derEncodable);
    }
    
    public DERTaggedObject(final boolean b, final int n, final DEREncodable derEncodable) {
        super(b, n, derEncodable);
    }
    
    public DERTaggedObject(final int n) {
        super(false, n, new DERSequence());
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        if (!this.empty) {
            final byte[] encoded = this.obj.getDERObject().getEncoded("DER");
            if (this.explicit) {
                derOutputStream.writeEncoded(160, this.tagNo, encoded);
            }
            else {
                int n;
                if ((encoded[0] & 0x20) != 0x0) {
                    n = 160;
                }
                else {
                    n = 128;
                }
                derOutputStream.writeTag(n, this.tagNo);
                derOutputStream.write(encoded, 1, encoded.length - 1);
            }
        }
        else {
            derOutputStream.writeEncoded(160, this.tagNo, DERTaggedObject.ZERO_BYTES);
        }
    }
    
    static {
        ZERO_BYTES = new byte[0];
    }
}
