package com.philips.appframework;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.customviews.CircleIndicator;

/**
 * Created by 310240027 on 5/31/2016.
 */
public class IntroductionScreenActivity extends UiKitActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_framework_introduction_activity);

        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        final CircleIndicator mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
