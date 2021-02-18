// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.utils;

import br.com.pirus.m2m.ativacao.controles.ControleArquivos;
import java.util.Random;
import java.util.Vector;

public class Utils
{
    public static String preencheCom(String linha_a_preencher, final String letra, final int tamanho, final int direcao) {
        if (linha_a_preencher == null || linha_a_preencher.trim() == "") {
            linha_a_preencher = "";
        }
        while (linha_a_preencher.contains(" ")) {
            linha_a_preencher = linha_a_preencher.replaceAll(" ", " ").trim();
        }
        linha_a_preencher = linha_a_preencher.replaceAll("[./-]", "");
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
    
    public static String[] quebrarString(final String conteudo, final String delimitador) {
        String cont = conteudo;
        final Vector<String> linhas = new Vector<String>();
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
    
    public static String gerarSessao() {
        final Random r = new Random();
        String sessao = preencheCom(new StringBuilder(String.valueOf(r.nextInt(1000000))).toString(), "0", 6, 1);
        
        if(System.getProperty("os.name").equals("Windows")) {
        if (!ControleArquivos.existeArquivo("C:/ATIVACAO/SESSAO/sessao.txt")) {
            ControleArquivos.criarDiretorio("C:/ATIVACAO/SESSAO");
            ControleArquivos.escreverCaracteresArquivo("C:/ATIVACAO/SESSAO/sessao.txt", sessao.toCharArray());
        }
        else {
            for (String conteudo = ControleArquivos.lerBytesArquivo("C:/ATIVACAO/SESSAO/sessao.txt"); conteudo.indexOf(sessao) != -1; sessao = preencheCom(new StringBuilder(String.valueOf(r.nextInt(1000000))).toString(), "0", 6, 1)) {}
            ControleArquivos.escreverCaracteresArquivo("C:/ATIVACAO/SESSAO/sessao.txt", ("\r\n" + sessao).toCharArray());
        	}
        }
        
        else if(System.getProperty("os.name").equals("Linux")){
        	if (!ControleArquivos.existeArquivo("/SAT/ATIVACAO/SESSAO/sessao.txt")) {
                ControleArquivos.criarDiretorio("/SAT/ATIVACAO/SESSAO");
                ControleArquivos.escreverCaracteresArquivo("/SAT/ATIVACAO/SESSAO/sessao.txt", sessao.toCharArray());
            }
            else {
                for (String conteudo = ControleArquivos.lerBytesArquivo("/SAT/ATIVACAO/SESSAO/sessao.txt"); conteudo.indexOf(sessao) != -1; sessao = preencheCom(new StringBuilder(String.valueOf(r.nextInt(1000000))).toString(), "0", 6, 1)) {}
                ControleArquivos.escreverCaracteresArquivo("/SAT/ATIVACAO/SESSAO/sessao.txt", ("\r\n" + sessao).toCharArray());
            	}
        }
        return sessao;
    }
    
    public static String tratarResultadoPing(final String retorno) {
        if (retorno.indexOf("|") != -1) {
            return quebrarString(retorno, "|")[2];
        }
        return retorno;
    }
}
