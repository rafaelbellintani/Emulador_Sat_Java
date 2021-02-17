// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemStatus;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeStatus extends WebService
{
    private String xServ;
    private String urlWebService;
    private String nomeMetodo;
    private String status;
    public String dtRecibo;
    public String cmd;
    public static final String STATUS_SEFAZ = "STATUS";
    public static final String STATUS_SAT = "STATUS-SAT";
    
    public WebServiceCFeStatus(final String servico) {
        this.xServ = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeStatus;
        this.nomeMetodo = null;
        this.status = null;
        this.dtRecibo = null;
        this.cmd = null;
        this.xServ = servico;
        this.nomeMetodo = "cfeStatus";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeStatus";
        this.soapAction = this.nameSpace;
    }
    
    public boolean consumirWebService() {
        try {
            if (this.xServ.equals("STATUS-SAT") && this.status == null) {
                return false;
            }
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
        final MensagemStatus msg = new MensagemStatus();
        msg.setxServ(this.xServ);
        msg.setStatus(this.status);
        final SoapRequest soap = new SoapRequest(this.nomeMetodo);
        soap.setMensagem(msg.getMensagem());
        soap.setNameSpace(this.nameSpace);
        final String request = soap.getRequest();
        ControleLogs.debugar("Entrada-" + this.nomeMetodo + "_" + this.xServ + ".xml", request.toString());
        return request;
    }
    
    private boolean tratarRetorno(String retorno) {
        try {
            retorno = WebService.desformatarDadosSOAP(retorno);
            ControleLogs.debugar("Retorno-" + this.nomeMetodo + "_" + this.xServ + ".xml", retorno);
            this.salvarUltimaComunicacao();
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retConsStat"), "infStat")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.dtRecibo = ControleDados.getConteudoTAG(retorno, "dhRecbto");
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
            this.cmd = ControleDados.getConteudoTAG(retorno, "cmd");
            this.setExisteComando(this.cmd != null && this.cmd.equals("1"));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.cStat = "999";
            return false;
        }
    }
    
    public String getxServ() {
        return this.xServ;
    }
    
    public String getDataRecibo() {
        return this.dtRecibo;
    }
    
    public void setxServ(final String xServ) {
        this.xServ = xServ;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
}
