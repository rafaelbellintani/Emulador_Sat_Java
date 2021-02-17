// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp.parser;

import java.util.regex.PatternSyntaxException;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

public abstract class RegexFTPFileEntryParserImpl extends FTPFileEntryParserImpl
{
    private Pattern pattern;
    private MatchResult result;
    protected Matcher _matcher_;
    
    public RegexFTPFileEntryParserImpl(final String regex) {
        this.pattern = null;
        this.result = null;
        this._matcher_ = null;
        this.setRegex(regex);
    }
    
    public boolean matches(final String s) {
        this.result = null;
        this._matcher_ = this.pattern.matcher(s);
        if (this._matcher_.matches()) {
            this.result = this._matcher_.toMatchResult();
        }
        return null != this.result;
    }
    
    public int getGroupCnt() {
        if (this.result == null) {
            return 0;
        }
        return this.result.groupCount();
    }
    
    public String group(final int matchnum) {
        if (this.result == null) {
            return null;
        }
        return this.result.group(matchnum);
    }
    
    public String getGroupsAsString() {
        final StringBuffer b = new StringBuffer();
        for (int i = 1; i <= this.result.groupCount(); ++i) {
            b.append(i).append(") ").append(this.result.group(i)).append(System.getProperty("line.separator"));
        }
        return b.toString();
    }
    
    public boolean setRegex(final String regex) {
        try {
            this.pattern = Pattern.compile(regex);
        }
        catch (PatternSyntaxException pse) {
            throw new IllegalArgumentException("Unparseable regex supplied: " + regex);
        }
        return this.pattern != null;
    }
}
