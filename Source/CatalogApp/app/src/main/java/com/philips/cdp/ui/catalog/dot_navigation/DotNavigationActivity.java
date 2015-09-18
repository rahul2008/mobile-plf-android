package com.philips.cdp.ui.catalog.dot_navigation;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.Random;

public class DotNavigationActivity extends CatalogActivity {

    private ViewPagerAdaptor adaptor;
    private ViewPager pager;
    private PageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        adaptor = new ViewPagerAdaptor(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adaptor);

        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

}