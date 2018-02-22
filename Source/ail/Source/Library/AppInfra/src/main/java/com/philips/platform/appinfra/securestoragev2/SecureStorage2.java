package com.philips.platform.appinfra.securestoragev2;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.File;
import java.security.Key;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.Cipher;

public class SecureStorage2 implements SecureStorageInterface {

    private final Context mContext;
    private final AppInfra mAppInfra;
    private final Lock writeLock;
    private final Lock readLock;
    private SSEncoderDecoder ssEncoderDecoder;
    private SSKeyProvider ssKeyProvider;
    private SSFileCache ssFileCache;
    private static final String RSA_WRAPPED_AES_KEY_MAIN = "rsa_wrapped_aes_encrypted_key";

    public SecureStorage2(AppInfra bAppInfra) {
        mAppInfra = bAppInfra;
        mContext = mAppInfra.getAppInfraContext();
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        writeLock = reentrantReadWriteLock.writeLock();
        readLock = reentrantReadWriteLock.readLock();
        ssEncoderDecoder = getSSEncoderDecoder();
        ssFileCache = getSecureStorageFileCahce();
        ssKeyProvider = getSecureStorageKeyprovider();
    }

    protected SSFileCache getSecureStorageFileCahce() {
        return new SSFileCache(mAppInfra);
    }

    protected SSKeyProvider getSecureStorageKeyprovider() {
        if (Build.VERSION.SDK_INT >= 23 && !SSUtils.isDeviceVersionUpgraded()) {
            return new SSKeyProvider23Impl(mAppInfra, ssFileCache);
        } else {
            return new SSKeyProvider18Impl(mAppInfra, ssFileCache);
        }
    }

    @NonNull
    protected SSEncoderDecoder getSSEncoderDecoder() {
        return new SSEncoderDecoder();
    }

    @Override
    public boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError) {
        boolean returnResult;
        try {
            writeLock.lock();
            if (null == userKey || userKey.isEmpty() || userKey.trim().isEmpty()) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return false;
            } else if (null == valueToBeEncrypted) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
                return false;
            }
            try {
                Key secretKey = ssKeyProvider.getSecureKey(RSA_WRAPPED_AES_KEY_MAIN);
                String encryptedString = ssEncoderDecoder.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, valueToBeEncrypted);
                returnResult = ssFileCache.putEncryptedString(userKey, encryptedString);// save encrypted value in data file
                if (!returnResult) { // if encrypted data is not saved then set error code accordingly
                    secureStorageError.setErrorCode(SecureStorageError.secureStorageError.StoreError);
                }
                encryptedString = returnResult ? encryptedString : null; // if save of encryption data fails return null
                final boolean isDebuggable = (0 != (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
                if (isDebuggable) {
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, "Encrypted Data" + encryptedString);
                }
            } catch (SSKeyProviderException exception) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
                returnResult = false;
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
            } catch (SSEncodeDecodeException exception) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
                returnResult = false;
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
            } catch (Exception e) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
                returnResult = false;
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, e.getMessage());
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
            final String encryptedString = ssFileCache.getEncryptedString(userKey);
            final Key secretKey = ssKeyProvider.getSecureKey(RSA_WRAPPED_AES_KEY_MAIN);//ssEncoderDecoder.getKey(ACCESS_KEY, SECURE_STORAGE_FILE_NAME, secureStorageError);
            if (null == encryptedString || null == secretKey) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return null; // if user entered key is not present
            }
            decryptedString = ssEncoderDecoder.encodeDecodeData(Cipher.DECRYPT_MODE, secretKey, encryptedString);
        } catch (SSKeyProviderException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
            decryptedString = null;
        } catch (SSEncodeDecodeException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            decryptedString = null;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage");
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            decryptedString = null; // if exception is thrown at:  decryptedString = new String(decText);
        } finally {
            readLock.unlock();
        }
        return decryptedString;
    }

    @Override
    public boolean removeValueForKey(String userKey) {
        boolean deleteResultValue;
        try {
            writeLock.lock();
            if (null == userKey || userKey.isEmpty()) {
                return false;
            }
            deleteResultValue = ssFileCache.deleteEncryptedData(userKey);
        } finally {
            writeLock.unlock();
        }
        return (deleteResultValue);
    }

    @Override
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
        try {
            writeLock.lock();

            return ssKeyProvider.getSecureKey(keyName) != null ? true : false;
        } catch (SSKeyProviderException exception) {
            error.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage  SqlCipher Data Key ");
        } finally {
            writeLock.unlock();
        }
        return false;

    }

    @Override
    public Key getKey(String keyName, SecureStorageError error) {
        Key decryptedKey;
        try {
            if (null == keyName || keyName.isEmpty()) {
                error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return null;
            }
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
        }
        try {
            decryptedKey = ssKeyProvider.getSecureKey(keyName);
        } catch (SSKeyProviderException exception) {
            error.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
            decryptedKey = null;
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error SecureStorage SqlCipher Data Key");
            error.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            decryptedKey = null; // if exception is thrown at:  decryptedString = new String(decText);
        }
        return decryptedKey;

    }

    @Override
    public boolean clearKey(String keyName, SecureStorageError error) {
        if (null == keyName || keyName.isEmpty()) {
            return false;
        }
        try {
            boolean clearKeyStatus = ssFileCache.deleteKey(keyName);
            if (!clearKeyStatus) {
                error.setErrorCode(SecureStorageError.secureStorageError.DeleteError);
            }
            return clearKeyStatus;
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.DeleteError);
            return false;
        }
    }

    @Override
    public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError) {
        if (null == dataToBeEncrypted) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        byte[] encryptedBytes = null;
        try {
            encryptedBytes = ssEncoderDecoder.encodeDecodeData(Cipher.ENCRYPT_MODE, ssKeyProvider.getSecureKey(RSA_WRAPPED_AES_KEY_MAIN), dataToBeEncrypted);
        } catch (SSKeyProviderException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (SSEncodeDecodeException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "EncryptionError");
        }
        return encryptedBytes;
    }

    @Override
    public void encryptBulkData(final byte[] dataToBeEncrypted, final DataEncryptionListener dataEncryptionListener) {
        HandlerThread handlerThread = getWorkerThread();
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = getHandler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                SecureStorageError secureStorageError = new SecureStorageError();
                byte[] encryptedData = encryptData(dataToBeEncrypted, secureStorageError);
                if (secureStorageError.getErrorCode() == null) {
                    dataEncryptionListener.onEncryptionSuccess(encryptedData);
                } else {
                    dataEncryptionListener.onEncryptionError(secureStorageError);
                }

            }
        });
    }

    @NonNull
    protected Handler getHandler(Looper looper) {
        return new Handler(looper);
    }

    @NonNull
    protected HandlerThread getWorkerThread() {
        return new HandlerThread("WorkerThread");
    }

    @Override
    public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
        if (null == dataToBeDecrypted) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NullData);
            return null;
        }
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = ssEncoderDecoder.encodeDecodeData(Cipher.DECRYPT_MODE, ssKeyProvider.getSecureKey(RSA_WRAPPED_AES_KEY_MAIN), dataToBeDecrypted); // decrypt data using AES
        } catch (SSKeyProviderException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (SSEncodeDecodeException exception) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, exception.getMessage());
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "DecryptionError");
        }
        return decryptedBytes;
    }

    @Override
    public void decryptBulkData(final byte[] dataToBeDecrypted, final DataDecryptionListener dataDecryptionListener) {
        HandlerThread handlerThread = getWorkerThread();
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = getHandler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                SecureStorageError secureStorageError = new SecureStorageError();
                byte[] decryptedData = decryptData(dataToBeDecrypted, secureStorageError);
                if (secureStorageError.getErrorCode() == null) {
                    dataDecryptionListener.onDecryptionSuccess(decryptedData);
                } else {
                    dataDecryptionListener.onDecyptionError(secureStorageError);
                }

            }
        });
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
        if (SSUtils.checkProcess()) {
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
