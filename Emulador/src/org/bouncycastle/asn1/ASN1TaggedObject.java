// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends ASN1Object implements ASN1TaggedObjectParser
{
    int tagNo;
    boolean empty;
    boolean explicit;
    DEREncodable obj;
    
    public static ASN1TaggedObject getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            return (ASN1TaggedObject)asn1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("implicitly tagged tagged object");
    }
    
    public static ASN1TaggedObject getInstance(final Object o) {
        if (o == null || o instanceof ASN1TaggedObject) {
            return (ASN1TaggedObject)o;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + o.getClass().getName());
    }
    
    public ASN1TaggedObject(final int tagNo, final DEREncodable obj) {
        this.empty = false;
        this.explicit = true;
        this.obj = null;
        this.explicit = true;
        this.tagNo = tagNo;
        this.obj = obj;
    }
    
    public ASN1TaggedObject(final boolean explicit, final int tagNo, final DEREncodable obj) {
        this.empty = false;
        this.explicit = true;
        this.obj = null;
        if (obj instanceof ASN1Choice) {
            this.explicit = true;
        }
        else {
            this.explicit = explicit;
        }
        this.tagNo = tagNo;
        this.obj = obj;
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        if (!(derObject instanceof ASN1TaggedObject)) {
            return false;
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)derObject;
        if (this.tagNo != asn1TaggedObject.tagNo || this.empty != asn1TaggedObject.empty || this.explicit != asn1TaggedObject.explicit) {
            return false;
        }
        if (this.obj == null) {
            if (asn1TaggedObject.obj != null) {
                return false;
            }
        }
        else if (!this.obj.getDERObject().equals(asn1TaggedObject.obj.getDERObject())) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int tagNo = this.tagNo;
        if (this.obj != null) {
            tagNo ^= this.obj.hashCode();
        }
        return tagNo;
    }
    
    @Override
    public int getTagNo() {
        return this.tagNo;
    }
    
    public boolean isExplicit() {
        return this.explicit;
    }
    
    public boolean isEmpty() {
        return this.empty;
    }
    
    public DERObject getObject() {
        if (this.obj != null) {
            return this.obj.getDERObject();
        }
        return null;
    }
    
    @Override
    public DEREncodable getObjectParser(final int i, final boolean b) {
        switch (i) {
            case 17: {
                return ASN1Set.getInstance(this, b).parser();
            }
            case 16: {
                return ASN1Sequence.getInstance(this, b).parser();
            }
            case 4: {
                return ASN1OctetString.getInstance(this, b).parser();
            }
            default: {
                if (b) {
                    return this.getObject();
                }
                throw new RuntimeException("implicit tagging not implemented for tag: " + i);
            }
        }
    }
    
    @Override
    abstract void encode(final DEROutputStream p0) throws IOException;
    
    @Override
    public String toString() {
        return "[" + this.tagNo + "]" + this.obj;
    }
}
