package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.TabUtils;

public class TabBarDemo extends CatalogActivity {

    TabLayout topLayout;
    TabLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_demo);
        TabLayout view = (TabLayout) findViewById(R.id.tab_bar);
        setTopBar();
        setBottomBar();
        if(savedInstanceState != null) {
            topLayout.getTabAt(savedInstanceState.getInt("top")).select();
            bottomLayout.getTabAt(savedInstanceState.getInt("bottom")).select();;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(topLayout, this);
        TabUtils.adjustTabs(bottomLayout,this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top", topLayout.getSelectedTabPosition());
        outState.putInt("bottom",bottomLayout.getSelectedTabPosition());
    }

    private void setTopBar() {
        topLayout = (TabLayout) findViewById(R.id.tab_bar);
        TabUtils utils = new TabUtils(this, topLayout, true);

        TabLayout.Tab tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0, false);
        utils.setTitle(tab, "Alarm");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.apple, 0, false);
        utils.setTitle(tab, "Wellness");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.barchart, 0, false);
        utils.setTitle(tab, "Statistics");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.gear, 0, true);
        utils.setTitle(tab, "Settings");
        topLayout.addTab(tab);
    }

    private void setBottomBar() {
        bottomLayout = (TabLayout) findViewById(R.id.tab_bar_text);
        TabUtils utils = new TabUtils(this, bottomLayout, false);
        TabLayout.Tab tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Alarm");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Wellness");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Statistics");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, true);
        utils.setTitle(tab, "Settings");
        bottomLayout.addTab(tab);
    }
}
