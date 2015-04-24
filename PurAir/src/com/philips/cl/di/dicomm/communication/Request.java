package com.philips.cl.di.dicomm.communication;

import java.util.Map;
import java.util.Set;


public abstract class Request {
	
	public abstract Response execute();

	protected String convertKeyValuesToJson(Map<String, String> dataMap) {
		if (dataMap == null || dataMap.size() <= 0) return "{}";
		
		StringBuilder builder = new StringBuilder("{");
		Set<String> keySet = dataMap.keySet();
		int index = 1;
		for (String key : keySet) {
			builder.append("\"").append(key).append("\":\"").append(dataMap.get(key)).append("\"");
			if (index < keySet.size()) {
				builder.append(",");
			}
			index++;
		}
		builder.append("}");
		return builder.toString();
	}
}
