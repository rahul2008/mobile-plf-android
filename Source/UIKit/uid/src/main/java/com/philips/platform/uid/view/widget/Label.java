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
import android.util.TypedValue;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDLocaleHelper;

public class Label extends AppCompatTextView {
    public Label(final Context context) {
        this(context, null);
    }

    public Label(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public Label(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }


    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UIDLabel, defStyleAttr, R.style.UIDLabel);
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(R.styleable.UIDLabel);

        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);

        setLabelTextSize(context, attrsArray);
        attrsArray.recycle();
        themeArray.recycle();
    }

    private void setLabelTextSize(Context context, final TypedArray attrsArray) {
        int textSize = context.getResources().getDimensionPixelSize(R.dimen.uid_label_text_size);
        textSize = attrsArray.getDimensionPixelSize(R.styleable.UIDLabel_uidLabelTextSize, textSize);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}
