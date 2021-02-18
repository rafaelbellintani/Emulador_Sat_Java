// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERUTCTime;
import java.util.Vector;
import org.bouncycastle.asn1.DERInteger;

public class V2TBSCertListGenerator
{
    DERInteger version;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time thisUpdate;
    Time nextUpdate;
    X509Extensions extensions;
    private Vector crlentries;
    
    public V2TBSCertListGenerator() {
        this.version = new DERInteger(1);
        this.nextUpdate = null;
        this.extensions = null;
        this.crlentries = null;
    }
    
    public void setSignature(final AlgorithmIdentifier signature) {
        this.signature = signature;
    }
    
    public void setIssuer(final X509Name issuer) {
        this.issuer = issuer;
    }
    
    public void setThisUpdate(final DERUTCTime derutcTime) {
        this.thisUpdate = new Time(derutcTime);
    }
    
    public void setNextUpdate(final DERUTCTime derutcTime) {
        this.nextUpdate = new Time(derutcTime);
    }
    
    public void setThisUpdate(final Time thisUpdate) {
        this.thisUpdate = thisUpdate;
    }
    
    public void setNextUpdate(final Time nextUpdate) {
        this.nextUpdate = nextUpdate;
    }
    
    public void addCRLEntry(final ASN1Sequence obj) {
        if (this.crlentries == null) {
            this.crlentries = new Vector();
        }
        this.crlentries.addElement(obj);
    }
    
    public void addCRLEntry(final DERInteger derInteger, final DERUTCTime derutcTime, final int n) {
        this.addCRLEntry(derInteger, new Time(derutcTime), n);
    }
    
    public void addCRLEntry(final DERInteger derInteger, final Time time, final int n) {
        this.addCRLEntry(derInteger, time, n, null);
    }
    
    public void addCRLEntry(final DERInteger derInteger, final Time time, final int n, final DERGeneralizedTime derGeneralizedTime) {
        final Vector<DERObjectIdentifier> vector = new Vector<DERObjectIdentifier>();
        final Vector<X509Extension> vector2 = new Vector<X509Extension>();
        if (n != 0) {
            final CRLReason crlReason = new CRLReason(n);
            try {
                vector.addElement(X509Extensions.ReasonCode);
                vector2.addElement(new X509Extension(false, new DEROctetString(crlReason.getEncoded())));
            }
            catch (IOException obj) {
                throw new IllegalArgumentException("error encoding reason: " + obj);
            }
        }
        if (derGeneralizedTime != null) {
            try {
                vector.addElement(X509Extensions.InvalidityDate);
                vector2.addElement(new X509Extension(false, new DEROctetString(derGeneralizedTime.getEncoded())));
            }
            catch (IOException obj2) {
                throw new IllegalArgumentException("error encoding invalidityDate: " + obj2);
            }
        }
        if (vector.size() != 0) {
            this.addCRLEntry(derInteger, time, new X509Extensions(vector, vector2));
        }
        else {
            this.addCRLEntry(derInteger, time, null);
        }
    }
    
    public void addCRLEntry(final DERInteger derInteger, final Time time, final X509Extensions x509Extensions) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(derInteger);
        asn1EncodableVector.add(time);
        if (x509Extensions != null) {
            asn1EncodableVector.add(x509Extensions);
        }
        this.addCRLEntry(new DERSequence(asn1EncodableVector));
    }
    
    public void setExtensions(final X509Extensions extensions) {
        this.extensions = extensions;
    }
    
    public TBSCertList generateTBSCertList() {
        if (this.signature == null || this.issuer == null || this.thisUpdate == null) {
            throw new IllegalStateException("Not all mandatory fields set in V2 TBSCertList generator.");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            asn1EncodableVector.add(this.nextUpdate);
        }
        if (this.crlentries != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            final Enumeration<ASN1Sequence> elements = this.crlentries.elements();
            while (elements.hasMoreElements()) {
                asn1EncodableVector2.add(elements.nextElement());
            }
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.extensions));
        }
        return new TBSCertList(new DERSequence(asn1EncodableVector));
    }
}
