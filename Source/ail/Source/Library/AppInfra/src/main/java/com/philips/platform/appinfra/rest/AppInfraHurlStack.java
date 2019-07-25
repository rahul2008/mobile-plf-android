package com.philips.platform.appinfra.rest;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.hpkp.HPKPInterface;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class AppInfraHurlStack extends HurlStack {

    private static final String SSL_RESPONSE_PUBLIC_KEY = "Public-Key-Pins";
    private HPKPInterface pinInterface;
    private LoggingInterface appInfraLogging;

    private static final ThreadLocal<HttpsURLConnection> localURLConnection = new ThreadLocal<>();

    public AppInfraHurlStack(HPKPInterface pinInterface, UrlRewriter urlRewriter, LoggingInterface logging) {
        super(urlRewriter);
        this.pinInterface = pinInterface;
        this.appInfraLogging = logging;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = super.createConnection(url);
        connection.setInstanceFollowRedirects(true);
        try {
            if (connection != null && "https".equals(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory());
                localURLConnection.set((HttpsURLConnection) connection);
            }
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, this.getClass().getSimpleName(), e.getMessage());
        }
        return connection;
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        HttpResponse httpResponse = super.executeRequest(request, additionalHeaders);

        if (localURLConnection.get() != null) {
            String hostName = localURLConnection.get().getURL().getHost();
            List<X509Certificate> certificatesList = getCertificatesList(localURLConnection.get());
            pinInterface.isPinnedCertificateMatching(hostName, certificatesList);

            String publicKeyDetails = getPublicKeyDetailsFromHeader(httpResponse);
            pinInterface.updatePinnedPublicKey(hostName, publicKeyDetails);
            localURLConnection.remove();
        }
        return httpResponse;
    }

    private String getPublicKeyDetailsFromHeader(HttpResponse httpResponse) {
        List<Header> headers = httpResponse.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals(SSL_RESPONSE_PUBLIC_KEY)) {
                return header.getValue();
            }
        }
        return "";
    }

    private List<X509Certificate> getCertificatesList(HttpsURLConnection connection) {
        List<X509Certificate> certificateChain = new ArrayList<>();
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            //get default X509TrustManager
            TrustManager[] trustManagers = tmf.getTrustManagers();
            final X509TrustManager x509Tm = (X509TrustManager) trustManagers[0];

            //trusted certificate issuers
            X509Certificate issuers[] = x509Tm.getAcceptedIssuers();

            //get the last intermediate certificate from server certificates.
            Certificate cert[] = connection.getServerCertificates();

            for (Certificate aCert : cert) {
                if (aCert instanceof X509Certificate)
                    certificateChain.add((X509Certificate) aCert);
            }

            X509Certificate intermediate = (X509Certificate) cert[cert.length - 1];
            for (X509Certificate issuer : issuers) {
                try {
                    intermediate.verify(issuer.getPublicKey());
                    //Verification ok. issuers[i] is the issuer
                    certificateChain.add(issuer);
                    break;
                } catch (CertificateException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                    appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, this.getClass().getSimpleName(), e.getMessage());
                } catch (Exception e) {
                    appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, this.getClass().getSimpleName(), "Uncaught Exception" + e.getMessage());
                }
            }
        } catch (NoSuchAlgorithmException | SSLPeerUnverifiedException | KeyStoreException e) {
            appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, this.getClass().getSimpleName(), e.getMessage());
        }
        return certificateChain;
    }
}
