// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.CertPath;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.Certificate;
import java.util.Set;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateFactory;
import java.security.Principal;
import java.util.Iterator;
import org.bouncycastle.util.Selector;
import java.io.IOException;
import java.util.List;
import java.util.Collection;
import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import java.security.cert.CertPathBuilderException;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import java.util.ArrayList;
import java.security.cert.PKIXParameters;
import java.security.InvalidAlgorithmParameterException;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathBuilderSpi;

public class PKIXAttrCertPathBuilderSpi extends CertPathBuilderSpi
{
    private Exception certPathException;
    
    @Override
    public CertPathBuilderResult engineBuild(final CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        if (!(certPathParameters instanceof PKIXBuilderParameters) && !(certPathParameters instanceof ExtendedPKIXBuilderParameters)) {
            throw new InvalidAlgorithmParameterException("Parameters must be an instance of " + PKIXBuilderParameters.class.getName() + " or " + ExtendedPKIXBuilderParameters.class.getName() + ".");
        }
        ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters;
        if (certPathParameters instanceof ExtendedPKIXBuilderParameters) {
            extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)certPathParameters;
        }
        else {
            extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)ExtendedPKIXBuilderParameters.getInstance((PKIXParameters)certPathParameters);
        }
        final ArrayList list = new ArrayList();
        final Selector targetConstraints = extendedPKIXBuilderParameters.getTargetConstraints();
        if (!(targetConstraints instanceof X509AttributeCertStoreSelector)) {
            throw new CertPathBuilderException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + this.getClass().getName() + " class.");
        }
        Collection certificates;
        try {
            certificates = CertPathValidatorUtilities.findCertificates((X509AttributeCertStoreSelector)targetConstraints, extendedPKIXBuilderParameters.getStores());
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathBuilderException("Error finding target attribute certificate.", ex);
        }
        if (certificates.isEmpty()) {
            throw new CertPathBuilderException("No attribute certificate found matching targetContraints.");
        }
        CertPathBuilderResult build = null;
        final Iterator<X509AttributeCertificate> iterator = certificates.iterator();
        while (iterator.hasNext() && build == null) {
            final X509AttributeCertificate x509AttributeCertificate = iterator.next();
            final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            final Principal[] principals = x509AttributeCertificate.getIssuer().getPrincipals();
            final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
            for (int i = 0; i < principals.length; ++i) {
                try {
                    if (principals[i] instanceof X500Principal) {
                        x509CertStoreSelector.setSubject(((X500Principal)principals[i]).getEncoded());
                    }
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXBuilderParameters.getStores()));
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(x509CertStoreSelector, extendedPKIXBuilderParameters.getCertStores()));
                }
                catch (AnnotatedException ex2) {
                    throw new ExtCertPathBuilderException("Public key certificate for attribute certificate cannot be searched.", ex2);
                }
                catch (IOException ex3) {
                    throw new ExtCertPathBuilderException("cannot encode X500Principal.", ex3);
                }
            }
            if (set.isEmpty()) {
                throw new CertPathBuilderException("Public key certificate for attribute certificate cannot be found.");
            }
            for (Iterator<Object> iterator2 = set.iterator(); iterator2.hasNext() && build == null; build = this.build(x509AttributeCertificate, iterator2.next(), extendedPKIXBuilderParameters, list)) {}
        }
        if (build == null && this.certPathException != null) {
            throw new ExtCertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
        }
        if (build == null && this.certPathException == null) {
            throw new CertPathBuilderException("Unable to find certificate chain.");
        }
        return build;
    }
    
    private CertPathBuilderResult build(final X509AttributeCertificate x509AttributeCertificate, final X509Certificate x509Certificate, final ExtendedPKIXBuilderParameters params, final List certificates) {
        if (certificates.contains(x509Certificate)) {
            return null;
        }
        if (params.getExcludedCerts().contains(x509Certificate)) {
            return null;
        }
        if (params.getMaxPathLength() != -1 && certificates.size() - 1 > params.getMaxPathLength()) {
            return null;
        }
        certificates.add(x509Certificate);
        CertPathBuilderResult build = null;
        CertificateFactory instance;
        CertPathValidator instance2;
        try {
            instance = CertificateFactory.getInstance("X.509", "BC");
            instance2 = CertPathValidator.getInstance("RFC3281", "BC");
        }
        catch (Exception ex6) {
            throw new RuntimeException("Exception creating support classes.");
        }
        try {
            if (CertPathValidatorUtilities.findTrustAnchor(x509Certificate, params.getTrustAnchors(), params.getSigProvider()) != null) {
                CertPath generateCertPath;
                try {
                    generateCertPath = instance.generateCertPath(certificates);
                }
                catch (Exception ex) {
                    throw new AnnotatedException("Certification path could not be constructed from certificate list.", ex);
                }
                PKIXCertPathValidatorResult pkixCertPathValidatorResult;
                try {
                    pkixCertPathValidatorResult = (PKIXCertPathValidatorResult)instance2.validate(generateCertPath, params);
                }
                catch (Exception ex2) {
                    throw new AnnotatedException("Certification path could not be validated.", ex2);
                }
                return new PKIXCertPathBuilderResult(generateCertPath, pkixCertPathValidatorResult.getTrustAnchor(), pkixCertPathValidatorResult.getPolicyTree(), pkixCertPathValidatorResult.getPublicKey());
            }
            try {
                CertPathValidatorUtilities.addAdditionalStoresFromAltNames(x509Certificate, params);
            }
            catch (CertificateParsingException ex3) {
                throw new AnnotatedException("No additional X.509 stores can be added from certificate locations.", ex3);
            }
            final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
            try {
                set.addAll((Collection<?>)CertPathValidatorUtilities.findIssuerCerts(x509Certificate, params));
            }
            catch (AnnotatedException ex4) {
                throw new AnnotatedException("Cannot find issuer certificate for certificate in certification path.", ex4);
            }
            if (set.isEmpty()) {
                throw new AnnotatedException("No issuer certificate for certificate in certification path found.");
            }
            final Iterator<Object> iterator = set.iterator();
            while (iterator.hasNext() && build == null) {
                final X509Certificate x509Certificate2 = iterator.next();
                if (x509Certificate2.getIssuerX500Principal().equals(x509Certificate2.getSubjectX500Principal())) {
                    continue;
                }
                build = this.build(x509AttributeCertificate, x509Certificate2, params, certificates);
            }
        }
        catch (AnnotatedException ex5) {
            this.certPathException = new AnnotatedException("No valid certification path could be build.", ex5);
        }
        if (build == null) {
            certificates.remove(x509Certificate);
        }
        return build;
    }
}
