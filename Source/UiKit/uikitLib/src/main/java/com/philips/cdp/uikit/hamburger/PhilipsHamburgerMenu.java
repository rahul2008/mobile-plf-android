package com.philips.cdp.uikit.hamburger;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.AdapterView;
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

    protected ListView mDrawerList;
    protected ArrayList<NavDrawerItem> navDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private TextView actionBarTitle;
    private LinearLayout linearLayout;
    private VectorDrawableImageView footerImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_menu);
        initializeHamburgerViews();
        setDrawerTitle();

        updateSmartFooter();
        configureDrawer(savedInstanceState, getSupportActionBar());

    }

    private void initializeHamburgerViews() {
        linearLayout = (LinearLayout) findViewById(R.id.list_view_parent);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title);
        actionBarTitle = (TextView) findViewById(R.id.title);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidingmenu);
        footerImage = (VectorDrawableImageView) findViewById(R.id.image);
        setActionBarSettings(actionBar);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    private void configureDrawer(final Bundle savedInstanceState, final ActionBar actionBar) {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                actionBar.setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    private void setActionBarSettings(final ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    private void setDrawerTitle() {
        mTitle = mDrawerTitle = getTitle();
    }



    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBarTitle.setText(title);
    }

    protected int getFragmentContainerID() {
        return R.id.frame_container;
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new FindPeopleFragment();
                break;
            case 2:
                fragment = new PhotosFragment();
                break;
            case 3:
                fragment = new CommunityFragment();
                break;
            case 4:
                fragment = new PagesFragment();
                break;
            case 5:
                fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
            mDrawerLayout.updateViewLayout(linearLayout, layoutParams);
            mDrawerLayout.closeDrawer(linearLayout);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSmartFooter() {
        mDrawerList.post(new Runnable() {
            @Override
            public void run() {
                int numItemsVisible = mDrawerList.getLastVisiblePosition() -
                        mDrawerList.getFirstVisiblePosition();
                if (navDrawerItems != null && navDrawerItems.size() - 1 > numItemsVisible) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.footer_view, null);
                    VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) v.findViewById(R.id.splash_logo);
                    setLogoCenterWithMargins(vectorDrawableImageView);
                    mDrawerList.addFooterView(v);
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

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
            displayView(i);
        }
    }
}
