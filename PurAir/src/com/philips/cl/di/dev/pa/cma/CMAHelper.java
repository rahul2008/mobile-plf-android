package com.philips.cl.di.dev.pa.cma;

import java.net.URLEncoder;

public class CMAHelper {
	
	private final String appId ;
	private final String privateKey ;
	
	public CMAHelper(String appID, String privateKey) {
		this.appId = appID ;
		this.privateKey = privateKey;
	}
	
	public String getURL(String baseUrl, String areaID, String type, String date) throws RuntimeException{
		if(baseUrl == null || baseUrl.isEmpty() || areaID == null || areaID.isEmpty()
				|| type == null || type.isEmpty() || date == null || date.isEmpty()) 
			throw new RuntimeException("Invalid params : Some param is null or empty");
		
		if(!CMAUtils.isValidURL(baseUrl)) throw new RuntimeException("URL is not in a valid format");
		if(!CMAUtils.isValidDate(date)) throw new RuntimeException("Date is not the format yyyyMMddHHmm");
		
		String publicKey = createPublicKeyUrl(baseUrl, areaID, type, date);
		String cmaUrlKey = generateCMAUrlKey(publicKey);
		String url = createWeatherUrl(baseUrl, areaID, type, date, cmaUrlKey);
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
			String base64key = CMAUtils.encodeToBase64(CMAUtils.hmacSha1(publicKey,privateKey));
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
}
