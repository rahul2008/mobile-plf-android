/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;


import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;
import com.philips.platform.appinfra.securestoragev2.SSUtils;
import com.philips.platform.appinfra.securestoragev2.SecureStorageV2;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.ArrayList;

/**
 * This class is used to manage multiple SS instance. This will redirect to proper SS instance based on key provided by user.
 */
public class SecureStorage implements SecureStorageInterface {

    private AppInfra mAppInfra;


    private SecureStorageInterface latestSecureStorage;

    private Context context;


    public SecureStorage(AppInfra bAppInfra) {
        mAppInfra = bAppInfra;
        latestSecureStorage = getLatestSecureStorage();
        context = mAppInfra.getAppInfraContext();
    }

    @NonNull
    protected SecureStorageInterface getLatestSecureStorage() {
        return new SecureStorageV2(mAppInfra);
    }


    @Override
    public boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError) {
        SecureStorageInterface secureStorageInterface=getOldSSInstanceWhereKeyIsAvailable(userKey);
        if(secureStorageInterface!=null){
            secureStorageInterface.removeValueForKey(userKey);
        }
        return latestSecureStorage.storeValueForKey(userKey, valueToBeEncrypted, secureStorageError);
    }


    @Override
    public String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
        if (latestSecureStorage.doesStorageKeyExist(userKey)) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Available in latest module :: Key" + userKey);
            return latestSecureStorage.fetchValueForKey(userKey, secureStorageError);
        } else if (doesKeyExistInOldSS(userKey)) {
            return migrateDataFromOldToNewSS(userKey, secureStorageError);
        } else {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null;
        }
    }

    @Override
    public boolean removeValueForKey(String userKey) {
        boolean isCleared = false;
        for (SecureStorageInterface secureStorage : getAllSecureStorageList()) {
            if (secureStorage.doesStorageKeyExist(userKey)) {
                isCleared = true;
                secureStorage.removeValueForKey(userKey);
            }
        }
        return isCleared;
    }

    @Override
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
        if (doesSecureKeyExistInOldSS(keyName)) {
            error.setErrorCode(SecureStorageError.secureStorageError.SecureKeyAlreadyPresent);
            return false;
        }
        return latestSecureStorage.createKey(keyType, keyName, error);
    }


    @Override
    public boolean doesStorageKeyExist(String name) {
        //Check whether key exist in all SS.
        for (SecureStorageInterface secureStorage : getAllSecureStorageList()) {
            if (secureStorage.doesStorageKeyExist(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doesEncryptionKeyExist(String name) {
        //Check whether secure key exist in all SS
        for (SecureStorageInterface secureStorage : getAllSecureStorageList()) {
            if (secureStorage.doesEncryptionKeyExist(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Key getKey(String keyName, SecureStorageError error) {
        //Returns Secure Key for provided user key.
        if (latestSecureStorage.doesEncryptionKeyExist(keyName)) {
            return latestSecureStorage.getKey(keyName, error);
        } else {
            SecureStorageInterface secureStorage = getOldSSInstanceWhereSecureKeyIsAvailable(keyName);
            if (secureStorage != null) {
                return secureStorage.getKey(keyName, error);
            }
        }
        return null;
    }

    @Override
    public boolean clearKey(String keyName, SecureStorageError error) {
        //Clears secure key for provided user key
        boolean isCleared = false;
        for (SecureStorageInterface secureStorage : getAllSecureStorageList()) {
            if (secureStorage.doesEncryptionKeyExist(keyName)) {
                isCleared = true;
                secureStorage.clearKey(keyName, error);
            }
        }
        return isCleared;
    }

    @Override
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError) {
        return latestSecureStorage.encryptData(dataToBeEncrypted, secureStorageError);
    }

    @Override
    public void encryptBulkData(byte[] dataToBeEncrypted, DataEncryptionListener dataEncryptionListener) {
        latestSecureStorage.encryptBulkData(dataToBeEncrypted, dataEncryptionListener);
    }

    @Override
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
        if (dataToBeDecrypted == null) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        String ssVersion = getSecureStorageVersion(dataToBeDecrypted);
        return getSecureStorage(ssVersion).decryptData(dataToBeDecrypted, secureStorageError);
    }

    @NonNull
    protected String getSecureStorageVersion(byte[] dataToBeDecrypted) {
        ByteBuffer byteBuffer = getWrappedByteArray(dataToBeDecrypted);
        int ssVersionLenth = byteBuffer.getInt();
        String ssVersion= SecureStorageV1.VERSION;
        if(ssVersionLenth<=2) {
            byte[] version = new byte[ssVersionLenth];
            byteBuffer.get(version);
            ssVersion = new String(version);
        }
        return ssVersion;
    }

    protected ByteBuffer getWrappedByteArray(byte[] dataToBeWrapped) {
        return ByteBuffer.wrap(dataToBeWrapped);
    }

    @Override
    public void decryptBulkData(byte[] dataToBeDecrypted, DataDecryptionListener dataDecryptionListener) {
        String ssVersion = getSecureStorageVersion(dataToBeDecrypted);
        getSecureStorage(ssVersion).decryptBulkData(dataToBeDecrypted, dataDecryptionListener);
    }

    protected SecureStorageInterface getSecureStorage(String version) {
        switch (version) {
            case SecureStorageV2.VERSION:
                return new SecureStorageV2(mAppInfra);
            case SecureStorageV1.VERSION:
            default:
                return new SecureStorageV1(mAppInfra);
        }
    }


    @Override
    public String getDeviceCapability() {
        return SSUtils.getDeviceCapability();
    }

    @Override
    public boolean deviceHasPasscode() {
        return SSUtils.deviceHasPasscode(context);
    }

    protected String migrateDataFromOldToNewSS(String userKey, SecureStorageError secureStorageError) {
        SecureStorageInterface oldSecureStorageInstance = getOldSSInstanceWhereKeyIsAvailable(userKey);
        if (oldSecureStorageInstance != null) {
            String data = oldSecureStorageInstance.fetchValueForKey(userKey, secureStorageError);
            if(data!=null) {
                boolean isSuccessful = latestSecureStorage.storeValueForKey(userKey, data, secureStorageError);
                if (isSuccessful) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Migrated to latest SS:: Key" + userKey);
                    boolean isDeleted = oldSecureStorageInstance.removeValueForKey(userKey);
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Is deleted from old SS? ::" + isDeleted);
                    return data;
                } else {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Failed to migrate to latest SS:: Key" + userKey);
                    return data;
                }
            }else{
                return null;
            }

        } else {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null;

        }
    }

    /**
     * Check availability of key in old SS and return same. If null means not present in Old SS's
     *
     * @param key
     * @return
     */
    protected boolean doesKeyExistInOldSS(String key) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Checking availability in old SS:: Key" + key);
        for (SecureStorageInterface secureStorage : getListOfOldSecureStorage()) {
            if (secureStorage.doesStorageKeyExist(key)) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Found in old ss:: Key" + key);
                return true;
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Not available in old SS:: Key" + key);
        return false;
    }

    /**
     * Check availability of key in old SS and return same. If null means not present in Old SS's
     *
     * @param key
     * @return
     */
    protected boolean doesSecureKeyExistInOldSS(String key) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Checking secure key availability in old SS:: Key" + key);
        for (SecureStorageInterface secureStorage : getListOfOldSecureStorage()) {
            if (secureStorage.doesEncryptionKeyExist(key)) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Found secure key in old ss:: Key" + key);
                return true;
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Secure key Not available in old SS:: Key" + key);
        return false;
    }

    /**
     * Check availability of key in old SS and return same. If null means not present in Old SS's
     *
     * @param key
     * @return
     */
    protected SecureStorageInterface getOldSSInstanceWhereSecureKeyIsAvailable(String key) {
        for (SecureStorageInterface secureStorage : getListOfOldSecureStorage()) {
            if (secureStorage.doesEncryptionKeyExist(key)) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Found in old ss:: Key" + key);
                return secureStorage;
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Not available in old SS:: Key" + key);
        return null;
    }

    /**
     * Check availability of key in old SS and return same. If null means not present in Old SS's
     *
     * @param key
     * @return
     */
    protected SecureStorageInterface getOldSSInstanceWhereKeyIsAvailable(String key) {
        for (SecureStorageInterface secureStorage : getListOfOldSecureStorage()) {
            if (secureStorage.doesStorageKeyExist(key)) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Found in old ss:: Key" + key);
                return secureStorage;
            }
        }
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Not available in old SS:: Key" + key);
        return null;
    }

    /**
     * Returns list of all SS.
     *
     * @return
     */
    protected ArrayList<SecureStorageInterface> getAllSecureStorageList() {
        ArrayList<SecureStorageInterface> allSSList = new ArrayList<>();
        allSSList.add(getSecureStorage(SecureStorageV1.VERSION));
        allSSList.add(getSecureStorage(SecureStorageV2.VERSION));
        return allSSList;
    }

    /**
     * Returns list of old SS.
     *
     * @return
     */
    protected ArrayList<SecureStorageInterface> getListOfOldSecureStorage() {
        ArrayList<SecureStorageInterface> oldSSList = new ArrayList<>();
        oldSSList.add(getSecureStorage(SecureStorageV1.VERSION));
        return oldSSList;
    }
}
