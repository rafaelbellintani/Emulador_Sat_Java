// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import java.awt.Dialog;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.Configuracoes;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JLabel;
import br.com.pirus.m2m.ativacao.controles.ControleBloqueio;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JanelaComandos extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 6339727816707169172L;
    private JMenuItem itemConfigurar;
    private JMenu menuConfiguracoes;
    private JMenuBar menuPrincipal;
    private JButton btAtivarACSATSEFAZ;
    private JButton btComunicarCertificado;
    private JButton btAssociarAssinatura;
    private JButton btBloquearSAT;
    private JButton btDesbloquearSAT;
    private JButton btOutrasFuncoes;
    private ImageIcon favicon;
    private ControleFuncoes cFuncoes;
    private ControleBloqueio cBloqueio;
    private JLabel labelIcone;
    
    public JanelaComandos(final ControleFuncoes cComandos) {
        this.cFuncoes = null;
        this.cBloqueio = null;
        this.cFuncoes = cComandos;
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.btAtivarACSATSEFAZ = new JButton();
        this.btComunicarCertificado = new JButton();
        this.btAssociarAssinatura = new JButton();
        this.btBloquearSAT = new JButton();
        this.btDesbloquearSAT = new JButton();
        this.btOutrasFuncoes = new JButton();
        this.labelIcone = new JLabel();
        this.favicon = new ImageIcon(this.getClass().getResource("/images/fazendasp.PNG"));
    }
    
    private void configurarComponentes() {
        final int padding = 40;
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(3);
        this.setIconImage(this.favicon.getImage());
        this.setTitle("COMANDOS ATIVA\u00c7\u00c3O SAT-CFe v2.2.5");
        this.setSize(350, 420);
        this.setResizable(false);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.getContentPane().setLayout(null);
        this.labelIcone.setIcon(new ImageIcon(this.getClass().getResource("/images/notafiscalpaulista.PNG")));
        this.labelIcone.setBorder(BorderFactory.createEtchedBorder());
        this.labelIcone.setBounds(117, 10, 111, 75);
        this.btAtivarACSATSEFAZ.setText("<HTML>Ativar SAT");
        this.btAtivarACSATSEFAZ.addActionListener(this);
        this.btAtivarACSATSEFAZ.setBounds(60, 110, 230, 30);
        this.btComunicarCertificado.setText("<HTML>Comunicar Certificado");
        this.btComunicarCertificado.addActionListener(this);
        this.btComunicarCertificado.setBounds(60, this.btAtivarACSATSEFAZ.getY() + padding, 230, 30);
        this.btAssociarAssinatura.setText("<HTML>Associar Assinatura</HTML>");
        this.btAssociarAssinatura.addActionListener(this);
        this.btAssociarAssinatura.setBounds(60, this.btComunicarCertificado.getY() + padding, 230, 30);
        this.btBloquearSAT.setText("<HTML>Bloquear SAT");
        this.btBloquearSAT.addActionListener(this);
        this.btBloquearSAT.setBounds(60, this.btAssociarAssinatura.getY() + padding, 230, 30);
        this.btDesbloquearSAT.setText("<HTML>Desbloquear SAT");
        this.btDesbloquearSAT.addActionListener(this);
        this.btDesbloquearSAT.setBounds(60, this.btBloquearSAT.getY() + padding, 230, 30);
        this.btOutrasFuncoes.setText("<HTML>Outras Fun\u00e7\u00f5es do SAT");
        this.btOutrasFuncoes.addActionListener(this);
        this.btOutrasFuncoes.setBounds(60, this.btDesbloquearSAT.getY() + padding, 230, 30);
    }
    
    private void adicionarComponentes() {
        this.setJMenuBar(this.getMenuPrincipal());
        this.getContentPane().add(this.btAtivarACSATSEFAZ);
        this.getContentPane().add(this.btComunicarCertificado);
        this.getContentPane().add(this.btAssociarAssinatura);
        this.getContentPane().add(this.btBloquearSAT);
        this.getContentPane().add(this.btDesbloquearSAT);
        this.getContentPane().add(this.btOutrasFuncoes);
        this.getContentPane().add(this.labelIcone);
    }
    
    public JMenuBar getMenuPrincipal() {
        if (this.menuPrincipal == null) {
            (this.menuPrincipal = new JMenuBar()).add(this.getJMenuConfigurar());
        }
        return this.menuPrincipal;
    }
    
    private JMenu getJMenuConfigurar() {
        if (this.menuConfiguracoes == null) {
            (this.menuConfiguracoes = new JMenu()).setText("Configurar");
            this.menuConfiguracoes.add(this.getConfigurar());
        }
        return this.menuConfiguracoes;
    }
    
    public JMenuItem getConfigurar() {
        if (this.itemConfigurar == null) {
            (this.itemConfigurar = new JMenuItem()).setText("Informar C\u00f3digo De Ativa\u00e7\u00e3o");
            this.itemConfigurar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    new JanelaInformarCodigo().setVisible(true);
                }
            });
        }
        return this.itemConfigurar;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btAtivarACSATSEFAZ) {
            final JanelaAtivacaoACSATSEFAZ ativarACSATSEFAZ = new JanelaAtivacaoACSATSEFAZ(this.cFuncoes);
            ativarACSATSEFAZ.setVisible(true);
            return;
        }
        if (e.getSource() == this.btComunicarCertificado) {
            final JanelaCertificadoICPBRASIL janela = new JanelaCertificadoICPBRASIL();
            janela.setVisible(true);
            return;
        }
        if (Configuracoes.SAT.codigoDeAtivacao == null || Configuracoes.SAT.codigoDeAtivacao.length() < 6) {
            JOptionPane.showMessageDialog(null, "C\u00f3digo de Ativa\u00e7\u00e3o n\u00e3o informado");
            return;
        }
        if (e.getSource() == this.btAssociarAssinatura) {
            final JanelaAssocinarAssinatura associarAssinatura = new JanelaAssocinarAssinatura(this.cFuncoes);
            associarAssinatura.setVisible(true);
        }
        else if (e.getSource() == this.btBloquearSAT) {
            if (this.cBloqueio == null) {
                this.cBloqueio = new ControleBloqueio(this.cFuncoes);
            }
            this.cBloqueio.iniciarBloqueioSAT();
        }
        else if (e.getSource() == this.btDesbloquearSAT) {
            if (this.cBloqueio == null) {
                this.cBloqueio = new ControleBloqueio(this.cFuncoes);
            }
            this.cBloqueio.iniciarDesbloqueioSAT();
        }
        else if (e.getSource() == this.btOutrasFuncoes) {
            final JanelaOutrosComandos janela2 = new JanelaOutrosComandos();
            janela2.setVisible(true);
            janela2.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        }
    }
}
