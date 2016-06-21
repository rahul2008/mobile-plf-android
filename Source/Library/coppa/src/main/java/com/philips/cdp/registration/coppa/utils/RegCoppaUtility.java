/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.utils;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;

/**
 * Created by 310190722 on 25-Mar-16.
 */
public class RegCoppaUtility {
    public static void linkifyTermAndPolicy(TextView privacyTextView, final Activity activity, ClickableSpan privacyClickListener) {

        String privacyPolicyText = privacyTextView.getText().toString();
        String privacyLink = activity.getString(R.string.reg_PrivacyNoticeText);
        privacyPolicyText = String.format(privacyPolicyText, privacyLink);
        privacyTextView.setText(privacyPolicyText);

        String link = activity.getString(R.string.reg_PrivacyNoticeText);
        SpannableString spanableString = new SpannableString(privacyPolicyText);
        int termStartIndex = privacyPolicyText.toLowerCase().indexOf(link.toLowerCase());
        spanableString.setSpan(privacyClickListener, termStartIndex, termStartIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        removeUnderlineFromLink(spanableString);

        privacyTextView.setText(spanableString);
        privacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        privacyTextView.setLinkTextColor(activity.getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        privacyTextView.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));
    }
    private static void removeUnderlineFromLink(SpannableString spanableString) {
        for (ClickableSpan u : spanableString.getSpans(0, spanableString.length(),
                ClickableSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }

        for (URLSpan u : spanableString.getSpans(0, spanableString.length(), URLSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }
    }
}
