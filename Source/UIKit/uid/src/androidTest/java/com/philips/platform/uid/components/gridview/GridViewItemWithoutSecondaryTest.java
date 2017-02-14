package com.philips.platform.uid.components.gridview;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class GridViewItemWithoutSecondaryTest {

    Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_gridview_item_plain_icon);
        testResources = activity.getResources();
    }

    @NonNull
    private Intent getLaunchIntent(final int navigationColor, final int contentColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    private ViewInteraction getImageView() {
        return onView(withId(R.id.uid_gridView_thumbnail));
    }

    private ViewInteraction getIconHolder() {
        return onView(withId(R.id.uid_gridView_star_icon_holder));
    }

    private ViewInteraction getStarIcon() {
        return onView(withId(R.id.uid_gridView_star_icon));
    }


}
