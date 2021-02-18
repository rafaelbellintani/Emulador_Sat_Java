// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertStoreException;
import java.util.Iterator;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.security.cert.CertSelector;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.CertStoreSpi;

public class CertStoreCollectionSpi extends CertStoreSpi
{
    private CollectionCertStoreParameters params;
    
    public CertStoreCollectionSpi(final CertStoreParameters params) throws InvalidAlgorithmParameterException {
        super(params);
        if (!(params instanceof CollectionCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException("org.bouncycastle.jce.provider.CertStoreCollectionSpi: parameter must be a CollectionCertStoreParameters object\n" + params.toString());
        }
        this.params = (CollectionCertStoreParameters)params;
    }
    
    @Override
    public Collection engineGetCertificates(final CertSelector certSelector) throws CertStoreException {
        final ArrayList<Certificate> list = new ArrayList<Certificate>();
        final Iterator<?> iterator = this.params.getCollection().iterator();
        if (certSelector == null) {
            while (iterator.hasNext()) {
                final Object next = iterator.next();
                if (next instanceof Certificate) {
                    list.add((Certificate)next);
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                final Object next2 = iterator.next();
                if (next2 instanceof Certificate && certSelector.match((Certificate)next2)) {
                    list.add((Certificate)next2);
                }
            }
        }
        return list;
    }
    
    @Override
    public Collection engineGetCRLs(final CRLSelector crlSelector) throws CertStoreException {
        final ArrayList<CRL> list = new ArrayList<CRL>();
        final Iterator<?> iterator = this.params.getCollection().iterator();
        if (crlSelector == null) {
            while (iterator.hasNext()) {
                final Object next = iterator.next();
                if (next instanceof CRL) {
                    list.add((CRL)next);
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                final Object next2 = iterator.next();
                if (next2 instanceof CRL && crlSelector.match((CRL)next2)) {
                    list.add((CRL)next2);
                }
            }
        }
        return list;
    }
}
