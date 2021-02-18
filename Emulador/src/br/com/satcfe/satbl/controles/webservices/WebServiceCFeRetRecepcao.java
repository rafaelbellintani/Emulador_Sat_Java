// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemRetRecepcao;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import java.util.List;

public class WebServiceCFeRetRecepcao extends WebService
{
    private String nRec;
    private String urlWebService;
    public String cmd;
    private List listaRetCFe;
    public String nomeMetodo;
    
    public WebServiceCFeRetRecepcao() {
        this.nRec = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeRetRecepcao;
        this.cmd = null;
        this.listaRetCFe = null;
        this.nomeMetodo = null;
        this.nomeMetodo = "cfeRetRecepcao";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeRetRecepcao";
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
        final MensagemRetRecepcao msg = new MensagemRetRecepcao();
        msg.setnRec(this.nRec);
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
            this.listaRetCFe = ControleDados.getListaTags(retorno, "retCFe");
            if (this.listaRetCFe == null || this.listaRetCFe.size() == 0) {
                System.out.println("Campo <retCFe> n\u00e3o encontrado");
            }
            else {
                for (int i = 0; i < this.listaRetCFe.size(); ++i) {
                    if (!ControleAssinaturaXML.validarAssinaturaXml(this.listaRetCFe.get(i).toString(), "infReci")) {
                        ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                        return false;
                    }
                }
            }
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
    
    public String getnRec() {
        return this.nRec;
    }
    
    public void setnRec(final String nRec) {
        this.nRec = nRec;
    }
    
    public List getListaRetCFe() {
        return this.listaRetCFe;
    }
}
