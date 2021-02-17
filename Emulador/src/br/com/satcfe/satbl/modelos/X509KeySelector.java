// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import javax.xml.crypto.KeySelectorException;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.security.KeyStore;
import javax.xml.crypto.KeySelector;

public class X509KeySelector extends KeySelector
{
    private KeyStore ks;
    private String senha;
    
    public X509KeySelector(final KeyStore ks, final String senha) {
        this.ks = ks;
        this.senha = senha;
    }
    
    @Override
    public KeySelectorResult select(final KeyInfo keyInfo, final Purpose purpose, final AlgorithmMethod method, final XMLCryptoContext context) throws KeySelectorException {
        try {
            final KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry)this.ks.getEntry("SAT", new KeyStore.PasswordProtection(this.senha.toCharArray()));
            final X509Certificate cert = (X509Certificate)keyEntry.getCertificate();
            final PublicKey key = cert.getPublicKey();
            if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
                return new KeySelectorResult() {
                    @Override
                    public Key getKey() {
                        return key;
                    }
                };
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new KeySelectorException("No key found!");
    }
    
    static boolean algEquals(final String algURI, final String algName) {
        return (algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase("http://www.w3.org/2000/09/xmldsig#dsa-sha1")) || (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase("http://www.w3.org/2000/09/xmldsig#rsa-sha1"));
    }
}
