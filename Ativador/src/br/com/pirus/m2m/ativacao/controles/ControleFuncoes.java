// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao.controles;

import java.util.Random;	
import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.Arrays;
import java.util.Calendar;
import br.com.pirus.m2m.ativacao.interfaces.InterfaceResultado;
import com.sun.jna.Native;
import br.com.pirus.m2m.ativacao.comunicacao.FuncoesDLL;

public class ControleFuncoes
{
    private FuncoesDLL dll;
    private String resultado;
    private String codigoDeAtivacao;
    private long tempoResposta;
    
    
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public ControleFuncoes() {
        this.dll = (FuncoesDLL) Native.load("SAT", (Class) FuncoesDLL.class); //REMOVIDO (CLASS) da frente de FuncoesDLL
        this.resultado = null;
        this.codigoDeAtivacao = null;
        this.tempoResposta = 0L;
    }
    
    public String processarComandoPing() {
        this.resultado = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ControleLogs.logar("------------- PINGAR -------------");
                try {
                	System.out.println("TESTE");
                	//resultado = ControleFuncoes.access$1(this.dll.ConsultarSAT(000000), resultado);
                    ControleFuncoes.access$1(ControleFuncoes.this, ControleFuncoes.this.dll.ConsultarSAT(000000));
                }
                catch (Exception e) {
                	if(System.getProperty("os.name").equals("Windows")) {
                		ControleLogs.logar("Falha na execu\u00e7\u00e3o da DLL SAT.dll.");
                		ControleLogs.logar(e.getMessage());
                	}else if(System.getProperty("os.name").equals("Linux")) {
                		ControleLogs.logar("Falha na execu\u00e7\u00e3o da SO libSAT.so.");
                		ControleLogs.logar(e.getMessage());
                	}
                }
                ControleLogs.logar("------------- FIM PINGAR -------------");
            }
        }).start();
        this.aguardaRecebimentoSincrono(5000L);
        return this.resultado;
    }
    
    public String processarComandoDesativarSAT() {
        this.resultado = null;
        ControleLogs.logar("------------- DESATIVAR SAT -------------");
        try {
            this.resultado = this.dll.DesativarSAT(getRandom(6));
        }
        catch (Exception e) {
        	if(System.getProperty("os.name").equals("Windows")) {
        		ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
        		ControleLogs.logar(e.getMessage());
        	}else if(System.getProperty("os.name").equals("Linux")) {
        		ControleLogs.logar("Falha Na execu\u00e7\u00e3o do SO libSAT.so.");
                ControleLogs.logar(e.getMessage());
        	}
        }
        ControleLogs.logar("------------- FIM DESATIVAR SAT -------------");
        return this.resultado;
    }
    
    public void processarComandoEmitirCFeTeste(final InterfaceResultado tratar, final String dados, final String codigoDeAtivacao) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------EMITIR CUPOM TESTE-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        try {
            final byte[] array = Arrays.copyOf(dados.getBytes("UTF-8"), dados.getBytes("UTF-8").length + 1);
            array[dados.getBytes("UTF-8").length] = 0;
            this.resultado = this.dll.TesteFimAFim(getRandom(6), codigoDeAtivacao, array);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM EMITIR CUPOM TESTE-------------");
        if (tratar != null) {
            tratar.tratarResultado(this.resultado);
        }
    }
    
    public void processarComunicarCertificadoICPBRASIL(final InterfaceResultado tratar, final String codigoDeAtivacao, final String certificado) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------Comunicar Certificado ICPBRASIL-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        try {
            this.resultado = this.dll.ComunicarCertificadoICPBRASIL(getRandom(6), codigoDeAtivacao, certificado);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM Comunicar Certificado ICPBRASIL-------------");
        if (tratar != null) {
            tratar.tratarResultado(this.resultado);
        }
    }
    
    public void processarComandoAtivarACSATSEFAZ(final InterfaceResultado tratar, final String codigoDeAtivacao, final String CNPJ, final int subComando) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------ATIVAR SAT-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        try {
            this.resultado = this.dll.AtivarSAT(getRandom(6), subComando, codigoDeAtivacao, CNPJ, 35);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM ATIVAR SAT-------------");
        if (tratar != null) {
            tratar.tratarResultado(this.resultado);
        }
    }
    
    public void processarComandoDesbloquearSAT(final InterfaceResultado tratar, final String codigoDeAtivacao) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------DESBLOQUEAR SAT-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        if (codigoDeAtivacao == null || codigoDeAtivacao.length() < 6) {
            JOptionPane.showMessageDialog(null, "C\u00f3digo de Ativa\u00e7\u00e3o Inv\u00e1lido");
            return;
        }
        try {
            this.resultado = this.dll.DesbloquearSAT(getRandom(6), codigoDeAtivacao);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM DESBLOQUEAR SAT-------------");
        tratar.tratarResultado(this.resultado);
    }
    
    public void processarComandoBloquearSAT(final InterfaceResultado tratar, final String codigoDeAtivacao) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------BLOQUEAR SAT-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        if (codigoDeAtivacao == null || codigoDeAtivacao.length() < 6) {
            JOptionPane.showMessageDialog(null, "C\u00f3digo de Ativa\u00e7\u00e3o Inv\u00e1lido");
            return;
        }
        try {
            this.resultado = this.dll.BloquearSAT(getRandom(6), codigoDeAtivacao);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM BLOQUEAR SAT-------------");
        tratar.tratarResultado(this.resultado);
    }
    
    public void processarComandoAssocinarAssinatura(final InterfaceResultado tratar, final String assinatura, final String CNPJvalue, final String codigoDeAtivacao) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------ASSOCINAR ASSINATURA-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        if (codigoDeAtivacao == null || codigoDeAtivacao.length() < 6) {
            JOptionPane.showMessageDialog(null, "C\u00f3digo de Ativa\u00e7\u00e3o n\u00e3o informado");
            return;
        }
        try {
            this.resultado = this.dll.AssociarAssinatura(getRandom(6), codigoDeAtivacao, CNPJvalue, assinatura);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM ASSOCINAR ASSINATURA-------------");
        if (tratar != null) {
            tratar.tratarResultado(this.resultado);
        }
    }
    
    public String processarComandoAtualizarSoftwareSAT(final InterfaceResultado tratar, String codigoDeAtivacao) {
        this.resultado = null;
        ControleLogs.logar("-------------ATUALIZAR SOFTWARE SAT-------------");
        if (codigoDeAtivacao == null || codigoDeAtivacao.length() < 6) {
            codigoDeAtivacao = JOptionPane.showInputDialog("Digite o C\u00f3digo de Ativa\u00e7\u00e3o");
        }
        try {
            this.resultado = this.dll.AtualizarSoftwareSAT(getRandom(6), codigoDeAtivacao);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        ControleLogs.logar("-------------FIM ATUALIZAR SOFTWARE SAT-------------");
        return this.resultado;
    }
    
    public void processarComandoTrocarCodigoAtivacao(final InterfaceResultado tratar, String codigoDeAtivacao, final String opcao, final String novoCodigo, final String confNovoCodigo) {
        this.resultado = null;
        this.tempoResposta = 0L;
        ControleLogs.logar("-------------TROCAR CODIGO ATIVACAO-------------");
        final long inicio = Calendar.getInstance().getTimeInMillis();
        if (codigoDeAtivacao == null || codigoDeAtivacao.length() < 6) {
            codigoDeAtivacao = JOptionPane.showInputDialog("Digite o C\u00f3digo de Ativa\u00e7\u00e3o");
        }
        System.out.println(opcao);
        final int opt = Integer.parseInt(opcao);
        System.out.println(opt);
        try {
            this.resultado = this.dll.TrocarCodigoDeAtivacao(getRandom(6), codigoDeAtivacao, opt, novoCodigo, confNovoCodigo);
        }
        catch (Exception e) {
            ControleLogs.logar("Falha Na execu\u00e7\u00e3o da DLL SAT.dll.");
            ControleLogs.logar(e.getMessage());
        }
        this.tempoResposta = Calendar.getInstance().getTimeInMillis() - inicio;
        ControleLogs.logar("-------------FIM TROCAR CODIGO ATIVACAO-------------");
        if (tratar != null) {
            tratar.tratarResultado(this.resultado);
        }
    }
    
    public static int getRandom(int tamanho) {
        --tamanho;
        int max = 1;
        for (int i = 0; i < tamanho; ++i) {
            max *= 10;
        }
        final int resultado = new Random().nextInt(max);
        if (resultado >= max / 10) {
            return resultado;
        }
        return getRandom(tamanho);
    }
    
    private String aguardaRecebimentoSincrono(final long tempo) {
        try {
            long t0;
            for (long tempoAtual = t0 = System.currentTimeMillis(); tempoAtual - t0 < tempo && this.resultado == null; tempoAtual = System.currentTimeMillis()) {
                Thread.sleep(200L);
            }
            if (this.resultado == null) {
                this.resultado = "Timeout.";
            }
        }
        catch (Exception e) {
            this.resultado = "Timeout.";
        }
        System.out.println(this.resultado);
        return this.resultado;
    }
    
    public long getTempoResposta() {
        return this.tempoResposta;
    }
    
    public String getCodigoDeAtivacao() {
        return this.codigoDeAtivacao;
    }
    
    public void setCodigoDeAtivacao(final String codigoDeAtivacao) {
        this.codigoDeAtivacao = codigoDeAtivacao;
    }
    
    //ControleFuncoes.access$1(ControleFuncoes.this, ControleFuncoes.this.dll.ConsultarSAT(ControleFuncoes.getRandom(6)));
    static /* synthetic */ void access$1(final ControleFuncoes controleFuncoes, final String resultado) {
        controleFuncoes.resultado = resultado;
    }
}
