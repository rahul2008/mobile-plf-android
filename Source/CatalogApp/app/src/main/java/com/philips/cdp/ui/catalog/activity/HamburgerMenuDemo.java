/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;

import java.util.ArrayList;

/**
 *  <b> Find the below steps to use Hamburger Menu</b>
 *      <pre>
 *          1.On SetContentView , pass the following layout uikit_hamburger_menu, with view id’s being
 *               *<b>DrawerLayout</b> - philips_drawer_layout
 *               *<b>Container </b> - frame_container
 *               *<b>ListView</b> - hamburger_list
 *               *<b>ImageView </b> - philips_logo (Footer view - Philips Shield)
 *               *<b>NavigationView</b> - navigation_view
 *          2.Use the HamburgerAdapter to set the adapter with parameters being Context, ArrayList< HamburgerItem> as shown below
 *               <pre>
 *                  HamburgerAdapter adapter = new HamburgerAdapter(this,hamburgerItems);
 ListView drawerListView.setAdapter(adapter);
 *               </pre>
 *          3.Find the Documentation for model HamburgerItem {@link com.philips.cdp.uikit.hamburger.HamburgerItem}
 *          4.Call the below code after setting/refreshing the adapter to resize the Philips Shield.
 *              <pre>
 *                  HamburgerUtil  hamburgerUtil = new HamburgerUtil(this,drawerListView);
 hamburgerUtil.updateSmartFooter(footerView);

 *              </pre>
 *          5.As we used tool bar to Support Action Bar theming kindly place the below code
 *              <pre>
 *                  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 setSupportActionBar(toolbar);
 *              </pre>
 *          6.Use the below snippet required to enable Action Bar
 *               <pre>
 *                    actionBar.setDisplayShowCustomEnabled(true);
 actionBar.setCustomView(com.philips.cdp.uikit.R.layout.uikit_action_bar_title); // Set your custom view for Action Bar if Required
 actionBar.setDisplayHomeAsUpEnabled(false);
 actionBar.setDisplayShowTitleEnabled(false);
 *               </pre>
 *          7.Create instance of Action Bar Title, Badge and Hamburger Click using following code
 *              <pre>
 *                  TextView  actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
 TextView  actionBarCount = (TextView) findViewById(R.id.hamburger_count);
 LinearLayout hamburgerClick = (LinearLayout) findViewById(R.id.hamburger_click);
 *              </pre>
 *          8.To Retain the selected list item override on ItemClickListener of listview and set adapter.setSelectionIndex(position).
 *
 *      </pre>
 */
public class HamburgerMenuDemo extends CatalogActivity {

    private String[] hamburgerMenuTitles;
    private TypedArray hamburgerMenuIcons;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView actionBarTitle;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;
    private int feature;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setNoActionBarTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.uikit_hamburger_menu);
        feature = getIntent().getIntExtra("feature", -1);

        initViews();
        initActionBar(getSupportActionBar());
        configureDrawer();
        loadSlideMenuItems();
        setHamburgerAdaptor();
        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
        setDrawerAdaptor();
        displayView(1);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title Long")) {
                    adapter.setSelectedIndex(position);
                    displayView(position);
                }
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        footerView = (ImageView) findViewById(R.id.philips_logo);
        int resID = com.philips.cdp.uikit.R.drawable.uikit_philips_logo;
        footerView.setImageDrawable(VectorDrawable.create(this, resID));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (feature == 2) {
            getMenuInflater().inflate(R.menu.uikit_hamburger_menu_item, menu);
            MenuItem reload = menu.findItem(R.id.action_reload);
            reload.setIcon(VectorDrawable.create(this, com.philips.cdp.uikit.R.drawable.uikit_reload));

            MenuItem info = menu.findItem(R.id.action_info);
            info.setIcon(VectorDrawable.create(this, com.philips.cdp.uikit.R.drawable.uikit_info));
        } else {
            getMenuInflater().inflate(R.menu.uikit_hamburger_menu_single_item, menu);
            MenuItem info = menu.findItem(R.id.action_info);
            info.setIcon(VectorDrawable.create(this, com.philips.cdp.uikit.R.drawable.uikit_info));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        actionBarTitle.setText(title);
        actionBarTitle.setSelected(true);
    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout, com.philips.cdp.uikit.R.string.app_name, com.philips.cdp.uikit.R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        philipsDrawerLayout.setDrawerListener(drawerToggle);
    }

    private void setHamburgerAdaptor() {
        if (feature == 1)
            addDrawerItems();
        else if (feature == 2)
            addDrawerItemsWithIcons();
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);

        hamburgerMenuIcons = getResources()
                .obtainTypedArray(R.array.hamburger_drawer_icons);

        hamburgerItems = new ArrayList<>();
    }

    private void addDrawerItems() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            if (i == 4) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null, 3));
            } else if (i == 6) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null, 222, false, true));
            } else if (hamburgerMenuTitles[i].equalsIgnoreCase("Title Long")) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null, 0, true));
            } else
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null));
        }
    }

    private void addDrawerItemsWithIcons() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            if (i == 4) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], VectorDrawable.create(this, hamburgerMenuIcons.getResourceId(i, -1)), 3));
            } else if (i == 6) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], VectorDrawable.create(this, hamburgerMenuIcons.getResourceId(i, -1)), 22, false, true));
            } else {
                if (hamburgerMenuTitles[i].equalsIgnoreCase("Title Long")) {
                    hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], VectorDrawable.create(this, hamburgerMenuIcons.getResourceId(i, -1)), 0, true));
                } else
                    hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], VectorDrawable.create(this, hamburgerMenuIcons.getResourceId(i, -1))));
            }
        }
    }

    private void setDrawerAdaptor() {
        TextView totalCountView = (TextView) findViewById(R.id.hamburger_count);
        adapter = new HamburgerAdapter(this, hamburgerItems, totalCountView);
        drawerListView.setAdapter(adapter);
    }

    private void displayView(int position) {
        final HamburgerFragment fragment = new HamburgerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = getBundle(hamburgerMenuTitles[position], hamburgerMenuIcons.getResourceId(position, -1));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
        setTitle(hamburgerMenuTitles[position]);
        philipsDrawerLayout.closeDrawer(navigationView);
    }

    @NonNull
    private Bundle getBundle(final String navMenuTitle, final int resourceId) {
        Bundle bundle = new Bundle();
        bundle.putString("data", navMenuTitle);
        bundle.putInt("resId", resourceId);
        return bundle;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_reload:
                Toast.makeText(this, "Clicked Reload", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_info:
                Toast.makeText(this, "Clicked Info", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(com.philips.cdp.uikit.R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
        hamburgerIcon = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_hamburger_icon));
        LinearLayout hamburgerClick = (LinearLayout) findViewById(R.id.hamburger_click);

        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                philipsDrawerLayout.openDrawer(navigationView);
            }
        });
    }

}
