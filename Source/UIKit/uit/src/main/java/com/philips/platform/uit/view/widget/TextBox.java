/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class TextBox extends AppCompatEditText {
    public TextBox(final Context context) {
        super(context, null);
    }

    public TextBox(final Context context, final AttributeSet attrs) {
        super(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public TextBox(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}