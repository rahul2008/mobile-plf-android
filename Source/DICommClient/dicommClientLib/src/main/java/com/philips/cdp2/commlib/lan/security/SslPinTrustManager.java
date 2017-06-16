/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.security;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class SslPinTrustManager implements X509TrustManager {
    private static final String TAG = "SslPinTrustManager";
    private final NetworkNode networkNode;

    public SslPinTrustManager(NetworkNode networkNode) {
        this.networkNode = networkNode;
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // This method will not be used
        throw new UnsupportedOperationException();
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        PublicKeyPin certificatePin = new PublicKeyPin(chain[0]);
        PublicKeyPin networkNodePin;

        if (networkNode.getPin() == null) {
            networkNodePin = certificatePin;
            networkNode.setPin(networkNodePin.toString());
        } else {
            networkNodePin = new PublicKeyPin(networkNode.getPin());
        }

        if (certificatePin.equals(networkNodePin)) {
            DICommLog.d(TAG, "Certificate is accepted");
        } else {
            throw new CertificateException();
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
