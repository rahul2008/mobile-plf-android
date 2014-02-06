package com.philips.cl.di.ews;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.disecurity.DISecurity;

import android.provider.SyncStateContract.Helpers;
import android.util.Base64;
import android.util.Log;

public class EasyWifiSetupInfo {
	public static final String WIFI_URI = "/di/v1/products/1/wifi";
	public static final String DEVICE_URI = "/di/v1/products/1/device";
	public static final String SECURITY_URI = "/di/v1/products/0/security";
	public String mCurrentSsid = "";
	public String mDeviceSsid = "PHILIPS Setup";
	public String mEndPoint = "http://192.168.1.1";
	public String mPassword = "";
	public String mDeviceName = "";
	public String mCapabilities = "";
	public String mDiffie = "";
	public String mHellman = "";
	public String mKey = "";

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

	public final static int XaValue = 5343;

	public final static int XbValue = 1234224;

	public byte[] encrypt(byte[] dataToEncrypt) throws Exception {

		Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
		byte[] longKey = (new BigInteger(mKey, 16)).toByteArray();
		byte[] key;
		if (longKey[0] == 0) {
			key = Arrays.copyOfRange(longKey, 1, 17);
		} else {
			key = Arrays.copyOf(longKey, 16);
		}

		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		byte[] ivBytes = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		c.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		return c.doFinal(dataToEncrypt);
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	StringEntity toJson(DISecurity diSecurity) throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("ssid", mCurrentSsid);
		holder.put("password", mPassword);
		/*if (mPassword != null && mPassword.equals("") == false) {
			String s = Base64.encodeToString(encrypt(mPassword.getBytes()),
					Base64.DEFAULT);
			holder.put("password", s);
		}*/
		String js = holder.toString();
		
		String builderTemp = diSecurity.encryptData(js, "devId01");
		Log.d(getClass().getSimpleName(), "toJson  = "+builderTemp);
		return new StringEntity(builderTemp) ;
		
		//return new StringEntity(js);
	}

	public UrlEncodedFormEntity toWifiFormParams()
			throws UnsupportedEncodingException {
		List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>(
				2);
		nameValuePair.add(new BasicNameValuePair("ssid", mCurrentSsid));
		nameValuePair.add(new BasicNameValuePair("key", mPassword));
		return new UrlEncodedFormEntity(nameValuePair);
	}

	public UrlEncodedFormEntity toDeviceFormParams()
			throws UnsupportedEncodingException {
		List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>(
				2);
		nameValuePair.add(new BasicNameValuePair("device[name]", mDeviceName));
		return new UrlEncodedFormEntity(nameValuePair);
	}

	public boolean isOpen() {
		if (mCapabilities.contains("WPA") || mCapabilities.contains("WEP")) {
			return false;
		} else {
			return true;
		}
	}

	public StringEntity toJsonDevice(DISecurity diSecurity) throws JSONException,
			UnsupportedEncodingException {
		JSONObject holder = new JSONObject();
		holder.put("name", mDeviceName);
		String js = holder.toString();
		// Call encryption here.
		String builderTemp = diSecurity.encryptData(js, "devId01");
		Log.d(getClass().getSimpleName(), "toJsonDevice  = "+builderTemp);
		return new StringEntity(builderTemp) ;
		//return new StringEntity(js);
	}

	public void calculateKey(String hellman) {
		mHellman = hellman;
		BigInteger p = new BigInteger(pValue, 16);
		BigInteger g = new BigInteger(mHellman, 16);
		BigInteger Xa = new BigInteger(Integer.toString(XaValue));
		BigInteger y = g.modPow(Xa, p);
		
		mKey = y.toString(16);// toString();//Arrays.copyOf(y.toByteArray(),
								// 16).toString();
	}

	public StringEntity toJsonDiffie() throws Exception {
		BigInteger p = new BigInteger(pValue, 16);
		BigInteger g = new BigInteger(gValue, 16);
		BigInteger Xa = new BigInteger(Integer.toString(XaValue));
		mDiffie = bytesToHex(g.modPow(Xa, p).toByteArray());
		JSONObject holder = new JSONObject();
		holder.put("diffie", mDiffie);
		String js = holder.toString();
		return new StringEntity(js);

	}

}
