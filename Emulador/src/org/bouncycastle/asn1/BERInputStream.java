// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.EOFException;
import java.util.Vector;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BERInputStream extends DERInputStream
{
    private static final DERObject END_OF_STREAM;
    
    public BERInputStream(final InputStream inputStream) {
        super(inputStream);
    }
    
    private byte[] readIndefiniteLengthFully() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read2;
        for (int read = this.read(); (read2 = this.read()) >= 0 && (read != 0 || read2 != 0); read = read2) {
            byteArrayOutputStream.write(read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    private BERConstructedOctetString buildConstructedOctetString() throws IOException {
        final Vector<DERObject> vector = new Vector<DERObject>();
        while (true) {
            final DERObject object = this.readObject();
            if (object == BERInputStream.END_OF_STREAM) {
                break;
            }
            vector.addElement(object);
        }
        return new BERConstructedOctetString(vector);
    }
    
    @Override
    public DERObject readObject() throws IOException {
        final int read = this.read();
        if (read == -1) {
            throw new EOFException();
        }
        final int length = this.readLength();
        if (length < 0) {
            switch (read) {
                case 5: {
                    return null;
                }
                case 48: {
                    final BERConstructedSequence berConstructedSequence = new BERConstructedSequence();
                    while (true) {
                        final DERObject object = this.readObject();
                        if (object == BERInputStream.END_OF_STREAM) {
                            break;
                        }
                        berConstructedSequence.addObject(object);
                    }
                    return berConstructedSequence;
                }
                case 36: {
                    return this.buildConstructedOctetString();
                }
                case 49: {
                    final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                    while (true) {
                        final DERObject object2 = this.readObject();
                        if (object2 == BERInputStream.END_OF_STREAM) {
                            break;
                        }
                        asn1EncodableVector.add(object2);
                    }
                    return new BERSet(asn1EncodableVector);
                }
                default: {
                    if ((read & 0x80) == 0x0) {
                        throw new IOException("unknown BER object encountered");
                    }
                    if ((read & 0x1F) == 0x1F) {
                        throw new IOException("unsupported high tag encountered");
                    }
                    if ((read & 0x20) == 0x0) {
                        return new BERTaggedObject(false, read & 0x1F, new DEROctetString(this.readIndefiniteLengthFully()));
                    }
                    final DERObject object3 = this.readObject();
                    if (object3 == BERInputStream.END_OF_STREAM) {
                        return new DERTaggedObject(read & 0x1F);
                    }
                    DERObject derObject = this.readObject();
                    if (derObject == BERInputStream.END_OF_STREAM) {
                        return new BERTaggedObject(read & 0x1F, object3);
                    }
                    final BERConstructedSequence berConstructedSequence2 = new BERConstructedSequence();
                    berConstructedSequence2.addObject(object3);
                    do {
                        berConstructedSequence2.addObject(derObject);
                        derObject = this.readObject();
                    } while (derObject != BERInputStream.END_OF_STREAM);
                    return new BERTaggedObject(false, read & 0x1F, berConstructedSequence2);
                }
            }
        }
        else {
            if (read == 0 && length == 0) {
                return BERInputStream.END_OF_STREAM;
            }
            final byte[] array = new byte[length];
            this.readFully(array);
            return this.buildObject(read, array);
        }
    }
    
    static {
        END_OF_STREAM = new DERObject() {
            @Override
            void encode(final DEROutputStream derOutputStream) throws IOException {
                throw new IOException("Eeek!");
            }
            
            @Override
            public int hashCode() {
                return 0;
            }
            
            @Override
            public boolean equals(final Object o) {
                return o == this;
            }
        };
    }
}
