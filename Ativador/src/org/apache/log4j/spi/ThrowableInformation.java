// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Serializable;

public class ThrowableInformation implements Serializable
{
    static final long serialVersionUID = -4748765566864322735L;
    private transient Throwable throwable;
    private String[] rep;
    
    public ThrowableInformation(final Throwable throwable) {
        this.throwable = throwable;
    }
    
    public ThrowableInformation(final String[] r) {
        if (r != null) {
            this.rep = r.clone();
        }
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public String[] getThrowableStrRep() {
        if (this.rep != null) {
            return this.rep.clone();
        }
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        this.throwable.printStackTrace(pw);
        pw.flush();
        final LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        final ArrayList lines = new ArrayList();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
        }
        catch (IOException ex) {
            lines.add(ex.toString());
        }
        lines.toArray(this.rep = new String[lines.size()]);
        return this.rep;
    }
}
