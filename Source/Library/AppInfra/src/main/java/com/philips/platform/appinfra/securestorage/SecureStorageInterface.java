/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

/**
 * The interface Secure storage interface.
 */
public  interface SecureStorageInterface {


    /**
     * Store value for key .
     *
     * @param userKey            the user key
     * @param valueToBeEncrypted the value to be encrypted
     * @param secureStorageError the secure storage error
     * @return  denote store operation success or failure
     */
    public boolean storeValueForKey(String userKey, String valueToBeEncrypted,  SecureStorageError secureStorageError);

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
     * @return  denote delete operation success or failure
     */
    public boolean removeValueForKey(String userKey);

    /**
     * encrypt Data .
     *
     * @param  dataToBeEncrypted Plain Byte array
     * @return  Encrypted Byte array
     */
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError);

    /**
     * decrypt Data .
     *
     * @param  dataToBeDecrypted Encrypted Byte array
     * @return  Decrypted/Plain Byte array
     */
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError);

    public class SecureStorageError {
        public  enum  secureStorageError {AccessKeyFailure, UnknownKey , EncryptionError, DecryptionError, StoreError   , NoDataFoundForKey, NullData };
        private secureStorageError errorCode = null;
        public secureStorageError getErrorCode() {
            return errorCode;
        }

        void setErrorCode(secureStorageError errorCode) {
            this.errorCode = errorCode;
        }



    }
}
