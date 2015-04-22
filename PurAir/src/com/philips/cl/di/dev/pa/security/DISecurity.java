package com.philips.cl.di.dev.pa.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;

public class DISecurity implements ServerResponseListener {
    
    public interface DecryptionFailedListener{
        void onDecryptionFailed(NetworkNode networkNode);
    }

	public static final String BOOT_STRAP_ID_3 = "MDAwMD" ;
	private static Hashtable<String, Integer> 
			exchangeKeyCounterTable = new Hashtable<String, Integer>();
	
    private DecryptionFailedListener mDecryptionFailedListener;

	/**
	 * Constructor
	 * @param context
	 */
    public DISecurity(KeyDecryptListener keyDecryptListener) {
        this();
    }
    
    public DISecurity() {
        ALog.i(ALog.SECURITY, "Initialized DISecurity");
    }
	
	public void initializeExchangeKeyCounter(String deviceEui64) {
		exchangeKeyCounterTable.put(deviceEui64, 0);
	}

    public void setDecryptionFailedListener(DecryptionFailedListener decryptionFailedListener) {
        this.mDecryptionFailedListener = decryptionFailedListener;
    }
    /**
     * 
     * @param data
     * @param deviceEui64
     * @return
     */
    public String encryptData(String data, NetworkNode networkNode) {
        if (networkNode == null) {
            ALog.i(ALog.SECURITY, "Did not encrypt data - NetworkNode is null");
            return null;
        }
        
        String key = networkNode.getEncryptionKey();
        if (key == null || key.isEmpty()) {
            ALog.i(ALog.SECURITY, "Did not encrypt data - Key is null or Empty");
            return null; // TODO return unencrypted data?
        }
        if (data == null || data.isEmpty()) {
            ALog.i(ALog.SECURITY, "Did not encrypt data - Data is null or Empty");
            return null; // TODO return unencrypted data?
        }
        
        String encryptedBase64Str = null;
        try {
            byte[] encrypDatas = DiffieHellmanUtil.aesEncryptData(data, key);
            encryptedBase64Str = Util.encodeToBase64(encrypDatas);
            ALog.i(ALog.SECURITY, "Encrypted data: " + encryptedBase64Str);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.i(ALog.SECURITY, "Failed to encrypt data with key - " + "Error: " + e.getMessage());
        }
        return encryptedBase64Str;
    }
    
    /**
     * 
     * @param data
     * @param deviceEui64
     * @return
     */
    public String decryptData(String data, NetworkNode networkNode) {
        ALog.i(ALog.SECURITY, "decryptData data:  "+data) ;


        if (networkNode == null) {
            ALog.i(ALog.SECURITY, "Did not encrypt data - NetworkNode is null");
            return null;
        }
        String key = networkNode.getEncryptionKey();
        ALog.i(ALog.SECURITY, "Decryption - Key   " + key);
        String decryptData = null;
        
        if (data == null || data.isEmpty()) {
            ALog.i(ALog.SECURITY, "Did not decrypt data - data is null");
            return null;
        }

        if (key == null || key.isEmpty()) {
            ALog.i(ALog.SECURITY, "Did not decrypt data - key is null");
            ALog.i(ALog.SECURITY, "Failed to decrypt data - requesting new key exchange");
            
//            String portUrl = Utils.getPortUrl(Port.SECURITY, networkNode.getIpAddress());
//          exchangeKey(portUrl, networkNode.getCppId());

            notifyDecryptionFailedListener(networkNode);
            return null;
        }
        
        data = data.trim() ;

        try {
            byte[] bytesEncData = Util.decodeFromBase64(data.trim());
            byte[] bytesDecData = DiffieHellmanUtil.aesDecryptData(bytesEncData, key);
            //For remove random bytes
            byte[] bytesDecData1 = Util.removeRandomBytes(bytesDecData);
            
            decryptData = new String(bytesDecData1, Charset.defaultCharset());
            
            ALog.i(ALog.SECURITY, "Decrypted data: " + decryptData);
            exchangeKeyCounterTable.put(networkNode.getCppId(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.i(ALog.SECURITY, "Failed to decrypt data - requesting new key exchange");
            
            String portUrl = Utils.getPortUrl(Port.SECURITY, networkNode.getIpAddress());
            exchangeKey(portUrl, networkNode.getCppId());
        }
        
        if(decryptData == null){
            notifyDecryptionFailedListener(networkNode);
        }

        return decryptData;
    }

    private void notifyDecryptionFailedListener(NetworkNode networkNode) {
        if(mDecryptionFailedListener!=null){
            mDecryptionFailedListener.onDecryptionFailed(networkNode);
        }
    }
    
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */ 
	public void exchangeKey(String url, String deviceEui64)  {
//		int counter = exchangeKeyCounter(deviceEui64);
//		ALog.i(ALog.SECURITY, "deviceEui64: " + deviceEui64 + ", exchange key counter: " + counter) ;
//		if (counter < 3) {
//			counter++;
//			exchangeKeyCounterTable.put(deviceEui64, counter);
//			ALog.i(ALog.SECURITY, "Requested Key exchange for device: " + deviceEui64+":"+isKeyExchanging(deviceEui64)) ;
//			if (!isKeyExchanging(deviceEui64)) {
//				ALog.i(ALog.SECURITY, "Exchanging key for device: " + deviceEui64) ;
//				isExchangingKeyTable.put(deviceEui64, true);
//				
//				//Get diffie key
//				String randomValue = Util.generateRandomNum();
//				String sdiffie = DiffieHellmanUtil.generateDiffieKey(randomValue);
//		
//				JSONObject holder = new JSONObject();
//				
//				try {
//					holder.put("diffie", sdiffie);
//					String js = holder.toString();
//					
//					//Send diffie to http
//					sendDiffie(url, deviceEui64, js, randomValue);
//					
//				} catch (JSONException e) {
//					e.printStackTrace();
//					isExchangingKeyTable.put(deviceEui64, false);
//				}
//				ALog.d(ALog.SECURITY, "Generated diffie key: "+sdiffie);
//			}
//		} else {
//			ALog.e(ALog.SECURITY, "Third time key exchange failed");
//		}
	}
	
	
	
//	/**
//	 * 
//	 * @param strEntity
//	 */
//	private void sendDiffie(String url, String deviceEui64, String strEntity, String randomValue)  {
//		
//		DISecurityTask diSecurityTask = new DISecurityTask(this);
//		diSecurityTask.execute(new String[]{url, deviceEui64, strEntity, randomValue});
//		
//	}

	/**
	 * 
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String deviceEui64, String url, String randomValue) {
//		ALog.i(ALog.SECURITY, "Received response from device: " + deviceEui64 + "    ResponseCode: " + responseCode);
//		ALog.i(ALog.SECURITY,"Response: "+ responseData);
//		isExchangingKeyTable.put(deviceEui64, false);
//		if ( responseCode == 200 ) {
//			exchangeKeyCounterTable.put(deviceEui64, 0);
//			JSONObject json;
//			try {
//				json = new JSONObject(responseData);
//				String shellman = json.getString("hellman");
//				ALog.d(ALog.SECURITY, "result hellmam= "+shellman + "     Length:= " +shellman.length());
//
//				String skeyEnc = json.getString("key");
//				ALog.d(ALog.SECURITY, "encrypted key= "+skeyEnc+"    length:= " + skeyEnc.length());
//				
//				String secKey = DiffieHellmanUtil.generateSecretKey(shellman, randomValue);
//				ALog.d(ALog.SECURITY, "secret key= "+secKey + "    length= "+secKey.length());
//				
//				secKey = Util.getEvenNumberSecretKey(secKey);
//				
//				byte[] bytesEncKey = Util.hexToBytes(skeyEnc);
//
//				byte [] bytesDecKey = DiffieHellmanUtil.aesDecryptData(bytesEncKey, secKey);
//				
//				String key = Util.bytesToHex(bytesDecKey);
//				ALog.i(ALog.SECURITY, "decryted key= " + key);
//				
//				keyDecryptListener.keyDecrypt(key, deviceEui64);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			exchangeKey(url, deviceEui64);
//		}
//		
	}
	
//	private boolean isKeyExchanging(String deviceEui64) {
//		// First time exchange
//		ALog.i(ALog.SECURITY, "isKeyExchanging: "+isExchangingKeyTable.get(deviceEui64)) ;
//		if (!isExchangingKeyTable.containsKey(deviceEui64)) return false;
//		// No exchange ongoing
//		if (!isExchangingKeyTable.get(deviceEui64)) return false;
//		
//		return true;
//	}
//	
//	private int exchangeKeyCounter(String deviceEui64) {
//		int counter = 0;
//		if (exchangeKeyCounterTable.get(deviceEui64) == null) {
//			exchangeKeyCounterTable.put(deviceEui64, 0);
//			counter = 0;
//		} else {
//			counter = exchangeKeyCounterTable.get(deviceEui64);
//		}
//		return counter;
//	}
}
