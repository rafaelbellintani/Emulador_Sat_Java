// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class RoleSyntax extends ASN1Encodable
{
    private GeneralNames roleAuthority;
    private GeneralName roleName;
    
    public static RoleSyntax getInstance(final Object o) {
        if (o == null || o instanceof RoleSyntax) {
            return (RoleSyntax)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RoleSyntax((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Unknown object in RoleSyntax factory.");
    }
    
    public RoleSyntax(final GeneralNames roleAuthority, final GeneralName roleName) {
        if (roleName == null || roleName.getTagNo() != 6 || ((DERString)roleName.getName()).getString().equals("")) {
            throw new IllegalArgumentException("the role name MUST be non empty and MUST use the URI option of GeneralName");
        }
        this.roleAuthority = roleAuthority;
        this.roleName = roleName;
    }
    
    public RoleSyntax(final GeneralName generalName) {
        this(null, generalName);
    }
    
    public RoleSyntax(final String s) {
        this(new GeneralName(6, (s == null) ? "" : s));
    }
    
    public RoleSyntax(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
            switch (instance.getTagNo()) {
                case 0: {
                    this.roleAuthority = GeneralNames.getInstance(instance, false);
                    break;
                }
                case 1: {
                    this.roleName = GeneralName.getInstance(instance, true);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown tag in RoleSyntax");
                }
            }
        }
    }
    
    public GeneralNames getRoleAuthority() {
        return this.roleAuthority;
    }
    
    public GeneralName getRoleName() {
        return this.roleName;
    }
    
    public String getRoleNameAsString() {
        return ((DERString)this.roleName.getName()).getString();
    }
    
    public String[] getRoleAuthorityAsString() {
        if (this.roleAuthority == null) {
            return new String[0];
        }
        final GeneralName[] names = this.roleAuthority.getNames();
        final String[] array = new String[names.length];
        for (int i = 0; i < names.length; ++i) {
            final DEREncodable name = names[i].getName();
            if (name instanceof DERString) {
                array[i] = ((DERString)name).getString();
            }
            else {
                array[i] = name.toString();
            }
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.roleAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.roleAuthority));
        }
        asn1EncodableVector.add(new DERTaggedObject(true, 1, this.roleName));
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Name: " + this.getRoleNameAsString() + " - Auth: ");
        if (this.roleAuthority == null || this.roleAuthority.getNames().length == 0) {
            sb.append("N/A");
        }
        else {
            final String[] roleAuthorityAsString = this.getRoleAuthorityAsString();
            sb.append('[').append(roleAuthorityAsString[0]);
            for (int i = 1; i < roleAuthorityAsString.length; ++i) {
                sb.append(", ").append(roleAuthorityAsString[i]);
            }
            sb.append(']');
        }
        return sb.toString();
    }
}
