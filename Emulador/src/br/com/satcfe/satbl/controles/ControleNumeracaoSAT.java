// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.um.controles.ControleDados;
import br.com.um.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;

public class ControleNumeracaoSAT
{
    public String gerarNumDocFiscal() {
        String numDocFiscal = new String();
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL)) {
            String codigoAtualString = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL);
            int codigoAtualInteiro = 0;
            try {
                codigoAtualInteiro = Integer.parseInt(codigoAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
                codigoAtualInteiro = 0;
            }
            codigoAtualString = String.valueOf(++codigoAtualInteiro);
            numDocFiscal = ControleDados.preencheEsqueda(codigoAtualString, '0', 6);
            return numDocFiscal;
        }
        return "000001";
    }
    
    public String gerarNumDocFiscalTeste() {
        String numDocFiscal = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE)) {
            String codigoAtualString = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE);
            int codigoAtualInteiro = 0;
            try {
                codigoAtualInteiro = Integer.parseInt(codigoAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
            }
            codigoAtualString = String.valueOf(++codigoAtualInteiro);
            numDocFiscal = ControleDados.preencheEsqueda(codigoAtualString, '0', 6);
            return numDocFiscal;
        }
        return "000001";
    }
    
    public void gravarNumeroCFe(final String numero) {
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL, numero.toCharArray());
    }
    
    public void gravarNumeroCFeTeste(final String numero) {
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE, numero.toCharArray());
    }
}
