package com.philips.platform.csw.utils;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;

import com.philips.platform.csw.permission.uielement.LinkSpan;
import com.philips.platform.csw.permission.uielement.LinkSpanClickListener;

public class SpannableHelper {
    @NonNull
    public static Spannable getSpannable(String text, String startCharacter, String endCharacter, LinkSpanClickListener spanClickListener) {
        int openingBracketIndex = text.indexOf(startCharacter);
        int closingBracketIndex = text.indexOf(endCharacter);
        if (openingBracketIndex == -1 || closingBracketIndex == -1) {
            openingBracketIndex = 0;
            closingBracketIndex = text.length();
        }
        text = text.replace(startCharacter, "");
        text = text.replace(endCharacter, "");

        Spannable privacyNotice = new SpannableString(text);
        privacyNotice.setSpan(new LinkSpan(spanClickListener), openingBracketIndex, closingBracketIndex - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return privacyNotice;
    }
}
