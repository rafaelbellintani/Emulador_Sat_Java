// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.List;

public class FTPListParseEngine
{
    private List<String> entries;
    private ListIterator<String> _internalIterator;
    FTPFileEntryParser parser;
    
    public FTPListParseEngine(final FTPFileEntryParser parser) {
        this.entries = new LinkedList<String>();
        this._internalIterator = this.entries.listIterator();
        this.parser = null;
        this.parser = parser;
    }
    
    public void readServerList(final InputStream stream, final String encoding) throws IOException {
        this.entries = new LinkedList<String>();
        this.readStream(stream, encoding);
        this.parser.preParse(this.entries);
        this.resetIterator();
    }
    
    @Deprecated
    public void readServerList(final InputStream stream) throws IOException {
        this.readServerList(stream, null);
    }
    
    private void readStream(final InputStream stream, final String encoding) throws IOException {
        BufferedReader reader;
        if (encoding == null) {
            reader = new BufferedReader(new InputStreamReader(stream));
        }
        else {
            reader = new BufferedReader(new InputStreamReader(stream, encoding));
        }
        for (String line = this.parser.readNextEntry(reader); line != null; line = this.parser.readNextEntry(reader)) {
            this.entries.add(line);
        }
        reader.close();
    }
    
    public FTPFile[] getNext(final int quantityRequested) {
        final List<FTPFile> tmpResults = new LinkedList<FTPFile>();
        for (int count = quantityRequested; count > 0 && this._internalIterator.hasNext(); --count) {
            final String entry = this._internalIterator.next();
            final FTPFile temp = this.parser.parseFTPEntry(entry);
            tmpResults.add(temp);
        }
        return tmpResults.toArray(new FTPFile[0]);
    }
    
    public FTPFile[] getPrevious(final int quantityRequested) {
        final List<FTPFile> tmpResults = new LinkedList<FTPFile>();
        for (int count = quantityRequested; count > 0 && this._internalIterator.hasPrevious(); --count) {
            final String entry = this._internalIterator.previous();
            final FTPFile temp = this.parser.parseFTPEntry(entry);
            tmpResults.add(0, temp);
        }
        return tmpResults.toArray(new FTPFile[0]);
    }
    
    public FTPFile[] getFiles() throws IOException {
        final List<FTPFile> tmpResults = new LinkedList<FTPFile>();
        for (final String entry : this.entries) {
            final FTPFile temp = this.parser.parseFTPEntry(entry);
            tmpResults.add(temp);
        }
        return tmpResults.toArray(new FTPFile[0]);
    }
    
    public boolean hasNext() {
        return this._internalIterator.hasNext();
    }
    
    public boolean hasPrevious() {
        return this._internalIterator.hasPrevious();
    }
    
    public void resetIterator() {
        this._internalIterator = this.entries.listIterator();
    }
}
