// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.nist;

import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import java.util.Enumeration;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.Hashtable;

public class NISTNamedCurves
{
    static final Hashtable objIds;
    static final Hashtable names;
    
    static void defineCurve(final String s, final DERObjectIdentifier derObjectIdentifier) {
        NISTNamedCurves.objIds.put(s, derObjectIdentifier);
        NISTNamedCurves.names.put(derObjectIdentifier, s);
    }
    
    public static X9ECParameters getByName(final String s) {
        final DERObjectIdentifier derObjectIdentifier = NISTNamedCurves.objIds.get(Strings.toUpperCase(s));
        if (derObjectIdentifier != null) {
            return getByOID(derObjectIdentifier);
        }
        return null;
    }
    
    public static X9ECParameters getByOID(final DERObjectIdentifier derObjectIdentifier) {
        return SECNamedCurves.getByOID(derObjectIdentifier);
    }
    
    public static DERObjectIdentifier getOID(final String s) {
        return NISTNamedCurves.objIds.get(Strings.toUpperCase(s));
    }
    
    public static String getName(final DERObjectIdentifier key) {
        return NISTNamedCurves.names.get(key);
    }
    
    public static Enumeration getNames() {
        return NISTNamedCurves.objIds.keys();
    }
    
    static {
        objIds = new Hashtable();
        names = new Hashtable();
        defineCurve("B-571", SECObjectIdentifiers.sect571r1);
        defineCurve("B-409", SECObjectIdentifiers.sect409r1);
        defineCurve("B-283", SECObjectIdentifiers.sect283r1);
        defineCurve("B-233", SECObjectIdentifiers.sect233r1);
        defineCurve("B-163", SECObjectIdentifiers.sect163r2);
        defineCurve("P-521", SECObjectIdentifiers.secp521r1);
        defineCurve("P-384", SECObjectIdentifiers.secp384r1);
        defineCurve("P-256", SECObjectIdentifiers.secp256r1);
        defineCurve("P-224", SECObjectIdentifiers.secp224r1);
        defineCurve("P-192", SECObjectIdentifiers.secp192r1);
    }
}
