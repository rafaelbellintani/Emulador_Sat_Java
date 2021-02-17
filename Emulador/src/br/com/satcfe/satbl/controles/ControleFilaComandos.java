// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.util.List;
import br.com.satcfe.satbl.modelos.ComandoSefaz;
import java.util.ArrayList;

public class ControleFilaComandos
{
    private ArrayList<ComandoSefaz> fila;
    private static ControleFilaComandos instance;
    private boolean processando;
    
    static {
        ControleFilaComandos.instance = null;
    }
    
    public static ControleFilaComandos getInstance() {
        if (ControleFilaComandos.instance == null) {
            ControleFilaComandos.instance = new ControleFilaComandos();
        }
        return ControleFilaComandos.instance;
    }
    
    private ControleFilaComandos() {
        this.fila = null;
        this.processando = false;
        this.fila = new ArrayList<ComandoSefaz>();
    }
    
    public void addComando(final ComandoSefaz cmd) {
        if (this.fila == null) {
            this.fila = new ArrayList<ComandoSefaz>();
        }
        if (cmd != null && !this.comandoExiste(cmd.getIdCmd())) {
            this.fila.add(cmd);
        }
    }
    
    public ComandoSefaz processarProximoComando() {
        if (!this.processando && this.fila != null && this.fila.size() > 0) {
            return this.fila.get(0);
        }
        return null;
    }
    
    public void removeComando(final String idCmd) {
        if (this.fila != null && this.fila.size() > 0) {
            for (int i = 0; i < this.fila.size(); ++i) {
                if (this.fila.get(i).getIdCmd().equals(idCmd)) {
                    this.fila.remove(i);
                    this.processando = false;
                    break;
                }
            }
        }
    }
    
    private boolean comandoExiste(final String idCmd) {
        for (int i = 0; i < this.fila.size(); ++i) {
            if (this.fila.get(i).getIdCmd().equals(idCmd)) {
                return true;
            }
        }
        return false;
    }
    
    public int size() {
        if (this.fila == null) {
            return 0;
        }
        return this.fila.size();
    }
    
    public void addListaComando(final List<ComandoSefaz> lista) {
        for (int i = 0; i < lista.size(); ++i) {
            this.addComando(lista.get(i));
        }
    }
    
    public void setProcessando(final boolean b) {
        this.processando = b;
    }
    
    public boolean processando() {
        return this.processando;
    }
}
