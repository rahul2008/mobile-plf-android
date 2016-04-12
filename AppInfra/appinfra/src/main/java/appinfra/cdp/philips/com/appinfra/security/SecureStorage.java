package appinfra.cdp.philips.com.appinfra.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


/**
 * Created by 310238114 on 4/5/2016.
 */
public class SecureStorage implements SecureStorageInterface{
    private static final String SINGLE_UNIVERSAL_KEY = "Single Universal key in keystore";
    private static final String  SECRET_KEY ="secret key name";
    private   Context mContext;
    private   String mEncryptedDataOutput ;
    private static KeyStore keyStore = null;



    public  SecureStorage(Context pContext, String pEncryptedDataOutput){
        mContext = pContext;
        mEncryptedDataOutput = pEncryptedDataOutput;
    }



    @Override
    public String storeValueForKey(String userKey,String valueToBeEncrypted) {
        String encryptedString=null;
        try {
            if(null==userKey || userKey.isEmpty() || null==valueToBeEncrypted || valueToBeEncrypted.isEmpty()) {
                return null;
            }
            generateKey();
          /*  KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(valueToBeEncrypted.getBytes("UTF-8"));
            cipherOutputStream.close();*/
            ///////////////////////////
            SecretKey secretKey = (SecretKey) keyStore.getKey(SECRET_KEY, null);
            Cipher cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                      cipher.init(Cipher.DECRYPT_MODE,secretKey);


            //////////////////////
           /* byte [] vals = outputStream.toByteArray();
            encryptedString=Base64.encodeToString(vals, Base64.DEFAULT);
            encryptedString = storeEncryptedData(userKey, encryptedString)?encryptedString : null; // if save of encryption data fails return null*/
        } catch (Exception e) {
            Log.e("SecureStorage", Log.getStackTraceString(e));
        }finally{
            System.out.println("ENCR" +encryptedString);
            return encryptedString;
        }
    }

    @Override
    public String fetchValueForKey(String userKey) {
        String decryptedString=null;
        decryptedString=fetchEncryptedData(userKey);
        if(null==userKey ||  userKey.isEmpty() ) {
            return null;
        }
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(SINGLE_UNIVERSAL_KEY, null);
            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(decryptedString, Base64.DEFAULT)), output);
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
        return deleteResult;
    }


    private  void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            try {
                keyGenerator.init(
                         new KeyGenParameterSpec.Builder(SECRET_KEY,
                                 KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                 .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                                 .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                                 .build());
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            SecretKey key = keyGenerator.generateKey();





        } catch (NoSuchAlgorithmException | NoSuchProviderException
            |  KeyStoreException
            | CertificateException | IOException e) {
                throw new RuntimeException("Failed to create a symmetric key", e);
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
            } else if (mEncryptedDataOutput.equals(CLOUD)) {
                //TBD encrypted data will be saved at Cloud
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
        }else if(mEncryptedDataOutput.equals(CLOUD)) {
            //encrypted data will be fetched from Cloud
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
            } else if (mEncryptedDataOutput.equals(CLOUD)) {
                //encrypted data will be deleted from Cloud
            }
        }catch(Exception e){
            e.printStackTrace();
            result= false;
        }
        return result;
    }


}
