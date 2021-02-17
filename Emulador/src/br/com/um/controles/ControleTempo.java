// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import br.com.um.interfaces.NTPAdapter;

public class ControleTempo
{
    private static long diferenca;
    private NTPAdapter ntp;
    
    static {
        ControleTempo.diferenca = 0L;
    }
    
    @Deprecated
    public ControleTempo(final long time) {
        this.ntp = null;
        ControleTempo.diferenca = time - System.currentTimeMillis();
    }
    
    @Deprecated
    public ControleTempo() {
        this(System.currentTimeMillis());
    }
    
    public static void atualizarTempo() {
        atualizarTempo(System.currentTimeMillis());
    }
    
    public static void atualizarTempo(final long time) {
        ControleTempo.diferenca = time - System.currentTimeMillis();
    }
    
    private static String format(final int n) {
        if (n < 10) {
            return "0" + String.valueOf(n);
        }
        return String.valueOf(n);
    }
    
    public static String getTrack() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCurrentTime());
        final int ano = cal.get(1);
        final int mes = cal.get(2) + 1;
        final int dia = cal.get(5);
        final int hora = cal.get(11);
        final int min = cal.get(12);
        final int seg = cal.get(13);
        final int mili = cal.get(14);
        final StringBuffer track = new StringBuffer();
        track.append(format(ano)).append("-");
        track.append(format(mes)).append("-");
        track.append(format(dia)).append(" ");
        track.append(format(hora)).append(":");
        track.append(format(min)).append(":");
        track.append(format(seg)).append(".");
        track.append(ControleDados.formatarDouble(new StringBuilder().append(mili).toString(), 3, 3, 0));
        return track.toString();
    }
    
    public static String getDateTime(final String formato) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCurrentTime());
        String mes = String.valueOf(cal.get(2) + 1);
        String dia = String.valueOf(cal.get(5));
        final String ano = String.valueOf(cal.get(1));
        dia = ControleDados.formatarDouble(dia, 2, 2, 0);
        mes = ControleDados.formatarDouble(mes, 2, 2, 0);
        String horas = String.valueOf(cal.get(11));
        String min = String.valueOf(cal.get(12));
        String seg = String.valueOf(cal.get(13));
        horas = ControleDados.formatarDouble(horas, 2, 2, 0);
        min = ControleDados.formatarDouble(min, 2, 2, 0);
        seg = ControleDados.formatarDouble(seg, 2, 2, 0);
        if (formato.equalsIgnoreCase("date")) {
            return String.valueOf(ano) + "-" + mes + "-" + dia;
        }
        if (formato.equalsIgnoreCase("dateTime")) {
            return String.valueOf(ano) + "-" + mes + "-" + dia + "T" + horas + ":" + min + ":" + seg;
        }
        return String.valueOf(cal.getTimeInMillis());
    }
    
    public static String getData(final boolean invertido) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCurrentTime());
        String mes = String.valueOf(cal.get(2) + 1);
        String dia = String.valueOf(cal.get(5));
        final String ano = String.valueOf(cal.get(1));
        dia = ControleDados.formatarDouble(dia, 2, 2, 0);
        mes = ControleDados.formatarDouble(mes, 2, 2, 0);
        if (invertido) {
            return String.valueOf(ano) + mes + dia;
        }
        return String.valueOf(dia) + mes + ano;
    }
    
    private static String getHorario() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCurrentTime());
        String horas = String.valueOf(cal.get(11));
        String min = String.valueOf(cal.get(12));
        String seg = String.valueOf(cal.get(13));
        horas = ControleDados.formatarDouble(horas, 2, 2, 0);
        min = ControleDados.formatarDouble(min, 2, 2, 0);
        seg = ControleDados.formatarDouble(seg, 2, 2, 0);
        return String.valueOf(horas) + min + seg;
    }
    
    public static long getCurrentTime() {
        return System.currentTimeMillis() + ControleTempo.diferenca;
    }
    
    public static String getTimeStamp() {
        return String.valueOf(getData(true)) + getHorario();
    }
    
    public static long parseTimeStamp(final String timeStamp) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(timeStamp).getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    public static Date parseDateTime(final String dateTime) {
        try {
            DateFormat dateFormat;
            if (dateTime.length() > 11) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            }
            else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            }
            return dateFormat.parse(dateTime);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void setNtp(final NTPAdapter ntp) {
        this.ntp = ntp;
    }
    
    public void atualizarNTP() {
        if (this.ntp == null) {
            System.out.println("erro ao atualizar o ntp");
        }
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                final Long time = ControleTempo.this.ntp.consultaNTP();
                if (time != null) {
                    ControleTempo.access$1(time - System.currentTimeMillis());
                    ControleTempo.this.ntp.setResultado("Data e Hora Sincronizada: " + new Date(ControleTempo.getCurrentTime()));
                }
                else {
                    ControleTempo.access$1(0L);
                    ControleTempo.this.ntp.setResultado("Erro ao Sincronizar o Relogio");
                }
            }
        };
        new Thread(run).start();
    }
    
    public boolean atualizarNTPSinc() {
        if (this.ntp == null) {
            System.out.println("Erro ao atualizar o ntp");
            return false;
        }
        final Long time = this.ntp.consultaNTP();
        if (time != null) {
            ControleTempo.diferenca = time - System.currentTimeMillis();
            return true;
        }
        ControleTempo.diferenca = 0L;
        return false;
    }
    
    public static String formatarData(final String data) {
        if (data.contains("T")) {
            return String.valueOf(data.substring(8, 10)) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4) + " " + data.substring(11);
        }
        return String.valueOf(data.substring(6, 10)) + "-" + data.substring(3, 5) + "-" + data.substring(0, 2) + "T" + data.substring(11);
    }
    
    static /* synthetic */ void access$1(final long diferenca) {
        ControleTempo.diferenca = diferenca;
    }
}
