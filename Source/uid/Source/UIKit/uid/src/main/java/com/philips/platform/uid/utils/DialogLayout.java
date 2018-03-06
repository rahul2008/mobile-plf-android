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

        int maxHeight = getContentAreaHeight() - layoutMargins - getSiblingHeight();
        if(scrollView.getMeasuredHeight() > maxHeight){
            scrollView.setViewHeight(maxHeight);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        int minHeight = scrollView.getMeasuredHeight() + layoutMargins + getSiblingHeight();
        if(getMeasuredHeight() < minHeight){
            setMinHeight(minHeight);
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
        int totalHeight = UIDUtils.getStatusBarHeight(getContext()) + UIDUtils.getActionBarHeight(getContext());
        return metrics.heightPixels - totalHeight;
    }
}
