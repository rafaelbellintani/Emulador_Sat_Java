// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import java.awt.Font;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;

public class JanelaLoading extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JLabel img;
    private JLabel aviso;
    private JPanel panel;
    
    public JanelaLoading() {
        this.img = null;
        this.aviso = null;
        this.panel = null;
        this.setTitle("Processando..");
        this.inicializar();
    }
    
    public JanelaLoading(final String titulo) {
        this.img = null;
        this.aviso = null;
        this.panel = null;
        this.setTitle(titulo);
        this.inicializar();
    }
    
    private void inicializar() {
        this.setSize(300, 180);
        this.setResizable(false);
        this.setContentPane(this.getJContentPane());
        this.setDefaultCloseOperation(0);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private JPanel getJContentPane() {
        if (this.panel == null) {
            (this.panel = new JPanel()).setLayout(null);
            this.panel.setBackground(Color.WHITE);
        }
        return this.panel;
    }
    
    private void iniciarComponentes() {
        this.aviso = new JLabel();
        this.img = new JLabel();
    }
    
    private void adicionarComponentes() {
        this.panel.add(this.img);
        this.panel.add(this.aviso);
    }
    
    private void configurarComponentes() {
        this.img.setBounds(15, this.getHeight() / 2 - 40, 50, 50);
        this.img.setIcon(new ImageIcon(this.getClass().getResource("/images/loading.gif")));
        this.img.setHorizontalAlignment(0);
        this.img.setVerticalAlignment(0);
        this.img.setVerticalAlignment(0);
        this.aviso.setBounds(this.img.getX() + this.img.getWidth() + 20, 25, this.getWidth() - (this.img.getX() + this.img.getWidth() + 20), this.getHeight() - 80);
        this.aviso.setFont(new Font("Arial", 1, 12));
    }
    
    public void setAviso(final String aviso) {
        this.aviso.setText(aviso);
    }
}
