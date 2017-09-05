/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.components.sidebar;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.DrawerMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.utils.UIDUtils;
import com.philips.platform.uid.view.widget.SideBar;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class SidebarTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private Resources resources;
    //private IdlingResource mIdlingResource;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(ULTRA_LIGHT, ColorRange.GROUP_BLUE.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        resources = activity.getResources();
        /*mIdlingResource = activity.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);*/
    }

    private ViewInteraction getSidebar() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_sidebar));
    }

    @Test
    public void verifySidebarElevation() {
        float expectedElevation = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.sidebar_elevation);
        getSidebar().check(matches(DrawerMatcher.isSameDrawerElevation(expectedElevation)));
    }

    @Test
    public void verifySidebarFollowMaxWidth() {
        float maxWidth = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.sidebar_max_width);
        getSidebar().check(matches(DrawerMatcher.isDrawerFollowMaxWidth(maxWidth)));
    }

    @Test
    public void verifySidebarHasFullScreenHeight() {
        float deviceHeight = UIDUtils.getDeviceHeight(activity.getBaseContext());//resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.sidebar_max_width);
        getSidebar().check(matches(DrawerMatcher.isSameDrawerHeight(deviceHeight)));
    }

    @Test
    public void verifyLeftSidebarIsClosedByDefault() {
        getSidebar().check(matches(DrawerMatcher.isDrawerClose(GravityCompat.START)));
    }

    @Test
    public void verifyRightSidebarIsClosedByDefault() {
        getSidebar().check(matches(DrawerMatcher.isDrawerClose(GravityCompat.END)));
    }

    @Test
    public void verifyLeftSidebarCanOpen() {
        getSidebar().check(matches(DrawerMatcher.isDrawerClose(GravityCompat.START)));
        getSidebar().perform(actionOpenDrawer(GravityCompat.START));
        waitFor(resources, 500);
        getSidebar().check(matches(DrawerMatcher.isDrawerOpen(GravityCompat.START)));
    }

    @Test
    public void verifyRightSidebarCanOpen() {
        getSidebar().check(matches(DrawerMatcher.isDrawerClose(GravityCompat.END)));
        getSidebar().perform(actionOpenDrawer(GravityCompat.END));
        waitFor(resources, 500);
        getSidebar().check(matches(DrawerMatcher.isDrawerOpen(GravityCompat.END)));
        /*waitFor(resources, 500);
        onView(withId(com.philips.platform.uid.test.R.id.container)).perform(ViewActions.click());
        waitFor(resources, 500);
        getSidebar().check(matches(DrawerMatcher.isDrawerClose(GravityCompat.END)));*/
    }

    @After
    public void closeDrawer() {

        getSidebar().perform(actionCloseDrawer(GravityCompat.START));
        getSidebar().perform(actionCloseDrawer(GravityCompat.END));
        /*if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }*/
    }

    public static ViewAction actionOpenDrawer(final int gravityCompat) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override public String getDescription() {
                return "open drawer";
            }

            @Override public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(gravityCompat);
            }
        };
    }

    public static ViewAction actionCloseDrawer(final int gravityCompat) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override public String getDescription() {
                return "close drawer";
            }

            @Override public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(gravityCompat);
            }
        };
    }

    /*@Test
    public void verifySidebarDimlayerColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidDimLayerSubtleBackgroundColor);
        getSidebar().check(matches(ViewPropertiesMatchers.isSameDrawerScrimColor(expectedColor)));
    }*/

}
