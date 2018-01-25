/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission.adapter;

import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.permission.PrivacyNoticeClickListener;
import com.philips.platform.uid.view.widget.Label;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.view.View;

class PermissionHeaderViewHolder extends BasePermissionViewHolder {
    private static final String TAG = "PrivacyNoticeView";
    private Label headerTextView;

    PermissionHeaderViewHolder(View itemView, int parentWidth) {
        super(itemView, parentWidth);
        this.headerTextView = itemView.findViewById(R.id.csw_privacy_settings_desc);
    }

    @Override
    public void onViewRecycled() {
        // NOP
    }

    public void setPrivacyURL(final String url, final PrivacyNoticeClickListener privacyNoticeClickListener) {
        Context context = itemView.getContext();
        String headerText = context.getString(R.string.csw_privacy_settings_desc);

        int openingBracketIndex = headerText.indexOf('{');
        int closingBracketIndex = headerText.indexOf('}');
        headerText = headerText.replace("{", "");
        headerText = headerText.replace("}", "");

        Spannable wordtoSpan = new SpannableString(headerText);
        wordtoSpan.setSpan(new URLSpan(""), openingBracketIndex, closingBracketIndex - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.headerTextView.setText(wordtoSpan);
        this.headerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacyNoticeClickListener.onPrivacyNoticeClicked(url);
            }
        });
    }
}
