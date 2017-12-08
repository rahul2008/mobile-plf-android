package com.philips.cdp.uikit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BadgeCountView extends Button {

    private static final int BADGE_COLOR = Color.RED;
    private static final ColorDrawable BACKGROUND_COLOR = new ColorDrawable(BADGE_COLOR);
    private static final int TEXT_COLOR = Color.WHITE;

    public BadgeCountView(final Context context) {
        super(context);
    }

    public BadgeCountView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeCountView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public final void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(BACKGROUND_COLOR);
    }

    @Override
    public final void setBackgroundColor(int backgroundColor) {
        super.setBackgroundColor(BADGE_COLOR);
    }

    @Override
    public final void setTextColor(int color) {
        super.setTextColor(TEXT_COLOR);
    }
}
