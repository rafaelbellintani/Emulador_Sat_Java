// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class Servidores
{
    public Servidores(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("enderecos")) {
                new Enderecos(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("porta")) {
                Parametrizacoes.porta = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("webservices")) {
                new WebServices(filhoAtual);
            }
        }
    }
}
