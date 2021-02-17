// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import org.apache.commons.net.ntp.TimeInfo;
import java.net.InetAddress;
import org.apache.commons.net.ntp.NTPUDPClient;
import java.rmi.UnknownHostException;
import br.com.um.interfaces.NTPAdapter;

public final class ControleNTP implements NTPAdapter
{
    private String host;
    private int porta;
    
    public ControleNTP(final String host, final int porta) {
        this.host = null;
        this.porta = 123;
        this.host = host;
        this.porta = porta;
    }
    
    public Long consultaNTP() {
        return this.consultaNTP(this.host, this.porta);
    }
    
    public Long consultaNTP(final String host, final int porta) {
        long timeStamp = 0L;
        try {
            if (host == null) {
                throw new UnknownHostException("Endere\u00e7o NTP invalido!!");
            }
            final NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(5000);
            client.open();
            final InetAddress hostAddr = InetAddress.getByName(host);
            final TimeInfo info = client.getTime(hostAddr, porta);
            timeStamp = info.getMessage().getReceiveTimeStamp().getTime();
            client.close();
            return timeStamp;
        }
        catch (Exception e) {
            e.printStackTrace();
            ControleLogs.logar("Erro NTP: " + e.getMessage());
            return null;
        }
    }
    
    public void setPorta(final int porta) {
        this.porta = porta;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public void setResultado(final String msg) {
        ControleLogs.logar(msg);
    }
}
