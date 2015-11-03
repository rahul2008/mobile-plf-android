package com.philips.cdp.uikit.CustomButton;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsDrawerLayout extends LinearLayout {

    private final View parentView;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private VectorDrawableImageView footerImage;
    private LinearLayout listViewParentLayout;
    private TextView actionBarTitle;


    public PhilipsDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_hamburger_parent, null, false);
        this.addView(parentView);
        moveDrawerToTop();

    }

    public PhilipsDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_hamburger_parent, null, false);
        this.addView(parentView);
        moveDrawerToTop();

    }

    private void moveDrawerToTop() {
        context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.uikit_hamburger_menu, null);
        Activity activity = (Activity) context;
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.frame_container);
        container.addView(child, 0);
        initializeDrawerViews(drawer);
        decor.addView(drawer);
    }

    private void initializeDrawerViews(DrawerLayout drawer) {
        drawer.findViewById(R.id.hamburger_list).setPadding(0, getStatusBarHeight(), 0, 0);
        listViewParentLayout = (LinearLayout) drawer.findViewById(R.id.list_view_parent);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) drawer.findViewById(R.id.hamburger_list);
        footerImage = (VectorDrawableImageView) drawer.findViewById(R.id.image);
    }

    private int getStatusBarHeight() {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public int getContainerID() {
        return parentView.getId();
    }


    public ListView getDrawerListView() {
        return drawerListView;
    }

    public void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
    }

    public void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(VectorDrawable.create(context, R.drawable.uikit_hamburger_icon));
        actionBarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.hamburger_title);
        updateSmartFooter();
    }

    private void updateSmartFooter() {
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
        if (adaptorTotalHeight > heightPixels) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.uikit_footer_view, null);
            VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) view.findViewById(R.id.hamburger_logo);
            setLogoCenterWithMargins(vectorDrawableImageView);
            drawerListView.addFooterView(view, null, false);
            setVectorImage(vectorDrawableImageView);
        } else {
            footerImage.setVisibility(View.VISIBLE);
        }
    }

    private int getDeviceHeightPixels() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getAdaptorTotalHeight() {
        if (drawerListView != null && drawerListView.getAdapter().getCount() != 0) {
            return (int) (drawerListView.getAdapter().getCount() * getResources().getDimension(R.dimen.uikit_hamburger_list_item_height));
        }
        return 0;
    }

    private void setVectorImage(final VectorDrawableImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(context, resID));
    }

    private void setLogoCenterWithMargins(final VectorDrawableImageView vectorDrawableImageView) {
        Resources resources = getResources();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) resources.getDimension(R.dimen.uikit_hamburger_logo_width), (int) resources.getDimension(R.dimen.uikit_hamburger_logo_height));
        lp.setMargins(0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_top_margin), 0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin));
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        vectorDrawableImageView.setLayoutParams(lp);
    }

    public void setTitle(CharSequence title) {
        actionBarTitle.setText(title);
    }


    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

}
