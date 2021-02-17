// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.util.Enumeration;

class CommPortEnumerator implements Enumeration
{
    private CommPortIdentifier curEntry;
    
    public Object nextElement() {
        synchronized (CommPortIdentifier.lock) {
            if (this.curEntry != null) {
                this.curEntry = this.curEntry.next;
            }
            else {
                this.curEntry = CommPortIdentifier.masterIdList;
            }
        }
        return this.curEntry;
    }
    
    public boolean hasMoreElements() {
        synchronized (CommPortIdentifier.lock) {
            if (this.curEntry != null) {
                return this.curEntry.next != null;
            }
            return CommPortIdentifier.masterIdList != null;
        }
    }
}
