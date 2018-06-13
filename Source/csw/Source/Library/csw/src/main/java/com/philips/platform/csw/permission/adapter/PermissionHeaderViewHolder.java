/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission.adapter;

import com.philips.platform.csw.R;
import com.philips.platform.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.csw.utils.SpannableHelper;
import com.philips.platform.uid.view.widget.Label;
import android.content.Context;
import android.text.Spannable;
import android.view.View;

class PermissionHeaderViewHolder extends BasePermissionViewHolder {
    private Label headerTextView;

    PermissionHeaderViewHolder(View itemView) {
        super(itemView);
        this.headerTextView = itemView.findViewById(R.id.csw_privacy_settings_desc);
    }

    @Override
    public void onViewRecycled() {
    }

    public void setPrivacyURL(final LinkSpanClickListener privacyNoticeClickListener) {
        Context context = itemView.getContext();
        String headerText = context.getString(R.string.csw_privacy_settings_desc);

        Spannable privacyNotice = SpannableHelper.getSpannable(headerText, "{", "}",privacyNoticeClickListener);
        this.headerTextView.setText(privacyNotice);
    }
}
