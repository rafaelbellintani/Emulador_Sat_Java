// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.microsoft;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface MicrosoftObjectIdentifiers
{
    public static final DERObjectIdentifier microsoft = new DERObjectIdentifier("1.3.6.1.4.1.311");
    public static final DERObjectIdentifier microsoftCertTemplateV1 = new DERObjectIdentifier(MicrosoftObjectIdentifiers.microsoft + ".20.2");
    public static final DERObjectIdentifier microsoftCaVersion = new DERObjectIdentifier(MicrosoftObjectIdentifiers.microsoft + ".21.1");
    public static final DERObjectIdentifier microsoftPrevCaCertHash = new DERObjectIdentifier(MicrosoftObjectIdentifiers.microsoft + ".21.2");
    public static final DERObjectIdentifier microsoftCertTemplateV2 = new DERObjectIdentifier(MicrosoftObjectIdentifiers.microsoft + ".21.7");
    public static final DERObjectIdentifier microsoftAppPolicies = new DERObjectIdentifier(MicrosoftObjectIdentifiers.microsoft + ".21.10");
}
