// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class EnderecoEmitente
{
    public EnderecoEmitente(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("xLgr")) {
                Parametrizacoes.xLgr = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("nro")) {
                Parametrizacoes.nro = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xCpl")) {
                Parametrizacoes.xCpl = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xBairro")) {
                Parametrizacoes.xBairro = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xMun")) {
                Parametrizacoes.xMun = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CEP")) {
                Parametrizacoes.CEP = filhoAtual.getTextContent().trim();
            }
        }
    }
}
