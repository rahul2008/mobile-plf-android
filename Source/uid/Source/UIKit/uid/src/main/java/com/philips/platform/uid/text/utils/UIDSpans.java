/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.text.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class UIDSpans {
    private static final String FONT_PATH_CENTRALESANS_BOLD = "fonts/centralesansbold.ttf";

    /**
     * Helper function for setting bold span on given substring.
     *
     * @param ignoreCase for case senstive in comparison.
     * @param context    for accessing assets
     * @param string     which acts primary string
     * @param subString  which needs to be looked in primary string.
     * @return SpannableString if given string contains the substring. Else returns the same string.
     */
    public static CharSequence boldSubString(boolean ignoreCase, Context context, final CharSequence string, final CharSequence subString) {
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(subString)) {
            return string;
        }

        int subStringIndex = UIDStringUtils.indexOfSubString(ignoreCase, string, subString);
        if (subStringIndex >= 0) {
            CalligraphyTypefaceSpan span = new CalligraphyTypefaceSpan(getTypefaceFromPath(context, FONT_PATH_CENTRALESANS_BOLD));
            SpannableString result = SpannableString.valueOf(string);
            result.setSpan(span, subStringIndex, subStringIndex + subString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return result;
        }
        return string;
    }

    private static Typeface getTypefaceFromPath(Context context, String path) {
        return TypefaceUtils.load(context.getAssets(), path);
    }
}