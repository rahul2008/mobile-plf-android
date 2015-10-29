package com.philips.cdp.uikit.hamburger;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.wnafee.vector.compat.VectorDrawable;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsHamburgerMenu extends UiKitActivity {

    protected ListView drawerListView;
    protected ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private TextView actionBarTitle;
    private LinearLayout listViewParentLayout;
    private VectorDrawableImageView footerImage;
    private FrameLayout parentView;
    private ListView.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.hamburger_menu);
        initializeHamburgerViews();
        setDrawerTitle();

        updateSmartFooter();
        configureDrawer(getSupportActionBar());
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
        parentView = (FrameLayout) findViewById(R.id.frame_container);
        listViewParentLayout = (LinearLayout) findViewById(R.id.list_view_parent);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.list_slidingmenu);
        footerImage = (VectorDrawableImageView) findViewById(R.id.image);
        setActionBarSettings(actionBar);
        setHamburgerItemClickListener();
    }

    private void setHamburgerItemClickListener() {
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    protected void setOnItemClickListener(ListView.OnItemClickListener onItemClickListener) {
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

    protected void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
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
                if (hamburgerItems != null && hamburgerItems.size() - 1 > numItemsVisible) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.footer_view, null);
                    VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) v.findViewById(R.id.splash_logo);
                    setLogoCenterWithMargins(vectorDrawableImageView);
                    drawerListView.addFooterView(v);
                    setVectorImage(vectorDrawableImageView);
                    v.setVisibility(View.VISIBLE);
                } else {
                    footerImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setVectorImage(final VectorDrawableImageView vectorDrawableImageView) {
        int resID = R.drawable.uikit_philips_logo;
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(getResources(), resID));
    }

    private void setLogoCenterWithMargins(final VectorDrawableImageView vectorDrawableImageView) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 50, 0, 50);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        vectorDrawableImageView.setLayoutParams(lp);
    }

}
