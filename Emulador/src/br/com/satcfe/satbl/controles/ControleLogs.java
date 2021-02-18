// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.io.File;
import java.io.UnsupportedEncodingException;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleTempo;
import java.io.OutputStream;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.interfaces.TelaPrincipal;

public class ControleLogs
{
    public static TelaPrincipal tp;
    
    static {
        ControleLogs.tp = null;
    }
    
    public static synchronized void logar(final Object conteudo) {
        logar(conteudo, Configuracoes.Gerais.saidaPadrao);
    }
    
    public static void logar(Object conteudo, final OutputStream saida) {
        try {
            conteudo = ControleArquivos.trocaAcentuacao(conteudo.toString());
            final String s = "[" + ControleTempo.getTrack() + "]:" + conteudo + "\n";
            saida.write(s.getBytes());
            saida.flush();
            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS && ControleLogs.tp != null && ControleLogs.tp.isVisible()) {
                ControleLogs.tp.append(s.toString());
            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX && ControleLogs.tp != null && ControleLogs.tp.isVisible()) {
            	ControleLogs.tp.append(s.toString());
            }
            gravarLog(conteudo.toString());
        }
        catch (Exception e) {
            System.out.println("falha ao Gravar os Logs..");
            e.printStackTrace();
        }
    }
    
    public static synchronized void gravarLog(final String conteudo) {
        try {
            final StringBuffer sb = new StringBuffer();
            sb.append(ControleTempo.getTimeStamp()).append("|SAT|info|").append(conteudo).append("\n");
            ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, sb.toString().toCharArray(), true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void gravarArquivoLogs(final Object dados) {
        try {
            final String caminho = Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS;
            ControleArquivos.escreverCaracteresArquivoUTF8(caminho, dados.toString().toCharArray(), true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getArquivoLogs() {
        try {
            String log = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS);
            log = StringUtil.base64Encode(log.getBytes("UTF-8"));
            return log;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void controlarAquivoLogs() {
        final String caminho = Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS;
        final File file = new File(caminho);
        final long tam = file.length();
        if (tam > 410000L) {
            String string = ControleArquivos.lerCaracteresArquivo(caminho);
            string = string.substring((int)(tam - 205000L));
            ControleArquivos.escreverCaracteresArquivo(caminho, string.toCharArray());
        }
    }
    
    public static void debugar(final String nome, final String dados) {
        final String endDebug = Configuracoes.SistemaArquivos.DIRETORIO_DEBUG;
        if (ControleArquivos.existeArquivo(endDebug)) {
            ControleArquivos.escreverCaracteresArquivoUTF8(String.valueOf(endDebug) + nome, dados.toCharArray(), false);
        }
    }
}
