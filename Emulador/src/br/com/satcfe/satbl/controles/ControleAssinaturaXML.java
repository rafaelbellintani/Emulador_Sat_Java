// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.security.cert.CertificateException;
import br.com.um.modelos.Certificado;
import br.com.um.controles.ControleCadeiaCertificado;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.MarshalException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Key;
import java.security.cert.Certificate;
import java.security.PrivateKey;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.SignedInfo;
import org.w3c.dom.NodeList;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.XMLStructure;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.Configuracoes;
import javax.xml.crypto.dsig.Reference;
import java.util.Collections;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import java.util.List;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.crypto.dsig.Transform;
import java.util.ArrayList;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import br.com.um.controles.ControleDados;
import org.w3c.dom.Document;

public class ControleAssinaturaXML
{
    private Document doc;
    private boolean serializarCert;
    final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    final String RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
    
    public ControleAssinaturaXML(final String xml) throws ParserConfigurationException, SAXException, IOException {
        this.doc = null;
        this.serializarCert = true;
        this.doc = ControleDados.parseXML(xml);
    }
    
    public ControleAssinaturaXML(final Document doc) {
        this.doc = null;
        this.serializarCert = true;
        this.doc = doc;
    }
    
    public Document assinar(final String codigoAtivacao) throws Exception {
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final List<Transform> transformList = new ArrayList<Transform>();
        final TransformParameterSpec tps = null;
        final Transform envelopedTransform = fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", tps);
        final Transform c14NTransform = fac.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);
        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final NodeList elements = this.doc.getElementsByTagName("infCFe");
        final Element el = (Element)elements.item(0);
        final String id = el.getAttribute("Id");
        final Reference ref = fac.newReference("#" + id, fac.newDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", null), transformList, null, null);
        final SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null), Collections.singletonList(ref));
        final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
        cKeyStore.carregarKeyStore();
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final X509Data xData = kif.newX509Data(Collections.singletonList(cKeyStore.getCertificado()));
        final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xData));
        dbf.setNamespaceAware(false);
        final DOMSignContext dsc = new DOMSignContext(cKeyStore.getPrivateKey(), this.doc.getElementsByTagName("infCFe").item(0));
        final Element rootEl = (Element)this.doc.getElementsByTagName("infCFe").item(0);
        rootEl.setIdAttribute("Id", true);
        dsc.setParent(this.doc.getElementsByTagName("CFe").item(0));
        final XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);
        final boolean b = validarAssinatura(ControleDados.outputXML(this.doc), cKeyStore.getCertificado().getPublicKey(), "infCFe");
        return serializarXMLSignature(this.doc);
    }
    
    public static Document serializarXMLSignature(final Document doc) {
        try {
            final String c = doc.getElementsByTagName("X509Certificate").item(0).getTextContent();
            doc.getElementsByTagName("X509Certificate").item(0).setTextContent(c.replace("\r", "").replace("\n", ""));
            final String sv = doc.getElementsByTagName("SignatureValue").item(0).getTextContent();
            doc.getElementsByTagName("SignatureValue").item(0).setTextContent(sv.replace("\r", "").replace("\n", ""));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    
    public Document assinar(final PrivateKey privateKey, final Certificate cert) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, DataLengthException, IllegalStateException, InvalidCipherTextException, MarshalException, XMLSignatureException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final List<Transform> transformList = new ArrayList<Transform>();
        final TransformParameterSpec tps = null;
        final Transform envelopedTransform = fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", tps);
        final Transform c14NTransform = fac.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);
        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final NodeList elements = this.doc.getElementsByTagName("infCFe");
        final Element el = (Element)elements.item(0);
        final String id = el.getAttribute("Id");
        final Reference ref = fac.newReference("#" + id, fac.newDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", null), transformList, null, null);
        final SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null), Collections.singletonList(ref));
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final X509Data xData = kif.newX509Data(Collections.singletonList(cert));
        final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xData));
        dbf.setNamespaceAware(false);
        final DOMSignContext dsc = new DOMSignContext(privateKey, this.doc.getElementsByTagName("infCFe").item(0));
        dsc.setParent(this.doc.getFirstChild());
        final XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);
        if (this.serializarCert) {
            return serializarXMLSignature(this.doc);
        }
        return this.doc;
    }
    
    public static boolean validarAssinatura(final String xml, final PublicKey key, final String tagName) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setNamespaceAware(true);
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            final Document doc = db.parse(is);
            final NodeList list = doc.getElementsByTagName("Signature");
            final Node tagSig = list.item(0);
            final DOMValidateContext validationContext = new DOMValidateContext(key, tagSig);
            validationContext.setIdAttributeNS((Element)doc.getElementsByTagName(tagName).item(0), null, "Id");
            final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            final XMLSignature signature = fac.unmarshalXMLSignature(validationContext);
            final boolean coreValidity = signature.validate(validationContext);
            if (coreValidity) {
                return true;
            }
            System.err.println("Signature failed core validation");
            final boolean sv = signature.getSignatureValue().validate(validationContext);
            System.err.println("signature validation status: " + sv);
            final Iterator i = signature.getSignedInfo().getReferences().iterator();
            int j = 0;
            while (i.hasNext()) {
           /*FEITO CAST*/     final boolean refValid = ((XMLSignature) i.next()).validate(validationContext);
                System.err.println("ref[" + j + "] validity status: " + refValid);
                ++j;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static synchronized boolean validarAssinaturaXml(final String xml, final String tagName) throws CertificateException, IOException {
        if (!Configuracoes.SAT.validarRetornoXML) {
            return true;
        }
        if (xml.indexOf("Signature") < 0) {
            return false;
        }
        try {
            String x509Data = ControleDados.getConteudoTAG(xml, "X509Certificate");
            x509Data = ControleDados.formatarCertificado(x509Data, true);
            final X509Certificate certXML = (X509Certificate)ControleKeyStore.parseCertificado(x509Data.getBytes());
            if (validarAssinatura(xml, certXML.getPublicKey(), tagName)) {
                final ControleCadeiaCertificado cadeia = ControleCadeiaCertificado.getInstance();
                cadeia.setCadeiaCompleta(Configuracoes.SAT.validarCadeiaCertificacao);
                Certificado emissor = null;
                if ((emissor = cadeia.procurarEmissor(certXML)) == null) {
                    ControleLogs.logar("Erro: Certificado da Assinatura XML do Retorno n\u00e3o pertence a cadeia de Certifica\u00e7\u00e3o do SAT.");
                    return false;
                }
                if (Configuracoes.SAT.validarPeriodoCertificados) {
                    if (!cadeia.validarData(certXML)) {
                        ControleLogs.logar("Erro: o Certificado do Retorno XML est\u00e1 fora do per\u00edodo de validade.");
                        return false;
                    }
                    if (!cadeia.validarDataCadeia(emissor)) {
                        ControleLogs.logar("Erro: o Certificado do Raiz ou Intermedi\u00e1rio est\u00e1 fora do per\u00edodo de validade.");
                        return false;
                    }
                }
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void setSerializarCert(final boolean serializarCert) {
        this.serializarCert = serializarCert;
    }
}
