// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import java.util.Vector;
import java.util.Enumeration;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

public class ECNamedCurveTable
{
    public static ECNamedCurveParameterSpec getParameterSpec(final String s) {
        X9ECParameters x9ECParameters = X962NamedCurves.getByName(s);
        if (x9ECParameters == null) {
            try {
                x9ECParameters = X962NamedCurves.getByOID(new DERObjectIdentifier(s));
            }
            catch (IllegalArgumentException ex) {}
        }
        if (x9ECParameters == null) {
            x9ECParameters = SECNamedCurves.getByName(s);
            if (x9ECParameters == null) {
                try {
                    x9ECParameters = SECNamedCurves.getByOID(new DERObjectIdentifier(s));
                }
                catch (IllegalArgumentException ex2) {}
            }
        }
        if (x9ECParameters == null) {
            x9ECParameters = TeleTrusTNamedCurves.getByName(s);
            if (x9ECParameters == null) {
                try {
                    x9ECParameters = TeleTrusTNamedCurves.getByOID(new DERObjectIdentifier(s));
                }
                catch (IllegalArgumentException ex3) {}
            }
        }
        if (x9ECParameters == null) {
            x9ECParameters = NISTNamedCurves.getByName(s);
        }
        if (x9ECParameters == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(s, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
    }
    
    public static Enumeration getNames() {
        final Vector vector = new Vector();
        addEnumeration(vector, X962NamedCurves.getNames());
        addEnumeration(vector, SECNamedCurves.getNames());
        addEnumeration(vector, NISTNamedCurves.getNames());
        addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        return vector.elements();
    }
    
    private static void addEnumeration(final Vector vector, final Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }
}
