/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class UIPicker extends ListPopupWindow{

    private Activity activity;
    public static boolean isBelowAnchorView;

    public UIPicker(@NonNull Context context) {
        this(context, null, android.support.v7.appcompat.R.attr.listPopupWindowStyle);
        activity = (Activity) context;
    }

    public UIPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.listPopupWindowStyle);
    }

    public UIPicker(@NonNull Context context, @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public UIPicker(@NonNull Context context, @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*@Override
    public void setHeight(int height) {
        super.setHeight(height);
    }*/

    /*@Override
    public void setAnchorView(@Nullable View anchor) {
        super.setAnchorView(anchor);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        setHeight(heightPixels - anchor.getBottom());
    }*/

    @Override
    public void show() {
        setUIDUIPickerHeight();
        if(!isBelowAnchorView){
            setVerticalOffset(-getAnchorView().getHeight());
        }
        super.show();
    }

    public void shouldShowBelowAnchorView(boolean isBelowAnchorView){
        this.isBelowAnchorView = isBelowAnchorView;
    }

    private void setUIDUIPickerHeight(){
        if(getHeight() == ViewGroup.LayoutParams.WRAP_CONTENT){
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int heightPixels = metrics.heightPixels;
            //int widthPixels = metrics.widthPixels;

            int[] coordinatesArray = new int[2];
            getAnchorView().getLocationInWindow(coordinatesArray);
            int anchorViewHeight = getAnchorView().getHeight();

            setHeight(heightPixels - (coordinatesArray[1] + anchorViewHeight));
            //setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
