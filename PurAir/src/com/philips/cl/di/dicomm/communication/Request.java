package com.philips.cl.di.dicomm.communication;

import java.util.Map;
import java.util.Set;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;


public abstract class Request {

	protected final Map<String, Object> mDataMap;
    protected final NetworkNode mNetworkNode;
    protected final ResponseHandler mResponseHandler;
    
    public Request(NetworkNode networkNode, Map<String, Object> dataMap, ResponseHandler responseHandler) {
        this.mDataMap = dataMap;
        this.mNetworkNode = networkNode;
        this.mResponseHandler = responseHandler;
    }

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
			} else if (value instanceof Boolean) {
			    builder.append("\"").append(key).append("\":").append(dataMap.get(key));
			} else if (value instanceof String[]){
			    builder.append("\"").append(key).append("\":");
			    appendStringArray(builder, (String[]) value);
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

    private void appendStringArray(StringBuilder builder, String[] array) {
       builder.append("[");
        for (int index = 0; index < array.length; index++) {
            builder.append("\"").append(array[index]).append("\"");
            if (index < ((String[]) array).length-1) {
                builder.append(",");
            }
        }
        builder.append("]");
    }
}
