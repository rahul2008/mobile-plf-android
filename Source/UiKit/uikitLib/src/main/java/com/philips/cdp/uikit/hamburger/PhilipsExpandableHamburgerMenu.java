package com.philips.cdp.uikit.hamburger;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.wnafee.vector.compat.VectorDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsExpandableHamburgerMenu extends UiKitActivity {

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView expListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private TextView actionBarTitle;
    private LinearLayout linearLayout;
    private VectorDrawableImageView footerImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_menu_expandable);
        initializeHamburgerViews();
        setDrawerTitle();

        loadSlideMenuItems();

//        addDrawerItems();
//        setDrawerAdaptor();
        configureDrawer(savedInstanceState, getSupportActionBar());

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView arg0, View itemView, int itemPosition, long itemId) {
//                expListView.expandGroup(itemPosition);
                return false;
            }
        });

        updateSmartFooter();
    }

    private void initializeHamburgerViews() {
        linearLayout = (LinearLayout) findViewById(R.id.list_view_parent);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title);
        actionBarTitle = (TextView) findViewById(R.id.title);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expListView = (ExpandableListView) findViewById(R.id.list_slidingmenu);
        footerImage = (VectorDrawableImageView) findViewById(R.id.image);
        setActionBarSettings(actionBar);
        expListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        expListView.setOnItemClickListener(new SlideMenuClickListener());
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


    private void loadSlideMenuItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<NavDrawerItem>();
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
            expListView.setItemChecked(position, true);
            expListView.setSelection(position);
            setTitle(navMenuTitles[position]);
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
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void updateSmartFooter() {
        expListView.post(new Runnable() {
            @Override
            public void run() {
                int numItemsVisible = expListView.getLastVisiblePosition() -
                        expListView.getFirstVisiblePosition();

                int childCount = getTotalChildCount();

                if (listAdapter != null && listVisibleRowsForExpandableGroup() > numItemsVisible) {
                    // set your footer on the ListView
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.footer_view, null);
                    VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) v.findViewById(R.id.splash_logo);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 50, 0, 50);
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    vectorDrawableImageView.setLayoutParams(lp);
                    expListView.addFooterView(v);
                    int resID = R.drawable.uikit_philips_logo;
                    vectorDrawableImageView.setImageDrawable(VectorDrawable.create(getResources(), resID));

                    v.setVisibility(View.VISIBLE);
                } else {
                    footerImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int getTotalChildCount() {
        int j = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            j += listAdapter.getChildrenCount(i);
        }
        return j;
    }

    private int listVisibleRowsForExpandableGroup() {
        int firstVis = expListView.getFirstVisiblePosition();
        int lastVis = expListView.getLastVisiblePosition();
        int count = firstVis;
        while (count <= lastVis) {
            count++;
        }
        return count;
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
            displayView(i);
        }
    }
}
