/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * UID UI Picker
 * <p>
 * <P>UID UI Picker is custom ListPopupWindow as per DLS design.
 * <p>
 * <P> For usage of UI Picker please refer to the DLS Catalog app or the confluence page below
 * <p>
 *
 * @see <a href="https://confluence.atlas.philips.com/display/MU/How+to+integrate+Android+UI+Picker">https://confluence.atlas.philips.com/display/MU/How+to+integrate+Android+UI+Picker</a>
 */


public class UIPicker extends ListPopupWindow{

    private static final String TAG = "UIPicker";
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetEpicenterBoundsMethod;

    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod(
                    "setClipToScreenEnabled", boolean.class);
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod(
                    "getMaxAvailableHeight", View.class, int.class, boolean.class);
            sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod(
                    "setEpicenterBounds", Rect.class);
        } catch (NoSuchMethodException e) {
            UIDLog.e(TAG, e.getMessage());
        }
    }

    private Context context;
    private ListAdapter adapter;
    private boolean shouldSetGravity = true;
    private boolean shouldSetHeight = true;
    private boolean shouldSetWidth = true;
    private boolean shouldNotOverlapAnchorView = false;
    private int adapterCount;
    private boolean isDistanceToTopLarger;

    public UIPicker(@NonNull Context context) {
        this(context, null);
        this.context = context;
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
        if(adapter != null){
            this.adapter = adapter;
            adapterCount = adapter.getCount();
        }
    }

    @Override
    public void setDropDownGravity(int dropDownGravity) {
        super.setDropDownGravity(dropDownGravity);
        shouldSetGravity = false;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        shouldSetWidth = false;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        shouldSetHeight = false;
    }

    /**
     * By default ListPopupWindow will overlap anchor view
     * This API will allow you to display ListPopupWindow without overlapping anchor view.
     *
     * @param shouldNotOverlapAnchorView Boolean to set if the ListPopupWindow should not overlap anchor view
     *                                   @since 3.0.0
     */
    public void setShouldNotOverlapAnchorView(boolean shouldNotOverlapAnchorView){
        this.shouldNotOverlapAnchorView = shouldNotOverlapAnchorView;
    }

    @Override
    public void show() {

        View anchorView = getAnchorView();
        int anchorHeight = 0;
        if(anchorView != null){
            anchorHeight = anchorView.getHeight();
        }
        final int LIST_EXPAND_MAX = 1;

        //By default setting Dropdown Gravity to End, if user has set some dropdown gravity then we will not set
        if(shouldSetGravity){
            setDropDownGravity(Gravity.END);
        }

        //By default setting Dropdown width according to DLS incremental width design spec, if user has set some width then we will not set
        if(shouldSetWidth){
            setContentWidth(measureContentWidth(adapter));
        }

        //By default setting Dropdown height according to DLS incremental height design spec, if user has set some height then we will not set
        if(shouldSetHeight){
            setContentHeight(anchorView, anchorHeight);
        }

        if(!shouldNotOverlapAnchorView){
            setVerticalOffset(- anchorHeight);
        }

        setListItemExpandMax(this, LIST_EXPAND_MAX);

        super.show();
    }

    //Reflection hack to fix Pop Window height change on scroll
    private void setListItemExpandMax(ListPopupWindow listPopupWindow, int max) {
        try {
            Method method = ListPopupWindow.class.getDeclaredMethod("setListItemExpandMax", Integer.TYPE);
            method.setAccessible(true);
            method.invoke(listPopupWindow, max);
        } catch (NoSuchMethodException| InvocationTargetException|  IllegalAccessException e) {
            UIDLog.e(TAG, e.getMessage());
        }
    }

    //Getting max available height of device screen to display ListPopupWindow
    private int getMaxAvailableHeight(@NonNull View anchor, int yOffset) {

        final Rect displayFrame = new Rect();
        anchor.getWindowVisibleDisplayFrame(displayFrame);

        final int[] anchorPos = new int[2];
        anchor.getLocationOnScreen(anchorPos);

        final int bottomEdge = displayFrame.bottom;

        // anchorPos[1] is distance from anchor to top of screen
        final int distanceToBottom = bottomEdge - anchorPos[1] - yOffset;

        final int distanceToTop = anchorPos[1] - displayFrame.top + yOffset;

        if(distanceToTop > distanceToBottom){
            isDistanceToTopLarger = true;
        }
        int returnedHeight = Math.max(distanceToBottom, distanceToTop);
        final Rect mTempRect = new Rect();
        if (getBackground() != null) {
            getBackground().getPadding(mTempRect);
            returnedHeight -= mTempRect.top + mTempRect.bottom;
        }

        return returnedHeight;
    }

    private void setContentHeight(View anchorView, int anchorHeight){
        int maxHeight = getMaxAvailableHeight(anchorView, anchorHeight);
        //incremental height calculation
        int contentHeight = adapterCount * context.getResources().getDimensionPixelSize(R.dimen.uid_uipicker_item_height);
        if(contentHeight < maxHeight){
            setHeight(contentHeight);
        } else {
            int temp = maxHeight % context.getResources().getDimensionPixelSize(R.dimen.uid_uipicker_item_height);
            if(temp != 0){
                maxHeight = maxHeight - temp;
            }

            //Fix to avoid ListPopupWindow to display outside the display bounds
            if(shouldNotOverlapAnchorView || isDistanceToTopLarger)
                setHeight(maxHeight - anchorHeight);
            else
                setHeight(maxHeight);
        }
    }


    private int measureContentWidth(ListAdapter adapter) {

        //Getting max available width of device screen to display ListPopupWindow
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;

        ViewGroup mMeasureParent = null;
        float minWidth = context.getResources().getDimensionPixelSize(R.dimen.uid_uipicker_incremental_width);
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();

        //Getting max width of content item
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(context);
            }

            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }

        //incremental width calculation
        if(maxWidth > minWidth && maxWidth < 2 * minWidth){
            return Math.round(2 * minWidth);
        } else if(maxWidth > 2 * minWidth && maxWidth < 3 * minWidth){
            return Math.round(3 * minWidth);
        } else if(maxWidth > 3 * minWidth && maxWidth < 6 * minWidth){
            return Math.round(6 * minWidth);
        } else if(maxWidth > 6 * minWidth && maxWidth < 7 * minWidth){
            return Math.round(7 * minWidth);
        } else if(maxWidth < widthPixels){
            return maxWidth;
        } else{
            return widthPixels - context.getResources().getDimensionPixelSize(R.dimen.uid_uipicker_less_left_right_padding);
        }
    }
}