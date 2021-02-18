// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import org.w3c.dom.NamedNodeMap;
import br.com.satcfe.satbl.Parametrizacoes;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;

public class ParametrosGestao
{
    private Node raiz;
    
    public ParametrosGestao() {
        this.raiz = null;
    }
    
    public ParametrosGestao(final String xml) {
        this.raiz = null;
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
        if (this.raiz == null) {
            return false;
        }
        if (this.raiz.getNodeName().equalsIgnoreCase("vigenciaLeiaute")) {
            return this.parseVigenciaLeiaute(this.raiz);
        }
        return this.raiz.getNodeName().equalsIgnoreCase("codigosANP") && this.parseCodigoAnp(this.raiz);
    }
    
    public boolean parseCodigoAnp(final Node raiz) {
        try {
            final TabelaCodigosAnp tabela = new TabelaCodigosAnp();
            final NodeList filhos = raiz.getChildNodes();
            for (int i = 0; i < filhos.getLength(); ++i) {
                final Node filhoAtual = filhos.item(i);
                if (filhoAtual.getNodeName().equalsIgnoreCase("codigo")) {
                    final CodigoAnp codigoAnp = new CodigoAnp();
                    codigoAnp.setcProdANP(filhoAtual.getAttributes().getNamedItem("cProdANP").getTextContent());
                    final NodeList filhos2 = filhoAtual.getChildNodes();
                    for (int j = 0; j < filhos2.getLength(); ++j) {
                        final Node filhoAtual2 = filhos2.item(j);
                        if (filhoAtual2.getNodeName().equalsIgnoreCase("descricao")) {
                            codigoAnp.setDescricao(filhoAtual2.getTextContent().trim());
                        }
                        else if (filhoAtual2.getNodeName().equalsIgnoreCase("dataInicio")) {
                            codigoAnp.setDataInicio(filhoAtual2.getTextContent().trim());
                        }
                        else if (filhoAtual2.getNodeName().equalsIgnoreCase("dataFim")) {
                            codigoAnp.setDataFim(filhoAtual2.getTextContent().trim());
                        }
                    }
                    tabela.addCodigoAnp(codigoAnp);
                }
            }
            final NamedNodeMap node = raiz.getAttributes();
            tabela.setAmbiente(node.getNamedItem("ambiente").getTextContent());
            tabela.setAtivarTabela(node.getNamedItem("ativarTabela").getTextContent());
            Parametrizacoes.tabelaCodigosAnp = tabela;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean parseVigenciaLeiaute(final Node raiz) {
        try {
            final TabelaVigenciaLeiaute tabela = new TabelaVigenciaLeiaute();
            final NodeList filhos = raiz.getChildNodes();
            for (int i = 0; i < filhos.getLength(); ++i) {
                final Node filhoAtual = filhos.item(i);
                if (filhoAtual.getNodeName().equalsIgnoreCase("mensagem")) {
                    tabela.setMensagem(filhoAtual.getTextContent().trim());
                }
                else if (filhoAtual.getNodeName().equalsIgnoreCase("leiaute")) {
                    final VigenciaLeiaute leiaute = new VigenciaLeiaute();
                    leiaute.setVersao(filhoAtual.getAttributes().getNamedItem("versao").getTextContent());
                    final NodeList filhos2 = filhoAtual.getChildNodes();
                    for (int j = 0; j < filhos2.getLength(); ++j) {
                        final Node filhoAtual2 = filhos2.item(j);
                        if (filhoAtual2.getNodeName().equalsIgnoreCase("dataInicio")) {
                            leiaute.setDataInicio(filhoAtual2.getTextContent().trim());
                        }
                        else if (filhoAtual2.getNodeName().equalsIgnoreCase("dataFim")) {
                            leiaute.setDataFim(filhoAtual2.getTextContent().trim());
                        }
                    }
                    tabela.addLeiaute(leiaute);
                }
            }
            final NamedNodeMap node = raiz.getAttributes();
            tabela.setAmbiente(node.getNamedItem("ambiente").getTextContent());
            Parametrizacoes.tabelaVigenciaLeiaute = tabela;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean existeArquivoVigencia() {
        return ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_VIGENCIA_LEIAUTE);
    }
    
    public static boolean existeArquivoCodigoAnp() {
        return ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TABELA_ANP);
    }
}
