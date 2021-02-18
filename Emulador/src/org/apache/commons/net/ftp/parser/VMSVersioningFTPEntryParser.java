// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp.parser;

import java.util.regex.MatchResult;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.net.ftp.FTPClientConfig;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class VMSVersioningFTPEntryParser extends VMSFTPEntryParser
{
    private Matcher _preparse_matcher_;
    private Pattern _preparse_pattern_;
    private static final String PRE_PARSE_REGEX = "(.*);([0-9]+)\\s*.*";
    
    public VMSVersioningFTPEntryParser() {
        this((FTPClientConfig)null);
    }
    
    public VMSVersioningFTPEntryParser(final FTPClientConfig config) {
        this.configure(config);
        try {
            this._preparse_pattern_ = Pattern.compile("(.*);([0-9]+)\\s*.*");
        }
        catch (PatternSyntaxException pse) {
            throw new IllegalArgumentException("Unparseable regex supplied:  (.*);([0-9]+)\\s*.*");
        }
    }
    
    @Override
    public List<String> preParse(List<String> original) {
        original = super.preParse(original);
        final HashMap<String, NameVersion> existingEntries = new HashMap<String, NameVersion>();
        final ListIterator<String> iter = original.listIterator();
        while (iter.hasNext()) {
            final String entry = iter.next().trim();
            MatchResult result = null;
            this._preparse_matcher_ = this._preparse_pattern_.matcher(entry);
            if (this._preparse_matcher_.matches()) {
                result = this._preparse_matcher_.toMatchResult();
                final String name = result.group(1);
                final String version = result.group(2);
                final NameVersion nv = new NameVersion(name, version);
                final NameVersion existing = existingEntries.get(name);
                if (null != existing && nv.versionNumber < existing.versionNumber) {
                    iter.remove();
                }
                else {
                    existingEntries.put(name, nv);
                }
            }
        }
        while (iter.hasPrevious()) {
            final String entry = iter.previous().trim();
            MatchResult result = null;
            this._preparse_matcher_ = this._preparse_pattern_.matcher(entry);
            if (this._preparse_matcher_.matches()) {
                result = this._preparse_matcher_.toMatchResult();
                final String name = result.group(1);
                final String version = result.group(2);
                final NameVersion nv = new NameVersion(name, version);
                final NameVersion existing = existingEntries.get(name);
                if (null == existing || nv.versionNumber >= existing.versionNumber) {
                    continue;
                }
                iter.remove();
            }
        }
        return original;
    }
    
    @Override
    protected boolean isVersioning() {
        return true;
    }
    
    private static class NameVersion
    {
        String name;
        int versionNumber;
        
        NameVersion(final String name, final String vers) {
            this.name = name;
            this.versionNumber = Integer.parseInt(vers);
        }
    }
}
