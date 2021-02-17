// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

import java.util.ArrayList;
import java.security.cert.X509Certificate;
import java.util.List;

public class Certificado
{
    private boolean raiz;
    private Certificado pai;
    private List<Certificado> filhos;
    private X509Certificate cert;
    
    public Certificado(final X509Certificate cert) {
        this.raiz = false;
        this.pai = null;
        this.filhos = null;
        this.cert = null;
        this.cert = cert;
    }
    
    public Certificado(final X509Certificate cert, final boolean raiz) {
        this.raiz = false;
        this.pai = null;
        this.filhos = null;
        this.cert = null;
        this.cert = cert;
        this.raiz = raiz;
    }
    
    public void setRaiz(final boolean raiz) {
        this.raiz = raiz;
    }
    
    public boolean isRaiz() {
        return this.raiz;
    }
    
    public Certificado getPai() {
        return this.pai;
    }
    
    public List<Certificado> getFilhos() {
        return this.filhos;
    }
    
    public void setPai(final Certificado pai) {
        this.pai = pai;
    }
    
    public void setFilhos(final List<Certificado> filhos) {
        this.filhos = filhos;
    }
    
    public void addFilho(final Certificado filho) {
        if (this.filhos == null) {
            this.filhos = new ArrayList<Certificado>();
        }
        filho.setPai(this);
        this.filhos.add(filho);
    }
    
    public boolean ehFilho(final X509Certificate cert) {
        try {
            if (this.cert.getSubjectDN().getName().equals(cert.getIssuerDN().getName())) {
                cert.verify(this.cert.getPublicKey());
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public boolean ehFilho(final Certificado cert) {
        return this.ehFilho(cert.getCert());
    }
    
    public X509Certificate getCert() {
        return this.cert;
    }
    
    public String getName() {
        return this.cert.getSubjectDN().getName();
    }
}
