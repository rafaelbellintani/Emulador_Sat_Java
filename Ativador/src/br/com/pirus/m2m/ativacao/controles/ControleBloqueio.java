// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.controles;

import java.awt.Component;
import javax.swing.JOptionPane;
import br.com.pirus.m2m.ativacao.Configuracoes;
import br.com.pirus.m2m.ativacao.interfaces.InterfaceResultado;

public class ControleBloqueio implements InterfaceResultado
{
    ControleFuncoes cFuncoes;
    
    public ControleBloqueio(final ControleFuncoes cFuncoes) {
        this.cFuncoes = null;
        this.cFuncoes = cFuncoes;
    }
    
    public void iniciarBloqueioSAT() {
        this.cFuncoes.processarComandoBloquearSAT(this, Configuracoes.SAT.codigoDeAtivacao);
    }
    
    public void iniciarDesbloqueioSAT() {
        this.cFuncoes.processarComandoDesbloquearSAT(this, Configuracoes.SAT.codigoDeAtivacao);
    }
    
    @Override
    public void tratarResultado(final String retorno) {
        JOptionPane.showMessageDialog(null, retorno);
    }
}
