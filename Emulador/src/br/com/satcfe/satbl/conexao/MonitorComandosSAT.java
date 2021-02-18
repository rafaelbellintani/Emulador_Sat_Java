// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.conexao;

import br.com.satcfe.satbl.controles.ControleArquivos;
import java.util.TimerTask;
import java.util.Timer;
import java.io.File;

public class MonitorComandosSAT
{
    private IOListener listener;
    private File diretorio;
    private Timer timer;
    
    public MonitorComandosSAT(final long pollingInterval, String dir) {
        if (dir.endsWith("/") && !dir.endsWith("\\")) {
            dir = String.valueOf(dir) + "/";
        }
        dir = String.valueOf(dir) + "/";
        this.diretorio = new File(dir);
        (this.timer = new Timer(false)).schedule(new NotificadorComandoSAT((NotificadorComandoSAT)null), 0L, pollingInterval);
    }
    
    public void stop() {
        this.timer.cancel();
    }
    
    public void setIOListener(final IOListener listener) {
        this.listener = listener;
    }
    
    public void responder(String cmd, final String dados) {
        final String lock = cmd.replaceFirst("env", "ret").replaceFirst(".sat", ".sat.lock");
        cmd = cmd.replaceFirst("env", "ret");
        ControleArquivos.escreverCaracteresArquivoASCII(lock, "".toCharArray(), false);
        ControleArquivos.escreverCaracteresArquivoASCII(cmd, dados.toCharArray(), false);
        try {
            ControleArquivos.excluirArquivo(lock);
            if (ControleArquivos.existeArquivo(lock)) {
                Thread.sleep(100L);
                ControleArquivos.excluirArquivo(lock);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class NotificadorComandoSAT extends TimerTask
    {
    //CRIADO CONSTRUTOR
    	public NotificadorComandoSAT(NotificadorComandoSAT notificadorComandoSAT) {
			// TODO Auto-generated constructor stub
		}

		@Override
        public void run() {
            if (MonitorComandosSAT.this.listener != null && MonitorComandosSAT.this.diretorio != null) {
                final String[] comandos = MonitorComandosSAT.this.diretorio.list();
                for (int i = 0; i < comandos.length; ++i) {
                    if (comandos[i].endsWith(".sat") && comandos[i].startsWith("env-")) {
                        boolean lockado = false;
                        for (int j = 0; j < comandos.length; ++j) {
                            if (comandos[j].equals(String.valueOf(comandos[i]) + ".lock")) {
                                lockado = true;
                                break;
                            }
                        }
                        if (!lockado) {
                            MonitorComandosSAT.this.listener.notifyIncomingData(String.valueOf(MonitorComandosSAT.this.diretorio.getAbsolutePath()) + "/" + comandos[i], ControleArquivos.lerArquivoUTF8(String.valueOf(MonitorComandosSAT.this.diretorio.getAbsolutePath()) + "/" + comandos[i]));
                            if (!ControleArquivos.excluirArquivo(String.valueOf(MonitorComandosSAT.this.diretorio.getAbsolutePath()) + "/" + comandos[i])) {
                                System.out.println("n\u00e3o foi possivel apagar o arquivo de envio de funcoes");
                            }
                        }
                    }
                }
            }
        }
    }
}
