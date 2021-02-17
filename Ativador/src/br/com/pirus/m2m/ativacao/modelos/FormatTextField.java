// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.modelos;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;

public class FormatTextField extends JFormattedTextField
{
    private String ultimo;
    private String validos;
    private int msk;
    private String mask;
    private int tam_minimo;
    private int tam_maximo;
    private int tamanho;
    public static final int F_PADRAO = 0;
    public static final int F_VALOR_MOEDA = 1;
    public static final int F_SOMENTE_NUMERO = 2;
    public static final int F_IE = 3;
    public static final int F_IM = 4;
    public static final int F_CPF = 5;
    public static final int F_CNPJ = 6;
    public static final int F_CPF_CNPJ = 7;
    public static final int F_DATA = 8;
    public static final int F_HORA = 9;
    public static final int F_DATA_HORA = 10;
    public static final int F_ALFANUMERICOS = 11;
    private static String MASK_NUMEROS;
    private static String MASK_ANY;
    private static final String MASK_IE = "###.###.###.###";
    private static final String MASK_CPF = "###.###.###-##";
    private static final String MASK_CNPJ = "##.###.###/####-##";
    private static final String MASK_IM = "#.###.###-#";
    private static final String MASK_DATA = "##/##/####";
    private static final String MASK_HORA = "##:##:##";
    private static final String MASK_DATA_HORA = "##/##/#### ##:##:##";
    
    static {
        FormatTextField.MASK_NUMEROS = "######";
        FormatTextField.MASK_ANY = "*****";
    }
    
    public FormatTextField(final int mascara, final int qtd) {
        this.ultimo = null;
        this.validos = "0123456789";
        this.msk = 0;
        this.mask = null;
        this.tam_minimo = 2;
        this.tam_maximo = 15;
        this.tamanho = 0;
        if (mascara == 2) {
            this.msk = mascara;
            FormatTextField.MASK_NUMEROS = this.gerarMascara(qtd, "#");
            try {
                this.setValue(null);
                final MaskFormatter m = new MaskFormatter(FormatTextField.MASK_NUMEROS);
                this.setFormatterFactory(new DefaultFormatterFactory(m));
            }
            catch (Exception ex) {}
        }
        else if (mascara == 11) {
            this.msk = mascara;
            FormatTextField.MASK_ANY = this.gerarMascara(qtd, "*");
            try {
                this.setValue(null);
                final MaskFormatter m = new MaskFormatter(FormatTextField.MASK_ANY);
                this.setFormatterFactory(new DefaultFormatterFactory(m));
            }
            catch (Exception ex2) {}
        }
    }
    
    public FormatTextField(final int mascara) {
        this.ultimo = null;
        this.validos = "0123456789";
        this.msk = 0;
        this.mask = null;
        this.tam_minimo = 2;
        this.tam_maximo = 15;
        this.tamanho = 0;
        switch (mascara) {
            case 1: {
                this.setHorizontalAlignment(4);
                this.setText(",  ");
                this.msk = mascara;
                break;
            }
            case 7: {
                this.msk = mascara;
                break;
            }
            case 4: {
                try {
                    this.setValue(null);
                    final MaskFormatter m = new MaskFormatter("#.###.###-#");
                    this.setFormatterFactory(new DefaultFormatterFactory(m));
                }
                catch (Exception ex) {}
                break;
            }
            case 3: {
                try {
                    this.setValue(null);
                    final MaskFormatter m = new MaskFormatter("###.###.###.###");
                    this.setFormatterFactory(new DefaultFormatterFactory(m));
                }
                catch (Exception ex2) {}
                break;
            }
            case 5: {
                try {
                    this.setValue(null);
                    final MaskFormatter m = new MaskFormatter("###.###.###-##");
                    this.setFormatterFactory(new DefaultFormatterFactory(m));
                }
                catch (Exception ex3) {}
                break;
            }
            case 6: {
                try {
                    this.setValue(null);
                    final MaskFormatter m = new MaskFormatter("##.###.###/####-##");
                    this.setFormatterFactory(new DefaultFormatterFactory(m));
                }
                catch (Exception ex4) {}
                break;
            }
        }
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(final KeyEvent evt) {
                switch (FormatTextField.this.msk) {
                    case 1: {
                        if (FormatTextField.this.validos.indexOf(evt.getKeyChar()) >= 0) {
                            FormatTextField.this.formatarValorMoeda(true, evt.getKeyChar());
                            break;
                        }
                        evt.setKeyChar('\0');
                        FormatTextField.this.formatarValorMoeda(false, evt.getKeyChar());
                        break;
                    }
                    case 7: {
                        if (FormatTextField.this.validos.indexOf(evt.getKeyChar()) >= 0) {
                            FormatTextField.this.formatarCpfCnpj(true, evt.getKeyChar());
                            break;
                        }
                        evt.setKeyChar('\0');
                        FormatTextField.this.formatarCpfCnpj(false, evt.getKeyChar());
                        break;
                    }
                }
            }
        });
    }
    
    private void formatarValorMoeda(final boolean isKey, final char key) {
        this.setFMask();
        final String text = this.getText().replace(",", "").replace("/", "").replace("-", "").replace(" ", "").replace(".", "");
        final char[] mas = this.mask.toCharArray();
        char[] val = null;
        if (isKey) {
            val = (String.valueOf(text) + key).toCharArray();
        }
        else {
            val = text.toCharArray();
        }
        for (int i = mas.length - 1, j = val.length - 1; i >= 0 && j >= 0; --i, --j) {
            if (mas[i] == '#') {
                mas[i] = val[j];
            }
            else {
                ++j;
            }
        }
        final String s = this.removeMascara(mas);
        if (!s.isEmpty() && isKey) {
            this.setText(s.substring(0, s.length() - 1));
        }
    }
    
    private void formatarCpfCnpj(final boolean isKey, final char key) {
        final String text = this.getText().replace("-", "").replace("/", "").replace(",", "").replace(" ", "").replace(".", "");
        if (text.length() >= 11 && isKey) {
            this.setFMask(6);
        }
        else {
            this.setFMask(5);
        }
        final char[] mas = this.mask.toCharArray();
        char[] val = null;
        if (isKey) {
            val = (String.valueOf(text) + key).toCharArray();
        }
        else {
            val = text.toCharArray();
        }
        for (int i = mas.length - 1, j = val.length - 1; i >= 0 && j >= 0; --i, --j) {
            if (mas[i] == '#') {
                mas[i] = val[j];
            }
            else {
                ++j;
            }
        }
        final String s = new String(mas).replace("#", " ");
        if (!s.isEmpty() && isKey) {
            this.setText(s.substring(0, s.length() - 1));
        }
    }
    
    private String removeMascara(final char[] mas) {
        boolean valid = false;
        for (int i = 0; i < mas.length; ++i) {
            if (this.validos.indexOf(mas[i]) >= 0) {
                valid = true;
            }
            if (!valid) {
                mas[i] = ' ';
            }
        }
        String s = new String(mas);
        s = s.replace(" ", "");
        if (s.length() == 1) {
            s = ", " + s;
        }
        if (s.length() == 2) {
            s = "," + s;
        }
        return s;
    }
    
    private void formatar(final boolean isKey) {
        String valor = this.getText();
        if (isKey) {
            valor = String.valueOf(valor) + "#";
        }
        valor = valor.replace(" ", "");
        if (valor.equals(",")) {
            valor = ",  ";
        }
        else if (valor.length() >= 1 && valor.length() <= 2) {
            valor = valor.replace(",", "").replace(".", "");
            valor = "," + valor;
        }
        else if (valor.length() > 2) {
            valor = valor.replace(",", "").replace(".", "");
            final int n = valor.length();
            if (n < 6) {
                valor = String.valueOf(valor.substring(0, n - 2)) + "," + valor.substring(n - 2);
            }
            else if (n >= 6 && n < 9) {
                valor = String.valueOf(valor.substring(0, n - 5)) + "." + valor.substring(n - 5, n - 2) + "," + valor.substring(n - 2);
            }
            else if (n >= 9 && n < 12) {
                valor = String.valueOf(valor.substring(0, n - 8)) + "." + valor.substring(n - 8, n - 5) + "." + valor.substring(n - 5, n - 2) + "," + valor.substring(n - 2);
            }
            else if (n >= 12 && n < 15) {
                valor = String.valueOf(valor.substring(0, n - 11)) + "." + valor.substring(n - 11, n - 8) + "." + valor.substring(n - 8, n - 5) + "." + valor.substring(n - 5, n - 2) + "," + valor.substring(n - 2);
            }
            else if (n == 15) {
                valor = String.valueOf(valor.substring(0, n - 14)) + "." + valor.substring(n - 14, n - 11) + "." + valor.substring(n - 11, n - 8) + "." + valor.substring(n - 8, n - 5) + "." + valor.substring(n - 5, n - 2) + "," + valor.substring(n - 2);
            }
            else {
                valor = this.ultimo;
            }
        }
        else {
            valor = ",  ";
        }
        this.ultimo = valor;
        this.setText(valor.replace("#", ""));
    }
    
    private String gerarMascara(final int qtd, final String ch) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < qtd; ++i) {
            sb.append(ch);
        }
        return sb.toString();
    }
    
    public void setValidos(final String validos) {
        this.validos = validos;
    }
    
    private void setFMask() {
        this.setFMask(this.msk);
    }
    
    private void setFMask(final int opt) {
        switch (opt) {
            case 1: {
                this.mask = "#.###.###.###.###,##";
                break;
            }
            case 3: {
                this.mask = "###.###.###.###";
                break;
            }
            case 4: {
                this.mask = "#.###.###-#";
                break;
            }
            case 5: {
                this.mask = "###.###.###-##";
                break;
            }
            case 6: {
                this.mask = "##.###.###/####-##";
                break;
            }
            case 7: {
                this.mask = "###.###.###-##";
                break;
            }
            case 8: {
                this.mask = "##/##/####";
                break;
            }
            case 9: {
                this.mask = "##:##:##";
                break;
            }
            case 10: {
                this.mask = "##/##/#### ##:##:##";
                break;
            }
            case 2: {
                this.mask = FormatTextField.MASK_NUMEROS;
                break;
            }
        }
    }
}
