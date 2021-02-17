// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.BufferedReader;

public abstract class FTPFileEntryParserImpl implements FTPFileEntryParser
{
    public String readNextEntry(final BufferedReader reader) throws IOException {
        return reader.readLine();
    }
    
    public List<String> preParse(final List<String> original) {
        final Iterator<String> it = original.iterator();
        while (it.hasNext()) {
            final String entry = it.next();
            if (null == this.parseFTPEntry(entry)) {
                it.remove();
            }
        }
        return original;
    }
}
