// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemCancelamento;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeCancelamento extends WebService
{
    private String idLote;
    private String loteCFesCanc;
    private String urlWebService;
    public String cmd;
    public String nRec;
    public String nomeMetodo;
    
    public WebServiceCFeCancelamento() {
        this.idLote = null;
        this.loteCFesCanc = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeCancelamento;
        this.cmd = null;
        this.nRec = null;
        this.nomeMetodo = null;
        this.nomeMetodo = "cfeCancelamento";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeCancelamento";
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
        final MensagemCancelamento msg = new MensagemCancelamento();
        msg.setIdLote(this.idLote);
        msg.setLote(this.loteCFesCanc);
        final SoapRequest soap = new SoapRequest(this.nomeMetodo);
        soap.setMensagem(msg.getMensagem());
        soap.setNameSpace(this.nameSpace);
        final String request = soap.getRequest();
        ControleLogs.debugar("Entrada-" + this.nomeMetodo + ".xml", request);
        return request;
    }
    
    private boolean tratarRetorno(String retorno) {
        try {
            retorno = WebService.desformatarDadosSOAP(retorno);
            ControleLogs.debugar("Retorno-" + this.nomeMetodo + ".xml", retorno);
            this.salvarUltimaComunicacao();
            ControleSeguranca.salvarDataHoraTransmissao();
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retCanc"), "infRet")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.nRec = ControleDados.getConteudoTAG(retorno, "nRec");
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
    
    public String getIdLote() {
        return this.idLote;
    }
    
    public void setIdLote(final String idLote) {
        this.idLote = idLote;
    }
    
    public String getCFesCanc() {
        return this.loteCFesCanc;
    }
    
    public void setLoteCFesCanc(final String CFesCanc) {
        this.loteCFesCanc = CFesCanc;
    }
    
    public String getnRec() {
        return this.nRec;
    }
}
