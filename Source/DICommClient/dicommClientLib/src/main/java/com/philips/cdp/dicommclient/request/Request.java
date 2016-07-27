/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Map;
import java.util.Set;

public abstract class Request {

    protected final Map<String, Object> mDataMap;
    protected final ResponseHandler mResponseHandler;

    public Request(Map<String, Object> dataMap, ResponseHandler responseHandler) {
        this.mDataMap = dataMap;
        this.mResponseHandler = responseHandler;
    }

    public abstract Response execute();

    @SuppressWarnings("unchecked")
    protected static String convertKeyValuesToJson(Map<String, Object> dataMap) {
        if (dataMap == null || dataMap.size() <= 0) return "{}";

        StringBuilder builder = new StringBuilder("{");
        Set<String> keySet = dataMap.keySet();
        int index = 1;
        for (String key : keySet) {
            Object value = dataMap.get(key);

            // TODO DICOMM REFACTOR add support for all DIComm datatypes
            if (value instanceof String) {
                builder.append("\"").append(key).append("\":").append(wrapIfNotJsonObject((String) value));
            } else if (value instanceof Integer) {
                builder.append("\"").append(key).append("\":").append(value);
            } else if (value instanceof Boolean) {
                builder.append("\"").append(key).append("\":").append(value);
            } else if (value instanceof String[]) {
                builder.append("\"").append(key).append("\":");
                appendStringArray(builder, (String[]) value);
            } else if (value instanceof Map<?, ?>) {
                builder.append("\"").append(key).append("\":").append(convertKeyValuesToJson((Map<String, Object>) value));
            } else {
                builder.append("\"").append(key).append("\":\"").append(value).append("\"");
            }

            if (index < keySet.size()) {
                builder.append(",");
            }
            index++;
        }
        builder.append("}");
        return builder.toString();
    }

    private static void appendStringArray(StringBuilder builder, String[] array) {
        builder.append("[");
        for (int index = 0; index < array.length; index++) {
            builder.append(wrapIfNotJsonObject(array[index]));
            if (index < ((String[]) array).length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
    }

    private static String wrapIfNotJsonObject(String value) {
        if (value == null) {
            return "\"\"";
        }

        if (value.startsWith("{")) {
            return value;
        }

        return String.format("\"%s\"", value);
    }
}
