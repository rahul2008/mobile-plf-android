package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class IAPHurlStack {
    private final OAuthHandler mOAuthHandler;
    private static final String PHILIPS_HOST = "philips.com";

    private static final boolean TEST_SERVER = Boolean.TRUE;

    static Certificate testCertificate;
    private Context mContext;
    public IAPHurlStack(OAuthHandler oAuthHandler) {
        mOAuthHandler = oAuthHandler;
    }

    public HurlStack getHurlStack() {
        return new HurlStack(null, buildSslSocketFactory(mContext)) {
            @Override
            protected HttpURLConnection createConnection(final URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(final String hostname, final SSLSession session) {
                            return hostname.contains(PHILIPS_HOST);
                        }
                    });
                    //If we remove OAuth dependency, it can be used everywhere where we have
                    // philips domain dependency like config download
                    if (mOAuthHandler != null) {
                        connection.setRequestProperty("Authorization", "Bearer " + mOAuthHandler.getAccessToken());
                    }
                }
                return connection;
            }
        };
    }

    //We need certificates for test server, else return false
    public SSLSocketFactory buildSslSocketFactory(Context context) {
        if (!TEST_SERVER) {
            return null;
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = readTestCertificate(context);
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                testCertificate = ca;
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] mngrs = new TrustManager[]{new TestTrustManager()};//tmf.getTrustManagers();
            sslContext.init(null, mngrs, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    //For supporting unit testing
    InputStream readTestCertificate(final Context context) throws IOException {
        return context.getResources().getAssets().open("test.crt");
    }

    public void setContext(final Context context) {
        mContext = context;
    }

    private static class TestTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{(X509Certificate) testCertificate};
        }
    }
}