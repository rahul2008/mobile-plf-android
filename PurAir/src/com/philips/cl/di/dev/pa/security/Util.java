package com.philips.cl.di.dev.pa.security;

import java.nio.charset.Charset;
import java.util.Random;

import com.philips.cl.di.dev.pa.utils.ALog;

import android.util.Base64;
import android.util.Log;

public class Util {
	
	public static final int MIN = 101;
	public static final int MAX = Integer.MAX_VALUE;
	
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public final static String pValue = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6"
			+ "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0"
			+ "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70"
			+ "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
			+ "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708"
			+ "DF1FB2BC2E4A4371";

	public final static String gValue = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
			+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
			+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
			+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
			+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24"
			+ "855E6EEB22B3B2E5";
	
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encodeToBase64(byte[] data) throws Exception{
		String strEncodeBase64 = null;
		if(data!=null && data.length > 0){
			strEncodeBase64 = Base64.encodeToString(data, Base64.DEFAULT);
		}
		return strEncodeBase64;
	}
	
	public static byte[] decodeFromBase64(String data) {
		byte[] byteDecodedBase64 = null;
		if (data != null) {
			byteDecodedBase64 = Base64.decode(data.getBytes(Charset.defaultCharset()), Base64.DEFAULT);
		}
		return byteDecodedBase64;
	}
	
	/**
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexToBytes(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

	
	/**
	 * 
	 * @param bytes
	 * @return hexString with letters (A-F) in capitals
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for ( int j = 0; j < bytes.length; j++ ) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String generateRandomNum() {
		Random random = new Random();
		String randStr = String.valueOf(random.nextInt((MAX - MIN) + 1) + MIN);
		return randStr;
	}
	
	public static String getEvenNumberSecretKey(String secKey) {
		String tempKey = secKey;
		if (secKey != null) {
			int keyLength = secKey.length();
			if (keyLength % 2 == 0) {
				tempKey = secKey;
			} else {
				tempKey = "0"+secKey;
				ALog.d(ALog.SECURITY, "Appended zero to Secret Key - Resulting lenght: " + tempKey.length());
			}
		}
		return tempKey;
	}


}
