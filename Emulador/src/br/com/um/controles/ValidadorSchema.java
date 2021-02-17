// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Node;
import javax.xml.transform.dom.DOMSource;

public class ValidadorSchema
{
    private String xml;
    private String xsd;
    private String erro;
    
    public ValidadorSchema() {
        this.xml = null;
        this.xsd = null;
        this.erro = null;
    }
    
    public void setSchema(final String schema) {
        this.xsd = schema;
    }
    
    public void setXml(final String xml) {
        this.xml = xml;
    }
    
    public String getErro() {
        return this.erro;
    }
    
    public boolean validar() {
        try {
            this.xml = this.xml.replace(" xmlns=\"http://www.fazenda.sp.gov.br/sat\"", "");
            this.xsd = this.xsd.replace("\"^", "\"").replace("$\"", "\"").replace("$|^", "|");
            final Source sourceXsd = new DOMSource(XmlUtil.parseXML(this.xsd));
            final Document doc = XmlUtil.parseXML(this.xml);
            final Source source = new DOMSource(doc);
            final SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            final Schema schema = sf.newSchema(sourceXsd);
            final Validator validator = schema.newValidator();
            validator.validate(source);
            return true;
        }
        catch (SAXException e) {
            this.erro = e.getMessage();
            e.printStackTrace();
            return false;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.erro = "Erro Desconhecido";
            return false;
        }
    }
}
