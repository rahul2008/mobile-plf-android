package com.philips.platform.appinfra.rest;

import android.net.http.X509TrustManagerExtensions;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class AppInfraTrustManager implements X509TrustManager {

    private X509TrustManagerExtensions trustManagerExtensions;

    public AppInfraTrustManager(String host) {
        this.trustManagerExtensions = new X509TrustManagerExtensions(this);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        throw new CertificateException(CertificateExceptions.CLIENT_CERTIFICATE.getText());
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException(CertificateExceptions.NULL_CHAIN.getText());
        }
        if (chain.length <= 0) {
            throw new IllegalArgumentException(CertificateExceptions.EMPTY_CHAIN.getText());
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
                throw new CertificateException(CertificateExceptions.USER_INSTALLED.getText());
            }
        }
    }

    private void verifyWithDeviceCertificates(X509Certificate[] chain, String authType) throws CertificateException {
        TrustManagerFactory tmf;
        boolean serverTrusted = false;
        boolean x509TrustManagerSupport = false;
        String exceptionMessage = CertificateExceptions.NOT_SUPPORTED.getText();

        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                if(trustManager instanceof X509TrustManager){
                    x509TrustManagerSupport = true;
                    try{
                        ((X509TrustManager) trustManager).checkServerTrusted(
                                chain, authType);
                        serverTrusted = true;
                        break;
                    } catch (CertificateException e){
                        exceptionMessage = e.getMessage();
                    }
                }
            }

            if(!x509TrustManagerSupport || !serverTrusted){
                throw new CertificateException(exceptionMessage);
            }

        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new CertificateException(e.getMessage());
        }
    }

    private enum CertificateExceptions {
        USER_INSTALLED("User installed certificates not supported!"),
        CLIENT_CERTIFICATE("Client certificates not supported!"),
        NULL_CHAIN("checkServerTrusted: X509Certificate array is null"),
        EMPTY_CHAIN("checkServerTrusted: X509Certificate is empty"),
        NOT_SUPPORTED("checkServerTrusted: X509Certificate not supported");

        private final String text;

        CertificateExceptions(final String text) {
            this.text = text;
        }
        
        public String getText() {
            return text;
        }
    }
}
