// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubnetUtils
{
    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final String SLASH_FORMAT = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})";
    private static final Pattern addressPattern;
    private static final Pattern cidrPattern;
    private static final int NBITS = 32;
    private int netmask;
    private int address;
    private int network;
    private int broadcast;
    
    public SubnetUtils(final String cidrNotation) {
        this.netmask = 0;
        this.address = 0;
        this.network = 0;
        this.broadcast = 0;
        this.calculate(cidrNotation);
    }
    
    public SubnetUtils(final String address, final String mask) {
        this.netmask = 0;
        this.address = 0;
        this.network = 0;
        this.broadcast = 0;
        this.calculate(this.toCidrNotation(address, mask));
    }
    
    public final SubnetInfo getInfo() {
        return new SubnetInfo();
    }
    
    private void calculate(final String mask) {
        final Matcher matcher = SubnetUtils.cidrPattern.matcher(mask);
        if (matcher.matches()) {
            this.address = this.matchAddress(matcher);
            for (int cidrPart = this.rangeCheck(Integer.parseInt(matcher.group(5)), 0, 31), j = 0; j < cidrPart; ++j) {
                this.netmask |= 1 << 31 - j;
            }
            this.network = (this.address & this.netmask);
            this.broadcast = (this.network | ~this.netmask);
            return;
        }
        throw new IllegalArgumentException("Could not parse [" + mask + "]");
    }
    
    private int toInteger(final String address) {
        final Matcher matcher = SubnetUtils.addressPattern.matcher(address);
        if (matcher.matches()) {
            return this.matchAddress(matcher);
        }
        throw new IllegalArgumentException("Could not parse [" + address + "]");
    }
    
    private int matchAddress(final Matcher matcher) {
        int addr = 0;
        for (int i = 1; i <= 4; ++i) {
            final int n = this.rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255);
            addr |= (n & 0xFF) << 8 * (4 - i);
        }
        return addr;
    }
    
    private int[] toArray(final int val) {
        final int[] ret = new int[4];
        for (int j = 3; j >= 0; --j) {
            final int[] array = ret;
            final int n = j;
            array[n] |= (val >>> 8 * (3 - j) & 0xFF);
        }
        return ret;
    }
    
    private String format(final int[] octets) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < octets.length; ++i) {
            str.append(octets[i]);
            if (i != octets.length - 1) {
                str.append(".");
            }
        }
        return str.toString();
    }
    
    private int rangeCheck(final int value, final int begin, final int end) {
        if (value >= begin && value <= end) {
            return value;
        }
        throw new IllegalArgumentException("Value out of range: [" + value + "]");
    }
    
    int pop(int x) {
        x -= (x >>> 1 & 0x55555555);
        x = (x & 0x33333333) + (x >>> 2 & 0x33333333);
        x = (x + (x >>> 4) & 0xF0F0F0F);
        x += x >>> 8;
        x += x >>> 16;
        return x & 0x3F;
    }
    
    private String toCidrNotation(final String addr, final String mask) {
        return addr + "/" + this.pop(this.toInteger(mask));
    }
    
    static {
        addressPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
        cidrPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})");
    }
    
    public final class SubnetInfo
    {
        private SubnetInfo() {
        }
        
        private int netmask() {
            return SubnetUtils.this.netmask;
        }
        
        private int network() {
            return SubnetUtils.this.network;
        }
        
        private int address() {
            return SubnetUtils.this.address;
        }
        
        private int broadcast() {
            return SubnetUtils.this.broadcast;
        }
        
        private int low() {
            return this.network() + 1;
        }
        
        private int high() {
            return this.broadcast() - 1;
        }
        
        public boolean isInRange(final String address) {
            return this.isInRange(SubnetUtils.this.toInteger(address));
        }
        
        private boolean isInRange(final int address) {
            return address - this.low() <= this.high() - this.low();
        }
        
        public String getBroadcastAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.broadcast()));
        }
        
        public String getNetworkAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.network()));
        }
        
        public String getNetmask() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.netmask()));
        }
        
        public String getAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.address()));
        }
        
        public String getLowAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.low()));
        }
        
        public String getHighAddress() {
            return SubnetUtils.this.format(SubnetUtils.this.toArray(this.high()));
        }
        
        public int getAddressCount() {
            return this.broadcast() - this.low();
        }
        
        public int asInteger(final String address) {
            return SubnetUtils.this.toInteger(address);
        }
        
        public String getCidrSignature() {
            return SubnetUtils.this.toCidrNotation(SubnetUtils.this.format(SubnetUtils.this.toArray(this.address())), SubnetUtils.this.format(SubnetUtils.this.toArray(this.netmask())));
        }
        
        public String[] getAllAddresses() {
            final String[] addresses = new String[this.getAddressCount()];
            for (int add = this.low(), j = 0; add <= this.high(); ++add, ++j) {
                addresses[j] = SubnetUtils.this.format(SubnetUtils.this.toArray(add));
            }
            return addresses;
        }
    }
}
