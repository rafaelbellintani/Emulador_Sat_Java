// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class DERInputStream extends FilterInputStream implements DERTags
{
    @Deprecated
    public DERInputStream(final InputStream in) {
        super(in);
    }
    
    protected int readLength() throws IOException {
        int read = this.read();
        if (read < 0) {
            throw new IOException("EOF found when length expected");
        }
        if (read == 128) {
            return -1;
        }
        if (read > 127) {
            final int n = read & 0x7F;
            if (n > 4) {
                throw new IOException("DER length more than 4 bytes");
            }
            read = 0;
            for (int i = 0; i < n; ++i) {
                final int read2 = this.read();
                if (read2 < 0) {
                    throw new IOException("EOF found reading length");
                }
                read = (read << 8) + read2;
            }
            if (read < 0) {
                throw new IOException("corrupted stream - negative length found");
            }
        }
        return read;
    }
    
    protected void readFully(final byte[] b) throws IOException {
        int i = b.length;
        if (i == 0) {
            return;
        }
        while (i > 0) {
            final int read = this.read(b, b.length - i, i);
            if (read < 0) {
                throw new EOFException("unexpected end of stream");
            }
            i -= read;
        }
    }
    
    protected DERObject buildObject(final int n, final byte[] buf) throws IOException {
        switch (n) {
            case 5: {
                return null;
            }
            case 48: {
                final BERInputStream berInputStream = new BERInputStream(new ByteArrayInputStream(buf));
                final DERConstructedSequence derConstructedSequence = new DERConstructedSequence();
                try {
                    while (true) {
                        derConstructedSequence.addObject(berInputStream.readObject());
                    }
                }
                catch (EOFException ex) {
                    return derConstructedSequence;
                }
            }
            case 49: {
                final BERInputStream berInputStream2 = new BERInputStream(new ByteArrayInputStream(buf));
                final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                try {
                    while (true) {
                        asn1EncodableVector.add(berInputStream2.readObject());
                    }
                }
                catch (EOFException ex2) {
                    return new DERConstructedSet(asn1EncodableVector);
                }
            }
            case 1: {
                return new DERBoolean(buf);
            }
            case 2: {
                return new DERInteger(buf);
            }
            case 10: {
                return new DEREnumerated(buf);
            }
            case 6: {
                return new DERObjectIdentifier(buf);
            }
            case 3: {
                final byte b = buf[0];
                final byte[] array = new byte[buf.length - 1];
                System.arraycopy(buf, 1, array, 0, buf.length - 1);
                return new DERBitString(array, b);
            }
            case 12: {
                return new DERUTF8String(buf);
            }
            case 19: {
                return new DERPrintableString(buf);
            }
            case 22: {
                return new DERIA5String(buf);
            }
            case 20: {
                return new DERT61String(buf);
            }
            case 26: {
                return new DERVisibleString(buf);
            }
            case 28: {
                return new DERUniversalString(buf);
            }
            case 27: {
                return new DERGeneralString(buf);
            }
            case 30: {
                return new DERBMPString(buf);
            }
            case 4: {
                return new DEROctetString(buf);
            }
            case 23: {
                return new DERUTCTime(buf);
            }
            case 24: {
                return new DERGeneralizedTime(buf);
            }
            default: {
                if ((n & 0x80) != 0x0) {
                    if ((n & 0x1F) == 0x1F) {
                        throw new IOException("unsupported high tag encountered");
                    }
                    if (buf.length == 0) {
                        if ((n & 0x20) == 0x0) {
                            return new DERTaggedObject(false, n & 0x1F, new DERNull());
                        }
                        return new DERTaggedObject(false, n & 0x1F, new DERConstructedSequence());
                    }
                    else {
                        if ((n & 0x20) == 0x0) {
                            return new DERTaggedObject(false, n & 0x1F, new DEROctetString(buf));
                        }
                        final BERInputStream berInputStream3 = new BERInputStream(new ByteArrayInputStream(buf));
                        final DERObject object = berInputStream3.readObject();
                        if (berInputStream3.available() == 0) {
                            return new DERTaggedObject(n & 0x1F, object);
                        }
                        final DERConstructedSequence derConstructedSequence2 = new DERConstructedSequence();
                        derConstructedSequence2.addObject(object);
                        try {
                            while (true) {
                                derConstructedSequence2.addObject(berInputStream3.readObject());
                            }
                        }
                        catch (EOFException ex3) {
                            return new DERTaggedObject(false, n & 0x1F, derConstructedSequence2);
                        }
                    }
                }
                return new DERUnknownTag(n, buf);
            }
        }
    }
    
    public DERObject readObject() throws IOException {
        final int read = this.read();
        if (read == -1) {
            throw new EOFException();
        }
        final byte[] array = new byte[this.readLength()];
        this.readFully(array);
        return this.buildObject(read, array);
    }
}
