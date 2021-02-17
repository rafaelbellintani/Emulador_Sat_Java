// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import java.text.ParseException;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.Date;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class Time extends ASN1Encodable implements ASN1Choice
{
    DERObject time;
    
    public static Time getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public Time(final DERObject time) {
        if (!(time instanceof DERUTCTime) && !(time instanceof DERGeneralizedTime)) {
            throw new IllegalArgumentException("unknown object passed to Time");
        }
        this.time = time;
    }
    
    public Time(final Date date) {
        final SimpleTimeZone timeZone = new SimpleTimeZone(0, "Z");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(timeZone);
        final String string = simpleDateFormat.format(date) + "Z";
        final int int1 = Integer.parseInt(string.substring(0, 4));
        if (int1 < 1950 || int1 > 2049) {
            this.time = new DERGeneralizedTime(string);
        }
        else {
            this.time = new DERUTCTime(string.substring(2));
        }
    }
    
    public static Time getInstance(final Object o) {
        if (o instanceof Time) {
            return (Time)o;
        }
        if (o instanceof DERUTCTime) {
            return new Time((DERObject)o);
        }
        if (o instanceof DERGeneralizedTime) {
            return new Time((DERObject)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public String getTime() {
        if (this.time instanceof DERUTCTime) {
            return ((DERUTCTime)this.time).getAdjustedTime();
        }
        return ((DERGeneralizedTime)this.time).getTime();
    }
    
    public Date getDate() {
        try {
            if (this.time instanceof DERUTCTime) {
                return ((DERUTCTime)this.time).getAdjustedDate();
            }
            return ((DERGeneralizedTime)this.time).getDate();
        }
        catch (ParseException ex) {
            throw new IllegalStateException("invalid date string: " + ex.getMessage());
        }
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.time;
    }
}
