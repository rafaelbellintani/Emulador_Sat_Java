// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.text.DecimalFormat;
import java.util.Vector;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.text.Normalizer;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class StringUtil
{
    private StringUtil() {
    }
    
    public static String inputStreamToString(final InputStream is, String charset) {
        if (charset == null) {
            charset = "UTF-8";
        }
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(is, charset));
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
    
    public static String inputStreamToString(final InputStream is) {
        return inputStreamToString(is, "utf-8");
    }
    
    public static String removerMascara(String texto) {
        texto = texto.replace(".", "").replace("-", "").replace("/", "").replace(" ", "").replace(",", "");
        return texto;
    }
    
    public static synchronized String removerAcentuacao(String acentuada) {
        if (!Normalizer.isNormalized(acentuada, Normalizer.Form.NFD)) {
            acentuada = Normalizer.normalize(acentuada, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        return acentuada;
    }
    
    public static boolean isBase64(String dados) {
        dados = dados.trim();
        return dados.length() % 4 == 0;
    }
    
    public static String base64Encode(final byte[] valor) {
        return Base64.encode(valor);
    }
    
    public static String base64Encode(final String valor) {
        return Base64.encode(valor.getBytes());
    }
    
    public static byte[] base64Decode(final String valor) {
        try {
            return Base64.decode(valor);
        }
        catch (Base64DecodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String base64DecodeUTF8(final String valor) {
        try {
            return new String(Base64.decode(valor), "utf-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
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
            linhasFinal[i] = linhas.elementAt(i);
        }
        return linhasFinal;
    }
    
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
    
    public static String preencheEsqueda(final String original, final char letra, final int tamanho) {
        return preencheCom(original, letra, tamanho, 1);
    }
    
    public static String preencheDireita(final String original, final char letra, final int tamanho) {
        return preencheCom(original, letra, tamanho, 2);
    }
    
    public static String formatarDecimal(final String numero, final int minimo, final int maximo, final int casas) {
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
