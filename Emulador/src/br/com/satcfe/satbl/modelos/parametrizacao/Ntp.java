// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class Ntp
{
    public Ntp(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("endereco")) {
                Parametrizacoes.enderecoNtp = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("porta")) {
                Parametrizacoes.portaNTP = Integer.parseInt(filhoAtual.getTextContent().trim());
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("horario_verao")) {
                new HorarioVerao(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("Fuso_horario")) {
                Parametrizacoes.fusoHorario = filhoAtual.getTextContent().trim();
            }
        }
    }
}
