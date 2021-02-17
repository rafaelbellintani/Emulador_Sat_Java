// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.or;

class DefaultRenderer implements ObjectRenderer
{
    public String doRender(final Object o) {
        return o.toString();
    }
}
