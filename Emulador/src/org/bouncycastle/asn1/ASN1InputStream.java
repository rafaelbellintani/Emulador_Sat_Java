// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.EOFException;
import org.bouncycastle.util.io.Streams;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FilterInputStream;

public class ASN1InputStream extends FilterInputStream implements DERTags
{
    private final int limit;
    private final boolean lazyEvaluate;
    
    public ASN1InputStream(final InputStream inputStream) {
        this(inputStream, Integer.MAX_VALUE);
    }
    
    public ASN1InputStream(final byte[] buf) {
        this(new ByteArrayInputStream(buf), buf.length);
    }
    
    public ASN1InputStream(final byte[] buf, final boolean b) {
        this(new ByteArrayInputStream(buf), buf.length, b);
    }
    
    public ASN1InputStream(final InputStream inputStream, final int n) {
        this(inputStream, n, false);
    }
    
    public ASN1InputStream(final InputStream in, final int limit, final boolean lazyEvaluate) {
        super(in);
        this.limit = limit;
        this.lazyEvaluate = lazyEvaluate;
    }
    
    protected int readLength() throws IOException {
        return readLength(this, this.limit);
    }
    
    protected void readFully(final byte[] array) throws IOException {
        if (Streams.readFully(this, array) != array.length) {
            throw new EOFException("EOF encountered in middle of object");
        }
    }
    
    protected DERObject buildObject(final int n, final int n2, final int n3) throws IOException {
        final boolean b = (n & 0x20) != 0x0;
        final DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, n3);
        if ((n & 0x40) != 0x0) {
            return new DERApplicationSpecific(b, n2, definiteLengthInputStream.toByteArray());
        }
        if ((n & 0x80) != 0x0) {
            return new BERTaggedObjectParser(n, n2, definiteLengthInputStream).getDERObject();
        }
        if (!b) {
            return createPrimitiveDERObject(n2, definiteLengthInputStream.toByteArray());
        }
        switch (n2) {
            case 4: {
                return new BERConstructedOctetString(this.buildDEREncodableVector(definiteLengthInputStream).v);
            }
            case 16: {
                if (this.lazyEvaluate) {
                    return new LazyDERSequence(definiteLengthInputStream.toByteArray());
                }
                return DERFactory.createSequence(this.buildDEREncodableVector(definiteLengthInputStream));
            }
            case 17: {
                return DERFactory.createSet(this.buildDEREncodableVector(definiteLengthInputStream), false);
            }
            case 8: {
                return new DERExternal(this.buildDEREncodableVector(definiteLengthInputStream));
            }
            default: {
                return new DERUnknownTag(true, n2, definiteLengthInputStream.toByteArray());
            }
        }
    }
    
    ASN1EncodableVector buildEncodableVector() throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        DERObject object;
        while ((object = this.readObject()) != null) {
            asn1EncodableVector.add(object);
        }
        return asn1EncodableVector;
    }
    
    ASN1EncodableVector buildDEREncodableVector(final DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        return new ASN1InputStream(definiteLengthInputStream).buildEncodableVector();
    }
    
    public DERObject readObject() throws IOException {
        final int read = this.read();
        if (read <= 0) {
            if (read == 0) {
                throw new IOException("unexpected end-of-contents marker");
            }
            return null;
        }
        else {
            final int tagNumber = readTagNumber(this, read);
            final boolean b = (read & 0x20) != 0x0;
            final int length = this.readLength();
            if (length >= 0) {
                return this.buildObject(read, tagNumber, length);
            }
            if (!b) {
                throw new IOException("indefinite length primitive encoding encountered");
            }
            final IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this);
            if ((read & 0x40) != 0x0) {
                return new BERApplicationSpecificParser(tagNumber, new ASN1StreamParser(indefiniteLengthInputStream, this.limit)).getDERObject();
            }
            if ((read & 0x80) != 0x0) {
                return new BERTaggedObjectParser(read, tagNumber, indefiniteLengthInputStream).getDERObject();
            }
            final ASN1StreamParser asn1StreamParser = new ASN1StreamParser(indefiniteLengthInputStream, this.limit);
            switch (tagNumber) {
                case 4: {
                    return new BEROctetStringParser(asn1StreamParser).getDERObject();
                }
                case 16: {
                    return new BERSequenceParser(asn1StreamParser).getDERObject();
                }
                case 17: {
                    return new BERSetParser(asn1StreamParser).getDERObject();
                }
                case 8: {
                    return new DERExternalParser(asn1StreamParser).getDERObject();
                }
                default: {
                    throw new IOException("unknown BER object encountered");
                }
            }
        }
    }
    
    static int readTagNumber(final InputStream inputStream, final int n) throws IOException {
        int n2 = n & 0x1F;
        if (n2 == 31) {
            int n3 = 0;
            int n4 = inputStream.read();
            if ((n4 & 0x7F) == 0x0) {
                throw new IOException("corrupted stream - invalid high tag number found");
            }
            while (n4 >= 0 && (n4 & 0x80) != 0x0) {
                n3 = (n3 | (n4 & 0x7F)) << 7;
                n4 = inputStream.read();
            }
            if (n4 < 0) {
                throw new EOFException("EOF found inside tag value.");
            }
            n2 = (n3 | (n4 & 0x7F));
        }
        return n2;
    }
    
    static int readLength(final InputStream inputStream, final int n) throws IOException {
        int read = inputStream.read();
        if (read < 0) {
            throw new EOFException("EOF found when length expected");
        }
        if (read == 128) {
            return -1;
        }
        if (read > 127) {
            final int i = read & 0x7F;
            if (i > 4) {
                throw new IOException("DER length more than 4 bytes: " + i);
            }
            read = 0;
            for (int j = 0; j < i; ++j) {
                final int read2 = inputStream.read();
                if (read2 < 0) {
                    throw new EOFException("EOF found reading length");
                }
                read = (read << 8) + read2;
            }
            if (read < 0) {
                throw new IOException("corrupted stream - negative length found");
            }
            if (read >= n) {
                throw new IOException("corrupted stream - out of bounds length found");
            }
        }
        return read;
    }
    
    static DERObject createPrimitiveDERObject(final int n, final byte[] array) {
        switch (n) {
            case 3: {
                final byte b = array[0];
                final byte[] array2 = new byte[array.length - 1];
                System.arraycopy(array, 1, array2, 0, array.length - 1);
                return new DERBitString(array2, b);
            }
            case 30: {
                return new DERBMPString(array);
            }
            case 1: {
                return new DERBoolean(array);
            }
            case 10: {
                return new DEREnumerated(array);
            }
            case 24: {
                return new DERGeneralizedTime(array);
            }
            case 27: {
                return new DERGeneralString(array);
            }
            case 22: {
                return new DERIA5String(array);
            }
            case 2: {
                return new DERInteger(array);
            }
            case 5: {
                return DERNull.INSTANCE;
            }
            case 18: {
                return new DERNumericString(array);
            }
            case 6: {
                return new DERObjectIdentifier(array);
            }
            case 4: {
                return new DEROctetString(array);
            }
            case 19: {
                return new DERPrintableString(array);
            }
            case 20: {
                return new DERT61String(array);
            }
            case 28: {
                return new DERUniversalString(array);
            }
            case 23: {
                return new DERUTCTime(array);
            }
            case 12: {
                return new DERUTF8String(array);
            }
            case 26: {
                return new DERVisibleString(array);
            }
            default: {
                return new DERUnknownTag(false, n, array);
            }
        }
    }
}
