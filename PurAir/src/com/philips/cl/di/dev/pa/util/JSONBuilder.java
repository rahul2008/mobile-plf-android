package com.philips.cl.di.dev.pa.util;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.security.DISecurity;

public class JSONBuilder {
	
	public static final int FAN_SPEED = 2 ;
	public static final int POWER_CONTROL = 1 ;
	public static final int CHILD_LOCK = 3 ;
	public static final int INDICATOR_LIGHT = 4 ;
	
	public static String getDICommBuilder(String key, String value, PurAirDevice purifier) {
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"") ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		dataToSend = new DISecurity(null).encryptData(dataToSend, purifier) ;
		return dataToSend ;
	}
	
	public static String getDICommBuilderForSubscribe(String subscriberId, int ttl, PurAirDevice purifier) {
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append("subscriber").append("\"").append(":").append("\"").append(subscriberId).append("\",") ;
		builder.append("\"").append("ttl\"").append(":").append(ttl) ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		ALog.i(ALog.SUBSCRIPTION, "dataToSend " + dataToSend) ;
		dataToSend = new DISecurity(null).encryptData(dataToSend, purifier) ;
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
	
	public static String getPublishEventBuilderForScheduler(String key, String value) {
		StringBuilder builder = new StringBuilder("{ \"product\":\"0\",\"port\":\"schedules\",\"data\":{} }") ;
		return builder.toString() ;
	}
	
	public static String getPublishEventBuilderForAddScheduler(String data) {
		StringBuilder builder = new StringBuilder("{ \"product\":\"0\",\"port\":\"schedules\",\"data\": ") ;
		builder.append(data).append("}") ;
		return builder.toString() ;
	}
	
	public static String getPublishEventBuilderForDeleteScheduler(int scheduleNumber) {
		String deleteScheduler = "{ \"product\":\"0\",\"port\":\"schedules/"+scheduleNumber+"\",\"data\": {}}" ;
		
		return deleteScheduler ;
	}
	
	public static String getPublishEventBuilderForGetSchedulerDetails(int scheduleNumber) {
		String deleteScheduler = "{ \"product\":\"0\",\"port\":\"schedules/"+scheduleNumber+"\",\"data\": {}}" ;
		
		return deleteScheduler ;
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
	
	public static String getSchedulesJson(String time, String fanSpeed, String days, boolean enabled) {
		ALog.i(ALog.SCHEDULER, time+":"+fanSpeed+":"+days) ;
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append("name").append("\"").append(":").append("\"").append(time).append("\",") ;
		builder.append("\"").append("enabled\"").append(":").append(enabled).append(",") ;
		builder.append("\"").append("time\"").append(":\"").append(time).append("\",") ;
		builder.append("\"").append("days\"").append(":\"").append(days).append("\",") ;
		builder.append("\"").append("product\"").append(":").append(1).append(",") ;
		builder.append("\"").append("port\"").append(":\"").append("air").append("\",") ;
		builder.append("\"").append("command\"").append(":") ;
		builder.append("{\"").append("om\"").append(":\"").append(fanSpeed).append("\"}") ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		ALog.i(ALog.SCHEDULER, dataToSend) ;
		return dataToSend ;
	}
	
	public static String getDICommUIBuilder(PurAirDevice purifier) {
		StringBuilder builder = new StringBuilder("{") ;
		builder.append("\"").append("setup").append("\"").append(":").append("\"").append("inactive").append("\",") ;
		builder.append("\"").append("connection").append("\"").append(":").append("\"").append("disconnected").append("\"") ;
		builder.append("}") ;
		String dataToSend = builder.toString();
		dataToSend = new DISecurity(null).encryptData(dataToSend, purifier) ;
		return dataToSend ;
	}

}
