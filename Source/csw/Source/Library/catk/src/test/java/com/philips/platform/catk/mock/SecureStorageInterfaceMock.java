package com.philips.platform.catk.mock;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.Key;

public class SecureStorageInterfaceMock implements SecureStorageInterface {

    public String valueToReturn;
    public String storeValueForKey_Key;
    public String storeValueForKey_Value;
    public String fetchValueForKey_Key;
    public String removeValueForKey_Key;

    @Override
    public boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError) {
        storeValueForKey_Key = userKey;
        storeValueForKey_Value = valueToBeEncrypted;
        return true;
    }

    @Override
    public boolean storeValueForKey(String userKey, byte[] valueToBeEncrypted, SecureStorageError secureStorageError) {
        return false;
    }

    @Override
    public String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
        fetchValueForKey_Key = userKey;
        return valueToReturn;
    }

    @Override
    public byte[] fetchByteArrayForKey(String userKey, SecureStorageError secureStorageError) {
        return new byte[0];
    }

    @Override
    public boolean removeValueForKey(String userKey) {
        removeValueForKey_Key = userKey;
        return true;
    }

    @Override
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
        return false;
    }

    @Override
    public boolean doesStorageKeyExist(String key) {
        return false;
    }

    @Override
    public boolean doesEncryptionKeyExist(String key) {
        return false;
    }

    @Override
    public Key getKey(String keyName, SecureStorageError error) {
        return null;
    }

    @Override
    public boolean clearKey(String keyName, SecureStorageError error) {
        return false;
    }

    @Override
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError) {
        return new byte[0];
    }

    @Override
    public void encryptBulkData(byte[] dataToBeEncrypted, DataEncryptionListener dataEncryptionListener) {

    }

    @Override
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
        return new byte[0];
    }

    @Override
    public void decryptBulkData(byte[] dataToBeDecrypted, DataDecryptionListener dataDecryptionListener) {

    }

    @Override
    public boolean isCodeTampered() {
        return false;
    }

    @Override
    public boolean isEmulator() {
        return false;
    }

    @Override
    public String getDeviceCapability() {
        return null;
    }

    @Override
    public boolean deviceHasPasscode() {
        return false;
    }
}
