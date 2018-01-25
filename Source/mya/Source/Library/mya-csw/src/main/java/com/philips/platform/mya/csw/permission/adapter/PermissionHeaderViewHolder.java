/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission.adapter;

import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.view.widget.Label;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.*;
import android.text.style.TextAppearanceSpan;
import android.view.View;

class PermissionHeaderViewHolder extends BasePermissionViewHolder {

    private Label headerTextView;

    PermissionHeaderViewHolder(View itemView, int parentWidth) {
        super(itemView, parentWidth);
        this.headerTextView = itemView.findViewById(R.id.csw_privacy_settings_desc);
    }

    @Override
    public void onViewRecycled() {
        // NOP
    }

    public void setPrivacyURL(Uri privacyUri) {
        Context context = itemView.getContext();
        String headerText = context.getString(R.string.csw_privacy_settings_desc);

        int openingBracketIndex = headerText.indexOf('{');
        int closingBracketIndex = headerText.indexOf('}');

        String startText = headerText.substring(0, openingBracketIndex);
        String privacyText = headerText.substring(openingBracketIndex, closingBracketIndex);
        String endText = headerText.substring(closingBracketIndex, headerText.length());

        int purpleColor = ContextCompat.getColor(context, android.R.color.holo_purple);
        TextAppearanceSpan colorSpan = new TextAppearanceSpan(context,
                Typeface.NORMAL,
                purpleColor);

        SpannableString headerSpannable = new SpannableString(headerText);
        headerSpannable.setSpan(colorSpan, openingBracketIndex, closingBracketIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        Spannable spannableStart = new SpannableString.Factory().newSpannable(startText);
//        Spannable spannablePrivacy = new SpannableString.Factory().newSpannable(privacyText);
//        spannablePrivacy.setSpan(colorSpan, 0, privacyText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Spannable spannableEnd = new SpannableString.Factory().newSpannable(endText);
//
//        CharSequence text = TextUtils.concat(spannableStart, spannablePrivacy, spannableEnd);
        // int leftIndex = headerText.s
        this.headerTextView.setText(headerSpannable);
    }



//    final SpannableString text = new SpannableString("Hello stackOverflow");
//      text.setSpan(new RelativeSizeSpan(1.5f), text.length() - "stackOverflow".length(), text.length(),
//    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//     text.setSpan(new ForegroundColorSpan(Color.RED), 3, text.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//      tv.setText(text);
}
