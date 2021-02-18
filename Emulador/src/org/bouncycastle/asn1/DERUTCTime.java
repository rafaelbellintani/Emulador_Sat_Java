// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.TimeZone;
import java.util.SimpleTimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class DERUTCTime extends ASN1Object
{
    String time;
    
    public static DERUTCTime getInstance(final Object o) {
        if (o == null || o instanceof DERUTCTime) {
            return (DERUTCTime)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERUTCTime(((ASN1OctetString)o).getOctets());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERUTCTime getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERUTCTime(final String time) {
        this.time = time;
        try {
            this.getDate();
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("invalid date string: " + ex.getMessage());
        }
    }
    
    public DERUTCTime(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = simpleDateFormat.format(date);
    }
    
    DERUTCTime(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.time = new String(value);
    }
    
    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyMMddHHmmssz").parse(this.getTime());
    }
    
    public Date getAdjustedDate() throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        return simpleDateFormat.parse(this.getAdjustedTime());
    }
    
    public String getTime() {
        if (this.time.indexOf(45) < 0 && this.time.indexOf(43) < 0) {
            if (this.time.length() == 11) {
                return this.time.substring(0, 10) + "00GMT+00:00";
            }
            return this.time.substring(0, 12) + "GMT+00:00";
        }
        else {
            int n = this.time.indexOf(45);
            if (n < 0) {
                n = this.time.indexOf(43);
            }
            String str = this.time;
            if (n == this.time.length() - 3) {
                str += "00";
            }
            if (n == 10) {
                return str.substring(0, 10) + "00GMT" + str.substring(10, 13) + ":" + str.substring(13, 15);
            }
            return str.substring(0, 12) + "GMT" + str.substring(12, 15) + ":" + str.substring(15, 17);
        }
    }
    
    public String getAdjustedTime() {
        final String time = this.getTime();
        if (time.charAt(0) < '5') {
            return "20" + time;
        }
        return "19" + time;
    }
    
    private byte[] getOctets() {
        final char[] charArray = this.time.toCharArray();
        final byte[] array = new byte[charArray.length];
        for (int i = 0; i != charArray.length; ++i) {
            array[i] = (byte)charArray[i];
        }
        return array;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(23, this.getOctets());
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERUTCTime && this.time.equals(((DERUTCTime)derObject).time);
    }
    
    @Override
    public int hashCode() {
        return this.time.hashCode();
    }
    
    @Override
    public String toString() {
        return this.time;
    }
}
