package com.philips.cl.disecurity;

import java.util.Random;

import android.util.Base64;

public class Util {
	
	public static final int MIN = 101;
	public static final int MAX = Integer.MAX_VALUE;
	
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	
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
			byteDecodedBase64 = Base64.decode(data.getBytes(), Base64.DEFAULT);
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


}
