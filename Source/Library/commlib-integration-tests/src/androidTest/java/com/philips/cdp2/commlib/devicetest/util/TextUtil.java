/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;

public final class TextUtil {

    public static CharSequence getHTMLText(String string) {
        CharSequence result;
        string = string.replace("\n", "<br>");
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(string,
                    Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(string);
        }
        return result;
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
}
