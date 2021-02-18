// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Strings;

public class DERUTF8String extends ASN1Object implements DERString
{
    String string;
    
    public static DERUTF8String getInstance(final Object o) {
        if (o == null || o instanceof DERUTF8String) {
            return (DERUTF8String)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERUTF8String(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERUTF8String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    DERUTF8String(final byte[] array) {
        this.string = Strings.fromUTF8ByteArray(array);
    }
    
    public DERUTF8String(final String string) {
        this.string = string;
    }
    
    @Override
    public String getString() {
        return this.string;
    }
    
    @Override
    public String toString() {
        return this.string;
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERUTF8String && this.getString().equals(((DERUTF8String)derObject).getString());
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(12, Strings.toUTF8ByteArray(this.string));
    }
}
