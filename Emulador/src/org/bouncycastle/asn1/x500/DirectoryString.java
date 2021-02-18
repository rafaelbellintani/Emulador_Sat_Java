// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x500;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class DirectoryString extends ASN1Encodable implements ASN1Choice, DERString
{
    private DERString string;
    
    public static DirectoryString getInstance(final Object o) {
        if (o instanceof DirectoryString) {
            return (DirectoryString)o;
        }
        if (o instanceof DERT61String) {
            return new DirectoryString((DERT61String)o);
        }
        if (o instanceof DERPrintableString) {
            return new DirectoryString((DERPrintableString)o);
        }
        if (o instanceof DERUniversalString) {
            return new DirectoryString((DERUniversalString)o);
        }
        if (o instanceof DERUTF8String) {
            return new DirectoryString((DERUTF8String)o);
        }
        if (o instanceof DERBMPString) {
            return new DirectoryString((DERBMPString)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DirectoryString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (!b) {
            throw new IllegalArgumentException("choice item must be explicitly tagged");
        }
        return getInstance(asn1TaggedObject.getObject());
    }
    
    private DirectoryString(final DERT61String string) {
        this.string = string;
    }
    
    private DirectoryString(final DERPrintableString string) {
        this.string = string;
    }
    
    private DirectoryString(final DERUniversalString string) {
        this.string = string;
    }
    
    private DirectoryString(final DERUTF8String string) {
        this.string = string;
    }
    
    private DirectoryString(final DERBMPString string) {
        this.string = string;
    }
    
    public DirectoryString(final String s) {
        this.string = new DERUTF8String(s);
    }
    
    @Override
    public String getString() {
        return this.string.getString();
    }
    
    @Override
    public String toString() {
        return this.string.getString();
    }
    
    @Override
    public DERObject toASN1Object() {
        return ((DEREncodable)this.string).getDERObject();
    }
}
