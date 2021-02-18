// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;

public class ICMSTotal
{
    private String vICMS;
    private String vProd;
    private String vDesc;
    private String vPIS;
    private String vCOFINS;
    private String vPISST;
    private String vCOFINSST;
    private String vOutro;
    
    public ICMSTotal() {
        this.vICMS = null;
        this.vProd = null;
        this.vDesc = null;
        this.vPIS = null;
        this.vCOFINS = null;
        this.vPISST = null;
        this.vCOFINSST = null;
        this.vOutro = null;
    }
    
    public ICMSTotal(final String vICMS, final String vProd, final String vDesc, final String vPIS, final String vCOFINS, final String vPISST, final String vCOFINSST, final String vOutro, final String vCFe, final String vISS) {
        this.vICMS = null;
        this.vProd = null;
        this.vDesc = null;
        this.vPIS = null;
        this.vCOFINS = null;
        this.vPISST = null;
        this.vCOFINSST = null;
        this.vOutro = null;
        this.vICMS = vICMS;
        this.vProd = vProd;
        this.vDesc = vDesc;
        this.vPIS = vPIS;
        this.vCOFINS = vCOFINS;
        this.vPISST = vPISST;
        this.vCOFINSST = vCOFINSST;
        this.vOutro = vOutro;
    }
    
    public String getvICMS() {
        return this.vICMS;
    }
    
    public void setvICMS(final String vICMS) {
        this.vICMS = vICMS;
    }
    
    public String getvProd() {
        return this.vProd;
    }
    
    public void setvProd(final String vProd) {
        this.vProd = vProd;
    }
    
    public String getvDesc() {
        return this.vDesc;
    }
    
    public void setvDesc(final String vDesc) {
        this.vDesc = vDesc;
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
    
    public String getvOutro() {
        return this.vOutro;
    }
    
    public void setvOutro(final String vOutro) {
        this.vOutro = vOutro;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.vICMS != null) {
            retorno.append("<vICMS>").append(this.vICMS).append("</vICMS>");
        }
        if (this.vProd != null) {
            retorno.append("<vProd>").append(this.vProd).append("</vProd>");
        }
        if (this.vDesc != null) {
            retorno.append("<vDesc>").append(this.vDesc).append("</vDesc>");
        }
        if (this.vPIS != null) {
            retorno.append("<vPIS>").append(this.vPIS).append("</vPIS>");
        }
        if (this.vCOFINS != null) {
            retorno.append("<vCOFINS>").append(this.vCOFINS).append("</vCOFINS>");
        }
        if (this.vPISST != null) {
            retorno.append("<vPISST>").append(this.vPISST).append("</vPISST>");
        }
        if (this.vCOFINSST != null) {
            retorno.append("<vCOFINSST>").append(this.vCOFINSST).append("</vCOFINSST>");
        }
        if (this.vOutro != null) {
            retorno.append("<vOutro>").append(this.vOutro).append("</vOutro>");
        }
    }
    
    public void completar() {
        if (this.vOutro != null) {
            this.vOutro = new ABNT5891(this.vOutro).arredondarNBR(2).toString();
        }
        if (this.vCOFINS != null) {
            this.vCOFINS = new ABNT5891(this.vCOFINS).arredondarNBR(2).toString();
        }
        if (this.vCOFINSST != null) {
            this.vCOFINSST = new ABNT5891(this.vCOFINSST).arredondarNBR(2).toString();
        }
        if (this.vPIS != null) {
            this.vPIS = new ABNT5891(this.vPIS).arredondarNBR(2).toString();
        }
        if (this.vPISST != null) {
            this.vPISST = new ABNT5891(this.vPISST).arredondarNBR(2).toString();
        }
        if (this.vDesc != null) {
            this.vDesc = new ABNT5891(this.vDesc).arredondarNBR(2).toString();
        }
        if (this.vProd != null) {
            this.vProd = new ABNT5891(this.vProd).arredondarNBR(2).toString();
        }
        if (this.vICMS != null) {
            this.vICMS = new ABNT5891(this.vICMS).arredondarNBR(2).toString();
        }
    }
}
