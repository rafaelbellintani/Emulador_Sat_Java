// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
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
import java.text.Normalizer;
import java.io.FileInputStream;
import java.io.File;
import java.security.MessageDigest;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class ControleDados
{
    @Deprecated
    public static final int ESQUERDA = 1;
    @Deprecated
    public static final int DIREITA = 2;
    
    @Deprecated
    public static String inputStreamToString(final InputStream is) {
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            int character = 0;
            final StringBuffer strBuffer = new StringBuffer(1000);
            while ((character = rd.read()) != -1) {
                strBuffer.append((char)character);
            }
            return strBuffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                is.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
    
    public static String byteToHex(final byte[] buf, final int off, final int len) {
        final StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = off; i < len; ++i) {
            if ((buf[i] & 0xFF) < 16) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString(buf[i] & 0xFF, 16));
        }
        return strbuf.toString();
    }
    
    public static String getHashArquivo(final String algoritmo, final String path) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algoritmo);
            final File f = new File(path);
            final InputStream is = new FileInputStream(f);
            final byte[] buffer = new byte[8192];
            int read = 0;
            try {
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                final byte[] md5sum = digest.digest();
                return byteToHex(md5sum, 0, md5sum.length);
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to process file for MD5", e);
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException e2) {
                    throw new RuntimeException("Unable to close input stream for MD5 calculation", e2);
                }
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }
    
    @Deprecated
    public static synchronized StringBuffer addCData(final StringBuffer xml) {
        return new StringBuffer("<![CDATA[").append(xml).append("]]>");
    }
    
    @Deprecated
    public static synchronized String addCData(final String xml) {
        return new String("<![CDATA[" + xml + "]]>");
    }
    
    @Deprecated
    public static synchronized String removerCData(final String xml) {
        return xml.replace("<![CDATA[", "").replace("]]>", "");
    }
    
    @Deprecated
    public static String removerMascara(String texto) {
        texto = texto.replace(".", "").replace("-", "").replace("/", "").replace(" ", "").replace(",", "");
        return texto;
    }
    
    public static boolean validarModulo11(final String valor) {
        if (valor == null) {
            return false;
        }
        final int dv = Integer.parseInt(valor.substring(valor.length() - 1));
        final String[] c = valor.split("\\B");
        final int[] pesos = { 2, 3, 4, 5, 6, 7, 8, 9 };
        int soma = 0;
        int i = c.length - 2;
        int j = 0;
        while (i >= 0) {
            if (j >= pesos.length) {
                j = 0;
            }
            soma += Integer.parseInt(c[i]) * pesos[j];
            ++j;
            --i;
        }
        final int dvc = soma % 11;
        if (dvc > 1) {
            return 11 - dvc == dv;
        }
        return dv == 0;
    }
    
    @Deprecated
    public static synchronized String removerAcentuacao(String acentuada) {
        if (!Normalizer.isNormalized(acentuada, Normalizer.Form.NFD)) {
            acentuada = Normalizer.normalize(acentuada, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        return acentuada;
    }
    
    @Deprecated
    public static synchronized String removerCabecalhoXML(final String dados) {
        final String retorno = dados.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "").replace("<?xml version=\"1.0\"?>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
        return retorno;
    }
    
    @Deprecated
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
    
    @Deprecated
    public static synchronized Document parseXML(final String xml) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setNamespaceAware(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
        final Document doc = db.parse(is);
        return doc;
    }
    
    @Deprecated
    public static synchronized String recuperaTag(final String xml, final String tag) {
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
    
    @Deprecated
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
    
    @Deprecated
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
    
    @Deprecated
    public static synchronized String getAtributoTAG(final String xml, final String tag, final String atrr) {
        String r = null;
        try {
            if (xml != null && xml.indexOf("<" + tag) >= 0) {
                r = xml.substring(xml.indexOf("<" + tag), xml.indexOf("</" + tag));
                r = r.substring(r.indexOf(String.valueOf(atrr) + "=\"") + (String.valueOf(atrr) + "=\"").length());
                r = r.substring(0, r.indexOf("\""));
            }
        }
        catch (Exception e) {
            r = null;
        }
        return r;
    }
    
    public static final String gerarHashMD5(final String conteudo) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final BigInteger hash = new BigInteger(1, md.digest(conteudo.getBytes()));
        for (sen = hash.toString(16); sen.length() < 32; sen = "0" + sen) {}
        return sen;
    }
    
    public static final String gerarHashSHA1(final String conteudo) {
        String sen = "";
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-1");
            final BigInteger hash = new BigInteger(1, md.digest(conteudo.getBytes()));
            for (sen = hash.toString(16); sen.length() < 40; sen = "0" + sen) {}
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sen;
    }
    
    @Deprecated
    public static boolean isBase64(String dados) {
        dados = dados.trim();
        return dados.length() % 4 == 0;
    }
    
    @Deprecated
    public static String base64Encode(final String valor) {
        return Base64.encode(valor.getBytes());
    }
    
    @Deprecated
    public static String base64Decode(final String valor) {
        try {
            return new String(Base64.decode(valor));
        }
        catch (Base64DecodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Deprecated
    public static String[] quebrarString(final String conteudo, final String delimitador) {
        String cont = conteudo;
        final Vector linhas = new Vector();
        while (cont.indexOf(delimitador) != -1) {
            linhas.addElement(cont.substring(0, cont.indexOf(delimitador)));
            cont = cont.substring(cont.indexOf(delimitador) + delimitador.length(), cont.length());
        }
        linhas.addElement(cont);
        final String[] linhasFinal = new String[linhas.size()];
        for (int i = 0; i <= linhas.size() - 1; ++i) {
            linhasFinal[i] = (String) linhas.elementAt(i);
        }
        return linhasFinal;
    }
    
    public static synchronized String formatarCertificado(String cert, final boolean isCertificate) {
        try {
            if (cert == null) {
                return null;
            }
            cert = cert.replace("-----BEGIN CERTIFICATE-----", "");
            cert = cert.replace("-----END CERTIFICATE-----", "");
            cert = cert.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("\\t", "");
            final byte[] array = cert.getBytes();
            final byte[] newArray = new byte[array.length + array.length / 64];
            for (int j = 0, i = 0, n = 1; i < array.length; ++i, ++n, ++j) {
                newArray[j] = array[i];
                if (n == 64) {
                    ++j;
                    n = 0;
                    newArray[j] = 10;
                }
            }
            if (isCertificate) {
                return "-----BEGIN CERTIFICATE-----\n" + new String(newArray) + '\n' + "-----END CERTIFICATE-----";
            }
            return new String(newArray);
        }
        catch (Exception e) {
            e.printStackTrace();
            return cert;
        }
    }
    
    @Deprecated
    public static String formatarData(final String data) {
        if (data.contains("T")) {
            return String.valueOf(data.substring(8, 10)) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4) + " " + data.substring(11);
        }
        return String.valueOf(data.substring(6, 10)) + "-" + data.substring(3, 5) + "-" + data.substring(0, 2) + "T" + data.substring(11);
    }
    
    @Deprecated
    public static String preencheCom(String linha_a_preencher, final String letra, final int tamanho, final int direcao) {
        if (linha_a_preencher == null || linha_a_preencher.trim() == "") {
            linha_a_preencher = "";
        }
        final StringBuffer sb = new StringBuffer(linha_a_preencher);
        if (direcao == 1) {
            for (int i = sb.length(); i < tamanho; ++i) {
                sb.insert(0, letra);
            }
        }
        else if (direcao == 2) {
            for (int i = sb.length(); i < tamanho; ++i) {
                sb.append(letra);
            }
        }
        return sb.toString();
    }
    
    @Deprecated
    private static String preencheCom(String original, final char letra, final int tamanho, final int direcao) {
        final int n = original.length();
        final String c = String.valueOf(letra);
        String s = "";
        if (original == null || n >= tamanho) {
            return original;
        }
        if (n < tamanho) {
            for (int i = 0; i < tamanho - n; ++i) {
                s = String.valueOf(s) + c;
            }
        }
        if (direcao == 1) {
            original = String.valueOf(s) + original;
        }
        else {
            original = String.valueOf(original) + s;
        }
        return original;
    }
    
    @Deprecated
    public static String preencheEsqueda(final String original, final char letra, final int tamanho) {
        return preencheCom(original, letra, tamanho, 1);
    }
    
    @Deprecated
    public static String preencheDireita(final String original, final char letra, final int tamanho) {
        return preencheCom(original, letra, tamanho, 2);
    }
    
    public static boolean validarCodMunicipio(final String cCodMun) {
        try {
            if (cCodMun.length() != 7) {
                return false;
            }
            final String[] validos = { "4305871", "2201919", "2202251", "2201988", "2611533", "3117836", "3152131", "5203939", "5203962" };
            final String[] uf = { "11", "12", "13", "14", "15", "16", "17", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31", "32", "33", "35", "41", "42", "43", "50", "51", "52", "53" };
            Arrays.sort(validos);
            final int n = Arrays.binarySearch(validos, cCodMun);
            if (n >= 0) {
                return true;
            }
            final String key = cCodMun.substring(0, 2);
            final int n2 = Arrays.binarySearch(uf, key);
            if (n2 < 0) {
                return false;
            }
            final int[] fator = { 1, 2, 1, 2, 1, 2 };
            final int[] digits = new int[cCodMun.length() - 1];
            final int digitoVerificador = Integer.parseInt(cCodMun.substring(cCodMun.length() - 1));
            for (int i = 0; i < cCodMun.length() - 1; ++i) {
                digits[i] = Integer.parseInt(cCodMun.substring(i, i + 1));
            }
            int sum = 0;
            for (int j = 0; j < digits.length; ++j) {
                sum += ((digits[j] * fator[j] > 9) ? (digits[j] * fator[j] - 9) : (digits[j] * fator[j]));
            }
            int check = 10 - sum % 10;
            check = ((check == 10) ? 0 : check);
            return check == digitoVerificador;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean isNumerico(final String n) {
        try {
            Double.parseDouble(n);
            return true;
        }
        catch (Exception ee) {
            return false;
        }
    }
    
    public static boolean isInteger(final String n) {
        try {
            Integer.parseInt(n);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    @Deprecated
    public static String replaceAll(String conteudo, final String antigo, final String novo) {
        while (conteudo.indexOf(antigo) != -1) {
            final String conteudo2 = conteudo.substring(0, conteudo.indexOf(antigo));
            final String conteudo3 = conteudo.substring(conteudo.indexOf(antigo) + antigo.length());
            conteudo = String.valueOf(conteudo2) + novo + conteudo3;
        }
        return conteudo;
    }
    
    @Deprecated
    public static byte[] encodedByte(final byte[] conteudo) {
        int count = 0;
        for (int i = 0; i < conteudo.length; ++i) {
            if (conteudo[i] == 43) {
                ++count;
            }
        }
        final int tamanho = count * 2 + conteudo.length;
        final byte[] byteEncoded = new byte[tamanho];
        int j = 0;
        for (int k = 0; k < conteudo.length; ++k) {
            if (conteudo[k] == 43) {
                byteEncoded[j] = 37;
                ++j;
                byteEncoded[j] = 50;
                ++j;
                byteEncoded[j] = 98;
                ++j;
            }
            else {
                byteEncoded[j] = conteudo[k];
                ++j;
            }
        }
        return byteEncoded;
    }
    
    public static boolean validarGTIN(final String gtin) {
        try {
            if (gtin.length() != 8 && gtin.length() != 12 && gtin.length() != 13 && gtin.length() != 14) {
                return false;
            }
            final int[] fator = { 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3 };
            final int[] digits = new int[gtin.length() - 1];
            final int digitoVerificador = Integer.parseInt(gtin.substring(gtin.length() - 1));
            for (int i = 0; i < gtin.length() - 1; ++i) {
                digits[i] = Integer.parseInt(gtin.substring(i, i + 1));
            }
            int sum = 0;
            for (int j = digits.length - 1, k = fator.length - 1; j >= 0; --j, --k) {
                sum += digits[j] * fator[k];
            }
            int check = 10 - sum % 10;
            check = ((check == 10) ? 0 : check);
            return check == digitoVerificador;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean validarGTIN8(final String gtin8) {
        if (gtin8.length() != 8 || gtin8.equals("00000000")) {
            return false;
        }
        try {
            Long.parseLong(gtin8);
        }
        catch (Exception e) {
            return false;
        }
        return validarGTIN(gtin8);
    }
    
    public static boolean validarGTIN12(final String gtin12) {
        if (gtin12.length() != 12 || gtin12.equals("000000000000")) {
            return false;
        }
        try {
            Long.parseLong(gtin12);
        }
        catch (Exception e) {
            return false;
        }
        return validarGTIN(gtin12);
    }
    
    public static boolean validarGTIN13(final String gtin13) {
        if (gtin13.length() != 13 || gtin13.equals("0000000000000")) {
            return false;
        }
        try {
            Long.parseLong(gtin13);
        }
        catch (Exception e) {
            return false;
        }
        return validarGTIN(gtin13);
    }
    
    public static boolean validarGTIN14(final String gtin14) {
        if (gtin14.length() != 14 || gtin14.equals("00000000000000")) {
            return false;
        }
        try {
            Long.parseLong(gtin14);
        }
        catch (Exception e) {
            return false;
        }
        return validarGTIN(gtin14);
    }
    
    public static boolean validarDouble(final int tamanhoMinimo, final int tamanhoMaximo, final int tamanhoAposVirgula, String numero) {
        if (!isNumerico(numero)) {
            return false;
        }
        if (numero.indexOf("-") >= 0) {
            numero = numero.replace("-", "");
        }
        final int index = numero.indexOf(".");
        if (index == -1) {
            return false;
        }
        final String antes = numero.substring(0, index);
        final String depois = numero.substring(index + 1);
        return antes.length() >= tamanhoMinimo && numero.replace(".", "").length() <= tamanhoMaximo && depois.length() == tamanhoAposVirgula;
    }
    
    public static boolean validarCNPJCPF(final String CNPJ) {
        if (!isNumerico(CNPJ)) {
            return false;
        }
        if (CNPJ.length() == 11) {
            int d3;
            int d2 = d3 = 0;
            int resto;
            int digito3;
            int digito2 = digito3 = (resto = 0);
            for (int n_Count = 1; n_Count < CNPJ.length() - 1; ++n_Count) {
                final int digitoCPF = Integer.valueOf(CNPJ.substring(n_Count - 1, n_Count));
                d3 += (11 - n_Count) * digitoCPF;
                d2 += (12 - n_Count) * digitoCPF;
            }
            resto = d3 % 11;
            if (resto < 2) {
                digito3 = 0;
            }
            else {
                digito3 = 11 - resto;
            }
            d2 += 2 * digito3;
            resto = d2 % 11;
            if (resto < 2) {
                digito2 = 0;
            }
            else {
                digito2 = 11 - resto;
            }
            final String nDigVerific = CNPJ.substring(CNPJ.length() - 2, CNPJ.length());
            final String nDigResult = String.valueOf(String.valueOf(digito3)) + String.valueOf(digito2);
            return nDigVerific.equals(nDigResult);
        }
        if (CNPJ.length() == 14) {
            int soma = 0;
            String cnpj_calc = CNPJ.substring(0, 12);
            final char[] chr_cnpj = CNPJ.toCharArray();
            for (int i = 0; i < 4; ++i) {
                if (chr_cnpj[i] - '0' >= 0 && chr_cnpj[i] - '0' <= 9) {
                    soma += (chr_cnpj[i] - '0') * (6 - (i + 1));
                }
            }
            for (int i = 0; i < 8; ++i) {
                if (chr_cnpj[i + 4] - '0' >= 0 && chr_cnpj[i + 4] - '0' <= 9) {
                    soma += (chr_cnpj[i + 4] - '0') * (10 - (i + 1));
                }
            }
            int dig = 11 - soma % 11;
            cnpj_calc = String.valueOf(cnpj_calc) + ((dig != 10 && dig != 11) ? Integer.toString(dig) : "0");
            soma = 0;
            for (int j = 0; j < 5; ++j) {
                if (chr_cnpj[j] - '0' >= 0 && chr_cnpj[j] - '0' <= 9) {
                    soma += (chr_cnpj[j] - '0') * (7 - (j + 1));
                }
            }
            for (int j = 0; j < 8; ++j) {
                if (chr_cnpj[j + 5] - '0' >= 0 && chr_cnpj[j + 5] - '0' <= 9) {
                    soma += (chr_cnpj[j + 5] - '0') * (10 - (j + 1));
                }
            }
            dig = 11 - soma % 11;
            cnpj_calc = String.valueOf(cnpj_calc) + ((dig != 10 && dig != 11) ? Integer.toString(dig) : "0");
            return CNPJ.equals(cnpj_calc);
        }
        return false;
    }
    
    @Deprecated
    public static String formatarDouble(final String numero, final int minimo, final int maximo, final int casas) {
        String mascara = "";
        for (int i = 0; i < minimo; ++i) {
            mascara = String.valueOf(mascara) + "0";
        }
        if (casas > 0) {
            mascara = String.valueOf(mascara) + ".";
        }
        for (int i = 0; i < casas; ++i) {
            mascara = String.valueOf(mascara) + "0";
        }
        final DecimalFormat df = new DecimalFormat(mascara);
        return df.format(Double.parseDouble(numero)).replace(",", ".");
    }
    
    public static int getRandom(final int tamanho) {
        int max = 1;
        for (int i = 0; i < tamanho; ++i) {
            max *= 10;
        }
        final int resultado = new Random().nextInt(max);
        if (resultado >= max / 10) {
            return resultado;
        }
        return getRandom(tamanho);
    }
    
    @Deprecated
    public static List getListaTags(String xml, final String tag) {
        if (xml != null && tag != null && xml.indexOf(tag) >= 0) {
            final List lista = new ArrayList();
            do {
                final String t = recuperaTag(xml, tag);
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
    public static int contarOcorrencia(final String alvo, final String ocorrencia) {
        int pos = -1;
        int contagem = 0;
        while (true) {
            pos = alvo.indexOf(ocorrencia, pos + 1);
            if (pos < 0) {
                break;
            }
            ++contagem;
        }
        return contagem;
    }
}
