// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.interfaces;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import br.com.satcfe.satbl.controles.ControladorEmuladorOffLine;
import br.com.satcfe.satbl.controles.ConfiguracaoOffLineFacade;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.Image;
import java.util.List;
import br.com.satcfe.satbl.modelos.ConfiguracoesOffLine;
import javax.swing.JCheckBox;
import br.com.um.view.TextBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class JanelaEmuladorOffLine extends JFrame
{
    private boolean help;
    private static final long serialVersionUID = 1L;
    private JPanel jContentPane;
    private JButton botaoOk;
    private JButton botaoCancelar;
    private JLabel labelAtualizacao;
    private JComboBox comboAtualizacao;
    private JButton botaoAtualizar;
    private TextBox avisoUsuario;
    private JButton botaoAviso;
    private JLabel labelRenovarCert;
    private JLabel labelBloqueio;
    private JCheckBox checkRenovacaoAviso;
    private JCheckBox checkRenovacao;
    private JLabel labelTitulo;
    private JCheckBox checkBloqueioSefaz;
    private JCheckBox checkBloqueioContribuinte;
    private JCheckBox checkBloqueioDesativacao;
    private JCheckBox checkBloqueioAutonomo;
    private ConfiguracoesOffLine configuracoes;
    
    public JanelaEmuladorOffLine() {
        this.help = false;
        this.jContentPane = null;
        this.botaoOk = null;
        this.botaoCancelar = null;
        this.labelAtualizacao = null;
        this.comboAtualizacao = null;
        this.botaoAtualizar = null;
        this.avisoUsuario = null;
        this.botaoAviso = null;
        this.labelRenovarCert = null;
        this.labelBloqueio = null;
        this.checkRenovacaoAviso = null;
        this.checkRenovacao = null;
        this.labelTitulo = null;
        this.checkBloqueioSefaz = null;
        this.checkBloqueioContribuinte = null;
        this.checkBloqueioDesativacao = null;
        this.checkBloqueioAutonomo = null;
        this.configuracoes = null;
        this.configuracoes = ConfiguracoesOffLine.getInstance();
        this.initialize();
    }
    
    private void initialize() {
        this.setIconImages(this.getIcons());
        this.setSize(400, 415);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Op\u00e7\u00f5es Emulador Off-Line");
        this.setResizable(false);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
    }
    
    private List<Image> getIcons() {
        final List<Image> icons = new ArrayList<Image>();
        try {
            icons.add(new ImageIcon(this.getClass().getResource("/imagens/logo_sat_128.png")).getImage());
            icons.add(new ImageIcon(this.getClass().getResource("/imagens/logo_sat_32.png")).getImage());
            icons.add(new ImageIcon(this.getClass().getResource("/imagens/logo_sat_48.png")).getImage());
            icons.add(new ImageIcon(this.getClass().getResource("/imagens/logo_sat_64.png")).getImage());
            return icons;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.iniciarLabels();
            (this.jContentPane = new JPanel()).setLayout(null);
            this.jContentPane.add(this.getBotaoOk());
            this.jContentPane.add(this.getBotaoCancelar());
            this.jContentPane.add(this.labelAtualizacao);
            this.jContentPane.add(this.getComboAtualizacao());
            this.jContentPane.add(this.getBotaoAtualizar());
            this.jContentPane.add((Component)this.getAvisoUsuario());
            this.jContentPane.add(this.getBotaoAviso());
            this.jContentPane.add(this.labelRenovarCert);
            this.jContentPane.add(this.labelBloqueio);
            this.jContentPane.add(this.getCheckRenovacaoAviso());
            this.jContentPane.add(this.getCheckRenovacao());
            this.jContentPane.add(this.labelTitulo);
            this.jContentPane.add(this.getCheckBloqueioSefaz(), null);
            this.jContentPane.add(this.getCheckBloqueioContribuinte(), null);
            this.jContentPane.add(this.getCheckBloqueioDesativacao(), null);
            this.jContentPane.add(this.getCheckBloqueioAutonomo(), null);
        }
        return this.jContentPane;
    }
    
    private void iniciarLabels() {
        (this.labelAtualizacao = new JLabel()).setBounds(new Rectangle(20, 25, 200, 20));
        this.labelAtualizacao.setFont(new Font("Arial", 1, 12));
        this.labelAtualizacao.setText("Comando de Atualiza\u00e7\u00e3o");
        (this.labelRenovarCert = new JLabel()).setBounds(new Rectangle(20, 170, 250, 20));
        this.labelRenovarCert.setText("Renova\u00e7\u00e3o do Certificado ICP-BRASIL");
        this.labelRenovarCert.setFont(new Font("Arial", 1, 12));
        (this.labelTitulo = new JLabel()).setBounds(new Rectangle(5, 5, 385, 20));
        this.labelTitulo.setText("Op\u00e7\u00f5es Emulador Off-Line");
        this.labelTitulo.setFont(new Font("Arial", 1, 12));
        this.labelTitulo.setHorizontalAlignment(0);
        (this.labelBloqueio = new JLabel()).setBounds(new Rectangle(20, 240, 325, 15));
        this.labelBloqueio.setText("<html><center>Bloqueio / Desbloquio SAT-CFe</center></html>");
        this.labelBloqueio.setFont(new Font("Arial", 1, 12));
    }
    
    private JButton getBotaoOk() {
        if (this.botaoOk == null) {
            (this.botaoOk = new JButton()).setBounds(new Rectangle(20, 353, 100, 25));
            this.botaoOk.setText("Ok");
            this.botaoOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaEmuladorOffLine.this.completarConfiguracoes();
                    final ConfiguracaoOffLineFacade facade = new ConfiguracaoOffLineFacade();
                    facade.executar();
                    new ControladorEmuladorOffLine().salvarConfiguracoes();
                    JanelaEmuladorOffLine.this.dispose();
                }
            });
        }
        return this.botaoOk;
    }
    
    private void completarConfiguracoes() {
        final ConfiguracoesOffLine conf = ConfiguracoesOffLine.getInstance();
        conf.setHabilitarBloqueioAutonomo(this.checkBloqueioAutonomo.isSelected());
        conf.setHabilitarBloqueioSefaz(this.checkBloqueioSefaz.isSelected());
        conf.setHabilitarBloqueioContribuinte(this.checkBloqueioContribuinte.isSelected());
        conf.setHabilitarDesativacao(this.checkBloqueioDesativacao.isSelected());
        conf.setHabilitarRenovacao(this.checkRenovacao.isSelected());
        conf.setHabilitarRenovacaoAviso(this.checkRenovacaoAviso.isSelected());
    }
    
    private JButton getBotaoCancelar() {
        if (this.botaoCancelar == null) {
            (this.botaoCancelar = new JButton()).setBounds(280, 353, 100, 25);
            this.botaoCancelar.setText("Cancelar");
            this.botaoCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaEmuladorOffLine.this.dispose();
                }
            });
        }
        return this.botaoCancelar;
    }
    
    private JComboBox getComboAtualizacao() {
        if (this.comboAtualizacao == null) {
            (this.comboAtualizacao = new JComboBox()).setBounds(new Rectangle(20, 45, 360, 25));
            this.comboAtualizacao.addItem("ATUALIZA\u00c7\u00c3O PENDENTE \u2013 CONCLUS\u00c3O SUCESSO");
            this.comboAtualizacao.addItem("ATUALIZA\u00c7\u00c3O PENDENTE \u2013 CONCLUS\u00c3O FALHA");
            this.comboAtualizacao.addItem("ATUALIZA\u00c7\u00c3O FOR\u00c7ADA SEFAZ");
            this.comboAtualizacao.setFont(new Font("Arial", 0, 12));
        }
        return this.comboAtualizacao;
    }
    
    private JButton getBotaoAtualizar() {
        if (this.botaoAtualizar == null) {
            (this.botaoAtualizar = new JButton()).setBounds(new Rectangle(270, 71, 110, 25));
            this.botaoAtualizar.setText("Atualizar");
            this.botaoAtualizar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final int n = JanelaEmuladorOffLine.this.comboAtualizacao.getSelectedIndex();
                    final String msg = "Voc\u00ea confirma a " + JanelaEmuladorOffLine.this.comboAtualizacao.getSelectedItem() + " ?";
                    if (JOptionPane.showConfirmDialog(null, msg, "Atualiza\u00e7\u00e3o", 2, 2) == 0) {
                        final ConfiguracoesOffLine conf = ConfiguracoesOffLine.getInstance();
                        conf.setAtualizarSoftwareBasico(n + 1);
                        JanelaEmuladorOffLine.this.completarConfiguracoes();
                        final ConfiguracaoOffLineFacade facade = new ConfiguracaoOffLineFacade();
                        facade.executar();
                        new ControladorEmuladorOffLine().salvarConfiguracoes();
                        JanelaEmuladorOffLine.this.dispose();
                    }
                }
            });
        }
        return this.botaoAtualizar;
    }
    
    private TextBox getAvisoUsuario() {
        if (this.avisoUsuario == null) {
            (this.avisoUsuario = new TextBox()).setBounds(new Rectangle(20, 100, 361, 40));
            this.avisoUsuario.setLabel("Envio de aviso ao usu\u00e1rio");
        }
        return this.avisoUsuario;
    }
    
    private JButton getBotaoAviso() {
        if (this.botaoAviso == null) {
            (this.botaoAviso = new JButton()).setBounds(new Rectangle(270, 140, 110, 25));
            this.botaoAviso.setText("Enviar Aviso");
            this.botaoAviso.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final int r = JOptionPane.showConfirmDialog(JanelaEmuladorOffLine.this, "Voc\u00ea deseja enviar um aviso n\u00e3o catalogado ao usu\u00e1rio?", "Aviso ao usu\u00e1rio", 2, 2);
                    if (r == 0) {
                        if (JanelaEmuladorOffLine.this.avisoUsuario.getTexto().length() > 1) {
                            final ConfiguracoesOffLine conf = ConfiguracoesOffLine.getInstance();
                            conf.setAvisoUsuario(JanelaEmuladorOffLine.this.avisoUsuario.getTexto());
                            JanelaEmuladorOffLine.this.completarConfiguracoes();
                            final ConfiguracaoOffLineFacade facade = new ConfiguracaoOffLineFacade();
                            facade.executar();
                            new ControladorEmuladorOffLine().salvarConfiguracoes();
                            JanelaEmuladorOffLine.this.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Campo aviso usu\u00e1rio inv\u00e1lido", "Aviso Usu\u00e1rio", -1);
                        }
                    }
                }
            });
        }
        return this.botaoAviso;
    }
    
    private JCheckBox getCheckRenovacaoAviso() {
        if (this.checkRenovacaoAviso == null) {
            (this.checkRenovacaoAviso = new JCheckBox()).setBounds(new Rectangle(20, 190, 360, 20));
            this.checkRenovacaoAviso.setText("Habilitar aviso de vencimento de certificado ICP-BRASIL");
            this.checkRenovacaoAviso.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarRenovacaoAviso()) {
                this.checkRenovacaoAviso.setSelected(true);
            }
            this.checkRenovacaoAviso.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaEmuladorOffLine.this.checkRenovacao.setSelected(false);
                }
            });
        }
        return this.checkRenovacaoAviso;
    }
    
    private JCheckBox getCheckRenovacao() {
        if (this.checkRenovacao == null) {
            (this.checkRenovacao = new JCheckBox()).setBounds(new Rectangle(20, 213, 362, 21));
            this.checkRenovacao.setText("Permitir ao AC solicitar renova\u00e7\u00e3o do certificado ICP-BRASIL");
            this.checkRenovacao.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarRenovacao()) {
                this.checkRenovacao.setSelected(true);
            }
            this.checkRenovacao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaEmuladorOffLine.this.checkRenovacaoAviso.setSelected(false);
                }
            });
        }
        return this.checkRenovacao;
    }
    
    public void setHelp(final boolean help) {
        this.help = help;
    }
    
    private JButton getBotaoHelp(final JButton bot, final Rectangle location, final Component comp) {
        bot.setBorderPainted(false);
        bot.setContentAreaFilled(false);
        bot.setBounds(location);
        bot.setToolTipText("Ajuda");
        final ImageIcon img = new ImageIcon(this.getClass().getResource("/imagens/help.png"));
        bot.setIcon(img);
        bot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
            }
        });
        return bot;
    }
    
    private JCheckBox getCheckBloqueioSefaz() {
        if (this.checkBloqueioSefaz == null) {
            (this.checkBloqueioSefaz = new JCheckBox()).setBounds(new Rectangle(20, 255, 360, 20));
            this.checkBloqueioSefaz.setText("Habilitar Bloqueio Equipamento pela SEFAZ");
            this.checkBloqueioSefaz.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarBloqueioSefaz()) {
                this.checkBloqueioSefaz.setSelected(true);
            }
            this.checkBloqueioSefaz.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (JanelaEmuladorOffLine.this.checkBloqueioSefaz.isSelected()) {
                        JanelaEmuladorOffLine.this.checkBloqueioAutonomo.setSelected(false);
                        JanelaEmuladorOffLine.this.checkBloqueioContribuinte.setSelected(false);
                        JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setSelected(false);
                        JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setEnabled(false);
                    }
                }
            });
        }
        return this.checkBloqueioSefaz;
    }
    
    private JCheckBox getCheckBloqueioContribuinte() {
        if (this.checkBloqueioContribuinte == null) {
            (this.checkBloqueioContribuinte = new JCheckBox()).setBounds(new Rectangle(20, 280, 360, 20));
            this.checkBloqueioContribuinte.setText("Habilitar Bloqueio Equipamento pelo Contribuinte");
            this.checkBloqueioContribuinte.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarBloqueioContribuinte()) {
                this.checkBloqueioContribuinte.setSelected(true);
            }
            this.checkBloqueioContribuinte.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (JanelaEmuladorOffLine.this.checkBloqueioContribuinte.isSelected()) {
                        JanelaEmuladorOffLine.this.checkBloqueioAutonomo.setSelected(false);
                        JanelaEmuladorOffLine.this.checkBloqueioSefaz.setSelected(false);
                        JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setEnabled(true);
                    }
                    else {
                        JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setEnabled(false);
                        JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setSelected(false);
                    }
                }
            });
        }
        return this.checkBloqueioContribuinte;
    }
    
    private JCheckBox getCheckBloqueioDesativacao() {
        if (this.checkBloqueioDesativacao == null) {
            (this.checkBloqueioDesativacao = new JCheckBox()).setBounds(new Rectangle(45, 300, 300, 20));
            this.checkBloqueioDesativacao.setText("Desativa\u00e7\u00e3o");
            this.checkBloqueioDesativacao.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarBloqueioContribuinte()) {
                if (this.configuracoes.getHabilitarDesativacao()) {
                    this.checkBloqueioDesativacao.setSelected(true);
                }
            }
            else {
                this.checkBloqueioDesativacao.setSelected(false);
                this.checkBloqueioDesativacao.setEnabled(false);
            }
        }
        return this.checkBloqueioDesativacao;
    }
    
    private JCheckBox getCheckBloqueioAutonomo() {
        if (this.checkBloqueioAutonomo == null) {
            (this.checkBloqueioAutonomo = new JCheckBox()).setBounds(new Rectangle(20, 323, 357, 20));
            this.checkBloqueioAutonomo.setText("Habilitar Bloqueio Aut\u00f4nomo");
            this.checkBloqueioAutonomo.setFont(new Font("arial", 0, 12));
            if (this.configuracoes.getHabilitarBloqueioAutonomo()) {
                this.checkBloqueioAutonomo.setSelected(true);
            }
            this.checkBloqueioAutonomo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JanelaEmuladorOffLine.this.checkBloqueioSefaz.setSelected(false);
                    JanelaEmuladorOffLine.this.checkBloqueioContribuinte.setSelected(false);
                    JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setSelected(false);
                    JanelaEmuladorOffLine.this.checkBloqueioDesativacao.setEnabled(false);
                }
            });
        }
        return this.checkBloqueioAutonomo;
    }
}
