// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class DadosContribuinte
{
    public DadosContribuinte(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("cUF")) {
                Parametrizacoes.cUF = "35";
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                Parametrizacoes.CNPJ = ControleDados.removerMascara(filhoAtual.getTextContent().trim());
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xNome")) {
                Parametrizacoes.razaoSocialEmitente = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xFantasia")) {
                Parametrizacoes.nomeFantasiaEmitente = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("IE")) {
                Parametrizacoes.IE = ControleDados.removerMascara(filhoAtual.getTextContent().trim());
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cRegTrib")) {
                Parametrizacoes.codigoRegimeTributario = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("enderEmit")) {
                new EnderecoEmitente(filhoAtual);
            }
        }
    }
}
