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
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PublicKeyManager implements X509TrustManager {

    @VisibleForTesting
    static final String SSL_PUBLIC_KEY_PIN_LOG_MESSAGE = "Certificate Mismatch!";
    static final String SSL_CERTIFICATE_VALIDITY_KEY = "_Validity";
    private AppInfraInterface appInfraInterface;

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


        SecureStorageInterface secureStorage = appInfraInterface.getSecureStorage();
        String stored_public_key = secureStorage.fetchValueForKey(certificate.getSerialNumber().toString(), getSecureStorageError());
        final boolean isSamePin = encoded.equalsIgnoreCase(stored_public_key);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

// check validity if expected public key is same as the public key that is currently pinned.
        if (isSamePin) {
            String storedDateValue = secureStorage.fetchValueForKey(certificate.getSerialNumber().toString() + SSL_CERTIFICATE_VALIDITY_KEY, getSecureStorageError());
            try {
                certificate.checkValidity(dateFormat.parse(storedDateValue));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                log(storedDateValue);
            }
        } else {
// Log it and Pin it!
            log(getCertificateDetails(certificate));

            secureStorage.storeValueForKey(certificate.getSerialNumber().toString(), encoded, getSecureStorageError());
            secureStorage.storeValueForKey(certificate.getSerialNumber().toString() + SSL_CERTIFICATE_VALIDITY_KEY, dateFormat.format(certificate.getNotAfter()), getSecureStorageError());
        }
    }

    private void log(String message) {
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), SSL_PUBLIC_KEY_PIN_LOG_MESSAGE);
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), message);
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

    private String getCertificateDetails(X509Certificate certificate) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(certificate.getSubjectDN().toString());
        builder.append("\n");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        builder.append(simpleDateFormat.format(certificate.getNotBefore()));
        builder.append(" - ");
        builder.append(simpleDateFormat.format(certificate.getNotAfter()));
        builder.append("\nSHA-256: ");
        builder.append(getHash(certificate, "SHA-256"));
        builder.append("\nSHA-1: ");
        builder.append(getHash(certificate, "SHA-1"));
        builder.append("\nSigned by: ");
        builder.append(certificate.getIssuerDN().toString());
        builder.append("\n");
        return builder.toString();
    }

    private String getHash(final X509Certificate certificate, String digest) {
        try {
            MessageDigest md = MessageDigest.getInstance(digest);
            md.update(certificate.getEncoded());
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
