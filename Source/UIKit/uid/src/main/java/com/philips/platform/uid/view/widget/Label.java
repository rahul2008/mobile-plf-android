/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDLocaleHelper;

public class Label extends AppCompatTextView {
    public Label(final Context context) {
        this(context, null);
    }

    public Label(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidLabelStyle);
    }

    public Label(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UIDLabel, defStyleAttr, R.style.UIDLabel);
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        attrsArray.recycle();
    }
}