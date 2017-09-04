/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;


public class DialogLayout extends ConstraintLayout {

    private int layoutMargins;

    public DialogLayout(Context context) {
        super(context);
    }

    public DialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutMargins = (getResources().getDimensionPixelSize(R.dimen.uid_dialog_margins)*2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DialogScrollView scrollView = (DialogScrollView) this.getChildAt(2);
        int siblingHeight = getSiblingHeight();
        int maxHeight = getContentAreaHeight() - layoutMargins - siblingHeight;
        if(scrollView.getMeasuredHeight() > maxHeight){
            scrollView.setViewHeight(maxHeight);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int getSiblingHeight() {
        return this.getChildAt(0).getMeasuredHeight()       //Measured height of view with id: uid_dialog_header in layout: uid_dialog.
                + this.getChildAt(1).getMeasuredHeight()    //Measured height of view with id: uid_dialog_top_divider in layout: uid_dialog.
                + this.getChildAt(3).getMeasuredHeight()    //Measured height of view with id: uid_dialog_bottom_divider in layout: uid_dialog.
                + this.getChildAt(4).getMeasuredHeight();   //Measured height of view with id: uid_dialog_control_area in layout: uid_dialog.
    }

    private int getContentAreaHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        this.getDisplay().getMetrics(metrics);
        int totalHeight = getStatusBarHeight() + getActionBarHeight();
        return metrics.heightPixels - totalHeight;
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getContext().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
