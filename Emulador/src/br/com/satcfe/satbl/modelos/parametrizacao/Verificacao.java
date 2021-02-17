// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class Verificacao
{
    public Verificacao(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("tipo")) {
                Parametrizacoes.tipoVerificacao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("valor")) {
                final String t = filhoAtual.getTextContent().trim();
                if (Parametrizacoes.tipoVerificacao != null && Parametrizacoes.tipoVerificacao.equalsIgnoreCase("tempo")) {
                    Parametrizacoes.valorVerificacao = new Valor(t).getValor();
                }
                else {
                    Parametrizacoes.valorVerificacao = Integer.parseInt(t);
                }
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("verProcesso")) {
                final int min = Integer.parseInt(filhoAtual.getTextContent().trim());
                final int milisegundos = Parametrizacoes.verProcesso = 60 * min * 1000;
            }
        }
    }
}
