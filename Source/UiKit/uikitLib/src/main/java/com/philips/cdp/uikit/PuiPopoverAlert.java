package com.philips.cdp.uikit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiPopoverAlert extends RelativeLayout {

    public PuiPopoverAlert(final Context context) {
        super(context);
    }

    public PuiPopoverAlert(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uikit_popover_alert, this, true);

        setBackground(getResources().getDrawable(R.drawable.uikit_popover_alert_background));
    }

    public PuiPopoverAlert(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
