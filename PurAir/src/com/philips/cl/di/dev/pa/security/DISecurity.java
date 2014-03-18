package com.philips.cl.di.dev.pa.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.utils.ALog;

public class DISecurity implements ServerResponseListener {

	public static Hashtable<String, String> 
			securityHashtable = new Hashtable<String,String>();
	private static Hashtable<String, Boolean> 
			isExchangingKeyTable = new Hashtable<String, Boolean>();
	
	public static Hashtable<String, String> 
			urlsTable = new Hashtable<String, String>();
	private String pValue;
	private String gValue;
	private String rValue;
	private KeyDecryptListener keyDecryptListener;
	
	private int counterExchangeKey;

	/**
	 * Constructor
	 * @param context
	 */
	public DISecurity(KeyDecryptListener keyDecryptListener) {
		this.keyDecryptListener = keyDecryptListener;
		pValue = Util.pValue;
		gValue = Util.gValue;
		counterExchangeKey = 0;
		ALog.i(ALog.SECURITY, "Initialized DISecurity") ;
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */ 
	public void exchangeKey(String url, String deviceId)  {
		if (counterExchangeKey < 3) {
			counterExchangeKey++;
			ALog.i(ALog.SECURITY, "requested Key exchange for device: " + deviceId+":"+isKeyExchanging(deviceId)) ;
			urlsTable.put(deviceId, url);
			if (!isKeyExchanging(deviceId)) {
				ALog.i(ALog.SECURITY, "Exchanging key for device: " + deviceId) ;
				isExchangingKeyTable.put(deviceId, true);
				
				//Get diffie key
				String sdiffie = generateDiffieKey();
		
				JSONObject holder = new JSONObject();
				
				try {
					holder.put("diffie", sdiffie);
					String js = holder.toString();
					
					//Send diffie to http
					sendDiffie(url, deviceId, js);
					
				} catch (JSONException e) {
					e.printStackTrace();
					isExchangingKeyTable.put(deviceId, false);
				}
				ALog.d(ALog.SECURITY, "Generated diffie key: "+sdiffie);
			}
		} else {
			ALog.e(ALog.SECURITY, "Third time key exchange failed");
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param deviceId
	 * @return
	 */
	public String encryptData(String data, String deviceId) {
		String key = securityHashtable.get(deviceId);
		
		if (key == null) {
			ALog.i(ALog.SECURITY, "Did not encrypt data - Key is null");
			return null; // TODO return unencrypted data?
		}
		
		String encryptedBase64Str = null;
		try {
			byte[] encrypDatas = aesEncryptData(data, key);
			encryptedBase64Str = Util.encodeToBase64(encrypDatas);
			ALog.i(ALog.SECURITY, "Encrypted data: " + encryptedBase64Str);
		} catch (Exception e) {
			e.printStackTrace();
			ALog.i(ALog.SECURITY, "Failed to encrypt data with key - " + e.getMessage());
		}
		return encryptedBase64Str;
	}
	
	/**
	 * 
	 * @param data
	 * @param deviceId
	 * @return
	 */
	public String decryptData(String data, String deviceId) {
		
		String key = securityHashtable.get(deviceId);
		ALog.i(ALog.SECURITY, "Decryption - Key   " + key);
		String decryptData = null;
		
		if (key == null || data == null) {
			ALog.i(ALog.SECURITY, "Did not decrypt data - Key is null");
			return null; // TODO return undecrypted data?
		}

		try {
			byte[] bytesEncData = Util.decodeFromBase64(data.trim());
			byte[] bytesDecData = aesDecryptData(bytesEncData, key);
			decryptData = new String(bytesDecData,Charset.defaultCharset());
			ALog.i(ALog.SECURITY, "Decrypted data: " + decryptData);
			counterExchangeKey = 0;
		} catch (Exception e) {
			e.printStackTrace();
			ALog.i(ALog.SECURITY, "Failed to decrypt data - requesting new key exchange");
			
			exchangeKey(urlsTable.get(deviceId), deviceId);
		}

		return decryptData;
	}
	
	
	/**
	 * Encrypting, decrypt data using AES algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	private byte[] aesEncryptData(String data, String keyStr) throws Exception {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
		byte[] longKey = (new BigInteger(keyStr,16)).toByteArray();
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

		return c.doFinal(data.getBytes(Charset.defaultCharset()));
	}


	/**
	 * Decrypt data using AES algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	private byte[] aesDecryptData(byte[] data, String keyStr) throws Exception {
		
		Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
		byte[] longKey = (new BigInteger(keyStr,16)).toByteArray();
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
	private String generateDiffieKey() {
		rValue = Util.generateRandomNum();
		BigInteger p = new BigInteger(pValue,16); 
		BigInteger g = new BigInteger(gValue,16);
		BigInteger r = new BigInteger(rValue);
		return Util.bytesToHex(g.modPow(r, p).toByteArray());
	}

	
	/**
	 * 
	 * @param strEntity
	 */
	private void sendDiffie(String url, String deviceId, String strEntity)  {
		
		DISecurityTask diSecurityTask = new DISecurityTask(this);
		diSecurityTask.execute(new String[]{url, deviceId, strEntity});
		
	}

	/**
	 * Generate Secret key using hellman key
	 * @param hellmanKey
	 * @return
	 */
	private String generateSecretKey(String hellmanKey) {
		BigInteger p = new BigInteger(pValue,16); 
		BigInteger g = new BigInteger(hellmanKey,16);
		BigInteger r= new BigInteger(rValue);
		return Util.bytesToHex(g.modPow(r, p).toByteArray());
	}
	
	/**
	 * 
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String deviceId, String url) {
		ALog.i(ALog.SECURITY, "Received response from device: " + deviceId + "    ResponseCode: " + responseCode) ;
		isExchangingKeyTable.put(deviceId, false);
		if ( responseCode == 200 ) {
			counterExchangeKey = 0;
			JSONObject json;
			try {
				json = new JSONObject(responseData);
				String shellman = json.getString("hellman");
				ALog.d(ALog.SECURITY, "result hellmam= "+shellman + "     Length:= " +shellman.length());

				String skeyEnc = json.getString("key");
				ALog.d(ALog.SECURITY, "encrypted key= "+skeyEnc+"    length:= " + skeyEnc.length());
				
				String secKey = generateSecretKey(shellman);
				ALog.d(ALog.SECURITY, "secret key= "+secKey + "    length= "+secKey.length());
				
				secKey = Util.getEvenNumberSecretKey(secKey);
				
				byte[] bytesEncKey = Util.hexToBytes(skeyEnc);

				byte [] bytesDecKey = aesDecryptData(bytesEncKey, secKey);
				
				String key = Util.bytesToHex(bytesDecKey);
				ALog.i(ALog.SECURITY, "decryted key= " + key);
				securityHashtable.put(deviceId, key);
				
				AppConstants.DEVICEID = deviceId;
				
				keyDecryptListener.keyDecrypt(key, deviceId);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			exchangeKey(urlsTable.get(deviceId), deviceId);
		}
		
	}
	
	

	private boolean isKeyExchanging(String deviceId) {
		// First time exchange
		ALog.i(ALog.SECURITY, "isKeyExchanging: "+isExchangingKeyTable.get(deviceId)) ;
		if (!isExchangingKeyTable.containsKey(deviceId)) return false;
		// No exchange ongoing
		if (!isExchangingKeyTable.get(deviceId)) return false;
		
		return true;
	}
}
