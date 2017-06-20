/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.philips.platform.uid.R;

public class RadioGroup extends android.widget.RadioGroup {

    private int topMargin;
    private LinearLayout.LayoutParams layoutParams;

    public RadioGroup(final Context context) {
        this(context, null);
    }

    public RadioGroup(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.uid_radiobutton_margin_top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=1; i<childCount; i++){
            RadioButton child = (RadioButton)getChildAt(i);
            if(child.getLineCount() > 1){
                layoutParams.setMargins(0, topMargin, 0, 0);
                child.setLayoutParams(layoutParams);
            } else {
                layoutParams.setMargins(0, 0, 0, 0);
                child.setLayoutParams(layoutParams);
            }
        }
    }
}