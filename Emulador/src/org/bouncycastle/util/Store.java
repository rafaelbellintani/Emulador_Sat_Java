// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import java.util.Collection;

public interface Store
{
    Collection getMatches(final Selector p0) throws StoreException;
}
