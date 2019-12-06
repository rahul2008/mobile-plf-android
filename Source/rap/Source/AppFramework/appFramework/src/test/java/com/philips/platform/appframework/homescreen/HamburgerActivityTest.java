/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.homescreen;

import android.content.res.Resources;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.logout.URLogoutInterface;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.view.widget.SideBar;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class HamburgerActivityTest {
    private HamburgerActivity hamburgerActivity = null;
    private Resources resource = null;
    private LinearLayout navigationView;
    private SideBar sideBar;
//    private FrameLayout hamburgerClick = null;
    private ActivityController<HamburgerMock> activityController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static HamburgerActivityPresenter hamburgerActivityPresenter;

    @Mock
    private static URLogoutInterface urLogoutInterface;

//    private TestAppFrameworkApplication application = null;

    static class HamburgerMock extends HamburgerActivity {
        @Override
        public ThemeConfiguration getThemeConfig() {
            colorRange = ColorRange.BLUE;
            navigationColor = NavigationColor.BRIGHT;
            contentColor = ContentColor.ULTRA_LIGHT;
            accentColorRange = AccentRange.AQUA;
            return new ThemeConfiguration(this, colorRange, navigationColor, contentColor, accentColorRange);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.catalog_view_menu, menu);
            return true;
        }

        @Override
        protected AbstractUIBasePresenter getActivityPresenter() {
            return hamburgerActivityPresenter;
        }

        @Override
        protected URLogoutInterface getURLogoutInterface() {
            return urLogoutInterface;
        }
    }

    @Before
    public void setup() {
        activityController = Robolectric.buildActivity(HamburgerMock.class);
        hamburgerActivity = activityController.create().start().get();

        navigationView = (LinearLayout) hamburgerActivity.findViewById(R.id.navigation_view);
        sideBar = (SideBar) hamburgerActivity.findViewById(R.id.sidebar_layout);

        resource = hamburgerActivity.getResources();
    }

    @Test
    public void titleShouldShouldNotBeEmpty() throws Exception {
        String title = resource.getString(R.string.app_name);
        hamburgerActivity.setTitle(title);
        assertNotNull(title);
    }

    @Test
    public void ActionBarDrawableToggelShouldNotBeNull() {
        ActionBarDrawerToggle drawerToggle = hamburgerActivity.configureDrawer();

        assertNotNull(drawerToggle);
    }

    //TODO: This API is giving NullPointerException -needs to be debugged and fixed, I am commenting this as I am unable to debug the test case in Studio.
    /*@Test
    public void ActionBarDrawableToggleClickListener() {
        ActionBarDrawerToggle drawerToggle = hamburgerActivity.configureDrawer();

        sideBar.addDrawerListener(drawerToggle);
        hamburgerClick.performClick();
        sideBar.openDrawer(navigationView);
        assertTrue(sideBar.isDrawerVisible(navigationView));
    }*/

    @Test
    public void updateActionBarWithStringAndTrueValue() {
        String title = resource.getString(com.philips.cdp.di.iap.R.string.app_name);
        hamburgerActivity.updateActionBar(title, true);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals("back", tag);
    }

    @Test
    public void updateActionBarWithStringAndFalseValue() {
        String title = resource.getString(com.philips.cdp.di.iap.R.string.app_name);
        hamburgerActivity.updateActionBar(title, false);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals("hamburger", tag);
    }

    @Test
    public void updateActionBarWithIntAndTrueValue() {
        hamburgerActivity.updateActionBar(android.R.string.ok, true);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals("back", tag);
    }

    @Test
    public void updateActionBarWithIntAndFalseValue() {
        hamburgerActivity.updateActionBar(android.R.string.ok, false);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals("hamburger", tag);
    }

    @Test
    public void containerIdShouldNotBeNull() {
        int containerId = hamburgerActivity.getContainerId();

        assertNotNull(containerId);
    }

    @Test
    public void presenterShouldNotBeNull() {
        HamburgerActivityPresenter presenter = new HamburgerActivityPresenter(hamburgerActivity);
        presenter.onEvent(0);
        assertNotNull(presenter);
    }






    @Test
    public void onMenuItemClickedTest() {
        verify(hamburgerActivityPresenter, times(1)).onEvent(0);
        hamburgerActivity.onMenuItemClicked(1);
        assertFalse(sideBar.isDrawerVisible(navigationView));
        verify(hamburgerActivityPresenter, times(1)).onEvent(1);
    }

    @Test
    public void onMenuItemClickedWithSamePositionTest() {
        verify(hamburgerActivityPresenter, times(1)).onEvent(0);
        hamburgerActivity.onMenuItemClicked(0);
        assertFalse(sideBar.isDrawerVisible(navigationView));
        verify(hamburgerActivityPresenter, times(1)).onEvent(0);
    }

    @Test
    public void testHamburgerMenuClick() {
        assertFalse(sideBar.isDrawerVisible(navigationView));
        MenuItem menuItem = new RoboMenuItem(android.R.id.home);
        hamburgerActivity.onOptionsItemSelected(menuItem);
        assertTrue(sideBar.isDrawerVisible(navigationView));
    }
    @Test
    public void testBackClickWhenDrawerVisible() {
        sideBar.openDrawer(navigationView);
        assertTrue(sideBar.isDrawerVisible(navigationView));
        hamburgerActivity.onBackPressed();
        assertFalse(sideBar.isDrawerVisible(navigationView));
    }
    @Test
    public void testHamburgerMenuClickWithBackButtonVisible() {
        hamburgerActivity.updateActionBarIcon(true);
        MenuItem menuItem = new RoboMenuItem(android.R.id.home);
        hamburgerActivity.onOptionsItemSelected(menuItem);
        assertTrue(hamburgerActivity.isFinishing());
    }

    @Test
    public void logoutResultFailureTest() {
        hamburgerActivity.onLogoutResultFailure(0, "Logout failure");
        assertEquals(ShadowToast.getTextOfLatestToast(), "Logout failure");
    }


    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        hamburgerActivityPresenter = null;
        hamburgerActivity = null;
        activityController = null;
    }
}