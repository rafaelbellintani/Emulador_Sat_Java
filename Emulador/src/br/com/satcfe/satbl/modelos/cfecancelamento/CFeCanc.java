// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.MarshalException;
import java.util.Iterator;
import javax.xml.crypto.dsig.XMLValidateContext;
import java.security.Key;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.security.PublicKey;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.XMLStructure;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.Configuracoes;
import javax.xml.crypto.dsig.Reference;
import java.util.Collections;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.Transform;
import java.util.List;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import org.w3c.dom.Element;
import java.util.ArrayList;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import org.w3c.dom.Node;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;

public class CFeCanc
{
    private InformacoesCFe infCFe;
    public static final int TESTE = 2;
    public static final int REAL = 1;
    
    public CFeCanc(final String xml) throws ParserConfigurationException, SAXException, IOException {
        this.infCFe = null;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document doc = db.parse(is);
        final NodeList raizes = doc.getChildNodes();
        this.parse(raizes.item(0));
    }
    
    public CFeCanc(final Node raiz) {
        this.infCFe = null;
        this.parse(raiz);
    }
    
    private void parse(final Node raiz) {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("infCFe")) {
                this.infCFe = new InformacoesCFe(filhoAtual);
            }
        }
    }
    
    public void setInfCFe(final InformacoesCFe inf) {
        this.infCFe = inf;
    }
    
    public InformacoesCFe getInfCFe() {
        return this.infCFe;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.infCFe == null) {
            resultado = "1999";
        }
        else {
            resultado = this.infCFe.validar();
        }
        return "0" + resultado;
    }
    
    public void completar(final String chave) {
        this.infCFe.completar(chave);
    }
    
    public Document assinar(final String xml, final String chave) throws Exception {
        final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final ArrayList transformList = new ArrayList();
        final TransformParameterSpec tps = null;
        final Transform envelopedTransform = fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", tps);
        final Transform c14NTransform = fac.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);
        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
        final NodeList elements = doc.getElementsByTagName("infCFe");
        final Element el = (Element)elements.item(0);
        final String id = el.getAttribute("Id");
        final Reference ref = fac.newReference("#" + id, fac.newDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", null), transformList, null, null);
        final SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null), Collections.singletonList(ref));
        final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
        cKeyStore.carregarKeyStore();
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final X509Data xData = kif.newX509Data(Collections.singletonList(cKeyStore.getCertificado()));
        final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xData));
        dbf.setNamespaceAware(true);
        final DOMSignContext dsc = new DOMSignContext(cKeyStore.getPrivateKey(), doc.getElementsByTagName("infCFe").item(0));
        final Element rootEl = (Element)doc.getElementsByTagName("infCFe").item(0);
        rootEl.setIdAttribute("Id", true);
        dsc.setParent(doc.getElementsByTagName("CFeCanc").item(0));
        final XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);
        this.validate(doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0), cKeyStore.getCertificado().getPublicKey());
        return ControleAssinaturaXML.serializarXMLSignature(doc);
    }
    
    public boolean validate(final Node sig, final PublicKey publica) throws MarshalException, XMLSignatureException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final DOMValidateContext validationContext = new DOMValidateContext(publica, sig);
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final XMLSignature signature = fac.unmarshalXMLSignature(validationContext);
        final boolean coreValidity = signature.validate(validationContext);
        if (!coreValidity) {
            System.err.println("Signature failed core validation");
            final boolean sv = signature.getSignatureValue().validate(validationContext);
            if (!sv) {
                final Iterator i = signature.getSignedInfo().getReferences().iterator();
                int j = 0;
                while (i.hasNext()) {
               /*FEITO CAST*/     ((XMLSignature) i.next()).validate(validationContext);
                    ++j;
                }
            }
        }
        return coreValidity;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<CFeCanc>");
        this.infCFe.toString(sb);
        sb.append("</CFeCanc>");
        return sb.toString();
    }
}
