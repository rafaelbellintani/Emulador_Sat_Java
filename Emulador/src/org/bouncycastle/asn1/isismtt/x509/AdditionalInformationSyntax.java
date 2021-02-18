// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.ASN1Encodable;

public class AdditionalInformationSyntax extends ASN1Encodable
{
    private DirectoryString information;
    
    public static AdditionalInformationSyntax getInstance(final Object o) {
        if (o == null || o instanceof AdditionalInformationSyntax) {
            return (AdditionalInformationSyntax)o;
        }
        if (o instanceof DERString) {
            return new AdditionalInformationSyntax(DirectoryString.getInstance(o));
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    private AdditionalInformationSyntax(final DirectoryString information) {
        this.information = information;
    }
    
    public AdditionalInformationSyntax(final String s) {
        this(new DirectoryString(s));
    }
    
    public DirectoryString getInformation() {
        return this.information;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.information.toASN1Object();
    }
}
