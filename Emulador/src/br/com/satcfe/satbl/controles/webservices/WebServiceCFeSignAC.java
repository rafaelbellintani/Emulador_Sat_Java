// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemSignAC;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeSignAC extends WebService
{
    private String CNPJValue;
    private String signAC;
    private String urlWebService;
    private String nomeMetodo;
    private String cmd;
    
    public WebServiceCFeSignAC() {
        this.CNPJValue = null;
        this.signAC = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeSignAC;
        this.nomeMetodo = null;
        this.cmd = null;
        this.nomeMetodo = "cfeSignAC";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeSignAC";
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
        final MensagemSignAC msg = new MensagemSignAC();
        msg.setCNPJValue(this.CNPJValue);
        msg.setSignAC(this.signAC);
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
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retAss"), "infAss")) {
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
    
    public String getCNPJValue() {
        return this.CNPJValue;
    }
    
    public void setCNPJValue(final String cNPJValue) {
        this.CNPJValue = cNPJValue;
    }
    
    public String getSignAC() {
        return this.signAC;
    }
    
    public void setSignAC(final String signAC) {
        this.signAC = signAC;
    }
}
