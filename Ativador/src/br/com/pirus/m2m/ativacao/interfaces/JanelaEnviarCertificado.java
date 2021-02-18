// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.utils.Utils;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.controles.ControleArquivos;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.awt.Component;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import javax.swing.JFrame;

public class JanelaEnviarCertificado extends JFrame implements InterfaceResultado
{
    private static final long serialVersionUID = -8943109528010696590L;
    private ControleFuncoes cFuncoes;
    private JanelaLoading loading;
    private JButton botaoEnvio;
    private JButton botaoCarregarCertificado;
    private JLabel labelSenha;
    private JLabel labelCertificado;
    private JLabel labelTitulo;
    private JLabel labelTitulo2;
    private JLabel labelStatus;
    private JPasswordField campoSenha;
    private JTextField campoCertificado;
    private JLabel labelIcone;
    private ImageIcon favicon;
    
    public JanelaEnviarCertificado() {
        this.loading = null;
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.botaoEnvio = new JButton();
        this.botaoCarregarCertificado = new JButton();
        this.registraEnterNoBotao(this.botaoEnvio);
        this.registraEnterNoBotao(this.botaoCarregarCertificado);
        this.labelSenha = new JLabel();
        this.labelCertificado = new JLabel();
        this.campoSenha = new JPasswordField();
        this.campoCertificado = new JTextField();
        this.labelIcone = new JLabel();
        this.labelTitulo = new JLabel();
        this.labelTitulo2 = new JLabel();
        this.labelStatus = new JLabel();
        this.favicon = new ImageIcon(this.getClass().getResource("/images/fazendasp.PNG"));
    }
    
    private void configurarComponentes() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(1);
        this.setTitle("ATIVA\u00c7\u00c3O SAT-CFe v2.2.5");
        this.setIconImage(this.favicon.getImage());
        this.setBackground(new Color(255, 255, 255));
        this.setMinimumSize(new Dimension(345, 520));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.labelIcone.setIcon(new ImageIcon(this.getClass().getResource("/images/notafiscalpaulista.PNG")));
        this.labelIcone.setBorder(BorderFactory.createEtchedBorder());
        this.labelIcone.setBounds(120, 10, 111, 75);
        this.labelTitulo.setFont(new Font("Tahoma", 1, 14));
        this.labelTitulo.setText("   SAT-FISCAL");
        this.labelTitulo.setBounds(120, 90, 111, 17);
        this.labelTitulo2.setFont(new Font("Tahoma", 1, 12));
        this.labelTitulo2.setText("<html><center>Sistema Autenticador e Transmissor de Cupom Fiscal Eletr\u00f4nico(CFe)</center></html>");
        this.labelTitulo2.setBounds(51, 110, 249, 49);
        this.labelTitulo2.setHorizontalTextPosition(0);
        this.labelSenha.setFont(new Font("Tahoma", 1, 11));
        this.labelSenha.setText("C\u00f3digo de ativa\u00e7\u00e3o do SAT:");
        this.labelSenha.setBounds(20, 170, 280, 14);
        this.campoSenha.setBounds(20, 185, 290, 20);
        this.labelCertificado.setFont(new Font("Tahoma", 1, 11));
        this.labelCertificado.setText("Caminho certificado:");
        this.labelCertificado.setBounds(20, 225, 280, 14);
        this.campoCertificado.setBounds(20, 240, 290, 20);
        this.campoCertificado.setEnabled(false);
        if(System.getProperty("os.name").equals("Windows")) {
        	this.campoCertificado.setText("C:\\");
        }else if(System.getProperty("os.name").equals("Linux")) {
        	this.campoCertificado.setText("\\");
        }
        this.botaoEnvio.setText("<HTML>Enviar certificado<HTML>");
        this.botaoEnvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaEnviarCertificado.this.botaoEnviarCertificado();
            }
        });
        this.botaoEnvio.setBounds(37, 280, 110, 40);
        this.botaoCarregarCertificado.setText("<html>Carregar certificado<html>");
        this.botaoCarregarCertificado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaEnviarCertificado.this.botaoCarregarCertificado();
            }
        });
        this.botaoCarregarCertificado.setBounds(180, 280, 120, 40);
        this.botaoCarregarCertificado.setEnabled(true);
        this.labelStatus.setFont(new Font("Tahoma", 1, 11));
        this.labelStatus.setText("");
        this.labelStatus.setBounds(90, 440, 340, 17);
    }
    
    private void registraEnterNoBotao(final JButton b) {
        b.registerKeyboardAction(b.getActionForKeyStroke(KeyStroke.getKeyStroke(32, 0, false)), KeyStroke.getKeyStroke(10, 0, false), 0);
        b.registerKeyboardAction(b.getActionForKeyStroke(KeyStroke.getKeyStroke(32, 0, true)), KeyStroke.getKeyStroke(10, 0, true), 0);
    }
    
    private void adicionarComponentes() {
        this.getContentPane().add(this.botaoEnvio);
        this.getContentPane().add(this.botaoCarregarCertificado);
        this.getContentPane().add(this.labelSenha);
        this.getContentPane().add(this.labelCertificado);
        this.getContentPane().add(this.labelStatus);
        this.getContentPane().add(this.campoSenha);
        this.getContentPane().add(this.campoCertificado);
        this.getContentPane().add(this.labelIcone);
        this.getContentPane().add(this.labelTitulo);
        this.getContentPane().add(this.labelTitulo2);
    }
    
    private void botaoEnviarCertificado() {
        if (new String(this.campoSenha.getPassword()).length() <= 5) {
            this.exibirAlerta("Campo \"Confirma\u00e7\u00e3o do c\u00f3digo de ativa\u00e7\u00e3o do SAT\" deve conter mais de 6 d\u00edgitos!");
            return;
        }
        if (!this.campoCertificado.getText().endsWith(".crt")) {
            this.exibirAlerta("O campo certificado deve ser preenchido!");
            return;
        }
        this.ativarBotoes(false);
        try {
            new ControleFuncoes().processarComunicarCertificadoICPBRASIL(this, this.getSenha(), this.getCertificado());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.ativarBotoes(true);
    }
    
    private void botaoCarregarCertificado() {
        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Certificado X.509 (PEM)", new String[] { "crt", "crt" });
        chooser.setFileFilter(filter);
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal == 0) {
            final String caminho = chooser.getSelectedFile().getAbsolutePath();
            if (caminho.endsWith(".crt")) {
                this.campoCertificado.setText(caminho);
            }
            else {
                this.exibirAlerta("O certificado deve estar no formato \"Certificado X.509 (PEM)\" e possuir a extens\u00e3o \".crt\"");
            }
        }
    }
    
    private String getSenha() {
        return new String(this.campoSenha.getPassword());
    }
    
    private String getCertificado() {
        return ControleArquivos.lerBytesArquivo(this.campoCertificado.getText());
    }
    
    private void exibirAlerta(final String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }
    
    private void ativarBotoes(final boolean boleano) {
        this.botaoEnvio.setEnabled(boleano);
        this.botaoCarregarCertificado.setEnabled(boleano);
    }
    
    @Override
    public void tratarResultado(final String retorno) {
        if (this.loading != null && this.loading.isShowing()) {
            this.loading.dispose();
        }
        this.ativarBotoes(true);
        if (retorno.indexOf("|") == -1) {
            if (retorno.length() == 0) {
                this.exibirAlerta("Timeout.");
            }
            else {
                this.exibirAlerta(retorno);
            }
        }
        else {
            final String[] partes = Utils.quebrarString(retorno, "|");
            this.exibirAlerta("Resultado = " + partes[2]);
            if (partes[4].length() > 0) {
                this.exibirAlerta("ATEN\u00c7\u00c3O!\nVoc\u00ea recebeu a seguinte mensagem da SEFAZ:\n" + partes[3]);
            }
        }
    }
}
