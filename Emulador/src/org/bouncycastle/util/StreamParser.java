// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import java.util.Collection;

public interface StreamParser
{
    Object read() throws StreamParsingException;
    
    Collection readAll() throws StreamParsingException;
}
