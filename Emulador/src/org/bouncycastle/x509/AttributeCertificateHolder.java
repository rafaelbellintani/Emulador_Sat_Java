// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.Principal;
import java.util.ArrayList;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.ObjectDigestInfo;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.jce.PrincipalUtil;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.GeneralName;
import java.math.BigInteger;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.Holder;
import org.bouncycastle.util.Selector;
import java.security.cert.CertSelector;

public class AttributeCertificateHolder implements CertSelector, Selector
{
    final Holder holder;
    
    AttributeCertificateHolder(final ASN1Sequence asn1Sequence) {
        this.holder = Holder.getInstance(asn1Sequence);
    }
    
    public AttributeCertificateHolder(final X509Principal x509Principal, final BigInteger bigInteger) {
        this.holder = new Holder(new IssuerSerial(new GeneralNames(new DERSequence(new GeneralName(x509Principal))), new DERInteger(bigInteger)));
    }
    
    public AttributeCertificateHolder(final X500Principal x500Principal, final BigInteger bigInteger) {
        this(X509Util.convertPrincipal(x500Principal), bigInteger);
    }
    
    public AttributeCertificateHolder(final X509Certificate x509Certificate) throws CertificateParsingException {
        X509Principal issuerX509Principal;
        try {
            issuerX509Principal = PrincipalUtil.getIssuerX509Principal(x509Certificate);
        }
        catch (Exception ex) {
            throw new CertificateParsingException(ex.getMessage());
        }
        this.holder = new Holder(new IssuerSerial(this.generateGeneralNames(issuerX509Principal), new DERInteger(x509Certificate.getSerialNumber())));
    }
    
    public AttributeCertificateHolder(final X509Principal x509Principal) {
        this.holder = new Holder(this.generateGeneralNames(x509Principal));
    }
    
    public AttributeCertificateHolder(final X500Principal x500Principal) {
        this(X509Util.convertPrincipal(x500Principal));
    }
    
    public AttributeCertificateHolder(final int n, final String s, final String s2, final byte[] array) {
        this.holder = new Holder(new ObjectDigestInfo(n, s2, new AlgorithmIdentifier(s), Arrays.clone(array)));
    }
    
    public int getDigestedObjectType() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getDigestedObjectType().getValue().intValue();
        }
        return -1;
    }
    
    public String getDigestAlgorithm() {
        if (this.holder.getObjectDigestInfo() != null) {
            this.holder.getObjectDigestInfo().getDigestAlgorithm().getObjectId().getId();
        }
        return null;
    }
    
    public byte[] getObjectDigest() {
        if (this.holder.getObjectDigestInfo() != null) {
            this.holder.getObjectDigestInfo().getObjectDigest().getBytes();
        }
        return null;
    }
    
    public String getOtherObjectTypeID() {
        if (this.holder.getObjectDigestInfo() != null) {
            this.holder.getObjectDigestInfo().getOtherObjectTypeID().getId();
        }
        return null;
    }
    
    private GeneralNames generateGeneralNames(final X509Principal x509Principal) {
        return new GeneralNames(new DERSequence(new GeneralName(x509Principal)));
    }
    
    private boolean matchesDN(final X509Principal x509Principal, final GeneralNames generalNames) {
        final GeneralName[] names = generalNames.getNames();
        for (int i = 0; i != names.length; ++i) {
            final GeneralName generalName = names[i];
            if (generalName.getTagNo() == 4) {
                try {
                    if (new X509Principal(((ASN1Encodable)generalName.getName()).getEncoded()).equals(x509Principal)) {
                        return true;
                    }
                }
                catch (IOException ex) {}
            }
        }
        return false;
    }
    
    private Object[] getNames(final GeneralName[] array) {
        final ArrayList<X500Principal> list = new ArrayList<X500Principal>(array.length);
        for (int i = 0; i != array.length; ++i) {
            if (array[i].getTagNo() == 4) {
                try {
                    list.add(new X500Principal(((ASN1Encodable)array[i].getName()).getEncoded()));
                }
                catch (IOException ex) {
                    throw new RuntimeException("badly formed Name object");
                }
            }
        }
        return list.toArray(new Object[list.size()]);
    }
    
    private Principal[] getPrincipals(final GeneralNames generalNames) {
        final Object[] names = this.getNames(generalNames.getNames());
        final ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i != names.length; ++i) {
            if (names[i] instanceof Principal) {
                list.add(names[i]);
            }
        }
        return list.toArray(new Principal[list.size()]);
    }
    
    public Principal[] getEntityNames() {
        if (this.holder.getEntityName() != null) {
            return this.getPrincipals(this.holder.getEntityName());
        }
        return null;
    }
    
    public Principal[] getIssuer() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.getPrincipals(this.holder.getBaseCertificateID().getIssuer());
        }
        return null;
    }
    
    public BigInteger getSerialNumber() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.holder.getBaseCertificateID().getSerial().getValue();
        }
        return null;
    }
    
    @Override
    public Object clone() {
        return new AttributeCertificateHolder((ASN1Sequence)this.holder.toASN1Object());
    }
    
    @Override
    public boolean match(final Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        final X509Certificate x509Certificate = (X509Certificate)certificate;
        try {
            if (this.holder.getBaseCertificateID() != null) {
                return this.holder.getBaseCertificateID().getSerial().getValue().equals(x509Certificate.getSerialNumber()) && this.matchesDN(PrincipalUtil.getIssuerX509Principal(x509Certificate), this.holder.getBaseCertificateID().getIssuer());
            }
            if (this.holder.getEntityName() != null && this.matchesDN(PrincipalUtil.getSubjectX509Principal(x509Certificate), this.holder.getEntityName())) {
                return true;
            }
            if (this.holder.getObjectDigestInfo() != null) {
                MessageDigest instance;
                try {
                    instance = MessageDigest.getInstance(this.getDigestAlgorithm(), "BC");
                }
                catch (Exception ex) {
                    return false;
                }
                switch (this.getDigestedObjectType()) {
                    case 0: {
                        instance.update(certificate.getPublicKey().getEncoded());
                        break;
                    }
                    case 1: {
                        instance.update(certificate.getEncoded());
                        break;
                    }
                }
                if (!Arrays.areEqual(instance.digest(), this.getObjectDigest())) {
                    return false;
                }
            }
        }
        catch (CertificateEncodingException ex2) {
            return false;
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AttributeCertificateHolder && this.holder.equals(((AttributeCertificateHolder)o).holder));
    }
    
    @Override
    public int hashCode() {
        return this.holder.hashCode();
    }
    
    @Override
    public boolean match(final Object o) {
        return o instanceof X509Certificate && this.match((Certificate)o);
    }
}
