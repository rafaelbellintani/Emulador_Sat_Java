// 
// Decompiled by Procyon v0.5.36
// 

package br.com.pirus.m2m.ativacao;

import br.com.pirus.m2m.ativacao.utils.Utils;
import br.com.pirus.m2m.ativacao.interfaces.JanelaComandos;
import br.com.pirus.m2m.ativacao.controles.ControleFuncoes;
import java.awt.Component;
import javax.swing.JOptionPane;

import com.sun.jna.Native;

import br.com.pirus.m2m.ativacao.comunicacao.FuncoesDLL;
import br.com.pirus.m2m.ativacao.controles.ControleArquivos;
import br.com.pirus.m2m.ativacao.interfaces.SplashScreen;

public class Main
{
    
	public static void main(final String[] args) {
    	String osName = System.getProperty("os.name");
    	if(osName.equals("Windows")) {
    		System.setProperty("jna.library.path", "C:/SAT/");
    	}else if(osName.equals("Linux")) {
    		System.setProperty("jna.library.path", "/SAT/");
    	}
    	
        final SplashScreen splashScreen = new SplashScreen("/images/nfpaulista.PNG");
        splashScreen.splash();
        
        if(osName.equals("Windows")) {
        if (!ControleArquivos.existeArquivo("c:/SAT/SAT.dll")) {
            JOptionPane.showMessageDialog(null, "N\u00e3o foi possivel se conectar ao SAT-FISCAL, SAT.dll não encontrado, o programa n\u00e3o ir\u00e1 iniciar!");
            System.exit(0);
        	}
        }else if(osName.equals("Linux")) {
        	if (!ControleArquivos.existeArquivo("/SAT/libSAT.so")) {
                JOptionPane.showMessageDialog(null, "N\u00e3o foi possivel se conectar ao SAT-FISCAL, libSAT.so não econtrado, o programa n\u00e3o ir\u00e1 iniciar!");
                System.exit(0);
            	}
        }
        
        final ControleFuncoes cAtivacao = new ControleFuncoes();
        final JanelaComandos comandos = new JanelaComandos(cAtivacao);
        final String retornoPing = cAtivacao.processarComandoPing();
        if (retornoPing.equals("Timeout.")) {
            JOptionPane.showMessageDialog(null, "N\u00e3o foi possivel se conectar ao SAT-FISCAL, o programa n\u00e3o ir\u00e1 iniciar!");
            System.exit(0);
        }
        else if (retornoPing != null) {
            JOptionPane.showMessageDialog(null, Utils.tratarResultadoPing(retornoPing));
        }
        splashScreen.disable();
        comandos.setVisible(true);
        comandos.setFocusable(true);
    }
}
