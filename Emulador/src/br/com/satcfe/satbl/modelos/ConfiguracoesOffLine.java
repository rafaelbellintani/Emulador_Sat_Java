// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.Configuracoes.SAT;
import br.com.satcfe.satbl.Configuracoes.SistemaArquivos;

public class ConfiguracoesOffLine
{
    private static ConfiguracoesOffLine instance;
    private int atualizarSoftwareBasico;
    public static final int ATUALIZACAO_PENDENTE_SUCESSO = 1;
    public static final int ATUALIZACAO_PENDENTE_FALHA = 2;
    public static final int ATUALIZACAO_FORCADA_SEFAZ = 3;
    private String avisoUsuario;
    private boolean habilitarRenovacaoCert;
    private boolean habilitarRenovacaoAviso;
    private boolean habilitarBloqueioSefaz;
    private boolean habilitarBloqueioContribuinte;
    private boolean habilitarDesativacao;
    private boolean habilitarBloqueioAutonomo;
    
    static {
        ConfiguracoesOffLine.instance = null;
    }
    
    public static ConfiguracoesOffLine getInstance() {
        if (ConfiguracoesOffLine.instance == null) {
            ConfiguracoesOffLine.instance = new ConfiguracoesOffLine();
        }
        return ConfiguracoesOffLine.instance;
    }
    
    private ConfiguracoesOffLine() {
        this.atualizarSoftwareBasico = 0;
        this.avisoUsuario = null;
        this.habilitarRenovacaoCert = false;
        this.habilitarRenovacaoAviso = false;
        this.habilitarBloqueioSefaz = false;
        this.habilitarBloqueioContribuinte = false;
        this.habilitarDesativacao = false;
        this.habilitarBloqueioAutonomo = false;
        try {
        		if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOES_OFFLINE)) {
        			final String xml = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOES_OFFLINE);
                    final Document doc = ControleDados.parseXML(xml);
                    final NodeList raizes = doc.getChildNodes();
                    this.parse(raizes.item(0));
                }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    private void parse(final Node raiz) {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("atualizacao")) {
                try {
                    this.atualizarSoftwareBasico = Integer.parseInt(filhoAtual.getTextContent());
                }
                catch (Exception e) {
                    this.atualizarSoftwareBasico = 0;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("aviso")) {
                if (filhoAtual.getTextContent().trim().length() > 0) {
                    this.avisoUsuario = filhoAtual.getTextContent();
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("renovacao")) {
                if (filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                    this.habilitarRenovacaoCert = true;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("renovacaoAviso")) {
                if (filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                    this.habilitarRenovacaoAviso = true;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("bloqueioSefaz")) {
                if (filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                    this.habilitarBloqueioSefaz = true;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("bloqueioContribuinte")) {
                if (filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                    this.habilitarBloqueioContribuinte = true;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cessacao")) {
                if (filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                    this.habilitarDesativacao = true;
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("bloqueioAutonomo") && filhoAtual.getTextContent().trim().equalsIgnoreCase("true")) {
                this.habilitarBloqueioAutonomo = true;
            }
        }
    }
    
    public boolean getHabilitarBloqueioAutonomo() {
        return this.habilitarBloqueioAutonomo;
    }
    
    public boolean getHabilitarDesativacao() {
        return this.habilitarDesativacao;
    }
    
    public int getAtualizarSoftwareBasico() {
        return this.atualizarSoftwareBasico;
    }
    
    public boolean getHabilitarRenovacaoAviso() {
        return this.habilitarRenovacaoAviso;
    }
    
    public void setHabilitarRenovacaoAviso(final boolean renovacaoAviso) {
        this.habilitarRenovacaoAviso = renovacaoAviso;
    }
    
    public boolean getHabilitarRenovacao() {
        return this.habilitarRenovacaoCert;
    }
    
    public void setAvisoUsuario(final String avisoUsuario) {
        this.avisoUsuario = avisoUsuario;
    }
    
    public String getAvisoUsuario() {
        return this.avisoUsuario;
    }
    
    public void setHabilitarBloqueioAutonomo(final boolean habilitarBloqueioAutonomo) {
        this.habilitarBloqueioAutonomo = habilitarBloqueioAutonomo;
    }
    
    public void setHabilitarDesativacao(final boolean habilitarDesativacao) {
        this.habilitarDesativacao = habilitarDesativacao;
    }
    
    public void setHabilitarRenovacao(final boolean habilitarRenovacao) {
        this.habilitarRenovacaoCert = habilitarRenovacao;
    }
    
    public void setAtualizarSoftwareBasico(final int atualizarSoftwareBasico) {
        this.atualizarSoftwareBasico = atualizarSoftwareBasico;
    }
    
    public void setHabilitarBloqueioContribuinte(final boolean habilitarBloqueioContribuinte) {
        this.habilitarBloqueioContribuinte = habilitarBloqueioContribuinte;
    }
    
    public void setHabilitarBloqueioSefaz(final boolean habilitarBloqueioSefaz) {
        this.habilitarBloqueioSefaz = habilitarBloqueioSefaz;
    }
    
    public boolean getHabilitarBloqueioContribuinte() {
        return this.habilitarBloqueioContribuinte;
    }
    
    public boolean getHabilitarBloqueioSefaz() {
        return this.habilitarBloqueioSefaz;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<configuracoes>");
        sb.append("<atualizacao>").append(this.atualizarSoftwareBasico).append("</atualizacao>");
        sb.append("<renovacao>").append(this.habilitarRenovacaoCert).append("</renovacao>");
        sb.append("<renovacaoAviso>").append(this.habilitarRenovacaoAviso).append("</renovacaoAviso>");
        sb.append("<bloqueioSefaz>").append(this.habilitarBloqueioSefaz).append("</bloqueioSefaz>");
        sb.append("<bloqueioContribuinte>").append(this.habilitarBloqueioContribuinte).append("</bloqueioContribuinte>");
        sb.append("<cessacao>").append(this.habilitarDesativacao).append("</cessacao>");
        sb.append("<bloqueioAutonomo>").append(this.habilitarBloqueioAutonomo).append("</bloqueioAutonomo>");
        sb.append("</configuracoes>");
        return sb.toString();
    }
}
