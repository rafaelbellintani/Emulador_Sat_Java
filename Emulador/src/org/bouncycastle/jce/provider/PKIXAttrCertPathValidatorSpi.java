// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.CertPathValidatorException;
import java.util.Date;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.util.Selector;
import java.util.List;
import org.bouncycastle.jce.exception.ExtCertPathValidatorException;
import java.security.cert.X509Certificate;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import java.security.InvalidAlgorithmParameterException;
import org.bouncycastle.x509.ExtendedPKIXParameters;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorSpi;

public class PKIXAttrCertPathValidatorSpi extends CertPathValidatorSpi
{
    @Override
    public CertPathValidatorResult engineValidate(final CertPath certPath, final CertPathParameters certPathParameters) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        if (!(certPathParameters instanceof ExtendedPKIXParameters)) {
            throw new InvalidAlgorithmParameterException("Parameters must be a " + ExtendedPKIXParameters.class.getName() + " instance.");
        }
        final ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)certPathParameters;
        final Selector targetConstraints = extendedPKIXParameters.getTargetConstraints();
        if (!(targetConstraints instanceof X509AttributeCertStoreSelector)) {
            throw new InvalidAlgorithmParameterException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + this.getClass().getName() + " class.");
        }
        final X509AttributeCertificate attributeCert = ((X509AttributeCertStoreSelector)targetConstraints).getAttributeCert();
        final CertPath processAttrCert1 = RFC3281CertPathUtilities.processAttrCert1(attributeCert, extendedPKIXParameters);
        final CertPathValidatorResult processAttrCert2 = RFC3281CertPathUtilities.processAttrCert2(certPath, extendedPKIXParameters);
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(0);
        RFC3281CertPathUtilities.processAttrCert3(x509Certificate, extendedPKIXParameters);
        RFC3281CertPathUtilities.processAttrCert4(x509Certificate, extendedPKIXParameters);
        RFC3281CertPathUtilities.processAttrCert5(attributeCert, extendedPKIXParameters);
        RFC3281CertPathUtilities.processAttrCert7(attributeCert, certPath, processAttrCert1, extendedPKIXParameters);
        RFC3281CertPathUtilities.additionalChecks(attributeCert, extendedPKIXParameters);
        Date validCertDateFromValidityModel;
        try {
            validCertDateFromValidityModel = CertPathValidatorUtilities.getValidCertDateFromValidityModel(extendedPKIXParameters, null, -1);
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Could not get validity date from attribute certificate.", ex);
        }
        RFC3281CertPathUtilities.checkCRLs(attributeCert, extendedPKIXParameters, x509Certificate, validCertDateFromValidityModel, certPath.getCertificates());
        return processAttrCert2;
    }
}
