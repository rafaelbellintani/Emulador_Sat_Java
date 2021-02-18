// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.util.Enumeration;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.io.SignerInputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.crypto.Signer;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import java.util.Hashtable;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import java.io.OutputStream;
import java.io.InputStream;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import java.security.SecureRandom;
import java.math.BigInteger;

public class TlsProtocolHandler
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final short RL_CHANGE_CIPHER_SPEC = 20;
    private static final short RL_ALERT = 21;
    private static final short RL_HANDSHAKE = 22;
    private static final short RL_APPLICATION_DATA = 23;
    private static final short HP_HELLO_REQUEST = 0;
    private static final short HP_CLIENT_HELLO = 1;
    private static final short HP_SERVER_HELLO = 2;
    private static final short HP_CERTIFICATE = 11;
    private static final short HP_SERVER_KEY_EXCHANGE = 12;
    private static final short HP_CERTIFICATE_REQUEST = 13;
    private static final short HP_SERVER_HELLO_DONE = 14;
    private static final short HP_CERTIFICATE_VERIFY = 15;
    private static final short HP_CLIENT_KEY_EXCHANGE = 16;
    private static final short HP_FINISHED = 20;
    private static final short CS_CLIENT_HELLO_SEND = 1;
    private static final short CS_SERVER_HELLO_RECEIVED = 2;
    private static final short CS_SERVER_CERTIFICATE_RECEIVED = 3;
    private static final short CS_SERVER_KEY_EXCHANGE_RECEIVED = 4;
    private static final short CS_CERTIFICATE_REQUEST_RECEIVED = 5;
    private static final short CS_SERVER_HELLO_DONE_RECEIVED = 6;
    private static final short CS_CLIENT_KEY_EXCHANGE_SEND = 7;
    private static final short CS_CLIENT_VERIFICATION_SEND = 8;
    private static final short CS_CLIENT_CHANGE_CIPHER_SPEC_SEND = 9;
    private static final short CS_CLIENT_FINISHED_SEND = 10;
    private static final short CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED = 11;
    private static final short CS_DONE = 12;
    protected static final short AP_close_notify = 0;
    protected static final short AP_unexpected_message = 10;
    protected static final short AP_bad_record_mac = 20;
    protected static final short AP_decryption_failed = 21;
    protected static final short AP_record_overflow = 22;
    protected static final short AP_decompression_failure = 30;
    protected static final short AP_handshake_failure = 40;
    protected static final short AP_bad_certificate = 42;
    protected static final short AP_unsupported_certificate = 43;
    protected static final short AP_certificate_revoked = 44;
    protected static final short AP_certificate_expired = 45;
    protected static final short AP_certificate_unknown = 46;
    protected static final short AP_illegal_parameter = 47;
    protected static final short AP_unknown_ca = 48;
    protected static final short AP_access_denied = 49;
    protected static final short AP_decode_error = 50;
    protected static final short AP_decrypt_error = 51;
    protected static final short AP_export_restriction = 60;
    protected static final short AP_protocol_version = 70;
    protected static final short AP_insufficient_security = 71;
    protected static final short AP_internal_error = 80;
    protected static final short AP_user_canceled = 90;
    protected static final short AP_no_renegotiation = 100;
    protected static final short AL_warning = 1;
    protected static final short AL_fatal = 2;
    private static final byte[] emptybuf;
    private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
    private ByteQueue applicationDataQueue;
    private ByteQueue changeCipherSpecQueue;
    private ByteQueue alertQueue;
    private ByteQueue handshakeQueue;
    private RecordStream rs;
    private SecureRandom random;
    private AsymmetricKeyParameter serverPublicKey;
    private TlsInputStream tlsInputStream;
    private TlsOuputStream tlsOutputStream;
    private boolean closed;
    private boolean failedWithError;
    private boolean appDataReady;
    private boolean extendedClientHello;
    private byte[] clientRandom;
    private byte[] serverRandom;
    private byte[] ms;
    private TlsCipherSuite chosenCipherSuite;
    private BigInteger SRP_A;
    private byte[] SRP_identity;
    private byte[] SRP_password;
    private BigInteger Yc;
    private byte[] pms;
    private CertificateVerifyer verifyer;
    private short connection_state;
    
    public TlsProtocolHandler(final InputStream inputStream, final OutputStream outputStream) {
        this.applicationDataQueue = new ByteQueue();
        this.changeCipherSpecQueue = new ByteQueue();
        this.alertQueue = new ByteQueue();
        this.handshakeQueue = new ByteQueue();
        this.serverPublicKey = null;
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.chosenCipherSuite = null;
        this.verifyer = null;
        (this.random = new SecureRandom()).setSeed(new ThreadedSeedGenerator().generateSeed(20, true));
        this.rs = new RecordStream(this, inputStream, outputStream);
    }
    
    public TlsProtocolHandler(final InputStream inputStream, final OutputStream outputStream, final SecureRandom random) {
        this.applicationDataQueue = new ByteQueue();
        this.changeCipherSpecQueue = new ByteQueue();
        this.alertQueue = new ByteQueue();
        this.handshakeQueue = new ByteQueue();
        this.serverPublicKey = null;
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.chosenCipherSuite = null;
        this.verifyer = null;
        this.random = random;
        this.rs = new RecordStream(this, inputStream, outputStream);
    }
    
    protected void processData(final short n, final byte[] array, final int n2, final int n3) throws IOException {
        switch (n) {
            case 20: {
                this.changeCipherSpecQueue.addData(array, n2, n3);
                this.processChangeCipherSpec();
                break;
            }
            case 21: {
                this.alertQueue.addData(array, n2, n3);
                this.processAlert();
                break;
            }
            case 22: {
                this.handshakeQueue.addData(array, n2, n3);
                this.processHandshake();
                break;
            }
            case 23: {
                if (!this.appDataReady) {
                    this.failWithError((short)2, (short)10);
                }
                this.applicationDataQueue.addData(array, n2, n3);
                this.processApplicationData();
                break;
            }
        }
    }
    
    private void processHandshake() throws IOException {
        boolean b;
        do {
            b = false;
            if (this.handshakeQueue.size() >= 4) {
                final byte[] buf = new byte[4];
                this.handshakeQueue.read(buf, 0, 4, 0);
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                final short uint8 = TlsUtils.readUint8(byteArrayInputStream);
                final int uint9 = TlsUtils.readUint24(byteArrayInputStream);
                if (this.handshakeQueue.size() < uint9 + 4) {
                    continue;
                }
                final byte[] buf2 = new byte[uint9];
                this.handshakeQueue.read(buf2, 0, uint9, 4);
                this.handshakeQueue.removeData(uint9 + 4);
                if (uint8 != 20) {
                    this.rs.hash1.update(buf, 0, 4);
                    this.rs.hash2.update(buf, 0, 4);
                    this.rs.hash1.update(buf2, 0, uint9);
                    this.rs.hash2.update(buf2, 0, uint9);
                }
                final ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(buf2);
                switch (uint8) {
                    case 11: {
                        switch (this.connection_state) {
                            case 2: {
                                final Certificate parse = Certificate.parse(byteArrayInputStream2);
                                this.assertEmpty(byteArrayInputStream2);
                                final X509CertificateStructure x509CertificateStructure = parse.certs[0];
                                final SubjectPublicKeyInfo subjectPublicKeyInfo = x509CertificateStructure.getSubjectPublicKeyInfo();
                                try {
                                    this.serverPublicKey = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                                }
                                catch (RuntimeException ex) {
                                    this.failWithError((short)2, (short)43);
                                }
                                if (this.serverPublicKey.isPrivate()) {
                                    this.failWithError((short)2, (short)80);
                                }
                                switch (this.chosenCipherSuite.getKeyExchangeAlgorithm()) {
                                    case 1: {
                                        if (!(this.serverPublicKey instanceof RSAKeyParameters)) {
                                            this.failWithError((short)2, (short)46);
                                        }
                                        this.validateKeyUsage(x509CertificateStructure, 32);
                                        break;
                                    }
                                    case 5:
                                    case 11: {
                                        if (!(this.serverPublicKey instanceof RSAKeyParameters)) {
                                            this.failWithError((short)2, (short)46);
                                        }
                                        this.validateKeyUsage(x509CertificateStructure, 128);
                                        break;
                                    }
                                    case 3:
                                    case 12: {
                                        if (!(this.serverPublicKey instanceof DSAPublicKeyParameters)) {
                                            this.failWithError((short)2, (short)46);
                                            break;
                                        }
                                        break;
                                    }
                                    default: {
                                        this.failWithError((short)2, (short)43);
                                        break;
                                    }
                                }
                                if (!this.verifyer.isValid(parse.getCerts())) {
                                    this.failWithError((short)2, (short)90);
                                    break;
                                }
                                break;
                            }
                            default: {
                                this.failWithError((short)2, (short)10);
                                break;
                            }
                        }
                        this.connection_state = 3;
                        b = true;
                        continue;
                    }
                    case 20: {
                        switch (this.connection_state) {
                            case 11: {
                                final byte[] array = new byte[12];
                                TlsUtils.readFully(array, byteArrayInputStream2);
                                this.assertEmpty(byteArrayInputStream2);
                                final byte[] array2 = new byte[12];
                                final byte[] array3 = new byte[36];
                                this.rs.hash2.doFinal(array3, 0);
                                TlsUtils.PRF(this.ms, TlsUtils.toByteArray("server finished"), array3, array2);
                                for (int i = 0; i < array.length; ++i) {
                                    if (array[i] != array2[i]) {
                                        this.failWithError((short)2, (short)40);
                                    }
                                }
                                this.connection_state = 12;
                                this.appDataReady = true;
                                b = true;
                                continue;
                            }
                            default: {
                                this.failWithError((short)2, (short)10);
                                continue;
                            }
                        }
                        continue;
                    }
                    case 2: {
                        switch (this.connection_state) {
                            case 1: {
                                TlsUtils.checkVersion(byteArrayInputStream2, this);
                                TlsUtils.readFully(this.serverRandom = new byte[32], byteArrayInputStream2);
                                TlsUtils.readOpaque8(byteArrayInputStream2);
                                this.chosenCipherSuite = TlsCipherSuiteManager.getCipherSuite(TlsUtils.readUint16(byteArrayInputStream2), this);
                                if (TlsUtils.readUint8(byteArrayInputStream2) != 0) {
                                    this.failWithError((short)2, (short)47);
                                }
                                if (this.extendedClientHello && byteArrayInputStream2.available() > 0) {
                                    final byte[] opaque16 = TlsUtils.readOpaque16(byteArrayInputStream2);
                                    final Hashtable<Integer, byte[]> hashtable = new Hashtable<Integer, byte[]>();
                                    final ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream(opaque16);
                                    while (byteArrayInputStream3.available() > 0) {
                                        hashtable.put(new Integer(TlsUtils.readUint16(byteArrayInputStream3)), TlsUtils.readOpaque16(byteArrayInputStream3));
                                    }
                                }
                                this.assertEmpty(byteArrayInputStream2);
                                this.connection_state = 2;
                                b = true;
                                continue;
                            }
                            default: {
                                this.failWithError((short)2, (short)10);
                                continue;
                            }
                        }
                        continue;
                    }
                    case 14: {
                        switch (this.connection_state) {
                            case 3: {
                                if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 1) {
                                    this.failWithError((short)2, (short)10);
                                }
                            }
                            case 4:
                            case 5: {
                                this.assertEmpty(byteArrayInputStream2);
                                final boolean b2 = this.connection_state == 5;
                                this.connection_state = 6;
                                if (b2) {
                                    this.sendClientCertificate();
                                }
                                switch (this.chosenCipherSuite.getKeyExchangeAlgorithm()) {
                                    case 1: {
                                        this.pms = new byte[48];
                                        this.random.nextBytes(this.pms);
                                        this.pms[0] = 3;
                                        this.pms[1] = 1;
                                        final PKCS1Encoding pkcs1Encoding = new PKCS1Encoding(new RSABlindedEngine());
                                        pkcs1Encoding.init(true, new ParametersWithRandom(this.serverPublicKey, this.random));
                                        byte[] processBlock = null;
                                        try {
                                            processBlock = pkcs1Encoding.processBlock(this.pms, 0, this.pms.length);
                                        }
                                        catch (InvalidCipherTextException ex2) {
                                            this.failWithError((short)2, (short)80);
                                        }
                                        this.sendClientKeyExchange(processBlock);
                                        break;
                                    }
                                    case 3:
                                    case 5: {
                                        this.sendClientKeyExchange(BigIntegers.asUnsignedByteArray(this.Yc));
                                        break;
                                    }
                                    case 10:
                                    case 11:
                                    case 12: {
                                        this.sendClientKeyExchange(BigIntegers.asUnsignedByteArray(this.SRP_A));
                                        break;
                                    }
                                    default: {
                                        this.failWithError((short)2, (short)10);
                                        break;
                                    }
                                }
                                this.connection_state = 7;
                                final byte[] array4 = { 1 };
                                this.rs.writeMessage((short)20, array4, 0, array4.length);
                                this.connection_state = 9;
                                this.ms = new byte[48];
                                final byte[] array5 = new byte[this.clientRandom.length + this.serverRandom.length];
                                System.arraycopy(this.clientRandom, 0, array5, 0, this.clientRandom.length);
                                System.arraycopy(this.serverRandom, 0, array5, this.clientRandom.length, this.serverRandom.length);
                                TlsUtils.PRF(this.pms, TlsUtils.toByteArray("master secret"), array5, this.ms);
                                (this.rs.writeSuite = this.chosenCipherSuite).init(this.ms, this.clientRandom, this.serverRandom);
                                final byte[] b3 = new byte[12];
                                final byte[] array6 = new byte[36];
                                this.rs.hash1.doFinal(array6, 0);
                                TlsUtils.PRF(this.ms, TlsUtils.toByteArray("client finished"), array6, b3);
                                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                TlsUtils.writeUint8((short)20, byteArrayOutputStream);
                                TlsUtils.writeUint24(12, byteArrayOutputStream);
                                byteArrayOutputStream.write(b3);
                                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                                this.rs.writeMessage((short)22, byteArray, 0, byteArray.length);
                                this.connection_state = 10;
                                b = true;
                                continue;
                            }
                            default: {
                                this.failWithError((short)2, (short)40);
                                continue;
                            }
                        }
                        continue;
                    }
                    case 12: {
                        Label_1679: {
                            switch (this.connection_state) {
                                case 2: {
                                    if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 10) {
                                        this.failWithError((short)2, (short)10);
                                    }
                                }
                                case 3: {
                                    switch (this.chosenCipherSuite.getKeyExchangeAlgorithm()) {
                                        case 5: {
                                            this.processDHEKeyExchange(byteArrayInputStream2, new TlsRSASigner());
                                            break Label_1679;
                                        }
                                        case 3: {
                                            this.processDHEKeyExchange(byteArrayInputStream2, new TlsDSSSigner());
                                            break Label_1679;
                                        }
                                        case 10: {
                                            this.processSRPKeyExchange(byteArrayInputStream2, null);
                                            break Label_1679;
                                        }
                                        case 11: {
                                            this.processSRPKeyExchange(byteArrayInputStream2, new TlsRSASigner());
                                            break Label_1679;
                                        }
                                        case 12: {
                                            this.processSRPKeyExchange(byteArrayInputStream2, new TlsDSSSigner());
                                            break Label_1679;
                                        }
                                        default: {
                                            this.failWithError((short)2, (short)10);
                                            break Label_1679;
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    this.failWithError((short)2, (short)10);
                                    break;
                                }
                            }
                        }
                        this.connection_state = 4;
                        b = true;
                        continue;
                    }
                    case 13: {
                        switch (this.connection_state) {
                            case 3: {
                                if (this.chosenCipherSuite.getKeyExchangeAlgorithm() != 1) {
                                    this.failWithError((short)2, (short)10);
                                }
                            }
                            case 4: {
                                TlsUtils.readOpaque8(byteArrayInputStream2);
                                TlsUtils.readOpaque8(byteArrayInputStream2);
                                this.assertEmpty(byteArrayInputStream2);
                                break;
                            }
                            default: {
                                this.failWithError((short)2, (short)10);
                                break;
                            }
                        }
                        this.connection_state = 5;
                        b = true;
                        continue;
                    }
                    default: {
                        this.failWithError((short)2, (short)10);
                        continue;
                    }
                }
            }
        } while (b);
    }
    
    private void processApplicationData() {
    }
    
    private void processAlert() throws IOException {
        while (this.alertQueue.size() >= 2) {
            final byte[] array = new byte[2];
            this.alertQueue.read(array, 0, 2, 0);
            this.alertQueue.removeData(2);
            final short n = array[0];
            final short n2 = array[1];
            if (n == 2) {
                this.failedWithError = true;
                this.closed = true;
                try {
                    this.rs.close();
                }
                catch (Exception ex) {}
                throw new IOException("Internal TLS error, this could be an attack");
            }
            if (n2 != 0) {
                continue;
            }
            this.failWithError((short)1, (short)0);
        }
    }
    
    private void processChangeCipherSpec() throws IOException {
        while (this.changeCipherSpecQueue.size() > 0) {
            final byte[] array = { 0 };
            this.changeCipherSpecQueue.read(array, 0, 1, 0);
            this.changeCipherSpecQueue.removeData(1);
            if (array[0] != 1) {
                this.failWithError((short)2, (short)10);
            }
            else if (this.connection_state == 10) {
                this.rs.readSuite = this.rs.writeSuite;
                this.connection_state = 11;
            }
            else {
                this.failWithError((short)2, (short)40);
            }
        }
    }
    
    private void processDHEKeyExchange(final ByteArrayInputStream byteArrayInputStream, final Signer signer) throws IOException {
        InputStream inputStream = byteArrayInputStream;
        if (signer != null) {
            signer.init(false, this.serverPublicKey);
            signer.update(this.clientRandom, 0, this.clientRandom.length);
            signer.update(this.serverRandom, 0, this.serverRandom.length);
            inputStream = new SignerInputStream(byteArrayInputStream, signer);
        }
        final byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
        final byte[] opaque17 = TlsUtils.readOpaque16(inputStream);
        final byte[] opaque18 = TlsUtils.readOpaque16(inputStream);
        if (signer != null && !signer.verifySignature(TlsUtils.readOpaque16(byteArrayInputStream))) {
            this.failWithError((short)2, (short)42);
        }
        this.assertEmpty(byteArrayInputStream);
        final BigInteger bigInteger = new BigInteger(1, opaque16);
        final BigInteger bigInteger2 = new BigInteger(1, opaque17);
        final BigInteger bigInteger3 = new BigInteger(1, opaque18);
        if (!bigInteger.isProbablePrime(10)) {
            this.failWithError((short)2, (short)47);
        }
        if (bigInteger2.compareTo(TlsProtocolHandler.TWO) < 0 || bigInteger2.compareTo(bigInteger.subtract(TlsProtocolHandler.TWO)) > 0) {
            this.failWithError((short)2, (short)47);
        }
        if (bigInteger3.compareTo(TlsProtocolHandler.TWO) < 0 || bigInteger3.compareTo(bigInteger.subtract(TlsProtocolHandler.ONE)) > 0) {
            this.failWithError((short)2, (short)47);
        }
        final DHParameters dhParameters = new DHParameters(bigInteger, bigInteger2);
        final DHBasicKeyPairGenerator dhBasicKeyPairGenerator = new DHBasicKeyPairGenerator();
        dhBasicKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, dhParameters));
        final AsymmetricCipherKeyPair generateKeyPair = dhBasicKeyPairGenerator.generateKeyPair();
        this.Yc = ((DHPublicKeyParameters)generateKeyPair.getPublic()).getY();
        final DHBasicAgreement dhBasicAgreement = new DHBasicAgreement();
        dhBasicAgreement.init(generateKeyPair.getPrivate());
        this.pms = BigIntegers.asUnsignedByteArray(dhBasicAgreement.calculateAgreement(new DHPublicKeyParameters(bigInteger3, dhParameters)));
    }
    
    private void processSRPKeyExchange(final ByteArrayInputStream byteArrayInputStream, final Signer signer) throws IOException {
        InputStream inputStream = byteArrayInputStream;
        if (signer != null) {
            signer.init(false, this.serverPublicKey);
            signer.update(this.clientRandom, 0, this.clientRandom.length);
            signer.update(this.serverRandom, 0, this.serverRandom.length);
            inputStream = new SignerInputStream(byteArrayInputStream, signer);
        }
        final byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
        final byte[] opaque17 = TlsUtils.readOpaque16(inputStream);
        final byte[] opaque18 = TlsUtils.readOpaque8(inputStream);
        final byte[] opaque19 = TlsUtils.readOpaque16(inputStream);
        if (signer != null && !signer.verifySignature(TlsUtils.readOpaque16(byteArrayInputStream))) {
            this.failWithError((short)2, (short)42);
        }
        this.assertEmpty(byteArrayInputStream);
        final BigInteger bigInteger = new BigInteger(1, opaque16);
        final BigInteger bigInteger2 = new BigInteger(1, opaque17);
        final byte[] array = opaque18;
        final BigInteger bigInteger3 = new BigInteger(1, opaque19);
        final SRP6Client srp6Client = new SRP6Client();
        srp6Client.init(bigInteger, bigInteger2, new SHA1Digest(), this.random);
        this.SRP_A = srp6Client.generateClientCredentials(array, this.SRP_identity, this.SRP_password);
        try {
            this.pms = BigIntegers.asUnsignedByteArray(srp6Client.calculateSecret(bigInteger3));
        }
        catch (CryptoException ex) {
            this.failWithError((short)2, (short)47);
        }
    }
    
    private void validateKeyUsage(final X509CertificateStructure x509CertificateStructure, final int n) throws IOException {
        final X509Extensions extensions = x509CertificateStructure.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final X509Extension extension = extensions.getExtension(X509Extensions.KeyUsage);
            if (extension != null && (KeyUsage.getInstance(extension).getBytes()[0] & 0xFF & n) != n) {
                this.failWithError((short)2, (short)46);
            }
        }
    }
    
    private void sendClientCertificate() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short)11, byteArrayOutputStream);
        TlsUtils.writeUint24(3, byteArrayOutputStream);
        TlsUtils.writeUint24(0, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short)22, byteArray, 0, byteArray.length);
    }
    
    private void sendClientKeyExchange(final byte[] array) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short)16, byteArrayOutputStream);
        TlsUtils.writeUint24(array.length + 2, byteArrayOutputStream);
        TlsUtils.writeOpaque16(array, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short)22, byteArray, 0, byteArray.length);
    }
    
    public void connect(final CertificateVerifyer verifyer) throws IOException {
        this.verifyer = verifyer;
        this.clientRandom = new byte[32];
        this.random.nextBytes(this.clientRandom);
        final int n = (int)(System.currentTimeMillis() / 1000L);
        this.clientRandom[0] = (byte)(n >> 24);
        this.clientRandom[1] = (byte)(n >> 16);
        this.clientRandom[2] = (byte)(n >> 8);
        this.clientRandom[3] = (byte)n;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeVersion(byteArrayOutputStream);
        byteArrayOutputStream.write(this.clientRandom);
        TlsUtils.writeUint8((short)0, byteArrayOutputStream);
        TlsCipherSuiteManager.writeCipherSuites(byteArrayOutputStream);
        TlsUtils.writeOpaque8(new byte[] { 0 }, byteArrayOutputStream);
        final Hashtable<Integer, Object> hashtable = new Hashtable<Integer, Object>();
        this.extendedClientHello = !hashtable.isEmpty();
        if (this.extendedClientHello) {
            final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            final Enumeration<Integer> keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                final Integer key = keys.nextElement();
                final byte[] array = hashtable.get(key);
                TlsUtils.writeUint16(key, byteArrayOutputStream2);
                TlsUtils.writeOpaque16(array, byteArrayOutputStream2);
            }
            TlsUtils.writeOpaque16(byteArrayOutputStream2.toByteArray(), byteArrayOutputStream);
        }
        final ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short)1, byteArrayOutputStream3);
        TlsUtils.writeUint24(byteArrayOutputStream.size(), byteArrayOutputStream3);
        byteArrayOutputStream3.write(byteArrayOutputStream.toByteArray());
        final byte[] byteArray = byteArrayOutputStream3.toByteArray();
        this.rs.writeMessage((short)22, byteArray, 0, byteArray.length);
        this.connection_state = 1;
        while (this.connection_state != 12) {
            this.rs.readData();
        }
        this.tlsInputStream = new TlsInputStream(this);
        this.tlsOutputStream = new TlsOuputStream(this);
    }
    
    protected int readApplicationData(final byte[] array, final int n, int min) throws IOException {
        while (this.applicationDataQueue.size() == 0) {
            if (this.failedWithError) {
                throw new IOException("Internal TLS error, this could be an attack");
            }
            if (this.closed) {
                return -1;
            }
            try {
                this.rs.readData();
                continue;
            }
            catch (IOException ex) {
                if (!this.closed) {
                    this.failWithError((short)2, (short)80);
                }
                throw ex;
            }
            catch (RuntimeException ex2) {
                if (!this.closed) {
                    this.failWithError((short)2, (short)80);
                }
                throw ex2;
            }
            break;
        }
        min = Math.min(min, this.applicationDataQueue.size());
        this.applicationDataQueue.read(array, n, min, 0);
        this.applicationDataQueue.removeData(min);
        return min;
    }
    
    protected void writeData(final byte[] array, int n, int i) throws IOException {
        if (this.failedWithError) {
            throw new IOException("Internal TLS error, this could be an attack");
        }
        if (this.closed) {
            throw new IOException("Sorry, connection has been closed, you cannot write more data");
        }
        this.rs.writeMessage((short)23, TlsProtocolHandler.emptybuf, 0, 0);
        do {
            final int min = Math.min(i, 16384);
            try {
                this.rs.writeMessage((short)23, array, n, min);
            }
            catch (IOException ex) {
                if (!this.closed) {
                    this.failWithError((short)2, (short)80);
                }
                throw ex;
            }
            catch (RuntimeException ex2) {
                if (!this.closed) {
                    this.failWithError((short)2, (short)80);
                }
                throw ex2;
            }
            n += min;
            i -= min;
        } while (i > 0);
    }
    
    @Deprecated
    public TlsOuputStream getTlsOuputStream() {
        return this.tlsOutputStream;
    }
    
    public OutputStream getOutputStream() {
        return this.tlsOutputStream;
    }
    
    @Deprecated
    public TlsInputStream getTlsInputStream() {
        return this.tlsInputStream;
    }
    
    public InputStream getInputStream() {
        return this.tlsInputStream;
    }
    
    protected void failWithError(final short n, final short n2) throws IOException {
        if (this.closed) {
            throw new IOException("Internal TLS error, this could be an attack");
        }
        final byte[] array = { (byte)n, (byte)n2 };
        this.closed = true;
        if (n == 2) {
            this.failedWithError = true;
        }
        this.rs.writeMessage((short)21, array, 0, 2);
        this.rs.close();
        if (n == 2) {
            throw new IOException("Internal TLS error, this could be an attack");
        }
    }
    
    public void close() throws IOException {
        if (!this.closed) {
            this.failWithError((short)1, (short)0);
        }
    }
    
    protected void assertEmpty(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (byteArrayInputStream.available() > 0) {
            this.failWithError((short)2, (short)50);
        }
    }
    
    protected void flush() throws IOException {
        this.rs.flush();
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
        emptybuf = new byte[0];
    }
}
