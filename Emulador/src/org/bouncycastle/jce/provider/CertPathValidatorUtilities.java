// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.GeneralSecurityException;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import java.security.interfaces.DSAParams;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.DSAPublicKeySpec;
import java.security.interfaces.DSAPublicKey;
import java.text.ParseException;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;
import java.security.cert.CertPath;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.ASN1Object;
import java.math.BigInteger;
import java.security.cert.X509CRLSelector;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import java.security.cert.CertSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.X509StoreParameters;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.asn1.x509.PolicyInformation;
import java.util.Map;
import java.security.cert.PolicyNode;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.PolicyQualifierInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.cert.CertStoreException;
import java.security.cert.CRLSelector;
import java.security.cert.CertStore;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509Store;
import java.util.HashSet;
import java.util.Collection;
import org.bouncycastle.x509.X509CRLStoreSelector;
import java.security.cert.CertPathValidatorException;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObject;
import java.security.cert.X509Extension;
import java.util.Date;
import java.security.cert.PKIXParameters;
import org.bouncycastle.x509.X509AttributeCertificate;
import java.security.cert.CertificateParsingException;
import java.util.List;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.util.Iterator;
import java.security.PublicKey;
import javax.security.auth.x500.X500Principal;
import java.security.cert.Certificate;
import java.io.IOException;
import java.security.cert.X509CertSelector;
import java.security.cert.TrustAnchor;
import java.util.Set;
import java.security.cert.X509Certificate;

public class CertPathValidatorUtilities
{
    protected static final String CERTIFICATE_POLICIES;
    protected static final String BASIC_CONSTRAINTS;
    protected static final String POLICY_MAPPINGS;
    protected static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String NAME_CONSTRAINTS;
    protected static final String KEY_USAGE;
    protected static final String INHIBIT_ANY_POLICY;
    protected static final String ISSUING_DISTRIBUTION_POINT;
    protected static final String DELTA_CRL_INDICATOR;
    protected static final String POLICY_CONSTRAINTS;
    protected static final String FRESHEST_CRL;
    protected static final String CRL_DISTRIBUTION_POINTS;
    protected static final String AUTHORITY_KEY_IDENTIFIER;
    protected static final String ANY_POLICY = "2.5.29.32.0";
    protected static final String CRL_NUMBER;
    protected static final int KEY_CERT_SIGN = 5;
    protected static final int CRL_SIGN = 6;
    protected static final String[] crlReasons;
    
    protected static TrustAnchor findTrustAnchor(final X509Certificate x509Certificate, final Set set) throws AnnotatedException {
        return findTrustAnchor(x509Certificate, set, null);
    }
    
    protected static TrustAnchor findTrustAnchor(final X509Certificate x509Certificate, final Set set, final String s) throws AnnotatedException {
        TrustAnchor trustAnchor = null;
        PublicKey publicKey = null;
        Throwable t = null;
        final X509CertSelector x509CertSelector = new X509CertSelector();
        final X500Principal encodedIssuerPrincipal = getEncodedIssuerPrincipal(x509Certificate);
        try {
            x509CertSelector.setSubject(encodedIssuerPrincipal.getEncoded());
        }
        catch (IOException ex) {
            throw new AnnotatedException("Cannot set subject search criteria for trust anchor.", ex);
        }
        final Iterator<TrustAnchor> iterator = set.iterator();
        while (iterator.hasNext() && trustAnchor == null) {
            trustAnchor = iterator.next();
            if (trustAnchor.getTrustedCert() != null) {
                if (x509CertSelector.match(trustAnchor.getTrustedCert())) {
                    publicKey = trustAnchor.getTrustedCert().getPublicKey();
                }
                else {
                    trustAnchor = null;
                }
            }
            else if (trustAnchor.getCAName() != null && trustAnchor.getCAPublicKey() != null) {
                try {
                    if (encodedIssuerPrincipal.equals(new X500Principal(trustAnchor.getCAName()))) {
                        publicKey = trustAnchor.getCAPublicKey();
                    }
                    else {
                        trustAnchor = null;
                    }
                }
                catch (IllegalArgumentException ex3) {
                    trustAnchor = null;
                }
            }
            else {
                trustAnchor = null;
            }
            if (publicKey != null) {
                try {
                    verifyX509Certificate(x509Certificate, publicKey, s);
                }
                catch (Exception ex2) {
                    t = ex2;
                    trustAnchor = null;
                }
            }
        }
        if (trustAnchor == null && t != null) {
            throw new AnnotatedException("TrustAnchor found but certificate validation failed.", t);
        }
        return trustAnchor;
    }
    
    protected static void addAdditionalStoresFromAltNames(final X509Certificate x509Certificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertificateParsingException {
        if (x509Certificate.getIssuerAlternativeNames() != null) {
            for (final List<Object> list : x509Certificate.getIssuerAlternativeNames()) {
                if (list.get(0).equals(new Integer(6))) {
                    addAdditionalStoreFromLocation((String)list.get(1), extendedPKIXParameters);
                }
            }
        }
    }
    
    protected static X500Principal getEncodedIssuerPrincipal(final Object o) {
        if (o instanceof X509Certificate) {
            return ((X509Certificate)o).getIssuerX500Principal();
        }
        return (X500Principal)((X509AttributeCertificate)o).getIssuer().getPrincipals()[0];
    }
    
    protected static Date getValidDate(final PKIXParameters pkixParameters) {
        Date date = pkixParameters.getDate();
        if (date == null) {
            date = new Date();
        }
        return date;
    }
    
    protected static X500Principal getSubjectPrincipal(final X509Certificate x509Certificate) {
        return x509Certificate.getSubjectX500Principal();
    }
    
    protected static boolean isSelfIssued(final X509Certificate x509Certificate) {
        return x509Certificate.getSubjectDN().equals(x509Certificate.getIssuerDN());
    }
    
    protected static DERObject getExtensionValue(final X509Extension x509Extension, final String s) throws AnnotatedException {
        final byte[] extensionValue = x509Extension.getExtensionValue(s);
        if (extensionValue == null) {
            return null;
        }
        return getObject(s, extensionValue);
    }
    
    private static DERObject getObject(final String str, final byte[] array) throws AnnotatedException {
        try {
            return new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(array).readObject()).getOctets()).readObject();
        }
        catch (Exception ex) {
            throw new AnnotatedException("exception processing extension " + str, ex);
        }
    }
    
    protected static X500Principal getIssuerPrincipal(final X509CRL x509CRL) {
        return x509CRL.getIssuerX500Principal();
    }
    
    protected static AlgorithmIdentifier getAlgorithmIdentifier(final PublicKey publicKey) throws CertPathValidatorException {
        try {
            return SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getAlgorithmId();
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Subject public key cannot be decoded.", ex);
        }
    }
    
    protected static final Collection findCRLs(final X509CRLStoreSelector selector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        final Iterator<X509Store> iterator = list.iterator();
        Object o = null;
        boolean b = false;
        while (iterator.hasNext()) {
            final X509Store next = iterator.next();
            if (next instanceof X509Store) {
                final X509Store x509Store = next;
                try {
                    set.addAll(x509Store.getMatches(selector));
                    b = true;
                }
                catch (StoreException ex) {
                    o = new AnnotatedException("Exception searching in X.509 CRL store.", ex);
                }
            }
            else {
                final CertStore certStore = (CertStore)next;
                try {
                    set.addAll(certStore.getCRLs(selector));
                    b = true;
                }
                catch (CertStoreException ex2) {
                    o = new AnnotatedException("Exception searching in X.509 CRL store.", ex2);
                }
            }
        }
        if (!b && o != null) {
            throw o;
        }
        return set;
    }
    
    protected static final Set getQualifierSet(final ASN1Sequence asn1Sequence) throws CertPathValidatorException {
        final HashSet<PolicyQualifierInfo> set = new HashSet<PolicyQualifierInfo>();
        if (asn1Sequence == null) {
            return set;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            try {
                asn1OutputStream.writeObject(objects.nextElement());
                set.add(new PolicyQualifierInfo(byteArrayOutputStream.toByteArray()));
            }
            catch (IOException ex) {
                throw new ExtCertPathValidatorException("Policy qualifier info cannot be decoded.", ex);
            }
            byteArrayOutputStream.reset();
        }
        return set;
    }
    
    protected static PKIXPolicyNode removePolicyNode(final PKIXPolicyNode pkixPolicyNode, final List[] array, final PKIXPolicyNode pkixPolicyNode2) {
        final PKIXPolicyNode pkixPolicyNode3 = (PKIXPolicyNode)pkixPolicyNode2.getParent();
        if (pkixPolicyNode == null) {
            return null;
        }
        if (pkixPolicyNode3 == null) {
            for (int i = 0; i < array.length; ++i) {
                array[i] = new ArrayList();
            }
            return null;
        }
        pkixPolicyNode3.removeChild(pkixPolicyNode2);
        removePolicyNodeRecurse(array, pkixPolicyNode2);
        return pkixPolicyNode;
    }
    
    private static void removePolicyNodeRecurse(final List[] array, final PKIXPolicyNode pkixPolicyNode) {
        array[pkixPolicyNode.getDepth()].remove(pkixPolicyNode);
        if (pkixPolicyNode.hasChildren()) {
            final Iterator children = pkixPolicyNode.getChildren();
            while (children.hasNext()) {
                removePolicyNodeRecurse(array, children.next());
            }
        }
    }
    
    protected static boolean processCertD1i(final int n, final List[] array, final DERObjectIdentifier derObjectIdentifier, final Set set) {
        final List list = array[n - 1];
        for (int i = 0; i < list.size(); ++i) {
            final PKIXPolicyNode pkixPolicyNode = list.get(i);
            if (pkixPolicyNode.getExpectedPolicies().contains(derObjectIdentifier.getId())) {
                final HashSet<String> set2 = new HashSet<String>();
                set2.add(derObjectIdentifier.getId());
                final PKIXPolicyNode pkixPolicyNode2 = new PKIXPolicyNode(new ArrayList(), n, set2, pkixPolicyNode, set, derObjectIdentifier.getId(), false);
                pkixPolicyNode.addChild(pkixPolicyNode2);
                array[n].add(pkixPolicyNode2);
                return true;
            }
        }
        return false;
    }
    
    protected static void processCertD1ii(final int n, final List[] array, final DERObjectIdentifier derObjectIdentifier, final Set set) {
        final List list = array[n - 1];
        for (int i = 0; i < list.size(); ++i) {
            final PKIXPolicyNode pkixPolicyNode = list.get(i);
            if ("2.5.29.32.0".equals(pkixPolicyNode.getValidPolicy())) {
                final HashSet<String> set2 = new HashSet<String>();
                set2.add(derObjectIdentifier.getId());
                final PKIXPolicyNode pkixPolicyNode2 = new PKIXPolicyNode(new ArrayList(), n, set2, pkixPolicyNode, set, derObjectIdentifier.getId(), false);
                pkixPolicyNode.addChild(pkixPolicyNode2);
                array[n].add(pkixPolicyNode2);
                return;
            }
        }
    }
    
    protected static void prepareNextCertB1(final int n, final List[] array, final String anObject, final Map map, final X509Certificate x509Certificate) throws AnnotatedException, CertPathValidatorException {
        boolean b = false;
        for (final PKIXPolicyNode pkixPolicyNode : array[n]) {
            if (pkixPolicyNode.getValidPolicy().equals(anObject)) {
                b = true;
                pkixPolicyNode.expectedPolicies = map.get(anObject);
                break;
            }
        }
        if (!b) {
            for (final PKIXPolicyNode pkixPolicyNode2 : array[n]) {
                if ("2.5.29.32.0".equals(pkixPolicyNode2.getValidPolicy())) {
                    Set qualifierSet = null;
                    ASN1Sequence instance;
                    try {
                        instance = ASN1Sequence.getInstance(getExtensionValue(x509Certificate, CertPathValidatorUtilities.CERTIFICATE_POLICIES));
                    }
                    catch (Exception ex) {
                        throw new AnnotatedException("Certificate policies cannot be decoded.", ex);
                    }
                    final Enumeration objects = instance.getObjects();
                    while (objects.hasMoreElements()) {
                        PolicyInformation instance2;
                        try {
                            instance2 = PolicyInformation.getInstance(objects.nextElement());
                        }
                        catch (Exception ex2) {
                            throw new AnnotatedException("Policy information cannot be decoded.", ex2);
                        }
                        if ("2.5.29.32.0".equals(instance2.getPolicyIdentifier().getId())) {
                            try {
                                qualifierSet = getQualifierSet(instance2.getPolicyQualifiers());
                                break;
                            }
                            catch (CertPathValidatorException ex3) {
                                throw new ExtCertPathValidatorException("Policy qualifier info set could not be built.", ex3);
                            }
                        }
                    }
                    boolean contains = false;
                    if (x509Certificate.getCriticalExtensionOIDs() != null) {
                        contains = x509Certificate.getCriticalExtensionOIDs().contains(CertPathValidatorUtilities.CERTIFICATE_POLICIES);
                    }
                    final PKIXPolicyNode pkixPolicyNode3 = (PKIXPolicyNode)pkixPolicyNode2.getParent();
                    if ("2.5.29.32.0".equals(pkixPolicyNode3.getValidPolicy())) {
                        final PKIXPolicyNode pkixPolicyNode4 = new PKIXPolicyNode(new ArrayList(), n, map.get(anObject), pkixPolicyNode3, qualifierSet, anObject, contains);
                        pkixPolicyNode3.addChild(pkixPolicyNode4);
                        array[n].add(pkixPolicyNode4);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    protected static PKIXPolicyNode prepareNextCertB2(final int n, final List[] array, final String anObject, PKIXPolicyNode removePolicyNode) {
        final Iterator<PKIXPolicyNode> iterator = (Iterator<PKIXPolicyNode>)array[n].iterator();
        while (iterator.hasNext()) {
            final PKIXPolicyNode pkixPolicyNode = iterator.next();
            if (pkixPolicyNode.getValidPolicy().equals(anObject)) {
                ((PKIXPolicyNode)pkixPolicyNode.getParent()).removeChild(pkixPolicyNode);
                iterator.remove();
                for (int i = n - 1; i >= 0; --i) {
                    final List list = array[i];
                    for (int j = 0; j < list.size(); ++j) {
                        final PKIXPolicyNode pkixPolicyNode2 = list.get(j);
                        if (!pkixPolicyNode2.hasChildren()) {
                            removePolicyNode = removePolicyNode(removePolicyNode, array, pkixPolicyNode2);
                            if (removePolicyNode == null) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return removePolicyNode;
    }
    
    protected static boolean isAnyPolicy(final Set set) {
        return set == null || set.contains("2.5.29.32.0") || set.isEmpty();
    }
    
    protected static void addAdditionalStoreFromLocation(String substring, final ExtendedPKIXParameters extendedPKIXParameters) {
        if (extendedPKIXParameters.isAdditionalLocationsEnabled()) {
            try {
                if (substring.startsWith("ldap://")) {
                    substring = substring.substring(7);
                    String substring2 = null;
                    String s;
                    if (substring.indexOf("/") != -1) {
                        substring2 = substring.substring(substring.indexOf("/"));
                        s = "ldap://" + substring.substring(0, substring.indexOf("/"));
                    }
                    else {
                        s = "ldap://" + substring;
                    }
                    final X509LDAPCertStoreParameters build = new X509LDAPCertStoreParameters.Builder(s, substring2).build();
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATE/LDAP", build, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CRL/LDAP", build, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("ATTRIBUTECERTIFICATE/LDAP", build, "BC"));
                    extendedPKIXParameters.addAdditionalStore(X509Store.getInstance("CERTIFICATEPAIR/LDAP", build, "BC"));
                }
            }
            catch (Exception ex) {
                throw new RuntimeException("Exception adding X.509 stores.");
            }
        }
    }
    
    protected static Collection findCertificates(final X509CertStoreSelector selector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        for (final X509Store next : list) {
            if (next instanceof X509Store) {
                final X509Store x509Store = next;
                try {
                    set.addAll(x509Store.getMatches(selector));
                }
                catch (StoreException ex) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", ex);
                }
            }
            else {
                final CertStore certStore = (CertStore)next;
                try {
                    set.addAll(certStore.getCertificates(selector));
                }
                catch (CertStoreException ex2) {
                    throw new AnnotatedException("Problem while picking certificates from certificate store.", ex2);
                }
            }
        }
        return set;
    }
    
    protected static Collection findCertificates(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        for (final X509Store next : list) {
            if (next instanceof X509Store) {
                final X509Store x509Store = next;
                try {
                    set.addAll(x509Store.getMatches(x509AttributeCertStoreSelector));
                }
                catch (StoreException ex) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", ex);
                }
            }
        }
        return set;
    }
    
    protected static void addAdditionalStoresFromCRLDistributionPoint(final CRLDistPoint crlDistPoint, final ExtendedPKIXParameters extendedPKIXParameters) throws AnnotatedException {
        if (crlDistPoint != null) {
            DistributionPoint[] distributionPoints;
            try {
                distributionPoints = crlDistPoint.getDistributionPoints();
            }
            catch (Exception ex) {
                throw new AnnotatedException("Distribution points could not be read.", ex);
            }
            for (int i = 0; i < distributionPoints.length; ++i) {
                final DistributionPointName distributionPoint = distributionPoints[i].getDistributionPoint();
                if (distributionPoint != null && distributionPoint.getType() == 0) {
                    final GeneralName[] names = GeneralNames.getInstance(distributionPoint.getName()).getNames();
                    for (int j = 0; j < names.length; ++j) {
                        if (names[j].getTagNo() == 6) {
                            addAdditionalStoreFromLocation(DERIA5String.getInstance(names[j].getName()).getString(), extendedPKIXParameters);
                        }
                    }
                }
            }
        }
    }
    
    protected static void getCRLIssuersFromDistributionPoint(final DistributionPoint distributionPoint, final Collection collection, final X509CRLSelector x509CRLSelector, final ExtendedPKIXParameters extendedPKIXParameters) throws AnnotatedException {
        final ArrayList<X500Principal> list = new ArrayList<X500Principal>();
        if (distributionPoint.getCRLIssuer() != null) {
            final GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < names.length; ++i) {
                if (names[i].getTagNo() == 4) {
                    try {
                        list.add(new X500Principal(names[i].getName().getDERObject().getEncoded()));
                    }
                    catch (IOException ex) {
                        throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", ex);
                    }
                }
            }
        }
        else {
            if (distributionPoint.getDistributionPoint() == null) {
                throw new AnnotatedException("CRL issuer is omitted from distribution point but no distributionPoint field present.");
            }
            final Iterator<X500Principal> iterator = collection.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            try {
                x509CRLSelector.addIssuerName(iterator2.next().getEncoded());
                continue;
            }
            catch (IOException ex2) {
                throw new AnnotatedException("Cannot decode CRL issuer information.", ex2);
            }
            break;
        }
    }
    
    private static BigInteger getSerialNumber(final Object o) {
        if (o instanceof X509Certificate) {
            return ((X509Certificate)o).getSerialNumber();
        }
        return ((X509AttributeCertificate)o).getSerialNumber();
    }
    
    protected static void getCertStatus(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus) throws AnnotatedException {
        X509CRLObject x509CRLObject;
        try {
            x509CRLObject = new X509CRLObject(new CertificateList((ASN1Sequence)ASN1Object.fromByteArray(x509CRL.getEncoded())));
        }
        catch (Exception ex) {
            throw new AnnotatedException("Bouncy Castle X509CRLObject could not be created.", ex);
        }
        final X509CRLEntryObject x509CRLEntryObject = (X509CRLEntryObject)x509CRLObject.getRevokedCertificate(getSerialNumber(o));
        if (x509CRLEntryObject != null && (getEncodedIssuerPrincipal(o).equals(x509CRLEntryObject.getCertificateIssuer()) || getEncodedIssuerPrincipal(o).equals(getIssuerPrincipal(x509CRL)))) {
            DEREnumerated instance = null;
            if (x509CRLEntryObject.hasExtensions()) {
                try {
                    instance = DEREnumerated.getInstance(getExtensionValue(x509CRLEntryObject, X509Extensions.ReasonCode.getId()));
                }
                catch (Exception ex2) {
                    new AnnotatedException("Reason code CRL entry extension could not be decoded.", ex2);
                }
            }
            if (date.getTime() >= x509CRLEntryObject.getRevocationDate().getTime() || instance == null || instance.getValue().intValue() == 0 || instance.getValue().intValue() == 1 || instance.getValue().intValue() == 2 || instance.getValue().intValue() == 8) {
                if (instance != null) {
                    certStatus.setCertStatus(instance.getValue().intValue());
                }
                else {
                    certStatus.setCertStatus(0);
                }
                certStatus.setRevocationDate(x509CRLEntryObject.getRevocationDate());
            }
        }
    }
    
    protected static Set getDeltaCRLs(final Date dateAndTime, final ExtendedPKIXParameters extendedPKIXParameters, final X509CRL x509CRL) throws AnnotatedException {
        final X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        if (extendedPKIXParameters.getDate() != null) {
            x509CRLStoreSelector.setDateAndTime(extendedPKIXParameters.getDate());
        }
        else {
            x509CRLStoreSelector.setDateAndTime(dateAndTime);
        }
        try {
            x509CRLStoreSelector.addIssuerName(getIssuerPrincipal(x509CRL).getEncoded());
        }
        catch (IOException ex) {
            new AnnotatedException("Cannot extract issuer from CRL.", ex);
        }
        BigInteger positiveValue = null;
        try {
            final DERObject extensionValue = getExtensionValue(x509CRL, CertPathValidatorUtilities.CRL_NUMBER);
            if (extensionValue != null) {
                positiveValue = DERInteger.getInstance(extensionValue).getPositiveValue();
            }
        }
        catch (Exception ex2) {
            throw new AnnotatedException("CRL number extension could not be extracted from CRL.", ex2);
        }
        byte[] extensionValue2;
        try {
            extensionValue2 = x509CRL.getExtensionValue(CertPathValidatorUtilities.ISSUING_DISTRIBUTION_POINT);
        }
        catch (Exception ex3) {
            throw new AnnotatedException("Issuing distribution point extension value could not be read.", ex3);
        }
        x509CRLStoreSelector.setMinCRLNumber((positiveValue == null) ? null : positiveValue.add(BigInteger.valueOf(1L)));
        x509CRLStoreSelector.setIssuingDistributionPoint(extensionValue2);
        x509CRLStoreSelector.setIssuingDistributionPointEnabled(true);
        x509CRLStoreSelector.setMaxBaseCRLNumber(positiveValue);
        final HashSet<X509CRL> set = new HashSet<X509CRL>();
        try {
            set.addAll((Collection<?>)findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            set.addAll((Collection<?>)findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            set.addAll((Collection<?>)findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
        }
        catch (AnnotatedException ex4) {
            throw new AnnotatedException("Could not search for delta CRLs.", ex4);
        }
        final HashSet<X509CRL> set2 = new HashSet<X509CRL>();
        for (final X509CRL x509CRL2 : set) {
            if (isDeltaCRL(x509CRL2)) {
                set2.add(x509CRL2);
            }
        }
        return set2;
    }
    
    private static boolean isDeltaCRL(final X509CRL x509CRL) {
        return x509CRL.getCriticalExtensionOIDs().contains(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
    }
    
    protected static Set getCompleteCRLs(final DistributionPoint distributionPoint, final Object o, final Date dateAndTime, final ExtendedPKIXParameters extendedPKIXParameters) throws AnnotatedException {
        final X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            final HashSet<X500Principal> set = new HashSet<X500Principal>();
            if (o instanceof X509AttributeCertificate) {
                set.add((X500Principal)((X509AttributeCertificate)o).getIssuer().getPrincipals()[0]);
            }
            else {
                set.add(getEncodedIssuerPrincipal(o));
            }
            getCRLIssuersFromDistributionPoint(distributionPoint, set, x509CRLStoreSelector, extendedPKIXParameters);
        }
        catch (AnnotatedException ex) {
            new AnnotatedException("Could not get issuer information from distribution point.", ex);
        }
        if (o instanceof X509Certificate) {
            x509CRLStoreSelector.setCertificateChecking((X509Certificate)o);
        }
        else if (o instanceof X509AttributeCertificate) {
            x509CRLStoreSelector.setAttrCertificateChecking((X509AttributeCertificate)o);
        }
        if (extendedPKIXParameters.getDate() != null) {
            x509CRLStoreSelector.setDateAndTime(extendedPKIXParameters.getDate());
        }
        else {
            x509CRLStoreSelector.setDateAndTime(dateAndTime);
        }
        x509CRLStoreSelector.setCompleteCRLEnabled(true);
        final HashSet set2 = new HashSet();
        try {
            set2.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            set2.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            set2.addAll(findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
        }
        catch (AnnotatedException ex2) {
            throw new AnnotatedException("Could not search for CRLs.", ex2);
        }
        if (!set2.isEmpty()) {
            return set2;
        }
        if (o instanceof X509AttributeCertificate) {
            throw new AnnotatedException("No CRLs found for issuer \"" + ((X509AttributeCertificate)o).getIssuer().getPrincipals()[0] + "\"");
        }
        throw new AnnotatedException("No CRLs found for issuer \"" + ((X509Certificate)o).getIssuerX500Principal() + "\"");
    }
    
    protected static Date getValidCertDateFromValidityModel(final ExtendedPKIXParameters extendedPKIXParameters, final CertPath certPath, final int n) throws AnnotatedException {
        if (extendedPKIXParameters.getValidityModel() != 1) {
            return getValidDate(extendedPKIXParameters);
        }
        if (n <= 0) {
            return getValidDate(extendedPKIXParameters);
        }
        if (n - 1 == 0) {
            DERGeneralizedTime instance = null;
            try {
                final byte[] extensionValue = ((X509Certificate)certPath.getCertificates().get(n - 1)).getExtensionValue(ISISMTTObjectIdentifiers.id_isismtt_at_dateOfCertGen.getId());
                if (extensionValue != null) {
                    instance = DERGeneralizedTime.getInstance(ASN1Object.fromByteArray(extensionValue));
                }
            }
            catch (IOException ex2) {
                throw new AnnotatedException("Date of cert gen extension could not be read.");
            }
            catch (IllegalArgumentException ex3) {
                throw new AnnotatedException("Date of cert gen extension could not be read.");
            }
            if (instance != null) {
                try {
                    return instance.getDate();
                }
                catch (ParseException ex) {
                    throw new AnnotatedException("Date from date of cert gen extension could not be parsed.", ex);
                }
            }
            return ((X509Certificate)certPath.getCertificates().get(n - 1)).getNotBefore();
        }
        return ((X509Certificate)certPath.getCertificates().get(n - 1)).getNotBefore();
    }
    
    protected static PublicKey getNextWorkingKey(final List list, final int n) throws CertPathValidatorException {
        final PublicKey publicKey = list.get(n).getPublicKey();
        if (!(publicKey instanceof DSAPublicKey)) {
            return publicKey;
        }
        final DSAPublicKey dsaPublicKey = (DSAPublicKey)publicKey;
        if (dsaPublicKey.getParams() != null) {
            return dsaPublicKey;
        }
        for (int i = n + 1; i < list.size(); ++i) {
            final PublicKey publicKey2 = list.get(i).getPublicKey();
            if (!(publicKey2 instanceof DSAPublicKey)) {
                throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
            }
            final DSAPublicKey dsaPublicKey2 = (DSAPublicKey)publicKey2;
            if (dsaPublicKey2.getParams() != null) {
                final DSAParams params = dsaPublicKey2.getParams();
                final DSAPublicKeySpec keySpec = new DSAPublicKeySpec(dsaPublicKey.getY(), params.getP(), params.getQ(), params.getG());
                try {
                    return KeyFactory.getInstance("DSA", "BC").generatePublic(keySpec);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
    }
    
    protected static Collection findIssuerCerts(final X509Certificate x509Certificate, final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters) throws AnnotatedException {
        final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
        final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
        try {
            x509CertStoreSelector.setSubject(x509Certificate.getIssuerX500Principal().getEncoded());
        }
        catch (IOException ex) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate could not be set.", ex);
        }
        Iterator<Object> iterator;
        try {
            final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
            list.addAll((Collection<?>)findCertificates(x509CertStoreSelector, extendedPKIXBuilderParameters.getCertStores()));
            list.addAll((Collection<?>)findCertificates(x509CertStoreSelector, extendedPKIXBuilderParameters.getStores()));
            list.addAll((Collection<?>)findCertificates(x509CertStoreSelector, extendedPKIXBuilderParameters.getAdditionalStores()));
            iterator = list.iterator();
        }
        catch (AnnotatedException ex2) {
            throw new AnnotatedException("Issuer certificate cannot be searched.", ex2);
        }
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }
    
    protected static void verifyX509Certificate(final X509Certificate x509Certificate, final PublicKey publicKey, final String s) throws GeneralSecurityException {
        if (s == null) {
            x509Certificate.verify(publicKey);
        }
        else {
            x509Certificate.verify(publicKey, s);
        }
    }
    
    static {
        CERTIFICATE_POLICIES = X509Extensions.CertificatePolicies.getId();
        BASIC_CONSTRAINTS = X509Extensions.BasicConstraints.getId();
        POLICY_MAPPINGS = X509Extensions.PolicyMappings.getId();
        SUBJECT_ALTERNATIVE_NAME = X509Extensions.SubjectAlternativeName.getId();
        NAME_CONSTRAINTS = X509Extensions.NameConstraints.getId();
        KEY_USAGE = X509Extensions.KeyUsage.getId();
        INHIBIT_ANY_POLICY = X509Extensions.InhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = X509Extensions.IssuingDistributionPoint.getId();
        DELTA_CRL_INDICATOR = X509Extensions.DeltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = X509Extensions.PolicyConstraints.getId();
        FRESHEST_CRL = X509Extensions.FreshestCRL.getId();
        CRL_DISTRIBUTION_POINTS = X509Extensions.CRLDistributionPoints.getId();
        AUTHORITY_KEY_IDENTIFIER = X509Extensions.AuthorityKeyIdentifier.getId();
        CRL_NUMBER = X509Extensions.CRLNumber.getId();
        crlReasons = new String[] { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
    }
}
