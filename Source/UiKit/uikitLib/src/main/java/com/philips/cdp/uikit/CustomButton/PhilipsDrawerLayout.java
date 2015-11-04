package com.philips.cdp.uikit.CustomButton;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerUtil;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsDrawerLayout extends LinearLayout {

    HamburgerUtil hamburgerUtil;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private VectorDrawableImageView footerImage;
    private LinearLayout listViewParentLayout;
    private TextView actionBarTitle;

    public PhilipsDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        hamburgerUtil = new HamburgerUtil(getContext());
        initializeDrawer();
        hamburgerUtil.updateSmartFooter(drawerListView, footerImage);
    }

    public PhilipsDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hamburgerUtil = new HamburgerUtil(getContext());
        initializeDrawer();
        hamburgerUtil.updateSmartFooter(drawerListView, footerImage);
    }

    private void initializeDrawer() {
        context = getContext();
        AppCompatActivity activity = (AppCompatActivity) context;
        initActionBar(activity.getSupportActionBar());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.uikit_hamburger_menu, null);
        hamburgerUtil.moveDrawerToTop(drawer);
        initializeDrawerViews(drawer);
    }

    private void initializeDrawerViews(DrawerLayout drawer) {
        listViewParentLayout = (LinearLayout) drawer.findViewById(R.id.list_view_parent);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) drawer.findViewById(R.id.hamburger_list);
        drawerListView.setPadding(0, hamburgerUtil.getStatusBarHeight(), 0, 0);
        footerImage = (VectorDrawableImageView) drawer.findViewById(R.id.image);
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

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(VectorDrawable.create(context, R.drawable.uikit_hamburger_icon));
        actionBarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.hamburger_title);
    }

    public void setTitle(CharSequence title) {
        if (actionBarTitle != null)
            actionBarTitle.setText(title);
    }

}
