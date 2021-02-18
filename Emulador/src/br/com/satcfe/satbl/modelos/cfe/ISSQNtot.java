// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.um.modelos.ABNT5891;

public class ISSQNtot
{
    private String vBC;
    private String vISS;
    private String vPIS;
    private String vCOFINS;
    private String vPISST;
    private String vCOFINSST;
    
    public ISSQNtot() {
        this.vBC = null;
        this.vISS = null;
        this.vPIS = null;
        this.vCOFINS = null;
        this.vPISST = null;
        this.vCOFINSST = null;
    }
    
    public ISSQNtot(final String vBC, final String vISS, final String vPIS, final String vCOFINS, final String vPISST, final String vCOFINSST) {
        this();
        this.vBC = vBC;
        this.vISS = vISS;
        this.vPIS = vPIS;
        this.vCOFINS = vCOFINS;
        this.vPISST = vPISST;
        this.vCOFINSST = vCOFINSST;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
    }
    
    public String getvISS() {
        return this.vISS;
    }
    
    public void setvISS(final String vISS) {
        this.vISS = vISS;
    }
    
    public String getvPIS() {
        return this.vPIS;
    }
    
    public void setvPIS(final String vPIS) {
        this.vPIS = vPIS;
    }
    
    public String getvCOFINS() {
        return this.vCOFINS;
    }
    
    public void setvCOFINS(final String vCOFINS) {
        this.vCOFINS = vCOFINS;
    }
    
    public String getvPISST() {
        return this.vPISST;
    }
    
    public void setvPISST(final String vPISST) {
        this.vPISST = vPISST;
    }
    
    public String getvCOFINSST() {
        return this.vCOFINSST;
    }
    
    public void setvCOFINSST(final String vCOFINSST) {
        this.vCOFINSST = vCOFINSST;
    }
    
    public void completar() {
        if (this.vBC != null) {
            this.vBC = new ABNT5891(this.vBC).arredondarNBR(2).toString();
        }
        if (this.vISS != null) {
            this.vISS = new ABNT5891(this.vISS).arredondarNBR(2).toString();
        }
        if (this.vPIS != null) {
            this.vPIS = new ABNT5891(this.vPIS).arredondarNBR(2).toString();
        }
        if (this.vCOFINS != null) {
            this.vCOFINS = new ABNT5891(this.vCOFINS).arredondarNBR(2).toString();
        }
        if (this.vPISST != null) {
            this.vPISST = new ABNT5891(this.vPISST).arredondarNBR(2).toString();
        }
        if (this.vCOFINSST != null) {
            this.vCOFINSST = new ABNT5891(this.vCOFINSST).arredondarNBR(2).toString();
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (ControleDados.isNumerico(this.vBC)) {
            retorno.append("<vBC>").append(this.vBC).append("</vBC>");
        }
        if (ControleDados.isNumerico(this.vISS)) {
            retorno.append("<vISS>").append(this.vISS).append("</vISS>");
        }
        if (ControleDados.isNumerico(this.vPIS)) {
            retorno.append("<vPIS>").append(this.vPIS).append("</vPIS>");
        }
        if (ControleDados.isNumerico(this.vCOFINS)) {
            retorno.append("<vCOFINS>").append(this.vCOFINS).append("</vCOFINS>");
        }
        if (ControleDados.isNumerico(this.vPISST)) {
            retorno.append("<vPISST>").append(this.vPISST).append("</vPISST>");
        }
        if (ControleDados.isNumerico(this.vCOFINSST)) {
            retorno.append("<vCOFINSST>").append(this.vCOFINSST).append("</vCOFINSST>");
        }
    }
}
