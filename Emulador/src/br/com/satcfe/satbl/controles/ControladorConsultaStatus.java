// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.modelos.StatusOperacionalSAT;

public class ControladorConsultaStatus
{
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorConsultaStatus(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String tratarMensagem(final String codigoAtivacao, final String aviso) {
        try {
            final StatusOperacionalSAT status = new StatusOperacionalSAT();
            final String dados = status.getStatusPipe();
            ControleLogs.logar("FIM CONSULTAR STATUS OPERACIONAL");
            return "10000|Resposta com Sucesso.||" + aviso + "|" + dados;
        }
        catch (Exception e) {
            e.printStackTrace();
            ControleLogs.logar("ERRO AO CONSULTAR STATUS OPERACIONAL");
            return "10099|Erro desconhecido.||" + aviso;
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: N\u00famero de Sess\u00e3o Inv\u00e1lido.");
                return "10099|Erro desconhecido";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "10001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ControleLogs.logar("ERRO Desconhecido ao Consultar Status do SAT.");
            return "10099|Erro desconhecido";
        }
        return r;
    }
}
