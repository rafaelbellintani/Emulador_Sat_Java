// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.modelos;

import br.com.pirus.m2m.ativacao.Configuracoes;

public class CFeTeste
{
    private String cnpj;
    
    public CFeTeste(final String cnpj) {
        this.cnpj = null;
        this.cnpj = cnpj;
    }
    
    public String getCupom() {
        final StringBuffer cfe = new StringBuffer();
        cfe.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        cfe.append("<CFe><infCFe versaoDadosEnt=\"0.02\">");
        cfe.append("<ide>");
        cfe.append("<CNPJ>").append(Configuracoes.SAT.cnpjSoftwareHouse).append("</CNPJ>");
        cfe.append("<signAC>").append(Configuracoes.SAT.signAC).append("</signAC>");
        cfe.append("<numeroCaixa>").append("012").append("</numeroCaixa>");
        cfe.append("</ide>");
        cfe.append("<emit><CNPJ>").append(this.cnpj).append("</CNPJ>");
        cfe.append("<IE>" + Configuracoes.SAT.IE + "</IE>");
        cfe.append("<cRegTribISSQN>1</cRegTribISSQN>");
        cfe.append("<indRatISSQN>N</indRatISSQN>");
        cfe.append("</emit>");
        cfe.append("<dest/>");
        cfe.append("<det nItem=\"1\">");
        cfe.append("<prod><cProd>001</cProd>");
        cfe.append("<xProd>Pao de forma</xProd>");
        cfe.append("<CFOP>5001</CFOP>");
        cfe.append("<uCom>kg</uCom>");
        cfe.append("<qCom>1.0000</qCom>");
        cfe.append("<vUnCom>1.000</vUnCom>");
        cfe.append("<indRegra>A</indRegra>");
        cfe.append("</prod>");
        cfe.append("<imposto>");
        cfe.append("<vItem12741>1.00</vItem12741>");
        cfe.append("<ICMS>");
        cfe.append("<ICMS00>");
        cfe.append("<Orig>0</Orig>");
        cfe.append("<CST>00</CST>");
        cfe.append("<pICMS>1.00</pICMS>");
        cfe.append("</ICMS00>");
        cfe.append("</ICMS>");
        cfe.append("<PIS>");
        cfe.append("<PISAliq>");
        cfe.append("<CST>01</CST>");
        cfe.append("<vBC>1.00</vBC>");
        cfe.append("<pPIS>1.0000</pPIS>");
        cfe.append("</PISAliq>");
        cfe.append("</PIS>");
        cfe.append("<PISST>");
        cfe.append("<vBC>1.00</vBC>");
        cfe.append("<pPIS>1.0000</pPIS>");
        cfe.append("</PISST>");
        cfe.append("<COFINS>");
        cfe.append("<COFINSAliq>");
        cfe.append("<CST>01</CST>");
        cfe.append("<vBC>1.00</vBC>");
        cfe.append("<pCOFINS>1.0000</pCOFINS>");
        cfe.append("</COFINSAliq>");
        cfe.append("</COFINS>");
        cfe.append("<COFINSST>");
        cfe.append("<vBC>1.00</vBC>");
        cfe.append("<pCOFINS>1.0000</pCOFINS>");
        cfe.append("</COFINSST>");
        cfe.append("</imposto>");
        cfe.append("</det>");
        cfe.append("<total>");
        cfe.append("<vCFeLei12741>1.00</vCFeLei12741>");
        cfe.append("</total>");
        cfe.append("<pgto><MP>");
        cfe.append("<cMP>01</cMP>");
        cfe.append("<vMP>2.10</vMP>");
        cfe.append("</MP></pgto>");
        cfe.append("</infCFe>");
        cfe.append("</CFe>");
        return cfe.toString();
    }
}
