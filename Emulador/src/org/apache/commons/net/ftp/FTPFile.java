// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.util.Calendar;
import java.io.Serializable;

public class FTPFile implements Serializable
{
    public static final int FILE_TYPE = 0;
    public static final int DIRECTORY_TYPE = 1;
    public static final int SYMBOLIC_LINK_TYPE = 2;
    public static final int UNKNOWN_TYPE = 3;
    public static final int USER_ACCESS = 0;
    public static final int GROUP_ACCESS = 1;
    public static final int WORLD_ACCESS = 2;
    public static final int READ_PERMISSION = 0;
    public static final int WRITE_PERMISSION = 1;
    public static final int EXECUTE_PERMISSION = 2;
    int _type;
    int _hardLinkCount;
    long _size;
    String _rawListing;
    String _user;
    String _group;
    String _name;
    String _link;
    Calendar _date;
    boolean[][] _permissions;
    
    public FTPFile() {
        this._permissions = new boolean[3][3];
        this._rawListing = null;
        this._type = 3;
        this._hardLinkCount = 0;
        this._size = 0L;
        this._user = null;
        this._group = null;
        this._date = null;
        this._name = null;
    }
    
    public void setRawListing(final String rawListing) {
        this._rawListing = rawListing;
    }
    
    public String getRawListing() {
        return this._rawListing;
    }
    
    public boolean isDirectory() {
        return this._type == 1;
    }
    
    public boolean isFile() {
        return this._type == 0;
    }
    
    public boolean isSymbolicLink() {
        return this._type == 2;
    }
    
    public boolean isUnknown() {
        return this._type == 3;
    }
    
    public void setType(final int type) {
        this._type = type;
    }
    
    public int getType() {
        return this._type;
    }
    
    public void setName(final String name) {
        this._name = name;
    }
    
    public String getName() {
        return this._name;
    }
    
    public void setSize(final long size) {
        this._size = size;
    }
    
    public long getSize() {
        return this._size;
    }
    
    public void setHardLinkCount(final int links) {
        this._hardLinkCount = links;
    }
    
    public int getHardLinkCount() {
        return this._hardLinkCount;
    }
    
    public void setGroup(final String group) {
        this._group = group;
    }
    
    public String getGroup() {
        return this._group;
    }
    
    public void setUser(final String user) {
        this._user = user;
    }
    
    public String getUser() {
        return this._user;
    }
    
    public void setLink(final String link) {
        this._link = link;
    }
    
    public String getLink() {
        return this._link;
    }
    
    public void setTimestamp(final Calendar date) {
        this._date = date;
    }
    
    public Calendar getTimestamp() {
        return this._date;
    }
    
    public void setPermission(final int access, final int permission, final boolean value) {
        this._permissions[access][permission] = value;
    }
    
    public boolean hasPermission(final int access, final int permission) {
        return this._permissions[access][permission];
    }
    
    @Override
    public String toString() {
        return this._rawListing;
    }
}
