package com.philips.platform.appinfra.securestorage;

/**
 * Created by 310238114 on 6/23/2016.
 */
public class SecureStorageError {
    public  enum  secureStorageError {AccessKeyFailure, UnknownKey , EncryptionError, DecryptionError, StoreError   , NoDataFoundForKey };
    private secureStorageError errorCode = null;
    public secureStorageError getErrorCode() {
        return errorCode;
    }

    void setErrorCode(secureStorageError errorCode) {
        this.errorCode = errorCode;
    }



}
