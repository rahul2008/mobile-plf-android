package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.philips.cdp.ui.catalog.CustomListView.ListViewPagerAdapter;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.TabUtils;

public class TabBarDemoTop extends CatalogActivity {

    TabLayout topLayout;
    TabLayout bottomLayout;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_demo_top);
        TabUtils.disableActionbarShadow(this);
        TabLayout view = (TabLayout) findViewById(R.id.tab_bar);
        setTopBar();
        setViewPager();
      //  setBottomBar();
        if(savedInstanceState != null) {
            topLayout.post(new Runnable() {
                @Override
                public void run() {
                    topLayout.getTabAt(savedInstanceState.getInt("top")).select();
                 //   bottomLayout.getTabAt(savedInstanceState.getInt("bottom")).select();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(topLayout, this);
      //  TabUtils.adjustTabs(bottomLayout, this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top", topLayout.getSelectedTabPosition());
       // outState.putInt("bottom", bottomLayout.getSelectedTabPosition());
    }
    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new ListViewPagerAdapter(getSupportFragmentManager(), topLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topLayout));
        viewPager.setOffscreenPageLimit(4);
        topLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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
    private void setTopBar() {
        topLayout = (TabLayout) findViewById(R.id.tab_bar);
        TabUtils utils = new TabUtils(this, topLayout, true);

        TabLayout.Tab tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0);
        utils.setIcon(tab, VectorDrawable.create(this, R.drawable.uikit_clock_32x32),true);
        utils.setTitle(tab, "Alarm");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.apple, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_apple_32x32),true);
        utils.setTitle(tab, "Wellness");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.barchart, 3);
        utils.setIcon(tab, VectorDrawable.create(this, R.drawable.uikit_stats_39x32), true);
        utils.setTitle(tab, "Statistics");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.gear, 0);
        utils.setIcon(tab, VectorDrawable.create(this, R.drawable.uikit_gear_32x32), true);
        utils.setTitle(tab, "Settings");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_clock_32x32),true);
        utils.setTitle(tab, "Alarm");
        topLayout.addTab(tab);
    }

   /* private void setBottomBar() {
      //  bottomLayout = (TabLayout) findViewById(R.id.tab_bar_text);
        TabUtils utils = new TabUtils(this, bottomLayout, false);
        TabLayout.Tab tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Alarm");
    //    bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Wellness");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Statistics");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab,"Settings");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Alarm");
        bottomLayout.addTab(tab);
    }*/


}

