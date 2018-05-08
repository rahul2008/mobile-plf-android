package com.philips.platform.appinfra.rest;

import android.net.http.X509TrustManagerExtensions;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class AppInfraTrustManager implements X509TrustManager {

    private X509TrustManagerExtensions trustManagerExtensions;

    public AppInfraTrustManager() {
        this.trustManagerExtensions = new X509TrustManagerExtensions(this);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        throw new CertificateException(CertificateExceptions.CLIENT_CERTIFICATE.toString());
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException(CertificateExceptions.NULL_CHAIN.toString());
        }
        if (!(chain.length > 0)) {
            throw new IllegalArgumentException(CertificateExceptions.EMPTY_CHAIN.toString());
        }
        checkForUserInstalledCertificates(chain);
        verifyWithDeviceCertificates(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    /*
     * Used for reflection in X509TrustManagerExtensions
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType, String hostname) throws CertificateException{
        checkServerTrusted(chain, authType);
    }

    private void checkForUserInstalledCertificates(X509Certificate[] chain) throws CertificateException{
        for (X509Certificate certificate : chain) {
            if (trustManagerExtensions.isUserAddedCertificate(certificate)) {
                throw new CertificateException(CertificateExceptions.USER_INSTALLED.toString());
            }
        }
    }

    private void verifyWithDeviceCertificates(X509Certificate[] chain, String authType) throws CertificateException {
        TrustManagerFactory tmf;
        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                if(trustManager instanceof X509TrustManager){
                    ((X509TrustManager) trustManager).checkServerTrusted(
                            chain, authType);
                }
            }

        } catch (Exception e) {
            throw new CertificateException(e.toString());
        }
    }

    private enum CertificateExceptions {
        USER_INSTALLED("User installed certificates not supported!"),
        CLIENT_CERTIFICATE("Client certificates not supported!"),
        NULL_CHAIN("checkServerTrusted: X509Certificate array is null"),
        EMPTY_CHAIN("checkServerTrusted: X509Certificate is empty");

        private final String text;

        CertificateExceptions(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
