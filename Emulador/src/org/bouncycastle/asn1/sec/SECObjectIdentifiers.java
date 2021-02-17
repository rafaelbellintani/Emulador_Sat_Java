// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.sec;

import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.DERObjectIdentifier;

public interface SECObjectIdentifiers
{
    public static final DERObjectIdentifier ellipticCurve = new DERObjectIdentifier("1.3.132.0");
    public static final DERObjectIdentifier sect163k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".1");
    public static final DERObjectIdentifier sect163r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".2");
    public static final DERObjectIdentifier sect239k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".3");
    public static final DERObjectIdentifier sect113r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".4");
    public static final DERObjectIdentifier sect113r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".5");
    public static final DERObjectIdentifier secp112r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".6");
    public static final DERObjectIdentifier secp112r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".7");
    public static final DERObjectIdentifier secp160r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".8");
    public static final DERObjectIdentifier secp160k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".9");
    public static final DERObjectIdentifier secp256k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".10");
    public static final DERObjectIdentifier sect163r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".15");
    public static final DERObjectIdentifier sect283k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".16");
    public static final DERObjectIdentifier sect283r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".17");
    public static final DERObjectIdentifier sect131r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".22");
    public static final DERObjectIdentifier sect131r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".23");
    public static final DERObjectIdentifier sect193r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".24");
    public static final DERObjectIdentifier sect193r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".25");
    public static final DERObjectIdentifier sect233k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".26");
    public static final DERObjectIdentifier sect233r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".27");
    public static final DERObjectIdentifier secp128r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".28");
    public static final DERObjectIdentifier secp128r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".29");
    public static final DERObjectIdentifier secp160r2 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".30");
    public static final DERObjectIdentifier secp192k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".31");
    public static final DERObjectIdentifier secp224k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".32");
    public static final DERObjectIdentifier secp224r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".33");
    public static final DERObjectIdentifier secp384r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".34");
    public static final DERObjectIdentifier secp521r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".35");
    public static final DERObjectIdentifier sect409k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".36");
    public static final DERObjectIdentifier sect409r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".37");
    public static final DERObjectIdentifier sect571k1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".38");
    public static final DERObjectIdentifier sect571r1 = new DERObjectIdentifier(SECObjectIdentifiers.ellipticCurve + ".39");
    public static final DERObjectIdentifier secp192r1 = X9ObjectIdentifiers.prime192v1;
    public static final DERObjectIdentifier secp256r1 = X9ObjectIdentifiers.prime256v1;
}
