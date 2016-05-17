/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;


/**
 * Created by 310238114 on 4/5/2016.
 * Current RSA implementation encrypts/decrypts given string in multiple of 256 character blocks.
 * RSA can encrypt only keyLength/8 byte at a time., eg 2048/8 -11  = 245  (11 bytes for padding)
 * "ISO-8859-1"  encoding id used for String because "ISO-8859-1" creates 1-1 mapping between byte and char. 1 byte will be converted to only 1 char only.
 *
 */
public class SecureStorage implements SecureStorageInterface{
    private static final String SINGLE_UNIVERSAL_KEY = "AppInfra.SecureStorage key pair";
    private static final String ENCRYPTION_ALGORITHM =  "RSA/ECB/PKCS1Padding";
    private final String FILE_NAME = "AppInfra.SecureStorage.file.name";
    //public static final String DEVICE_FILE = "AppInfra Device file";
    private   Context mContext;
    private static KeyStore keyStore = null;

    //this variable(encryptedTextTemp) must only  be used  for Demo App to see encrypted text and must be removed from release build
    public static  String encryptedTextTemp= null;


    public  SecureStorage(Context pContext){
        mContext = pContext;
    }



    @Override
    public synchronized boolean storeValueForKey(String userKey,String valueToBeEncrypted) {
        // TODO: RayKlo: define max size limit recommendation
        boolean returnResult= true;
        String encryptedString=null;
        try {
            if(null==userKey || userKey.isEmpty() || userKey.trim().isEmpty() || null==valueToBeEncrypted ) {
                returnResult=false;
                return false;
            }
            generateKeyPair();
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);

            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            int rsaKeyLength=  publicKey.getModulus().bitLength();
            int blockSize = (rsaKeyLength / 8)-11; // 11 bytes for padding 2048/8=11= 245 key size can be 512, 768, 1024, 2048, 3072, 4096
            Cipher input = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(valueToBeEncrypted.getBytes("ISO-8859-1"));
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] encodedMsg=null;
            while (dataInputStream.available() > 0) {
               byte[]  buffer = new byte[Math.min(blockSize, dataInputStream.available())];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = dataInputStream.readByte();
                }
                encodedMsg = input.doFinal(buffer);
                byteArrayOutputStream.write(encodedMsg, 0, encodedMsg.length);
            }
            dataInputStream.close();
            encryptedString = new String(byteArrayOutputStream.toByteArray(), "ISO-8859-1");
            returnResult = storeEncryptedData(userKey, encryptedString);
            encryptedString = returnResult?encryptedString:null; // if save of encryption data fails return null
            boolean isDebuggable =  ( 0 != ( mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
            if (isDebuggable) {
                encryptedTextTemp = encryptedString; // to be removed from release build
            }
        } catch (Exception e) {
            Log.e("SecureStorage", Log.getStackTraceString(e));
        }finally{
            System.out.println("ENCR" +encryptedString);
            return returnResult;
        }
    }

    @Override
    public synchronized String fetchValueForKey(String userKey) {
        String decryptedString=null;

        if(null==userKey ||  userKey.isEmpty() ) {
            return null;
        }
        String encryptedString =fetchEncryptedData(userKey);
        if(null==encryptedString){
            return null; // if user entered key is not present
        }
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if someone tries to fetch key even before it is created
                return null;
            }
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);


            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            int rsaKeyLength=  publicKey.getModulus().bitLength();
            int blockSize = (rsaKeyLength / 8); // 2048/8= 256 key length can be 512, 768, 1024, 2048, 3072, 4096

            Cipher output = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey()); // private key for decrypting

            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(encryptedString.getBytes("ISO-8859-1")));
            ByteArrayOutputStream sos = new ByteArrayOutputStream();
            DataOutputStream pOutRSA = new DataOutputStream(sos);
            byte [] buffer = new byte[blockSize];
            while (dataInputStream.available() > 0) {
                for (int i = 0; i < blockSize; i++) {
                    buffer[i] = dataInputStream.readByte();
                }
                byte[] decodedMsg = output.doFinal(buffer);
                pOutRSA.write(decodedMsg, 0, decodedMsg.length);
            }
            dataInputStream.close();
            decryptedString = new String(sos.toByteArray(), "ISO-8859-1");
            pOutRSA.close();

        } catch (Exception e) {
            Log.e("SecureStorage", Log.getStackTraceString(e));
        } finally{
            return decryptedString;
        }
    }


    @Override
    public synchronized boolean removeValueForKey(String userKey) {
        boolean deleteResult =false;
        if(null==userKey ||  userKey.isEmpty() ) {
            return false;
        }
        deleteResult=deleteEncryptedData(userKey);
        return deleteResult;
    }


    private  void generateKeyPair() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(SINGLE_UNIVERSAL_KEY)) {
                // if key is not generated
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 5);
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

               /* System.out.println("key private" +keyPair.getPrivate().getEncoded().toString());
                System.out.println("key public" +keyPair.getPublic().getEncoded().toString());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    protected boolean storeEncryptedData(String key, String encryptedData){
        boolean storeEncryptedDataResult= true;
        try {
                // encrypted data will be saved in device  SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString(key, encryptedData);
                storeEncryptedDataResult=editor.commit();
        }catch(Exception e){
            storeEncryptedDataResult=false;
            e.printStackTrace();
        }
            return storeEncryptedDataResult;
    }


    protected String  fetchEncryptedData(String key){
        String result =null;
            // encrypted data will be fetched from device  SharedPreferences
            SharedPreferences prefs = getSharedPreferences();
            result = prefs.getString(key, null);
        return result;
        }

    protected SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(FILE_NAME, mContext.MODE_PRIVATE);
    }

    protected boolean deleteEncryptedData(String key){
        boolean deleteResult= false;
        try {
                SharedPreferences prefs = getSharedPreferences();
               // String isGivenKeyPresentInSharedPreferences = prefs.getString(key, null);
               if( prefs.contains(key)){  // if given key is present in SharedPreferences
                   // encrypted data will be deleted from device  SharedPreferences
                   SharedPreferences.Editor editor = getSharedPreferences().edit();
                   editor.remove(key);
                   deleteResult=editor.commit();
               }

        }catch(Exception e){
            e.printStackTrace();
            deleteResult= false;
        }
        return deleteResult;
    }


}
