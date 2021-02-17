// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.interfaces;

import java.awt.Font;
import br.com.satcfe.satbl.controles.ControladorDesativacao;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import br.com.satcfe.satbl.Configuracoes;
import javax.swing.BorderFactory;
import java.awt.Toolkit;
import java.awt.Container;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JFrame;

public class TelaPrincipal extends JFrame
{
    private JMenuBar menuPrincipal;
    private JMenu menuOpcoes;
    private JMenuItem menuEmuladorOff;
    private JMenuItem menuDesativacao;
    private SimpleAttributeSet styleRed;
    private SimpleAttributeSet stylePlain;
    private static final long serialVersionUID = 1L;
    private JTextPane area;
    private JScrollPane scroll;
    private JPanel panel;
    private JPanel panel_1;
    private JLabel labelInfo;
    
    public TelaPrincipal() {
        this.menuPrincipal = null;
        this.menuOpcoes = null;
        this.menuEmuladorOff = null;
        this.menuDesativacao = null;
        this.styleRed = null;
        this.stylePlain = null;
        this.panel = null;
        this.iniciarComponentes();
        this.configurarComponentes();
        this.adicionarComponentes();
    }
    
    private void iniciarComponentes() {
        this.setIconImages(this.getIcons());
        this.panel_1 = new JPanel();
        (this.area = new JTextPane()).setLocation(-413, 0);
        final JPanel noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.add(this.area);
        this.scroll = new JScrollPane(noWrapPanel, 20, 31);
        this.styleRed = new SimpleAttributeSet();
        StyleConstants.setForeground(this.stylePlain = new SimpleAttributeSet(), Color.BLACK);
        StyleConstants.setFontFamily(this.stylePlain, "Dialog");
        StyleConstants.setForeground(this.styleRed, Color.RED);
        StyleConstants.setFontFamily(this.styleRed, "Dialog");
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
    
    private void configurarComponentes() {
        this.setContentPane(this.panel_1);
        this.panel_1.setBackground(Color.GRAY);
        this.setDefaultCloseOperation(3);
        this.setTitle("S@T-CFe Sistema Autenticador e Transmissor de Cupom Fiscal Eletr\u00f4nico - v0.1");
        this.setSize(660, 400);
        this.setResizable(false);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.getContentPane().setLayout(null);
        this.area.setEditable(false);
        this.scroll.setBounds(30, 25, 600, 320);
        this.scroll.setBorder(BorderFactory.createBevelBorder(0));
        if (Configuracoes.SAT.emuladorOffLine) {
            this.setJMenuBar(this.getMenuPrincipal());
        }
    }
    
    @Override
    public void repaint() {
        super.repaint();
        if (Configuracoes.SAT.emuladorOffLine) {
            this.setJMenuBar(this.getMenuPrincipal());
        }
        this.getJMenuBar().repaint();
        this.requestFocus();
    }
    
    public void append(final String t) {
        try {
            final String p1 = t.substring(0, t.indexOf("]") + 1);
            final String p2 = t.substring(t.indexOf("]") + 1);
            if (t.toLowerCase().indexOf("erro") >= 0) {
                this.area.getStyledDocument().insertString(this.area.getDocument().getLength(), p1, this.stylePlain);
                this.area.getStyledDocument().insertString(this.area.getDocument().getLength(), p2, this.styleRed);
            }
            else {
                this.area.getStyledDocument().insertString(this.area.getDocument().getLength(), t, this.stylePlain);
            }
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
        final JScrollBar bar = this.scroll.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    }
    
    private void adicionarComponentes() {
        this.getContentPane().add(this.scroll);
        if ("0.1".toLowerCase().indexOf("alpha") >= 0) {
            this.panel_1.add(this.getLabelInfoTesteAlpha());
        }
    }
    
    private JMenuBar getMenuPrincipal() {
        if (this.menuPrincipal == null) {
            (this.menuPrincipal = new JMenuBar()).add(this.getMenuOpcoes());
        }
        return this.menuPrincipal;
    }
    
    private JMenu getMenuOpcoes() {
        if (this.menuOpcoes == null) {
            (this.menuOpcoes = new JMenu()).setText("Op\u00e7\u00f5es");
            this.menuOpcoes.add(this.getMenuEmuladorOff());
            this.menuOpcoes.add(this.getMenuDesativacao());
        }
        return this.menuOpcoes;
    }
    
    public JMenuItem getMenuEmuladorOff() {
        if (this.menuEmuladorOff == null) {
            (this.menuEmuladorOff = new JMenuItem()).setText("Op\u00e7\u00f5es Emulador Off-Line");
            this.menuEmuladorOff.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    new JanelaEmuladorOffLine().setVisible(true);
                }
            });
        }
        return this.menuEmuladorOff;
    }
    
    public JMenuItem getMenuDesativacao() {
        if (this.menuDesativacao == null) {
            (this.menuDesativacao = new JMenuItem()).setText("Desativar SAT-CFe");
            this.menuDesativacao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final String msg = "Voc\u00ea deseja realmente Desativar o emulador SAT-CFe?";
                    final int r = JOptionPane.showConfirmDialog(null, msg, "Desativar SAT", 2, 3);
                    if (r == 0) {
                        final String s = new ControladorDesativacao().botaoDesativarSAT();
                        JOptionPane.showMessageDialog(null, s, "Desativar SAT", 1);
                    }
                }
            });
        }
        return this.menuDesativacao;
    }
    
    private JLabel getLabelInfoTesteAlpha() {
        if (this.labelInfo == null) {
            (this.labelInfo = new JLabel("Teste ALPHA")).setBounds(530, 0, 100, 25);
            this.labelInfo.setHorizontalAlignment(4);
            this.labelInfo.setVerticalAlignment(0);
            this.labelInfo.setFont(new Font("Courier New", 1, 12));
            this.labelInfo.setForeground(Color.red);
        }
        return this.labelInfo;
    }
}
