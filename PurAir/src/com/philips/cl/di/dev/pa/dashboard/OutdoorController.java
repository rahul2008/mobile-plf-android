package com.philips.cl.di.dev.pa.dashboard;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class OutdoorController implements ServerResponseListener {
	
	private static String APP_ID ;
	
	private static String BASE_URL;
	
	private static final String BASE_URL_AQI = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php";
	
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

	@SuppressLint("SimpleDateFormat")
	public void startCitiesTask(String areaID) {
		//If purifier in demo mode, skip download data
		if (PurAirApplication.isDemoModeEnable()) return;
			
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		ALog.i(ALog.DASHBOARD, "startCitiesTask dateFormate " + date);
		TaskGetHttp citiesList = new TaskGetHttp(buildURL(BASE_URL_AQI,areaID, "air", date, APP_ID), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	@SuppressLint("SimpleDateFormat")
	public void startOutdoorWeatherTask(String areaID) {
		//If purifier in demo mode, skip download data
		if (PurAirApplication.isDemoModeEnable()) return;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		TaskGetHttp citiesList = new TaskGetHttp(buildURL(BASE_URL,areaID, "observe", date, APP_ID), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	private void notifyListeners(String data, String areaID) {
		if(outdoorEventListeners == null) return;
		
		List<OutdoorAQI> outdoorAQIList = DataParser.parseLocationAQI(data);
		List<OutdoorWeather> outdoorWeatherList = DataParser.parseLocationWeather(data);
		
		for(int index = 0; index < outdoorEventListeners.size(); index++) {
			if(outdoorAQIList != null && !outdoorAQIList.isEmpty()) {
				Iterator<OutdoorAQI> iter = outdoorAQIList.iterator();
				while(iter.hasNext()) {
					OutdoorAQI outdoorAQI = iter.next();
					outdoorEventListeners.get(index).outdoorAQIDataReceived(outdoorAQI, areaID);
				}
			}
			
			if(outdoorWeatherList != null && !outdoorWeatherList.isEmpty()) {
				Iterator<OutdoorWeather> iter = outdoorWeatherList.iterator();
				while(iter.hasNext()) {
					OutdoorWeather outdoorWeather = iter.next();
					outdoorEventListeners.get(index).outdoorWeatherDataReceived(outdoorWeather, areaID);
				}
			}
		}
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String data, String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorController data received " + data + " responseCode " + responseCode);
		if(data != null) {
			notifyListeners(data, areaID);
		}
	}
	
	private String buildURL(String baseUrl, String areaID, String type, String date, String appID) {
		String url = AppConstants.EMPTY_STRING;
		String mostCertainlyTheFinalKey = AppConstants.EMPTY_STRING;
		StringBuilder publicKeyBuilder = new StringBuilder(baseUrl) ;
		publicKeyBuilder.append("?areaid=").append(areaID) ;
		publicKeyBuilder.append("&type=").append(type);
		publicKeyBuilder.append("&date=").append(date);
		publicKeyBuilder.append("&appid=").append(APP_ID);
		ALog.i(ALog.OUTDOOR_LOCATION, "Public key :: " + publicKeyBuilder.toString());
		String key = "";
		String finalKey = "";
		try {
			finalKey = Util.encodeToBase64(hmacSha1(publicKeyBuilder.toString(),  Utils.getCMA_PrivateKey()));
			mostCertainlyTheFinalKey = URLEncoder.encode(finalKey.trim(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		ALog.i(ALog.OUTDOOR_LOCATION, "key :: " + key + " finalKey " + finalKey + " mostCertainlyTheFinalKey " + mostCertainlyTheFinalKey);
		
		StringBuilder urlBuilder = new StringBuilder() ; 
		urlBuilder.append(baseUrl).append("?areaid=").append(areaID);
		urlBuilder.append("&type=").append(type).append("&date=").append(date) ;
		urlBuilder.append("&appid=").append(APP_ID.substring(0, 6));
		urlBuilder.append("&key=").append(mostCertainlyTheFinalKey);
		
		url = urlBuilder.toString() ;
		ALog.i(ALog.OUTDOOR_LOCATION, "Final Weather URL " + url);
		
		return url;
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

}