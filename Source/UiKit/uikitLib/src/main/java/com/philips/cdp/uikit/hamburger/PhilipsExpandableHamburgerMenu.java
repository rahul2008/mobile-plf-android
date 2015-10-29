package com.philips.cdp.uikit.hamburger;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
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
    private CharSequence drawerTitle;
    private CharSequence title;
    private TextView actionBarTitle;
    private LinearLayout listViewParentLayout;
    private VectorDrawableImageView footerImage;
    private FrameLayout parentView;
    private ExpandableListView.OnChildClickListener onItemClickListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.hamburger_menu_expandable);
        initializeHamburgerViews();
        setDrawerTitle();
        configureDrawer(getSupportActionBar());
        drawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.i(getClass() + "", " Clicked group");
                parent.expandGroup(groupPosition);
                return false;
            }
        });
        updateSmartFooter();
    }

    public void setContentView(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
        parentView.removeAllViews();
        parentView.addView(view);
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

    private void initializeHamburgerViews() {
        listViewParentLayout = (LinearLayout) findViewById(R.id.list_view_parent);
        parentView = (FrameLayout) findViewById(R.id.frame_container);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ExpandableListView) findViewById(R.id.list_slidingmenu);
        footerImage = (VectorDrawableImageView) findViewById(R.id.image);
        setActionBarSettings(actionBar);
        setHamburgerItemClickListener();
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
                actionBar.setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setActionBarSettings(final ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.uikit_hamburger_icon);
    }

    private void setDrawerTitle() {
        title = drawerTitle = getTitle();
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        actionBarTitle.setText(title);
    }

    protected int getFragmentContainerID() {
        return R.id.frame_container;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
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
                    VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) v.findViewById(R.id.splash_logo);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 50, 0, 50);
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    vectorDrawableImageView.setLayoutParams(lp);
                    drawerListView.addFooterView(v);
                    int resID = R.drawable.uikit_philips_logo;
                    vectorDrawableImageView.setImageDrawable(VectorDrawable.create(PhilipsExpandableHamburgerMenu.this, resID));

                    v.setVisibility(View.VISIBLE);
                } else {
                    footerImage.setVisibility(View.VISIBLE);
                }
            }
        });
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

    protected void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
    }
}
