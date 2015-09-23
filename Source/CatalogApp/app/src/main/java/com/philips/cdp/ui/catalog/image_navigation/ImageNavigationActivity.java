package com.philips.cdp.ui.catalog.image_navigation;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.dot_navigation.DotNavigationTestFragment;
import com.philips.cdp.ui.catalog.dot_navigation.ViewPagerAdaptor;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

public class ImageNavigationActivity extends AppCompatActivity {

    private IconPagerAdaptor adaptor;
    private ViewPager pager;
    private PageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_navigation);

        adaptor = new IconPagerAdaptor(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adaptor);

        indicator = (IconPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

}
