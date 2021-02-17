// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionStore implements Store
{
    private Collection _local;
    
    public CollectionStore(final Collection c) {
        this._local = new ArrayList(c);
    }
    
    @Override
    public Collection getMatches(final Selector selector) {
        if (selector == null) {
            return new ArrayList(this._local);
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final Object next : this._local) {
            if (selector.match(next)) {
                list.add(next);
            }
        }
        return list;
    }
}
