// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.comunicacao;

import com.sun.jna.Library;

public interface FuncoesDLL extends Library
{
    String AtivarSAT(final int p0, final int p1, final String p2, final String p3, final int p4);
    
    String EnviarDadosVenda(final int p0, final String p1, final byte[] p2);
    
    String CancelarUltimaVenda(final int p0, final String p1, final String p2, final byte[] p3);
    
    String ConsultarSAT(final int p0);
    
    String TesteFimAFim(final int p0, final String p1, final byte[] p2);
    
    String ConsultarNumeroSessao(final int p0, final String p1, final int p2);
    
    String AssociarAssinatura(final int p0, final String p1, final String p2, final String p3);
    
    String ComunicarCertificadoICPBRASIL(final int p0, final String p1, final String p2);
    
    String ConsultarStatusOperacional(final int p0, final String p1);
    
    String ConfigurarInterfaceDeRede(final int p0, final String p1, final byte[] p2);
    
    String AtualizarSoftwareSAT(final int p0, final String p1);
    
    String ExtrairLogs(final int p0, final String p1);
    
    String BloquearSAT(final int p0, final String p1);
    
    String DesbloquearSAT(final int p0, final String p1);
    
    String DesativarSAT(final int p0);
    
    String TrocarCodigoDeAtivacao(final int p0, final String p1, final int p2, final String p3, final String p4);
}
