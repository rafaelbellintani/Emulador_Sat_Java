// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import br.com.um.controles.ControleDados;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayDeque;
import java.util.Deque;

public class ListaNumeroSessao
{
    private Deque<Integer> listaNumeroSessao;
    private int ultimaSessao;
    
    public ListaNumeroSessao() {
        this.listaNumeroSessao = null;
        this.ultimaSessao = 0;
        this.listaNumeroSessao = new ArrayDeque<Integer>();
    }
    
    public ListaNumeroSessao(String xml) {
        this.listaNumeroSessao = null;
        this.ultimaSessao = 0;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            final DocumentBuilder db = dbf.newDocumentBuilder();
            xml = ControleDados.removerCabecalhoXML(xml);
            final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
            final Document doc = db.parse(is);
            final NodeList raizes = doc.getChildNodes();
            final Node raiz = raizes.item(0);
            this.parse(raiz);
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
            this.listaNumeroSessao = new ArrayDeque<Integer>();
        }
    }
    
    public ListaNumeroSessao(final Node raiz) {
        this.listaNumeroSessao = null;
        this.ultimaSessao = 0;
        try {
            this.parse(raiz);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.listaNumeroSessao = new ArrayDeque<Integer>();
        }
    }
    
    private void parse(final Node raiz) {
        this.listaNumeroSessao = new ArrayDeque<Integer>();
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("sessao")) {
                final Integer e = Integer.parseInt(filhoAtual.getTextContent());
                this.listaNumeroSessao.add(e);
            }
        }
    }
    
    @Override
    public String toString() {
        final Object[] lista = this.listaNumeroSessao.toArray();
        final StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<numerosSessao>");
        for (int i = 0; i < lista.length; ++i) {
            sb.append("<sessao>").append(lista[i]).append("</sessao>");
        }
        sb.append("</numerosSessao>");
        return sb.toString();
    }
    
    public boolean addNumeroSessao(final int numeroSessao) {
        if (numeroSessao < 1 || numeroSessao > 999999) {
            return false;
        }
        if (this.listaNumeroSessao.size() >= 100) {
            this.listaNumeroSessao.removeFirst();
        }
        this.listaNumeroSessao.add(numeroSessao);
        this.ultimaSessao = numeroSessao;
        return true;
    }
    
    public boolean existeNumeroSessao(final int numeroSessao) {
        return this.listaNumeroSessao.contains(numeroSessao);
    }
    
    public int getUltimaSessao() {
        return this.ultimaSessao;
    }
    
    public void setUltimaSessao(final int ultimaSessao) {
        this.ultimaSessao = ultimaSessao;
    }
}
