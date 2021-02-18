// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x500.DirectoryString;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class ProfessionInfo extends ASN1Encodable
{
    public static final DERObjectIdentifier Rechtsanwltin;
    public static final DERObjectIdentifier Rechtsanwalt;
    public static final DERObjectIdentifier Rechtsbeistand;
    public static final DERObjectIdentifier Steuerberaterin;
    public static final DERObjectIdentifier Steuerberater;
    public static final DERObjectIdentifier Steuerbevollmchtigte;
    public static final DERObjectIdentifier Steuerbevollmchtigter;
    public static final DERObjectIdentifier Notarin;
    public static final DERObjectIdentifier Notar;
    public static final DERObjectIdentifier Notarvertreterin;
    public static final DERObjectIdentifier Notarvertreter;
    public static final DERObjectIdentifier Notariatsverwalterin;
    public static final DERObjectIdentifier Notariatsverwalter;
    public static final DERObjectIdentifier Wirtschaftsprferin;
    public static final DERObjectIdentifier Wirtschaftsprfer;
    public static final DERObjectIdentifier VereidigteBuchprferin;
    public static final DERObjectIdentifier VereidigterBuchprfer;
    public static final DERObjectIdentifier Patentanwltin;
    public static final DERObjectIdentifier Patentanwalt;
    private NamingAuthority namingAuthority;
    private ASN1Sequence professionItems;
    private ASN1Sequence professionOIDs;
    private String registrationNumber;
    private ASN1OctetString addProfessionInfo;
    
    public static ProfessionInfo getInstance(final Object o) {
        if (o == null || o instanceof ProfessionInfo) {
            return (ProfessionInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ProfessionInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    private ProfessionInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 5) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        DEREncodable derEncodable = objects.nextElement();
        if (derEncodable instanceof ASN1TaggedObject) {
            if (((ASN1TaggedObject)derEncodable).getTagNo() != 0) {
                throw new IllegalArgumentException("Bad tag number: " + ((ASN1TaggedObject)derEncodable).getTagNo());
            }
            this.namingAuthority = NamingAuthority.getInstance((ASN1TaggedObject)derEncodable, true);
            derEncodable = objects.nextElement();
        }
        this.professionItems = ASN1Sequence.getInstance(derEncodable);
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable2 = objects.nextElement();
            if (derEncodable2 instanceof ASN1Sequence) {
                this.professionOIDs = ASN1Sequence.getInstance(derEncodable2);
            }
            else if (derEncodable2 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(derEncodable2).getString();
            }
            else {
                if (!(derEncodable2 instanceof ASN1OctetString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + derEncodable2.getClass());
                }
                this.addProfessionInfo = ASN1OctetString.getInstance(derEncodable2);
            }
        }
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable3 = objects.nextElement();
            if (derEncodable3 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(derEncodable3).getString();
            }
            else {
                if (!(derEncodable3 instanceof DEROctetString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + ((DEROctetString)derEncodable3).getClass());
                }
                this.addProfessionInfo = (DEROctetString)derEncodable3;
            }
        }
        if (objects.hasMoreElements()) {
            final DEREncodable derEncodable4 = objects.nextElement();
            if (!(derEncodable4 instanceof DEROctetString)) {
                throw new IllegalArgumentException("Bad object encountered: " + ((DEROctetString)derEncodable4).getClass());
            }
            this.addProfessionInfo = (DEROctetString)derEncodable4;
        }
    }
    
    public ProfessionInfo(final NamingAuthority namingAuthority, final DirectoryString[] array, final DERObjectIdentifier[] array2, final String registrationNumber, final ASN1OctetString addProfessionInfo) {
        this.namingAuthority = namingAuthority;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.professionItems = new DERSequence(asn1EncodableVector);
        if (array2 != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int j = 0; j != array2.length; ++j) {
                asn1EncodableVector2.add(array2[j]);
            }
            this.professionOIDs = new DERSequence(asn1EncodableVector2);
        }
        this.registrationNumber = registrationNumber;
        this.addProfessionInfo = addProfessionInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.namingAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.namingAuthority));
        }
        asn1EncodableVector.add(this.professionItems);
        if (this.professionOIDs != null) {
            asn1EncodableVector.add(this.professionOIDs);
        }
        if (this.registrationNumber != null) {
            asn1EncodableVector.add(new DERPrintableString(this.registrationNumber, true));
        }
        if (this.addProfessionInfo != null) {
            asn1EncodableVector.add(this.addProfessionInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public ASN1OctetString getAddProfessionInfo() {
        return this.addProfessionInfo;
    }
    
    public NamingAuthority getNamingAuthority() {
        return this.namingAuthority;
    }
    
    public DirectoryString[] getProfessionItems() {
        final DirectoryString[] array = new DirectoryString[this.professionItems.size()];
        int n = 0;
        final Enumeration objects = this.professionItems.getObjects();
        while (objects.hasMoreElements()) {
            array[n++] = DirectoryString.getInstance(objects.nextElement());
        }
        return array;
    }
    
    public DERObjectIdentifier[] getProfessionOIDs() {
        if (this.professionOIDs == null) {
            return new DERObjectIdentifier[0];
        }
        final DERObjectIdentifier[] array = new DERObjectIdentifier[this.professionOIDs.size()];
        int n = 0;
        final Enumeration objects = this.professionOIDs.getObjects();
        while (objects.hasMoreElements()) {
            array[n++] = DERObjectIdentifier.getInstance(objects.nextElement());
        }
        return array;
    }
    
    public String getRegistrationNumber() {
        return this.registrationNumber;
    }
    
    static {
        Rechtsanwltin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".1");
        Rechtsanwalt = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".2");
        Rechtsbeistand = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".3");
        Steuerberaterin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".4");
        Steuerberater = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".5");
        Steuerbevollmchtigte = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".6");
        Steuerbevollmchtigter = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".7");
        Notarin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".8");
        Notar = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".9");
        Notarvertreterin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".10");
        Notarvertreter = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".11");
        Notariatsverwalterin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".12");
        Notariatsverwalter = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".13");
        Wirtschaftsprferin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".14");
        Wirtschaftsprfer = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".15");
        VereidigteBuchprferin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".16");
        VereidigterBuchprfer = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".17");
        Patentanwltin = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".18");
        Patentanwalt = new DERObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".19");
    }
}
