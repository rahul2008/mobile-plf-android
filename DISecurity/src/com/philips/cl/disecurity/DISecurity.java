package com.philips.cl.disecurity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class DISecurity implements ServerResponseListener {
	public static final String TAG = DISecurity.class.getSimpleName();
	public static Hashtable<String, String> 
			securityHashtable = new Hashtable<String,String>();
	private static Hashtable<String, Boolean> 
			isExchangingKeyTable = new Hashtable<String, Boolean>();
	
	private static Hashtable<String, String> 
			urlsTable = new Hashtable<String, String>();
	private String pValue;
	private String gValue;
	private String rValue;
	private KeyDecryptListener keyDecryptListener;

	/**
	 * Constructor
	 * @param context
	 */
	public DISecurity(KeyDecryptListener keyDecryptListener) {
		this.keyDecryptListener = keyDecryptListener;
		pValue = Util.pValue;
		gValue = Util.gValue;
		Log.i(TAG, "initialize constructor: ") ;
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */ 
	public void exchangeKey(String url, String deviceId)  {
		Log.i(TAG, "exchangeKey ") ;
		urlsTable.put(deviceId, url);
		if (!isKeyExchanging(deviceId)) {
			Log.i(TAG, "exchangeKey diffie") ;
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
			Log.d(TAG, "Diffie= "+sdiffie);
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
		Log.i(TAG, "key in getEncryptData() = " + key);
		String encryptedBase64Str = null;
		if (key != null) {
			try {
				byte[] encrypDatas = aesEncryptData(data, key);
				encryptedBase64Str = Util.encodeToBase64(encrypDatas);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		String decryptData = null;

		if (key != null) {
			try {
				byte[] bytesEncData = Util.decodeFromBase64(data);
				byte[] bytesDecData = aesDecryptData(bytesEncData, key);
				decryptData = new String(bytesDecData);
			} catch (Exception e) {
				e.printStackTrace();
				exchangeKey(urlsTable.get(deviceId), deviceId);

			}
		}
		Log.i(TAG, "decryptData= " + decryptData) ;
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

		return c.doFinal(data.getBytes());
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
		Log.i(TAG, "rValue= " +rValue);
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
		Log.i(TAG, "Response Code: "+responseCode) ;
		if ( responseCode == 200 ) {
			JSONObject json;
			try {
				json = new JSONObject(responseData);
				String shellman = json.getString("hellman");
				Log.d(TAG, "result hellmam= "+shellman + " :Length:= " +shellman.length());

				String skeyEnc = json.getString("key");
				Log.d(TAG, "keyEnc by device= "+skeyEnc+" :length:= " + skeyEnc.length());
				
				String secKey = generateSecretKey(shellman);
				Log.d(TAG, "secKey= "+secKey + " : length= "+secKey.length());
				
				secKey = Util.getEvenNumberSecretKey(secKey);
				
				byte[] bytesEncKey = Util.hexToBytes(skeyEnc);

				byte [] bytesDecKey = aesDecryptData(bytesEncKey, secKey);
				
				String key = Util.bytesToHex(bytesDecKey);
				Log.i(TAG, "decryted key == " + key);
				securityHashtable.put(deviceId, key);
				keyDecryptListener.keyDecrypt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		isExchangingKeyTable.put(deviceId, false);
	}
	
	

	private boolean isKeyExchanging(String deviceId) {
		// First time exchange
		if (isExchangingKeyTable.get(deviceId) == null) return false;
		
		// No exchange ongoing
		if (!isExchangingKeyTable.get(deviceId)) return false;
		
		return true;
	}
}
