package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.CustomListView.ListViewPagerAdapter;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.utils.TabUtils;

public class TabBarDemoBottom extends CatalogActivity {
    ListViewWithOptions adapter;
    ListView list;
    TabLayout bottomLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview_viewpager);
        TabUtils.disableActionbarShadow(this);
        setBottomBar();
        setViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(bottomLayout, this);
    }



    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new ListViewPagerAdapter(getSupportFragmentManager(), bottomLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomLayout));
        viewPager.setOffscreenPageLimit(4);
        bottomLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });
    }

    private void setBottomBar() {
        bottomLayout = (TabLayout) findViewById(R.id.tab_bar_for_list);
        TabUtils utils = new TabUtils(this, bottomLayout, false);

        TabLayout.Tab tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        bottomLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}

