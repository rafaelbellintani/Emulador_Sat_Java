// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl;

import java.io.PrintStream;
import java.util.Scanner;
import java.io.Console;
import java.io.File;
import java.io.IOException;

import br.com.satcfe.satbl.Configuracoes.SistemaArquivos;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.interfaces.TelaPrincipal;

public class Main {
	
    public static boolean sair;
    
    public static void main(final String[] args) throws IOException{
    	//DETECTA E IMPRIME QUAL SISTEMA OPERACIONAL É
        Configuracoes.setSistemaOperacional();
        
        //Se o sistema operacional detectado for o Windows, ele executará isso.
        if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
            try {
                Configuracoes.carregarSATOffLine();
                final TelaPrincipal tp = new TelaPrincipal();
                Thread.sleep(200L);
                tp.setVisible(true);
                ControleLogs.tp = tp;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
            try {
                Configuracoes.carregarSATOffLine();
                final TelaPrincipal tp = new TelaPrincipal();
                Thread.sleep(200L);
                tp.setVisible(true);
                ControleLogs.tp = tp;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ControleLogs.logar("Iniciando emulador SAT-CFe (SWEDA) V0.1");
        ControleLogs.logar("Carregando configura\u00e7\u00f5es iniciais.");
    process:
        while (true) {
        	//Verifica se todas as configuracões iniciais foram carregadas, caso não foi, ele finaliza o programa
            if (!new Configuracoes().carregarConfiguracoesIniciais()) {
                Main.sair = true;
                ControleLogs.logar("ERRO:Arquivo de configuracoes n\u00e3o foi encontrado!");
                //Main.sair();
                break process;
            }
            ControleLogs.logar("Numero de Serie: " + Configuracoes.SAT.numeroDeSerie);
            if (Configuracoes.SAT.emuladorOffLine) {
                ControleLogs.tp.repaint();
                ControleLogs.logar("EMULADOR DO SAT OFF LINE");
                ControleLogs.tp.invalidate();
            }
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.DIRETORIO_COMANDOS);
            Configuracoes.SistemaArquivos.criarDiretoriosSATBL();

            try {
                if (Configuracoes.SAT.debug) {
                    ControleLogs.logar("Debug ativado");
                    if (!new File(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS).exists()) {
                    	final String os = System.getProperty("os.name");
                    	if(os.equals("Linux")) {
                    		Scanner entrada = new Scanner(System.in);
                    		System.out.println("Você não tem autorização de superusuário, digite sua senha para executar o emulador");
                    		System.out.println("SENHA: ");
                    		String passwd = entrada.next();
                    		Runtime.getRuntime().exec("sudo " + passwd);
                    		entrada.close();
                    		new File(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS).createNewFile();
                    		System.out.println("Pasta DEBUG criada com sucesso...");
                		}else {
                			new File(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS).createNewFile();
                		}
                    }
                    System.setErr(new PrintStream(new File(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ERROS)));
                    ControleLogs.logar("Pasta DEBUG encontrada...");
                }
                
            }
            catch (Exception e2) {
            	if(e2.getMessage().contains("denied")) {
            		System.out.println("Senha de super usuário foi digitada incorretamente");
            	}
            }
            final MainSATBL SATBL = new MainSATBL();
            
            //CARREGAR PARAMETRIZAÇÃO
            SATBL.executar();
            while (true) {
                try {
                    while (true) {
                        if (Main.sair) {
                            sair();
                        }
                        Thread.sleep(1000L);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                //continue process;
            }
           //break;
        }
    }
    
    public static void sair() {
        try {
        	System.out.println("Desligando o emulador...");
            Thread.sleep(6000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    
}
