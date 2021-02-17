// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class WebServices
{
    public WebServices(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CFeServicoNacional")) {
                Parametrizacoes.CFeServicoNacional = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeAtivacao")) {
                Parametrizacoes.CFeAtivacao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeStatus")) {
                Parametrizacoes.CFeStatus = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeCertificacao")) {
                Parametrizacoes.CFeCertificacao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeParametrizacao")) {
                Parametrizacoes.CFeParametrizacao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeRecepcao")) {
                Parametrizacoes.CFeRecepcao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeRetRecepcao")) {
                Parametrizacoes.CFeRetRecepcao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeCancelamento")) {
                Parametrizacoes.CFeCancelamento = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeComandos")) {
                Parametrizacoes.CFeComandos = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeAtualizacao")) {
                Parametrizacoes.CFeAtualizacao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeTeste")) {
                Parametrizacoes.CFeTeste = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeSignAC")) {
                Parametrizacoes.CFeSignAC = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeLogs")) {
                Parametrizacoes.CFeLogs = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeConsultaGestao")) {
                Parametrizacoes.CFeConsultaGestao = filhoAtual.getTextContent().trim();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFeReset")) {
                Parametrizacoes.CFeReset = filhoAtual.getTextContent().trim();
            }
        }
    }
}
