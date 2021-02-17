// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.util.HashMap;
import java.util.HashSet;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.Strings;
import org.bouncycastle.crypto.Digest;
import java.util.Map;
import java.util.Set;

class JCEDigestUtil
{
    private static Set md5;
    private static Set sha1;
    private static Set sha224;
    private static Set sha256;
    private static Set sha384;
    private static Set sha512;
    private static Map oids;
    
    static Digest getDigest(String upperCase) {
        upperCase = Strings.toUpperCase(upperCase);
        if (JCEDigestUtil.sha1.contains(upperCase)) {
            return new SHA1Digest();
        }
        if (JCEDigestUtil.md5.contains(upperCase)) {
            return new MD5Digest();
        }
        if (JCEDigestUtil.sha224.contains(upperCase)) {
            return new SHA224Digest();
        }
        if (JCEDigestUtil.sha256.contains(upperCase)) {
            return new SHA256Digest();
        }
        if (JCEDigestUtil.sha384.contains(upperCase)) {
            return new SHA384Digest();
        }
        if (JCEDigestUtil.sha512.contains(upperCase)) {
            return new SHA512Digest();
        }
        return null;
    }
    
    static boolean isSameDigest(final String s, final String s2) {
        return (JCEDigestUtil.sha1.contains(s) && JCEDigestUtil.sha1.contains(s2)) || (JCEDigestUtil.sha224.contains(s) && JCEDigestUtil.sha224.contains(s2)) || (JCEDigestUtil.sha256.contains(s) && JCEDigestUtil.sha256.contains(s2)) || (JCEDigestUtil.sha384.contains(s) && JCEDigestUtil.sha384.contains(s2)) || (JCEDigestUtil.sha512.contains(s) && JCEDigestUtil.sha512.contains(s2)) || (JCEDigestUtil.md5.contains(s) && JCEDigestUtil.md5.contains(s2));
    }
    
    static DERObjectIdentifier getOID(final String s) {
        return JCEDigestUtil.oids.get(s);
    }
    
    static {
        JCEDigestUtil.md5 = new HashSet();
        JCEDigestUtil.sha1 = new HashSet();
        JCEDigestUtil.sha224 = new HashSet();
        JCEDigestUtil.sha256 = new HashSet();
        JCEDigestUtil.sha384 = new HashSet();
        JCEDigestUtil.sha512 = new HashSet();
        JCEDigestUtil.oids = new HashMap();
        JCEDigestUtil.md5.add("MD5");
        JCEDigestUtil.md5.add(PKCSObjectIdentifiers.md5.getId());
        JCEDigestUtil.sha1.add("SHA1");
        JCEDigestUtil.sha1.add("SHA-1");
        JCEDigestUtil.sha1.add(OIWObjectIdentifiers.idSHA1.getId());
        JCEDigestUtil.sha224.add("SHA224");
        JCEDigestUtil.sha224.add("SHA-224");
        JCEDigestUtil.sha224.add(NISTObjectIdentifiers.id_sha224.getId());
        JCEDigestUtil.sha256.add("SHA256");
        JCEDigestUtil.sha256.add("SHA-256");
        JCEDigestUtil.sha256.add(NISTObjectIdentifiers.id_sha256.getId());
        JCEDigestUtil.sha384.add("SHA384");
        JCEDigestUtil.sha384.add("SHA-384");
        JCEDigestUtil.sha384.add(NISTObjectIdentifiers.id_sha384.getId());
        JCEDigestUtil.sha512.add("SHA512");
        JCEDigestUtil.sha512.add("SHA-512");
        JCEDigestUtil.sha512.add(NISTObjectIdentifiers.id_sha512.getId());
        JCEDigestUtil.oids.put("MD5", PKCSObjectIdentifiers.md5);
        JCEDigestUtil.oids.put(PKCSObjectIdentifiers.md5.getId(), PKCSObjectIdentifiers.md5);
        JCEDigestUtil.oids.put("SHA1", OIWObjectIdentifiers.idSHA1);
        JCEDigestUtil.oids.put("SHA-1", OIWObjectIdentifiers.idSHA1);
        JCEDigestUtil.oids.put(OIWObjectIdentifiers.idSHA1.getId(), OIWObjectIdentifiers.idSHA1);
        JCEDigestUtil.oids.put("SHA224", NISTObjectIdentifiers.id_sha224);
        JCEDigestUtil.oids.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        JCEDigestUtil.oids.put(NISTObjectIdentifiers.id_sha224.getId(), NISTObjectIdentifiers.id_sha224);
        JCEDigestUtil.oids.put("SHA256", NISTObjectIdentifiers.id_sha256);
        JCEDigestUtil.oids.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        JCEDigestUtil.oids.put(NISTObjectIdentifiers.id_sha256.getId(), NISTObjectIdentifiers.id_sha256);
        JCEDigestUtil.oids.put("SHA384", NISTObjectIdentifiers.id_sha384);
        JCEDigestUtil.oids.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        JCEDigestUtil.oids.put(NISTObjectIdentifiers.id_sha384.getId(), NISTObjectIdentifiers.id_sha384);
        JCEDigestUtil.oids.put("SHA512", NISTObjectIdentifiers.id_sha512);
        JCEDigestUtil.oids.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        JCEDigestUtil.oids.put(NISTObjectIdentifiers.id_sha512.getId(), NISTObjectIdentifiers.id_sha512);
    }
}
