/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.util.Base64;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.io.File;
import java.security.Key;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.philips.platform.appinfra.securestorage.SecureStorageHelper.AES_ENCRYPTION_ALGORITHM;


/**
 * The class for store the key and value pair using AES Encryption with Cipher and Keystore .
 * Value will stored in SharedPrefrence in encrypted manner.
 * And also we can use as Create and store password securly using Create.
 * Current Implementation changed to MAIL-161.
 */
public class SecureStorage implements SecureStorageInterface {

    private static final String DATA_FILE_NAME = "AppInfra.Storage.file";
    private static final String KEY_FILE_NAME = "AppInfra.Storage.kfile";
    private static final String SINGLE_AES_KEY_TAG = "AppInfra.aes";
    private static final String SS_WRAP_KEY = "AppInfra.ss";
    private static final String KEY_FILE_NAME_FOR_PLAIN_KEY = "AppInfra.StoragePlainKey.kfile";
    private final Context mContext;
    private final AppInfra mAppInfra;

    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    private final Lock writeLock;
    private final Lock readLock;
    private SecureStorageHelper secureStorageHelper;
    private String TAG = getClass().getSimpleName();

    public SecureStorage(AppInfra bAppInfra) {
        mAppInfra = bAppInfra;
        mContext = mAppInfra.getAppInfraContext();
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        writeLock = reentrantReadWriteLock.writeLock();
        readLock = reentrantReadWriteLock.readLock();
        secureStorageHelper = new SecureStorageHelper(mAppInfra);
    }

    @Override
    public boolean storeValueForKey(String userKey, final String valueToBeEncrypted, final SecureStorageError secureStorageError) {
        // TODO: RayKlo: define max size limit recommendation
        boolean returnResult;
        try {
            writeLock.lock();
            if (null == userKey || userKey.isEmpty() || userKey.trim().isEmpty() || null == valueToBeEncrypted) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return false;
            }
            userKey = secureStorageHelper.getDecodedString(userKey);
            final String userKeyFinal = userKey;
            try {
                final SecretKey secretKey = secureStorageHelper.generateAESKey(); // generate AES key
                String encryptedString = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, valueToBeEncrypted);
                returnResult = secureStorageHelper.storeEncryptedData(userKeyFinal, encryptedString, DATA_FILE_NAME);// save encrypted value in data file
                if (returnResult) {
                    returnResult = secureStorageHelper.storeKey(userKeyFinal, secretKey, KEY_FILE_NAME);
                    if (!returnResult) { // if key is not saved then remove previously saved value
                        secureStorageHelper.deleteEncryptedData(userKeyFinal, DATA_FILE_NAME);
                    }
                } else {
                    // storing failed in shared preferences
                    secureStorageError.setErrorCode(SecureStorageError.secureStorageError.StoreError);
                }
                encryptedString = returnResult ? encryptedString : null; // if save of encryption data fails return null
                final boolean isDebuggable = (0 != (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
                if (isDebuggable) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Encrypted Data" + encryptedString);
                }
            } catch (Exception e) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
                returnResult = false;
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage");
                //Log.e("SecureStorage", Log.getStackTraceString(e));
            }

        } finally {
            writeLock.unlock();
        }
        return returnResult;

    }

    @Override
    public String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
        String decryptedString;
        try {
            readLock.lock();
            if (null == userKey || userKey.isEmpty()) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return null;
            }
            userKey = secureStorageHelper.getDecodedString(userKey);
            final String encryptedString = secureStorageHelper.fetchEncryptedData(userKey, secureStorageError, DATA_FILE_NAME);
            final String encryptedAESString = secureStorageHelper.fetchEncryptedData(userKey, secureStorageError, KEY_FILE_NAME);
            if (null == encryptedString || null == encryptedAESString) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return null; // if user entered key is not present
            }
            try {
                final Key key = secureStorageHelper.fetchKey(encryptedAESString, secureStorageError);
                decryptedString = secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, key, encryptedString);
            } catch (Exception e) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage");
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
                decryptedString = null; // if exception is thrown at:  decryptedString = new String(decText);
            }
        } finally {
            readLock.unlock();
        }
        return decryptedString;

    }

    @Override
    public boolean removeValueForKey(String userKey) {
        boolean deleteResultValue;
        boolean deleteResultKey;
        try {
            writeLock.lock();
            if (null == userKey || userKey.isEmpty()) {
                return false;
            }
            deleteResultValue = secureStorageHelper.deleteEncryptedData(userKey, DATA_FILE_NAME);
            deleteResultKey = secureStorageHelper.deleteEncryptedData(userKey, KEY_FILE_NAME);
        } finally {
            writeLock.unlock();
        }
        return (deleteResultValue && deleteResultKey);
    }

    @Override
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
        boolean returnResult;
        try {
            writeLock.lock();
            if (null == keyName || keyName.isEmpty() || keyName.trim().isEmpty()) {
                error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return false;
            }
            final SecretKey secretKey = secureStorageHelper.generateAESKey(); // generate AES key
            returnResult = secureStorageHelper.storeKey(keyName, secretKey, KEY_FILE_NAME_FOR_PLAIN_KEY);
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            returnResult = false;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage  SqlCipher Data Key ");
            //Log.e("SecureStorage", Log.getStackTraceString(e));
        } finally {
            writeLock.unlock();
        }
        return returnResult;

    }

    @Override
    public Key getKey(String keyName, SecureStorageError error) {
        Key decryptedKey;
        if (null == keyName || keyName.isEmpty()) {
            error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null;
        }
        final String encryptedAESString = secureStorageHelper.fetchEncryptedData(keyName, error, KEY_FILE_NAME_FOR_PLAIN_KEY);
        if (null == encryptedAESString) {
            error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null; // if user entered key is not present
        }
        try {
            decryptedKey = secureStorageHelper.fetchKey(encryptedAESString, error);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error SecureStorage SqlCipher Data Key");
            error.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            decryptedKey = null; // if exception is thrown at:  decryptedString = new String(decText);
        }
        return decryptedKey;

    }

    @Override
    public boolean clearKey(String keyName, SecureStorageError error) {
        boolean deleteResultValue;
        if (null == keyName || keyName.isEmpty()) {
            return false;
        }
        deleteResultValue = secureStorageHelper.deleteEncryptedData(keyName, KEY_FILE_NAME_FOR_PLAIN_KEY);
        if (!deleteResultValue) {
            //clear or deleting failed in shared preferences
            error.setErrorCode(SecureStorageError.secureStorageError.DeleteError);
        }
        return deleteResultValue;
    }

    @Override
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError) {
        if (null == dataToBeEncrypted) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        byte[] encryptedBytes = null;
        try {
            if (null == encryptCipher) {
                encryptCipher = getCipher(Cipher.ENCRYPT_MODE, secureStorageError);
            }
            encryptedBytes = encryptCipher.doFinal(dataToBeEncrypted); // encrypt data using AES
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "EncryptionError");
        }
        return encryptedBytes;
    }

    @Override
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
        if (null == dataToBeDecrypted) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        byte[] decryptedBytes = null;
        try {
            if (null == decryptCipher) {
                decryptCipher = getCipher(Cipher.DECRYPT_MODE, secureStorageError);
            }
            decryptedBytes = decryptCipher.doFinal(dataToBeDecrypted); // decrypt data using AES
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "DecryptionError");
        }
        return decryptedBytes;
    }

    Cipher getCipher(int CipherEncryptOrDecryptMode, SecureStorageError secureStorageError) {
        Cipher cipher = null;
        Key key;
        try {
            cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            SharedPreferences keySharedPreferences = getSharedPreferences();
            if (keySharedPreferences.contains(SINGLE_AES_KEY_TAG)) { // if  key is present
                final String aesKeyForEncryptDecrypt = keySharedPreferences.getString(SINGLE_AES_KEY_TAG, null);
                byte[] secretKeyBytes = Base64.decode(aesKeyForEncryptDecrypt, Base64.DEFAULT);//  AES key bytes
                SharedPreferences.Editor editor = keySharedPreferences.edit();
                editor.remove(SINGLE_AES_KEY_TAG);
                editor.apply();
                key = new SecretKeySpec(secretKeyBytes, "AES");
                boolean storeKeySuccessfully = secureStorageHelper.storeKey(SS_WRAP_KEY, (SecretKey) key, KEY_FILE_NAME);
                if(storeKeySuccessfully){
                    Log.e(TAG, " Successfully stored RSA wrapped data with key " + SS_WRAP_KEY + " value=" + keySharedPreferences.getString(SS_WRAP_KEY, null));
                    Log.e(TAG, " Deleted previous encoded AES key " + SINGLE_AES_KEY_TAG + " value=" + keySharedPreferences.getString(SINGLE_AES_KEY_TAG, null));
                }
            } else if (keySharedPreferences.contains(SS_WRAP_KEY)) {
                final String aesKeyForEncryptDecrypt = keySharedPreferences.getString(SS_WRAP_KEY, null);
                key = secureStorageHelper.fetchKey(aesKeyForEncryptDecrypt, secureStorageError);
                Log.e(TAG, " Successfully fetched RSA wrapped data with key " + SS_WRAP_KEY + " value=" + keySharedPreferences.getString(SS_WRAP_KEY, null));
            } else {
                key = secureStorageHelper.generateAESKey(); // generate AES key
                secureStorageHelper.storeKey(SS_WRAP_KEY, (SecretKey) key, KEY_FILE_NAME);
                Log.e(TAG, " Successfully generated new AES Wrapped key " + SS_WRAP_KEY + " value=" + keySharedPreferences.getString(SS_WRAP_KEY, null));
            }
            final byte[] ivBlockSize = new byte[cipher.getBlockSize()];
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
            cipher.init(CipherEncryptOrDecryptMode, key, ivParameterSpec);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, "getCipher error", e.getMessage());
        }
        return cipher;
    }

    SharedPreferences getSharedPreferences() {
        return secureStorageHelper.getSharedPreferences(KEY_FILE_NAME);
    }

    @Override
    public String getDeviceCapability() {
        final String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return "true";
        }
        final String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
                "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return "true";
        }
        if (secureStorageHelper.checkProcess()) {
            return "true";
        }

        return "false";

    }

    @Override
    public boolean deviceHasPasscode() {
        if (mContext != null) {
            KeyguardManager manager = (KeyguardManager)
                    mContext.getSystemService(Context.KEYGUARD_SERVICE);
            return manager.isKeyguardSecure();
        }
        return false;
    }

}
