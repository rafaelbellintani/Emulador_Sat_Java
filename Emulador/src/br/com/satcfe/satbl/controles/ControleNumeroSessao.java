// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.ListaNumeroSessao;

public class ControleNumeroSessao
{
    private ListaNumeroSessao listaNumeroSessao;
    
    public ControleNumeroSessao() {
        this.listaNumeroSessao = null;
        this.carregarNumerosSessao();
    }
    
    private void carregarNumerosSessao() {
        try {
            final String caminho = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + "listaNumeroSessao.txt";
            String xml = null;
            if (ControleArquivos.existeArquivo(caminho)) {
                xml = ControleArquivos.lerCaracteresArquivo(caminho);
                this.listaNumeroSessao = new ListaNumeroSessao(xml);
            }
            else {
                this.listaNumeroSessao = new ListaNumeroSessao();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void teste() {
    	Runtime.getRuntime()
    }
    
    public boolean addNumeroSessao(final int numeroSessao) {
        if (this.listaNumeroSessao == null) {
            return false;
        }
        final boolean b = this.listaNumeroSessao.addNumeroSessao(numeroSessao);
        this.salvarListaNumeroSessao();
        return b;
    }
    
    public boolean validarNumeroSessao(final int numeroSessao) {
        return numeroSessao >= 1 && numeroSessao <= 999999 && this.listaNumeroSessao != null && !this.listaNumeroSessao.existeNumeroSessao(numeroSessao);
    }
    
    public void salvarListaNumeroSessao() {
        try {
            final String caminho = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + "listaNumeroSessao.txt";
            ControleArquivos.excluirArquivo(caminho);
            ControleArquivos.escreverCaracteresArquivo(caminho, this.listaNumeroSessao.toString().toCharArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void armazenarRetorno(final String sessao, final String retorno) {
        final String[] remanecentes = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO);
        for (int i = 0; i < remanecentes.length; ++i) {
            ControleArquivos.excluirArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + remanecentes[i]);
        }
        ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + sessao + ".txt", retorno.toCharArray());
    }
}
