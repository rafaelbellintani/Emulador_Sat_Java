// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface PKCSObjectIdentifiers
{
    public static final String pkcs_1 = "1.2.840.113549.1.1";
    public static final DERObjectIdentifier rsaEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.1");
    public static final DERObjectIdentifier md2WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.2");
    public static final DERObjectIdentifier md4WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.3");
    public static final DERObjectIdentifier md5WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.4");
    public static final DERObjectIdentifier sha1WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.5");
    public static final DERObjectIdentifier srsaOAEPEncryptionSET = new DERObjectIdentifier("1.2.840.113549.1.1.6");
    public static final DERObjectIdentifier id_RSAES_OAEP = new DERObjectIdentifier("1.2.840.113549.1.1.7");
    public static final DERObjectIdentifier id_mgf1 = new DERObjectIdentifier("1.2.840.113549.1.1.8");
    public static final DERObjectIdentifier id_pSpecified = new DERObjectIdentifier("1.2.840.113549.1.1.9");
    public static final DERObjectIdentifier id_RSASSA_PSS = new DERObjectIdentifier("1.2.840.113549.1.1.10");
    public static final DERObjectIdentifier sha256WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.11");
    public static final DERObjectIdentifier sha384WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.12");
    public static final DERObjectIdentifier sha512WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.13");
    public static final DERObjectIdentifier sha224WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.14");
    public static final String pkcs_3 = "1.2.840.113549.1.3";
    public static final DERObjectIdentifier dhKeyAgreement = new DERObjectIdentifier("1.2.840.113549.1.3.1");
    public static final String pkcs_5 = "1.2.840.113549.1.5";
    public static final DERObjectIdentifier pbeWithMD2AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.1");
    public static final DERObjectIdentifier pbeWithMD2AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.4");
    public static final DERObjectIdentifier pbeWithMD5AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.3");
    public static final DERObjectIdentifier pbeWithMD5AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.6");
    public static final DERObjectIdentifier pbeWithSHA1AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.10");
    public static final DERObjectIdentifier pbeWithSHA1AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.11");
    public static final DERObjectIdentifier id_PBES2 = new DERObjectIdentifier("1.2.840.113549.1.5.13");
    public static final DERObjectIdentifier id_PBKDF2 = new DERObjectIdentifier("1.2.840.113549.1.5.12");
    public static final String encryptionAlgorithm = "1.2.840.113549.3";
    public static final DERObjectIdentifier des_EDE3_CBC = new DERObjectIdentifier("1.2.840.113549.3.7");
    public static final DERObjectIdentifier RC2_CBC = new DERObjectIdentifier("1.2.840.113549.3.2");
    public static final String digestAlgorithm = "1.2.840.113549.2";
    public static final DERObjectIdentifier md2 = new DERObjectIdentifier("1.2.840.113549.2.2");
    public static final DERObjectIdentifier md4 = new DERObjectIdentifier("1.2.840.113549.2.4");
    public static final DERObjectIdentifier md5 = new DERObjectIdentifier("1.2.840.113549.2.5");
    public static final DERObjectIdentifier id_hmacWithSHA1 = new DERObjectIdentifier("1.2.840.113549.2.7");
    public static final DERObjectIdentifier id_hmacWithSHA224 = new DERObjectIdentifier("1.2.840.113549.2.8");
    public static final DERObjectIdentifier id_hmacWithSHA256 = new DERObjectIdentifier("1.2.840.113549.2.9");
    public static final DERObjectIdentifier id_hmacWithSHA384 = new DERObjectIdentifier("1.2.840.113549.2.10");
    public static final DERObjectIdentifier id_hmacWithSHA512 = new DERObjectIdentifier("1.2.840.113549.2.11");
    public static final String pkcs_7 = "1.2.840.113549.1.7";
    public static final DERObjectIdentifier data = new DERObjectIdentifier("1.2.840.113549.1.7.1");
    public static final DERObjectIdentifier signedData = new DERObjectIdentifier("1.2.840.113549.1.7.2");
    public static final DERObjectIdentifier envelopedData = new DERObjectIdentifier("1.2.840.113549.1.7.3");
    public static final DERObjectIdentifier signedAndEnvelopedData = new DERObjectIdentifier("1.2.840.113549.1.7.4");
    public static final DERObjectIdentifier digestedData = new DERObjectIdentifier("1.2.840.113549.1.7.5");
    public static final DERObjectIdentifier encryptedData = new DERObjectIdentifier("1.2.840.113549.1.7.6");
    public static final String pkcs_9 = "1.2.840.113549.1.9";
    public static final DERObjectIdentifier pkcs_9_at_emailAddress = new DERObjectIdentifier("1.2.840.113549.1.9.1");
    public static final DERObjectIdentifier pkcs_9_at_unstructuredName = new DERObjectIdentifier("1.2.840.113549.1.9.2");
    public static final DERObjectIdentifier pkcs_9_at_contentType = new DERObjectIdentifier("1.2.840.113549.1.9.3");
    public static final DERObjectIdentifier pkcs_9_at_messageDigest = new DERObjectIdentifier("1.2.840.113549.1.9.4");
    public static final DERObjectIdentifier pkcs_9_at_signingTime = new DERObjectIdentifier("1.2.840.113549.1.9.5");
    public static final DERObjectIdentifier pkcs_9_at_counterSignature = new DERObjectIdentifier("1.2.840.113549.1.9.6");
    public static final DERObjectIdentifier pkcs_9_at_challengePassword = new DERObjectIdentifier("1.2.840.113549.1.9.7");
    public static final DERObjectIdentifier pkcs_9_at_unstructuredAddress = new DERObjectIdentifier("1.2.840.113549.1.9.8");
    public static final DERObjectIdentifier pkcs_9_at_extendedCertificateAttributes = new DERObjectIdentifier("1.2.840.113549.1.9.9");
    public static final DERObjectIdentifier pkcs_9_at_signingDescription = new DERObjectIdentifier("1.2.840.113549.1.9.13");
    public static final DERObjectIdentifier pkcs_9_at_extensionRequest = new DERObjectIdentifier("1.2.840.113549.1.9.14");
    public static final DERObjectIdentifier pkcs_9_at_smimeCapabilities = new DERObjectIdentifier("1.2.840.113549.1.9.15");
    public static final DERObjectIdentifier pkcs_9_at_friendlyName = new DERObjectIdentifier("1.2.840.113549.1.9.20");
    public static final DERObjectIdentifier pkcs_9_at_localKeyId = new DERObjectIdentifier("1.2.840.113549.1.9.21");
    @Deprecated
    public static final DERObjectIdentifier x509certType = new DERObjectIdentifier("1.2.840.113549.1.9.22.1");
    public static final String certTypes = "1.2.840.113549.1.9.22";
    public static final DERObjectIdentifier x509Certificate = new DERObjectIdentifier("1.2.840.113549.1.9.22.1");
    public static final DERObjectIdentifier sdsiCertificate = new DERObjectIdentifier("1.2.840.113549.1.9.22.2");
    public static final String crlTypes = "1.2.840.113549.1.9.23";
    public static final DERObjectIdentifier x509Crl = new DERObjectIdentifier("1.2.840.113549.1.9.23.1");
    public static final DERObjectIdentifier id_alg_PWRI_KEK = new DERObjectIdentifier("1.2.840.113549.1.9.16.3.9");
    public static final DERObjectIdentifier preferSignedData = new DERObjectIdentifier("1.2.840.113549.1.9.15.1");
    public static final DERObjectIdentifier canNotDecryptAny = new DERObjectIdentifier("1.2.840.113549.1.9.15.2");
    public static final DERObjectIdentifier sMIMECapabilitiesVersions = new DERObjectIdentifier("1.2.840.113549.1.9.15.3");
    public static final String id_ct = "1.2.840.113549.1.9.16.1";
    public static final DERObjectIdentifier id_ct_authData = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.2");
    public static final DERObjectIdentifier id_ct_TSTInfo = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.4");
    public static final DERObjectIdentifier id_ct_compressedData = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.9");
    public static final DERObjectIdentifier id_ct_authEnvelopedData = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.23");
    public static final String id_cti = "1.2.840.113549.1.9.16.6";
    public static final DERObjectIdentifier id_cti_ets_proofOfOrigin = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.1");
    public static final DERObjectIdentifier id_cti_ets_proofOfReceipt = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.2");
    public static final DERObjectIdentifier id_cti_ets_proofOfDelivery = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.3");
    public static final DERObjectIdentifier id_cti_ets_proofOfSender = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.4");
    public static final DERObjectIdentifier id_cti_ets_proofOfApproval = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.5");
    public static final DERObjectIdentifier id_cti_ets_proofOfCreation = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.6");
    public static final String id_aa = "1.2.840.113549.1.9.16.2";
    public static final DERObjectIdentifier id_aa_receiptRequest = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.1");
    public static final DERObjectIdentifier id_aa_contentHint = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.4");
    public static final DERObjectIdentifier id_aa_encrypKeyPref = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.11");
    public static final DERObjectIdentifier id_aa_signingCertificate = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.12");
    public static final DERObjectIdentifier id_aa_signingCertificateV2 = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.47");
    public static final DERObjectIdentifier id_aa_contentIdentifier = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.7");
    public static final DERObjectIdentifier id_aa_signatureTimeStampToken = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.14");
    public static final DERObjectIdentifier id_aa_ets_sigPolicyId = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.15");
    public static final DERObjectIdentifier id_aa_ets_commitmentType = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.16");
    public static final DERObjectIdentifier id_aa_ets_signerLocation = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.17");
    public static final DERObjectIdentifier id_aa_ets_signerAttr = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.18");
    public static final DERObjectIdentifier id_aa_ets_otherSigCert = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.19");
    public static final DERObjectIdentifier id_aa_ets_contentTimestamp = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.20");
    public static final DERObjectIdentifier id_aa_ets_certificateRefs = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.21");
    public static final DERObjectIdentifier id_aa_ets_revocationRefs = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.22");
    public static final DERObjectIdentifier id_aa_ets_certValues = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.23");
    public static final DERObjectIdentifier id_aa_ets_revocationValues = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.24");
    public static final DERObjectIdentifier id_aa_ets_escTimeStamp = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.25");
    public static final DERObjectIdentifier id_aa_ets_certCRLTimestamp = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.26");
    public static final DERObjectIdentifier id_aa_ets_archiveTimestamp = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.27");
    @Deprecated
    public static final DERObjectIdentifier id_aa_sigPolicyId = PKCSObjectIdentifiers.id_aa_ets_sigPolicyId;
    @Deprecated
    public static final DERObjectIdentifier id_aa_commitmentType = PKCSObjectIdentifiers.id_aa_ets_commitmentType;
    @Deprecated
    public static final DERObjectIdentifier id_aa_signerLocation = PKCSObjectIdentifiers.id_aa_ets_signerLocation;
    @Deprecated
    public static final DERObjectIdentifier id_aa_otherSigCert = PKCSObjectIdentifiers.id_aa_ets_otherSigCert;
    public static final String id_spq = "1.2.840.113549.1.9.16.5";
    public static final DERObjectIdentifier id_spq_ets_uri = new DERObjectIdentifier("1.2.840.113549.1.9.16.5.1");
    public static final DERObjectIdentifier id_spq_ets_unotice = new DERObjectIdentifier("1.2.840.113549.1.9.16.5.2");
    public static final String pkcs_12 = "1.2.840.113549.1.12";
    public static final String bagtypes = "1.2.840.113549.1.12.10.1";
    public static final DERObjectIdentifier keyBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.1");
    public static final DERObjectIdentifier pkcs8ShroudedKeyBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.2");
    public static final DERObjectIdentifier certBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.3");
    public static final DERObjectIdentifier crlBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.4");
    public static final DERObjectIdentifier secretBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.5");
    public static final DERObjectIdentifier safeContentsBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.6");
    public static final String pkcs_12PbeIds = "1.2.840.113549.1.12.1";
    public static final DERObjectIdentifier pbeWithSHAAnd128BitRC4 = new DERObjectIdentifier("1.2.840.113549.1.12.1.1");
    public static final DERObjectIdentifier pbeWithSHAAnd40BitRC4 = new DERObjectIdentifier("1.2.840.113549.1.12.1.2");
    public static final DERObjectIdentifier pbeWithSHAAnd3_KeyTripleDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.3");
    public static final DERObjectIdentifier pbeWithSHAAnd2_KeyTripleDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.4");
    public static final DERObjectIdentifier pbeWithSHAAnd128BitRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.5");
    public static final DERObjectIdentifier pbewithSHAAnd40BitRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.6");
    public static final DERObjectIdentifier id_alg_CMS3DESwrap = new DERObjectIdentifier("1.2.840.113549.1.9.16.3.6");
    public static final DERObjectIdentifier id_alg_CMSRC2wrap = new DERObjectIdentifier("1.2.840.113549.1.9.16.3.7");
}
