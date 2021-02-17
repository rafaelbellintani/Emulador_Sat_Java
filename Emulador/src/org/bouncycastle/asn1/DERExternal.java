// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class DERExternal extends ASN1Object
{
    private DERObjectIdentifier directReference;
    private DERInteger indirectReference;
    private ASN1Object dataValueDescriptor;
    private int encoding;
    private DERObject externalContent;
    
    public DERExternal(final ASN1EncodableVector asn1EncodableVector) {
        int n = 0;
        DERObject derObject = asn1EncodableVector.get(n).getDERObject();
        if (derObject instanceof DERObjectIdentifier) {
            this.directReference = (DERObjectIdentifier)derObject;
            ++n;
            derObject = asn1EncodableVector.get(n).getDERObject();
        }
        if (derObject instanceof DERInteger) {
            this.indirectReference = (DERInteger)derObject;
            ++n;
            derObject = asn1EncodableVector.get(n).getDERObject();
        }
        if (!(derObject instanceof DERTaggedObject)) {
            this.dataValueDescriptor = (DERInteger)derObject;
            ++n;
            derObject = asn1EncodableVector.get(n).getDERObject();
        }
        if (!(derObject instanceof DERTaggedObject)) {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        }
        final DERTaggedObject derTaggedObject = (DERTaggedObject)derObject;
        this.setEncoding(derTaggedObject.getTagNo());
        this.externalContent = derTaggedObject.getObject();
    }
    
    public DERExternal(final DERObjectIdentifier derObjectIdentifier, final DERInteger derInteger, final ASN1Object asn1Object, final DERTaggedObject derTaggedObject) {
        this(derObjectIdentifier, derInteger, asn1Object, derTaggedObject.getTagNo(), derTaggedObject.getDERObject());
    }
    
    public DERExternal(final DERObjectIdentifier directReference, final DERInteger indirectReference, final ASN1Object dataValueDescriptor, final int encoding, final DERObject derObject) {
        this.setDirectReference(directReference);
        this.setIndirectReference(indirectReference);
        this.setDataValueDescriptor(dataValueDescriptor);
        this.setEncoding(encoding);
        this.setExternalContent(derObject.getDERObject());
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (this.directReference != null) {
            hashCode = this.directReference.hashCode();
        }
        if (this.indirectReference != null) {
            hashCode ^= this.indirectReference.hashCode();
        }
        if (this.dataValueDescriptor != null) {
            hashCode ^= this.dataValueDescriptor.hashCode();
        }
        return hashCode ^ this.externalContent.hashCode();
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.directReference != null) {
            byteArrayOutputStream.write(this.directReference.getDEREncoded());
        }
        if (this.indirectReference != null) {
            byteArrayOutputStream.write(this.indirectReference.getDEREncoded());
        }
        if (this.dataValueDescriptor != null) {
            byteArrayOutputStream.write(this.dataValueDescriptor.getDEREncoded());
        }
        byteArrayOutputStream.write(new DERTaggedObject(this.encoding, this.externalContent).getDEREncoded());
        derOutputStream.writeEncoded(32, 8, byteArrayOutputStream.toByteArray());
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        if (!(derObject instanceof DERExternal)) {
            return false;
        }
        if (this == derObject) {
            return true;
        }
        final DERExternal derExternal = (DERExternal)derObject;
        return (this.directReference == null || (derExternal.directReference != null && derExternal.directReference.equals(this.directReference))) && (this.indirectReference == null || (derExternal.indirectReference != null && derExternal.indirectReference.equals(this.indirectReference))) && (this.dataValueDescriptor == null || (derExternal.dataValueDescriptor != null && derExternal.dataValueDescriptor.equals(this.dataValueDescriptor))) && this.externalContent.equals(derExternal.externalContent);
    }
    
    public ASN1Object getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }
    
    public DERObjectIdentifier getDirectReference() {
        return this.directReference;
    }
    
    public int getEncoding() {
        return this.encoding;
    }
    
    public DERObject getExternalContent() {
        return this.externalContent;
    }
    
    public DERInteger getIndirectReference() {
        return this.indirectReference;
    }
    
    private void setDataValueDescriptor(final ASN1Object dataValueDescriptor) {
        this.dataValueDescriptor = dataValueDescriptor;
    }
    
    private void setDirectReference(final DERObjectIdentifier directReference) {
        this.directReference = directReference;
    }
    
    private void setEncoding(final int n) {
        if (n < 0 || n > 2) {
            throw new IllegalArgumentException("invalid encoding value: " + n);
        }
        this.encoding = n;
    }
    
    private void setExternalContent(final DERObject externalContent) {
        this.externalContent = externalContent;
    }
    
    private void setIndirectReference(final DERInteger indirectReference) {
        this.indirectReference = indirectReference;
    }
}
