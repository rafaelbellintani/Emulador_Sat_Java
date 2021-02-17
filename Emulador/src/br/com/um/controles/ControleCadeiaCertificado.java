// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.util.Collections;
import java.security.cert.Certificate;
import java.util.Date;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import br.com.um.modelos.Certificado;
import java.util.List;
import java.util.HashMap;

public class ControleCadeiaCertificado
{
    private static ControleCadeiaCertificado instance;
    private boolean cadeiaCompleta;
    private HashMap<String, Boolean> tabelaCertificados;
    private List<Certificado> listaRaizes;
    private List<Certificado> listaCertificadosSemRaiz;
    
    static {
        ControleCadeiaCertificado.instance = null;
    }
    
    public static ControleCadeiaCertificado getInstance() {
        if (ControleCadeiaCertificado.instance == null) {
            ControleCadeiaCertificado.instance = new ControleCadeiaCertificado();
        }
        return ControleCadeiaCertificado.instance;
    }
    
    public static ControleCadeiaCertificado createNewInstance() {
        return new ControleCadeiaCertificado();
    }
    
    private ControleCadeiaCertificado() {
        this.cadeiaCompleta = true;
        this.tabelaCertificados = null;
        this.listaRaizes = null;
        this.listaCertificadosSemRaiz = null;
        this.listaRaizes = new ArrayList<Certificado>();
        this.listaCertificadosSemRaiz = new ArrayList<Certificado>();
        this.tabelaCertificados = new HashMap<String, Boolean>();
    }
    
    public void addCertificado(final X509Certificate cert) {
        if (this.tabelaCertificados.containsKey(this.getKey(cert))) {
            System.err.println("certificado j\u00e1 foi adicionado na tabela. [" + this.getKey(cert) + "]");
            return;
        }
        this.tabelaCertificados.put(this.getKey(cert), true);
        if (this.certificadoRaiz(cert)) {
            this.listaRaizes.add(new Certificado(cert, true));
        }
        else {
            for (final Certificado raiz : this.listaRaizes) {
                if (this.inserirNaCadeia(cert, raiz)) {
                    return;
                }
            }
            this.listaCertificadosSemRaiz.add(new Certificado(cert));
        }
        this.refatorar();
    }
    
    private boolean inserirNaCadeia(final X509Certificate cert, final Certificado cadeia) {
        if (cadeia.ehFilho(cert)) {
            cadeia.addFilho(new Certificado(cert));
            return true;
        }
        if (cadeia.getFilhos() != null && cadeia.getFilhos().size() > 0) {
            for (final Certificado filho : cadeia.getFilhos()) {
                if (this.inserirNaCadeia(cert, filho)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void refatorar() {
        if (this.listaRaizes.size() == 0) {
            return;
        }
        boolean adicionou = false;
        for (int i = 0; i < this.listaCertificadosSemRaiz.size(); ++i) {
            for (final Certificado raiz : this.listaRaizes) {
                if (this.inserirNaCadeia(this.listaCertificadosSemRaiz.get(i).getCert(), raiz)) {
                    adicionou = true;
                    this.listaCertificadosSemRaiz.remove(i);
                    break;
                }
            }
        }
        if (adicionou) {
            this.refatorar();
        }
    }
    
    private boolean certificadoRaiz(final X509Certificate cert) {
        try {
            if (cert.getIssuerDN().getName().equals(cert.getSubjectDN().getName())) {
                cert.verify(cert.getPublicKey());
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    @Deprecated
    public boolean pertenceCadeia(final X509Certificate cert) {
        if (this.listaRaizes.size() > 0) {
            for (final Certificado raiz : this.listaRaizes) {
                if (raiz.ehFilho(cert)) {
                    return true;
                }
                if (raiz.getFilhos() != null && raiz.getFilhos().size() > 0 && this.verificaFilhos(raiz, cert)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Certificado procurarEmissor(final X509Certificate cert) {
        try {
            for (final Certificado raiz : this.listaRaizes) {
                if (raiz.ehFilho(cert)) {
                    return raiz;
                }
                if (raiz.getFilhos() == null || raiz.getFilhos().size() <= 0) {
                    continue;
                }
                final Certificado ret = this.procuraEmissor(raiz, cert);
                if (ret != null) {
                    return ret;
                }
            }
            if (this.cadeiaCompleta) {
                return null;
            }
            for (final Certificado intermediario : this.listaCertificadosSemRaiz) {
                if (intermediario.ehFilho(cert)) {
                    return intermediario;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Certificado procuraEmissor(final Certificado pai, final X509Certificate cert) {
        for (final Certificado filhoAtual : pai.getFilhos()) {
            if (filhoAtual.ehFilho(cert)) {
                return filhoAtual;
            }
            if (filhoAtual.getFilhos() == null || filhoAtual.getFilhos().size() <= 0) {
                continue;
            }
            final Certificado ret = this.procuraEmissor(filhoAtual, cert);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
    
    @Deprecated
    private boolean verificaFilhos(final Certificado pai, final X509Certificate cert) {
        if (pai.getFilhos() != null && pai.getFilhos().size() > 0) {
            for (int i = 0; i < pai.getFilhos().size(); ++i) {
                final Certificado filhoAtual = pai.getFilhos().get(i);
                if (filhoAtual.ehFilho(cert)) {
                    return true;
                }
                if (filhoAtual.getFilhos() != null && filhoAtual.getFilhos().size() > 0 && this.verificaFilhos(filhoAtual, cert)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean removerRaiz(final String subjectDN) {
        if (subjectDN == null || this.listaRaizes.size() == 0) {
            return false;
        }
        for (int i = 0; i < this.listaRaizes.size(); ++i) {
            if (this.listaRaizes.get(i).getName().equals(subjectDN)) {
                this.listaRaizes.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public void exibirCadeia(final OutputStream out) {
        try {
            if (this.listaRaizes.size() == 0) {
                out.write("N\u00e3o existe nenhum Certificado Raiz Cadastrado!".getBytes());
            }
            for (int i = 0; i < this.listaRaizes.size(); ++i) {
                this.exibirFilhos(this.listaRaizes.get(i), 1, out);
            }
            if (this.listaCertificadosSemRaiz != null && this.listaCertificadosSemRaiz.size() > 0) {
                out.write("\r\n===========Lista de Certificados que n\u00e3o possuem nenhum Certificado Raiz==============\r\n".getBytes());
                for (int i = 0; i < this.listaCertificadosSemRaiz.size(); ++i) {
                    this.exibirFilhos(this.listaCertificadosSemRaiz.get(i), 0, out);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void exibirCadeia() {
        this.exibirCadeia(System.out);
    }
    
    private void exibirFilhos(final Certificado cadeia, final int hierarquia, final OutputStream out) throws IOException {
        this.printName(cadeia.getName(), hierarquia, out);
        out.write("\r\n".getBytes());
        if (cadeia.getFilhos() != null && cadeia.getFilhos().size() > 0) {
            for (int i = 0; i < cadeia.getFilhos().size(); ++i) {
                this.exibirFilhos(cadeia.getFilhos().get(i), hierarquia + 1, out);
            }
        }
    }
    
    private void printName(final String nome, final int n, final OutputStream out) throws IOException {
        final StringBuffer sb = new StringBuffer();
        if (n == 1) {
            sb.append("Raiz:");
        }
        else {
            for (int i = 0; i < n; ++i) {
                if (i == n - 1) {
                    sb.append("|____");
                }
                else {
                    sb.append("     ");
                }
            }
            out.write(sb.toString().replace("_", " ").getBytes());
            out.write("\r\n".getBytes());
        }
        sb.append(nome);
        out.write(sb.toString().getBytes());
    }
    
    public void limpar() {
        this.listaRaizes = new ArrayList<Certificado>();
        this.listaCertificadosSemRaiz = new ArrayList<Certificado>();
        this.tabelaCertificados = new HashMap<String, Boolean>();
    }
    
    private String getKey(final X509Certificate cert) {
        return cert.getSubjectDN() + ":" + cert.getSerialNumber().toString();
    }
    
    public boolean validarData(final X509Certificate cert) {
        try {
            cert.checkValidity(new Date(ControleTempo.getCurrentTime()));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean validarDataCadeia(final Certificado cadeia) {
        try {
            if (!this.validarData(cadeia.getCert())) {
                return false;
            }
            if (cadeia.getPai() != null) {
                return this.validarDataCadeia(cadeia.getPai());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void setCadeiaCompleta(final boolean cadeiaCompleta) {
        this.cadeiaCompleta = cadeiaCompleta;
    }
    
    public Certificate[] cadeiaToArray() {
        final List<Certificate> l = new ArrayList<Certificate>();
        Certificado cc = this.listaRaizes.get(0);
        try {
            while (true) {
                l.add(cc.getCert());
                if (cc.getFilhos() == null) {
                    break;
                }
                cc = cc.getFilhos().get(0);
            }
        }
        catch (Exception ex) {}
        Collections.reverse(l);
        final Certificate[] t = new Certificate[l.size()];
        return l.toArray(t);
    }
}
