// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import java.util.ArrayList;
import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.modelos.soap.SoapRequest;
import br.com.satcfe.satbl.modelos.soap.MensagemComandos;
import br.com.satcfe.satbl.conexao.Conexao;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttps;
import br.com.satcfe.satbl.conexao.ConexaoSoapHttp;
import java.net.URL;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.ComandoSefaz;
import java.util.List;

public class WebServiceCFeComandos extends WebService
{
    private String xServ;
    private String idCmd;
    private String status;
    private List<ComandoSefaz> comandos;
    private String urlWebService;
    private String nomeMetodo;
    public static final String CONSULTA_COMANDOS = "COMANDOS";
    public static final String RESPOSTA = "RESPOSTA";
    
    public WebServiceCFeComandos() {
        this.xServ = null;
        this.idCmd = null;
        this.status = null;
        this.comandos = null;
        this.urlWebService = String.valueOf(Configuracoes.SAT.protocolo) + "://" + Parametrizacoes.endereco[0] + ":" + Parametrizacoes.porta + "/" + Parametrizacoes.CFeComandos;
        this.nomeMetodo = null;
        this.nomeMetodo = "cfeComandos";
        this.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/CfeComandos";
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
        final MensagemComandos msg = new MensagemComandos();
        msg.setIdCmd(this.idCmd);
        msg.setStatus(this.status);
        msg.setxServ(this.xServ);
        final SoapRequest soap = new SoapRequest(this.nomeMetodo);
        soap.setMensagem(msg.getMensagem());
        soap.setNameSpace(this.nameSpace);
        final String request = soap.getRequest();
        if ("2.9.4".indexOf("teste") >= 0) {
            ControleLogs.debugar("Entrada-" + this.nomeMetodo + "_" + this.xServ + ControleTempo.getCurrentTime() + ".xml", request);
        }
        else {
            ControleLogs.debugar("Entrada-" + this.nomeMetodo + "_" + this.xServ + ".xml", request);
        }
        return request;
    }
    
    private boolean tratarRetorno(String retorno) {
        try {
            retorno = WebService.desformatarDadosSOAP(retorno);
            if ("2.9.4".indexOf("teste") >= 0) {
                ControleLogs.debugar("Retorno-" + this.nomeMetodo + "_" + this.xServ + ControleTempo.getCurrentTime() + ".xml", retorno);
            }
            else {
                ControleLogs.debugar("Retorno-" + this.nomeMetodo + "_" + this.xServ + ".xml", retorno);
            }
            this.salvarUltimaComunicacao();
            if (!ControleAssinaturaXML.validarAssinaturaXml(ControleDados.recuperaTag(retorno, "retConsCmd"), "infCmd")) {
                ControleLogs.logar("Assinatura XML do retorno inv\u00e1lida.");
                return false;
            }
            if (retorno.indexOf("<nome>COMANDO") >= 0) {
                this.parseComandos(retorno);
            }
            else {
                this.parseComandosLayoutAntigo(retorno);
            }
            this.cStat = ControleDados.getConteudoTAG(retorno, "cStat");
            System.out.println();
            this.xMotivo = ControleDados.getConteudoTAG(retorno, "xMotivo");
            return true;
        }
        catch (Exception e) {
            this.cStat = "999";
            e.printStackTrace();
            return false;
        }
    }
    
    private void parseComandosLayoutAntigo(String retorno) {
        try {
            this.comandos = new ArrayList<ComandoSefaz>();
            if (retorno.indexOf("<comandos") >= 0 && retorno.indexOf("</comandos>") >= 0) {
                do {
                    final ComandoSefaz cmd = new ComandoSefaz();
                    final String xml = ControleDados.getConteudoTAG(retorno, "comandos");
                    final String nome = ControleDados.getConteudoTAG(xml, "comando");
                    final String idCmd = ControleDados.getAtributoTAG(xml, "comando", "idCmd");
                    cmd.setNome(nome);
                    cmd.setIdCmd(idCmd);
                    this.comandos.add(cmd);
                    retorno = retorno.substring(retorno.indexOf("</comandos>") + "</comandos>".length());
                } while (retorno.indexOf("<comando") >= 0 || retorno.indexOf("</comando>") >= 0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void parseComandos(final String retorno) {
        try {
            final List listaComandos = ControleDados.getListaTags(retorno, "comando");
            for (int i = 0; i < listaComandos.size(); ++i) {
                final String comando = listaComandos.get(i).toString();
                final String nome = ControleDados.getConteudoTAG(comando, "nome");
                if (comando != null && nome != null) {
                    final ComandoSefaz cmd = new ComandoSefaz(nome);
                    cmd.setIdCmd(ControleDados.getAtributoTAG(retorno, "comando", "idCmd"));
                    if (nome != null && nome.equalsIgnoreCase("COMANDO_008")) {
                        cmd.setCod(ControleDados.getConteudoTAG(comando, "cod"));
                        cmd.setxMsg(ControleDados.getConteudoTAG(comando, "xMsg"));
                    }
                    this.addComando(cmd);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List getComando() {
        return this.comandos;
    }
    
    private void addComando(final ComandoSefaz comandoSefaz) {
        if (this.comandos == null) {
            this.comandos = new ArrayList<ComandoSefaz>();
        }
        this.comandos.add(comandoSefaz);
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public void setxServ(final String xServ) {
        this.xServ = xServ;
    }
    
    public void setIdCmd(final String idCmd) {
        this.idCmd = idCmd;
    }
    
    public void setUrlWebService(final String urlWebService) {
        this.urlWebService = urlWebService;
    }
}
