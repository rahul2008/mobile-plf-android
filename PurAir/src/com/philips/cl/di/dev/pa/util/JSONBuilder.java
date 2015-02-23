package com.philips.cl.di.dev.pa.util;

import java.util.Hashtable;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DISecurity;

public class JSONBuilder {

	public static final int FAN_SPEED = 2;
	public static final int POWER_CONTROL = 1;
	public static final int CHILD_LOCK = 3;
	public static final int INDICATOR_LIGHT = 4;

	public static String getDICommBuilder(String key, String value,
			NetworkNode networkNode) {
		StringBuilder builder = new StringBuilder("{");
		builder.append("\"").append(key).append("\"").append(":").append("\"")
				.append(value).append("\"");
		builder.append("}");
		String dataToSend = builder.toString();
		dataToSend = new DISecurity(null).encryptData(dataToSend, networkNode);
		return dataToSend;
	}

	public static String getAirporDICommBuilder(
			Hashtable<String, String> hashtable, NetworkNode networkNode) {
		if (hashtable == null || hashtable.size() <= 0)
			return null;
		StringBuilder builder = new StringBuilder("{");
		Set<String> keySet = hashtable.keySet();
		int index = 1;
		for (String key : keySet) {
			builder.append("\"").append(key).append("\"").append(":")
					.append("\"").append(hashtable.get(key)).append("\"");
			if (index < keySet.size()) {
				builder.append(",");
			}
			index++;
		}
		builder.append("}");
		ALog.i(ALog.DEVICEHANDLER, builder.toString());
		return new DISecurity(null).encryptData(builder.toString(), networkNode);
	}

	public static String getAirPortPublishEventBuilder(
			Hashtable<String, String> hashtable) {
		if (hashtable == null || hashtable.size() <= 0)
			return null;
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"1\",\"port\":\"air\",\"data\":{");
		Set<String> keySet = hashtable.keySet();
		int index = 1;
		for (String key : keySet) {
			builder.append("\"").append(key).append("\"").append(":")
					.append("\"").append(hashtable.get(key)).append("\"");
			if (index < keySet.size()) {
				builder.append(",");
			}
			index++;
		}
		builder.append("}}");
		ALog.i(ALog.DEVICEHANDLER, builder.toString());
		return builder.toString();
	}

	public static String getDICommBuilderForSubscribe(String subscriberId,
			int ttl, NetworkNode networkNode) {
		StringBuilder builder = new StringBuilder("{");
		builder.append("\"").append("subscriber").append("\"").append(":")
				.append("\"").append(subscriberId).append("\",");
		builder.append("\"").append("ttl\"").append(":").append(ttl);
		builder.append("}");
		String dataToSend = builder.toString();
		ALog.i(ALog.SUBSCRIPTION, "Subscription Data " + dataToSend);
		dataToSend = new DISecurity(null).encryptData(dataToSend, networkNode);
		return dataToSend;
	}

	public static String getDICommBuilderForUnSubscribe(String subscriberId,
			NetworkNode networkNode) {
		StringBuilder builder = new StringBuilder("{");
		builder.append("\"").append("subscriber").append("\"").append(":")
				.append("\"").append(subscriberId).append("\"");
		builder.append("}");
		String dataToSend = builder.toString();
		ALog.i(ALog.SUBSCRIPTION, "UnSubscription Data  " + dataToSend);
		dataToSend = new DISecurity(null).encryptData(dataToSend, networkNode);
		return dataToSend;
	}

	public static String getPublishEventBuilder(String key, String value) {
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"1\",\"port\":\"air\",\"data\":{");
		builder.append("\"").append(key).append("\"").append(":").append("\"")
				.append(value).append("\"");
		builder.append("}}");
		return builder.toString();
	}

	public static String getPublishEventBuilderForSubscribe(String key,
			String value) {
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"1\",\"port\":\"air\",\"data\":{");
		builder.append("\"").append(key).append("\"").append(":").append("\"")
				.append(value).append("\"");
		builder.append(",\"ttl\":").append(AppConstants.LOCAL_SUBSCRIPTIONTIME)
				.append("}}");
		return builder.toString();
	}

	public static String getPublishEventBuilderForSubscribeFirmware(String key,
			String value) {
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"0\",\"port\":\"firmware\",\"data\":{");
		builder.append("\"").append(key).append("\"").append(":").append("\"")
				.append(value).append("\"");
		builder.append(",\"ttl\":").append(AppConstants.LOCAL_SUBSCRIPTIONTIME)
				.append("}}");
		return builder.toString();
	}

	public static String getPublishEventBuilderForScheduler(String key,
			String value) {
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"0\",\"port\":\"schedules\",\"data\":{} }");
		return builder.toString();
	}

	public static String getPublishEventBuilderForAddScheduler(String data) {
		StringBuilder builder = new StringBuilder(
				"{ \"product\":\"0\",\"port\":\"schedules\",\"data\": ");
		builder.append(data).append("}");
		return builder.toString();
	}

	public static String getPublishEventBuilderForEditScheduler(String data,
			int schedulerNumber) {
		String editScheduler = "{ \"product\":\"0\",\"port\":\"schedules/"
				+ schedulerNumber + "\",\"data\":" + data + "}";
		return editScheduler;
	}

	public static String getPublishEventBuilderForDeleteScheduler(
			int scheduleNumber) {
		String deleteScheduler = "{ \"product\":\"0\",\"port\":\"schedules/"
				+ scheduleNumber + "\",\"data\": {}}";

		return deleteScheduler;
	}

	public static String getPublishEventBuilderForGetSchedulerDetails(
			int scheduleNumber) {
		String deleteScheduler = "{ \"product\":\"0\",\"port\":\"schedules/"
				+ scheduleNumber + "\",\"data\": {}}";

		return deleteScheduler;
	}

	public static String getDICOMMPairingJSON(String appEui64, String secretKey) {
		StringBuilder builder = new StringBuilder("{\"Pair\":[\"");
		builder.append(AppConstants.APP_TYPE);
		builder.append("\",\"");
		builder.append(appEui64);
		builder.append("\",\"").append(secretKey);
		builder.append("\"]}");

		return builder.toString();
	}

	public static String getSchedulesJson(String time, String fanSpeed,
			String days, boolean enabled) {
		ALog.i(ALog.SCHEDULER, time + ":" + fanSpeed + ":" + days);
		int pwr = 1;
		if (fanSpeed.equals("0")) {
			pwr = 0;
		}

		StringBuilder builder = new StringBuilder("{");
		builder.append("\"").append("name").append("\"").append(":")
				.append("\"").append(time).append("\",");
		builder.append("\"").append("enabled\"").append(":").append(enabled)
				.append(",");
		builder.append("\"").append("time\"").append(":\"").append(time)
				.append("\",");
		builder.append("\"").append("days\"").append(":\"").append(days)
				.append("\",");
		builder.append("\"").append("product\"").append(":").append(1)
				.append(",");
		builder.append("\"").append("port\"").append(":\"").append("air")
				.append("\",");
		builder.append("\"").append("command\"").append(":");
		builder.append("{\"").append("pwr\"").append(":\"").append(pwr)
				.append("\",\"");
		builder.append("om\"").append(":\"").append(fanSpeed).append("\"}");
		builder.append("}");
		String dataToSend = builder.toString();
		ALog.i(ALog.SCHEDULER, dataToSend);
		return dataToSend;
	}

	public static String getDICommUIBuilder(NetworkNode networkNode) {
		StringBuilder builder = new StringBuilder("{");
		builder.append("\"").append("setup").append("\"").append(":")
				.append("\"").append("inactive").append("\",");
		builder.append("\"").append("connection").append("\"").append(":")
				.append("\"").append("disconnected").append("\"");
		builder.append("}");
		String dataToSend = builder.toString();
		dataToSend = new DISecurity(null).encryptData(dataToSend, networkNode);
		return dataToSend;
	}
	
	public static String getWifiPortJson(String ssid, String password, NetworkNode networkNode) {
		ALog.i(ALog.EWS, "getWifiPortJson");
		JSONObject holder = new JSONObject();
		try {
			holder.put("ssid", ssid);
			holder.put("password", password);
		} catch (JSONException e) {
			ALog.e(ALog.EWS, "Error: " + e.getMessage());
		}
		String js = holder.toString();
		ALog.i(ALog.EWS, "getWifiPortJson js: " + js);
		String encryptedData = new DISecurity(null).encryptData(js, networkNode);

		return encryptedData ;
	}
	
	public static String getWifiPortWithAdvConfigJson(String ssid,
			String password, String ipAdd, String subnetMask, String gateWay,
			NetworkNode networkNode) {
		ALog.i(ALog.EWS, "getWifiPortJson");
		JSONObject holder = new JSONObject();
		try {
			holder.put("ssid", ssid);
			holder.put("password", password);
			holder.put("ipaddress", ipAdd);
			holder.put("dhcp", false);
			holder.put("netmask", subnetMask);
			holder.put("gateway", gateWay);
		} catch (JSONException e) {
			ALog.e(ALog.EWS, "Error: " + e.getMessage());
		}
		String js = holder.toString();
		ALog.i(ALog.EWS, "getWifiPortWithAdvConfigJson js: " + js);
		String encryptedData = new DISecurity(null).encryptData(js, networkNode);

		return encryptedData ;
	}

}
