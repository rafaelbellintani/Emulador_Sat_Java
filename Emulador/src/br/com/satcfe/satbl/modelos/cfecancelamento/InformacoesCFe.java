// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import br.com.um.controles.ControleDados;
import java.security.PrivateKey;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.controles.ControladorCancelamento;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesCFe
{
    private String versao;
    private String id;
    private IdentificacaoCFe ide;
    private IdentificacaoEmitenteCFe emit;
    private IdentificacaoDestinatarioCFe dest;
    private ValoresTotaisCFe total;
    private String chCanc;
    private String dEmi;
    private String hEmi;
    
    public InformacoesCFe(final Node no) {
        this.versao = null;
        this.id = null;
        this.ide = null;
        this.emit = null;
        this.dest = null;
        this.total = null;
        this.chCanc = null;
        this.dEmi = null;
        this.hEmi = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("ide")) {
                this.ide = new IdentificacaoCFe(filhoAtual);
            }
            if (filhoAtual.getNodeName().equalsIgnoreCase("emit")) {
                this.emit = new IdentificacaoEmitenteCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("dest")) {
                this.dest = new IdentificacaoDestinatarioCFe(filhoAtual);
            }
        }
        final NamedNodeMap atributos = no.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("chCanc")) {
                this.chCanc = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getVersao() {
        return this.versao;
    }
    
    public void setVersao(final String versao) {
        this.versao = versao;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public IdentificacaoCFe getIde() {
        return this.ide;
    }
    
    public void setIde(final IdentificacaoCFe ideCFe) {
        this.ide = ideCFe;
    }
    
    public IdentificacaoEmitenteCFe getEmit() {
        return this.emit;
    }
    
    public void setEmit(final IdentificacaoEmitenteCFe emitCfe) {
        this.emit = emitCfe;
    }
    
    public ValoresTotaisCFe getTotal() {
        return this.total;
    }
    
    public void setTotal(final ValoresTotaisCFe totalCFe) {
        this.total = totalCFe;
    }
    
    public IdentificacaoDestinatarioCFe getDest() {
        return this.dest;
    }
    
    public String getChCanc() {
        return this.chCanc;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.ide == null) {
            resultado = "1999";
        }
        else if (!(resultado = this.ide.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'ide': " + resultado);
        }
        else if (this.dest == null) {
            resultado = "1999";
        }
        else if (!(resultado = this.dest.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'dest': " + resultado);
        }
        else if (this.chCanc == null || this.chCanc.length() != 47) {
            ControleLogs.logar("Erro: Chave de cancelamento inv\u00e1lida.");
            resultado = "1999";
        }
        return resultado;
    }
    
    public void completar(final String chaveAcessoCancelar) {
        if (this.ide == null) {
            this.ide = new IdentificacaoCFe();
        }
        this.ide.completar();
        this.versao = Configuracoes.SAT.VERSAO_LAYOUT_CFE;
        final String data = this.ide.getdEmi();
        final String[] dadosCancelamento = ControladorCancelamento.recuperarCFeParaCancelamento(chaveAcessoCancelar);
        this.chCanc = dadosCancelamento[0];
        this.total = new ValoresTotaisCFe(dadosCancelamento[1]);
        if (this.emit == null) {
            this.emit = new IdentificacaoEmitenteCFe();
        }
        this.emit.completar(dadosCancelamento[3]);
        this.dEmi = dadosCancelamento[4];
        this.hEmi = dadosCancelamento[5];
        final String cpfCnpjDest = dadosCancelamento[6];
        final StringBuilder chave = new StringBuilder("CFe");
        chave.append(this.ide.getcUF()).append(data.substring(2, 4)).append(data.subSequence(4, 6));
        chave.append(Configuracoes.SAT.CNPJEstabelecimento).append(this.ide.getMod());
        chave.append(this.ide.getNserieSAT()).append(this.ide.getnCFe());
        chave.append(this.ide.getcNF()).append(this.ide.getcDV());
        this.id = chave.toString();
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) > 0.06 && cpfCnpjDest != null && cpfCnpjDest.length() > 0) {
            if (cpfCnpjDest.length() == 11) {
                this.dest.setCPF(cpfCnpjDest);
            }
            else if (cpfCnpjDest.length() == 14) {
                this.dest.setCNPJ(cpfCnpjDest);
            }
        }
        String cpfCnpj = "";
        if ((cpfCnpj = this.dest.getCNPJ()) == null) {
            cpfCnpj = this.dest.getCPF();
        }
        if (cpfCnpj == null || cpfCnpj.trim().length() < 11) {
            cpfCnpj = "";
        }
        final String campoQRCode = this.gerarCampoQRCode(this.id, String.valueOf(this.ide.getdEmi()) + this.ide.gethEmi(), this.total.getvCFe(), cpfCnpj);
        ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_DEBUG) + "qrCode.txt", campoQRCode.toCharArray());
        try {
            final ControleKeyStore keyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            keyStore.carregarKeyStore();
            String assinaturaQRCode = keyStore.gerarAssinaturaSHA256((PrivateKey)keyStore.getPrivateKey(), campoQRCode.getBytes());
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) == 0.03 && assinaturaQRCode.length() == 344) {
                assinaturaQRCode = String.valueOf(assinaturaQRCode) + "hygljwuohmmoewarfnmighlxzke7k2bjlto4sb2vltorgm26khhangknnfvpzydt5terudyw5vuvtzlhlqs3qrzvplfnlvw==";
            }
            this.ide.setQrCode(assinaturaQRCode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String gerarCampoQRCode(final String chave, final String timeStamp, final String valor, final String cpfCnpj) {
        final StringBuffer s = new StringBuffer();
        s.append(chave.replace("CFe", "")).append("|");
        s.append(timeStamp).append("|");
        s.append(valor).append("|");
        s.append(cpfCnpj);
        return s.toString();
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.versao != null) {
            retorno.append("<infCFe versao=\"").append(ControleDados.formatarDouble(this.versao, 1, 4, 2)).append("\"");
        }
        if (this.id != null) {
            retorno.append(" Id=\"").append(this.id).append("\"");
        }
        if (this.chCanc != null) {
            retorno.append(" chCanc=\"").append(this.chCanc).append("\">");
        }
        else {
            retorno.append(">");
        }
        if (this.dEmi != null) {
            retorno.append("<dEmi>").append(this.dEmi).append("</dEmi>");
        }
        if (this.hEmi != null) {
            retorno.append("<hEmi>").append(this.hEmi).append("</hEmi>");
        }
        if (this.ide != null) {
            retorno.append("<ide>");
            this.ide.toString(retorno);
            retorno.append("</ide>");
        }
        if (this.emit != null) {
            retorno.append("<emit>");
            this.emit.toString(retorno);
            retorno.append("</emit>");
        }
        if (this.dest != null) {
            retorno.append("<dest>");
            this.dest.toString(retorno);
            retorno.append("</dest>");
        }
        if (this.total != null) {
            retorno.append("<total>");
            this.total.toString(retorno);
            retorno.append("</total>");
        }
        retorno.append("</infCFe>");
    }
}
