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

import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.ALog;

public class DISecurity implements ServerResponseListener {

	private static Hashtable<String, String> 
			securityKeyHashtable = new Hashtable<String,String>();
	private static Hashtable<String, Boolean> 
			isExchangingKeyTable = new Hashtable<String, Boolean>();
	private static Hashtable<String, String> 
			urlsTable = new Hashtable<String, String>();
	private static Hashtable<String, Integer> 
			exchangeKeyCounterTable = new Hashtable<String, Integer>();
	
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
		pValue = Util.PVALUE;
		gValue = Util.GVALUE;
		ALog.i(ALog.SECURITY, "Initialized DISecurity") ;
	}
	
	public void initializeExchangeKeyCounter(String deviceEui64) {
		exchangeKeyCounterTable.put(deviceEui64, 0);
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */ 
	public void exchangeKey(String url, String deviceEui64)  {
		int counter = exchangeKeyCounter(deviceEui64);
		ALog.i(ALog.SECURITY, "deviceEui64: " + deviceEui64 + ", exchange key counter: " + counter) ;
		if (counter < 3) {
			counter++;
			exchangeKeyCounterTable.put(deviceEui64, counter);
			ALog.i(ALog.SECURITY, "Requested Key exchange for device: " + deviceEui64+":"+isKeyExchanging(deviceEui64)) ;
			urlsTable.put(deviceEui64, url);
			if (!isKeyExchanging(deviceEui64)) {
				ALog.i(ALog.SECURITY, "Exchanging key for device: " + deviceEui64) ;
				isExchangingKeyTable.put(deviceEui64, true);
				
				//Get diffie key
				String sdiffie = generateDiffieKey();
		
				JSONObject holder = new JSONObject();
				
				try {
					holder.put("diffie", sdiffie);
					String js = holder.toString();
					
					//Send diffie to http
					sendDiffie(url, deviceEui64, js);
					
				} catch (JSONException e) {
					e.printStackTrace();
					isExchangingKeyTable.put(deviceEui64, false);
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
	 * @param deviceEui64
	 * @return
	 */
	public String encryptData(String data, PurAirDevice purifier) {
		if (purifier == null) {
			ALog.i(ALog.SECURITY, "Did not encrypt data - Purifier is null");
			return null;
		}
		
		String deviceEui64 = purifier.getEui64();
		String key = securityKeyHashtable.get(deviceEui64);
		
		if (key == null || data == null) {
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
	 * @param deviceEui64
	 * @return
	 */
	public String decryptData(String data, PurAirDevice purifier) {
		ALog.i(ALog.SECURITY, "decryptData data:  "+data) ;

		if (purifier == null) {
			ALog.i(ALog.SECURITY, "Did not encrypt data - Purifier is null");
			return null;
		}
		
		String deviceEui64 = purifier.getEui64();
		String key = securityKeyHashtable.get(deviceEui64);
		ALog.i(ALog.SECURITY, "Decryption - Key   " + key);
		String decryptData = null;
		
		if (key == null || data == null) {
			ALog.i(ALog.SECURITY, "Did not decrypt data - Key is null");
			return null; // TODO return undecrypted data?
		}
		
		data = data.trim() ;

		try {
			byte[] bytesEncData = Util.decodeFromBase64(data.trim());
			byte[] bytesDecData = aesDecryptData(bytesEncData, key);
			//For remove random bytes
			byte[] bytesDecData1 = Util.removeRandomBytes(bytesDecData);
			
			decryptData = new String(bytesDecData1, Charset.defaultCharset());
			
			ALog.i(ALog.SECURITY, "Decrypted data: " + decryptData);
			exchangeKeyCounterTable.put(deviceEui64, 0);
		} catch (Exception e) {
			e.printStackTrace();
			ALog.i(ALog.SECURITY, "Failed to decrypt data - requesting new key exchange");
			
			exchangeKey(urlsTable.get(deviceEui64), deviceEui64);
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
	private byte[] aesDecryptData(byte[] data, String keyStr) throws Exception {
		
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
	private void sendDiffie(String url, String deviceEui64, String strEntity)  {
		
		DISecurityTask diSecurityTask = new DISecurityTask(this);
		diSecurityTask.execute(new String[]{url, deviceEui64, strEntity});
		
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
	public void receiveServerResponse(int responseCode, String responseData, String deviceEui64, String url) {
		ALog.i(ALog.SECURITY, "Received response from device: " + deviceEui64 + "    ResponseCode: " + responseCode) ;
		isExchangingKeyTable.put(deviceEui64, false);
		if ( responseCode == 200 ) {
			exchangeKeyCounterTable.put(deviceEui64, 0);
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
				securityKeyHashtable.put(deviceEui64, key);
				
				keyDecryptListener.keyDecrypt(key, deviceEui64);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			exchangeKey(urlsTable.get(deviceEui64), deviceEui64);
		}
		
	}
	
	private boolean isKeyExchanging(String deviceEui64) {
		// First time exchange
		ALog.i(ALog.SECURITY, "isKeyExchanging: "+isExchangingKeyTable.get(deviceEui64)) ;
		if (!isExchangingKeyTable.containsKey(deviceEui64)) return false;
		// No exchange ongoing
		if (!isExchangingKeyTable.get(deviceEui64)) return false;
		
		return true;
	}
	
	public static void setKeyIntoSecurityHashTable(String deviceEui64, String key) {
		securityKeyHashtable.put(deviceEui64, key);
	}
	
	public static void setUrlIntoUrlsTable(String devId, String url) {
		urlsTable.put(devId, url);
	}
	
	private int exchangeKeyCounter(String deviceEui64) {
		int counter = 0;
		if (exchangeKeyCounterTable.get(deviceEui64) == null) {
			exchangeKeyCounterTable.put(deviceEui64, 0);
			counter = 0;
		} else {
			counter = exchangeKeyCounterTable.get(deviceEui64);
		}
		return counter;
	}
}
