// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import org.w3c.dom.NodeList;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.BaseCertificados;
import org.w3c.dom.Node;

public class CertSefaz
{
    public CertSefaz(final Node raiz) throws Exception {
        final BaseCertificados base = new BaseCertificados();
        base.setCaminhoCertSefaz(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT_SEFAZ);
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("cert")) {
                String cert = filhoAtual.getTextContent().trim();
                if (cert.indexOf("BEGIN CERTIFICATE") >= 0) {
                    cert = ControleDados.formatarCertificado(cert, true);
                    base.addCertSefaz(cert);
                }
            }
        }
        try {
            base.gravarCertSefaz();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
