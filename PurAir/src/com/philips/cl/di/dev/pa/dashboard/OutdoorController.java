package com.philips.cl.di.dev.pa.dashboard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class OutdoorController implements ServerResponseListener {
	
	private static String APP_ID ;
	
	private static String BASE_URL;
	
	private static final String HASH_ALG = "HmacSHA1";

	private List<OutdoorEventListener> outdoorEventListeners;
	
	private static OutdoorController smInstance;
	
	private OutdoorController() {
		APP_ID = Utils.getCMA_AppID() ;
		BASE_URL = Utils.getCMA_BaseURL() ;
		outdoorEventListeners = new ArrayList<OutdoorEventListener>();
	}
	
	public static OutdoorController getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorController();
		}
		return smInstance;
	}
	
	public void setOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.add(outdoorEventListener);
	}
	
	public void removeOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.remove(outdoorEventListener);
	}
	
	private String buildURL(String areaID, String type, String date, String appID) {
		String url = "";
		String publicKey = BASE_URL + "?areaid=" + areaID + "&type=" + type + "&date=" + date + "&appid="  + APP_ID;
		ALog.i(ALog.DASHBOARD, "Public key :: " + publicKey);
		String key = "";
		String finalKey = "";
		try {
			finalKey = Util.encodeToBase64(hmacSha1(publicKey,  Utils.getCMA_PrivateKey()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String mostCertainlyTheFinalKey = "";
		try {
			mostCertainlyTheFinalKey = URLEncoder.encode(finalKey.trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ALog.i(ALog.DASHBOARD, "key :: " + key + " finalKey " + finalKey + " mostCertainlyTheFinalKey " + mostCertainlyTheFinalKey);
		
		url = BASE_URL + "?areaid=" + areaID + "&type=" + type + "&date=" + date + "&appid="  + APP_ID.substring(0, 6) + "&key=" + mostCertainlyTheFinalKey;
		
		ALog.i(ALog.DASHBOARD, "Final URL " + url);
		
		return url;
	}

	public void startCitiesTask(String areaID) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		ALog.i(ALog.DASHBOARD, "startCitiesTask dateFormate " + date);
		TaskGetHttp citiesList = new TaskGetHttp(buildURL(areaID, "air", date, APP_ID), PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	public void startOutdoorWeatherTask(String areaID) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		TaskGetHttp citiesList = new TaskGetHttp(buildURL(areaID, "observe", date, APP_ID), PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	private void notifyListeners(String data) {
		if(outdoorEventListeners == null) return;
		
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data);
		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data);
		
		for(int index = 0; index < outdoorEventListeners.size(); index++) {
			if(outdoorAQI != null)
				outdoorEventListeners.get(index).outdoorAQIDataReceived(outdoorAQI, outdoorAQI.getAreaID());
			
			if(outdoorWeather != null) 
				outdoorEventListeners.get(index).outdoorWeatherDataReceived(outdoorWeather, outdoorWeather.getAreaID());
		}
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String data, String fromIp) {
		ALog.i(ALog.DASHBOARD, "OutdoorController data received " + data + " responseCode " + responseCode);
		if(data != null) {
			notifyListeners(data);
		}
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
		
		if(mac == null) {
			return null;
		}
		
	    byte[] bytes = mac.doFinal(value.getBytes(Charset.defaultCharset()));
	    return bytes;
	}

}
