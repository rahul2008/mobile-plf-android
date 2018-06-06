package com.philips.platform.appinfra.securestoragev1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.Key;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by abhishek on 3/27/18.
 */

public class SecureStorageV1 implements SecureStorageInterface {
    private static final long serialVersionUID = -6433107689089347839L;
    private static final String DATA_FILE_NAME = "AppInfra.Storage.file";
    private static final String KEY_FILE_NAME = "AppInfra.Storage.kfile";
    private static final String SINGLE_AES_KEY_TAG = "AppInfra.aes";
    private static final String SS_WRAP_KEY = "AppInfra.ss";
    private static final String SECURE_KEYS_FILE = "AppInfra.StoragePlainKey.kfile";
    private final Context mContext;
    private final AppInfra mAppInfra;

    private final Lock writeLock;
    private final Lock readLock;
    private transient SecureStorageHelper secureStorageHelper;
    public static final String VERSION="v1";
    private String TAG = getClass().getSimpleName();

    public SecureStorageV1(AppInfra bAppInfra) {
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
                    log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Encrypted Data" + encryptedString);
                }
            } catch (Exception e) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
                returnResult = false;
                log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage");
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
                log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage");
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
            returnResult = secureStorageHelper.storeKey(keyName, secretKey, SECURE_KEYS_FILE);
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            returnResult = false;
            log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage  SqlCipher Data Key ");
        } finally {
            writeLock.unlock();
        }
        return returnResult;

    }

    @Override
    public boolean doesStorageKeyExist(String keyName) {
        return secureStorageHelper.isKeyAvailable(keyName,DATA_FILE_NAME);
    }

    @Override
    public boolean doesEncryptionKeyExist(String name) {
        return secureStorageHelper.isKeyAvailable(name, SECURE_KEYS_FILE);
    }

    @Override
    public Key getKey(String keyName, SecureStorageError error) {
        Key decryptedKey;
        if (null == keyName || keyName.isEmpty()) {
            error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null;
        }
        final String encryptedAESString = secureStorageHelper.fetchEncryptedData(keyName, error, SECURE_KEYS_FILE);
        if (null == encryptedAESString) {
            error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null; // if user entered key is not present
        }
        try {
            decryptedKey = secureStorageHelper.fetchKey(encryptedAESString, error);
        } catch (Exception e) {
            log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error SecureStorage SqlCipher Data Key");
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
        deleteResultValue = secureStorageHelper.deleteEncryptedData(keyName, SECURE_KEYS_FILE);
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
            encryptedBytes = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, getSecretKey(secureStorageError), dataToBeEncrypted);
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "EncryptionError");
        }
        return encryptedBytes;
    }

    @Override
    public void encryptBulkData(final byte[] dataToBeEncrypted, final DataEncryptionListener dataEncryptionListener) {
        HandlerThread handlerThread=new HandlerThread("EncryptionWorkerThread");
        handlerThread.start();
        Looper looper=handlerThread.getLooper();
        Handler handler=new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                SecureStorageError secureStorageError=new SecureStorageError();
                byte[] encryptedData=encryptData(dataToBeEncrypted,secureStorageError);
                if(secureStorageError.getErrorCode()==null){
                    dataEncryptionListener.onEncryptionSuccess(encryptedData);
                }else{
                    dataEncryptionListener.onEncryptionError(secureStorageError);
                }

            }
        });
    }

    @Override
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
        if (null == dataToBeDecrypted) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, getSecretKey(secureStorageError), dataToBeDecrypted); // decrypt data using AES
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "DecryptionError");
        }
        return decryptedBytes;
    }
    @Override
    public void decryptBulkData(final byte[] dataToBeDecrypted, final DataDecryptionListener dataDecryptionListener) {
        HandlerThread handlerThread=new HandlerThread("EncryptionWorkerThread");
        handlerThread.start();
        Looper looper=handlerThread.getLooper();
        Handler handler=new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                SecureStorageError secureStorageError=new SecureStorageError();
                byte[] decryptedData=decryptData(dataToBeDecrypted,secureStorageError);
                if(secureStorageError.getErrorCode()==null){
                    dataDecryptionListener.onDecryptionSuccess(decryptedData);
                }else{
                    dataDecryptionListener.onDecyptionError(secureStorageError);
                }

            }
        });
    }

    Key getSecretKey(SecureStorageError secureStorageError) {
        Key key = null;
        try {
            SharedPreferences keySharedPreferences = getSharedPreferences();
            if (keySharedPreferences.contains(SINGLE_AES_KEY_TAG)) { // if  key is present
                final String aesKeyForEncryptDecrypt = keySharedPreferences.getString(SINGLE_AES_KEY_TAG, null);
                byte[] secretKeyBytes = Base64.decode(aesKeyForEncryptDecrypt, Base64.DEFAULT);//  AES key bytes
                SharedPreferences.Editor editor = keySharedPreferences.edit();
                editor.remove(SINGLE_AES_KEY_TAG);
                editor.apply();
                key = new SecretKeySpec(secretKeyBytes, "AES");
                secureStorageHelper.storeKey(SS_WRAP_KEY, (SecretKey) key, KEY_FILE_NAME);
            } else if (keySharedPreferences.contains(SS_WRAP_KEY)) {
                final String aesKeyForEncryptDecrypt = keySharedPreferences.getString(SS_WRAP_KEY, null);
                key = secureStorageHelper.fetchKey(aesKeyForEncryptDecrypt, secureStorageError);
            } else {
                key = secureStorageHelper.generateAESKey(); // generate AES key
                secureStorageHelper.storeKey(SS_WRAP_KEY, (SecretKey) key, KEY_FILE_NAME);
            }
        } catch (Exception e) {
            log(LoggingInterface.LogLevel.DEBUG, "getCipher error", e.getMessage());
        }
        return key;
    }

    SharedPreferences getSharedPreferences() {
        return secureStorageHelper.getSharedPreferences(KEY_FILE_NAME);
    }

    @Override
    public String getDeviceCapability() {
        //Empty implementation as functionality is moved to common class
        return null;

    }

    @Override
    public boolean deviceHasPasscode() {
        //Empty implementation as functionality is moved to common class
        return false;
    }

    @Override
    public boolean isCodeTampered() {
        return false;
    }

    @Override
    public boolean isEmulator() {
        return false;
    }

    private void log(LoggingInterface.LogLevel logLevel, String eventId, String message) {
        if(mAppInfra !=null && mAppInfra.getAppInfraLogInstance()!=null) {
            mAppInfra.getAppInfraLogInstance().log(logLevel,eventId,message);
        }
    }
}
