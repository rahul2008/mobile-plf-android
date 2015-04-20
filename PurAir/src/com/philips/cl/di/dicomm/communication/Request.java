package com.philips.cl.di.dicomm.communication;

import java.util.Map;
import java.util.Set;


public abstract class Request {

	public abstract Response execute();

	protected String convertKeyValuesToJson(Map<String, Object> dataMap) {
		if (dataMap == null || dataMap.size() <= 0) return "{}";

		StringBuilder builder = new StringBuilder("{");
		Set<String> keySet = dataMap.keySet();
		int index = 1;
		for (String key : keySet) {
			Object value = dataMap.get(key);

			// TODO DICOMM REFACTOR add support for all DIComm datatypes
			if (value instanceof String) {
				builder.append("\"").append(key).append("\":\"").append(dataMap.get(key)).append("\"");
			} else if (value instanceof Integer) {
				builder.append("\"").append(key).append("\":").append(dataMap.get(key));
			} else {
				builder.append("\"").append(key).append("\":\"").append(dataMap.get(key)).append("\"");
			}

			if (index < keySet.size()) {
				builder.append(",");
			}
			index++;
		}
		builder.append("}");
		return builder.toString();
	}
}
