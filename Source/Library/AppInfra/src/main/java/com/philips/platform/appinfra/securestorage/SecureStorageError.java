package com.philips.platform.appinfra.securestorage;


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
