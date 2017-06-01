/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.text.utils;

public class UIDStringUtils {

    /**
     * Utility function for find the substring in a string.
     *
     * @param ignoreCase pass for case sensitive comparison
     * @param str  Primary string in which substring need to searched.
     * @param subString substring to be searched.
     * @return 0 if main string contains substring else -1
     */
    public static int containsSubString(boolean ignoreCase, final String str, final String subString) {
        if (str == null || subString == null) {
            return -1;
        }
        final int subStringLen = subString.length();
        final int max = str.length() - subStringLen;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(ignoreCase, i, subString, 0, subStringLen)) {
                return i;
            }
        }
        return -1;
    }
}