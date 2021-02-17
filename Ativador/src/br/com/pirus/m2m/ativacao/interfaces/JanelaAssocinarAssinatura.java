// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.utils.Utils;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.Configuracoes;
import java.awt.Component;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JanelaAssocinarAssinatura extends JFrame implements InterfaceResultado
{
    private static final long serialVersionUID = -8943109528010696590L;
    private JButton botaoEnvio;
    private JLabel labelAssinatura;
    private JLabel labelTitulo;
    private JLabel labelTitulo2;
    private JLabel labelTitulo3;
    private JLabel labelStatus;
    private JTextField campoAssinatura;
    private JLabel labelIcone;
    private ImageIcon favicon;
    private ControleFuncoes cAtivacao;
    private JFormattedTextField campoCNPJEmit;
    private JLabel labelCNPJEmit;
    private JLabel labelCNPJSH;
    private JFormattedTextField campoCNPJSH;
    private JLabel tempoResposta;
    
    public JanelaAssocinarAssinatura(final ControleFuncoes ca) {
        this.cAtivacao = ca;
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.registraEnterNoBotao(this.botaoEnvio = new JButton());
        this.labelAssinatura = new JLabel();
        try {
            final MaskFormatter formatadorCNPJ = new MaskFormatter("##.###.###/####-##");
            this.campoCNPJEmit = new JFormattedTextField(formatadorCNPJ);
            this.campoCNPJSH = new JFormattedTextField(formatadorCNPJ);
        }
        catch (ParseException ex) {}
        this.campoAssinatura = new JTextField();
        this.labelIcone = new JLabel();
        this.labelTitulo = new JLabel();
        this.labelTitulo2 = new JLabel();
        this.labelTitulo3 = new JLabel();
        this.labelStatus = new JLabel();
        this.labelCNPJEmit = new JLabel();
        this.labelCNPJSH = new JLabel();
        this.tempoResposta = new JLabel();
        this.favicon = new ImageIcon(this.getClass().getResource("/images/fazendasp.PNG"));
    }
    
    private void configurarComponentes() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(1);
        this.setTitle("ATIVA\u00c7\u00c3O SAT-CFe v0.1");
        this.setIconImage(this.favicon.getImage());
        this.setBackground(new Color(255, 255, 255));
        this.setMinimumSize(new Dimension(350, 400));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.tempoResposta.setFont(new Font("Tahoma", 1, 11));
        this.tempoResposta.setText("");
        this.tempoResposta.setBounds(280, 10, 70, 17);
        this.labelIcone.setIcon(new ImageIcon(this.getClass().getResource("/images/notafiscalpaulista.PNG")));
        this.labelIcone.setBorder(BorderFactory.createEtchedBorder());
        this.labelIcone.setBounds(117, 10, 111, 75);
        this.labelTitulo.setFont(new Font("Tahoma", 1, 14));
        this.labelTitulo.setText("   SAT-FISCAL");
        this.labelTitulo.setBounds(117, 90, 111, 17);
        this.labelTitulo2.setFont(new Font("Tahoma", 1, 12));
        this.labelTitulo2.setText("   Sistema Autenticador e Transmissor");
        this.labelTitulo2.setBounds(48, 110, 249, 17);
        this.labelTitulo3.setFont(new Font("Tahoma", 1, 12));
        this.labelTitulo3.setText("       de Cupom Fiscal Eletr\u00f4nico(CFe)");
        this.labelTitulo3.setBounds(48, 127, 249, 17);
        this.labelCNPJEmit.setFont(new Font("Tahoma", 1, 11));
        this.labelCNPJEmit.setText("CNPJ Contribuinte");
        this.labelCNPJEmit.setBounds(20, 160, 340, 17);
        this.campoCNPJEmit.setBounds(20, 175, 290, 20);
        this.labelCNPJSH.setFont(new Font("Tahoma", 1, 11));
        this.labelCNPJSH.setText("CNPJ Software House");
        this.labelCNPJSH.setBounds(20, 200, 340, 17);
        this.campoCNPJSH.setBounds(20, 215, 290, 20);
        this.labelAssinatura.setFont(new Font("Tahoma", 1, 11));
        this.labelAssinatura.setText("Assinatura AC:");
        this.labelAssinatura.setBounds(20, 240, 280, 14);
        this.campoAssinatura.setBounds(20, 255, 290, 20);
        this.botaoEnvio.setText("<HTML>Enviar assinatura<HTML>");
        this.botaoEnvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaAssocinarAssinatura.this.botaoEnviarCertificado();
            }
        });
        this.botaoEnvio.setBounds(20, 285, 110, 40);
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
        this.getContentPane().add(this.labelCNPJEmit);
        this.getContentPane().add(this.labelCNPJSH);
        this.getContentPane().add(this.campoCNPJEmit);
        this.getContentPane().add(this.campoCNPJSH);
        this.getContentPane().add(this.labelAssinatura);
        this.getContentPane().add(this.labelStatus);
        this.getContentPane().add(this.campoAssinatura);
        this.getContentPane().add(this.labelIcone);
        this.getContentPane().add(this.labelTitulo);
        this.getContentPane().add(this.labelTitulo2);
        this.getContentPane().add(this.labelTitulo3);
        this.getContentPane().add(this.tempoResposta);
    }
    
    private void botaoEnviarCertificado() {
        if (new String(this.campoCNPJEmit.getText()).replaceAll(" ", "").length() <= 4) {
            this.exibirAlerta("Campo \"CNPJ do Contribuinte\" n\u00e3o foi preenchido!");
            return;
        }
        if (new String(this.campoCNPJSH.getText()).replaceAll(" ", "").length() <= 4) {
            this.exibirAlerta("Campo \"CNPJ da Software House\" n\u00e3o foi preenchido!");
            return;
        }
        if (Configuracoes.SAT.codigoDeAtivacao == null || Configuracoes.SAT.codigoDeAtivacao.length() < 6) {
            JOptionPane.showMessageDialog(null, "C\u00f3digo de Ativa\u00e7\u00e3o n\u00e3o informado");
            return;
        }
        this.ativarBotoes(false);
        this.cAtivacao.processarComandoAssocinarAssinatura(this, this.campoAssinatura.getText(), String.valueOf(this.getCNPJSH().replaceAll("-", "").replaceAll("/", "").replaceAll("\\.", "")) + this.getCNPJEmit().replaceAll("-", "").replaceAll("/", "").replaceAll("\\.", ""), Configuracoes.SAT.codigoDeAtivacao);
    }
    
    @Override
    public void tratarResultado(final String retorno) {
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
            this.tempoResposta.setText(String.valueOf(this.cAtivacao.getTempoResposta()) + " ms");
        }
    }
    
    private void exibirAlerta(final String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }
    
    private void ativarBotoes(final boolean boleano) {
        this.botaoEnvio.setEnabled(boleano);
    }
    
    private String getCNPJEmit() {
        return this.campoCNPJEmit.getText();
    }
    
    private String getCNPJSH() {
        return this.campoCNPJSH.getText();
    }
}
