// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NodeList;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import java.io.StringWriter;
import org.w3c.dom.Document;

public class XmlUtil
{
    private XmlUtil() {
    }
    
    public static synchronized StringBuffer addCData(final StringBuffer xml) {
        return new StringBuffer("<![CDATA[").append(xml).append("]]>");
    }
    
    public static synchronized String addCData(final String xml) {
        return new String("<![CDATA[" + xml + "]]>");
    }
    
    public static synchronized String removerCData(final String xml) {
        return xml.replace("<![CDATA[", "").replace("]]>", "");
    }
    
    public static synchronized String removerCabecalhoXML(final String dados) {
        final String retorno = dados.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "").replace("<?xml version=\"1.0\"?>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
        return retorno;
    }
    
    public static synchronized String outputXML(final Document doc) {
        try {
            final StringWriter sw = new StringWriter();
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "no");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static synchronized Document parseXML(final String xml) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setNamespaceAware(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
        final Document doc = db.parse(is);
        return doc;
    }
    
    public static synchronized String recuperarTag(final String xml, final String tag) {
        String r = null;
        int n = -1;
        if (xml != null && ((n = xml.indexOf("<" + tag + ">")) >= 0 || (n = xml.indexOf("<" + tag + " ")) >= 0)) {
            r = xml.substring(n);
            if (xml.indexOf("<" + tag + "/>") >= 0 || xml.indexOf("<" + tag + " />") >= 0) {
                return null;
            }
            r = r.substring(0, r.indexOf("</" + tag + ">") + ("</" + tag + ">").length());
        }
        return r;
    }
    
    public static synchronized String getConteudoTAG(final String xml, final String tag) {
        String r = null;
        int n = -1;
        if (xml != null && ((n = xml.indexOf("<" + tag + ">")) >= 0 || (n = xml.indexOf("<" + tag + " ")) >= 0)) {
            r = xml.substring(n + ("<" + tag).length());
            if (xml.indexOf("<" + tag + "/>") >= 0 || xml.indexOf("<" + tag + " />") >= 0) {
                return null;
            }
            r = r.substring(r.indexOf(">") + 1, r.indexOf("</" + tag + ">"));
        }
        return r;
    }
    
    public static synchronized String getConteudoTAG(final Document docXml, final String tag) {
        String r = null;
        if (docXml != null && tag != null) {
            final NodeList n = docXml.getElementsByTagName(tag);
            if (n != null && n.item(0) != null && n.item(0).getTextContent() != null) {
                r = n.item(0).getTextContent().trim();
            }
        }
        if (r == null) {
            return r;
        }
        return (r.length() == 0) ? null : r;
    }
    
    public static synchronized String getAtributoTAG(final String xml, final String tag, final String atributo) {
        String r = null;
        try {
            if (xml != null && xml.indexOf("<" + tag) >= 0) {
                r = xml.substring(xml.indexOf("<" + tag), xml.indexOf("</" + tag));
                r = r.substring(r.indexOf(String.valueOf(atributo) + "=\"") + (String.valueOf(atributo) + "=\"").length());
                r = r.substring(0, r.indexOf("\""));
            }
        }
        catch (Exception e) {
            r = null;
        }
        return r;
    }
    
    public static List getListaTags(String xml, final String tag) {
        if (xml != null && tag != null && xml.indexOf(tag) >= 0) {
            final List lista = new ArrayList();
            do {
                final String t = recuperarTag(xml, tag);
                if (t != null) {
                    lista.add(t);
                }
                xml = xml.substring(xml.indexOf("</" + tag + ">") + ("</" + tag + ">").length());
            } while (xml.indexOf("<" + tag) >= 0);
            return lista;
        }
        return null;
    }
    
    @Deprecated
    public static boolean validarSchema(String xml, String xsd) {
        try {
            xml = xml.replace(" xmlns=\"http://www.fazenda.sp.gov.br/sat\"", "");
            xsd = xsd.replace("\"^", "\"").replace("$\"", "\"").replace("$|^.{", "|.{");
            final Source sourceXsd = new DOMSource(parseXML(xsd));
            final Document doc = parseXML(xml);
            final Source source = new DOMSource(doc);
            final SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            final Schema schema = sf.newSchema(sourceXsd);
            final Validator validator = schema.newValidator();
            validator.validate(source);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
