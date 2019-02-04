package com.janrain.android;

/**
 * Fetch, store and remove user key from secure storage.
 */
public interface StorageInterface {
    String fetchValueForKey(String userKey);
    void storeValueForKey(String userKey, String valueToBeEncrypted);
    void removeValueForKey(String userKey);
}
