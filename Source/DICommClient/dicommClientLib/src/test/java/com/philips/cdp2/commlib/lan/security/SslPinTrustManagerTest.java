/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.security;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SslPinTrustManagerTest extends RobolectricTest {
    private SslPinTrustManager sslPinTrustManager;

    private final String ALGORITHM = "don't care";

    @Mock
    private X509Certificate certificateMock;

    @Mock
    private PublicKey publicKeyMock;

    @Mock
    private NetworkNode networkNodeMock;

    private byte[] encodedPublicKey = new byte[4];

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        sslPinTrustManager = new SslPinTrustManager(networkNodeMock);

        when(certificateMock.getPublicKey()).thenReturn(publicKeyMock);
        when(publicKeyMock.getEncoded()).thenReturn(encodedPublicKey);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void whenUsingManagerForClientTrusted_ThenShouldThrowException() {
        try {
            sslPinTrustManager.checkClientTrusted(null, null);
        } catch (CertificateException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void whenCheckingUnpinnedCertificate_ThenCertificateIsAccepted() {
        try {
            sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, ALGORITHM);
        } catch (CertificateException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void whenCheckingPinnedCertificate_ThenCertificateIsAccepted() {
        try {
            sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, ALGORITHM);
            sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, ALGORITHM);
        } catch (CertificateException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = PinMismatchException.class)
    public void whenCheckingDifferentCertificateThanPinned_ThenCertificateIsRejected() throws Exception {
        when(networkNodeMock.getPin()).thenReturn("1234567890123456789012345678901234567890123");

        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, ALGORITHM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenChainIsNull_ThenCertificateIsRejected() throws Exception {
        sslPinTrustManager.checkServerTrusted(null, ALGORITHM);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenChainIsEmpty_ThenCertificateIsRejected() throws Exception {
        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{}, ALGORITHM);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAuthTypeIsNotSet_ThenCertificateIsRejected() throws Exception {
        sslPinTrustManager.checkServerTrusted(new X509Certificate[]{certificateMock}, null);
        fail();
    }

    @Test
    public void whenGettingAcceptedIssuers_ThenReturnEmptyArray() {
        assertEquals(0, sslPinTrustManager.getAcceptedIssuers().length);
    }
}
