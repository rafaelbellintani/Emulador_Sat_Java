// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import org.w3c.dom.NamedNodeMap;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.Node;

public class ParametrizacaoUf
{
    private Node raiz;
    
    public ParametrizacaoUf() {
        this.raiz = null;
    }
    
    public ParametrizacaoUf(final String xml) {
        this.raiz = null;
        Parametrizacoes.autorBloqueio = 0;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            final Document doc = db.parse(is);
            final NodeList raizes = doc.getChildNodes();
            this.raiz = raizes.item(0);
        }
        catch (Exception e) {
            this.raiz = null;
            e.printStackTrace();
        }
    }
    
    public boolean parse() {
        return this.raiz != null;/* && this.parse(this.raiz);*/ // Entra em LOOP se fica ativo
    }
    
    public boolean parse(final Node raiz) {
        try {
            final NodeList filhos = raiz.getChildNodes();
            for (int i = 0; i < filhos.getLength(); ++i) {
                final Node filhoAtual = filhos.item(i);
                if (filhoAtual.getNodeName().equalsIgnoreCase("mensagem")) {
                    Parametrizacoes.mensagemAmbiente = filhoAtual.getTextContent().trim();
                }
                else if (filhoAtual.getNodeName().equalsIgnoreCase("conexao")) {
                    new Conexao(filhoAtual);
                }
                else if (filhoAtual.getNodeName().equalsIgnoreCase("cert_https")) {
                    new CertHttps(filhoAtual);
                }
                else if (filhoAtual.getNodeName().equalsIgnoreCase("ntp")) {
                    new Ntp(filhoAtual);
                }
            }
            final NamedNodeMap node = raiz.getAttributes();
            Parametrizacoes.ambiente = node.getNamedItem("ambiente").getTextContent();
            ControleSeguranca.carregarCadeiaCertificados();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean existe() {
        return ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_UF);
    }
}
