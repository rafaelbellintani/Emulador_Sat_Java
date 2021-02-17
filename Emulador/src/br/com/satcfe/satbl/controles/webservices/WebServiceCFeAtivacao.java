// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemAtivacao;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class WebServiceCFeAtivacao extends WebService
{
    private String CNPJ;
    private String cUF;
    private String nomeMetodo;
    private String paramAtivacao;
    private String urlWebService;
    
    public WebServiceCFeAtivacao() {
        this.CNPJ = null;
        this.cUF = null;
        this.nomeMetodo = null;
        this.paramAtivacao = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeAtivacao;
        this.nomeMetodo = "cfeAtivacao";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeAtivacao";
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
        final MensagemAtivacao msg = new MensagemAtivacao();
        msg.setCNPJ(this.CNPJ);
        msg.setcUF(this.cUF);
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
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retAtiva"), "infAtiva")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            this.paramAtivacao = ControleDados.getConteudoTAG(retorno, "paramAtiva");
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
        }
        catch (Exception e) {
            this.cStat = "999";
            return false;
        }
        return true;
    }
    
    public String getCNPJ() {
        return this.CNPJ;
    }
    
    public void setCNPJ(final String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    public void setUF(final String UF) {
        this.cUF = UF;
    }
    
    public String getParamAtivacao() {
        return this.paramAtivacao;
    }
}
