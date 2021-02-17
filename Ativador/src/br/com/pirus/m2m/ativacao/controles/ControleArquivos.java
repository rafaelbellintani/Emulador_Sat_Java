// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.controles;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class ControleArquivos
{
    public static synchronized String lerBytesArquivo(final String caminho) {
        File arquivo = null;
        BufferedReader leitor = null;
        try {
            arquivo = new File(caminho);
            final int qtdBytesArquivo = (int)arquivo.length();
            if (qtdBytesArquivo > 0) {
                leitor = new BufferedReader(new FileReader(arquivo));
                final char[] saida = new char[qtdBytesArquivo];
                leitor.read(saida);
                return new String(saida);
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
        finally {
            try {
                if (escritor != null) {
                    escritor.close();
                }
            }
            catch (IOException ex2) {}
        }
        return false;
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
        File novoArquivo = null;
        novoArquivo = new File(caminho);
        return novoArquivo.delete();
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
}
