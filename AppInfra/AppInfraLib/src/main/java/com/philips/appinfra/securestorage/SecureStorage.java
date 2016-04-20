package com.philips.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;


/**
 * Created by 310238114 on 4/5/2016.
 */
public class SecureStorage implements SecureStorageInterface{
    private static final String SINGLE_UNIVERSAL_KEY = "Single Universal key in keystore";
    private final String FILE_NAME = "appinfra_file_name";
    public static final String DEVICE_FILE = "AppInfra Device file";
    private   Context mContext;
    private   String mEncryptedDataOutput ;
    private static KeyStore keyStore = null;

    //this variable(encryptedTextTemp) must only  be used  for Demo App to see encrypted text and must be removed from release build
    public static  String encryptedTextTemp= null;



    public  SecureStorage(Context pContext, String pEncryptedDataOutput){
        mContext = pContext;
        mEncryptedDataOutput = pEncryptedDataOutput;
    }



    @Override
    public void storeValueForKey(String userKey,String valueToBeEncrypted) {
        String encryptedString=null;
        try {
            if(null==userKey || userKey.isEmpty() || null==valueToBeEncrypted ) {
                return ;
            }
            generateKeyPair();
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(valueToBeEncrypted.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
            encryptedString=Base64.encodeToString(vals, Base64.DEFAULT);
            encryptedString = storeEncryptedData(userKey, encryptedString)?encryptedString:null; // if save of encryption data fails return null
            encryptedTextTemp=encryptedString; // to be removed from release build
        } catch (Exception e) {
            Log.e("SecureStorage", Log.getStackTraceString(e));
        }finally{
            System.out.println("ENCR" +encryptedString);
        }
    }

    @Override
    public String fetchValueForKey(String userKey) {
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
            //RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedString, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }
            decryptedString = new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            Log.e("SecureStorage", Log.getStackTraceString(e));
        } finally{
            return decryptedString;
        }
    }


    @Override
    public boolean RemoveValueForKey(String userKey) {
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
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

    }



    protected boolean storeEncryptedData(String key, String encryptedData){
        boolean storeEncryptedDataResult= true;
        try {
            if (mEncryptedDataOutput.equals(DEVICE_FILE)) {
                // encrypted data will be saved in device  SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString(key, encryptedData);
                editor.commit();
            }
        }catch(Exception e){
            storeEncryptedDataResult=false;
            e.printStackTrace();
        }
            return storeEncryptedDataResult;
    }


    protected String  fetchEncryptedData(String key){
        String result =null;
        if(mEncryptedDataOutput.equals(DEVICE_FILE)) {
            // encrypted data will be fetched from device  SharedPreferences
            SharedPreferences prefs = getSharedPreferences();
            result = prefs.getString(key, null);
        }
        return result;
        }

    protected SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(FILE_NAME, mContext.MODE_PRIVATE);
    }

    protected boolean deleteEncryptedData(String key){
        boolean result= true;
        try {
            if (mEncryptedDataOutput.equals(DEVICE_FILE)) {
                // encrypted data will be deleted from device  SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.remove(key);
                editor.commit();
            }
        }catch(Exception e){
            e.printStackTrace();
            result= false;
        }
        return result;
    }


}
