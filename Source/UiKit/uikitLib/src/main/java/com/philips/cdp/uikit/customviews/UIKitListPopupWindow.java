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
import com.philips.cdp.uikit.utils.CustomListViewAdapter;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UIKitListPopupWindow extends ListPopupWindow {

    private Context mcontext;
    private Type mtype;
    private View mview;
    private int verylightthemecolor;
    private int darkerColor;
    private int mwidth = 0;
    private Drawable drawable;
    private ColorFilter verylightcolor;
    private ColorFilterStateListDrawable mselector;

    public enum Type
    {
        TOPLEFT, TOPRIGHT , LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT
    }
    public UIKitListPopupWindow(final Context context,final View view, final Type type, List<RowItem> rowItems) {
        super(context);
        mcontext = context;
        setAnchorView(view);
        mtype = type;
        mview = view;

        CustomListViewAdapter adapter = new CustomListViewAdapter(mcontext, R.layout.simple_list_image_text, rowItems);
        setThemeDrawable();
        setAdapter(adapter);
        setThemeSelector();
    }

    public void setOffsetValue(Type type) {

        switch (type) {
            case TOPLEFT:
                setVerticalOffset(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case TOPRIGHT:
                setHorizontalOffset(-(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mview.getWidth()));
                setVerticalOffset(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case LEFT:
                setHorizontalOffset(mview.getWidth() + (mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin)));
                setVerticalOffset(-mview.getHeight());
                break;
            case RIGHT:
                setHorizontalOffset(-((mcontext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) +
                                       mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin))));
                setVerticalOffset(-mview.getHeight());
                break;
            case BOTTOMLEFT:
                setVerticalOffset(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            case BOTTOMRIGHT:
                setHorizontalOffset(-(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width) - mview.getWidth()));
                setVerticalOffset(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
                break;
            default:
                setVerticalOffset(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_top_margin));
        }

    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;
    }

    public UIKitListPopupWindow(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mcontext = context;
    }


    @Override
    public void show() {
        setBackgroundDrawable(new ColorDrawable(0));
        if(mwidth == 0) {
            setWidth((int) mcontext.getResources().getDimension(R.dimen.popup_menu_width));
        } else {
            setWidth(mwidth);
        }
        setOffsetValue(mtype);
        super.show();
        if (isShowing()) {
            getListView().setDivider(getThemeDrawabe());
            getListView().setBackgroundColor(Color.WHITE);
            getListView().setSelector(mselector);
        }
    }

    private void setThemeDrawable() {

        TypedArray a = mcontext.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor,R.attr.darkerColor});
        ContextCompat.getColor(mcontext,R.color.uikit_philips_very_light_blue);
        verylightthemecolor = a.getColor(0, ContextCompat.getColor(mcontext, R.color.uikit_philips_very_light_blue));
        darkerColor = a.getColor(1,ContextCompat.getColor(mcontext, R.color.uikit_philips_very_light_blue));
        drawable = ResourcesCompat.getDrawable(mcontext.getResources(),R.drawable.pop_over_menu_divider,null);
        verylightcolor = new PorterDuffColorFilter(verylightthemecolor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(verylightcolor);
        a.recycle();
    }

    private void setThemeSelector() {
        Drawable selector = new ColorDrawable(verylightthemecolor);
        mselector = new ColorFilterStateListDrawable();
        mselector.addState(new int[]{android.R.attr.state_pressed}, selector.mutate(), verylightcolor);
    }


    private Drawable getThemeDrawabe() {
        return  drawable;
    }

    @Override
    public void setWidth(final int width) {
        super.setWidth(width);
        mwidth = width;
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
