package com.philips.cdp.uikit.com.philips.cdp.uikit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerUtil {

    private Context context;
    private ListView drawerListView;
    private View footerView;

    public HamburgerUtil(Context context, ListView drawerListView) {
        this.context = context;
        this.drawerListView = drawerListView;
        drawerListView.setFooterDividersEnabled(true);
        drawerListView.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    public void moveDrawerToTop(View drawer) {
        Activity activity = (Activity) context;
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.frame_container);
        container.addView(child, 0);
        decor.addView(drawer);
    }

    public int getStatusBarHeight() {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public void updateSmartFooter() {
        drawerListView.post(new Runnable() {
            @Override
            public void run() {
                int heightPixels = getDeviceHeightPixels();
                int adaptorTotalHeight = getAdaptorTotalHeight();
                validateLogoView(heightPixels, adaptorTotalHeight);
            }
        });
    }

    private void validateLogoView(final int heightPixels, final int adaptorTotalHeight) {
        removeFooterViewIfExists();
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = vi.inflate(R.layout.uikit_footer_view, null, false);
        drawerListView.addFooterView(footerView, null, false);
        VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) footerView.findViewById(R.id.hamburger_logo);
        setLogoCenterWithMargins(heightPixels, adaptorTotalHeight, vectorDrawableImageView);
        setVectorImage(vectorDrawableImageView);
    }

    private void removeFooterViewIfExists() {
        if (footerView != null)
            drawerListView.removeFooterView(footerView);
    }

    private void setLogoCenterWithMargins(int heightPixels, int adaptorTotalHeight, VectorDrawableImageView vectorDrawableImageView) {
        Resources resources = context.getResources();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vectorDrawableImageView.getLayoutParams();
        int staticHeight = heightPixels - getLogoHeight();
        int definedTopMargin = (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_top_margin);
        if (adaptorTotalHeight > staticHeight) {
            lp.topMargin = definedTopMargin;
        } else if (staticHeight - adaptorTotalHeight - getStatusBarHeight() < definedTopMargin)
            lp.topMargin = definedTopMargin;
        else
            lp.topMargin = (staticHeight - adaptorTotalHeight - getStatusBarHeight());
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        vectorDrawableImageView.setLayoutParams(lp);
    }

    private int getLogoHeight() {
        Resources resources = context.getResources();
        int logoHeight = (int) (resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin) + resources.getDimension(R.dimen.uikit_hamburger_logo_height));
        return logoHeight;
    }

    private int getDeviceHeightPixels() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getAdaptorTotalHeight() {
        if (drawerListView != null && drawerListView.getAdapter().getCount() != 0) {
            double listViewItemHeight = context.getResources().getDimension(R.dimen.uikit_hamburger_list_item_height);
            return (int) (drawerListView.getAdapter().getCount() * listViewItemHeight);
        }
        return 0;
    }

    public void setVectorImage(final VectorDrawableImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(context, resID));
    }

}
