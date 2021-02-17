// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Container;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JanelaTrocarCodigoAtivacao extends JFrame implements ActionListener, InterfaceResultado
{
    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JLabel labelTitulo;
    private JButton btAlterar;
    private JPasswordField txtCodigo;
    private JPasswordField txtNovoCodigo;
    private JPasswordField txtConfCodigo;
    private JLabel labelCodigo;
    private JLabel labelNovoCodigo;
    private JLabel labelConfCodigo;
    private JButton btCancelar;
    private JEditorPane jEditorPane;
    
    public JanelaTrocarCodigoAtivacao() {
        this.panel = null;
        this.labelTitulo = null;
        this.btAlterar = null;
        this.txtCodigo = null;
        this.txtNovoCodigo = null;
        this.txtConfCodigo = null;
        this.labelCodigo = null;
        this.labelNovoCodigo = null;
        this.labelConfCodigo = null;
        this.btCancelar = null;
        this.jEditorPane = null;
        this.initialize();
    }
    
    private void initialize() {
        this.setSize(300, 300);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Trocar C\u00f3digo de Ativa\u00e7\u00e3o");
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
    }
    
    private JPanel getJContentPane() {
        if (this.panel == null) {
            this.iniciarLabels();
            (this.panel = new JPanel()).setLayout(null);
            this.panel.setBackground(Color.white);
            this.panel.add(this.labelTitulo, null);
            this.panel.add(this.labelCodigo, null);
            this.panel.add(this.labelNovoCodigo, null);
            this.panel.add(this.labelConfCodigo, null);
            this.panel.add(this.getTxtCodigo(), null);
            this.panel.add(this.getTxtNovoCodigo(), null);
            this.panel.add(this.getTxtConfCodigo(), null);
            this.panel.add(this.getBtCancelar(), null);
            this.panel.add(this.getBtAlterar(), null);
        }
        return this.panel;
    }
    
    private void iniciarLabels() {
        (this.labelTitulo = new JLabel()).setBounds(new Rectangle(10, 5, this.getWidth() - 35, 30));
        this.labelTitulo.setHorizontalAlignment(0);
        this.labelTitulo.setText("Trocar C\u00f3digo de Ativa\u00e7\u00e3o");
        (this.labelCodigo = new JLabel()).setBounds(new Rectangle(20, 40, 150, 15));
        this.labelCodigo.setText("C\u00f3digo de Ativa\u00e7\u00e3o");
        (this.labelNovoCodigo = new JLabel()).setBounds(new Rectangle(20, 90, 200, 15));
        this.labelNovoCodigo.setText("Novo C\u00f3digo de Ativa\u00e7\u00e3o");
        (this.labelConfCodigo = new JLabel()).setBounds(new Rectangle(20, 140, 200, 15));
        this.labelConfCodigo.setText("Confirma\u00e7\u00e3o C\u00f3digo de Ativa\u00e7\u00e3o");
    }
    
    private JButton getBtAlterar() {
        if (this.btAlterar == null) {
            (this.btAlterar = new JButton()).setText("Alterar");
            this.btAlterar.addActionListener(this);
            this.btAlterar.setBounds(new Rectangle(180, this.btCancelar.getY(), 85, 30));
        }
        return this.btAlterar;
    }
    
    private JPasswordField getTxtCodigo() {
        if (this.txtCodigo == null) {
            (this.txtCodigo = new JPasswordField()).setBounds(new Rectangle(20, this.labelCodigo.getY() + 15, 245, 25));
        }
        return this.txtCodigo;
    }
    
    private JPasswordField getTxtNovoCodigo() {
        if (this.txtNovoCodigo == null) {
            (this.txtNovoCodigo = new JPasswordField()).setBounds(new Rectangle(20, this.labelNovoCodigo.getY() + 15, 245, 25));
        }
        return this.txtNovoCodigo;
    }
    
    private JPasswordField getTxtConfCodigo() {
        if (this.txtConfCodigo == null) {
            (this.txtConfCodigo = new JPasswordField()).setBounds(new Rectangle(20, this.labelConfCodigo.getY() + 15, 245, 25));
        }
        return this.txtConfCodigo;
    }
    
    private JButton getBtCancelar() {
        if (this.btCancelar == null) {
            (this.btCancelar = new JButton()).setText("Cancelar");
            this.btCancelar.addActionListener(this);
            this.btCancelar.setBounds(new Rectangle(20, this.txtConfCodigo.getY() + 40, 85, 30));
        }
        return this.btCancelar;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btAlterar) {
            final String opcao = "1";
            final String codigoDeAtivacao = new String(this.txtCodigo.getPassword());
            final String novoCodigo = new String(this.txtNovoCodigo.getPassword());
            final String confCodigo = new String(this.txtConfCodigo.getPassword());
            new ControleFuncoes().processarComandoTrocarCodigoAtivacao(this, codigoDeAtivacao, opcao, novoCodigo, confCodigo);
        }
        else {
            e.getSource();
        }
    }
    
    private JEditorPane getJEditorPane() {
        if (this.jEditorPane == null) {
            this.jEditorPane = new JEditorPane();
        }
        return this.jEditorPane;
    }
    
    @Override
    public void tratarResultado(final String retorno) {
        System.out.println(retorno);
    }
}
