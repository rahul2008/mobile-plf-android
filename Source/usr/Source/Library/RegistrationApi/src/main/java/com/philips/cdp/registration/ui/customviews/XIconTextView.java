/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;

public class XIconTextView extends androidx.appcompat.widget.AppCompatTextView {
    final String iconFontAssetName = "PUIIcon.ttf";

    public XIconTextView(Context context) {
        super(context);
        applyAttributes(this, iconFontAssetName);
    }

    public XIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(this, iconFontAssetName);
    }

    private void applyAttributes(TextView view, String fontAssetName) {
        FontLoader.getInstance().setTypeface(view, fontAssetName);
    }

}
