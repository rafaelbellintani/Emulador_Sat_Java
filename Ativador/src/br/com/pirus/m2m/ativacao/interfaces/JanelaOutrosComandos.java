// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.interfaces;

import br.com.pirus.m2m.ativacao.Configuracoes;
import br.com.pirus.m2m.ativacao.utils.Utils;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class JanelaOutrosComandos extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JPanel jPane;
    private JButton btConfigurarRede;
    private JButton btTrocarCodigo;
    private JLabel labelTitulo;
    private JButton btResetSAT;
    private JButton btAtualizarSAT;
    
    public JanelaOutrosComandos() {
        this.jPane = null;
        this.btConfigurarRede = null;
        this.btTrocarCodigo = null;
        this.labelTitulo = null;
        this.btResetSAT = null;
        this.btAtualizarSAT = null;
        this.initialize();
    }
    
    private void initialize() {
        this.setSize(300, 242);
        this.setContentPane(this.getJPane());
        this.setTitle("Ativa\u00e7\u00e3o SAT-CFe v2.2.5");
        this.setModalExclusionType(this.getModalExclusionType());
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
        this.setResizable(false);
    }
    
    private JPanel getJPane() {
        if (this.jPane == null) {
            (this.labelTitulo = new JLabel()).setBounds(new Rectangle(5, 5, 282, 20));
            this.labelTitulo.setText("Outras Fun\u00e7\u00f5es do SAT-CFe");
            this.labelTitulo.setHorizontalAlignment(0);
            (this.jPane = new JPanel()).setLayout(null);
            this.jPane.setBackground(Color.white);
            this.jPane.add(this.labelTitulo, null);
            this.jPane.add(this.getBtTrocarCodigo(), null);
            this.jPane.add(this.getBtResetSAT(), null);
            this.jPane.add(this.getBtConfigurarRede(), null);
            this.jPane.add(this.getBtAtualizarSAT(), null);
        }
        return this.jPane;
    }
    
    private JButton getBtConfigurarRede() {
        if (this.btConfigurarRede == null) {
            (this.btConfigurarRede = new JButton()).setText("Configurar Interface de Rede");
            this.btConfigurarRede.setBounds(new Rectangle(40, 125, 200, 30));
            this.btConfigurarRede.addActionListener(this);
        }
        return this.btConfigurarRede;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() != this.btConfigurarRede) {
            if (e.getSource() == this.btTrocarCodigo) {
                final JanelaTrocarCodigoAtivacao janela = new JanelaTrocarCodigoAtivacao();
                janela.setVisible(true);
            }
            else if (e.getSource() == this.btResetSAT) {
                final String res = new ControleFuncoes().processarComandoDesativarSAT();
                if (res == null) {
                    JOptionPane.showMessageDialog(null, "Erro ao Resetar o SAT");
                }
                else {
                    final String[] array = Utils.quebrarString(res, "|");
                    JOptionPane.showMessageDialog(null, String.valueOf(array[1]) + array[2]);
                }
            }
            else if (e.getSource() == this.btAtualizarSAT) {
                final String codigoAtivacao = null;
                final Runnable loading = new Runnable() {
                    @Override
                    public void run() {
                        final JanelaLoading loading = new JanelaLoading("Atualizando");
                        loading.setVisible(true);
                        final String res = new ControleFuncoes().processarComandoAtualizarSoftwareSAT(null, Configuracoes.SAT.codigoDeAtivacao);
                        loading.dispose();
                        JOptionPane.showMessageDialog(null, res, "Atualiza\u00e7\u00e3o", 1);
                    }
                };
                new Thread(loading).start();
            }
        }
    }
    
    private JButton getBtTrocarCodigo() {
        if (this.btTrocarCodigo == null) {
            (this.btTrocarCodigo = new JButton()).addActionListener(this);
            this.btTrocarCodigo.setText("Trocar C\u00f3digo de Ativa\u00e7\u00e3o");
            this.btTrocarCodigo.setBounds(40, 45, 200, 30);
        }
        return this.btTrocarCodigo;
    }
    
    private JButton getBtResetSAT() {
        if (this.btResetSAT == null) {
            (this.btResetSAT = new JButton()).setText("Reset SAT-CFe");
            this.btResetSAT.setBounds(40, 85, 200, 30);
            this.btResetSAT.addActionListener(this);
        }
        return this.btResetSAT;
    }
    
    private JButton getBtAtualizarSAT() {
        if (this.btAtualizarSAT == null) {
            (this.btAtualizarSAT = new JButton()).setBounds(new Rectangle(39, 162, 201, 34));
            this.btAtualizarSAT.setText("Atualizar SAT");
            this.btAtualizarSAT.addActionListener(this);
        }
        return this.btAtualizarSAT;
    }
}
