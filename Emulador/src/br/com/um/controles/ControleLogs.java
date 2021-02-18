// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.util.Date;
import java.util.Calendar;
import java.io.OutputStream;
import br.com.um.interfaces.ILogger;
import java.util.ArrayList;

public class ControleLogs
{
    private String caminhoDebug;
    private ArrayList<ILogger> loggers;
    private static ControleLogs instance;
    
    static {
        ControleLogs.instance = null;
    }
    
    public static ControleLogs getInstance() {
        if (ControleLogs.instance == null) {
            ControleLogs.instance = new ControleLogs();
        }
        return ControleLogs.instance;
    }
    
    private ControleLogs() {
        this.caminhoDebug = null;
        this.loggers = new ArrayList<ILogger>();
    }
    
    public void addLogger(final ILogger log) {
        if (this.loggers != null) {
            this.loggers.add(log);
        }
    }
    
    public void setCaminhoDebug(final String caminhoDebug) {
        this.caminhoDebug = caminhoDebug;
    }
    
    public static synchronized void LOGAR(final int conteudo) {
        LOGAR(new StringBuilder().append(conteudo).toString());
    }
    
    public static synchronized void LOGAR(Object conteudo) {
        try {
            final OutputStream saida = System.out;
            conteudo = "[" + makeNewTrack() + "]:" + conteudo + "\n";
            saida.write(conteudo.toString().getBytes());
            saida.flush();
            final ControleLogs log = getInstance();
            for (int i = 0; i < log.loggers.size(); ++i) {
                log.loggers.get(i).logar(conteudo);
            }
        }
        catch (Exception e) {
            System.out.println("falha ao Gravar os Logs..");
            e.printStackTrace();
        }
    }
    
    private static String makeNewTrack() {
        final Calendar cal = Calendar.getInstance();
        final Date date = new Date();
        cal.setTime(date);
        final int ano = cal.get(1);
        final int mes = cal.get(2) + 1;
        final int dia = cal.get(5);
        final int hora = cal.get(11);
        final int minuto = cal.get(12);
        final int segundo = cal.get(13);
        final int mili = cal.get(14);
        final StringBuffer track = new StringBuffer();
        track.append(format(ano)).append("-");
        track.append(format(mes)).append("-");
        track.append(format(dia)).append(" ");
        track.append(format(hora)).append(":");
        track.append(format(minuto)).append(":");
        track.append(format(segundo)).append(".");
        track.append(ControleDados.formatarDouble(new StringBuilder().append(mili).toString(), 3, 3, 0));
        return track.toString();
    }
    
    private static String format(final int n) {
        if (n < 10) {
            return "0" + String.valueOf(n);
        }
        return String.valueOf(n);
    }
    
    public static void debugar(final String nome, final String conteudo) {
        final String path = getInstance().caminhoDebug;
        if (path != null) {
            ControleArquivos.escreverCaracteresArquivo(String.valueOf(path) + nome, conteudo.toCharArray());
        }
    }
}
