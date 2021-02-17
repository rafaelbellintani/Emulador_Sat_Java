// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class Comandos
{
    public Comandos(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("tipo")) {
                Parametrizacoes.tipoComandos = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("valor")) {
                final String t = filhoAtual.getTextContent().trim();
                if (Parametrizacoes.tipoComandos != null && Parametrizacoes.tipoComandos.equalsIgnoreCase("tempo")) {
                    Parametrizacoes.valorComandos = new Valor(t).getValor();
                }
                else {
                    Parametrizacoes.valorComandos = Integer.parseInt(t);
                }
            }
        }
    }
}
