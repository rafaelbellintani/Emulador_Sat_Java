// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.iana;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface IANAObjectIdentifiers
{
    public static final DERObjectIdentifier isakmpOakley = new DERObjectIdentifier("1.3.6.1.5.5.8.1");
    public static final DERObjectIdentifier hmacMD5 = new DERObjectIdentifier(IANAObjectIdentifiers.isakmpOakley + ".1");
    public static final DERObjectIdentifier hmacSHA1 = new DERObjectIdentifier(IANAObjectIdentifiers.isakmpOakley + ".2");
    public static final DERObjectIdentifier hmacTIGER = new DERObjectIdentifier(IANAObjectIdentifiers.isakmpOakley + ".3");
    public static final DERObjectIdentifier hmacRIPEMD160 = new DERObjectIdentifier(IANAObjectIdentifiers.isakmpOakley + ".4");
}
