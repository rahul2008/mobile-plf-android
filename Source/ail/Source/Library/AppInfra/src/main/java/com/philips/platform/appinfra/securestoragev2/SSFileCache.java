package com.philips.platform.appinfra.securestoragev2;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Created by abhishek on 2/5/18.
 */

public class SSFileCache {
    private static final String SS_KEYS_FILE_STORE ="AppInfra.sskeys";

    private static final String SS_DATA_FILE_STORE="AppInfra.ssdata";

    private AppInfra appInfra;

    public SSFileCache(AppInfra appInfra){
        this.appInfra=appInfra;
    }

    public String getRSAWrappedAESKeyFromFileCache(String keyId){
        SharedPreferences secureStorageSharedPrefs = getSharedPreferences(SS_KEYS_FILE_STORE);
        return secureStorageSharedPrefs.getString(keyId, null);
    }

    public boolean putRSAWrappedAESKeyInFileCache(String keyId,String value){
        SharedPreferences secureStorageSharedPrefs = getSharedPreferences(SS_KEYS_FILE_STORE);
        SharedPreferences.Editor editor = secureStorageSharedPrefs.edit();
        editor.putString(keyId, value);
        editor.commit();
        return true;
    }


    public boolean deleteKey(String keyId) {
        boolean deleteResult = false;
        try {
            SharedPreferences mAppInfraPrefs = getSharedPreferences(SS_KEYS_FILE_STORE);
            // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
            if (mAppInfraPrefs.contains(keyId)) {  // if given key is present in SharedPreferences
                // encrypted data will be deleted from device  SharedPreferences
                SharedPreferences.Editor editor = mAppInfraPrefs.edit();
                editor.remove(keyId);
                deleteResult = editor.commit();
            }

        } catch (Exception e) {
            appInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in S-Storage when deleting e-data"+e.getMessage());
            deleteResult = false;
            return deleteResult;
        }
        return deleteResult;
    }

    public boolean putEncryptedString(String key, String value){
        SharedPreferences secureStorageSharedPrefs = getSharedPreferences(SS_DATA_FILE_STORE);
        SharedPreferences.Editor editor = secureStorageSharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public String getEncryptedString(String key){
        SharedPreferences secureStorageSharedPrefs = getSharedPreferences(SS_DATA_FILE_STORE);
        return secureStorageSharedPrefs.getString(key, null);
    }

    public boolean deleteEncryptedData(String key) {
        boolean deleteResult = false;
        try {
            SharedPreferences mAppInfraPrefs = getSharedPreferences(SS_DATA_FILE_STORE);
            // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
            if (mAppInfraPrefs.contains(key)) {  // if given key is present in SharedPreferences
                // encrypted data will be deleted from device  SharedPreferences
                SharedPreferences.Editor editor = mAppInfraPrefs.edit();
                editor.remove(key);
                deleteResult = editor.commit();
            }

        } catch (Exception e) {
            appInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in S-Storage when deleting e-data"+e.getMessage());
            deleteResult = false;
            return deleteResult;

        }
        return deleteResult;
    }



    protected SharedPreferences getSharedPreferences(String fileName) {
        return appInfra.getAppInfraContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
