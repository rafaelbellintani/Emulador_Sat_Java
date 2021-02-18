// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.controles.webservices.WebServiceCFeReset;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorDesativacao
{
    public String tratarComandoDesativarSAT(final String aviso) {
        String r = "true";
        if (!Configuracoes.SAT.ativado) {
            r = "19099|SAT-CFe n\u00e3o est\u00e1 Ativo.";
        }
        else if (!Configuracoes.SAT.bloqueado) {
            r = "19099|SAT-CFe n\u00e3o est\u00e1 bloqueado.";
        }
        else if (!Parametrizacoes.cessacao) {
            r = "19099|SAT-CFe n\u00e3o pode ser Reativado.";
        }
        if (r.equalsIgnoreCase("true")) {
            r = this.comunicarReset();
        }
        if (r.equalsIgnoreCase("true")) {
            r = String.valueOf(this.desativarSAT()) + "||" + aviso;
            ControleLogs.logar("FIM RESET DO EQUIPAMENTO SAT");
        }
        else {
            ControleLogs.logar(r.split("\\|")[1]);
            r = String.valueOf(r) + "||" + aviso;
        }
        return r;
    }
    
    public String botaoDesativarSAT() {
        ControleLogs.logar("INICIO RESET DO EQUIPAMENTO SAT");
        final String s = this.tratarComandoDesativarSAT("");
        return s.split("\\|")[1];
    }
    
    private String desativarSAT() {
        ControleArquivos.removerTodosArquivos(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_PARAMETRIZACAO);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS);
        ControleArquivos.removerTodosArquivos(Configuracoes.SistemaArquivos.DIRETORIO_KEYSTORE);
        ControleArquivos.removerTodosArquivos(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
        ControleLogs.logar("SAT-CFe foi Desativado com Sucesso.");
        Configuracoes.SAT.bloqueado = false;
        Configuracoes.SAT.associado = false;
        Configuracoes.SAT.ativado = false;
        return "19000|SAT-CFe foi Desativado com Sucesso.";
    }
    
    private String comunicarReset() {
        final WebServiceCFeReset wsReset = new WebServiceCFeReset();
        if (!wsReset.consumirWebService()) {
            return "19099|Erro na comunica\u00e7\u00e3o de Reset.";
        }
        if (wsReset.getcStat() == null) {
            return "19099|Erro na comunica\u00e7\u00e3o de Reset.";
        }
        if (wsReset.getcStat().equals("132")) {
            return "19099|Rejei\u00e7\u00e3o: Equipamento n\u00e3o pode ser desativado.";
        }
        if (wsReset.getcStat().equals("131")) {
            return "true";
        }
        return "19099|Erro desconhecido na comunica\u00e7\u00e3o de Reset.";
    }
}
