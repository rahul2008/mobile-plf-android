package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListPopupWindow;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.ColorFilterStateListDrawable;
import com.philips.cdp.uikit.utils.PopupOverAdapter;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UIKitListPopupWindow extends ListPopupWindow {

    private Context mContext;
    private UIKIT_Type mUIKITType;
    private View mView;
    private int mVerylightthemecolor;
    private int mDarkerColor;
    private int mWidth = 0;
    private Drawable mDrawable;
    private ColorFilter mVerylightcolor;
    private ColorFilterStateListDrawable mSelector;

    public UIKitListPopupWindow(final Context context, final View view, final UIKIT_Type UIKITType, List<RowItem> rowItems) {
        super(context);
        mContext = context;
        setAnchorView(view);
        mUIKITType = UIKITType;
        mView = view;

        PopupOverAdapter adapter = new PopupOverAdapter(mContext, R.layout.uikit_simple_list_image_text, rowItems);
        setThemeDrawable();
        setAdapter(adapter);
        setThemeSelector();
    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public void setOffsetValue(UIKIT_Type UIKITType) {

        switch (UIKITType) {
            case UIKIT_TOPLEFT:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case UIKIT_TOPRIGHT:
                setHorizontalOffset(-(mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mView.getWidth()));
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case UIKIT_LEFT:
                setHorizontalOffset(mView.getWidth() + (mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin)));
                setVerticalOffset(-mView.getHeight());
                break;
            case UIKIT_RIGHT:
                setHorizontalOffset(-((mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) +
                        mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin))));
                setVerticalOffset(-mView.getHeight());
                break;
            case UIKIT_BOTTOMLEFT:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case UIKIT_BOTTOMRIGHT:
                setHorizontalOffset(-(mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mView.getWidth()));
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            default:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
        }

    }

    @Override
    public void show() {
        //setBackgroundDrawable(new ColorDrawable(0));
        setBackgroundDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.popovermenu, null));
        if (mWidth == 0) {
            setWidth((int) mContext.getResources().getDimension(R.dimen.popup_menu_width));
        } else {
            setWidth(mWidth);
        }
        setOffsetValue(mUIKITType);
        super.show();
        if (isShowing()) {
            getListView().setDivider(mDrawable);
            getListView().setBackgroundColor(Color.WHITE);
            getListView().setSelector(mSelector);
            getListView().setVerticalScrollBarEnabled(false);
        }
    }

    private void setThemeDrawable() {

        TypedArray a = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_veryLightColor, R.attr.uikit_darkerColor});
        ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue);
        mVerylightthemecolor = a.getColor(0, ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue));
        mDarkerColor = a.getColor(1, ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue));
        mDrawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.pop_over_menu_divider, null);
        mVerylightcolor = new PorterDuffColorFilter(mVerylightthemecolor, PorterDuff.Mode.SRC_ATOP);
        mDrawable.setColorFilter(mVerylightcolor);
        a.recycle();
    }

    private void setThemeSelector() {

        int fifteenopa_mVerylightthemecolor = Color.argb(38, Color.red(mVerylightthemecolor), Color.green(mVerylightthemecolor), Color.blue(mVerylightthemecolor));
        ColorFilter FifteenVerylightcolor = new PorterDuffColorFilter(fifteenopa_mVerylightthemecolor, PorterDuff.Mode.SRC_ATOP);
        Drawable selector = new ColorDrawable(fifteenopa_mVerylightthemecolor);
        mSelector = new ColorFilterStateListDrawable();
        mSelector.addState(new int[]{android.R.attr.state_pressed}, selector.mutate(), FifteenVerylightcolor);
    }

    @Override
    public void setWidth(final int width) {
        super.setWidth(width);
        mWidth = width;
    }

    @Override
    public void setHeight(final int height) {
        super.setHeight(height);
    }

    @Override
    public void setModal(final boolean modal) {
        super.setModal(true);
    }

    public enum UIKIT_Type {
        UIKIT_TOPLEFT, UIKIT_TOPRIGHT, UIKIT_LEFT, UIKIT_RIGHT, UIKIT_BOTTOMLEFT, UIKIT_BOTTOMRIGHT
    }
}
