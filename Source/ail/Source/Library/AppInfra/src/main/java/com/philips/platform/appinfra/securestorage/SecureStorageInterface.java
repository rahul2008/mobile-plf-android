/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;


import java.io.Serializable;
import java.security.Key;

/**
 * The interface Secure storage interface.
 */
public interface SecureStorageInterface extends Serializable {


    /**
     * Store value for key .
     *
     * @param userKey            the user key
     * @param valueToBeEncrypted the value to be encrypted
     * @param secureStorageError the secure storage error
     * @return denote store operation success or failure
     * @since 1.0.0
     */
    boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError);

    /**
     * Fetch value for key .
     *
     * @param userKey            the user key
     * @param secureStorageError the secure storage error code if any
     * @return the string, decrypted value
     * @since 1.0.0
     */
    String fetchValueForKey(String userKey, SecureStorageError secureStorageError);

    /**
     * Remove value for given key .
     *
     * @param userKey the user key
     * @return denote delete operation success or failure
     * @since 1.0.0
     */
    boolean removeValueForKey(String userKey);



    enum KeyTypes {AES}

    /**
     * Store value for Create Key  .
     *
     * @param keyType the  keytype of encrypt
     * @param keyName            the name of key
     * @param error the secure storage error
     * @return denote store operation success or failure
     * @since 1.2.1
     */
    boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error);

    /**
     * Retrieve value for Create Key .
     *
     * @param keyName the user key to access the password
     * @param error the secure storage error code if any
     * @return the string, decrypted value
     * @since 1.2.1
     */
    Key getKey(String keyName, SecureStorageError error) ;

    /**
     * Remove value for passWord .
     *
     * @param keyName the user key to access the password
     * @param error the secure storage error code if any
     * @return denote delete operation success or failure
     * @since 1.2.1
     */
    boolean clearKey(String keyName, SecureStorageError error) ;



    /**
     * Encrypt data.
     *
     * @param dataToBeEncrypted Plain Byte array
     * @return Encrypted Byte array
     * @since 1.0.0
     */
    byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError);

    /**
     * Encrypt bulk Data.Provides asynchronous behavior
     *
     * @param dataToBeEncrypted Plain Byte array
     * @return Encrypted Byte array
     * @since 2018.1.0
     */
    void encryptBulkData(byte[] dataToBeEncrypted,DataEncryptionListener dataEncryptionListener);

    /**
     * Decrypt data.
     *
     * @param dataToBeDecrypted Encrypted Byte array
     * @return Decrypted/Plain Byte array
     * @since 1.0.0
     */
    byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError);


    /**
     * Decrypt bulk daata.Provides asynchronous behavior
     *
     * @param dataToBeDecrypted Encrypted Byte array
     * @return Decrypted/Plain Byte array
     * @since 2018.1.0
     */
    void decryptBulkData(byte[] dataToBeDecrypted,DataDecryptionListener dataDecryptionListener);

    class SecureStorageError {
        public enum secureStorageError {AccessKeyFailure, UnknownKey, EncryptionError, DecryptionError, StoreError,DeleteError, NoDataFoundForKey, NullData}

        private secureStorageError errorCode = null;

        public secureStorageError getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(secureStorageError errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            switch(errorCode){
                case AccessKeyFailure:
                    return "Not able to access key";
                case UnknownKey:
                    return "Unknown Key";
                case EncryptionError:
                    return "Encryption Error";
                case DecryptionError:
                    return "Decryption Error";
                case StoreError:
                    return "Error while saving encrypted data";
                case DeleteError:
                    return "Error while deleting";
                case NoDataFoundForKey:
                    return "Not able to find data for given key";
                case NullData:
                    return "Null data";
                default:
                    return "Error in secure storage";

            }
        }

    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     * @since 2.2.0
     */
    String getDeviceCapability();

    /**
     *  checks if device is secured with pin/password/pattern.
     * @return <code>true</code> if the device is secured with pin/pattern/password
     * @since 2.1.0
     */
    boolean deviceHasPasscode();

    /**
     * Callback will be from worker thread. Make sure that you update UI elements from Main Thread.
     */
    interface DataDecryptionListener {

        void onDecryptionSuccess(byte[] decryptedData);

        void onDecyptionError(SecureStorageError secureStorageError);
    }

    /**
     * Callback will be from worker thread. Make sure that you update UI elements from Main Thread.
     */
    interface DataEncryptionListener {

        void onEncryptionSuccess(byte[] encryptedData);

        void onEncryptionError(SecureStorageError secureStorageError);
    }
}
