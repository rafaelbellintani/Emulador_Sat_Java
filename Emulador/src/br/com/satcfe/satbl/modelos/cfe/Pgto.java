// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.Decimal;
import java.math.BigDecimal;
import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.ArrayList;

public class Pgto
{
    private ArrayList<MP> Mp;
    private static String vTroco;
    
    static {
        Pgto.vTroco = null;
    }
    
    public Pgto(final Node no) {
        this.Mp = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("MP")) {
                if (this.Mp == null) {
                    this.Mp = new ArrayList<MP>();
                }
                this.Mp.add(new MP(filhoAtual));
            }
        }
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.Mp == null) {
            ControleLogs.logar("Campo 'MP' inexistente.");
            resultado = "1999";
        }
        else if (this.Mp.size() >= 1) {
            for (int i = 0; i < this.Mp.size(); ++i) {
                if (!(resultado = this.Mp.get(i).validar()).equals("1000")) {
                    break;
                }
            }
        }
        return resultado;
    }
    
    public void completar() {
        if (this.Mp != null && this.Mp.size() >= 1) {
            for (int i = 0; i < this.Mp.size(); ++i) {
                if (this.Mp.get(i).getvMP() != null) {
                    this.Mp.get(i).setvMP(new ABNT5891(this.Mp.get(i).getvMP()).arredondarNBR(2).toString());
                }
            }
        }
        if (Pgto.vTroco != null) {
            Pgto.vTroco = new ABNT5891(Pgto.vTroco).arredondarNBR(2).toString();
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer retorno = new StringBuffer();
        if (this.Mp != null) {
            for (int i = 0; i < this.Mp.size(); ++i) {
                retorno.append("<MP>").append(this.Mp.get(i).toString()).append("</MP>");
            }
        }
        if (Pgto.vTroco != null) {
            retorno.append("<vTroco>").append(Pgto.vTroco).append("</vTroco>");
        }
        return retorno.toString();
    }
    
    public void calcularVTroco(final String vCFe) {
        BigDecimal valor = new BigDecimal("0");
        for (int i = 0; i < this.Mp.size(); ++i) {
            valor = Decimal.somar(valor.toString(), this.Mp.get(i).getvMP());
        }
        this.setvTroco(Decimal.subtrair(valor.toString(), vCFe).toString());
    }
    
    public void setvTroco(final String vTroco) {
        Pgto.vTroco = vTroco;
    }
    
    public String getvTroco() {
        return Pgto.vTroco;
    }
    
    public ArrayList<MP> getMp() {
        return this.Mp;
    }
}
