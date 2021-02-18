// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.view;

import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComponent;

public class TextBox extends JComponent
{
    private static final long serialVersionUID = 1L;
    private JTextField texto;
    private FormatTextField mascaraTexto;
    private JPasswordField senha;
    private JLabel label;
    
    public TextBox(final int mask) {
        this.texto = null;
        this.mascaraTexto = null;
        this.senha = null;
        this.label = null;
        if (mask == -1) {
            this.add(this.senha = new JPasswordField());
        }
        else {
            this.add(this.mascaraTexto = new FormatTextField(mask));
        }
        this.add(this.label = new JLabel());
    }
    
    public TextBox() {
        this.texto = null;
        this.mascaraTexto = null;
        this.senha = null;
        this.label = null;
        this.texto = new JTextField();
        this.label = new JLabel();
        this.add(this.texto);
        this.add(this.label);
    }
    
    public void setTexto(final String texto) {
        if (this.mascaraTexto != null) {
            this.mascaraTexto.setText(texto);
        }
        else if (texto != null) {
            this.texto.setText(texto);
        }
        else if (this.senha != null) {
            this.senha.setText(texto);
        }
    }
    
    @Override
    public void setBounds(final Rectangle r) {
        this.label.setBounds(0, 0, (int)r.getWidth(), 15);
        if (this.mascaraTexto != null) {
            this.mascaraTexto.setBounds(0, 15, (int)r.getWidth(), 25);
        }
        else if (this.texto != null) {
            this.texto.setBounds(0, 15, (int)r.getWidth(), 25);
        }
        else if (this.senha != null) {
            this.senha.setBounds(0, 15, (int)r.getWidth(), 25);
        }
        super.setBounds(r);
    }
    
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        this.label.setBounds(0, 0, width, 15);
        if (this.mascaraTexto != null) {
            this.mascaraTexto.setBounds(0, 15, width, 25);
        }
        else if (this.texto != null) {
            this.texto.setBounds(0, 15, width, 25);
        }
        else if (this.senha != null) {
            this.senha.setBounds(0, 15, width, 25);
        }
        super.setBounds(x, y, width, height);
    }
    
    public void setLabel(final String label) {
        this.label.setText(label);
    }
    
    public String getTexto() {
        if (this.mascaraTexto != null) {
            return this.mascaraTexto.getText();
        }
        if (this.texto != null) {
            return this.texto.getText();
        }
        return new String(this.senha.getPassword());
    }
    
    @Override
    public String toString() {
        return this.getTexto();
    }
    
    public String getLabel() {
        return this.label.getText();
    }
}
