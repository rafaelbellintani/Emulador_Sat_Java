// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class AutoBloqueio
{
    public AutoBloqueio(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("tipoBloqueio")) {
                Parametrizacoes.tipoBloqueio = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("valorBloqueio")) {
                String s = filhoAtual.getTextContent().trim();
                if (s.length() < 6) {
                    s = "00000" + s;
                }
                final int h = Integer.parseInt(s.substring(0, 4));
                final int m = Integer.parseInt(s.substring(4, 6));
                Parametrizacoes.valorBloqueio = h * 60 + m;
            }
        }
    }
}
