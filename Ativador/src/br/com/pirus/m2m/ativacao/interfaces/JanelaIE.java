// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import br.com.pirus.m2m.ativacao.Configuracoes;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Container;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import br.com.pirus.m2m.ativacao.modelos.FormatTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JanelaIE extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JPanel jContentPane;
    private final String IE_DEFAULT = "000000000000";
    private final String SIGNAC_DEFAULT = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private final String CNPJ_DEFAULT = "00000000000000";
    private JLabel labelIE;
    private JLabel labelSignAC;
    private JLabel labelCnpj;
    private JLabel labelAtivacao;
    private FormatTextField textoIE;
    private JTextField textoSignAC;
    private FormatTextField textoCnpj;
    private JButton btOk;
    private JButton btCancelar;
    private JComboBox comboTesteAtivacao;
    private JanelaAtivacaoACSATSEFAZ jAtivacao;
    private boolean testeAtivacao;
    
    public JanelaIE(final JanelaAtivacaoACSATSEFAZ jAtivacao) {
        this.jContentPane = null;
        this.labelIE = null;
        this.labelSignAC = null;
        this.labelCnpj = null;
        this.labelAtivacao = null;
        this.textoIE = null;
        this.textoSignAC = null;
        this.textoCnpj = null;
        this.btOk = null;
        this.btCancelar = null;
        this.comboTesteAtivacao = null;
        this.jAtivacao = null;
        this.testeAtivacao = true;
        this.jAtivacao = jAtivacao;
        this.initialize();
    }
    
    private void initialize() {
        this.setSize(319, 350);
        this.setContentPane(this.getJContentPane());
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setTitle("Teste Fim-a-Fim");
    }
    
    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            (this.labelIE = new JLabel()).setBounds(new Rectangle(30, 100, 240, 20));
            this.labelIE.setText("Informe o 'IE' com 12 posi\u00e7\u00f5es");
            (this.labelAtivacao = new JLabel()).setBounds(new Rectangle(30, 30, 240, 20));
            this.labelAtivacao.setText("Ativa\u00e7\u00e3o:");
            (this.labelSignAC = new JLabel()).setBounds(new Rectangle(30, 160, 240, 20));
            this.labelSignAC.setText("Assinatura do AC:");
            (this.labelCnpj = new JLabel()).setBounds(new Rectangle(30, 215, 240, 20));
            this.labelCnpj.setText("CNPJ Software House:");
            (this.jContentPane = new JPanel()).setLayout(null);
            this.jContentPane.add(this.labelIE, null);
            this.jContentPane.add(this.labelAtivacao, null);
            this.jContentPane.add(this.labelSignAC, null);
            this.jContentPane.add(this.labelCnpj, null);
            this.jContentPane.add(this.getTextoIE(), null);
            this.jContentPane.add(this.getTextoSignAC(), null);
            this.jContentPane.add(this.getTextoCNPJ(), null);
            this.jContentPane.add(this.getComboTesteAtivacao(), null);
            this.jContentPane.add(this.getBtOk(), null);
            this.jContentPane.add(this.getBtCancelar(), null);
        }
        return this.jContentPane;
    }
    
    private JTextField getTextoIE() {
        if (this.textoIE == null) {
            (this.textoIE = new FormatTextField(3)).setBounds(new Rectangle(30, 120, 240, 25));
            this.textoIE.setBackground(Color.LIGHT_GRAY);
            if (Configuracoes.SAT.IE != null && !Configuracoes.SAT.IE.equals("000000000000")) {
                this.textoIE.setText(Configuracoes.SAT.IE);
            }
            else {
                this.textoIE.setEnabled(false);
            }
        }
        return this.textoIE;
    }
    
    private JTextField getTextoCNPJ() {
        if (this.textoCnpj == null) {
            (this.textoCnpj = new FormatTextField(6)).setBounds(new Rectangle(30, 235, 240, 25));
            this.textoCnpj.setBackground(Color.LIGHT_GRAY);
            if (Configuracoes.SAT.cnpjSoftwareHouse != null && !Configuracoes.SAT.cnpjSoftwareHouse.equals("00000000000000")) {
                this.textoCnpj.setText(Configuracoes.SAT.cnpjSoftwareHouse);
            }
            else {
                this.textoCnpj.setEnabled(false);
            }
        }
        return this.textoCnpj;
    }
    
    private JTextField getTextoSignAC() {
        if (this.textoSignAC == null) {
            (this.textoSignAC = new JTextField()).setBounds(new Rectangle(30, 180, 240, 25));
            this.textoSignAC.setBackground(Color.LIGHT_GRAY);
            if (Configuracoes.SAT.signAC != null && !Configuracoes.SAT.signAC.equals("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")) {
                this.textoSignAC.setText(Configuracoes.SAT.signAC);
            }
            else {
                this.textoSignAC.setEnabled(false);
            }
        }
        return this.textoSignAC;
    }
    
    private JComboBox getComboTesteAtivacao() {
        if (this.comboTesteAtivacao == null) {
            (this.comboTesteAtivacao = new JComboBox()).setBounds(new Rectangle(30, 50, 240, 25));
            this.comboTesteAtivacao.addItem("Teste Fim-a-Fim de Ativa\u00e7\u00e3o");
            this.comboTesteAtivacao.addItem("Teste Fim-a-Fim de Produ\u00e7\u00e3o");
            this.comboTesteAtivacao.addActionListener(this);
            if ((Configuracoes.SAT.IE != null && !Configuracoes.SAT.IE.equals("000000000000")) || (Configuracoes.SAT.signAC != null && !Configuracoes.SAT.signAC.equals("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")) || (Configuracoes.SAT.cnpjSoftwareHouse != null && !Configuracoes.SAT.cnpjSoftwareHouse.equals("00000000000000"))) {
                this.comboTesteAtivacao.setSelectedIndex(1);
            }
        }
        return this.comboTesteAtivacao;
    }
    
    private void habilitaCampos(final boolean b) {
        this.testeAtivacao = !b;
        Color c;
        if (b) {
            c = Color.WHITE;
        }
        else {
            c = Color.LIGHT_GRAY;
        }
        this.textoIE.setEnabled(b);
        this.textoSignAC.setEnabled(b);
        this.textoCnpj.setEnabled(b);
        this.textoIE.setBackground(c);
        this.textoSignAC.setBackground(c);
        this.textoCnpj.setBackground(c);
    }
    
    private JButton getBtOk() {
        if (this.btOk == null) {
            (this.btOk = new JButton()).setBounds(new Rectangle(30, 275, 100, 25));
            this.btOk.setText("Testar");
            this.btOk.addActionListener(this);
        }
        return this.btOk;
    }
    
    private JButton getBtCancelar() {
        if (this.btCancelar == null) {
            (this.btCancelar = new JButton()).setBounds(new Rectangle(170, 275, 100, 25));
            this.btCancelar.setText("Cancelar");
            this.btCancelar.addActionListener(this);
        }
        return this.btCancelar;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.btOk) {
            String ie = this.textoIE.getText().replace(".", "").replace("-", "").replace("/", "");
            String signAC = this.textoSignAC.getText();
            String cnpj = this.textoCnpj.getText().replace(".", "").replace("-", "").replace("/", "");
            if (!this.testeAtivacao) {
                if (ie == null || ie.length() != 12) {
                    JOptionPane.showMessageDialog(null, "Campo IE inv\u00e1lido");
                    return;
                }
                if (signAC == null || signAC.length() != 344) {
                    JOptionPane.showMessageDialog(null, "Campo SignAC inv\u00e1lido");
                    return;
                }
                if (cnpj == null || cnpj.length() != 14) {
                    JOptionPane.showMessageDialog(null, "Campo CNPJ inv\u00e1lido");
                    return;
                }
            }
            else {
                ie = "000000000000";
                signAC = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                cnpj = "00000000000000";
            }
            Configuracoes.SAT.IE = ie;
            Configuracoes.SAT.signAC = signAC;
            Configuracoes.SAT.cnpjSoftwareHouse = cnpj;
            this.jAtivacao.enviarTesteFimAFim(this.testeAtivacao);
            this.dispose();
        }
        else if (e.getSource() == this.btCancelar) {
            this.dispose();
        }
        else if (e.getSource() == this.comboTesteAtivacao) {
            final int index = this.comboTesteAtivacao.getSelectedIndex();
            if (index == 0) {
                this.habilitaCampos(false);
            }
            else if (index == 1) {
                this.habilitaCampos(true);
            }
        }
    }
}
