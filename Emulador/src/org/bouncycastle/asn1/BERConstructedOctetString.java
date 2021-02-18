// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.util.Enumeration;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.Vector;

public class BERConstructedOctetString extends DEROctetString
{
    private static final int MAX_LENGTH = 1000;
    private Vector octs;
    
    private static byte[] toBytes(final Vector vector) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i != vector.size(); ++i) {
            try {
                byteArrayOutputStream.write(vector.elementAt(i).getOctets());
            }
            catch (ClassCastException ex2) {
                throw new IllegalArgumentException(vector.elementAt(i).getClass().getName() + " found in input should only contain DEROctetString");
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("exception converting octets " + ex.toString());
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public BERConstructedOctetString(final byte[] array) {
        super(array);
    }
    
    public BERConstructedOctetString(final Vector octs) {
        super(toBytes(octs));
        this.octs = octs;
    }
    
    public BERConstructedOctetString(final DERObject derObject) {
        super(derObject);
    }
    
    public BERConstructedOctetString(final DEREncodable derEncodable) {
        super(derEncodable.getDERObject());
    }
    
    @Override
    public byte[] getOctets() {
        return this.string;
    }
    
    public Enumeration getObjects() {
        if (this.octs == null) {
            return this.generateOcts().elements();
        }
        return this.octs.elements();
    }
    
    private Vector generateOcts() {
        final Vector<DEROctetString> vector = new Vector<DEROctetString>();
        for (int i = 0; i < this.string.length; i += 1000) {
            int length;
            if (i + 1000 > this.string.length) {
                length = this.string.length;
            }
            else {
                length = i + 1000;
            }
            final byte[] array = new byte[length - i];
            System.arraycopy(this.string, i, array, 0, array.length);
            vector.addElement(new DEROctetString(array));
        }
        return vector;
    }
    
    public void encode(final DEROutputStream derOutputStream) throws IOException {
        if (derOutputStream instanceof ASN1OutputStream || derOutputStream instanceof BEROutputStream) {
            derOutputStream.write(36);
            derOutputStream.write(128);
            final Enumeration objects = this.getObjects();
            while (objects.hasMoreElements()) {
                derOutputStream.writeObject(objects.nextElement());
            }
            derOutputStream.write(0);
            derOutputStream.write(0);
        }
        else {
            super.encode(derOutputStream);
        }
    }
}
