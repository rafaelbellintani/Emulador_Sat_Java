// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class Transmissao
{
    public Transmissao(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("tipo")) {
                Parametrizacoes.tipoTransmissao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("valor")) {
                final String t = filhoAtual.getTextContent().trim();
                if (Parametrizacoes.tipoTransmissao != null && Parametrizacoes.tipoTransmissao.equalsIgnoreCase("tempo")) {
                    Parametrizacoes.valorTransmissao = new Valor(t).getValor();
                }
                else {
                    Parametrizacoes.valorTransmissao = Integer.parseInt(t);
                }
            }
        }
    }
}
