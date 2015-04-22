package com.philips.cl.di.dev.pa.security;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.philips.cl.di.dev.pa.util.ALog;

public class DiffieHellmanUtil {

    /**
     * Encrypting, decrypt data using AES algorithm
     * @param data
     * @param key
     * @return
     */
    public static byte[] aesEncryptData(String data, String keyStr) throws Exception {
    	Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
    	byte[] longKey = new BigInteger(keyStr,16).toByteArray();
    	byte[] key;
    	if(longKey[0]== 0){
    		key = Arrays.copyOfRange(longKey, 1, 17);	
    	}else{
    		key=Arrays.copyOf(longKey, 16);
    	}
    
    	SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    	byte[] ivBytes=new byte[]{0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
    	IvParameterSpec iv = new IvParameterSpec(ivBytes);
    	c.init(Cipher.ENCRYPT_MODE, keySpec,iv);
    	byte[] dataBytes = Util.addRandomBytes(data.getBytes(Charset.defaultCharset()));// for add random bytes
    	ALog.i(ALog.SECURITY, "dataBytes length: " + dataBytes.length);// for add random bytes
    	return c.doFinal(dataBytes);
    }

    /**
     * Decrypt data using AES algorithm
     * @param data
     * @param key
     * @return
     */
    public static byte[] aesDecryptData(byte[] data, String keyStr) throws Exception {
    	
    	Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
    	byte[] longKey = new BigInteger(keyStr,16).toByteArray();
    	byte[] key;
    	if(longKey[0]== 0){
    		key = Arrays.copyOfRange(longKey, 1, 17);	
    	}else{
    		key=Arrays.copyOf(longKey, 16);
    	}
    
    	SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    	byte[] ivBytes=new byte[]{0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
    	IvParameterSpec iv = new IvParameterSpec(ivBytes);
    	c.init(Cipher.DECRYPT_MODE, keySpec,iv);
    
    	return c.doFinal(data);
    }

    /**
     * Generate diffie key
     * @return
     */
    public static String generateDiffieKey(String randomValue) {
    	BigInteger p = new BigInteger(Util.PVALUE,16); 
    	BigInteger g = new BigInteger(Util.GVALUE,16);
    	BigInteger r = new BigInteger(randomValue);
    	return Util.bytesToHex(g.modPow(r, p).toByteArray());
    }

    /**
     * Generate Secret key using hellman key
     * @param hellmanKey
     * @return
     */
    public static String generateSecretKey(String hellmanKey, String randomValue) {
    	BigInteger p = new BigInteger(Util.PVALUE,16); 
    	BigInteger g = new BigInteger(hellmanKey,16);
    	BigInteger r= new BigInteger(randomValue);
    	return Util.bytesToHex(g.modPow(r, p).toByteArray());
    }

    public static String extractEncryptionKey(String shellman, String skeyEnc, String randomValue) throws Exception {
        String secKey = generateSecretKey(shellman, randomValue);
        secKey = Util.getEvenNumberSecretKey(secKey);
        byte[] bytesEncKey = Util.hexToBytes(skeyEnc);
        byte[] bytesDecKey = aesDecryptData(bytesEncKey, secKey);
    
        String key = Util.bytesToHex(bytesDecKey);
        return key;
    }

}
