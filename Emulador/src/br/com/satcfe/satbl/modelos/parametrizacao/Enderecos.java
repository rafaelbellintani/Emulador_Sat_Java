// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import java.util.ArrayList;
import org.w3c.dom.Node;

public class Enderecos
{
    public Enderecos(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        final ArrayList<String> enderecos = new ArrayList<String>();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("endereco")) {
                if (filhoAtual.getTextContent().trim().equals("spointerdes01")) {
                    filhoAtual.setTextContent("webhomolog.fazenda.sp.gov.br");
                }
                enderecos.add(filhoAtual.getTextContent().trim());
            }
        }
        final String[] aux = new String[enderecos.size()];
        for (int j = 0; j < aux.length; ++j) {
            aux[j] = enderecos.get(j);
        }
        Parametrizacoes.endereco = aux;
    }
}
