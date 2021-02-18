// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.ResponseBytes;
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.asn1.ocsp.OCSPResponseStatus;

public class OCSPRespGenerator
{
    public static final int SUCCESSFUL = 0;
    public static final int MALFORMED_REQUEST = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int TRY_LATER = 3;
    public static final int SIG_REQUIRED = 5;
    public static final int UNAUTHORIZED = 6;
    
    public OCSPResp generate(final int n, final Object o) throws OCSPException {
        if (o == null) {
            return new OCSPResp(new OCSPResponse(new OCSPResponseStatus(n), null));
        }
        if (o instanceof BasicOCSPResp) {
            final BasicOCSPResp basicOCSPResp = (BasicOCSPResp)o;
            DEROctetString derOctetString;
            try {
                derOctetString = new DEROctetString(basicOCSPResp.getEncoded());
            }
            catch (IOException ex) {
                throw new OCSPException("can't encode object.", ex);
            }
            return new OCSPResp(new OCSPResponse(new OCSPResponseStatus(n), new ResponseBytes(OCSPObjectIdentifiers.id_pkix_ocsp_basic, derOctetString)));
        }
        throw new OCSPException("unknown response object");
    }
}
