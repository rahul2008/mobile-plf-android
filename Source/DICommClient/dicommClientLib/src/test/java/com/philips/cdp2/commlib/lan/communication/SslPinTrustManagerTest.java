/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SslPinTrustManagerTest extends RobolectricTest {
    private SslPinTrustManager sslPinTrustManager;

    @Mock
    private X509Certificate certificateMock;

    @Mock
    private PublicKey publicKeyMock;

    private byte[] encodedPublicKey = new byte[4];

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        sslPinTrustManager = new SslPinTrustManager();

        when(certificateMock.getPublicKey()).thenReturn(publicKeyMock);
        when(publicKeyMock.getEncoded()).thenReturn(encodedPublicKey);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void whenUsingManagerForClientTrusted_ThenShouldThrowException() throws Exception {
        sslPinTrustManager.checkClientTrusted(null, null);
    }

    @Test
    public void whenCheckingUnpinnedCertificate_ThenCertificateIsAccepted() throws Exception {
        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);
    }

    @Test
    public void whenCheckingPinnedCertificate_ThenCertificateIsAccepted() throws Exception {
        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);

        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);
    }

    @Test(expected = CertificateException.class)
    public void whenCheckingDifferentCertificateThenPinned_ThenCertificateIsRejected() throws Exception {
        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);
        when(publicKeyMock.getEncoded()).thenReturn(new byte[5]);

        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);
    }

    @Test
    public void whenGettingAcceptedIssuers_ThenReturnEmptyArray() throws Exception {
        assertEquals(0, sslPinTrustManager.getAcceptedIssuers().length);
    }
}