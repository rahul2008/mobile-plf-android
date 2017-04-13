/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;



import java.security.Key;

/**
 * The interface Secure storage interface.
 */
public interface SecureStorageInterface {


    /**
     * Store value for key .
     *
     * @param userKey            the user key
     * @param valueToBeEncrypted the value to be encrypted
     * @param secureStorageError the secure storage error
     * @return denote store operation success or failure
     */
    public boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError);

    /**
     * Fetch value for key .
     *
     * @param userKey            the user key
     * @param secureStorageError the secure storage error code if any
     * @return the string, decrypted value
     */
    public String fetchValueForKey(String userKey, SecureStorageError secureStorageError);

    /**
     * Remove value for given key .
     *
     * @param userKey the user key
     * @return denote delete operation success or failure
     */
    public boolean removeValueForKey(String userKey);



    enum KeyTypes {AES};

    /**
     * Store value for Create Key  .
     *
     * @param keyType the  keytype of encrypt
     * @param keyName            the name of key
     * @param error the secure storage error
     * @return denote store operation success or failure
     */
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error);

    /**
     * Retrieve value for Create Key .
     *
     * @param keyName the user key to access the password
     * @param error the secure storage error code if any
     * @return the string, decrypted value
     */
    public Key getKey(String keyName, SecureStorageError error) ;

    /**
     * Remove value for passWord .
     *
     * @param keyName the user key to access the password
     * @param error the secure storage error code if any
     * @return denote delete operation success or failure
     */
    public boolean clearKey(String keyName, SecureStorageError error) ;



    /**
     * encrypt Data .
     *
     * @param dataToBeEncrypted Plain Byte array
     * @return Encrypted Byte array
     */
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError);

    /**
     * decrypt Data .
     *
     * @param dataToBeDecrypted Encrypted Byte array
     * @return Decrypted/Plain Byte array
     */
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError);

    public class SecureStorageError {
        public enum secureStorageError {AccessKeyFailure, UnknownKey, EncryptionError, DecryptionError, StoreError,DeleteError, NoDataFoundForKey, NullData}

        ;
        private secureStorageError errorCode = null;

        public secureStorageError getErrorCode() {
            return errorCode;
        }

        void setErrorCode(secureStorageError errorCode) {
            this.errorCode = errorCode;
        }


    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    String getDeviceCapability();

    /**
     *  checks if device is secured with pin/password/pattern.
     * @return <code>true</code> if the device is secured with pin/pattern/password
     */
    boolean deviceHasPasscode();
}
