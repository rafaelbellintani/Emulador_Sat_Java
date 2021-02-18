// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class JanelaExibirCSR extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel labelTitulo;
    private JScrollPane txtScrollPane;
    private String csr;
    private JButton btOk;
    
    public JanelaExibirCSR(final String csr) {
        this.contentPane = null;
        this.labelTitulo = null;
        this.txtScrollPane = null;
        this.csr = null;
        this.btOk = null;
        this.csr = csr;
        this.initialize();
    }
    
    private void initialize() {
        this.setSize(500, 400);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Ativa\u00e7\u00e3o SAT-CFe v2.2.5");
        this.setDefaultCloseOperation(1);
        this.setBackground(new Color(255, 255, 255));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
    }
    
    private JPanel getJContentPane() {
        if (this.contentPane == null) {
            (this.labelTitulo = new JLabel()).setFont(new Font("Verdana", 1, 14));
            this.labelTitulo.setHorizontalAlignment(0);
            this.labelTitulo.setBounds(new Rectangle(3, 5, 487, 43));
            this.labelTitulo.setText("Copie CSR (Certificate Sign Request) gerado pelo SAT");
            (this.contentPane = new JPanel()).setLayout(null);
            this.contentPane.add(this.labelTitulo, null);
            this.contentPane.add(this.getTextArea(), null);
            this.contentPane.add(this.getJButton(), null);
        }
        return this.contentPane;
    }
    
    private JScrollPane getTextArea() {
        if (this.txtScrollPane == null) {
            final JTextArea txtCSR = new JTextArea(this.csr);
            txtCSR.setFont(new Font("Courier New", 0, 10));
            txtCSR.setLineWrap(true);
            txtCSR.setWrapStyleWord(true);
            (this.txtScrollPane = new JScrollPane(txtCSR)).setBounds(new Rectangle(25, 60, 445, 215));
        }
        return this.txtScrollPane;
    }
    
    private JButton getJButton() {
        if (this.btOk == null) {
            (this.btOk = new JButton()).setText("Ok");
            this.btOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaExibirCSR.this.fechar();
                }
            });
            this.btOk.setBounds(new Rectangle(350, 320, 100, 30));
        }
        return this.btOk;
    }
    
    private void fechar() {
        this.setVisible(false);
        this.dispose();
    }
}
