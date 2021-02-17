// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import java.util.Iterator;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.MarshalException;
import java.io.FileNotFoundException;
import java.security.KeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import br.com.satcfe.satbl.controles.ControleAssinaturaXML;
import javax.xml.crypto.dsig.XMLSignContext;
import java.security.Key;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.XMLStructure;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import java.security.spec.InvalidKeySpecException;
import br.com.um.controles.StringUtil;
import javax.xml.crypto.dsig.Reference;
import java.util.Collections;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.Transform;
import java.util.List;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import org.w3c.dom.Element;
import br.com.um.controles.ControleDados;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import java.util.ArrayList;
import br.com.um.modelos.Decimal;
import java.math.BigDecimal;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.controles.ControleLogs;
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

public class CFe
{
    private static String falhaCFe;
    private InformacoesCFe infCFe;
    public static final int TESTE = 2;
    public static final int REAL = 1;
    
    static {
        CFe.falhaCFe = null;
    }
    
    public CFe() {
        this.infCFe = null;
        CFe.falhaCFe = null;
    }
    
    public CFe(final String xml) throws ParserConfigurationException, SAXException, IOException {
        this.infCFe = null;
        CFe.falhaCFe = null;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        final Document doc = db.parse(is);
        final NodeList raizes = doc.getChildNodes();
        this.parse(raizes.item(0));
    }
    
    @Deprecated
    public CFe(final Node raiz) {
        this.infCFe = null;
        CFe.falhaCFe = null;
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
        try {
            if (this.infCFe == null) {
                resultado = "1999";
            }
            else {
                (resultado = this.infCFe.validar()).equals("1000");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            resultado = "1999";
        }
        if (CFe.falhaCFe != null) {
            return CFe.falhaCFe;
        }
        return resultado;
    }
    
    public String validarCamposAposCalculo() {
        String resultado = "1000";
        try {
            if (this.infCFe.getTotal().getvCFe() == null) {
                resultado = "1999";
            }
            else if (Double.parseDouble(this.infCFe.getTotal().getvCFe()) > Double.parseDouble(this.somarValoresMP().toString())) {
                ControleLogs.logar("Erro no Campo 'vCFe' : Campo vCFe maior que a soma dos valores do MP.");
                resultado = "1408";
            }
            else if (Double.parseDouble(this.infCFe.getTotal().getvCFe()) > Parametrizacoes.limiteCFe) {
                ControleLogs.logar("Erro no Campo 'vCFe' : Campo vCFe maior que o Permitido.");
                resultado = "1409";
            }
            else if (Double.parseDouble(this.infCFe.getTotal().getvCFe()) < 0.0) {
                ControleLogs.logar("Erro no Campo 'vCFe' : Campo vCFe menor que zero.");
                resultado = "1999";
            }
            if (resultado.equals("1000")) {
                for (int i = 0; i < this.infCFe.getDet().size(); ++i) {
                    if (Double.parseDouble(this.infCFe.getDet().get(i).getProd().getvItem()) < 0.0) {
                        ControleLogs.logar("Erro no Campo 'vItem' : Campo vItem menor que zero.");
                        resultado = "1999";
                    }
                }
            }
            if (resultado.equals("1000") && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE)) {
                final long esse_cfe = ControleTempo.parseTimeStamp(String.valueOf(this.infCFe.getIde().getdEmi()) + this.infCFe.getIde().gethEmi());
                final long ultimo_cfe = ControleTempo.parseTimeStamp(ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIMESTAMP_ULTIMO_CFE));
                if (ultimo_cfe > esse_cfe) {
                    ControleLogs.logar("Erro na Data e hora: Valida\u00e7\u00e3o se data/hora \u00e9 anterior \u00e0 data/hora do \u00faltimo CF-e-SAT emitido ou cancelado");
                    resultado = "1258";
                }
            }
            if (resultado.equals("1000") && this.infCFe.getDet().size() > 300) {
                final int maximo = 1500000;
                final int envCFe = 600;
                final int signature = 3500;
                final String preCFe = this.toString();
                if (envCFe + preCFe.length() + signature > maximo) {
                    ControleLogs.logar("Erro no CFe : Tamanho do XML de Dados superior a 1.500 Kbytes.");
                    resultado = "1999";
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "1999";
        }
        return resultado;
    }
    
    private BigDecimal somarValoresMP() {
        BigDecimal totalMP = new BigDecimal("0");
        final ArrayList<MP> mps = this.infCFe.getPgto().getMp();
        for (int i = 0; i < mps.size(); ++i) {
            totalMP = Decimal.somar(totalMP.toString(), mps.get(i).getvMP());
        }
        return totalMP;
    }
    
    public void completar(final int tipo, final String senhaKeyStore) {
        this.infCFe.completar(tipo, senhaKeyStore);
    }
    
    @Deprecated
    public Document assinar(final String xml, final String codigoAtivacao) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, KeyException, FileNotFoundException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, InstantiationException, IllegalAccessException, ClassNotFoundException, DataLengthException, IllegalStateException, InvalidCipherTextException, TransformerFactoryConfigurationError, TransformerException {
        final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final CanonicalizationMethod cm = fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null);
        final ArrayList transformList = new ArrayList();
        final TransformParameterSpec tps = null;
        final Transform envelopedTransform = fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", tps);
        final Transform c14NTransform = fac.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);
        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final Document doc = ControleDados.parseXML(xml);
        final NodeList elements = doc.getElementsByTagName("infCFe");
        final Element el = (Element)elements.item(0);
        final String id = el.getAttribute("Id");
        final Reference ref = fac.newReference("#" + id, fac.newDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", null), transformList, null, null);
        final SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null), Collections.singletonList(ref));
        final String base64ks = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA);
        byte[] ks;
        try {
            ks = StringUtil.base64Decode(base64ks);
        }
        catch (Exception e) {
            throw new InvalidKeySpecException();
        }
        final byte[] ksDescriptografado = ControleSeguranca.descriptografarAES(codigoAtivacao.getBytes(), ks);
        final String[] keyStore = new String(ksDescriptografado).split("\\:");
        final PublicKey publica = ControleSeguranca.deserializarPublicaRSA(keyStore[0]);
        final PrivateKey privada = ControleSeguranca.deserializarPrivadaRSA(keyStore[1]);
        final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
        cKeyStore.carregarKeyStore();
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final X509Data xData = kif.newX509Data(Collections.singletonList(cKeyStore.getCertificado()));
        final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xData));
        dbf.setNamespaceAware(false);
        final DOMSignContext dsc = new DOMSignContext(privada, doc.getElementsByTagName("infCFe").item(0));
        dsc.setParent(doc.getElementsByTagName("CFe").item(0));
        final XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);
        this.validate(doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0), publica);
        return ControleAssinaturaXML.serializarXMLSignature(doc);
    }
    
    @Deprecated
    public boolean validate(final Node sig, final PublicKey publica) throws MarshalException, XMLSignatureException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final DOMValidateContext validationContext = new DOMValidateContext(publica, sig);
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        final XMLSignature signature = fac.unmarshalXMLSignature(validationContext);
        final boolean coreValidity = signature.validate(validationContext);
        if (!coreValidity) {
            final boolean sv = signature.getSignatureValue().validate(validationContext);
            if (!sv) {
                final Iterator i = signature.getSignedInfo().getReferences().iterator();
                int j = 0;
                while (i.hasNext()) {
                 /*FEITO CAST*/ ((XMLSignature) i.next()).validate(validationContext);
                    ++j;
                }
            }
        }
        return coreValidity;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<CFe>");
        this.infCFe.toString(sb);
        sb.append("</CFe>");
        return sb.toString();
    }
    
    public static void setFalhaCFe(final String falha) {
        CFe.falhaCFe = falha;
    }
}
