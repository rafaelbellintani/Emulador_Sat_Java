// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.util;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;

public class DERDump extends ASN1Dump
{
    public static String dumpAsString(final DERObject derObject) {
        final StringBuffer sb = new StringBuffer();
        ASN1Dump._dumpAsString("", false, derObject, sb);
        return sb.toString();
    }
    
    public static String dumpAsString(final DEREncodable derEncodable) {
        final StringBuffer sb = new StringBuffer();
        ASN1Dump._dumpAsString("", false, derEncodable.getDERObject(), sb);
        return sb.toString();
    }
}
