// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeSignAC;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorAssociacaoAssinatura
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorAssociacaoAssinatura(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String tratarMensagem(final String CNPJValue, final String assinaturaCNPJValue, final String aviso) {
        try {
            if (!Configuracoes.SAT.emuladorOffLine) {
                final WebServiceCFeSignAC wsSignAC = new WebServiceCFeSignAC();
                wsSignAC.setCNPJValue(CNPJValue);
                wsSignAC.setSignAC(assinaturaCNPJValue);
                if (!wsSignAC.consumirWebService()) {
                    throw new ErroComunicacaoRetaguardaException();
                }
                if (wsSignAC.getcStat() != null && wsSignAC.getcStat().equalsIgnoreCase("111")) {
                    if (wsSignAC.getxMotivo() != null) {
                        ControleLogs.logar(wsSignAC.getxMotivo());
                    }
                    return "1111|Rejei\u00e7\u00e3o: Dados informados no processo de assinatura n\u00e3o s\u00e3o v\u00e1lidos conforme controles da retaguarda";
                }
                if (wsSignAC.getcStat() != null && wsSignAC.getcStat().equalsIgnoreCase("540")) {
                    if (wsSignAC.getxMotivo() != null) {
                        ControleLogs.logar(wsSignAC.getxMotivo());
                    }
                    return "1540|Rejei\u00e7\u00e3o: CNPJ da Software House + CNPJ do emitente assinado no campo \u201csignAC\u201d difere do informado no campo \u201cCNPJvalue\u201d";
                }
                if (!wsSignAC.getcStat().equalsIgnoreCase("112")) {
                    if (wsSignAC.getxMotivo() != null) {
                        ControleLogs.logar(wsSignAC.getxMotivo());
                    }
                    throw new ErroDesconhecidoException();
                }
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA, assinaturaCNPJValue.toCharArray());
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CNPJ_VALUE, CNPJValue.toCharArray());
            Configuracoes.SAT.signAC = assinaturaCNPJValue;
            Configuracoes.SAT.cnpjSoftwareHouse = CNPJValue.substring(0, 14);
            Configuracoes.SAT.associado = true;
            ControleLogs.logar("ASSOCIADO COM SUCESSO");
            return "13000|Assinatura do AC Registrada||" + aviso;
        }
        catch (ErroComunicacaoRetaguardaException e) {
            e.printStackTrace();
            return "13001|Erro de comunicacao com a SEFAZ||" + aviso;
        }
        catch (ErroDesconhecidoException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return "13099|Erro de desconhecido||" + aviso;
    }
    
    public String validarParametros(final String codigoAtivacao, final String numeroSessao, String CNPJValue, String assinaturaCNPJValue, String aviso) {
        final String r = "true";
        try {
            CNPJValue = CNPJValue.trim();
            aviso = aviso.trim();
            assinaturaCNPJValue = assinaturaCNPJValue.trim();
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "13099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO NA ASSOCIACAO, C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "13001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
            if (CNPJValue.length() != 28) {
                ControleLogs.logar("ERRO NA ASSOCIACAO, Campos CNPJValue Invalido.");
                return "13099|Erro desconhecido";
            }
            if (!Configuracoes.SAT.emuladorOffLine && !ControleDados.validarCNPJCPF(CNPJValue.substring(0, 14))) {
                ControleLogs.logar("ERRO NA ASSOCIACAO, CNPJ da Software House inv\u00e1lido.");
                return "13099|Erro desconhecido";
            }
            if (assinaturaCNPJValue.length() == 0) {
                ControleLogs.logar("ERRO NA ASSOCIACAO, assinatura com tamanho Invalido.");
                return "13003|Assinatura fora do padr\u00e3o informado";
            }
            if (assinaturaCNPJValue.length() > 344) {
                ControleLogs.logar("ERRO NA ASSOCIACAO, assinatura com tamanho diferente de 344 caracteres.");
                return "13003|Assinatura fora do padr\u00e3o informado";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "13099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        return r;
    }
}
