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

public class DERGeneralizedTime extends ASN1Object
{
    String time;
    
    public static DERGeneralizedTime getInstance(final Object o) {
        if (o == null || o instanceof DERGeneralizedTime) {
            return (DERGeneralizedTime)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERGeneralizedTime(((ASN1OctetString)o).getOctets());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERGeneralizedTime getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERGeneralizedTime(final String time) {
        this.time = time;
        try {
            this.getDate();
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("invalid date string: " + ex.getMessage());
        }
    }
    
    public DERGeneralizedTime(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = simpleDateFormat.format(date);
    }
    
    DERGeneralizedTime(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.time = new String(value);
    }
    
    public String getTimeString() {
        return this.time;
    }
    
    public String getTime() {
        if (this.time.charAt(this.time.length() - 1) == 'Z') {
            return this.time.substring(0, this.time.length() - 1) + "GMT+00:00";
        }
        final int beginIndex = this.time.length() - 5;
        final char char1 = this.time.charAt(beginIndex);
        if (char1 == '-' || char1 == '+') {
            return this.time.substring(0, beginIndex) + "GMT" + this.time.substring(beginIndex, beginIndex + 3) + ":" + this.time.substring(beginIndex + 3);
        }
        final int beginIndex2 = this.time.length() - 3;
        final char char2 = this.time.charAt(beginIndex2);
        if (char2 == '-' || char2 == '+') {
            return this.time.substring(0, beginIndex2) + "GMT" + this.time.substring(beginIndex2) + ":00";
        }
        return this.time + this.calculateGMTOffset();
    }
    
    private String calculateGMTOffset() {
        String str = "+";
        final TimeZone default1 = TimeZone.getDefault();
        int rawOffset = default1.getRawOffset();
        if (rawOffset < 0) {
            str = "-";
            rawOffset = -rawOffset;
        }
        int n = rawOffset / 3600000;
        final int n2 = (rawOffset - n * 60 * 60 * 1000) / 60000;
        try {
            if (default1.useDaylightTime() && default1.inDaylightTime(this.getDate())) {
                n += (str.equals("+") ? 1 : -1);
            }
        }
        catch (ParseException ex) {}
        return "GMT" + str + this.convert(n) + ":" + this.convert(n2);
    }
    
    private String convert(final int n) {
        if (n < 10) {
            return "0" + n;
        }
        return Integer.toString(n);
    }
    
    public Date getDate() throws ParseException {
        String source = this.time;
        SimpleDateFormat simpleDateFormat;
        if (this.time.endsWith("Z")) {
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
            }
            simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        }
        else if (this.time.indexOf(45) > 0 || this.time.indexOf(43) > 0) {
            source = this.getTime();
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSSz");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
            }
            simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        }
        else {
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            }
            simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
        }
        if (this.hasFractionalSeconds()) {
            String substring;
            int i;
            char char1;
            for (substring = source.substring(14), i = 1; i < substring.length(); ++i) {
                char1 = substring.charAt(i);
                if ('0' > char1) {
                    break;
                }
                if (char1 > '9') {
                    break;
                }
            }
            if (i - 1 > 3) {
                source = source.substring(0, 14) + (substring.substring(0, 4) + substring.substring(i));
            }
        }
        return simpleDateFormat.parse(source);
    }
    
    private boolean hasFractionalSeconds() {
        return this.time.indexOf(46) == 14;
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
        derOutputStream.writeEncoded(24, this.getOctets());
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERGeneralizedTime && this.time.equals(((DERGeneralizedTime)derObject).time);
    }
    
    @Override
    public int hashCode() {
        return this.time.hashCode();
    }
}
