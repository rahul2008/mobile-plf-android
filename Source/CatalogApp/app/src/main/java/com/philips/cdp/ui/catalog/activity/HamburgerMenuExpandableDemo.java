package com.philips.cdp.ui.catalog.activity;

import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
import com.philips.cdp.uikit.CustomButton.PhilipsExpandableDrawerLayout;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.hamburger.PhilipsExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuExpandableDemo extends CatalogActivity {

    private String[] hamburgerMenuTitles;
    private TypedArray hamburgerMenuIcons;
    private List<String> listDataHeader;
    private HashMap<String, List<HamburgerItem>> listDataChild;
    private PhilipsExpandableDrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_expandable_demo);
        philipsDrawerLayout = (PhilipsExpandableDrawerLayout) findViewById(R.id.drawer_layout_demo);
        loadSlideMenuItems();
        prepareListData();
        setHamburgerAdaptor();
        if (savedInstanceState == null) {
            displayView(0, 0);
        }

        philipsDrawerLayout.getDrawerListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {
                displayView(groupPosition, childPosition);
                return false;
            }
        });
        setTitle(getResources().getString(R.string.app_name));
        configureDrawer();
    }

    private void setHamburgerAdaptor() {
        final PhilipsExpandableListAdapter listAdapter = new PhilipsExpandableListAdapter(this, listDataHeader, listDataChild);
        philipsDrawerLayout.getDrawerListView().setAdapter(listAdapter);
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        hamburgerMenuIcons = getResources()
                .obtainTypedArray(R.array.hamburger_drawer_icons);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_reload:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int groupPosition, final int childPosition) {
        final HamburgerFragment fragment = new HamburgerFragment();
            FragmentManager fragmentManager = getFragmentManager();
        List<HamburgerItem> child = listDataChild.get(listDataHeader.get(groupPosition));
        HamburgerItem hamburgerItem = child.get(childPosition);
        Bundle bundle = getBundle(hamburgerItem.getTitle(), hamburgerMenuIcons.getResourceId(groupPosition, -1));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.drawer_layout_demo, fragment).commit();
        setTitle(hamburgerItem.getTitle());
        philipsDrawerLayout.closeDrawer();
    }

    @NonNull
    private Bundle getBundle(final String navMenuTitle, final int resourceId) {
        Bundle bundle = new Bundle();
        bundle.putString("data", navMenuTitle);
        bundle.putInt("resId", resourceId);
        return bundle;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Title Long");
        listDataHeader.add("Title long");

        int feature = getIntent().getIntExtra("feature", -1);

        List<HamburgerItem> Title_Long = new ArrayList<>();
        List<HamburgerItem> Title_long = new ArrayList<>();
        if (feature == 1) {
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[0], 0));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[1], 0));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[2], 0));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[3], 0));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[4], 0));

            Title_long.add(new HamburgerItem(hamburgerMenuTitles[5], 0));
            Title_long.add(new HamburgerItem(hamburgerMenuTitles[6], 0));
        } else if (feature == 2) {
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[0], hamburgerMenuIcons.getResourceId(0, -1)));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[1], hamburgerMenuIcons.getResourceId(1, -1)));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[2], hamburgerMenuIcons.getResourceId(2, -1)));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[3], hamburgerMenuIcons.getResourceId(3, -1)));
            Title_Long.add(new HamburgerItem(hamburgerMenuTitles[4], hamburgerMenuIcons.getResourceId(4, -1)));

            Title_long.add(new HamburgerItem(hamburgerMenuTitles[5], hamburgerMenuIcons.getResourceId(5, -1)));
            Title_long.add(new HamburgerItem(hamburgerMenuTitles[6], hamburgerMenuIcons.getResourceId(6, -1)));
        }

        listDataChild.put(listDataHeader.get(0), Title_Long);
        listDataChild.put(listDataHeader.get(1), Title_long);
    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout.getDrawerLayout(), com.philips.cdp.uikit.R.string.app_name, com.philips.cdp.uikit.R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        philipsDrawerLayout.getDrawerLayout().setDrawerListener(drawerToggle);
    }
}
