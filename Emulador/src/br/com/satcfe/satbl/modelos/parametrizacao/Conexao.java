// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class Conexao
{
    public Conexao(final Node raiz) throws Exception {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("cert_sefaz")) {
                new CertSefaz(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("servidores")) {
                new Servidores(filhoAtual);
            }
        }
    }
}
