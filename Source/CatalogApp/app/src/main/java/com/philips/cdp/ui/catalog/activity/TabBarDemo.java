package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.TabUtils;

public class TabBarDemo extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_demo);
        TabLayout view = (TabLayout) findViewById(R.id.tab_bar);
        setTopBar();
        setBottomBar();
    }

    private void setTopBar() {
        TabLayout view = (TabLayout) findViewById(R.id.tab_bar);
        TabUtils utils = new TabUtils(this, view, true);

        TabLayout.Tab tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0);
        utils.setTitle(tab, "Alarm");
        view.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.apple, 0);
        utils.setTitle(tab, "Wellness");
        view.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.barchart, 0);
        utils.setTitle(tab, "Statistics");
        view.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.gear, 0);
        utils.setTitle(tab, "Settings");
        view.addTab(tab);
    }

    private void setBottomBar() {
        TabLayout textBar = (TabLayout) findViewById(R.id.tab_bar_text);
        TabUtils utils = new TabUtils(this, textBar, false);
        TabLayout.Tab tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Alarm");
        textBar.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Wellness");
        textBar.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Statistics");
        textBar.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Settings");
        textBar.addTab(tab);
    }
}
