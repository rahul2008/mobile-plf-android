/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;


import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

public class DialogScrollView extends NestedScrollView{

    private int viewHeight;

    public DialogScrollView(Context context) {
        super(context);
    }

    public DialogScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(viewHeight!= 0){
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(viewHeight,MeasureSpec.AT_MOST));
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
