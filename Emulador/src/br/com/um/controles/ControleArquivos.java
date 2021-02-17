// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
    
    public static synchronized boolean escreverCaracteresArquivo(final String caminho, final char[] b) {
        return escreverCaracteresArquivo(caminho, b, false);
    }
    
    public static synchronized boolean escreverCaracteresArquivo(final String caminho, final char[] b, final boolean concatenar) {
        File arquivo = null;
        BufferedWriter escritor = null;
        try {
            arquivo = new File(caminho);
            if (!concatenar) {
                if (arquivo.exists()) {
                    arquivo.delete();
                }
                criarArquivo(caminho);
                escreverCaracteresArquivo(caminho, b, true);
                return true;
            }
            if (!arquivo.exists()) {
                criarArquivo(caminho);
            }
            escritor = new BufferedWriter(new FileWriter(arquivo));
            escritor.write(b);
            escritor.flush();
            escritor.close();
            return true;
        }
        catch (IOException ex) {}
        catch (Exception ex2) {}
        finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
            }
            catch (Exception ex3) {}
        }
        return false;
    }
    
    public static synchronized boolean escreverCaracteresArquivoUTF8(final String caminho, final char[] b, final boolean concatenar) {
        File arquivo = null;
        OutputStreamWriter escritor = null;
        try {
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
            escritor = new OutputStreamWriter(new FileOutputStream(arquivo, concatenar), "utf-8");
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
    
    public static synchronized long getTamanhoArquivo(final String caminho) {
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
    
    public static synchronized boolean moverArquivo(final String origem, final String destino) {
        try {
            if (origem != null && destino != null) {
                final File fOrigem = new File(origem);
                return fOrigem.renameTo(new File(destino));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
}
