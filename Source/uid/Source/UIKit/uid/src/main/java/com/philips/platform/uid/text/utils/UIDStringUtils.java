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
     * @param str        Primary charsequence in which substring need to searched.
     * @param subString  substring to be searched.
     * @return 0 if main string contains substring else -1
     */
    public static int indexOfSubString(boolean ignoreCase, final CharSequence str, final CharSequence subString) {
        if (str == null || subString == null) {
            return -1;
        }
        final int subStringLen = subString.length();
        final int max = str.length() - subStringLen;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(ignoreCase, str, i, subString, 0, subStringLen)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper function to find matching region for two Charsequence.
     *
     * @param ignoreCase   pass true for case insensitive comparison
     * @param str          primary string for searching
     * @param strOffset    offset in primary string
     * @param subStr       sub string
     * @param subStrOffset offset in sub string
     * @param length       lenght of the lookup
     * @return the comparsion result if the string contains substring of given legth
     */
    public static boolean regionMatches(final boolean ignoreCase, final CharSequence str, final int strOffset,
                                        final CharSequence subStr, final int subStrOffset, final int length) {
        if (str == null || subStr == null) {
            return false;
        }

        if (str instanceof String && subStr instanceof String) {
            return ((String) str).regionMatches(ignoreCase, strOffset, (String) subStr, subStrOffset, length);
        }

        //SubString length is more than string
        if (subStr.length() > str.length()) {
            return false;
        }

        //Invalid start point
        if (strOffset < 0 || subStrOffset < 0 || length < 0) {
            return false;
        }

        //Length can't be greater than diff of string length and offset
        if ((str.length() - strOffset) < length
                || (subStr.length() - subStrOffset) < length) {
            return false;
        }

        //Start comparing
        int strIndex = strOffset;
        int subStrIndex = subStrOffset;
        int tmpLenth = length;

        while (tmpLenth-- > 0) {
            char c1 = str.charAt(strIndex++);
            char c2 = subStr.charAt(subStrIndex++);

            if (c1 == c2) {
                continue;
            }

            //Same comparison as java framework
            if (ignoreCase &&
                    (Character.toUpperCase(c1) == Character.toUpperCase(c2)
                            || Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
                continue;
            }
            return false;
        }
        return true;
    }
}