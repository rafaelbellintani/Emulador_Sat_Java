// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.CertPath;
import java.util.HashSet;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.Certificate;
import java.util.Set;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateFactory;
import java.util.Iterator;
import org.bouncycastle.util.Selector;
import org.bouncycastle.jce.exception.ExtCertPathBuilderException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.security.cert.CertPathBuilderException;
import org.bouncycastle.x509.X509CertStoreSelector;
import java.util.ArrayList;
import java.security.cert.PKIXParameters;
import java.security.InvalidAlgorithmParameterException;
import org.bouncycastle.x509.ExtendedPKIXBuilderParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathBuilderSpi;

public class PKIXCertPathBuilderSpi extends CertPathBuilderSpi
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
        if (!(targetConstraints instanceof X509CertStoreSelector)) {
            throw new CertPathBuilderException("TargetConstraints must be an instance of " + X509CertStoreSelector.class.getName() + " for " + this.getClass().getName() + " class.");
        }
        Collection certificates;
        try {
            certificates = CertPathValidatorUtilities.findCertificates((X509CertStoreSelector)targetConstraints, extendedPKIXBuilderParameters.getStores());
            certificates.addAll(CertPathValidatorUtilities.findCertificates((X509CertStoreSelector)targetConstraints, extendedPKIXBuilderParameters.getCertStores()));
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathBuilderException("Error finding target certificate.", ex);
        }
        if (certificates.isEmpty()) {
            throw new CertPathBuilderException("No certificate found matching targetContraints.");
        }
        CertPathBuilderResult build = null;
        for (Iterator<X509Certificate> iterator = certificates.iterator(); iterator.hasNext() && build == null; build = this.build(iterator.next(), extendedPKIXBuilderParameters, list)) {}
        if (build == null && this.certPathException != null) {
            if (this.certPathException instanceof AnnotatedException) {
                throw new CertPathBuilderException(this.certPathException.getMessage(), this.certPathException.getCause());
            }
            throw new CertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
        }
        else {
            if (build == null && this.certPathException == null) {
                throw new CertPathBuilderException("Unable to find certificate chain.");
            }
            return build;
        }
    }
    
    protected CertPathBuilderResult build(final X509Certificate x509Certificate, final ExtendedPKIXBuilderParameters params, final List certificates) {
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
            instance2 = CertPathValidator.getInstance("PKIX", "BC");
        }
        catch (Exception ex5) {
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
                throw new AnnotatedException("No additiontal X.509 stores can be added from certificate locations.", ex3);
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
            for (Iterator<Object> iterator = set.iterator(); iterator.hasNext() && build == null; build = this.build(iterator.next(), params, certificates)) {}
        }
        catch (AnnotatedException certPathException) {
            this.certPathException = certPathException;
        }
        if (build == null) {
            certificates.remove(x509Certificate);
        }
        return build;
    }
}
