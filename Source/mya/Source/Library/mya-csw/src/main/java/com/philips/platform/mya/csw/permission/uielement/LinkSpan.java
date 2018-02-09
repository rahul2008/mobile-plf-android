/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.csw.permission.uielement;

import android.text.style.ClickableSpan;
import android.view.View;

public class LinkSpan extends ClickableSpan {
    private LinkSpanClickListener privacyNoticeClickListener;

    public LinkSpan(LinkSpanClickListener privacyNoticeClickListener) {
        this.privacyNoticeClickListener = privacyNoticeClickListener;
    }

    @Override
    public void onClick(View view) {
        privacyNoticeClickListener.onClick();
    }
}

