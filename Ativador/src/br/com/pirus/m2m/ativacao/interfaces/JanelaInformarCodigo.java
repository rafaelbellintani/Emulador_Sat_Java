// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.Configuracoes;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JanelaInformarCodigo extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JPanel jContentPane;
    private JPasswordField txtCodigoDeAtivacao;
    private JLabel labelCodigo;
    private JButton btSalvar;
    private JButton btCancelar;
    
    public JanelaInformarCodigo() {
        this.jContentPane = null;
        this.txtCodigoDeAtivacao = null;
        this.labelCodigo = null;
        this.btSalvar = null;
        this.btCancelar = null;
        this.initialize();
    }
    
    private void initialize() {
        this.setSize(300, 200);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Informar C\u00f3digo de Ativa\u00e7\u00e3o");
    }
    
    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            (this.labelCodigo = new JLabel()).setBounds(new Rectangle(25, 23, 200, 25));
            this.labelCodigo.setText("Digite o C\u00f3digo de Ativa\u00e7\u00e3o");
            (this.jContentPane = new JPanel()).setLayout(null);
            this.jContentPane.add(this.getTxtCodigoDeAtivacao(), null);
            this.jContentPane.add(this.labelCodigo, null);
            this.jContentPane.add(this.getBtSalvar(), null);
            this.jContentPane.add(this.getBtCancelar(), null);
        }
        return this.jContentPane;
    }
    
    private JPasswordField getTxtCodigoDeAtivacao() {
        if (this.txtCodigoDeAtivacao == null) {
            (this.txtCodigoDeAtivacao = new JPasswordField()).setBounds(new Rectangle(25, 50, 230, 25));
        }
        return this.txtCodigoDeAtivacao;
    }
    
    private JButton getBtSalvar() {
        if (this.btSalvar == null) {
            (this.btSalvar = new JButton()).setText("Salvar");
            this.btSalvar.setBounds(25, 115, 100, 30);
            this.btSalvar.addActionListener(this);
        }
        return this.btSalvar;
    }
    
    private JButton getBtCancelar() {
        if (this.btCancelar == null) {
            (this.btCancelar = new JButton()).setText("Cancelar");
            this.btCancelar.setBounds(155, 115, 100, 30);
            this.btCancelar.addActionListener(this);
        }
        return this.btCancelar;
    }
    
    @Override
    public void actionPerformed(final ActionEvent evt) {
        if (evt.getSource() == this.btSalvar) {
            final String codigo = this.txtCodigoDeAtivacao.getText();
            if (codigo == null || codigo.length() < 6) {
                JOptionPane.showMessageDialog(null, "Erro: C\u00f3digo de Ativa\u00e7\u00e3o Inv\u00e1lido", "C\u00f3digo de Ativa\u00e7\u00e3o", 0);
            }
            else {
                JOptionPane.showMessageDialog(null, "C\u00f3digo Informado Com Sucesso", "C\u00f3digo de Ativa\u00e7\u00e3o", 1);
                Configuracoes.SAT.codigoDeAtivacao = codigo;
                this.dispose();
            }
        }
        else if (evt.getSource() == this.btCancelar) {
            this.dispose();
        }
    }
}
