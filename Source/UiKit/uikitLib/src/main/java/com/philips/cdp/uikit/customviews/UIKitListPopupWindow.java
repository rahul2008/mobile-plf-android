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
    private Type mType;
    private View mView;
    private int mVerylightthemecolor;
    private int mDarkerColor;
    private int mWidth = 0;
    private Drawable mDrawable;
    private ColorFilter mVerylightcolor;
    private ColorFilterStateListDrawable mSelector;

    public enum Type
    {
        TOPLEFT, TOPRIGHT , LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT
    }
    public UIKitListPopupWindow(final Context context,final View view, final Type type, List<RowItem> rowItems) {
        super(context);
        mContext = context;
        setAnchorView(view);
        mType = type;
        mView = view;

        PopupOverAdapter adapter = new PopupOverAdapter(mContext, R.layout.simple_list_image_text, rowItems);
        setThemeDrawable();
        setAdapter(adapter);
        setThemeSelector();
    }

    public void setOffsetValue(Type type) {

        switch (type) {
            case TOPLEFT:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case TOPRIGHT:
                setHorizontalOffset(-(mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mView.getWidth()));
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case LEFT:
                setHorizontalOffset(mView.getWidth() + (mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin)));
                setVerticalOffset(-mView.getHeight());
                break;
            case RIGHT:
                setHorizontalOffset(-((mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) +
                                       mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin))));
                setVerticalOffset(-mView.getHeight());
                break;
            case BOTTOMLEFT:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case BOTTOMRIGHT:
                setHorizontalOffset(-(mContext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mView.getWidth()));
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            default:
                setVerticalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
        }

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


    @Override
    public void show() {
        setBackgroundDrawable(new ColorDrawable(0));
        if(mWidth == 0) {
            setWidth((int) mContext.getResources().getDimension(R.dimen.popup_menu_width));
        } else {
            setWidth(mWidth);
        }
        setOffsetValue(mType);
        super.show();
        if (isShowing()) {
            getListView().setDivider(mDrawable);
            getListView().setBackgroundColor(Color.WHITE);
            getListView().setSelector(mSelector);
        }
    }

    private void setThemeDrawable() {

        TypedArray a = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor,R.attr.darkerColor});
        ContextCompat.getColor(mContext,R.color.uikit_philips_very_light_blue);
        mVerylightthemecolor = a.getColor(0, ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue));
        mDarkerColor = a.getColor(1,ContextCompat.getColor(mContext, R.color.uikit_philips_very_light_blue));
        mDrawable = ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.pop_over_menu_divider,null);
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
}
