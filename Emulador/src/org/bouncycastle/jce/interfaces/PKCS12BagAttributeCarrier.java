// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;

public interface PKCS12BagAttributeCarrier
{
    void setBagAttribute(final DERObjectIdentifier p0, final DEREncodable p1);
    
    DEREncodable getBagAttribute(final DERObjectIdentifier p0);
    
    Enumeration getBagAttributeKeys();
}
