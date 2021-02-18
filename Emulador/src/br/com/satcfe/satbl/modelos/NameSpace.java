// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

public class NameSpace
{
    private static String nameSpace;
    public static String CFeParametrizacao;
    public static String CFeRecepcao;
    public static String CFeRetRecepcao;
    public static String CFeCancelamento;
    public static String CFeStatus;
    public static String CFeComandos;
    public static String CFeAtualizacao;
    public static String CFeAtivacao;
    public static String CFeCertificacao;
    public static String CFeTeste;
    public static String CFeSignAC;
    public static String CFeServicoNacional;
    public static String CFeLogs;
    
    static {
        NameSpace.nameSpace = "http://www.fazenda.sp.gov.br/sat/wsdl/";
        NameSpace.CFeParametrizacao = "cfeParametrizacao";
        NameSpace.CFeRecepcao = "cfeRecepcaoLote";
        NameSpace.CFeRetRecepcao = "cfeRetRecepcao";
        NameSpace.CFeCancelamento = "cfeCancelamento";
        NameSpace.CFeStatus = "cfeStatus";
        NameSpace.CFeComandos = "cfeComandos";
        NameSpace.CFeAtualizacao = "cfeAtualiza";
        NameSpace.CFeAtivacao = "cfeAtivacao";
        NameSpace.CFeCertificacao = "cfeCertificacao";
        NameSpace.CFeTeste = "cfeTeste";
        NameSpace.CFeSignAC = "cfeSignAC";
        NameSpace.CFeServicoNacional = "cfeServicoNacional";
        NameSpace.CFeLogs = "cfeLog";
    }
    
    public static String getCFeParametrizacao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeParametrizacao;
    }
    
    public static String getCFeRecepcao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeRecepcao;
    }
    
    public static String getCFeRetRecepcao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeRetRecepcao;
    }
    
    public static String getCFeCancelamento() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeCancelamento;
    }
    
    public static String getCFeStatus() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeStatus;
    }
    
    public static String getCFeComandos() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeComandos;
    }
    
    public static String getCFeAtualizacao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeAtualizacao;
    }
    
    public static String getCFeTeste() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeTeste;
    }
    
    public static String getCFeSignAC() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeSignAC;
    }
    
    public static String getCFeLogs() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeLogs;
    }
    
    public static String getCFeAtivacao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeAtivacao;
    }
    
    public static String getCFeServicoNacional() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeServicoNacional;
    }
    
    public static String getCFeCertificacao() {
        return String.valueOf(NameSpace.nameSpace) + NameSpace.CFeCertificacao;
    }
}
