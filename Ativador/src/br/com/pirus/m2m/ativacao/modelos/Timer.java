// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.modelos;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timer extends JPanel implements Runnable
{
    private static final long serialVersionUID = 1L;
    private JLabel horario;
    private long segundos;
    private boolean parar;
    
    public Timer() {
        this.parar = false;
        this.add(this.horario = new JLabel());
        this.horario.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
    
    public void setTempo(final long tempo) {
        if (tempo < 0L) {
            return;
        }
        this.transformarSegundos(this.segundos = tempo);
    }
    
    public void iniciarContagem() {
        this.parar = false;
        new Thread(this).start();
    }
    
    public void pararContagem() {
        this.parar = true;
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while (this.segundos >= 0L && !this.parar) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setTempo(this.segundos - 1L);
        }
    }
    
    private void transformarSegundos(final long tempo) {
        final String seg = (new StringBuilder().append(tempo / 60L).toString().length() < 2) ? ("0" + tempo / 60L) : new StringBuilder().append(tempo / 60L).toString();
        final String min = (new StringBuilder().append(tempo % 60L).toString().length() < 2) ? ("0" + tempo % 60L) : new StringBuilder().append(tempo % 60L).toString();
        this.horario.setText(String.valueOf(seg) + ":" + min);
    }
}
