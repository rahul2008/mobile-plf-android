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
    private static final String SSL_CERTIFICATE_VALIDITY_KEY = "_Validity";
    private AppInfraInterface appInfraInterface;
    private SecureStorageInterface secureStorage;

    public PublicKeyManager(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
         secureStorage = appInfraInterface.getSecureStorage();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        verifyCertificates(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        verifyCertificates(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    private void verifyCertificates(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }
        if (!(chain.length > 0)) {
            throw new IllegalArgumentException(
                    "checkServerTrusted: X509Certificate is empty");
        }

        X509Certificate certificate = chain[0];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

// Perform customary SSL/TLS checks
        verifyChainWithTLS(chain, authType);

        String encoded = getEncodedPin(certificate);
        String stored_public_key = secureStorage.fetchValueForKey(certificate.getSerialNumber().toString(), getSecureStorageError());

        final boolean isSamePin = encoded.equalsIgnoreCase(stored_public_key);

// check validity if expected public key is same as the public key that is currently pinned.
        if (isSamePin) {
            String storedDateValue = secureStorage.fetchValueForKey(certificate.getSerialNumber().toString() + SSL_CERTIFICATE_VALIDITY_KEY, getSecureStorageError());
            verifyCertificateValidity(certificate, dateFormat, storedDateValue);
        } else {
// Log it and Pin it!
            log(getCertificateDetails(certificate));

            secureStorage.storeValueForKey(certificate.getSerialNumber().toString(), encoded, getSecureStorageError());
            secureStorage.storeValueForKey(certificate.getSerialNumber().toString() + SSL_CERTIFICATE_VALIDITY_KEY, dateFormat.format(certificate.getNotAfter()), getSecureStorageError());
        }
    }

    private String getEncodedPin(X509Certificate certificate) {
// BigInteger and toString(). We know a DER encoded PublicKey starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0x00 to drop.
        RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
        return new BigInteger(1 /* positive */, publicKey.getEncoded())
                .toString(16);
    }

    private void verifyCertificateValidity(X509Certificate certificate, SimpleDateFormat dateFormat, String storedDateValue) {
        try {
            certificate.checkValidity(dateFormat.parse(storedDateValue));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            log(storedDateValue);
        }
    }

    private void verifyChainWithTLS(X509Certificate[] chain, String authType) throws CertificateException {
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
    }

    private void log(String message) {
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), SSL_PUBLIC_KEY_PIN_LOG_MESSAGE);
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, PublicKeyManager.class.getSimpleName(), message);
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
