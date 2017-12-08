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

    public RadioGroup(final Context context) {
        this(context, null);
    }

    public RadioGroup(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        topMargin = context.getResources().getDimensionPixelSize(R.dimen.uid_radiobutton_margin_top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=0; i<childCount; i++){
            RadioButton child = (RadioButton)getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
            if(child.getLineCount() > 1){
                int childTopMargin = layoutParams.topMargin;
                if(childTopMargin == 0){
                    layoutParams.topMargin = topMargin;
                    child.setLayoutParams(layoutParams);
                }
            } else {
                layoutParams.topMargin = 0;
                child.setLayoutParams(layoutParams);
            }
        }
    }
}