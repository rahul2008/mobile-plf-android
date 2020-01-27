/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestoragev1;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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

@SuppressWarnings("deprecation")
class SecureStorageHelper {

    private static final String AES_ENCRYPTION_ALGORITHM = "AES/CTR/NoPadding";
    private static final String SINGLE_UNIVERSAL_KEY = "AppInfra.SecureStorage key pair";
    private static final String RSA_ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private Context mContext;
    private AppInfraInterface mAppInfra;
    private final String deLimiter = "delimiter";

    SecureStorageHelper(AppInfraInterface mAppInfra) {
        this.mAppInfra = mAppInfra;
        this.mContext = mAppInfra.getAppInfraContext();
    }

    SecretKey generateAESKey() throws NoSuchAlgorithmException {
        final int outputKeyLength = 256; // Generate a 256-bit key
        final SecureRandom secureRandom = new SecureRandom();    // Do *not* seed secureRandom! Automatically seeded from system entropy.
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        return keyGenerator.generateKey();
    }

    boolean storeKey(String userKeyOrKeyName, SecretKey secretKey, String keyFileName) throws Exception {
        boolean returnResult = false;
        final String aesEncryptedString = generateKeyPair(secretKey);
        if (null != aesEncryptedString) {
            returnResult = storeEncryptedData(userKeyOrKeyName, aesEncryptedString, keyFileName); // save encrypted AES key in file
        }
        return returnResult;
    }

    Key fetchKey(String encryptedAESString, SecureStorageInterface.SecureStorageError secureStorageError) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
            // if someone tries to fetch key even before it is created
            secureStorageError.setErrorCode(SecureStorageInterface.SecureStorageError.secureStorageError.AccessKeyFailure); // key store not accessible
            return null;
        }

        final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
        final Cipher output = Cipher.getInstance(RSA_ENCRYPTION_ALGORITHM);
        output.init(Cipher.UNWRAP_MODE, privateKeyEntry.getPrivateKey());
        final SecretKey aesKey = (SecretKey) output.unwrap(Base64.decode(encryptedAESString, Base64.DEFAULT), "AES", Cipher.SECRET_KEY);// Unwrap AES key using RSA

        return new SecretKeySpec(aesKey.getEncoded(), "AES");
    }


    boolean storeEncryptedData(String key, String encryptedData, String filename) {
        boolean storeEncryptedDataResult;
        try {
            // encrypted data will be saved in device  SharedPreferences
            SharedPreferences mAppInfraPrefs = getSharedPreferences(filename);
            SharedPreferences.Editor editor = mAppInfraPrefs.edit();
            editor.putString(key, encryptedData);
            storeEncryptedDataResult = editor.commit();
        } catch (Exception e) {
            storeEncryptedDataResult = false;
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in S-Storage while storing e-data");
        }
        return storeEncryptedDataResult;
    }

    String fetchEncryptedData(String key, SecureStorageInterface.SecureStorageError secureStorageError, String fileName) {
        String result = null;
        // encrypted data will be fetched from device  SharedPreferences
        SharedPreferences mAppInfraPrefs = getSharedPreferences(fileName);
        if (mAppInfraPrefs.contains(key)) { // if key is present
            result = mAppInfraPrefs.getString(key, null);
            if (null == result) {
                // key is present but there is no data for that key
                secureStorageError.setErrorCode(SecureStorageInterface.SecureStorageError.secureStorageError.NoDataFoundForKey);
            }
        } else {
            // key not found at shared preference
            secureStorageError.setErrorCode(SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
        }
        return result;
    }

    SharedPreferences getSharedPreferences(String filename) {
        return mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }


    String getDecodedString(String data) {
        try {
            return java.net.URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,
                    AppInfraLogEventID.AI_SECURE_STORAGE, "Unsupported encoding exception " + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private String generateKeyPair(SecretKey secretKey) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if key is not generated
                Calendar start = Calendar.getInstance();
                start.add(Calendar.YEAR, -1);
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 50);

                AlgorithmParameterSpec algorithmParameterSpec;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    algorithmParameterSpec = new KeyGenParameterSpec.Builder(SINGLE_UNIVERSAL_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setCertificateSubject(new X500Principal("CN=Secure Storage, O=Philips AppInfra"))
                            .setCertificateSerialNumber(BigInteger.ONE)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .setKeyValidityStart(start.getTime())
                            .setKeyValidityEnd(end.getTime())
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
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in S-Storage when gen k-pair" + e.getMessage());
        }
        return null;
    }

    boolean deleteEncryptedData(String key, String fileName) {
        boolean deleteResult = false;
        try {
            SharedPreferences mAppInfraPrefs = getSharedPreferences(fileName);
            // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
            if (mAppInfraPrefs.contains(key)) {  // if given key is present in SharedPreferences
                // encrypted data will be deleted from device  SharedPreferences
                SharedPreferences.Editor editor = mAppInfraPrefs.edit();
                editor.remove(key);
                deleteResult = editor.commit();
            }

        } catch (Exception e) {
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Error in S-Storage when deleting e-data" + e.getMessage());
            deleteResult = false;
        }
        return deleteResult;
    }

    boolean isKeyAvailable(String key, String fileName) {
        SharedPreferences mAppInfraPrefs = getSharedPreferences(fileName);
        // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
        return mAppInfraPrefs.contains(key);

    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    boolean checkProcess() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "/system/bin/which", "su"});
            final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_SECURE_STORAGE, "Throwable exception");
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }


    String encodeDecodeData(int mode, Key secretKey, String value) throws Exception {
        final Key key = new SecretKeySpec(secretKey.getEncoded(), "AES");
        final Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);

        if (mode == Cipher.ENCRYPT_MODE) {
            byte[] ivBytes = getUniqueIV();
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            final byte[] encText = cipher.doFinal(value.getBytes()); // encrypt string value using AES
            String encodedEncryptedString = Base64.encodeToString(encText, Base64.DEFAULT);
            String encodedIV = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);
            encodedEncryptedString = encodedEncryptedString.concat(deLimiter);// appending delimiter on encrypted data
            encodedEncryptedString = encodedEncryptedString.concat(encodedIV);// appending Base 64 encoded IV on encrypted data
            return encodedEncryptedString;
        } else if (mode == Cipher.DECRYPT_MODE) {
            byte[] iv;
            String[] splitData = getSplitData(value);
            value = splitData[0];
            if (splitData.length == 2) {
                iv = Base64.decode(splitData[1].getBytes(), Base64.DEFAULT);
            } else {
                iv = new byte[cipher.getBlockSize()];
            }
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            final byte[] decodedEncryptedBytes = Base64.decode(value, Base64.DEFAULT);
            final byte[] decryptedString = cipher.doFinal(decodedEncryptedBytes); // decrypt string value using AES key
            return new String(decryptedString);
        }
        return null;
    }

    byte[] encodeDecodeData(int mode, Key secretKey, byte[] value) throws Exception {
        final Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
        if (mode == Cipher.ENCRYPT_MODE) {
            byte[] ivBytes = getUniqueIV();
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] iv = Base64.encode(cipher.getIV(), Base64.DEFAULT);
            final byte[] encText = Base64.encode(cipher.doFinal(value), Base64.DEFAULT); // encrypt string value using AES
            return getAppendedByte(encText, iv);
        } else if (mode == Cipher.DECRYPT_MODE) {
            byte[] iv;
            String[] splitData = getSplitData(new String(value));
            if (splitData.length == 2) {
                value = splitData[0].getBytes();
                iv = Base64.decode(splitData[1].getBytes(), Base64.DEFAULT);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
                byte[] decodedData = Base64.decode(value, Base64.DEFAULT);
                return cipher.doFinal(decodedData);// decrypt string value using AES key
            } else {
                iv = new byte[cipher.getBlockSize()];
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
                return cipher.doFinal(value);
            }

        }
        return null;
    }

    @NonNull
    byte[] getAppendedByte(byte[] encText, byte[] iv) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(encText.length + deLimiter.length() + iv.length);
        byteBuffer.put(encText);
        byteBuffer.put(deLimiter.getBytes());
        byteBuffer.put(iv);
        return byteBuffer.array();
    }

    private byte[] getUniqueIV() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    String[] getSplitData(String value) {
        return value.split(deLimiter);
    }
}
