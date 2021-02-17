// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.util.Enumeration;
import java.io.IOException;

public class LazyDERSequence extends DERSequence
{
    private byte[] encoded;
    private boolean parsed;
    private int size;
    
    LazyDERSequence(final byte[] encoded) throws IOException {
        this.parsed = false;
        this.size = -1;
        this.encoded = encoded;
    }
    
    private void parse() {
        final LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
        while (lazyDERConstructionEnumeration.hasMoreElements()) {
            this.addObject(lazyDERConstructionEnumeration.nextElement());
        }
        this.parsed = true;
    }
    
    @Override
    public DEREncodable getObjectAt(final int n) {
        if (!this.parsed) {
            this.parse();
        }
        return super.getObjectAt(n);
    }
    
    @Override
    public Enumeration getObjects() {
        if (this.parsed) {
            return super.getObjects();
        }
        return new LazyDERConstructionEnumeration(this.encoded);
    }
    
    @Override
    public int size() {
        if (this.size < 0) {
            final LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
            this.size = 0;
            while (lazyDERConstructionEnumeration.hasMoreElements()) {
                lazyDERConstructionEnumeration.nextElement();
                ++this.size;
            }
        }
        return this.size;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(48, this.encoded);
    }
}
