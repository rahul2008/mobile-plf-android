package com.philips.cl.di.dev.pa.dashboard;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;

public class CMAHelper {
	
	private final String appId ;
	private static final String HASH_ALG = "HmacSHA1";
	
	private static final String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	public CMAHelper(String appID) {
		appId = appID ;
	}
	
	public String getURL(String baseUrl, String areaID, String type, String date) throws RuntimeException{
		if(baseUrl == null || baseUrl.isEmpty() || areaID == null || areaID.isEmpty()
				|| type == null || type.isEmpty() || date == null || date.isEmpty()) 
			throw new RuntimeException("Invalid params : Some param is null or empty");
		
		if(!isValidURL(baseUrl)) throw new RuntimeException("URL is not in a valid format");
		if(!isValidDate(date)) throw new RuntimeException("Date is not the format yyyyMMddHHmm");
		
		String publicKey = createPublicKeyUrl(baseUrl, areaID, type, date);
		String cmaUrlKey = generateCMAUrlKey(publicKey);
		String url = createWeatherUrl(baseUrl, areaID, type, date, cmaUrlKey);
		ALog.i(ALog.OUTDOOR_LOCATION, "Final Weather URL " + url);
		return url;
	}

	private String createPublicKeyUrl(String baseUrl, String areaID, String type, String date) {
		StringBuilder publicKeyBuilder = new StringBuilder(baseUrl) ;
		publicKeyBuilder.append("?areaid=").append(areaID) ;
		publicKeyBuilder.append("&type=").append(type);
		publicKeyBuilder.append("&date=").append(date);
		publicKeyBuilder.append("&appid=").append(appId);
		return publicKeyBuilder.toString();
	}
	
	private String generateCMAUrlKey(String publicKey) {
		String finalKey = "";
		try {
			String base64key = Util.encodeToBase64(hmacSha1(publicKey,  Utils.getCMA_PrivateKey()));
			finalKey = URLEncoder.encode(base64key.trim(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return finalKey;
	}
	
	private String createWeatherUrl(String baseUrl, String areaID, String type, String date, String key) {
		StringBuilder urlBuilder = new StringBuilder() ; 
		urlBuilder.append(baseUrl).append("?areaid=").append(areaID);
		urlBuilder.append("&type=").append(type).append("&date=").append(date) ;
		urlBuilder.append("&appid=").append(appId.substring(0, 6));
		urlBuilder.append("&key=").append(key);
		return urlBuilder.toString();
	}

	private byte[] hmacSha1(String value, String key) {
		SecretKeySpec secret = new SecretKeySpec(key.getBytes(Charset.defaultCharset()), HASH_ALG);
		Mac mac = null;
		try {
			mac = Mac.getInstance(HASH_ALG);
			mac.init(secret);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		if( mac == null ) return null ;
		byte[] bytes = mac.doFinal(value.getBytes(Charset.defaultCharset()));
		return bytes;
	}
	
	private boolean isValidURL(String url) {
		Pattern pattern = Pattern.compile(URL_PATTERN);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	@SuppressLint("SimpleDateFormat")
	private boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
}
