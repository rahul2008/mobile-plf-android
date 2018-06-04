package com.philips.platform.appinfra.rest;


import java.security.cert.X509Certificate;
import java.util.List;

/**
 * The SSL Public Key pinning Interface .
 */
public interface PublicKeyPinInterface {

    /**
     * Persistently store and update public key for a particular hostname.
     * If there is a mismatch in the stored value and the provided value, new value is to be updated.
     *
     * @param hostName hostname of a URL which would be used as key to store the public key in secure storage
     * @param publicKeyDetails  Public key details of a particular hostname
     * @since 2018.2.0
     */
    void updatePinnedPublicKey(String hostName, String publicKeyDetails);

    /**
     * Validate if the stored public key for a particular hostname matches the public key in the chain of certificates for the same hostname
     *
     * @param hostName country code to persistently store, code must be according to ISO 3166-1
     * @param chain Chain of certificates provided by the server for a particular hostname.
     * @since 2018.2.0
     */
    boolean isPinnedCertificateMatching(String hostName, List<X509Certificate> chain);
}
