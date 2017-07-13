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
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UIPicker extends ListPopupWindow{

    private static final String TAG = "UIPicker";
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetEpicenterBoundsMethod;

    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod(
                    "setClipToScreenEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        try {
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod(
                    "getMaxAvailableHeight", View.class, int.class, boolean.class);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method getMaxAvailableHeight(View, int, boolean)"
                    + " on PopupWindow. Oh well.");
        }
        try {
            sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod(
                    "setEpicenterBounds", Rect.class);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
        }
    }

    private Activity activity;
    private ListAdapter adapter;
    public static boolean isBelowAnchorView;
    private static final int LIST_EXPAND_MAX = 1;

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

    @Override
    public void setAdapter(@Nullable ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    public void show() {
        setListItemExpandMax(this, LIST_EXPAND_MAX);
        super.show();
    }

    private void setListItemExpandMax(ListPopupWindow listPopupWindow, int max) {
        if(!isBelowAnchorView){
            setVerticalOffset(-getAnchorView().getHeight());
        }
        try {
            Method method = ListPopupWindow.class.getDeclaredMethod("setListItemExpandMax", Integer.TYPE);
            method.setAccessible(true);
            method.invoke(listPopupWindow, max);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /*@Override
    public void setAnchorView(@Nullable View anchor) {
        super.setAnchorView(anchor);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        setHeight(heightPixels - anchor.getBottom());
    }*/

    /*@Override
    public void show() {
        setUIDUIPickerHeight();
        if(!isBelowAnchorView){
            setVerticalOffset(-getAnchorView().getHeight());
        }
        //setVerticalOffset(getAnchorView().getHeight());
        super.show();
    }*/

    /*public void shouldShowBelowAnchorView(boolean isBelowAnchorView){
        this.isBelowAnchorView = isBelowAnchorView;
    }*/

    /*private void setUIDUIPickerHeight(){
        if(getHeight() == ViewGroup.LayoutParams.WRAP_CONTENT){
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int heightPixels = metrics.heightPixels;
            //int widthPixels = metrics.widthPixels;

            int[] coordinatesArray = new int[2];
            getAnchorView().getLocationInWindow(coordinatesArray);
            int anchorViewHeight = getAnchorView().getHeight();
            //setHeight(800);
            setHeight(heightPixels - (coordinatesArray[1] + anchorViewHeight));
            //setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }*/
}
