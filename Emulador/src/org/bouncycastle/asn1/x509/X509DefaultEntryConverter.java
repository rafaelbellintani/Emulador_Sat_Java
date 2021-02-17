// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERIA5String;
import java.io.IOException;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;

public class X509DefaultEntryConverter extends X509NameEntryConverter
{
    @Override
    public DERObject getConvertedValue(final DERObjectIdentifier derObjectIdentifier, String substring) {
        if (substring.length() != 0 && substring.charAt(0) == '#') {
            try {
                return this.convertHexEncoded(substring, 1);
            }
            catch (IOException ex) {
                throw new RuntimeException("can't recode value for oid " + derObjectIdentifier.getId());
            }
        }
        if (substring.length() != 0 && substring.charAt(0) == '\\') {
            substring = substring.substring(1);
        }
        if (derObjectIdentifier.equals(X509Name.EmailAddress) || derObjectIdentifier.equals(X509Name.DC)) {
            return new DERIA5String(substring);
        }
        if (derObjectIdentifier.equals(X509Name.DATE_OF_BIRTH)) {
            return new DERGeneralizedTime(substring);
        }
        if (derObjectIdentifier.equals(X509Name.C) || derObjectIdentifier.equals(X509Name.SN) || derObjectIdentifier.equals(X509Name.DN_QUALIFIER) || derObjectIdentifier.equals(X509Name.TELEPHONE_NUMBER)) {
            return new DERPrintableString(substring);
        }
        return new DERUTF8String(substring);
    }
}
