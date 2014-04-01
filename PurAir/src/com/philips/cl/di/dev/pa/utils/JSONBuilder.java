package com.philips.cl.di.dev.pa.utils;

import com.philips.cl.di.dev.pa.constant.AppConstants;
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
		dataToSend = new DISecurity(null).encryptData(dataToSend, AppConstants.DEVICEID) ;
		return dataToSend ;
	}

	public static String getPublishEventBuilder(String key, String value) {
		StringBuilder builder = new StringBuilder("{ \"product\":\"1\",\"port\":\"air\",\"data\":{") ;
		builder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"") ;
		builder.append("}}") ;		
		return builder.toString() ;
	}

}
