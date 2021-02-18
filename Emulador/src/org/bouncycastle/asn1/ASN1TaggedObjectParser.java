// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1TaggedObjectParser extends DEREncodable
{
    int getTagNo();
    
    DEREncodable getObjectParser(final int p0, final boolean p1) throws IOException;
}
