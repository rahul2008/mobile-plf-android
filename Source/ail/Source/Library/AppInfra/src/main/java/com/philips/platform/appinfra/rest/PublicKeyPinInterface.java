package com.philips.platform.appinfra.rest;


import java.security.cert.X509Certificate;

public interface PublicKeyPinInterface {

    void updatePublicPins(String hostName, String publicKeyDetails);
    void validatePublicPins(String hostName, X509Certificate[] chain);
}
