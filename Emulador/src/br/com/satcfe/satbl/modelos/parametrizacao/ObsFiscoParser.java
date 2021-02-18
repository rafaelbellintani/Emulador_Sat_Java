// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import java.util.ArrayList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.modelos.cfe.ObsFisco;
import org.w3c.dom.Node;

public class ObsFiscoParser
{
    public ObsFiscoParser(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        final ObsFisco obs = new ObsFisco();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("xCampo")) {
                obs.setxCampo(filhoAtual.getTextContent().trim());
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xTexto")) {
                obs.setxTexto(filhoAtual.getTextContent().trim());
            }
        }
        addObsFisco(obs);
    }
    
    public static void addObsFisco(final ObsFisco obs) {
        if (Parametrizacoes.obsFisco == null) {
            Parametrizacoes.obsFisco = new ArrayList<ObsFisco>();
        }
        Parametrizacoes.obsFisco.add(obs);
    }
}
