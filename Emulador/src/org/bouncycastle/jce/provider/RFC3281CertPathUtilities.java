// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.PublicKey;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.security.Principal;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilder;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import java.io.IOException;
import org.bouncycastle.x509.X509CertStoreSelector;
import java.util.HashSet;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPathParameters;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.TrustAnchor;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.PKIXParameters;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.ASN1InputStream;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import java.util.List;
import java.util.Date;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;
import java.security.cert.CertPathValidatorException;
import java.util.Collection;
import org.bouncycastle.x509.PKIXAttrCertChecker;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle.asn1.x509.TargetInformation;
import java.security.cert.X509Extension;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.security.cert.CertPath;
import org.bouncycastle.x509.X509AttributeCertificate;

class RFC3281CertPathUtilities
{
    private static final String TARGET_INFORMATION;
    private static final String NO_REV_AVAIL;
    private static final String CRL_DISTRIBUTION_POINTS;
    private static final String AUTHORITY_INFO_ACCESS;
    
    protected static void processAttrCert7(final X509AttributeCertificate x509AttributeCertificate, final CertPath certPath, final CertPath certPath2, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        final Set<String> criticalExtensionOIDs = x509AttributeCertificate.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs.contains(RFC3281CertPathUtilities.TARGET_INFORMATION)) {
            try {
                TargetInformation.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, RFC3281CertPathUtilities.TARGET_INFORMATION));
            }
            catch (AnnotatedException ex) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", ex);
            }
            catch (IllegalArgumentException ex2) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", ex2);
            }
        }
        criticalExtensionOIDs.remove(RFC3281CertPathUtilities.TARGET_INFORMATION);
        final Iterator<PKIXAttrCertChecker> iterator = extendedPKIXParameters.getAttrCertCheckers().iterator();
        while (iterator.hasNext()) {
            iterator.next().check(x509AttributeCertificate, certPath, certPath2, criticalExtensionOIDs);
        }
        if (!criticalExtensionOIDs.isEmpty()) {
            throw new CertPathValidatorException("Attribute certificate contains unsupported critical extensions: " + criticalExtensionOIDs);
        }
    }
    
    protected static void checkCRLs(final X509AttributeCertificate x509AttributeCertificate, final ExtendedPKIXParameters extendedPKIXParameters, final X509Certificate x509Certificate, final Date date, final List list) throws CertPathValidatorException {
        if (extendedPKIXParameters.isRevocationEnabled()) {
            if (x509AttributeCertificate.getExtensionValue(RFC3281CertPathUtilities.NO_REV_AVAIL) == null) {
                CRLDistPoint instance;
                try {
                    instance = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS));
                }
                catch (AnnotatedException cause) {
                    throw new CertPathValidatorException("CRL distribution point extension could not be read.", cause);
                }
                try {
                    CertPathValidatorUtilities.addAdditionalStoresFromCRLDistributionPoint(instance, extendedPKIXParameters);
                }
                catch (AnnotatedException cause2) {
                    throw new CertPathValidatorException("No additional CRL locations could be decoded from CRL distribution point extension.", cause2);
                }
                final CertStatus certStatus = new CertStatus();
                final ReasonsMask reasonsMask = new ReasonsMask();
                Throwable t = null;
                boolean b = false;
                if (instance != null) {
                    DistributionPoint[] distributionPoints;
                    try {
                        distributionPoints = instance.getDistributionPoints();
                    }
                    catch (Exception ex) {
                        throw new ExtCertPathValidatorException("Distribution points could not be read.", ex);
                    }
                    try {
                        for (int n = 0; n < distributionPoints.length && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons(); ++n) {
                            checkCRL(distributionPoints[n], x509AttributeCertificate, (ExtendedPKIXParameters)extendedPKIXParameters.clone(), date, x509Certificate, certStatus, reasonsMask, list);
                            b = true;
                        }
                    }
                    catch (AnnotatedException ex2) {
                        t = new AnnotatedException("No valid CRL for distribution point found.", ex2);
                    }
                }
                if (certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
                    try {
                        DERObject object;
                        try {
                            object = new ASN1InputStream(((X500Principal)x509AttributeCertificate.getIssuer().getPrincipals()[0]).getEncoded()).readObject();
                        }
                        catch (Exception ex3) {
                            throw new AnnotatedException("Issuer from certificate for CRL could not be reencoded.", ex3);
                        }
                        checkCRL(new DistributionPoint(new DistributionPointName(0, new GeneralNames(new GeneralName(4, object))), null, null), x509AttributeCertificate, (ExtendedPKIXParameters)extendedPKIXParameters.clone(), date, x509Certificate, certStatus, reasonsMask, list);
                        b = true;
                    }
                    catch (AnnotatedException ex4) {
                        t = new AnnotatedException("No valid CRL for distribution point found.", ex4);
                    }
                }
                if (!b) {
                    throw new ExtCertPathValidatorException("No valid CRL found.", t);
                }
                if (certStatus.getCertStatus() != 11) {
                    throw new CertPathValidatorException("Attribute certificate revocation after " + certStatus.getRevocationDate() + ", reason: " + RFC3280CertPathUtilities.crlReasons[certStatus.getCertStatus()]);
                }
                if (!reasonsMask.isAllReasons() && certStatus.getCertStatus() == 11) {
                    certStatus.setCertStatus(12);
                }
                if (certStatus.getCertStatus() == 12) {
                    throw new CertPathValidatorException("Attribute certificate status could not be determined.");
                }
            }
            else if (x509AttributeCertificate.getExtensionValue(RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS) != null || x509AttributeCertificate.getExtensionValue(RFC3281CertPathUtilities.AUTHORITY_INFO_ACCESS) != null) {
                throw new CertPathValidatorException("No rev avail extension is set, but also an AC revocation pointer.");
            }
        }
    }
    
    protected static void additionalChecks(final X509AttributeCertificate x509AttributeCertificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        for (final String str : extendedPKIXParameters.getProhibitedACAttributes()) {
            if (x509AttributeCertificate.getAttributes(str) != null) {
                throw new CertPathValidatorException("Attribute certificate contains prohibited attribute: " + str + ".");
            }
        }
        for (final String str2 : extendedPKIXParameters.getNecessaryACAttributes()) {
            if (x509AttributeCertificate.getAttributes(str2) == null) {
                throw new CertPathValidatorException("Attribute certificate does not contain necessary attribute: " + str2 + ".");
            }
        }
    }
    
    protected static void processAttrCert5(final X509AttributeCertificate x509AttributeCertificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        try {
            x509AttributeCertificate.checkValidity(CertPathValidatorUtilities.getValidDate(extendedPKIXParameters));
        }
        catch (CertificateExpiredException ex) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", ex);
        }
        catch (CertificateNotYetValidException ex2) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", ex2);
        }
    }
    
    protected static void processAttrCert4(final X509Certificate x509Certificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        final Set trustedACIssuers = extendedPKIXParameters.getTrustedACIssuers();
        boolean b = false;
        for (final TrustAnchor trustAnchor : trustedACIssuers) {
            if (x509Certificate.getSubjectX500Principal().getName("RFC2253").equals(trustAnchor.getCAName()) || x509Certificate.equals(trustAnchor.getTrustedCert())) {
                b = true;
            }
        }
        if (!b) {
            throw new CertPathValidatorException("Attribute certificate issuer is not directly trusted.");
        }
    }
    
    protected static void processAttrCert3(final X509Certificate x509Certificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        if (x509Certificate.getKeyUsage() != null && !x509Certificate.getKeyUsage()[0] && !x509Certificate.getKeyUsage()[1]) {
            throw new CertPathValidatorException("Attribute certificate issuer public key cannot be used to validate digital signatures.");
        }
        if (x509Certificate.getBasicConstraints() != -1) {
            throw new CertPathValidatorException("Attribute certificate issuer is also a public key certificate issuer.");
        }
    }
    
    protected static CertPathValidatorResult processAttrCert2(final CertPath certPath, final ExtendedPKIXParameters params) throws CertPathValidatorException {
        CertPathValidator instance;
        try {
            instance = CertPathValidator.getInstance("PKIX", "BC");
        }
        catch (NoSuchProviderException ex) {
            throw new ExtCertPathValidatorException("Support class could not be created.", ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new ExtCertPathValidatorException("Support class could not be created.", ex2);
        }
        try {
            return instance.validate(certPath, params);
        }
        catch (CertPathValidatorException ex3) {
            throw new ExtCertPathValidatorException("Certification path for issuer certificate of attribute certificate could not be validated.", ex3);
        }
        catch (InvalidAlgorithmParameterException ex4) {
            throw new RuntimeException(ex4.getMessage());
        }
    }
    
    protected static CertPath processAttrCert1(final X509AttributeCertificate x509AttributeCertificate, final ExtendedPKIXParameters extendedPKIXParameters) throws CertPathValidatorException {
        CertPathBuilderResult build = null;
        final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
        if (x509AttributeCertificate.getHolder().getIssuer() != null) {
            final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            x509CertStoreSelector.setSerialNumber(x509AttributeCertificate.getHolder().getSerialNumber());
            final Principal[] issuer = x509AttributeCertificate.getHolder().getIssuer();
            for (int i = 0; i < issuer.length; ++i) {
                try {
                    if (issuer[i] instanceof X500Principal) {
                        x509CertStoreSelector.setIssuer(((X500Principal)issuer[i]).getEncoded());
                    }
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXParameters.getStores()));
                }
                catch (AnnotatedException ex) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", ex);
                }
                catch (IOException ex2) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", ex2);
                }
            }
            if (set.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in base certificate ID for attribute certificate cannot be found.");
            }
        }
        if (x509AttributeCertificate.getHolder().getEntityNames() != null) {
            final X509CertStoreSelector x509CertStoreSelector2 = new X509CertStoreSelector();
            final Principal[] entityNames = x509AttributeCertificate.getHolder().getEntityNames();
            for (int j = 0; j < entityNames.length; ++j) {
                try {
                    if (entityNames[j] instanceof X500Principal) {
                        x509CertStoreSelector2.setIssuer(((X500Principal)entityNames[j]).getEncoded());
                    }
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(x509CertStoreSelector2, extendedPKIXParameters.getStores()));
                }
                catch (AnnotatedException ex3) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", ex3);
                }
                catch (IOException ex4) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", ex4);
                }
            }
            if (set.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in entity name for attribute certificate cannot be found.");
            }
        }
        final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)ExtendedPKIXBuilderParameters.getInstance(extendedPKIXParameters);
        Object o = null;
        final Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final X509CertStoreSelector targetConstraints = new X509CertStoreSelector();
            targetConstraints.setCertificate(iterator.next());
            extendedPKIXBuilderParameters.setTargetConstraints(targetConstraints);
            CertPathBuilder instance;
            try {
                instance = CertPathBuilder.getInstance("PKIX", "BC");
            }
            catch (NoSuchProviderException ex5) {
                throw new ExtCertPathValidatorException("Support class could not be created.", ex5);
            }
            catch (NoSuchAlgorithmException ex6) {
                throw new ExtCertPathValidatorException("Support class could not be created.", ex6);
            }
            try {
                build = instance.build(ExtendedPKIXBuilderParameters.getInstance(extendedPKIXBuilderParameters));
            }
            catch (CertPathBuilderException ex7) {
                o = new ExtCertPathValidatorException("Certification path for public key certificate of attribute certificate could not be build.", ex7);
            }
            catch (InvalidAlgorithmParameterException ex8) {
                throw new RuntimeException(ex8.getMessage());
            }
        }
        if (o != null) {
            throw o;
        }
        return build.getCertPath();
    }
    
    private static void checkCRL(final DistributionPoint distributionPoint, final X509AttributeCertificate x509AttributeCertificate, final ExtendedPKIXParameters extendedPKIXParameters, final Date date, final X509Certificate x509Certificate, final CertStatus certStatus, final ReasonsMask reasonsMask, final List list) throws AnnotatedException {
        if (x509AttributeCertificate.getExtensionValue(X509Extensions.NoRevAvail.getId()) != null) {
            return;
        }
        final Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        final Set completeCRLs = CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509AttributeCertificate, date2, extendedPKIXParameters);
        boolean b = false;
        AnnotatedException ex = null;
        final Iterator<X509CRL> iterator = completeCRLs.iterator();
        while (iterator.hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
            try {
                final X509CRL x509CRL = iterator.next();
                final ReasonsMask processCRLD = RFC3280CertPathUtilities.processCRLD(x509CRL, distributionPoint);
                if (!processCRLD.hasNewReasons(reasonsMask)) {
                    continue;
                }
                final PublicKey processCRLG = RFC3280CertPathUtilities.processCRLG(x509CRL, RFC3280CertPathUtilities.processCRLF(x509CRL, x509AttributeCertificate, null, null, extendedPKIXParameters, list));
                X509CRL processCRLH = null;
                if (extendedPKIXParameters.isUseDeltasEnabled()) {
                    processCRLH = RFC3280CertPathUtilities.processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date2, extendedPKIXParameters, x509CRL), processCRLG);
                }
                if (extendedPKIXParameters.getValidityModel() != 1 && x509AttributeCertificate.getNotAfter().getTime() < x509CRL.getThisUpdate().getTime()) {
                    throw new AnnotatedException("No valid CRL for current time found.");
                }
                RFC3280CertPathUtilities.processCRLB1(distributionPoint, x509AttributeCertificate, x509CRL);
                RFC3280CertPathUtilities.processCRLB2(distributionPoint, x509AttributeCertificate, x509CRL);
                RFC3280CertPathUtilities.processCRLC(processCRLH, x509CRL, extendedPKIXParameters);
                RFC3280CertPathUtilities.processCRLI(date, processCRLH, x509AttributeCertificate, certStatus, extendedPKIXParameters);
                RFC3280CertPathUtilities.processCRLJ(date, x509CRL, x509AttributeCertificate, certStatus);
                if (certStatus.getCertStatus() == 8) {
                    certStatus.setCertStatus(11);
                }
                reasonsMask.addReasons(processCRLD);
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
    
    static {
        TARGET_INFORMATION = X509Extensions.TargetInformation.getId();
        NO_REV_AVAIL = X509Extensions.NoRevAvail.getId();
        CRL_DISTRIBUTION_POINTS = X509Extensions.CRLDistributionPoints.getId();
        AUTHORITY_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
    }
}
