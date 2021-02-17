// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509.extension;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.ArrayList;
import java.util.Collections;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.util.Collection;
import java.security.cert.X509Certificate;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Object;

public class X509ExtensionUtil
{
    public static ASN1Object fromExtensionValue(final byte[] array) throws IOException {
        return ASN1Object.fromByteArray(((ASN1OctetString)ASN1Object.fromByteArray(array)).getOctets());
    }
    
    public static Collection getIssuerAlternativeNames(final X509Certificate x509Certificate) throws CertificateParsingException {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extensions.IssuerAlternativeName.getId()));
    }
    
    public static Collection getSubjectAlternativeNames(final X509Certificate x509Certificate) throws CertificateParsingException {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extensions.SubjectAlternativeName.getId()));
    }
    
    private static Collection getAlternativeNames(final byte[] array) throws CertificateParsingException {
        if (array == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            final ArrayList<ArrayList<Integer>> c = new ArrayList<ArrayList<Integer>>();
            final Enumeration objects = ASN1Sequence.getInstance(fromExtensionValue(array)).getObjects();
            while (objects.hasMoreElements()) {
                final GeneralName instance = GeneralName.getInstance(objects.nextElement());
                final ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(new Integer(instance.getTagNo()));
                switch (instance.getTagNo()) {
                    case 0:
                    case 3:
                    case 5: {
                        list.add((Integer)instance.getName().getDERObject());
                        break;
                    }
                    case 4: {
                        list.add((Integer)X509Name.getInstance(instance.getName()).toString());
                        break;
                    }
                    case 1:
                    case 2:
                    case 6: {
                        list.add((Integer)((DERString)instance.getName()).getString());
                        break;
                    }
                    case 8: {
                        list.add((Integer)DERObjectIdentifier.getInstance(instance.getName()).getId());
                        break;
                    }
                    case 7: {
                        list.add((Integer)(Object)ASN1OctetString.getInstance(instance.getName()).getOctets());
                        break;
                    }
                    default: {
                        throw new IOException("Bad tag number: " + instance.getTagNo());
                    }
                }
                c.add(list);
            }
            return Collections.unmodifiableCollection((Collection<?>)c);
        }
        catch (Exception ex) {
            throw new CertificateParsingException(ex.getMessage());
        }
    }
}
