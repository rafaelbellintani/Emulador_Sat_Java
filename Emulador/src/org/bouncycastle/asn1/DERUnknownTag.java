// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;
import java.io.IOException;

public class DERUnknownTag extends DERObject
{
    private boolean isConstructed;
    private int tag;
    private byte[] data;
    
    public DERUnknownTag(final int n, final byte[] array) {
        this(false, n, array);
    }
    
    public DERUnknownTag(final boolean isConstructed, final int tag, final byte[] data) {
        this.isConstructed = isConstructed;
        this.tag = tag;
        this.data = data;
    }
    
    public boolean isConstructed() {
        return this.isConstructed;
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(this.isConstructed ? 32 : 0, this.tag, this.data);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DERUnknownTag)) {
            return false;
        }
        final DERUnknownTag derUnknownTag = (DERUnknownTag)o;
        return this.isConstructed == derUnknownTag.isConstructed && this.tag == derUnknownTag.tag && Arrays.areEqual(this.data, derUnknownTag.data);
    }
    
    @Override
    public int hashCode() {
        return (this.isConstructed ? -1 : 0) ^ this.tag ^ Arrays.hashCode(this.data);
    }
}
