// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.ConfiguracoesOffLine;

public class ControladorEmuladorOffLine
{
    private static boolean atualizando;
    
    static {
        ControladorEmuladorOffLine.atualizando = false;
    }
    
    public void atualizacaoSoftwareBasico(final int n) {
        if (n == 1 || n == 2) {
            this.atualizacao();
        }
        else if (n == 3) {
            this.atualizacaoForcada();
        }
    }
    
    private void atualizacao() {
        ControleLogs.logar("Comando \"COMANDO_008\" recebido da SEFAZ");
        ControleSeguranca.salvarAviso("Existem atualiza\u00e7\u00f5es pendentes para o SAT.");
    }
    
    public void renovacaoAviso() {
        ControleLogs.logar("Comando \"COMANDO_008\" recebido da SEFAZ");
        ControleSeguranca.salvarAviso("999 \u2013 ATEN\u00c7\u00c3O CONTRIBUINTE, O CERTIFICADO ICP-BRASIL DE SEU SAT-CF-e VENCE EM 15/05/2015");
    }
    
    private void atualizacaoForcada() {
        ControleLogs.logar("Comando \"COMANDO_004\" recebido da SEFAZ");
        ControleLogs.logar("INICIO ATUALIZA\u00c7\u00c3O DO SOFTWARE B\u00c1SICO");
        ControladorEmuladorOffLine.atualizando = true;
        try {
            Thread.sleep(90000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        ControleLogs.logar("Software Atualizado com Sucesso");
        ControleLogs.logar("FIM ATUALIZA\u00c7\u00c3O DO SOFTWARE B\u00c1SICO");
        ConfiguracoesOffLine.getInstance().setAtualizarSoftwareBasico(0);
        this.salvarConfiguracoes();
        ControladorEmuladorOffLine.atualizando = false;
    }
    
    public String comandoAtualizar(final String[] parametro) {
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
            ControleLogs.logar("ERRO NA Atualizacao, C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
            return "14001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
        }
        final int n = ConfiguracoesOffLine.getInstance().getAtualizarSoftwareBasico();
        if (n != 0) {
            if (ControladorEmuladorOffLine.atualizando) {
                ControleLogs.logar("Atualiza\u00e7\u00e3o em Andamento.");
                return "14002|Atualiza\u00e7\u00e3o em Andamento.";
            }
            ControleLogs.logar("INICIO ATUALIZA\u00c7\u00c3O DO SOFTWARE B\u00c1SICO");
            if (n == 1) {
                ControleLogs.logar("Software Atualizado com Sucesso");
                ControleLogs.logar("FIM ATUALIZA\u00c7\u00c3O DO SOFTWARE B\u00c1SICO");
                ConfiguracoesOffLine.getInstance().setAtualizarSoftwareBasico(0);
                this.salvarConfiguracoes();
                return "14000|Software Atualizado com Sucesso.";
            }
            if (n == 2) {
                ControleLogs.logar("Falha no Software Atualizado com Sucesso");
                ControleLogs.logar("FIM ATUALIZA\u00c7\u00c3O DO SOFTWARE B\u00c1SICO");
                ConfiguracoesOffLine.getInstance().setAtualizarSoftwareBasico(0);
                this.salvarConfiguracoes();
                return "14003|Erro na atualiza\u00e7\u00e3o.";
            }
        }
        return "14003|Erro na atualiza\u00e7\u00e3o.";
    }
    
    public static boolean isAtualizando() {
        return ControladorEmuladorOffLine.atualizando;
    }
    
    public void salvarConfiguracoes() {
        final ConfiguracoesOffLine c = ConfiguracoesOffLine.getInstance();
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CONFIGURACOES_OFFLINE, c.toString().toCharArray());
    }
}
