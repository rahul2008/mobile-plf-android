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

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("Certificate chain is null.");
        }

        if (chain.length == 0) {
            throw new IllegalArgumentException("Certificate chain is empty.");
        }

        if (authType == null || authType.isEmpty()) {
            throw new IllegalArgumentException("Invalid key exchange algorithm.");
        }

        PublicKeyPin certificatePin = new PublicKeyPin(chain[0]);
        PublicKeyPin networkNodePin;

        if (networkNode.getPin() == null) {
            networkNodePin = certificatePin;
            networkNode.setPin(networkNodePin.toString());
            DICommLog.i(TAG, "Added pin for appliance with cppid " + networkNode.getCppId());
        } else {
            try {
                networkNodePin = new PublicKeyPin(networkNode.getPin());
            } catch (IllegalArgumentException e) {
                throw new CertificateException(e);
            }
        }

        if (certificatePin.equals(networkNodePin)) {
            DICommLog.d(TAG, "Certificate is accepted.");
        } else {
            DICommLog.e(TAG, "Pin mismatch for appliance with cppid " + networkNode.getCppId());
            networkNode.setMismatchedPin(certificatePin.toString());

            throw new PinMismatchException("The appliance's certificate doesn't match the stored pin.");
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
