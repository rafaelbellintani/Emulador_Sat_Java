// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.text.Normalizer;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import br.com.satcfe.satbl.Configuracoes;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;

public class ControleArquivos
{
    public static synchronized String lerArquivoUTF8(final String caminho) {
        StringBuffer saida = null;
        BufferedReader rd = null;
        try {
            final File f = new File(caminho);
            final int qtdBytesArquivo = (int)f.length();
            if (qtdBytesArquivo > 0) {
                rd = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
                saida = new StringBuffer(qtdBytesArquivo);
                int character;
                while ((character = rd.read()) != -1) {
                    saida.append((char)character);
                }
                if (rd != null) {
                    rd.close();
                }
                return saida.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (rd != null) {
                rd.close();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return "";
    }
    
    public static synchronized String lerArquivo(final String caminho) {
        BufferedReader rd = null;
        StringBuffer saida = null;
        try {
            File arquivo = null;
            arquivo = new File(caminho);
            final int qtdBytesArquivo = (int)arquivo.length();
            if (qtdBytesArquivo > 0) {
                rd = new BufferedReader(new FileReader(arquivo));
                saida = new StringBuffer(qtdBytesArquivo);
                int character;
                while ((character = rd.read()) != -1) {
                    saida.append((char)character);
                }
                if (rd != null) {
                    rd.close();
                }
                return saida.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (rd != null) {
                rd.close();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return "";
    }
    
    public static synchronized String lerCaracteresArquivo(final String caminho) {
        BufferedReader leitor = null;
        StringBuffer saida = null;
        try {
            final File arquivo = new File(caminho);
            final int qtdBytesArquivo = (int)arquivo.length();
            if (qtdBytesArquivo > 0) {
                leitor = new BufferedReader(new FileReader(arquivo));
                saida = new StringBuffer(qtdBytesArquivo);
                int character;
                while ((character = leitor.read()) != -1) {
                    saida.append((char)character);
                }
                if (leitor != null) {
                    leitor.close();
                }
                return saida.toString();
            }
            return "";
        }
        catch (Exception ex) {}
        finally {
            try {
                if (leitor != null) {
                    leitor.close();
                }
            }
            catch (IOException ex2) {}
        }
        return "";
    }
    
    public static synchronized boolean escreverCaracteresArquivo(final String caminho, final char[] b) {
        return escreverCaracteresArquivoUTF8(caminho, b, false);
    }
    
    public static synchronized boolean escreverCaracteresArquivoUTF8(final String caminho, char[] b, final boolean concatenar) {
        File arquivo = null;
        OutputStreamWriter escritor = null;
        try {
            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                String temp = String.valueOf(b);
                temp = trocaAcentuacao(temp);
                b = temp.toCharArray();
            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
            	String temp = String.valueOf(b);
                temp = trocaAcentuacao(temp);
                b = temp.toCharArray();
            }
            arquivo = new File(caminho);
            if (!concatenar) {
                if (arquivo.exists()) {
                    arquivo.delete();
                }
                criarArquivo(caminho);
                escreverCaracteresArquivoUTF8(caminho, b, true);
                return true;
            }
            if (!arquivo.exists()) {
                criarArquivo(caminho);
            }
            escritor = new OutputStreamWriter(new FileOutputStream(arquivo, concatenar), "UTF-8");
            escritor.write(b);
            escritor.flush();
            escritor.close();
            return true;
        }
        catch (Exception ex) {}
        finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
            }
            catch (Exception ex2) {}
        }
        return false;
    }
    
    public static synchronized boolean escreverCaracteresArquivoASCII(final String caminho, char[] b, final boolean concatenar) {
        File arquivo = null;
        Writer escritor = null;
        try {
            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                String temp = String.valueOf(b);
                temp = trocaAcentuacao(temp);
                b = temp.toCharArray();
            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
            	String temp = String.valueOf(b);
                temp = trocaAcentuacao(temp);
                b = temp.toCharArray();
            }
            arquivo = new File(caminho);
            if (!concatenar) {
                if (arquivo.exists()) {
                    arquivo.delete();
                }
                criarArquivo(caminho);
                escreverCaracteresArquivoASCII(caminho, b, true);
                return true;
            }
            if (!arquivo.exists()) {
                criarArquivo(caminho);
            }
            escritor = new OutputStreamWriter(new FileOutputStream(arquivo, concatenar));
            escritor.write(b);
            escritor.flush();
            escritor.close();
            return true;
        }
        catch (Exception ex) {}
        finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
            }
            catch (Exception ex2) {}
        }
        return false;
    }
    
    public static synchronized String trocaAcentuacao(String acentuada) {
        if (!Normalizer.isNormalized(acentuada, Normalizer.Form.NFD)) {
            acentuada = Normalizer.normalize(acentuada, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        return acentuada;
    }
    
    @Deprecated
    public static synchronized String retiraAcentuacao(String acentuada) {
        if (acentuada.length() <= 0) {
            return acentuada;
        }
        final char[] acentuados = { '\u00e7', '\u00e1', '\u00e0', '\u00e3', '\u00e2', '\u00e4', '\u00e9', '\u00e8', '\u00ea', '\u00eb', '\u00ed', '\u00ec', '\u00ee', '\u00ef', '\u00f3', '\u00f2', '\u00f5', '\u00f4', '\u00f6', '\u00fa', '\u00f9', '\u00fb', '\u00fc' };
        final char[] naoAcentuados = { 'c', 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u' };
        for (int i = 0; i < acentuados.length; ++i) {
            acentuada = acentuada.replace(acentuados[i], naoAcentuados[i]);
            acentuada = acentuada.replace(Character.toUpperCase(acentuados[i]), Character.toUpperCase(naoAcentuados[i]));
        }
        return acentuada;
    }
    
    public static synchronized long tamanhoArquivo(final String caminho) {
        File arquivo = null;
        arquivo = new File(caminho);
        return arquivo.length();
    }
    
    public static synchronized boolean existeArquivo(final String caminho) {
        File novoArquivo = null;
        novoArquivo = new File(caminho);
        return novoArquivo.exists();
    }
    
    public static synchronized boolean excluirArquivo(final String caminho) {
        System.gc();
        File novoArquivo = null;
        novoArquivo = new File(caminho);
        novoArquivo.delete();
        return !novoArquivo.exists();
    }
    
    public static synchronized boolean criarArquivo(final String caminho) {
        File novoArquivo = null;
        novoArquivo = new File(caminho);
        boolean resultado = false;
        try {
            resultado = novoArquivo.createNewFile();
        }
        catch (IOException ex) {}
        return resultado;
    }
    
    public static synchronized boolean criarDiretorio(final String caminho) {
        File novoArquivo = null;
        novoArquivo = new File(caminho);
        return novoArquivo.mkdirs();
    }
    
    public static synchronized String[] listarDiretorio(final String caminho) {
        File dir = null;
        String[] lista = null;
        dir = new File(caminho);
        if (dir.exists() && dir.isDirectory()) {
            lista = dir.list();
        }
        return lista;
    }
    
    public static synchronized void removerTodosArquivos(final String path) {
        final String[] arquivos = listarDiretorio(path);
        if (arquivos == null || arquivos.length < 1) {
            return;
        }
        for (int i = 0; i < arquivos.length; ++i) {
            excluirArquivo(String.valueOf(path) + arquivos[i]);
        }
    }
    
    private void obterDataCriacaoArquivo() {
    }
}
