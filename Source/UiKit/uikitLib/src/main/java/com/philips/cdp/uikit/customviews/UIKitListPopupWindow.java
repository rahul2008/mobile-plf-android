package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListPopupWindow;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.utils.FontIconUtils;

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

    public enum Type
    {
        TOPLEFT, TOPRIGHT , LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT
    }
    public UIKitListPopupWindow(final Context context,final View view, final Type type) {
        super(context);
        mcontext = context;
        setAnchorView(view);
        mtype = type;
        mview = view;

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
        setWidth(mcontext.getResources().getDimensionPixelSize(R.dimen.popup_menu_width));
        setOffsetValue(mtype);
        super.show();
        if (isShowing()) {

            TypedArray a = mcontext.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor});
            TypedArray arr = mcontext.getTheme().obtainStyledAttributes(new int[]{R.attr.darkerColor});
            verylightthemecolor = a.getColor(0, mcontext.getResources().getColor(R.color.uikit_philips_very_light_blue));
            darkerColor = arr.getColor(0, mcontext.getResources().getColor(R.color.uikit_philips_very_light_blue));
            Drawable drawable = ResourcesCompat.getDrawable(mcontext.getResources(),R.drawable.pop_over_menu_divider,null);
            ColorFilter verylightcolor = new PorterDuffColorFilter(verylightthemecolor, PorterDuff.Mode.SRC_ATOP);
            drawable.setColorFilter(verylightcolor);
            getListView().setDivider(drawable);
            getListView().setBackgroundColor(Color.WHITE);


            Drawable selector = ResourcesCompat.getDrawable(mcontext.getResources(),R.drawable.uikit_popup_selector,null);
            selector.setColorFilter(verylightcolor);
            getListView().setSelector(selector);



            Drawable fontdrawable =FontIconUtils.getInfo(mcontext.getApplicationContext(), FontIconUtils.ICONS.HEART, 22, darkerColor,
                    false);
            ColorFilter darkcolorfilter = new PorterDuffColorFilter(darkerColor, PorterDuff.Mode.SRC_ATOP);
            fontdrawable.setColorFilter(darkcolorfilter);

            //getListView().findViewById(R.id.listimageview).setBackgroundTintList(darkerColor);



                    //getListView().setDivider(mcontext.getResources().getDrawable(R.drawable.pop_over_menu_divider));

        }
    }

    @Override
    public void setWidth(final int width) {
        super.setWidth(width);
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
