package com.philips.cdp.uikit.hamburger;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsExpandableHamburgerMenu extends UiKitActivity {

    protected ExpandableListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView actionBarTitle;
    private LinearLayout listViewParentLayout;
    private VectorDrawableImageView footerImage;
    private FrameLayout parentView;
    private ExpandableListView.OnChildClickListener onItemClickListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.uikit_hamburger_parent);
        initializeHamburgerParentView();
        moveDrawerToTop();
        initActionBar();
        configureDrawer();
        disableGroupCollapse();
        updateSmartFooter();
        setHamburgerChildItemClickListener();
    }

    private void disableGroupCollapse() {
        drawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    private void initializeHamburgerParentView() {
        parentView = (FrameLayout) findViewById(R.id.main_content);
    }

    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.uikit_hamburger_menu_expandable, null);
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.frame_container);
        container.addView(child, 0);
        initializeDrawerViews(drawer);
        decor.addView(drawer);
    }

    private void initializeDrawerViews(final DrawerLayout drawer) {
        drawer.findViewById(R.id.hamburger_list).setPadding(0, getStatusBarHeight(), 0, 0);
        listViewParentLayout = (LinearLayout) drawer.findViewById(R.id.list_view_parent);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.philips_drawer_layout);
        drawerListView = (ExpandableListView) drawer.findViewById(R.id.hamburger_list);
        footerImage = (VectorDrawableImageView) drawer.findViewById(R.id.image);
    }

    private int getStatusBarHeight() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public void setContentView(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
        parentView.removeAllViews();
        parentView.addView(view);
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.uikit_hamburger_icon);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
    }

    @Override
    public void setContentView(final View view) {
        parentView.removeAllViews();
        parentView.addView(view);
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams params) {
        parentView.removeAllViews();
        view.setLayoutParams(params);
        parentView.addView(view);
    }

    private void setHamburgerChildItemClickListener() {

        drawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onChildClick(parent, v, groupPosition, childPosition, id);
                }
                return true;
            }
        });
    }

    protected void setOnItemClickListener(ExpandableListView.OnChildClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void setTitle(CharSequence title) {
        actionBarTitle.setText(title);
    }

    protected int getFragmentContainerID() {
        return R.id.main_content;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uikit_hamburger_menu_item, menu);
        MenuItem menuItem = menu.findItem(R.id.action_reload);
        inflateVectorMenu(menuItem);
        return true;
    }

    private void inflateVectorMenu(MenuItem menuItem) {
        VectorDrawableImageView vectorDrawableImageView = new VectorDrawableImageView(this);
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_reload));
        menuItem.setActionView(vectorDrawableImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setVectorImage(final VectorDrawableImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(PhilipsExpandableHamburgerMenu.this, resID));
    }

    private void setLogoCenterWithMargins(final VectorDrawableImageView vectorDrawableImageView) {
        Resources resources = getResources();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) resources.getDimension(R.dimen.uikit_hamburger_logo_width), (int) resources.getDimension(R.dimen.uikit_hamburger_logo_height));
        lp.setMargins(0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_top_margin), 0, (int) resources.getDimension(R.dimen.uikit_hamburger_menu_logo_bottom_margin));
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        vectorDrawableImageView.setLayoutParams(lp);
    }

    protected void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
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
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        PhilipsExpandableHamburgerMenu.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getAdaptorTotalHeight() {
        int childCount = drawerListView.getAdapter().getCount();
        if (drawerListView != null) {
            int groupCount = getGroupCount();
            return (int) ((childCount + groupCount) * getResources().getDimension(R.dimen.uikit_hamburger_list_item_height));
        }
        return 0;
    }

    private int getGroupCount() {
        PhilipsExpandableListAdapter philipsExpandableListAdapter = (PhilipsExpandableListAdapter) drawerListView.getExpandableListAdapter();
        return philipsExpandableListAdapter.getGroupCount();
    }

    public ExpandableListView getDrawerListView() {
        return drawerListView;
    }

}
