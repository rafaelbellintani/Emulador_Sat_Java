// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.utils.Utils;
import br.com.pirus.m2m.ativacao.modelos.CFeTeste;
import br.com.pirus.m2m.ativacao.Configuracoes;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import br.com.pirus.m2m.ativacao.modelos.FormatTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class JanelaAtivacaoACSATSEFAZ extends JFrame implements InterfaceResultado
{
    private static final long serialVersionUID = 1L;
    private JComboBox comboOpcao;
    private JButton botaoAtivacao;
    private JButton botaoTeste;
    private FormatTextField txtCNPJ;
    private JLabel labelOpcao;
    private JLabel labelCNPJ;
    private JLabel labelSenha;
    private JLabel labelConfirmacaoSenha;
    private JLabel labelAlerta4;
    private JLabel labelAlerta1;
    private JLabel labelAlerta2;
    private JLabel labelAlerta3;
    private JLabel labelTitulo1;
    private JLabel labelTitulo2;
    private JLabel labelTitulo3;
    private JLabel labelStatus;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmacaoSenha;
    private JLabel labelIcone;
    private ImageIcon favicon;
    private ControleFuncoes cAtivacao;
    private JLabel tempoResposta;
    
    public JanelaAtivacaoACSATSEFAZ(final ControleFuncoes ca) {
        this.comboOpcao = null;
        this.cAtivacao = ca;
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.comboOpcao = new JComboBox();
        this.botaoAtivacao = new JButton();
        this.botaoTeste = new JButton();
        this.labelCNPJ = new JLabel();
        this.labelSenha = new JLabel();
        this.labelConfirmacaoSenha = new JLabel();
        this.txtCNPJ = new FormatTextField(6);
        this.txtSenha = new JPasswordField();
        this.txtConfirmacaoSenha = new JPasswordField();
        this.labelOpcao = new JLabel();
        this.labelAlerta1 = new JLabel();
        this.labelAlerta2 = new JLabel();
        this.labelAlerta3 = new JLabel();
        this.labelAlerta4 = new JLabel();
        this.labelIcone = new JLabel();
        this.labelTitulo1 = new JLabel();
        this.labelTitulo2 = new JLabel();
        this.labelTitulo3 = new JLabel();
        this.labelStatus = new JLabel();
        this.tempoResposta = new JLabel();
        this.favicon = new ImageIcon(this.getClass().getResource("/images/fazendasp.PNG"));
    }
    
    private void configurarComponentes() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(1);
        this.setTitle("ATIVA\u00c7\u00c3O SAT-CFe v2.2.5");
        this.setIconImage(this.favicon.getImage());
        this.setBackground(new Color(255, 255, 255));
        this.setMinimumSize(new Dimension(350, 500));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.labelIcone.setIcon(new ImageIcon(this.getClass().getResource("/images/notafiscalpaulista.PNG")));
        this.labelIcone.setBorder(BorderFactory.createEtchedBorder());
        this.labelIcone.setBounds(117, 10, 111, 75);
        this.labelTitulo1.setFont(new Font("Tahoma", 1, 14));
        this.labelTitulo1.setText("   SAT-FISCAL");
        this.labelTitulo1.setBounds(117, 90, 111, 17);
        this.labelTitulo2.setFont(new Font("Tahoma", 1, 12));
        this.labelTitulo2.setText("   Sistema Autenticador e Transmissor");
        this.labelTitulo2.setBounds(48, 110, 249, 17);
        this.labelTitulo3.setFont(new Font("Tahoma", 1, 12));
        this.labelTitulo3.setText("       de Cupom Fiscal Eletr\u00f4nico(CFe)");
        this.labelTitulo3.setBounds(48, 127, 249, 17);
        this.labelOpcao.setText("Informe o tipo de Certificado");
        this.labelOpcao.setFont(new Font("Tahoma", 1, 12));
        this.labelOpcao.setBounds(20, this.labelTitulo3.getY() + 20, 100, 15);
        this.comboOpcao.addItem("Tipo de Certificado = AC-SAT/SEFAZ");
        this.comboOpcao.addItem("Tipo de Certificado = ICP-BRASIL");
        this.comboOpcao.addItem("Renova\u00e7\u00e3o do Certificado ICP-BRASIL");
        this.comboOpcao.setBounds(20, this.labelOpcao.getY() + 20, 290, 20);
        this.comboOpcao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(null, JanelaAtivacaoACSATSEFAZ.this.comboOpcao.getItemAt(JanelaAtivacaoACSATSEFAZ.this.comboOpcao.getSelectedIndex()));
            }
        });
        this.tempoResposta.setFont(new Font("Tahoma", 1, 11));
        this.tempoResposta.setText("");
        this.tempoResposta.setBounds(280, 10, 70, 17);
        this.labelCNPJ.setFont(new Font("Tahoma", 1, 11));
        this.labelCNPJ.setText("CNPJ:");
        this.labelCNPJ.setBounds(20, this.comboOpcao.getY() + 25, 30, 15);
        this.txtCNPJ.setBounds(20, this.labelCNPJ.getY() + 15, 290, 20);
        this.labelSenha.setFont(new Font("Tahoma", 1, 11));
        this.labelSenha.setText("C\u00f3digo de ativa\u00e7\u00e3o do SAT:");
        this.labelSenha.setBounds(20, this.txtCNPJ.getY() + 25, 280, 15);
        this.txtSenha.setBounds(20, this.labelSenha.getY() + 15, 290, 20);
        this.labelConfirmacaoSenha.setFont(new Font("Tahoma", 1, 11));
        this.labelConfirmacaoSenha.setText("Confirma\u00e7\u00e3o do c\u00f3digo de ativa\u00e7\u00e3o do SAT:");
        this.labelConfirmacaoSenha.setBounds(20, this.txtSenha.getY() + 25, 280, 15);
        this.txtConfirmacaoSenha.setBounds(20, this.labelConfirmacaoSenha.getY() + 15, 290, 20);
        this.labelAlerta1.setFont(new Font("Tahoma", 1, 11));
        this.labelAlerta1.setText("Importante:");
        this.labelAlerta1.setBounds(20, this.txtConfirmacaoSenha.getY() + 25, 290, 15);
        this.labelAlerta2.setFont(new Font("Tahoma", 0, 11));
        this.labelAlerta2.setText("Em caso de esquecimento da senha, a mesma n\u00e3o poder\u00e1 ");
        this.labelAlerta2.setBounds(20, this.labelAlerta1.getY() + 15, 290, 15);
        this.labelAlerta3.setFont(new Font("Tahoma", 0, 11));
        this.labelAlerta3.setText("ser recuperada e o equipamento dever\u00e1 ser reinicializado ");
        this.labelAlerta3.setBounds(20, this.labelAlerta2.getY() + 15, 290, 15);
        this.labelAlerta4.setFont(new Font("Tahoma", 0, 11));
        this.labelAlerta4.setText("pelo fabricante.");
        this.labelAlerta4.setBounds(20, this.labelAlerta3.getY() + 15, 290, 15);
        this.botaoAtivacao.setText("<HTML>Clique aqui para ativar<HTML>");
        this.botaoAtivacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaAtivacaoACSATSEFAZ.this.botaoAtivarSat();
            }
        });
        this.botaoAtivacao.setBounds(37, this.labelAlerta4.getY() + 20, 110, 40);
        this.botaoTeste.setText("<html>Testar comunica\u00e7\u00e3o<html>");
        this.botaoTeste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaAtivacaoACSATSEFAZ.this.botaoTestarComunicacao();
            }
        });
        this.botaoTeste.setBounds(180, this.botaoAtivacao.getY(), 120, 40);
        this.botaoTeste.setEnabled(true);
        this.labelStatus.setFont(new Font("Tahoma", 1, 11));
        this.labelStatus.setText("");
        this.labelStatus.setBounds(90, this.botaoAtivacao.getY() + 40, 340, 17);
        this.pack();
    }
    
    private void adicionarComponentes() {
        this.getContentPane().add(this.labelOpcao);
        this.getContentPane().add(this.comboOpcao);
        this.getContentPane().add(this.botaoAtivacao);
        this.getContentPane().add(this.labelCNPJ);
        this.getContentPane().add(this.botaoTeste);
        this.getContentPane().add(this.labelSenha);
        this.getContentPane().add(this.labelStatus);
        this.getContentPane().add(this.labelConfirmacaoSenha);
        this.getContentPane().add(this.txtCNPJ);
        this.getContentPane().add(this.txtSenha);
        this.getContentPane().add(this.txtConfirmacaoSenha);
        this.getContentPane().add(this.labelAlerta1);
        this.getContentPane().add(this.labelAlerta2);
        this.getContentPane().add(this.labelAlerta3);
        this.getContentPane().add(this.labelAlerta4);
        this.getContentPane().add(this.labelIcone);
        this.getContentPane().add(this.labelTitulo1);
        this.getContentPane().add(this.labelTitulo2);
        this.getContentPane().add(this.labelTitulo3);
        this.getContentPane().add(this.tempoResposta);
    }
    
    private void botaoAtivarSat() {
        if (new String(this.txtCNPJ.getText()).replaceAll(" ", "").length() <= 4) {
            this.exibirAlerta("Campo \"CNPJ\" n\u00e3o foi preenchido!");
            return;
        }
        if (new String(this.txtSenha.getPassword()).length() <= 5) {
            this.exibirAlerta("Campo \"Confirma\u00e7\u00e3o do c\u00f3digo de ativa\u00e7\u00e3o do SAT\" deve conter mais de 6 d\u00edgitos!");
            return;
        }
        if (new String(this.txtConfirmacaoSenha.getPassword()).length() <= 5) {
            this.exibirAlerta("Campo \"C\u00f3digo de ativa\u00e7\u00e3o do SAT\" deve conter mais de 6 d\u00edgitos!");
            return;
        }
        if (!new String(this.txtSenha.getPassword()).equals(new String(this.txtConfirmacaoSenha.getPassword()))) {
            this.exibirAlerta("C\u00f3digo de ativa\u00e7\u00e3o e confirma\u00e7\u00e3o n\u00e3o s\u00e3o iguais!");
            return;
        }
        Configuracoes.SAT.codigoDeAtivacao = this.getSenha();
        Configuracoes.SAT.CNPJ = this.getCNPJ();
        this.ativarBotoes(false);
        final int subComando = this.comboOpcao.getSelectedIndex() + 1;
        this.cAtivacao.processarComandoAtivarACSATSEFAZ(this, this.getSenha(), this.getCNPJ(), subComando);
    }
    
    private void botaoTestarComunicacao() {
        final JanelaIE janela = new JanelaIE(this);
        janela.setVisible(true);
    }
    
    public void enviarTesteFimAFim(final boolean testeAtivacao) {
        this.ativarBotoes(false);
        Configuracoes.SAT.IE = Configuracoes.SAT.IE.replace(".", "").replace("-", "").replace("/", "");
        if (this.getCNPJ() == null || this.getCNPJ().length() != 14) {
            JOptionPane.showMessageDialog(null, "CNPJ inv\u00e1lido!");
            this.ativarBotoes(true);
            return;
        }
        if (this.getSenha() == null || this.getSenha().length() < 8) {
            JOptionPane.showMessageDialog(null, "Codigo de Ativa\u00e7\u00e3o inv\u00e1lido!");
            this.ativarBotoes(true);
            return;
        }
        if (Configuracoes.SAT.IE != null && Configuracoes.SAT.IE.length() == 12 && Configuracoes.SAT.signAC != null && Configuracoes.SAT.signAC.length() == 344 && Configuracoes.SAT.cnpjSoftwareHouse != null && Configuracoes.SAT.cnpjSoftwareHouse.length() == 14) {
            this.cAtivacao.processarComandoEmitirCFeTeste(this, new CFeTeste(this.getCNPJ()).getCupom(), this.getSenha());
        }
        else {
            JOptionPane.showMessageDialog(null, "Informa\u00e7\u00f5es Inv\u00e1lidas");
        }
        this.ativarBotoes(true);
    }
    
    @Override
    public void tratarResultado(final String retorno) {
        this.ativarBotoes(true);
        System.out.println(retorno);
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
                this.exibirAlerta("ATEN\u00c7\u00c3O!\nVoc\u00ea recebeu a seguinte mensagem da SEFAZ:\n" + partes[4]);
            }
            this.tempoResposta.setText(String.valueOf(this.cAtivacao.getTempoResposta()) + " ms");
            if (partes[1].equalsIgnoreCase("04006")) {
                new JanelaExibirCSR(partes[5]).setVisible(true);
            }
        }
    }
    
    private String getCNPJ() {
        return this.txtCNPJ.getText().trim().replace("-", "").replace("/", "").replace(".", "");
    }
    
    private String getSenha() {
        return new String(this.txtSenha.getPassword());
    }
    
    private void exibirAlerta(final String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }
    
    private void ativarBotoes(final boolean boleano) {
        this.botaoAtivacao.setEnabled(boleano);
        this.botaoTeste.setEnabled(boleano);
    }
}
