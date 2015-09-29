package com.philips.cdp.ui.catalog.imagenavigation;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.IconsAdaptor;
import com.viewpagerindicator.PageIndicator;

public class ImageNavigationActivity extends CatalogActivity {

    private IconsAdaptor adaptor;
    private ViewPager pager;
    private PageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_navigation);

        adaptor = new UserAdapter(getSupportFragmentManager(),new int[] {
                R.drawable.perm_group_calendar,
                R.drawable.perm_group_camera,
                R.drawable.perm_group_device_alarms,
                R.drawable.perm_group_location
        });

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adaptor);

        indicator = (IconPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

}
