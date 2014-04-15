package com.philips.cl.di.dev.pa.util;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.security.DISecurity;

public class JSONBuilder {
	
	public static final int FAN_SPEED = 2 ;
	public static final int POWER_CONTROL = 1 ;
	public static final int CHILD_LOCK = 3 ;
	public static final int INDICATOR_LIGHT = 4 ;
	
	public static String getDICommBuilder(String key, String value) {
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"") ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		dataToSend = new DISecurity(null).encryptData(dataToSend, AppConstants.deviceId) ;
		return dataToSend ;
	}
	
	public static String getDICommBuilderForSubscribe(String cppID, int ttl) {
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append("subscriber").append("\"").append(":").append("\"").append(cppID).append("\",") ;
		builder.append("\"").append("ttl\"").append(":").append(ttl) ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		ALog.i(ALog.SUBSCRIPTION, dataToSend) ;
		dataToSend = new DISecurity(null).encryptData(dataToSend, AppConstants.deviceId) ;
		return dataToSend ;
	}

	public static String getPublishEventBuilder(String key, String value) {
		StringBuilder builder = new StringBuilder("{ \"product\":\"1\",\"port\":\"air\",\"data\":{") ;
		builder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"") ;
		builder.append("}}") ;		
		return builder.toString() ;
	}
	
	public static String getPublishEventBuilderForSubscribe(String key, String value) {
		StringBuilder builder = new StringBuilder("{ \"product\":\"1\",\"port\":\"air\",\"data\":{") ;
		builder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"") ;
		builder.append(",\"ttl\":3360}}") ;		
		return builder.toString() ;
	}
	
	public static String getDICOMMPairingJSON(String appEui64, String secretKey) {
		StringBuilder builder = new StringBuilder("{\"Pair\":[\"");
		builder.append(AppConstants.APP_TYPE) ;
		builder.append("\",\"") ;
		builder.append(appEui64);
		builder.append("\",\"").append(secretKey);
		builder.append("\"]}");
		
		return builder.toString() ;
	}

}
