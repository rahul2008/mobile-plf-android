package com.philips.dhpclient.util;

import java.util.Map;

public abstract class MapUtils {
    private MapUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T extract(Map<String, Object> map, String path) {
        Object value = map;

        try {
            for (String p : path.split("\\."))
                value = ((Map<String, Object>) value).get(p);

            return (T) value;
        } catch (Exception e) {
            return null;
        }
    }

}
