// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1SequenceParser extends DEREncodable
{
    DEREncodable readObject() throws IOException;
}
