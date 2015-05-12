package com.philips.cl.di.dicomm.security;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import android.util.Base64;


public class ByteUtil {
	
	public static final int MIN = 101;
	public static final int MAX = Integer.MAX_VALUE;
	public static final int RANDOM_BYTE_ARR_SIZE = 2;
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	public final static String PVALUE = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6"
			+ "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0"
			+ "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70"
			+ "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
			+ "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708"
			+ "DF1FB2BC2E4A4371";

	public final static String GVALUE = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
			+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
			+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
			+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
			+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24"
			+ "855E6EEB22B3B2E5";
	
	public static String encodeToBase64(byte[] data) throws Exception {
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
	
	public static byte[] hexToBytes(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }
	
	public static String bytesToCapitalizedHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for ( int j = 0; j < bytes.length; j++ ) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String generateRandomNum() {
		Random random = new Random();
		String randStr = String.valueOf(random.nextInt(MAX - MIN) + 1 + MIN);
		return randStr;
	}
	
	public static byte[] getRandomByteArray(int size) {
		byte[] result = new byte[size];
		Random random = new Random();
		random.nextBytes(result);
		return result;
	}

	public static byte[] addRandomBytes(byte[] data) {
		
		if (data == null) {
			return null;
		}
		
		byte[] randomBytes = getRandomByteArray(RANDOM_BYTE_ARR_SIZE);
		
		int dataLength = data.length;
		int randomBytesLength = randomBytes.length;
		
		byte[] dataBytes = new byte[dataLength + randomBytesLength];
		
		System.arraycopy(randomBytes, 0, dataBytes, 0, randomBytesLength);
		System.arraycopy(data, 0, dataBytes, randomBytesLength, dataLength);
		
		return dataBytes;
	}
	
	public static byte[] removeRandomBytes(byte[] data) {
		
		if (data == null || data.length < 3) {
			return data;
		}
		
		byte[] dataBytes = Arrays.copyOfRange(data, RANDOM_BYTE_ARR_SIZE, data.length);
		return dataBytes;
	}
}
