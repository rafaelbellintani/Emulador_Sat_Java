// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class NamingAuthority extends ASN1Encodable
{
    public static final DERObjectIdentifier id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern;
    private DERObjectIdentifier namingAuthorityId;
    private String namingAuthorityUrl;
    private DirectoryString namingAuthorityText;
    
    public static NamingAuthority getInstance(final Object o) {
        if (o == null || o instanceof NamingAuthority) {
            return (NamingAuthority)o;
        }
        if (o instanceof ASN1Sequence) {
            return new NamingAuthority((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static NamingAuthority getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private NamingAuthority(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable = objects.nextElement();
            if (derEncodable instanceof DERObjectIdentifier) {
                this.namingAuthorityId = (DERObjectIdentifier)derEncodable;
            }
            else if (derEncodable instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(derEncodable).getString();
            }
            else {
                if (!(derEncodable instanceof DERString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + ((DERObjectIdentifier)derEncodable).getClass());
                }
                this.namingAuthorityText = DirectoryString.getInstance(derEncodable);
            }
        }
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable2 = objects.nextElement();
            if (derEncodable2 instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(derEncodable2).getString();
            }
            else {
                if (!(derEncodable2 instanceof DERString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + derEncodable2.getClass());
                }
                this.namingAuthorityText = DirectoryString.getInstance(derEncodable2);
            }
        }
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable3 = objects.nextElement();
            if (!(derEncodable3 instanceof DERString)) {
                throw new IllegalArgumentException("Bad object encountered: " + derEncodable3.getClass());
            }
            this.namingAuthorityText = DirectoryString.getInstance(derEncodable3);
        }
    }
    
    public DERObjectIdentifier getNamingAuthorityId() {
        return this.namingAuthorityId;
    }
    
    public DirectoryString getNamingAuthorityText() {
        return this.namingAuthorityText;
    }
    
    public String getNamingAuthorityUrl() {
        return this.namingAuthorityUrl;
    }
    
    public NamingAuthority(final DERObjectIdentifier namingAuthorityId, final String namingAuthorityUrl, final DirectoryString namingAuthorityText) {
        this.namingAuthorityId = namingAuthorityId;
        this.namingAuthorityUrl = namingAuthorityUrl;
        this.namingAuthorityText = namingAuthorityText;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.namingAuthorityId != null) {
            asn1EncodableVector.add(this.namingAuthorityId);
        }
        if (this.namingAuthorityUrl != null) {
            asn1EncodableVector.add(new DERIA5String(this.namingAuthorityUrl, true));
        }
        if (this.namingAuthorityText != null) {
            asn1EncodableVector.add(this.namingAuthorityText);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern = new DERObjectIdentifier(ISISMTTObjectIdentifiers.id_isismtt_at_namingAuthorities + ".1");
    }
}
