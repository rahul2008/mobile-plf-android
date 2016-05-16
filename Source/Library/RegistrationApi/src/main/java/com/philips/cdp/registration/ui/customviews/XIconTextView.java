/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.
Project           : Registration
Description       : Custom view for text loading different type face
----------------------------------------------------------------------------*/

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;

public class XIconTextView extends TextView {
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
