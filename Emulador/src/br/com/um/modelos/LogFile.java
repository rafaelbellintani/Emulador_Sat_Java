// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

import java.io.IOException;
import java.io.File;
import br.com.um.interfaces.ILogger;

public class LogFile implements ILogger
{
    private File file;
    
    public LogFile(final String caminho) {
        this.file = null;
        try {
            (this.file = new File(caminho)).createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void logar(final Object object) {
    }
}
