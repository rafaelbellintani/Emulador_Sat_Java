// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.oiw;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface OIWObjectIdentifiers
{
    public static final DERObjectIdentifier md4WithRSA = new DERObjectIdentifier("1.3.14.3.2.2");
    public static final DERObjectIdentifier md5WithRSA = new DERObjectIdentifier("1.3.14.3.2.3");
    public static final DERObjectIdentifier md4WithRSAEncryption = new DERObjectIdentifier("1.3.14.3.2.4");
    public static final DERObjectIdentifier desECB = new DERObjectIdentifier("1.3.14.3.2.6");
    public static final DERObjectIdentifier desCBC = new DERObjectIdentifier("1.3.14.3.2.7");
    public static final DERObjectIdentifier desOFB = new DERObjectIdentifier("1.3.14.3.2.8");
    public static final DERObjectIdentifier desCFB = new DERObjectIdentifier("1.3.14.3.2.9");
    public static final DERObjectIdentifier desEDE = new DERObjectIdentifier("1.3.14.3.2.17");
    public static final DERObjectIdentifier idSHA1 = new DERObjectIdentifier("1.3.14.3.2.26");
    public static final DERObjectIdentifier dsaWithSHA1 = new DERObjectIdentifier("1.3.14.3.2.27");
    public static final DERObjectIdentifier sha1WithRSA = new DERObjectIdentifier("1.3.14.3.2.29");
    public static final DERObjectIdentifier elGamalAlgorithm = new DERObjectIdentifier("1.3.14.7.2.1.1");
}
