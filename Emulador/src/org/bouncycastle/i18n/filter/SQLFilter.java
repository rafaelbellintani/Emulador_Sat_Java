// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.i18n.filter;

public class SQLFilter implements Filter
{
    @Override
    public String doFilter(final String str) {
        final StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < sb.length(); ++i) {
            switch (sb.charAt(i)) {
                case '\'': {
                    sb.replace(i, i + 1, "\\'");
                    ++i;
                    break;
                }
                case '\"': {
                    sb.replace(i, i + 1, "\\\"");
                    ++i;
                    break;
                }
                case '=': {
                    sb.replace(i, i + 1, "\\=");
                    ++i;
                    break;
                }
                case '-': {
                    sb.replace(i, i + 1, "\\-");
                    ++i;
                    break;
                }
                case '/': {
                    sb.replace(i, i + 1, "\\/");
                    ++i;
                    break;
                }
                case '\\': {
                    sb.replace(i, i + 1, "\\\\");
                    ++i;
                    break;
                }
                case ';': {
                    sb.replace(i, i + 1, "\\;");
                    ++i;
                    break;
                }
                case '\r': {
                    sb.replace(i, i + 1, "\\r");
                    ++i;
                    break;
                }
                case '\n': {
                    sb.replace(i, i + 1, "\\n");
                    ++i;
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    @Override
    public String doFilterUrl(final String s) {
        return this.doFilter(s);
    }
}
