// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import br.com.pirus.m2m.ativacao.controles.ControleArquivos;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.awt.Insets;
import javax.swing.ListModel;
import br.com.pirus.m2m.ativacao.utils.Utils;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JanelaCertificadoICPBRASIL<E> extends JFrame implements InterfaceResultado
{
    private static final long serialVersionUID = -8943109528010696590L;
    StringBuffer cadeiaCertificado;
    private JanelaLoading loading;
    private JButton botaoEnvio;
    private JLabel labelSenha;
    private JLabel labelTitulo;
    private JLabel labelSubTitulo;
    private JLabel labelStatus;
    private JPasswordField campoSenha;
    private JLabel labelIcone;
    private ImageIcon favicon;
    private JScrollPane scrollPane;
    private JList lista;
    private DefaultListModel<String> listaModel;
    private JLabel labelPah;
    private JLabel labelAddCert;
    private JButton btnAdicionarCertificado;
    private JLabel labelAlerta;
    
    public JanelaCertificadoICPBRASIL() {
        this.cadeiaCertificado = null;
        this.loading = null;
        this.cadeiaCertificado = new StringBuffer();
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.registraEnterNoBotao(this.botaoEnvio = new JButton());
        this.labelSenha = new JLabel();
        this.campoSenha = new JPasswordField();
        this.labelIcone = new JLabel();
        this.labelTitulo = new JLabel();
        this.labelSubTitulo = new JLabel();
        this.labelStatus = new JLabel();
        this.favicon = new ImageIcon(this.getClass().getResource("/images/fazendasp.PNG"));
    }
    
    private void configurarComponentes() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(1);
        this.setTitle("ATIVA\u00c7\u00c3O SAT-CFe v2.2.5");
        this.setIconImage(this.favicon.getImage());
        this.setBackground(new Color(255, 255, 255));
        this.setSize(new Dimension(438, 520));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.labelIcone.setIcon(new ImageIcon(this.getClass().getResource("/images/notafiscalpaulista.PNG")));
        this.labelIcone.setBorder(BorderFactory.createEtchedBorder());
        this.labelIcone.setBounds(163, 10, 111, 75);
        this.labelTitulo.setFont(new Font("Tahoma", 1, 14));
        this.labelTitulo.setText("   SAT-FISCAL");
        this.labelTitulo.setBounds(163, 90, 111, 17);
        this.labelSubTitulo.setFont(new Font("Tahoma", 1, 12));
        this.labelSubTitulo.setText("<html><center>Sistema Autenticador e Transmissor de Cupom Fiscal Eletr\u00f4nico(CFe)</center></html>");
        this.labelSubTitulo.setBounds(94, 110, 249, 49);
        this.labelSubTitulo.setHorizontalTextPosition(0);
        this.labelSenha.setFont(new Font("Tahoma", 1, 11));
        this.labelSenha.setText("C\u00f3digo de ativa\u00e7\u00e3o do SAT:");
        this.labelSenha.setBounds(20, 216, 280, 14);
        this.campoSenha.setBounds(20, 231, 391, 20);
        this.botaoEnvio.setText("<html><center>Enviar certificado</center></html>");
        this.botaoEnvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JanelaCertificadoICPBRASIL.this.botaoEnviarCertificado();
            }
        });
        this.botaoEnvio.setBounds(163, 417, 110, 40);
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
        this.getContentPane().add(this.labelSenha);
        this.getContentPane().add(this.labelStatus);
        this.getContentPane().add(this.campoSenha);
        this.getContentPane().add(this.labelIcone);
        this.getContentPane().add(this.labelTitulo);
        this.getContentPane().add(this.labelSubTitulo);
        this.getContentPane().add(this.getScrollPane());
        this.getContentPane().add(this.getTextField());
        this.getContentPane().add(this.getLabelAddCert());
        this.getContentPane().add(this.getBtnAdicionarCertificado());
        (this.labelAlerta = new JLabel("<html>ATEN\u00c7\u00c3O: voc\u00ea deve inserir al\u00e9m do certificado do equipamento SAT, toda a cadeia de certifica\u00e7\u00e3o da autoridade certificadora onde o certificado foi gerado.</html>")).setFont(new Font("Tahoma", 1, 12));
        this.labelAlerta.setBounds(20, 160, 391, 49);
        this.labelAlerta.setForeground(Color.red);
        this.getContentPane().add(this.labelAlerta);
    }
    
    private void botaoEnviarCertificado() {
        if (new String(this.campoSenha.getPassword()).length() <= 5) {
            this.exibirAlerta("Campo \"Confirma\u00e7\u00e3o do c\u00f3digo de ativa\u00e7\u00e3o do SAT\" deve conter mais de 6 d\u00edgitos!");
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
    
    private String getCertificado() {
        return this.cadeiaCertificado.toString();
    }
    
    private String getSenha() {
        return new String(this.campoSenha.getPassword());
    }
    
    private void exibirAlerta(final String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }
    
    private void ativarBotoes(final boolean boleano) {
        this.botaoEnvio.setEnabled(boleano);
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.listaModel = new DefaultListModel<String>();
            (this.lista = new JList((ListModel<E>)this.listaModel)).setSelectionMode(1);
            this.lista.setLayoutOrientation(2);
            this.lista.setVisibleRowCount(-1);
            final JScrollPane listScroller = new JScrollPane(this.lista);
            listScroller.setPreferredSize(new Dimension(250, 80));
            (this.scrollPane = new JScrollPane(this.lista)).setBounds(20, 306, 391, 100);
        }
        return this.scrollPane;
    }
    
    private JLabel getTextField() {
        if (this.labelPah == null) {
            (this.labelPah = new JLabel()).setBorder(BorderFactory.createLineBorder(Color.black, 1));
            this.labelPah.setBounds(20, 276, 253, 20);
        }
        return this.labelPah;
    }
    
    private JLabel getLabelAddCert() {
        if (this.labelAddCert == null) {
            (this.labelAddCert = new JLabel()).setText("Adicionar um Certificado :");
            this.labelAddCert.setFont(new Font("Tahoma", 1, 11));
            this.labelAddCert.setBounds(20, 262, 280, 14);
        }
        return this.labelAddCert;
    }
    
    private JButton getBtnAdicionarCertificado() {
        if (this.btnAdicionarCertificado == null) {
            (this.btnAdicionarCertificado = new JButton("Adicionar Certificado")).setBounds(280, 272, 131, 25);
            this.btnAdicionarCertificado.setMargin(new Insets(0, 0, 0, 0));
            this.btnAdicionarCertificado.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaCertificadoICPBRASIL.this.carregarCertificado();
                }
            });
        }
        return this.btnAdicionarCertificado;
    }
    
    private void adicionarCertificado(final String nome) {
        this.listaModel.addElement(nome);
    }
    
    private void carregarCertificado() {
        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Certificado X.509 (PEM)", new String[] { "crt", "cer" });
        chooser.setFileFilter(filter);
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal == 0) {
            final String caminho = chooser.getSelectedFile().getAbsolutePath();
            this.labelPah.setText(chooser.getSelectedFile().getPath());
            final String cert = ControleArquivos.lerBytesArquivo(caminho);
            this.cadeiaCertificado.append(cert);
            final String xName = this.parseCertificado(cert);
            this.adicionarCertificado(xName);
        }
    }
    
    private String parseCertificado(final String cert) {
        try {
            final InputStream io = new ByteArrayInputStream(cert.getBytes());
            final BufferedInputStream bis = new BufferedInputStream(io);
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate c = null;
            if (bis.available() > 0) {
                c = cf.generateCertificate(bis);
                bis.close();
            }
            return ((X509Certificate)c).getSubjectDN().getName();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
