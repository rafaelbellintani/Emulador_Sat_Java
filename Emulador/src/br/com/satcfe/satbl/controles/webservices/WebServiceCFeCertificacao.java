// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemCertificacao;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeCertificacao extends WebService
{
    private String opt;
    private String CRT;
    private String CSR;
    private String cert;
    private String conf;
    private String nomeMetodo;
    private String urlWebService;
    
    public WebServiceCFeCertificacao() {
        this.opt = null;
        this.CRT = null;
        this.CSR = null;
        this.cert = null;
        this.conf = "";
        this.nomeMetodo = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeCertificacao;
        this.nomeMetodo = "cfeCertificacao";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeCertificacao";
        this.soapAction = this.nameSpace;
    }
    
    public boolean consumirWebService() {
        try {
            if (this.opt == null || (this.CSR == null && this.CRT == null)) {
                return false;
            }
            final URL url = new URL(this.urlWebService);
            Conexao conexao = null;
            if (Configuracoes.SAT.protocolo.equals("http")) {
                conexao = new ConexaoSoapHttp();
            }
            else if (Configuracoes.SAT.protocolo.equals("https")) {
                conexao = new ConexaoSoapHttps();
                ((ConexaoSoapHttps)conexao).setAutenticacaoMutua(false);
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
        final MensagemCertificacao msg = new MensagemCertificacao();
        msg.setCRT(this.CRT);
        msg.setCSR(this.CSR);
        msg.setOpt(this.opt);
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
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retCertifica"), "infCert")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.conf = ControleDados.getConteudoTAG(retorno, "conf");
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
            this.cert = ControleDados.getConteudoTAG(retorno, "cert");
            return true;
        }
        catch (Exception e) {
            this.cStat = "999";
            this.cert = null;
            this.conf = null;
            return false;
        }
    }
    
    public void setOpt(final String opt) {
        this.opt = opt;
    }
    
    public String getCRT() {
        return this.CRT;
    }
    
    public void setCRT(final String cRT) {
        this.CRT = cRT;
    }
    
    public String getCSR() {
        return this.CSR;
    }
    
    public void setCSR(final String cSR) {
        this.CSR = cSR;
    }
    
    public String getCert() {
        return this.cert;
    }
    
    public String getConf() {
        return this.conf;
    }
}
