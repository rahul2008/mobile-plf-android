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
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


/**
 * The class for store the key and value pair using AES Encryption with Cipher and Keystore .
 * Value will stored in SharedPrefrence in encrypted manner.
 * And also we can use as Create and store password securly using Create.
 * Current Implementation changed to MAIL-161.
 */
public class SecureStorage implements SecureStorageInterface {

    private static final String SINGLE_UNIVERSAL_KEY = "AppInfra.SecureStorage key pair";
    private static final String RSA_ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String AES_ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final String DATA_FILE_NAME = "AppInfra.Storage.file";
    private static final String KEY_FILE_NAME = "AppInfra.Storage.kfile";
    private static final String SINGLE_AES_KEY_TAG = "AppInfra.aes";
    private static final String KEY_FILE_NAME_FOR_PLAIN_KEY = "AppInfra.StoragePlainKey.kfile";
    private static KeyStore keyStore = null;
    private final Context mContext;
    private final AppInfra mAppInfra;
    private SharedPreferences mAppInfraPrefs;
    private SharedPreferences.Editor editor;
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    public SecureStorage(AppInfra bAppInfra) {
        mAppInfra = bAppInfra;
        mContext = mAppInfra.getAppInfraContext();
    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    private static boolean checkProcess() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "/system/bin/which", "su"});
            final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    @Override
    public synchronized boolean storeValueForKey(String userKey, final String valueToBeEncrypted, final SecureStorageError secureStorageError) {
        // TODO: RayKlo: define max size limit recommendation
        long startTime = System.currentTimeMillis();
            if (null == userKey || userKey.isEmpty() || userKey.trim().isEmpty() || null == valueToBeEncrypted) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                postLog(startTime, " duration for executing storeValueForKey ");
                return false;
            }
            userKey = getDecodedString(userKey);
            final String userKeyFinal = userKey;
                    boolean returnResult;
                    try {
                        final SecretKey secretKey = generateAESKey(); // generate AES key
                        final Key key = new SecretKeySpec(secretKey.getEncoded(), "AES");
                        final Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
                        final byte[] ivBlockSize = new byte[cipher.getBlockSize()];
                        final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
                        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
                        final byte[] encText = cipher.doFinal(valueToBeEncrypted.getBytes()); // encrypt string value using AES
                        String encryptedString = Base64.encodeToString(encText, Base64.DEFAULT);
                        returnResult = storeEncryptedData(userKeyFinal, encryptedString, DATA_FILE_NAME);// save encrypted value in data file
                        if (returnResult) {
                            returnResult = storeKey(userKeyFinal, secretKey, KEY_FILE_NAME);
                            if (!returnResult) { // if key is not saved then remove previously saved value
                                deleteEncryptedData(userKeyFinal, DATA_FILE_NAME);
                            }
                        }
                        if (!returnResult) {
                            // storing failed in shared preferences
                            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.StoreError);
                        }
                        encryptedString = returnResult ? encryptedString : null; // if save of encryption data fails return null
                        final boolean isDebuggable = (0 != (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
                        if (isDebuggable) {
                            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SECURE_STORAGE, "Encrypted Data"+encryptedString);
                        }
                    }
                    catch (Exception e) {
                        secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
                        returnResult = false;
                        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage"+ e.getMessage());
                        //Log.e("SecureStorage", Log.getStackTraceString(e));
                    }

        postLog(startTime, " duration for executing storeValueForKey ");
        return true;

    }

    private void postLog(long startTime, String message) {
        long endTime = System.currentTimeMillis();
        long methodDuration = (endTime - startTime);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE, getClass() + ""+message + methodDuration);
    }

    @Override
    public synchronized String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
        long startTime = System.currentTimeMillis();
        String decryptedString;
        if (null == userKey || userKey.isEmpty()) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            postLog(startTime, " duration for executing fetchValueForKey ");
            return null;
        }
        userKey = getDecodedString(userKey);
        final String encryptedString = fetchEncryptedData(userKey, secureStorageError, DATA_FILE_NAME);
        final String encryptedAESString = fetchEncryptedData(userKey, secureStorageError, KEY_FILE_NAME);
        if (null == encryptedString || null == encryptedAESString) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            postLog(startTime, " duration for executing fetchValueForKey ");
            return null; // if user entered key is not present
        }
        try {
            final Key key = fetchKey(encryptedAESString, secureStorageError);
            final Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            final byte[] ivBlockSize = new byte[cipher.getBlockSize()];
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            final byte[] encryptedValueBytes = Base64.decode(encryptedString, Base64.DEFAULT);
            final byte[] decText = cipher.doFinal(encryptedValueBytes); // decrypt string value using AES key
            decryptedString = new String(decText);

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE ,"Error in SecureStorage"+e.getMessage());
            //Log.e("SecureStorage", Log.getStackTraceString(e));
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            decryptedString = null; // if exception is thrown at:  decryptedString = new String(decText);
        }
        postLog(startTime, " duration for executing fetchValueForKey ");
        return decryptedString;

    }

    @Override
    public synchronized boolean removeValueForKey(String userKey) {
        boolean deleteResultValue;
        boolean deleteResultKey;
        if (null == userKey || userKey.isEmpty()) {
            return false;
        }
        deleteResultValue = deleteEncryptedData(userKey, DATA_FILE_NAME);
        deleteResultKey = deleteEncryptedData(userKey, KEY_FILE_NAME);
        return (deleteResultValue && deleteResultKey);
    }

    @Override
    public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
        boolean returnResult;
        try {
            if (null == keyName || keyName.isEmpty() || keyName.trim().isEmpty()) {
                error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                return false;
            }
            final SecretKey secretKey = generateAESKey(); // generate AES key
            returnResult = storeKey(keyName, secretKey, KEY_FILE_NAME_FOR_PLAIN_KEY);
        } catch (Exception e) {
            error.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            returnResult = false;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage  SqlCipher Data Key "+e.getMessage());
            //Log.e("SecureStorage", Log.getStackTraceString(e));
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
        final String encryptedAESString = fetchEncryptedData(keyName, error, KEY_FILE_NAME_FOR_PLAIN_KEY);
        if (null == encryptedAESString) {
            error.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null; // if user entered key is not present
        }
        try {
            decryptedKey = fetchKey(encryptedAESString, error);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error SecureStorage SqlCipher Data Key"+e.getMessage());
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
        deleteResultValue = deleteEncryptedData(keyName, KEY_FILE_NAME_FOR_PLAIN_KEY);
        if (!deleteResultValue) {
            //clear or deleting failed in shared preferences
            error.setErrorCode(SecureStorageError.secureStorageError.DeleteError);
        }
        return deleteResultValue;
    }

    private boolean storeKey(String userKeyOrKeyName, SecretKey secretKey, String keyFileName) throws Exception {
        boolean returnResult = false;
        final String aesEncryptedString = generateKeyPair(secretKey);
        if (null != aesEncryptedString) {
            returnResult = storeEncryptedData(userKeyOrKeyName, aesEncryptedString, keyFileName); // save encrypted AES key in file
        }
        return returnResult;
    }

    private Key fetchKey(String encryptedAESString, SecureStorageError secureStorageError) throws Exception {
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
            // if someone tries to fetch key even before it is created
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure); // key store not accessible
            return null;
        }

        final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
        final Cipher output = Cipher.getInstance(RSA_ENCRYPTION_ALGORITHM);
        output.init(Cipher.UNWRAP_MODE, privateKeyEntry.getPrivateKey());
        final SecretKey aesKey = (SecretKey) output.unwrap(Base64.decode(encryptedAESString, Base64.DEFAULT), "AES", Cipher.SECRET_KEY);// Unwrap AES key using RSA

        return new SecretKeySpec(aesKey.getEncoded(), "AES");
    }

    @SuppressWarnings("deprecation")
    private String generateKeyPair(SecretKey secretKey) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if key is not generated
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 50);

                AlgorithmParameterSpec algorithmParameterSpec;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    algorithmParameterSpec = new KeyGenParameterSpec.Builder(SINGLE_UNIVERSAL_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setCertificateSubject(new X500Principal("CN=Secure Storage, O=Philips AppInfra"))
                            .setCertificateSerialNumber(BigInteger.ONE)
                            .setKeyValidityStart(start.getTime())
                            .setKeyValidityEnd(end.getTime())
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build();

                } else {
                    algorithmParameterSpec = new KeyPairGeneratorSpec.Builder(mContext)
                            .setAlias(SINGLE_UNIVERSAL_KEY)
                            .setSubject(new X500Principal("CN=Secure Storage, O=Philips AppInfra"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
                }

                final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(algorithmParameterSpec);
                generator.generateKeyPair();
            }


            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
            final RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            final Cipher input = Cipher.getInstance(RSA_ENCRYPTION_ALGORITHM);
            input.init(Cipher.WRAP_MODE, publicKey);
            final byte[] AESbytes = input.wrap(secretKey);  // Wrap AES key using RSA

            return Base64.encodeToString(AESbytes, Base64.DEFAULT);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage"+ e.getMessage());
        }
        return null;

    }

    private boolean storeEncryptedData(String key, String encryptedData, String filename) {
        boolean storeEncryptedDataResult = true;
        try {
            // encrypted data will be saved in device  SharedPreferences
            mAppInfraPrefs = getSharedPreferences(filename);
            editor = mAppInfraPrefs.edit();
            editor.putString(key, encryptedData);
            storeEncryptedDataResult = editor.commit();
        } catch (Exception e) {
            storeEncryptedDataResult = false;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE,"Error in SecureStorage"+e.getMessage());
        }
        return storeEncryptedDataResult;
    }

    private String fetchEncryptedData(String key, SecureStorageError secureStorageError, String fileName) {
        String result = null;
        // encrypted data will be fetched from device  SharedPreferences
        mAppInfraPrefs = getSharedPreferences(fileName);
        if (mAppInfraPrefs.contains(key)) { // if key is present
            result = mAppInfraPrefs.getString(key, null);
            if (null == result) {
                // key is present but there is no data for that key
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NoDataFoundForKey);
            }
        } else {
            // key not found at shared preference
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
        }
        return result;
    }

    private SharedPreferences getSharedPreferences(String filename) {
        return mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    private boolean deleteEncryptedData(String key, String fileName) {
        boolean deleteResult = false;
        try {
            mAppInfraPrefs = getSharedPreferences(fileName);
            // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
            if (mAppInfraPrefs.contains(key)) {  // if given key is present in SharedPreferences
                // encrypted data will be deleted from device  SharedPreferences
                editor = mAppInfraPrefs.edit();
                editor.remove(key);
                deleteResult = editor.commit();
            }

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "Error in SecureStorage"+e.getMessage());
            deleteResult = false;
        }
        return deleteResult;
    }

    private SecretKey generateAESKey() throws NoSuchAlgorithmException {
        final int outputKeyLength = 256; // Generate a 256-bit key
        final SecureRandom secureRandom = new SecureRandom();    // Do *not* seed secureRandom! Automatically seeded from system entropy.
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        return keyGenerator.generateKey();
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
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "EncryptionError"+e.getMessage());
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
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,AppInfraLogEventID.AI_SECURE_STORAGE, "DecryptionError"+e.getMessage());
        }
        return decryptedBytes;
    }

    private Cipher getCipher(int CipherEncryptOrDecryptMode, SecureStorageError secureStorageError) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            SharedPreferences prefs = getSharedPreferences(KEY_FILE_NAME);
            if (prefs.contains(SINGLE_AES_KEY_TAG)) { // if  key is present
                final String encryptedAESString = fetchEncryptedData(SINGLE_AES_KEY_TAG, secureStorageError, KEY_FILE_NAME);
                final Key key = fetchKey(encryptedAESString, secureStorageError);
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SECURE_STORAGE,"key ####### ########" + key.toString());
                cipherInit(CipherEncryptOrDecryptMode, cipher, key);
            } else {
                final SecretKey secretKey = generateAESKey(); // generate AES key
                final Key key = new SecretKeySpec(secretKey.getEncoded(), "AES");
                storeKey(SINGLE_AES_KEY_TAG, secretKey, KEY_FILE_NAME);
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SECURE_STORAGE, "key ####### ########" + key.toString());
                cipherInit(CipherEncryptOrDecryptMode, cipher, key);
            }

        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE,"getCipher error"+e.getMessage());
        }
        return cipher;
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
        if (checkProcess()) {
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

    private String getDecodedString(String data) {
        try {
            return java.net.URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cipherInit(int CipherEncryptOrDecryptMode, Cipher cipher, final Key key) throws InvalidKeyException, InvalidAlgorithmParameterException {
        final byte[] ivBlockSize = new byte[cipher.getBlockSize()];
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
        cipher.init(CipherEncryptOrDecryptMode, key, ivParameterSpec);
        if (Cipher.DECRYPT_MODE == CipherEncryptOrDecryptMode) {
            decryptCipher = cipher;
        } else {
            encryptCipher = cipher;
        }
    }


}
