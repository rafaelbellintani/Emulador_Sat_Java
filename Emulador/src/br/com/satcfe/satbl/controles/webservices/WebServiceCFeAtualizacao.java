// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemAtualizacao;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeAtualizacao extends WebService
{
    private String hash;
    private String urlAtualizacao;
    private String xServ;
    private String urlWebService;
    private String cmd;
    private String nomeMetodo;
    
    public WebServiceCFeAtualizacao() {
        this.hash = null;
        this.urlAtualizacao = null;
        this.xServ = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeAtualizacao;
        this.cmd = null;
        this.nomeMetodo = null;
        this.nomeMetodo = "cfeAtualiza";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeAtualizacao";
        this.soapAction = this.nameSpace;
    }
    
    public boolean consumirWebService() {
        try {
            final URL url = new URL(this.urlWebService);
            Conexao conexao = null;
            if (Configuracoes.SAT.protocolo.equals("http")) {
                conexao = new ConexaoSoapHttp();
            }
            else if (Configuracoes.SAT.protocolo.equals("https")) {
                conexao = new ConexaoSoapHttps();
            }
            conexao.setRequest(this.gerarSOAP());
            conexao.setTimeout(this.timeout);
            if (!conexao.consumir(url, this.soapAction)) {
                throw new ErroComunicacaoRetaguardaException();
            }
            this.responseCode = conexao.getResponseCode();
            this.responseMessage = conexao.getResponseMessage();
            return this.tratarRetorno(conexao.getResponse());
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String gerarSOAP() {
        final MensagemAtualizacao msg = new MensagemAtualizacao();
        msg.setxServ(this.xServ);
        final SoapRequest soap = new SoapRequest(this.nomeMetodo);
        soap.setNameSpace(this.nameSpace);
        soap.setMensagem(msg.getMensagem());
        final String request = soap.getRequest();
        ControleLogs.debugar("Entrada-" + this.nomeMetodo + ".xml", request);
        return request;
    }
    
    private boolean tratarRetorno(String retorno) {
        try {
            retorno = WebService.desformatarDadosSOAP(retorno);
            ControleLogs.debugar("Retorno-" + this.nomeMetodo + ".xml", retorno);
            this.salvarUltimaComunicacao();
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retAtualiza"), "infAtualiza")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
            this.cmd = ControleDados.getConteudoTAG(retorno, "cmd");
            this.setExisteComando(this.cmd != null && this.cmd.equals("1"));
            this.urlAtualizacao = ControleDados.getConteudoTAG(retorno, "url");
            this.hash = ControleDados.getConteudoTAG(retorno, "sha");
            this.urlAtualizacao = ((this.urlAtualizacao == null) ? null : this.urlAtualizacao.replace("&amp;", "&"));
            return true;
        }
        catch (Exception e) {
            this.cStat = "999";
            return false;
        }
    }
    
    public String getxServ() {
        return this.xServ;
    }
    
    public void setxServ(final String xServ) {
        this.xServ = xServ;
    }
    
    public String getUrlAtualizacao() {
        return this.urlAtualizacao;
    }
    
    public String getHash() {
        return this.hash;
    }
}
