package com.philips.platform.csw.utils;

import java.util.List;

public abstract class TaggingUtils {
    public static String join(final List<String> types) {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        for (String type : types) {
            sb.append(delimiter);
            sb.append(type);
            delimiter = "|";
        }
        return sb.toString();
    }
}
