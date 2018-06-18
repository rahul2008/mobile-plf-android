package com.philips.platform.appinfra.rest.hpkp;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.util.HashMap;

class HPKPStorageHelper {

    private static final String STORAGE_KEY_PREFIX = "SSL_HPKP_";
    private SecureStorageInterface secureStorageInterface;
    private HashMap<String, String> publicKeyPinCache;

    HPKPStorageHelper(SecureStorageInterface secureStorage) {
        this.secureStorageInterface = secureStorage;
        publicKeyPinCache = new HashMap<>();
    }

    String getStoredPublicKeyDetails(String hostName) {
        String storedPublicKeyDetails;
        if (publicKeyPinCache.containsKey(hostName)) {
            storedPublicKeyDetails = publicKeyPinCache.get(hostName);
        } else {
            storedPublicKeyDetails = secureStorageInterface.fetchValueForKey(STORAGE_KEY_PREFIX + hostName, getSecureStorageError());
            if (storedPublicKeyDetails != null) {
                publicKeyPinCache.put(hostName, storedPublicKeyDetails);
            }
        }
        return storedPublicKeyDetails == null ? "" : storedPublicKeyDetails;
    }

    boolean updateStoredPublicKeyDetails(String hostName, String publicKeyDetails) {
        publicKeyPinCache.put(hostName, publicKeyDetails);
        return secureStorageInterface.storeValueForKey(STORAGE_KEY_PREFIX + hostName, publicKeyDetails, getSecureStorageError());
    }

    private SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }
}
