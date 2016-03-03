/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;

/**
 *  HamburgerUtil class to handle Hamburger Philips Logo alignments
 */
public class HamburgerUtil {

    private Context context;
    private ListView drawerListView;
    private View footerView;
    private int itemCount;

    public HamburgerUtil(Context context, ListView drawerListView) {
        this.context = context;
        this.drawerListView = drawerListView;
        drawerListView.setFooterDividersEnabled(true);
    }

    /**
     * API to control Philips Logo to remain always at bottom, to be called when adapter is refreshed
     *
     * @param footerImageView - Instance of Footer view
     */
    public void updateSmartFooter(final ImageView footerImageView, int itemCount) {
        this.itemCount = itemCount;
        int heightPixels = getDeviceHeightPixels();
        int adapterTotalHeight = getAdaptorTotalHeight();
        validateLogoView(heightPixels, adapterTotalHeight, footerImageView);
        /*drawerListView.post(new Runnable() {
            @Override
            public void run() {
                int heightPixels = getDeviceHeightPixels();
                int adapterTotalHeight = getAdaptorTotalHeight();
                validateLogoView(heightPixels, adapterTotalHeight, footerImageView);
            }
        });*/
    }

    private void validateLogoView(final int deviceHeight, final int adapterTotalHeight, ImageView footerImageView) {
        int logoDedicatedHeight = getLogoDedicatedHeight();
        int remainingHeight = deviceHeight - logoDedicatedHeight;
        if (adapterTotalHeight <= remainingHeight) {
            removeFooterViewIfExists();
            footerImageView.setVisibility(View.VISIBLE);
        } else {
            footerImageView.setVisibility(View.GONE);
            showListViewFooterView();
        }
    }

    private void showListViewFooterView() {
        removeFooterViewIfExists();
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = vi.inflate(R.layout.uikit_footer_view, null);
        ImageView vectorDrawableImageView = (ImageView) footerView.findViewById(R.id.philips_logo);
        vectorDrawableImageView.setAlpha(229);
        RelativeLayout.LayoutParams lp = getLayoutParams();
        vectorDrawableImageView.setLayoutParams(lp);
        setVectorImage(vectorDrawableImageView);
        drawerListView.addFooterView(footerView, null, false);
    }

    @NonNull
    private RelativeLayout.LayoutParams getLayoutParams() {
        Resources resources = context.getResources();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) resources.getDimension(R.dimen.uikit_hamburger_logo_width), (int) resources.getDimension(R.dimen.uikit_hamburger_logo_height) +(int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin));

     // lp.setMargins(0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_top_margin), 0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin));
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return lp;
    }

    private void removeFooterViewIfExists() {
        if (footerView != null) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) drawerListView.getAdapter();
            HamburgerAdapter hamburgerAdapter = (HamburgerAdapter) headerViewListAdapter.getWrappedAdapter();
            drawerListView.setAdapter(hamburgerAdapter);
            drawerListView.removeFooterView(footerView);
        }
    }
    private int getLogoDedicatedHeight() {
        Resources resources = context.getResources();
        int logoHeight = (int) (resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin) + resources.getDimension(R.dimen.uikit_hamburger_logo_height)
                + resources.getDimension(R.dimen.uikit_hamburger_menu_logo_top_margin));
        return logoHeight;
    }

    private int getDeviceHeightPixels() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getAdaptorTotalHeight() {
        if (itemCount != 0) {
            double listViewItemHeight = context.getResources().getDimension(R.dimen.uikit_hamburger_list_item_height);
            return (int) (itemCount * listViewItemHeight);
        }
        return 0;
    }

    private void setVectorImage(final ImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(context, resID));
    }

}
