// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.PKIXCertPathChecker;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1TaggedObject;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import java.security.GeneralSecurityException;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.cert.PolicyNode;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashMap;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import java.security.cert.CertPath;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import java.util.Date;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.HashSet;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathParameters;
import java.security.cert.PKIXParameters;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import java.security.cert.CertSelector;
import java.security.cert.CertPathBuilder;
import java.util.Collection;
import org.bouncycastle.x509.X509CertStoreSelector;
import java.util.Set;
import java.util.List;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.security.PublicKey;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.util.Arrays;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.DistributionPointName;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import java.io.IOException;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralName;
import java.util.ArrayList;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import java.security.cert.X509Extension;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.x509.DistributionPoint;

public class RFC3280CertPathUtilities
{
    protected static final String CERTIFICATE_POLICIES;
    protected static final String POLICY_MAPPINGS;
    protected static final String INHIBIT_ANY_POLICY;
    protected static final String ISSUING_DISTRIBUTION_POINT;
    protected static final String FRESHEST_CRL;
    protected static final String DELTA_CRL_INDICATOR;
    protected static final String POLICY_CONSTRAINTS;
    protected static final String BASIC_CONSTRAINTS;
    protected static final String CRL_DISTRIBUTION_POINTS;
    protected static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String NAME_CONSTRAINTS;
    protected static final String AUTHORITY_KEY_IDENTIFIER;
    protected static final String KEY_USAGE;
    protected static final String CRL_NUMBER;
    protected static final String ANY_POLICY = "2.5.29.32.0";
    protected static final int KEY_CERT_SIGN = 5;
    protected static final int CRL_SIGN = 6;
    protected static final String[] crlReasons;
    
    protected static void processCRLB2(final DistributionPoint distributionPoint, final Object o, final X509CRL x509CRL) throws AnnotatedException {
        IssuingDistributionPoint instance;
        try {
            instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
        }
        catch (Exception ex) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex);
        }
        if (instance != null) {
            if (instance.getDistributionPoint() != null) {
                final DistributionPointName distributionPoint2 = IssuingDistributionPoint.getInstance(instance).getDistributionPoint();
                final ArrayList<GeneralName> list = new ArrayList<GeneralName>();
                if (distributionPoint2.getType() == 0) {
                    final GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                    for (int i = 0; i < names.length; ++i) {
                        list.add(names[i]);
                    }
                }
                if (distributionPoint2.getType() == 1) {
                    final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                    try {
                        final Enumeration objects = ASN1Sequence.getInstance(ASN1Object.fromByteArray(CertPathValidatorUtilities.getIssuerPrincipal(x509CRL).getEncoded())).getObjects();
                        while (objects.hasMoreElements()) {
                            asn1EncodableVector.add(objects.nextElement());
                        }
                    }
                    catch (IOException ex2) {
                        throw new AnnotatedException("Could not read CRL issuer.", ex2);
                    }
                    asn1EncodableVector.add(distributionPoint2.getName());
                    list.add(new GeneralName(X509Name.getInstance(new DERSequence(asn1EncodableVector))));
                }
                int n = 0;
                if (distributionPoint.getDistributionPoint() != null) {
                    final DistributionPointName distributionPoint3 = distributionPoint.getDistributionPoint();
                    GeneralName[] array = null;
                    if (distributionPoint3.getType() == 0) {
                        array = GeneralNames.getInstance(distributionPoint3.getName()).getNames();
                    }
                    if (distributionPoint3.getType() == 1) {
                        if (distributionPoint.getCRLIssuer() != null) {
                            array = distributionPoint.getCRLIssuer().getNames();
                        }
                        else {
                            array = new GeneralName[] { null };
                            try {
                                array[0] = new GeneralName(new X509Name((ASN1Sequence)ASN1Object.fromByteArray(CertPathValidatorUtilities.getEncodedIssuerPrincipal(o).getEncoded())));
                            }
                            catch (IOException ex3) {
                                throw new AnnotatedException("Could not read certificate issuer.", ex3);
                            }
                        }
                        for (int j = 0; j < array.length; ++j) {
                            final Enumeration objects2 = ASN1Sequence.getInstance(array[j].getName().getDERObject()).getObjects();
                            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
                            while (objects2.hasMoreElements()) {
                                asn1EncodableVector2.add(objects2.nextElement());
                            }
                            asn1EncodableVector2.add(distributionPoint3.getName());
                            array[j] = new GeneralName(new X509Name(new DERSequence(asn1EncodableVector2)));
                        }
                    }
                    if (array != null) {
                        for (int k = 0; k < array.length; ++k) {
                            if (list.contains(array[k])) {
                                n = 1;
                                break;
                            }
                        }
                    }
                    if (n == 0) {
                        throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                    }
                }
                else {
                    if (distributionPoint.getCRLIssuer() == null) {
                        throw new AnnotatedException("Either the cRLIssuer or the distributionPoint field must be contained in DistributionPoint.");
                    }
                    final GeneralName[] names2 = distributionPoint.getCRLIssuer().getNames();
                    for (int l = 0; l < names2.length; ++l) {
                        if (list.contains(names2[l])) {
                            n = 1;
                            break;
                        }
                    }
                    if (n == 0) {
                        throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                    }
                }
            }
            BasicConstraints instance2;
            try {
                instance2 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)o, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
            }
            catch (Exception ex4) {
                throw new AnnotatedException("Basic constraints extension could not be decoded.", ex4);
            }
            if (o instanceof X509Certificate) {
                if (instance.onlyContainsUserCerts() && instance2 != null && instance2.isCA()) {
                    throw new AnnotatedException("CA Cert CRL only contains user certificates.");
                }
                if (instance.onlyContainsCACerts() && (instance2 == null || !instance2.isCA())) {
                    throw new AnnotatedException("End CRL only contains CA certificates.");
                }
            }
            if (instance.onlyContainsAttributeCerts()) {
                throw new AnnotatedException("onlyContainsAttributeCerts boolean is asserted.");
            }
        }
    }
    
    protected static void processCRLB1(final DistributionPoint distributionPoint, final Object o, final X509CRL x509CRL) throws AnnotatedException {
        final DERObject extensionValue = CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
        boolean b = false;
        if (extensionValue != null && IssuingDistributionPoint.getInstance(extensionValue).isIndirectCRL()) {
            b = true;
        }
        final byte[] encoded = CertPathValidatorUtilities.getIssuerPrincipal(x509CRL).getEncoded();
        int n = 0;
        if (distributionPoint.getCRLIssuer() != null) {
            final GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < names.length; ++i) {
                if (names[i].getTagNo() == 4) {
                    try {
                        if (Arrays.areEqual(names[i].getName().getDERObject().getEncoded(), encoded)) {
                            n = 1;
                        }
                    }
                    catch (IOException ex) {
                        throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", ex);
                    }
                }
            }
            if (n != 0 && !b) {
                throw new AnnotatedException("Distribution point contains cRLIssuer field but CRL is not indirect.");
            }
            if (n == 0) {
                throw new AnnotatedException("CRL issuer of CRL does not match CRL issuer of distribution point.");
            }
        }
        else if (CertPathValidatorUtilities.getIssuerPrincipal(x509CRL).equals(CertPathValidatorUtilities.getEncodedIssuerPrincipal(o))) {
            n = 1;
        }
        if (n == 0) {
            throw new AnnotatedException("Cannot find matching CRL issuer for certificate.");
        }
    }
    
    protected static ReasonsMask processCRLD(final X509CRL x509CRL, final DistributionPoint distributionPoint) throws AnnotatedException {
        IssuingDistributionPoint instance;
        try {
            instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
        }
        catch (Exception ex) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex);
        }
        if (instance != null && instance.getOnlySomeReasons() != null && distributionPoint.getReasons() != null) {
            return new ReasonsMask(distributionPoint.getReasons().intValue()).intersect(new ReasonsMask(instance.getOnlySomeReasons().intValue()));
        }
        if ((instance == null || instance.getOnlySomeReasons() == null) && distributionPoint.getReasons() == null) {
            return ReasonsMask.allReasons;
        }
        return ((distributionPoint.getReasons() == null) ? ReasonsMask.allReasons : new ReasonsMask(distributionPoint.getReasons().intValue())).intersect((instance == null) ? ReasonsMask.allReasons : new ReasonsMask(instance.getOnlySomeReasons().intValue()));
    }
    
    protected static Set processCRLF(final X509CRL x509CRL, final Object o, final X509Certificate other, final PublicKey publicKey, final ExtendedPKIXParameters extendedPKIXParameters, final List list) throws AnnotatedException {
        final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
        try {
            x509CertStoreSelector.setSubject(CertPathValidatorUtilities.getIssuerPrincipal(x509CRL).getEncoded());
        }
        catch (IOException ex) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate for CRL could not be set.", ex);
        }
        Collection certificates;
        try {
            certificates = CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXParameters.getStores());
            certificates.addAll(CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            certificates.addAll(CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXParameters.getCertStores()));
        }
        catch (AnnotatedException ex2) {
            throw new AnnotatedException("Issuer certificate for CRL cannot be searched.", ex2);
        }
        certificates.add(other);
        final Iterator<X509Certificate> iterator = certificates.iterator();
        final ArrayList<X509Certificate> list2 = new ArrayList<X509Certificate>();
        final ArrayList<PublicKey> list3 = new ArrayList<PublicKey>();
        while (iterator.hasNext()) {
            final X509Certificate certificate = iterator.next();
            if (certificate.equals(other)) {
                list2.add(certificate);
                list3.add(publicKey);
            }
            else {
                try {
                    final CertPathBuilder instance = CertPathBuilder.getInstance("PKIX", "BC");
                    final X509CertStoreSelector targetCertConstraints = new X509CertStoreSelector();
                    targetCertConstraints.setCertificate(certificate);
                    final ExtendedPKIXParameters extendedPKIXParameters2 = (ExtendedPKIXParameters)extendedPKIXParameters.clone();
                    extendedPKIXParameters2.setTargetCertConstraints(targetCertConstraints);
                    final ExtendedPKIXBuilderParameters params = (ExtendedPKIXBuilderParameters)ExtendedPKIXBuilderParameters.getInstance(extendedPKIXParameters2);
                    if (list.contains(certificate)) {
                        params.setRevocationEnabled(false);
                    }
                    else {
                        params.setRevocationEnabled(true);
                    }
                    final List<? extends Certificate> certificates2 = instance.build(params).getCertPath().getCertificates();
                    list2.add(certificate);
                    list3.add(CertPathValidatorUtilities.getNextWorkingKey(certificates2, 0));
                }
                catch (CertPathBuilderException ex3) {
                    throw new AnnotatedException("Internal error.", ex3);
                }
                catch (CertPathValidatorException ex4) {
                    throw new AnnotatedException("Public key of issuer certificate of CRL could not be retrieved.", ex4);
                }
                catch (Exception ex5) {
                    throw new RuntimeException(ex5.getMessage());
                }
            }
        }
        final HashSet<PublicKey> set = new HashSet<PublicKey>();
        Object o2 = null;
        for (int i = 0; i < list2.size(); ++i) {
            final boolean[] keyUsage = list2.get(i).getKeyUsage();
            if (keyUsage != null && (keyUsage.length < 7 || !keyUsage[6])) {
                o2 = new AnnotatedException("Issuer certificate key usage extension does not permit CRL signing.");
            }
            else {
                set.add((PublicKey)list3.get(i));
            }
        }
        if (set.isEmpty() && o2 == null) {
            throw new AnnotatedException("Cannot find a valid issuer certificate.");
        }
        if (set.isEmpty() && o2 != null) {
            throw o2;
        }
        return set;
    }
    
    protected static PublicKey processCRLG(final X509CRL x509CRL, final Set set) throws AnnotatedException {
        Throwable t = null;
        for (final PublicKey publicKey : set) {
            try {
                x509CRL.verify(publicKey);
                return publicKey;
            }
            catch (Exception ex) {
                t = ex;
                continue;
            }
            break;
        }
        throw new AnnotatedException("Cannot verify CRL.", t);
    }
    
    protected static X509CRL processCRLH(final Set set, final PublicKey publicKey) throws AnnotatedException {
        Throwable t = null;
        for (final X509CRL x509CRL : set) {
            try {
                x509CRL.verify(publicKey);
                return x509CRL;
            }
            catch (Exception ex) {
                t = ex;
                continue;
            }
            break;
        }
        if (t != null) {
            throw new AnnotatedException("Cannot verify delta CRL.", t);
        }
        return null;
    }
    
    protected static Set processCRLA1i(final Date date, final ExtendedPKIXParameters extendedPKIXParameters, final X509Certificate x509Certificate, final X509CRL x509CRL) throws AnnotatedException {
        final HashSet set = new HashSet();
        if (extendedPKIXParameters.isUseDeltasEnabled()) {
            CRLDistPoint crlDistPoint;
            try {
                crlDistPoint = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.FRESHEST_CRL));
            }
            catch (AnnotatedException ex) {
                throw new AnnotatedException("Freshest CRL extension could not be decoded from certificate.", ex);
            }
            if (crlDistPoint == null) {
                try {
                    crlDistPoint = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.FRESHEST_CRL));
                }
                catch (AnnotatedException ex2) {
                    throw new AnnotatedException("Freshest CRL extension could not be decoded from CRL.", ex2);
                }
            }
            if (crlDistPoint != null) {
                try {
                    CertPathValidatorUtilities.addAdditionalStoresFromCRLDistributionPoint(crlDistPoint, extendedPKIXParameters);
                }
                catch (AnnotatedException ex3) {
                    throw new AnnotatedException("No new delta CRL locations could be added from Freshest CRL extension.", ex3);
                }
                try {
                    set.addAll(CertPathValidatorUtilities.getDeltaCRLs(date, extendedPKIXParameters, x509CRL));
                }
                catch (AnnotatedException ex4) {
                    throw new AnnotatedException("Exception obtaining delta CRLs.", ex4);
                }
            }
        }
        return set;
    }
    
    protected static Set[] processCRLA1ii(final Date dateAndTime, final ExtendedPKIXParameters extendedPKIXParameters, final X509Certificate certificateChecking, final X509CRL x509CRL) throws AnnotatedException {
        final HashSet set = new HashSet();
        final HashSet set2 = new HashSet();
        final X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        x509CRLStoreSelector.setCertificateChecking(certificateChecking);
        if (extendedPKIXParameters.getDate() != null) {
            x509CRLStoreSelector.setDateAndTime(extendedPKIXParameters.getDate());
        }
        else {
            x509CRLStoreSelector.setDateAndTime(dateAndTime);
        }
        try {
            x509CRLStoreSelector.addIssuerName(x509CRL.getIssuerX500Principal().getEncoded());
        }
        catch (IOException obj) {
            throw new AnnotatedException("Cannot extract issuer from CRL." + obj, obj);
        }
        x509CRLStoreSelector.setCompleteCRLEnabled(true);
        try {
            set.addAll(CertPathValidatorUtilities.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            set.addAll(CertPathValidatorUtilities.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            set.addAll(CertPathValidatorUtilities.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
        }
        catch (AnnotatedException ex) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", ex);
        }
        if (extendedPKIXParameters.isUseDeltasEnabled()) {
            try {
                set2.addAll(CertPathValidatorUtilities.getDeltaCRLs(dateAndTime, extendedPKIXParameters, x509CRL));
            }
            catch (AnnotatedException ex2) {
                throw new AnnotatedException("Exception obtaining delta CRLs.", ex2);
            }
        }
        return new Set[] { set, set2 };
    }
    
    protected static void processCRLC(final X509CRL x509CRL, final X509CRL x509CRL2, final ExtendedPKIXParameters extendedPKIXParameters) throws AnnotatedException {
        if (x509CRL == null) {
            return;
        }
        IssuingDistributionPoint instance;
        try {
            instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL2, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
        }
        catch (Exception ex) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex);
        }
        if (extendedPKIXParameters.isUseDeltasEnabled()) {
            if (!x509CRL.getIssuerX500Principal().equals(x509CRL2.getIssuerX500Principal())) {
                throw new AnnotatedException("Complete CRL issuer does not match delta CRL issuer.");
            }
            IssuingDistributionPoint instance2;
            try {
                instance2 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
            }
            catch (Exception ex2) {
                throw new AnnotatedException("Issuing distribution point extension from delta CRL could not be decoded.", ex2);
            }
            boolean b = false;
            if (instance == null) {
                if (instance2 == null) {
                    b = true;
                }
            }
            else if (instance.equals(instance2)) {
                b = true;
            }
            if (!b) {
                throw new AnnotatedException("Issuing distribution point extension from delta CRL and complete CRL does not match.");
            }
            DERObject extensionValue;
            try {
                extensionValue = CertPathValidatorUtilities.getExtensionValue(x509CRL2, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
            }
            catch (AnnotatedException ex3) {
                throw new AnnotatedException("Authority key identifier extension could not be extracted from complete CRL.", ex3);
            }
            DERObject extensionValue2;
            try {
                extensionValue2 = CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
            }
            catch (AnnotatedException ex4) {
                throw new AnnotatedException("Authority key identifier extension could not be extracted from delta CRL.", ex4);
            }
            if (extensionValue == null) {
                throw new AnnotatedException("CRL authority key identifier is null.");
            }
            if (extensionValue2 == null) {
                throw new AnnotatedException("Delta CRL authority key identifier is null.");
            }
            if (!extensionValue.equals(extensionValue2)) {
                throw new AnnotatedException("Delta CRL authority key identifier does not match complete CRL authority key identifier.");
            }
        }
    }
    
    protected static void processCRLI(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus, final ExtendedPKIXParameters extendedPKIXParameters) throws AnnotatedException {
        if (extendedPKIXParameters.isUseDeltasEnabled() && x509CRL != null) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, o, certStatus);
        }
    }
    
    protected static void processCRLJ(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus) throws AnnotatedException {
        if (certStatus.getCertStatus() == 11) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, o, certStatus);
        }
    }
    
    protected static PKIXPolicyNode prepareCertB(final CertPath certPath, final int index, final List[] array, final PKIXPolicyNode pkixPolicyNode, final int n) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(index);
        final int n2 = certificates.size() - index;
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_MAPPINGS));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", ex, certPath, index);
        }
        PKIXPolicyNode removePolicyNode = pkixPolicyNode;
        if (instance != null) {
            final ASN1Sequence asn1Sequence = instance;
            final HashMap<Object, HashSet<String>> hashMap = new HashMap<Object, HashSet<String>>();
            final HashSet<String> set = new HashSet<String>();
            for (int i = 0; i < asn1Sequence.size(); ++i) {
                final ASN1Sequence asn1Sequence2 = (ASN1Sequence)asn1Sequence.getObjectAt(i);
                final String id = ((DERObjectIdentifier)asn1Sequence2.getObjectAt(0)).getId();
                final String id2 = ((DERObjectIdentifier)asn1Sequence2.getObjectAt(1)).getId();
                if (!hashMap.containsKey(id)) {
                    final HashSet<String> set2 = new HashSet<String>();
                    set2.add(id2);
                    hashMap.put(id, set2);
                    set.add(id);
                }
                else {
                    hashMap.get(id).add(id2);
                }
            }
            for (final String s : set) {
                if (n > 0) {
                    boolean b = false;
                    for (final PKIXPolicyNode pkixPolicyNode2 : array[n2]) {
                        if (pkixPolicyNode2.getValidPolicy().equals(s)) {
                            b = true;
                            pkixPolicyNode2.expectedPolicies = hashMap.get(s);
                            break;
                        }
                    }
                    if (b) {
                        continue;
                    }
                    for (final PKIXPolicyNode pkixPolicyNode3 : array[n2]) {
                        if ("2.5.29.32.0".equals(pkixPolicyNode3.getValidPolicy())) {
                            Set qualifierSet = null;
                            ASN1Sequence asn1Sequence3;
                            try {
                                asn1Sequence3 = (ASN1Sequence)CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                            }
                            catch (AnnotatedException ex2) {
                                throw new ExtCertPathValidatorException("Certificate policies extension could not be decoded.", ex2, certPath, index);
                            }
                            final Enumeration objects = asn1Sequence3.getObjects();
                            while (objects.hasMoreElements()) {
                                PolicyInformation instance2;
                                try {
                                    instance2 = PolicyInformation.getInstance(objects.nextElement());
                                }
                                catch (Exception cause) {
                                    throw new CertPathValidatorException("Policy information could not be decoded.", cause, certPath, index);
                                }
                                if ("2.5.29.32.0".equals(instance2.getPolicyIdentifier().getId())) {
                                    try {
                                        qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                                        break;
                                    }
                                    catch (CertPathValidatorException ex3) {
                                        throw new ExtCertPathValidatorException("Policy qualifier info set could not be decoded.", ex3, certPath, index);
                                    }
                                }
                            }
                            boolean contains = false;
                            if (x509Certificate.getCriticalExtensionOIDs() != null) {
                                contains = x509Certificate.getCriticalExtensionOIDs().contains(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                            }
                            final PKIXPolicyNode pkixPolicyNode4 = (PKIXPolicyNode)pkixPolicyNode3.getParent();
                            if ("2.5.29.32.0".equals(pkixPolicyNode4.getValidPolicy())) {
                                final PKIXPolicyNode pkixPolicyNode5 = new PKIXPolicyNode(new ArrayList(), n2, hashMap.get(s), pkixPolicyNode4, qualifierSet, s, contains);
                                pkixPolicyNode4.addChild(pkixPolicyNode5);
                                array[n2].add(pkixPolicyNode5);
                                break;
                            }
                            break;
                        }
                    }
                }
                else {
                    if (n > 0) {
                        continue;
                    }
                    final Iterator iterator4 = array[n2].iterator();
                    while (iterator4.hasNext()) {
                        final PKIXPolicyNode pkixPolicyNode6 = iterator4.next();
                        if (pkixPolicyNode6.getValidPolicy().equals(s)) {
                            ((PKIXPolicyNode)pkixPolicyNode6.getParent()).removeChild(pkixPolicyNode6);
                            iterator4.remove();
                            for (int j = n2 - 1; j >= 0; --j) {
                                final List list = array[j];
                                for (int k = 0; k < list.size(); ++k) {
                                    final PKIXPolicyNode pkixPolicyNode7 = list.get(k);
                                    if (!pkixPolicyNode7.hasChildren()) {
                                        removePolicyNode = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode7);
                                        if (removePolicyNode == null) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return removePolicyNode;
    }
    
    protected static void prepareNextCertA(final CertPath certPath, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_MAPPINGS));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final ASN1Sequence asn1Sequence = instance;
            for (int i = 0; i < asn1Sequence.size(); ++i) {
                DERObjectIdentifier instance3;
                DERObjectIdentifier instance4;
                try {
                    final ASN1Sequence instance2 = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(i));
                    instance3 = DERObjectIdentifier.getInstance(instance2.getObjectAt(0));
                    instance4 = DERObjectIdentifier.getInstance(instance2.getObjectAt(1));
                }
                catch (Exception ex2) {
                    throw new ExtCertPathValidatorException("Policy mappings extension contents could not be decoded.", ex2, certPath, n);
                }
                if ("2.5.29.32.0".equals(instance3.getId())) {
                    throw new CertPathValidatorException("IssuerDomainPolicy is anyPolicy", null, certPath, n);
                }
                if ("2.5.29.32.0".equals(instance4.getId())) {
                    throw new CertPathValidatorException("SubjectDomainPolicy is anyPolicy,", null, certPath, n);
                }
            }
        }
    }
    
    protected static void processCertF(final CertPath certPath, final int n, final PKIXPolicyNode pkixPolicyNode, final int n2) throws CertPathValidatorException {
        if (n2 <= 0 && pkixPolicyNode == null) {
            throw new ExtCertPathValidatorException("No valid policy tree found when one expected.", null, certPath, n);
        }
    }
    
    protected static PKIXPolicyNode processCertE(final CertPath certPath, final int n, PKIXPolicyNode pkixPolicyNode) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", ex, certPath, n);
        }
        if (instance == null) {
            pkixPolicyNode = null;
        }
        return pkixPolicyNode;
    }
    
    protected static void processCertBC(final CertPath certPath, final int n, final PKIXNameConstraintValidator pkixNameConstraintValidator) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(n);
        final int size = certificates.size();
        final int n2 = size - n;
        if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate) || n2 >= size) {
            final ASN1InputStream asn1InputStream = new ASN1InputStream(CertPathValidatorUtilities.getSubjectPrincipal(x509Certificate).getEncoded());
            ASN1Sequence instance;
            try {
                instance = ASN1Sequence.getInstance(asn1InputStream.readObject());
            }
            catch (Exception cause) {
                throw new CertPathValidatorException("Exception extracting subject name when checking subtrees.", cause, certPath, n);
            }
            try {
                pkixNameConstraintValidator.checkPermittedDN(instance);
                pkixNameConstraintValidator.checkExcludedDN(instance);
            }
            catch (PKIXNameConstraintValidatorException cause2) {
                throw new CertPathValidatorException("Subtree check for certificate subject failed.", cause2, certPath, n);
            }
            GeneralNames instance2;
            try {
                instance2 = GeneralNames.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME));
            }
            catch (Exception cause3) {
                throw new CertPathValidatorException("Subject alternative name extension could not be decoded.", cause3, certPath, n);
            }
            final Enumeration elements = new X509Name(instance).getValues(X509Name.EmailAddress).elements();
            while (elements.hasMoreElements()) {
                final GeneralName generalName = new GeneralName(1, elements.nextElement());
                try {
                    pkixNameConstraintValidator.checkPermitted(generalName);
                    pkixNameConstraintValidator.checkExcluded(generalName);
                }
                catch (PKIXNameConstraintValidatorException cause4) {
                    throw new CertPathValidatorException("Subtree check for certificate subject alternative email failed.", cause4, certPath, n);
                }
            }
            if (instance2 != null) {
                GeneralName[] names;
                try {
                    names = instance2.getNames();
                }
                catch (Exception cause5) {
                    throw new CertPathValidatorException("Subject alternative name contents could not be decoded.", cause5, certPath, n);
                }
                for (int i = 0; i < names.length; ++i) {
                    try {
                        pkixNameConstraintValidator.checkPermitted(names[i]);
                        pkixNameConstraintValidator.checkExcluded(names[i]);
                    }
                    catch (PKIXNameConstraintValidatorException cause6) {
                        throw new CertPathValidatorException("Subtree check for certificate subject alternative name failed.", cause6, certPath, n);
                    }
                }
            }
        }
    }
    
    protected static PKIXPolicyNode processCertD(final CertPath certPath, final int n, final Set set, final PKIXPolicyNode pkixPolicyNode, final List[] array, final int n2) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(n);
        final int size = certificates.size();
        final int n3 = size - n;
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", ex, certPath, n);
        }
        if (instance != null && pkixPolicyNode != null) {
            final Enumeration objects = instance.getObjects();
            final HashSet<String> set2 = new HashSet<String>();
            while (objects.hasMoreElements()) {
                final PolicyInformation instance2 = PolicyInformation.getInstance(objects.nextElement());
                final DERObjectIdentifier policyIdentifier = instance2.getPolicyIdentifier();
                set2.add(policyIdentifier.getId());
                if (!"2.5.29.32.0".equals(policyIdentifier.getId())) {
                    Set qualifierSet;
                    try {
                        qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                    }
                    catch (CertPathValidatorException ex2) {
                        throw new ExtCertPathValidatorException("Policy qualifier info set could not be build.", ex2, certPath, n);
                    }
                    if (CertPathValidatorUtilities.processCertD1i(n3, array, policyIdentifier, qualifierSet)) {
                        continue;
                    }
                    CertPathValidatorUtilities.processCertD1ii(n3, array, policyIdentifier, qualifierSet);
                }
            }
            if (set.isEmpty() || set.contains("2.5.29.32.0")) {
                set.clear();
                set.addAll(set2);
            }
            else {
                final Iterator<Object> iterator = set.iterator();
                final HashSet<Object> set3 = new HashSet<Object>();
                while (iterator.hasNext()) {
                    final Object next = iterator.next();
                    if (set2.contains(next)) {
                        set3.add(next);
                    }
                }
                set.clear();
                set.addAll(set3);
            }
            if (n2 > 0 || (n3 < size && CertPathValidatorUtilities.isSelfIssued(x509Certificate))) {
                final Enumeration objects2 = instance.getObjects();
                while (objects2.hasMoreElements()) {
                    final PolicyInformation instance3 = PolicyInformation.getInstance(objects2.nextElement());
                    if ("2.5.29.32.0".equals(instance3.getPolicyIdentifier().getId())) {
                        final Set qualifierSet2 = CertPathValidatorUtilities.getQualifierSet(instance3.getPolicyQualifiers());
                        final List list = array[n3 - 1];
                        for (int i = 0; i < list.size(); ++i) {
                            final PKIXPolicyNode pkixPolicyNode2 = list.get(i);
                            for (final String next2 : pkixPolicyNode2.getExpectedPolicies()) {
                                String id;
                                if (next2 instanceof String) {
                                    id = next2;
                                }
                                else {
                                    if (!(next2 instanceof DERObjectIdentifier)) {
                                        continue;
                                    }
                                    id = ((DERObjectIdentifier)next2).getId();
                                }
                                boolean b = false;
                                final Iterator children = pkixPolicyNode2.getChildren();
                                while (children.hasNext()) {
                                    if (id.equals(children.next().getValidPolicy())) {
                                        b = true;
                                    }
                                }
                                if (!b) {
                                    final HashSet<String> set4 = new HashSet<String>();
                                    set4.add(id);
                                    final PKIXPolicyNode pkixPolicyNode3 = new PKIXPolicyNode(new ArrayList(), n3, set4, pkixPolicyNode2, qualifierSet2, id, false);
                                    pkixPolicyNode2.addChild(pkixPolicyNode3);
                                    array[n3].add(pkixPolicyNode3);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            PKIXPolicyNode removePolicyNode = pkixPolicyNode;
            for (int j = n3 - 1; j >= 0; --j) {
                final List list2 = array[j];
                for (int k = 0; k < list2.size(); ++k) {
                    final PKIXPolicyNode pkixPolicyNode4 = list2.get(k);
                    if (!pkixPolicyNode4.hasChildren()) {
                        removePolicyNode = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode4);
                        if (removePolicyNode == null) {
                            break;
                        }
                    }
                }
            }
            final Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs != null) {
                final boolean contains = criticalExtensionOIDs.contains(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                final List list3 = array[n3];
                for (int l = 0; l < list3.size(); ++l) {
                    list3.get(l).setCritical(contains);
                }
            }
            return removePolicyNode;
        }
        return null;
    }
    
    protected static void processCertA(final CertPath certPath, final ExtendedPKIXParameters extendedPKIXParameters, final int n, final PublicKey publicKey, final boolean b, final X500Principal x500Principal, final X509Certificate x509Certificate) throws ExtCertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate2 = (X509Certificate)certificates.get(n);
        if (!b) {
            try {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, publicKey, extendedPKIXParameters.getSigProvider());
            }
            catch (GeneralSecurityException ex) {
                throw new ExtCertPathValidatorException("Could not validate certificate signature.", ex, certPath, n);
            }
        }
        try {
            x509Certificate2.checkValidity(CertPathValidatorUtilities.getValidCertDateFromValidityModel(extendedPKIXParameters, certPath, n));
        }
        catch (CertificateExpiredException ex2) {
            throw new ExtCertPathValidatorException("Could not validate certificate: " + ex2.getMessage(), ex2, certPath, n);
        }
        catch (CertificateNotYetValidException ex3) {
            throw new ExtCertPathValidatorException("Could not validate certificate: " + ex3.getMessage(), ex3, certPath, n);
        }
        catch (AnnotatedException ex4) {
            throw new ExtCertPathValidatorException("Could not validate time of certificate.", ex4, certPath, n);
        }
        if (extendedPKIXParameters.isRevocationEnabled()) {
            try {
                checkCRLs(extendedPKIXParameters, x509Certificate2, CertPathValidatorUtilities.getValidCertDateFromValidityModel(extendedPKIXParameters, certPath, n), x509Certificate, publicKey, certificates);
            }
            catch (AnnotatedException ex5) {
                Throwable cause = ex5;
                if (null != ex5.getCause()) {
                    cause = ex5.getCause();
                }
                throw new ExtCertPathValidatorException(ex5.getMessage(), cause, certPath, n);
            }
        }
        if (!CertPathValidatorUtilities.getEncodedIssuerPrincipal(x509Certificate2).equals(x500Principal)) {
            throw new ExtCertPathValidatorException("IssuerName(" + CertPathValidatorUtilities.getEncodedIssuerPrincipal(x509Certificate2) + ") does not match SubjectName(" + x500Principal + ") of signing certificate.", null, certPath, n);
        }
    }
    
    protected static int prepareNextCertI1(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                try {
                    final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                    if (instance2.getTagNo() != 0) {
                        continue;
                    }
                    final int intValue = DERInteger.getInstance(instance2).getValue().intValue();
                    if (intValue < n2) {
                        return intValue;
                    }
                }
                catch (IllegalArgumentException ex2) {
                    throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", ex2, certPath, n);
                }
                break;
            }
        }
        return n2;
    }
    
    protected static int prepareNextCertI2(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                try {
                    final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                    if (instance2.getTagNo() != 1) {
                        continue;
                    }
                    final int intValue = DERInteger.getInstance(instance2).getValue().intValue();
                    if (intValue < n2) {
                        return intValue;
                    }
                }
                catch (IllegalArgumentException ex2) {
                    throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", ex2, certPath, n);
                }
                break;
            }
        }
        return n2;
    }
    
    protected static void prepareNextCertG(final CertPath certPath, final int n, final PKIXNameConstraintValidator pkixNameConstraintValidator) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        NameConstraints nameConstraints = null;
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.NAME_CONSTRAINTS));
            if (instance != null) {
                nameConstraints = new NameConstraints(instance);
            }
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Name constraints extension could not be decoded.", ex, certPath, n);
        }
        if (nameConstraints != null) {
            final ASN1Sequence permittedSubtrees = nameConstraints.getPermittedSubtrees();
            if (permittedSubtrees != null) {
                try {
                    pkixNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                }
                catch (Exception ex2) {
                    throw new ExtCertPathValidatorException("Permitted subtrees cannot be build from name constraints extension.", ex2, certPath, n);
                }
            }
            final ASN1Sequence excludedSubtrees = nameConstraints.getExcludedSubtrees();
            if (excludedSubtrees != null) {
                final Enumeration objects = excludedSubtrees.getObjects();
                try {
                    while (objects.hasMoreElements()) {
                        pkixNameConstraintValidator.addExcludedSubtree(GeneralSubtree.getInstance(objects.nextElement()));
                    }
                }
                catch (Exception ex3) {
                    throw new ExtCertPathValidatorException("Excluded subtrees cannot be build from name constraints extension.", ex3, certPath, n);
                }
            }
        }
    }
    
    private static void checkCRL(final DistributionPoint distributionPoint, final ExtendedPKIXParameters extendedPKIXParameters, final X509Certificate x509Certificate, final Date date, final X509Certificate x509Certificate2, final PublicKey publicKey, final CertStatus certStatus, final ReasonsMask reasonsMask, final List list) throws AnnotatedException {
        final Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        final Set completeCRLs = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509Certificate, date2, extendedPKIXParameters);
        boolean b = false;
        AnnotatedException ex = null;
        final Iterator<X509CRL> iterator = completeCRLs.iterator();
        while (iterator.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
            try {
                final X509CRL x509CRL = iterator.next();
                final ReasonsMask processCRLD = processCRLD(x509CRL, distributionPoint);
                if (!processCRLD.hasNewReasons(reasonsMask)) {
                    continue;
                }
                final PublicKey processCRLG = processCRLG(x509CRL, processCRLF(x509CRL, x509Certificate, x509Certificate2, publicKey, extendedPKIXParameters, list));
                X509CRL processCRLH = null;
                if (extendedPKIXParameters.isUseDeltasEnabled()) {
                    processCRLH = processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date2, extendedPKIXParameters, x509CRL), processCRLG);
                }
                if (extendedPKIXParameters.getValidityModel() != 1 && x509Certificate.getNotAfter().getTime() < x509CRL.getThisUpdate().getTime()) {
                    throw new AnnotatedException("No valid CRL for current time found.");
                }
                processCRLB1(distributionPoint, x509Certificate, x509CRL);
                processCRLB2(distributionPoint, x509Certificate, x509CRL);
                processCRLC(processCRLH, x509CRL, extendedPKIXParameters);
                processCRLI(date, processCRLH, x509Certificate, certStatus, extendedPKIXParameters);
                processCRLJ(date, x509CRL, x509Certificate, certStatus);
                if (certStatus.getCertStatus() == 8) {
                    certStatus.setCertStatus(11);
                }
                reasonsMask.addReasons(processCRLD);
                final Set<String> criticalExtensionOIDs = x509CRL.getCriticalExtensionOIDs();
                if (criticalExtensionOIDs != null) {
                    final HashSet set = new HashSet(criticalExtensionOIDs);
                    set.remove(X509Extensions.IssuingDistributionPoint.getId());
                    set.remove(X509Extensions.DeltaCRLIndicator.getId());
                    if (!set.isEmpty()) {
                        throw new AnnotatedException("CRL contains unsupported critical extensions.");
                    }
                }
                if (processCRLH != null) {
                    final Set<String> criticalExtensionOIDs2 = processCRLH.getCriticalExtensionOIDs();
                    if (criticalExtensionOIDs2 != null) {
                        final HashSet set2 = new HashSet(criticalExtensionOIDs2);
                        set2.remove(X509Extensions.IssuingDistributionPoint.getId());
                        set2.remove(X509Extensions.DeltaCRLIndicator.getId());
                        if (!set2.isEmpty()) {
                            throw new AnnotatedException("Delta CRL contains unsupported critical extension.");
                        }
                    }
                }
                b = true;
            }
            catch (AnnotatedException ex2) {
                ex = ex2;
            }
        }
        if (!b) {
            throw ex;
        }
    }
    
    protected static void checkCRLs(final ExtendedPKIXParameters extendedPKIXParameters, final X509Certificate x509Certificate, final Date date, final X509Certificate x509Certificate2, final PublicKey publicKey, final List list) throws AnnotatedException {
        Throwable t = null;
        CRLDistPoint instance;
        try {
            instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS));
        }
        catch (Exception ex) {
            throw new AnnotatedException("CRL distribution point extension could not be read.", ex);
        }
        try {
            CertPathValidatorUtilities.addAdditionalStoresFromCRLDistributionPoint(instance, extendedPKIXParameters);
        }
        catch (AnnotatedException ex2) {
            throw new AnnotatedException("No additional CRL locations could be decoded from CRL distribution point extension.", ex2);
        }
        final CertStatus certStatus = new CertStatus();
        final ReasonsMask reasonsMask = new ReasonsMask();
        boolean b = false;
        if (instance != null) {
            DistributionPoint[] distributionPoints;
            try {
                distributionPoints = instance.getDistributionPoints();
            }
            catch (Exception ex3) {
                throw new AnnotatedException("Distribution points could not be read.", ex3);
            }
            if (distributionPoints != null) {
                for (int n = 0; n < distributionPoints.length && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons(); ++n) {
                    final ExtendedPKIXParameters extendedPKIXParameters2 = (ExtendedPKIXParameters)extendedPKIXParameters.clone();
                    try {
                        checkCRL(distributionPoints[n], extendedPKIXParameters2, x509Certificate, date, x509Certificate2, publicKey, certStatus, reasonsMask, list);
                        b = true;
                    }
                    catch (AnnotatedException ex4) {
                        t = ex4;
                    }
                }
            }
        }
        if (certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
            try {
                DERObject object;
                try {
                    object = new ASN1InputStream(CertPathValidatorUtilities.getEncodedIssuerPrincipal(x509Certificate).getEncoded()).readObject();
                }
                catch (Exception ex5) {
                    throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", ex5);
                }
                checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, object))), null, null), (ExtendedPKIXParameters)extendedPKIXParameters.clone(), x509Certificate, date, x509Certificate2, publicKey, certStatus, reasonsMask, list);
                b = true;
            }
            catch (AnnotatedException ex6) {
                t = ex6;
            }
        }
        if (!b) {
            if (t instanceof AnnotatedException) {
                throw t;
            }
            throw new AnnotatedException("No valid CRL found.", t);
        }
        else {
            if (certStatus.getCertStatus() != 11) {
                throw new AnnotatedException("Certificate revocation after " + certStatus.getRevocationDate() + ", reason: " + RFC3280CertPathUtilities.crlReasons[certStatus.getCertStatus()]);
            }
            if (!reasonsMask.isAllReasons() && certStatus.getCertStatus() == 11) {
                certStatus.setCertStatus(12);
            }
            if (certStatus.getCertStatus() == 12) {
                throw new AnnotatedException("Certificate status could not be determined.");
            }
        }
    }
    
    protected static int prepareNextCertJ(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        DERInteger instance;
        try {
            instance = DERInteger.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.INHIBIT_ANY_POLICY));
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Inhibit any-policy extension cannot be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final int intValue = instance.getValue().intValue();
            if (intValue < n2) {
                return intValue;
            }
        }
        return n2;
    }
    
    protected static void prepareNextCertK(final CertPath certPath, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        BasicConstraints instance;
        try {
            instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", ex, certPath, n);
        }
        if (instance == null) {
            throw new CertPathValidatorException("Intermediate certificate lacks BasicConstraints");
        }
        if (!instance.isCA()) {
            throw new CertPathValidatorException("Not a CA certificate");
        }
    }
    
    protected static int prepareNextCertL(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        if (CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n))) {
            return n2;
        }
        if (n2 <= 0) {
            throw new ExtCertPathValidatorException("Max path length not greater than zero", null, certPath, n);
        }
        return n2 - 1;
    }
    
    protected static int prepareNextCertM(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        BasicConstraints instance;
        try {
            instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final BigInteger pathLenConstraint = instance.getPathLenConstraint();
            if (pathLenConstraint != null) {
                final int intValue = pathLenConstraint.intValue();
                if (intValue < n2) {
                    return intValue;
                }
            }
        }
        return n2;
    }
    
    protected static void prepareNextCertN(final CertPath certPath, final int n) throws CertPathValidatorException {
        final boolean[] keyUsage = ((X509Certificate)certPath.getCertificates().get(n)).getKeyUsage();
        if (keyUsage != null && !keyUsage[5]) {
            throw new ExtCertPathValidatorException("Issuer certificate keyusage extension is critical and does not permit key signing.", null, certPath, n);
        }
    }
    
    protected static void prepareNextCertO(final CertPath certPath, final int index, final Set set, final List list) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(index);
        final Iterator<PKIXCertPathChecker> iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().check(x509Certificate, set);
                continue;
            }
            catch (CertPathValidatorException ex) {
                throw new CertPathValidatorException(ex.getMessage(), ex.getCause(), certPath, index);
            }
            break;
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension.", null, certPath, index);
        }
    }
    
    protected static int prepareNextCertH1(final CertPath certPath, final int n, final int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            return n2 - 1;
        }
        return n2;
    }
    
    protected static int prepareNextCertH2(final CertPath certPath, final int n, final int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            return n2 - 1;
        }
        return n2;
    }
    
    protected static int prepareNextCertH3(final CertPath certPath, final int n, final int n2) {
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && n2 != 0) {
            return n2 - 1;
        }
        return n2;
    }
    
    protected static int wrapupCertA(int n, final X509Certificate x509Certificate) {
        if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate) && n != 0) {
            --n;
        }
        return n;
    }
    
    protected static int wrapupCertB(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        ASN1Sequence instance;
        try {
            instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Policy constraints could no be decoded.", ex, certPath, n);
        }
        if (instance != null) {
            final Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
                switch (asn1TaggedObject.getTagNo()) {
                    case 0: {
                        int intValue;
                        try {
                            intValue = DERInteger.getInstance(asn1TaggedObject).getValue().intValue();
                        }
                        catch (Exception ex2) {
                            throw new ExtCertPathValidatorException("Policy constraints requireExplicitPolicy field could no be decoded.", ex2, certPath, n);
                        }
                        if (intValue == 0) {
                            return 0;
                        }
                        continue;
                    }
                }
            }
        }
        return n2;
    }
    
    protected static void wrapupCertF(final CertPath certPath, final int n, final List list, final Set set) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        final Iterator<PKIXCertPathChecker> iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().check(x509Certificate, set);
                continue;
            }
            catch (CertPathValidatorException ex) {
                throw new ExtCertPathValidatorException("Additional certificate path checker failed.", ex, certPath, n);
            }
            break;
        }
        if (!set.isEmpty()) {
            throw new ExtCertPathValidatorException("Certificate has unsupported critical extension", null, certPath, n);
        }
    }
    
    protected static PKIXPolicyNode wrapupCertG(final CertPath certPath, final ExtendedPKIXParameters extendedPKIXParameters, final Set set, final int n, final List[] array, PKIXPolicyNode pkixPolicyNode, final Set set2) throws CertPathValidatorException {
        final int size = certPath.getCertificates().size();
        PKIXPolicyNode pkixPolicyNode2;
        if (pkixPolicyNode == null) {
            if (extendedPKIXParameters.isExplicitPolicyRequired()) {
                throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, n);
            }
            pkixPolicyNode2 = null;
        }
        else if (CertPathValidatorUtilities.isAnyPolicy(set)) {
            if (extendedPKIXParameters.isExplicitPolicyRequired()) {
                if (set2.isEmpty()) {
                    throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, n);
                }
                final HashSet<PKIXPolicyNode> set3 = new HashSet<PKIXPolicyNode>();
                for (int i = 0; i < array.length; ++i) {
                    final List list = array[i];
                    for (int j = 0; j < list.size(); ++j) {
                        final PKIXPolicyNode pkixPolicyNode3 = list.get(j);
                        if ("2.5.29.32.0".equals(pkixPolicyNode3.getValidPolicy())) {
                            final Iterator children = pkixPolicyNode3.getChildren();
                            while (children.hasNext()) {
                                set3.add(children.next());
                            }
                        }
                    }
                }
                final Iterator<Object> iterator = set3.iterator();
                while (iterator.hasNext()) {
                    if (!set2.contains(iterator.next().getValidPolicy())) {}
                }
                if (pkixPolicyNode != null) {
                    for (int k = size - 1; k >= 0; --k) {
                        final List list2 = array[k];
                        for (int l = 0; l < list2.size(); ++l) {
                            final PKIXPolicyNode pkixPolicyNode4 = list2.get(l);
                            if (!pkixPolicyNode4.hasChildren()) {
                                pkixPolicyNode = CertPathValidatorUtilities.removePolicyNode(pkixPolicyNode, array, pkixPolicyNode4);
                            }
                        }
                    }
                }
            }
            pkixPolicyNode2 = pkixPolicyNode;
        }
        else {
            final HashSet<PKIXPolicyNode> set4 = new HashSet<PKIXPolicyNode>();
            for (int n2 = 0; n2 < array.length; ++n2) {
                final List list3 = array[n2];
                for (int n3 = 0; n3 < list3.size(); ++n3) {
                    final PKIXPolicyNode pkixPolicyNode5 = list3.get(n3);
                    if ("2.5.29.32.0".equals(pkixPolicyNode5.getValidPolicy())) {
                        final Iterator children2 = pkixPolicyNode5.getChildren();
                        while (children2.hasNext()) {
                            final PKIXPolicyNode pkixPolicyNode6 = children2.next();
                            if (!"2.5.29.32.0".equals(pkixPolicyNode6.getValidPolicy())) {
                                set4.add(pkixPolicyNode6);
                            }
                        }
                    }
                }
            }
            for (final PKIXPolicyNode pkixPolicyNode7 : set4) {
                if (!set.contains(pkixPolicyNode7.getValidPolicy())) {
                    pkixPolicyNode = CertPathValidatorUtilities.removePolicyNode(pkixPolicyNode, array, pkixPolicyNode7);
                }
            }
            if (pkixPolicyNode != null) {
                for (int n4 = size - 1; n4 >= 0; --n4) {
                    final List list4 = array[n4];
                    for (int n5 = 0; n5 < list4.size(); ++n5) {
                        final PKIXPolicyNode pkixPolicyNode8 = list4.get(n5);
                        if (!pkixPolicyNode8.hasChildren()) {
                            pkixPolicyNode = CertPathValidatorUtilities.removePolicyNode(pkixPolicyNode, array, pkixPolicyNode8);
                        }
                    }
                }
            }
            pkixPolicyNode2 = pkixPolicyNode;
        }
        return pkixPolicyNode2;
    }
    
    static {
        CERTIFICATE_POLICIES = X509Extensions.CertificatePolicies.getId();
        POLICY_MAPPINGS = X509Extensions.PolicyMappings.getId();
        INHIBIT_ANY_POLICY = X509Extensions.InhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = X509Extensions.IssuingDistributionPoint.getId();
        FRESHEST_CRL = X509Extensions.FreshestCRL.getId();
        DELTA_CRL_INDICATOR = X509Extensions.DeltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = X509Extensions.PolicyConstraints.getId();
        BASIC_CONSTRAINTS = X509Extensions.BasicConstraints.getId();
        CRL_DISTRIBUTION_POINTS = X509Extensions.CRLDistributionPoints.getId();
        SUBJECT_ALTERNATIVE_NAME = X509Extensions.SubjectAlternativeName.getId();
        NAME_CONSTRAINTS = X509Extensions.NameConstraints.getId();
        AUTHORITY_KEY_IDENTIFIER = X509Extensions.AuthorityKeyIdentifier.getId();
        KEY_USAGE = X509Extensions.KeyUsage.getId();
        CRL_NUMBER = X509Extensions.CRLNumber.getId();
        crlReasons = new String[] { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
    }
}
