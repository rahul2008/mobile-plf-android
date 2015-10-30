package com.philips.cdp.uikit.hamburger;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.HashMap;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsExpandableHamburgerMenu extends UiKitActivity {

    protected List<String> listDataHeader;
    protected HashMap<String, List<String>> listDataChild;
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
        configureDrawer(getSupportActionBar());
        drawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition);
                return false;
            }
        });
        updateSmartFooter();
        setHamburgerItemClickListener();
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
        drawer.findViewById(R.id.list_slidingmenu).setPadding(0, getStatusBarHeight(), 0, 0);
        listViewParentLayout = (LinearLayout) drawer.findViewById(R.id.list_view_parent);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.drawer_layout);
        drawerListView = (ExpandableListView) drawer.findViewById(R.id.list_slidingmenu);
        footerImage = (VectorDrawableImageView) drawer.findViewById(R.id.image);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    private void setHamburgerItemClickListener() {

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

    private void configureDrawer(final ActionBar actionBar) {
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
        return true;
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

    private void updateSmartFooter() {
        drawerListView.post(new Runnable() {
            @Override
            public void run() {
                int numItemsVisible = drawerListView.getLastVisiblePosition() -
                        drawerListView.getFirstVisiblePosition();

                if (drawerListView.getAdapter() != null && listVisibleRowsForExpandableGroup() > numItemsVisible) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.uikit_footer_view, null);
                    VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) v.findViewById(R.id.hamburger_logo);
                    setLogoCenterWithMargins(vectorDrawableImageView);
                    drawerListView.addFooterView(v);
                    setVectorImage(vectorDrawableImageView);
                } else {
                    footerImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setVectorImage(final VectorDrawableImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(PhilipsExpandableHamburgerMenu.this, resID));
    }

    private int listVisibleRowsForExpandableGroup() {
        int firstVis = drawerListView.getFirstVisiblePosition();
        int lastVis = drawerListView.getLastVisiblePosition();
        int count = firstVis;
        while (count <= lastVis) {
            count++;
        }
        return count;
    }

    private void setLogoCenterWithMargins(final VectorDrawableImageView vectorDrawableImageView) {
        Resources resources = getResources();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) resources.getDimension(R.dimen.uikit_hamburger_logo_width), (int) resources.getDimension(R.dimen.uikit_hamburger_logo_height));
        lp.setMargins(0, 50, 0, 50);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        vectorDrawableImageView.setLayoutParams(lp);
    }

    protected void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
    }
}
