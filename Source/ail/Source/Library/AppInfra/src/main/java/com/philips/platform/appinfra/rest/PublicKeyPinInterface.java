package com.philips.platform.appinfra.rest;


import java.security.cert.X509Certificate;
import java.util.List;

public interface PublicKeyPinInterface {

    void updatePublicPins(String hostName, String publicKeyDetails);
    void validatePublicPins(String hostName, List<X509Certificate> chain);
}
