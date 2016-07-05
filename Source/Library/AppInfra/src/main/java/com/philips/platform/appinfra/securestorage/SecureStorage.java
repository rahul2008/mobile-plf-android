/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


/**
 * Current Implementation changed to MAIL-161.
 */
public class SecureStorage implements SecureStorageInterface{
    private static final String SINGLE_UNIVERSAL_KEY = "AppInfra.SecureStorage key pair";
    private static final String RSA_ENCRYPTION_ALGORITHM =  "RSA/ECB/PKCS1Padding";
    private static final String AES_ENCRYPTION_ALGORITHM =  "AES/CTR/NoPadding";
    private static final String DATA_FILE_NAME = "AppInfra.Storage.file";
    private static final String KEY_FILE_NAME = "AppInfra.Storage.kfile";
    private final  Context mContext;
    private static KeyStore keyStore = null;
    AppInfra mAppInfra;




    public  SecureStorage(AppInfra bAppInfra){
        mAppInfra=bAppInfra;
        mContext = mAppInfra.getAppInfraContext();
    }



    @Override
    public synchronized boolean storeValueForKey(String userKey,String valueToBeEncrypted, SecureStorageError secureStorageError) {
        // TODO: RayKlo: define max size limit recommendation
        boolean returnResult= true;
        String encryptedString=null;
        try {
            if(null==userKey || userKey.isEmpty() || userKey.trim().isEmpty() || null==valueToBeEncrypted ) {
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
                returnResult=false;
                return false;
            }
            SecretKey secretKey = generateAESKey(); // generate AES key
            Key key = (Key) new SecretKeySpec(secretKey.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            byte[] ivBlockSize = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
            cipher.init(Cipher.ENCRYPT_MODE, key,ivParameterSpec);
            byte[] encText = cipher.doFinal(valueToBeEncrypted.getBytes()); // encrypt string value using AES
            encryptedString =  Base64.encodeToString(encText, Base64.DEFAULT);
            returnResult= storeEncryptedData(userKey,encryptedString,DATA_FILE_NAME);// save encrypted value in data file
            if(returnResult){ // if value is saved
                generateKeyPair();
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
                RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
                Cipher input = Cipher.getInstance(RSA_ENCRYPTION_ALGORITHM);
                input.init(Cipher.WRAP_MODE, publicKey);
                byte[] AESbytes = input.wrap(secretKey);  // Wrap AES key using RSA
                String AESencryptedString = Base64.encodeToString(AESbytes, Base64.DEFAULT);
                returnResult = storeEncryptedData(userKey, AESencryptedString ,KEY_FILE_NAME); // save encrypted AES key in file
                if(!returnResult){ // if key is not saved then remove previously saved value
                    deleteEncryptedData(userKey,DATA_FILE_NAME);
                }
            }
            if(!returnResult){
                // storing failed in shared preferences
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.StoreError);
            }
            encryptedString = returnResult?encryptedString:null; // if save of encryption data fails return null
            boolean isDebuggable =  ( 0 != ( mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
            if (isDebuggable) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,"Encrypted Data",encryptedString);
            }
        } catch (Exception e) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.EncryptionError);
            returnResult=false;
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SecureStorage",e.getMessage());
            //Log.e("SecureStorage", Log.getStackTraceString(e));
        }finally{
            return returnResult;
        }
    }



    @Override
    public synchronized String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
        String decryptedString=null;
        String decryptedAESkey=null;
        if(null==userKey ||  userKey.isEmpty() ) {
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null;
        }
        String encryptedString =fetchEncryptedData(userKey,secureStorageError,DATA_FILE_NAME);
        String encryptedAESString =fetchEncryptedData(userKey,secureStorageError,KEY_FILE_NAME);
        if(null==encryptedString || null==encryptedAESString){
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            return null; // if user entered key is not present
        }
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if someone tries to fetch key even before it is created
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.AccessKeyFailure); // key store not accessible
                return null;
            }

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
            Cipher output = Cipher.getInstance(RSA_ENCRYPTION_ALGORITHM);
            output.init(Cipher.UNWRAP_MODE, privateKeyEntry.getPrivateKey());
            SecretKey AESkey = (SecretKey) output.unwrap(Base64.decode(encryptedAESString, Base64.DEFAULT), "AES",Cipher.SECRET_KEY);// Unwrap AES key using RSA

            Key key2 = (Key) new SecretKeySpec(AESkey.getEncoded(), "AES");
            Cipher cipher2 = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            byte[] ivBlockSize = new byte[cipher2.getBlockSize()];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBlockSize);
            cipher2.init(Cipher.DECRYPT_MODE, key2,ivParameterSpec);
            byte[] encryptedValueBytes =Base64.decode(encryptedString, Base64.DEFAULT);
            byte[] decText = cipher2.doFinal(encryptedValueBytes); // decrypt string value using AES key
             decryptedString = new String(decText);
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR,"SecureStorage",e.getMessage());
            //Log.e("SecureStorage", Log.getStackTraceString(e));
            secureStorageError.setErrorCode(SecureStorageError.secureStorageError.DecryptionError);
            if(null!=decryptedString){  // if exception is thrown at:  decryptedString = new String(decText);
                decryptedString=null;
            }
        } finally{
            return decryptedString;
        }
    }



    @Override
    public synchronized boolean removeValueForKey(String userKey) {
        boolean deleteResultValue =false;
        boolean deleteResultKey =false;
        if(null==userKey ||  userKey.isEmpty() ) {
            return false;
        }
        deleteResultValue=deleteEncryptedData(userKey,DATA_FILE_NAME);
        deleteResultKey=deleteEncryptedData(userKey,KEY_FILE_NAME);
        return (deleteResultValue &&  deleteResultKey);
    }


    private  void generateKeyPair() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if key is not generated
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 50);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(mContext)
                        .setAlias(SINGLE_UNIVERSAL_KEY)
                        .setSubject(new X500Principal("CN=Secure Storage, O=Philips AppInfra"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                KeyPair keyPair = generator.generateKeyPair();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private boolean storeEncryptedData(String key, String encryptedData,String filename){
        boolean storeEncryptedDataResult= true;
        try {
                // encrypted data will be saved in device  SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences(filename).edit();
                editor.putString(key, encryptedData);
                storeEncryptedDataResult=editor.commit();
        }catch(Exception e){
            storeEncryptedDataResult=false;
            e.printStackTrace();
        }
            return storeEncryptedDataResult;
    }



    private String  fetchEncryptedData(String key,SecureStorageError secureStorageError,String fileName){
        String result =null;
            // encrypted data will be fetched from device  SharedPreferences
            SharedPreferences prefs = getSharedPreferences(fileName);
            if(prefs.contains(key)){ // if key is present
                result = prefs.getString(key, null);
                if(null==result){
                    // key is present but there is no data for that key
                    secureStorageError.setErrorCode(SecureStorageError.secureStorageError.NoDataFoundForKey);
                }
            }else{
                // key not found at shared preference
                secureStorageError.setErrorCode(SecureStorageError.secureStorageError.UnknownKey);
            }
        return result;
        }


    private SharedPreferences getSharedPreferences(String filename) {
        return mContext.getSharedPreferences(filename, mContext.MODE_PRIVATE);
    }


    private boolean deleteEncryptedData(String key, String fileName){
        boolean deleteResult= false;
        try {
                SharedPreferences prefs = getSharedPreferences(fileName);
               // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
               if( prefs.contains(key)){  // if given key is present in SharedPreferences
                   // encrypted data will be deleted from device  SharedPreferences
                   SharedPreferences.Editor editor = getSharedPreferences(fileName).edit();
                   editor.remove(key);
                   deleteResult=editor.commit();
               }

        }catch(Exception e){
            e.printStackTrace();
            deleteResult= false;
        }
        return deleteResult;
    }

    private SecretKey generateAESKey() throws NoSuchAlgorithmException {
        final int outputKeyLength = 256; // Generate a 256-bit key
        SecureRandom secureRandom = new SecureRandom();    // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }
}
