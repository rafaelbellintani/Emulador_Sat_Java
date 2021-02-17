// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.opencsv;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

public class CSV
{
    private final char separator;
    private final char quotechar;
    private final char escapechar;
    private final Charset charset;
    private final String lineEnd;
    private final int skipLines;
    private final boolean strictQuotes;
    private final boolean ignoreLeadingWhiteSpace;
    
    private CSV(final char separator, final char quotechar, final char escapechar, final String lineEnd, final int skipLines, final boolean strictQuotes, final boolean ignoreLeadingWhiteSpace, final Charset charset) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
        this.skipLines = skipLines;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.charset = charset;
    }
    
    private CSV() {
        this(',', '\"', '\\', "\n", 0, false, true, Charset.defaultCharset());
    }
    
    public CSVWriter writer(final Writer writer) {
        return new CSVWriter(writer, this.separator, this.quotechar, this.escapechar, this.lineEnd);
    }
    
    public CSVWriter writer(final OutputStream os) {
        return this.writer(new OutputStreamWriter(os, this.charset));
    }
    
    public CSVWriter writer(final File file) {
        try {
            return this.writer(new FileOutputStream(file));
        }
        catch (FileNotFoundException e) {
            throw new CSVRuntimeException(e);
        }
    }
    
    public CSVWriter writer(final String fileName) {
        return this.writer(new File(fileName));
    }
    
    public void write(final Writer writer, final CSVWriteProc proc) {
        this.write(this.writer(writer), proc);
    }
    
    public void write(final OutputStream os, final CSVWriteProc proc) {
        this.write(this.writer(os), proc);
    }
    
    public void write(final String fileName, final CSVWriteProc proc) {
        this.write(new File(fileName), proc);
    }
    
    public void write(final CSVWriter writer, final CSVWriteProc proc) {
        try {
            writer.write(proc);
            writer.flush();
        }
        catch (IOException e) {
            throw new CSVRuntimeException(e);
        }
    }
    
    public void write(final File file, final CSVWriteProc proc) {
        this.writeAndClose(this.writer(file), proc);
    }
    
    public void writeAndClose(final Writer writer, final CSVWriteProc proc) {
        this.writeAndClose(this.writer(writer), proc);
    }
    
    public void writeAndClose(final OutputStream os, final CSVWriteProc proc) {
        this.writeAndClose(this.writer(os), proc);
    }
    
    public void writeAndClose(final CSVWriter writer, final CSVWriteProc proc) {
        try {
            writer.write(proc);
        }
        catch (RuntimeException re) {
            try {
                writer.close();
            }
            catch (Exception ex) {}
            throw re;
        }
        try {
            writer.close();
        }
        catch (Exception e) {
            throw new CSVRuntimeException(e);
        }
    }
    
    public CSVReader reader(final Reader reader) {
        return new CSVReader(reader, this.separator, this.quotechar, this.escapechar, this.skipLines, this.strictQuotes, this.ignoreLeadingWhiteSpace);
    }
    
    public CSVReader reader(final InputStream is) {
        return this.reader(new InputStreamReader(is, this.charset));
    }
    
    public CSVReader reader(final File file) {
        try {
            return this.reader(new FileInputStream(file));
        }
        catch (IOException e) {
            throw new CSVRuntimeException(e);
        }
    }
    
    public CSVReader reader(final String fileName) {
        return this.reader(new File(fileName));
    }
    
    public void read(final InputStream is, final CSVReadProc proc) {
        this.read(this.reader(is), proc);
    }
    
    public void read(final Reader reader, final CSVReadProc proc) {
        this.read(this.reader(reader), proc);
    }
    
    public void read(final File file, final CSVReadProc proc) {
        this.readAndClose(this.reader(file), proc);
    }
    
    public void read(final String fileName, final CSVReadProc proc) {
        this.read(new File(fileName), proc);
    }
    
    public void read(final CSVReader reader, final CSVReadProc proc) {
        reader.read(proc);
    }
    
    public void readAndClose(final InputStream is, final CSVReadProc proc) {
        this.readAndClose(this.reader(is), proc);
    }
    
    public void readAndClose(final Reader reader, final CSVReadProc proc) {
        this.readAndClose(this.reader(reader), proc);
    }
    
    public void readAndClose(final CSVReader reader, final CSVReadProc proc) {
        try {
            this.read(reader, proc);
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException ex) {}
        }
        try {
            reader.close();
        }
        catch (IOException ex2) {}
    }
    
    public static CSV create() {
        return new CSV();
    }
    
    public static Builder separator(final char separator) {
        return new Builder((Builder)null).separator(separator);
    }
    
    public static Builder quote(final char quoteChar) {
        return new Builder((Builder)null).quote(quoteChar);
    }
    
    public static Builder noQuote() {
        return new Builder((Builder)null).noQuote();
    }
    
    public static Builder escape(final char escapeChar) {
        return new Builder((Builder)null).escape(escapeChar);
    }
    
    public static Builder noEscape() {
        return new Builder((Builder)null).noEscape();
    }
    
    public static Builder lineEnd(final String lineEnd) {
        return new Builder((Builder)null).lineEnd(lineEnd);
    }
    
    public static Builder skipLines(final int skipLines) {
        return new Builder((Builder)null).skipLines(skipLines);
    }
    
    public static Builder strictQuotes() {
        return new Builder((Builder)null).strictQuotes();
    }
    
    public static Builder notStrictQuotes() {
        return new Builder((Builder)null).notStrictQuotes();
    }
    
    public static Builder ignoreLeadingWhiteSpace() {
        return new Builder((Builder)null).ignoreLeadingWhiteSpace();
    }
    
    public static Builder notIgnoreLeadingWhiteSpace() {
        return new Builder((Builder)null).notIgnoreLeadingWhiteSpace();
    }
    
    public static Builder charset(final Charset charset) {
        return new Builder((Builder)null).charset(charset);
    }
    
    public static Builder charset(final String charsetName) {
        return new Builder((Builder)null).charset(charsetName);
    }
    
    public static class Builder
    {
        private final CSV csv;
        
        private Builder() {
            this.csv = new CSV(null);
        }
        
        private Builder(final CSV csv) {
            this.csv = csv;
        }
        
        public CSV create() {
            return this.csv;
        }
        
        public Builder separator(final char separator) {
            return new Builder(new CSV(separator, this.csv.quotechar, this.csv.escapechar, this.csv.lineEnd, this.csv.skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        public Builder quote(final char quoteChar) {
            return new Builder(new CSV(this.csv.separator, quoteChar, this.csv.escapechar, this.csv.lineEnd, this.csv.skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        public Builder escape(final char escapeChar) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, escapeChar, this.csv.lineEnd, this.csv.skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        public Builder lineEnd(final String lineEnd) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, this.csv.escapechar, lineEnd, this.csv.skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        public Builder skipLines(final int skipLines) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, this.csv.escapechar, this.csv.lineEnd, skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        private Builder setStrictQuotes(final boolean strictQuotes) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, this.csv.escapechar, this.csv.lineEnd, this.csv.skipLines, strictQuotes, this.csv.ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        private Builder setIgnoreLeadingWhiteSpace(final boolean ignoreLeadingWhiteSpace) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, this.csv.escapechar, this.csv.lineEnd, this.csv.skipLines, this.csv.strictQuotes, ignoreLeadingWhiteSpace, this.csv.charset, null));
        }
        
        public Builder charset(final Charset charset) {
            return new Builder(new CSV(this.csv.separator, this.csv.quotechar, this.csv.escapechar, this.csv.lineEnd, this.csv.skipLines, this.csv.strictQuotes, this.csv.ignoreLeadingWhiteSpace, charset, null));
        }
        
        public Builder noQuote() {
            return this.quote('\0');
        }
        
        public Builder noEscape() {
            return this.escape('\0');
        }
        
        public Builder strictQuotes() {
            return this.setStrictQuotes(true);
        }
        
        public Builder notStrictQuotes() {
            return this.setStrictQuotes(false);
        }
        
        public Builder ignoreLeadingWhiteSpace() {
            return this.setIgnoreLeadingWhiteSpace(true);
        }
        
        public Builder notIgnoreLeadingWhiteSpace() {
            return this.setIgnoreLeadingWhiteSpace(false);
        }
        
        public Builder charset(final String charsetName) {
            return this.charset(Charset.forName(charsetName));
        }
    }
}
