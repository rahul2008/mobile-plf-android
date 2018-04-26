package com.philips.platform.appinfra.rest;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PublicKeyManager implements X509TrustManager {

    private AppInfraInterface appInfraInterface;

    @VisibleForTesting
    static final String SSL_PUBLIC_KEY_LOG_MESSAGE = "Current PublicKey not pinned";

    public PublicKeyManager(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        verifyCertificates(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        verifyCertificates(chain, authType);
    }

    private void verifyCertificates(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }
        if (!(chain.length > 0)) {
            throw new IllegalArgumentException(
                    "checkServerTrusted: X509Certificate is empty");
        }

// Perform customary SSL/TLS checks
        TrustManagerFactory tmf;
        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(
                        chain, authType);
            }

        } catch (Exception e) {
            throw new CertificateException(e.toString());
        }

        X509Certificate certificate = chain[0];
// Hack ahead: BigInteger and toString(). We know a DER encoded Public
// Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0x00 to drop.
        RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
        String encoded = new BigInteger(1 /* positive */, publicKey.getEncoded())
                .toString(16);

// Pin it!
        SecureStorageInterface secureStorage = appInfraInterface.getSecureStorage();
        String stored_public_key = secureStorage.fetchValueForKey(certificate.getSerialNumber().toString(), getSecureStorageError());
        final boolean expected = encoded.equalsIgnoreCase(stored_public_key);

// fail if expected public key is different from the public key that is currently pinned.
        if (!expected) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), SSL_PUBLIC_KEY_LOG_MESSAGE);
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), getCertificateDetails(certificate));
            secureStorage.storeValueForKey(certificate.getSerialNumber().toString(), encoded, getSecureStorageError());
        }
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private String getCertificateDetails(X509Certificate c) {
        StringBuilder si = new StringBuilder();
        SimpleDateFormat validityDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        si.append("\n");
        si.append(c.getSubjectDN().toString());
        si.append("\n");
        si.append(validityDateFormater.format(c.getNotBefore()));
        si.append(" - ");
        si.append(validityDateFormater.format(c.getNotAfter()));
        si.append("\nSHA-256: ");
        si.append(getHash(c, "SHA-256"));
        si.append("\nSHA-1: ");
        si.append(getHash(c, "SHA-1"));
        si.append("\nSigned by: ");
        si.append(c.getIssuerDN().toString());
        si.append("\n");
        return si.toString();
    }

    private String getHash(final X509Certificate cert, String digest) {
        try {
            MessageDigest md = MessageDigest.getInstance(digest);
            md.update(cert.getEncoded());
            return hexString(md.digest());
        } catch (java.security.cert.CertificateEncodingException e) {
            return e.getMessage();
        } catch (java.security.NoSuchAlgorithmException e) {
            return e.getMessage();
        }
    }

    private String hexString(byte[] data) {
        StringBuilder si = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            si.append(String.format("%02x", data[i]));
            if (i < data.length - 1)
                si.append(":");
        }
        return si.toString();
    }
}
