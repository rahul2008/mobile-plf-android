package com.philips.platform.appinfra.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PublicKeyManager implements X509TrustManager {

    private AppInfraInterface appInfraInterface;

    @VisibleForTesting
    static final String SSL_PUBLIC_KEY = "SubjectPublicKeyInfo";
    static final String SSL_PUBLIC_KEY_MANAGER = "PublicKeyManager";
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

// Hack ahead: BigInteger and toString(). We know a DER encoded Public
        // Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
        // no leading 0x00 to drop.
        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        chain[0].checkValidity();
        String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded())
                .toString(16);

// Pin it!
        SecureStorageInterface secureStorage = appInfraInterface.getSecureStorage();
        String stored_public_key = secureStorage.fetchValueForKey(SSL_PUBLIC_KEY, getSecureStorageError());
        final boolean expected = encoded.equalsIgnoreCase(stored_public_key);
        // fail if expected public key is different from the public key that is currently pinned.
        if (!expected) {
            appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, SSL_PUBLIC_KEY_MANAGER, SSL_PUBLIC_KEY_LOG_MESSAGE);
            secureStorage.storeValueForKey(SSL_PUBLIC_KEY, encoded, getSecureStorageError());
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
}
