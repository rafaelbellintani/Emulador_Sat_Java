// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import java.util.Vector;
import java.io.ByteArrayOutputStream;

public final class Strings
{
    public static String fromUTF8ByteArray(final byte[] array) {
        int i = 0;
        int n = 0;
        while (i < array.length) {
            ++n;
            if ((array[i] & 0xF0) == 0xF0) {
                ++n;
                i += 4;
            }
            else if ((array[i] & 0xE0) == 0xE0) {
                i += 3;
            }
            else if ((array[i] & 0xC0) == 0xC0) {
                i += 2;
            }
            else {
                ++i;
            }
        }
        final char[] value = new char[n];
        int j = 0;
        int n2 = 0;
        while (j < array.length) {
            char c3;
            if ((array[j] & 0xF0) == 0xF0) {
                final int n3 = ((array[j] & 0x3) << 18 | (array[j + 1] & 0x3F) << 12 | (array[j + 2] & 0x3F) << 6 | (array[j + 3] & 0x3F)) - 65536;
                final char c = (char)(0xD800 | n3 >> 10);
                final char c2 = (char)(0xDC00 | (n3 & 0x3FF));
                value[n2++] = c;
                c3 = c2;
                j += 4;
            }
            else if ((array[j] & 0xE0) == 0xE0) {
                c3 = (char)((array[j] & 0xF) << 12 | (array[j + 1] & 0x3F) << 6 | (array[j + 2] & 0x3F));
                j += 3;
            }
            else if ((array[j] & 0xD0) == 0xD0) {
                c3 = (char)((array[j] & 0x1F) << 6 | (array[j + 1] & 0x3F));
                j += 2;
            }
            else if ((array[j] & 0xC0) == 0xC0) {
                c3 = (char)((array[j] & 0x1F) << 6 | (array[j + 1] & 0x3F));
                j += 2;
            }
            else {
                c3 = (char)(array[j] & 0xFF);
                ++j;
            }
            value[n2++] = c3;
        }
        return new String(value);
    }
    
    public static byte[] toUTF8ByteArray(final String s) {
        return toUTF8ByteArray(s.toCharArray());
    }
    
    public static byte[] toUTF8ByteArray(final char[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < array.length; ++i) {
            final char b = array[i];
            if (b < '\u0080') {
                byteArrayOutputStream.write(b);
            }
            else if (b < '\u0800') {
                byteArrayOutputStream.write(0xC0 | b >> 6);
                byteArrayOutputStream.write(0x80 | (b & '?'));
            }
            else if (b >= '\ud800' && b <= '\udfff') {
                if (i + 1 >= array.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                final char c = b;
                final char c2 = array[++i];
                if (c > '\udbff') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                final int n = ((c & '\u03ff') << 10 | (c2 & '\u03ff')) + 65536;
                byteArrayOutputStream.write(0xF0 | n >> 18);
                byteArrayOutputStream.write(0x80 | (n >> 12 & 0x3F));
                byteArrayOutputStream.write(0x80 | (n >> 6 & 0x3F));
                byteArrayOutputStream.write(0x80 | (n & 0x3F));
            }
            else {
                byteArrayOutputStream.write(0xE0 | b >> 12);
                byteArrayOutputStream.write(0x80 | (b >> 6 & 0x3F));
                byteArrayOutputStream.write(0x80 | (b & '?'));
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static String toUpperCase(final String s) {
        boolean b = false;
        final char[] charArray = s.toCharArray();
        for (int i = 0; i != charArray.length; ++i) {
            final char c = charArray[i];
            if ('a' <= c && 'z' >= c) {
                b = true;
                charArray[i] = (char)(c - 'a' + 65);
            }
        }
        if (b) {
            return new String(charArray);
        }
        return s;
    }
    
    public static String toLowerCase(final String s) {
        boolean b = false;
        final char[] charArray = s.toCharArray();
        for (int i = 0; i != charArray.length; ++i) {
            final char c = charArray[i];
            if ('A' <= c && 'Z' >= c) {
                b = true;
                charArray[i] = (char)(c - 'A' + 97);
            }
        }
        if (b) {
            return new String(charArray);
        }
        return s;
    }
    
    public static byte[] toByteArray(final String s) {
        final byte[] array = new byte[s.length()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = (byte)s.charAt(i);
        }
        return array;
    }
    
    public static String[] split(String substring, final char ch) {
        final Vector<String> vector = new Vector<String>();
        int i = 1;
        while (i != 0) {
            final int index = substring.indexOf(ch);
            if (index > 0) {
                vector.addElement(substring.substring(0, index));
                substring = substring.substring(index + 1);
            }
            else {
                i = 0;
                vector.addElement(substring);
            }
        }
        final String[] array = new String[vector.size()];
        for (int j = 0; j != array.length; ++j) {
            array[j] = vector.elementAt(j);
        }
        return array;
    }
}
