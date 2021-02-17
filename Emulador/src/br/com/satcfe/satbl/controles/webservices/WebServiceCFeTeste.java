// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemTeste;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeTeste extends WebService
{
    private String optTeste;
    private String idLote;
    private String CFe;
    private String cmd;
    private String nomeMetodo;
    private String urlWebService;
    
    public WebServiceCFeTeste() {
        this.optTeste = null;
        this.idLote = null;
        this.CFe = null;
        this.cmd = null;
        this.nomeMetodo = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeTeste;
        this.nomeMetodo = "cfeTeste";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeTeste";
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
        final MensagemTeste msg = new MensagemTeste();
        msg.setOptTeste(this.optTeste);
        msg.setIdLote(this.idLote);
        msg.setCFeTeste(this.CFe);
        final SoapRequest soap = new SoapRequest(this.nomeMetodo);
        soap.setMensagem(msg.getMensagem());
        soap.setNameSpace(this.nameSpace);
        final String request = soap.getRequest();
        ControleLogs.debugar("Entrada-" + this.nomeMetodo + ".xml", request.toString());
        return request;
    }
    
    private boolean tratarRetorno(String retorno) {
        try {
            retorno = WebService.desformatarDadosSOAP(retorno);
            ControleLogs.debugar("Retorno-" + this.nomeMetodo + ".xml", retorno);
            this.salvarUltimaComunicacao();
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retTeste"), "infTeste")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
            this.cmd = ControleDados.getConteudoTAG(retorno, "cmd");
            this.setExisteComando(this.cmd != null && this.cmd.equals("1"));
            return true;
        }
        catch (Exception e) {
            this.cStat = "999";
            return false;
        }
    }
    
    public String getOptTeste() {
        return this.optTeste;
    }
    
    public void setOptTeste(final String optTeste) {
        this.optTeste = optTeste;
    }
    
    public String getIdLote() {
        return this.idLote;
    }
    
    public void setIdLote(final String idLote) {
        this.idLote = idLote;
    }
    
    public void setCFe(final String cFe) {
        this.CFe = cFe;
    }
}
