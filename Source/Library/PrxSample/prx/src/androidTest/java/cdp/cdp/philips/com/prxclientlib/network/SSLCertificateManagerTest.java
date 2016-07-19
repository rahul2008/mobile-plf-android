package cdp.cdp.philips.com.prxclientlib.network;

import android.test.InstrumentationTestCase;

import com.philips.cdp.prxclient.network.SSLCertificateManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SSLCertificateManagerTest extends InstrumentationTestCase {

    SSLCertificateManager sslCertificateManager;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sslCertificateManager= new SSLCertificateManager();
    }

    public void testSetSSLSocketFactory() throws Exception {
        sslCertificateManager.disableAllServerCertificateChecking();
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        });


    }
}