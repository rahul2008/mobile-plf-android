/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class HamburgerActivityTest {
    private HamburgerActivity hamburgerActivity = null;
    private Resources resource = null;
    private NavigationView navigationView;
    private DrawerLayout philipsDrawerLayout;
    private FrameLayout hamburgerClick = null;
    private ActivityController<TestActivity> activityController;

    @Before
    public void setup() {
        activityController=Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity=activityController.create().start().get();
        navigationView = (NavigationView) hamburgerActivity.findViewById(R.id.navigation_view);
        philipsDrawerLayout = (DrawerLayout) hamburgerActivity.findViewById(R.id.philips_drawer_layout);

        View customView = LayoutInflater.from(hamburgerActivity).
                inflate(R.layout.af_action_bar_shopping_cart, null);
        hamburgerClick = (FrameLayout) customView.findViewById(R.id.af_hamburger_frame_layout);
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

    @Test
    public void ActionBarDrawableToggleClickListener() {
        ActionBarDrawerToggle drawerToggle = hamburgerActivity.configureDrawer();

        philipsDrawerLayout.addDrawerListener(drawerToggle);
        hamburgerClick.performClick();
        philipsDrawerLayout.openDrawer(navigationView);
        assertTrue(philipsDrawerLayout.isDrawerVisible(navigationView));
    }

    @Test
    public void updateActionBarWithStringAndTrueValue() {
        String title = resource.getString(com.philips.cdp.di.iap.R.string.app_name);
        hamburgerActivity.updateActionBar(title, true);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals(String.valueOf(R.drawable.left_arrow), tag);
    }

    @Test
    public void updateActionBarWithStringAndFalseValue() {
        String title = resource.getString(com.philips.cdp.di.iap.R.string.app_name);
        hamburgerActivity.updateActionBar(title, false);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals(String.valueOf(R.drawable.uikit_hamburger_icon), tag);
    }

    @Test
    public void updateActionBarWithIntAndTrueValue() {
        hamburgerActivity.updateActionBar(android.R.string.ok, true);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals(String.valueOf(R.drawable.left_arrow), tag);
    }

    @Test
    public void updateActionBarWithIntAndFalseValue() {
        hamburgerActivity.updateActionBar(android.R.string.ok, false);

        String tag = hamburgerActivity.getActionbarTag();

        assertEquals(String.valueOf(R.drawable.uikit_hamburger_icon), tag);
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
    public void onBackPressFragmentCount(){
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        if(philipsDrawerLayout.isDrawerOpen(navigationView))
        {
            philipsDrawerLayout.closeDrawer(navigationView);
        }

        hamburgerActivity.onBackPressed();

        HamburgerAdapter adapter = hamburgerActivity.getHamburgerAdapter();
        HamburgerItem hamburgerItem = (HamburgerItem) adapter.getItem(fragmentCount);

        String menuItem = hamburgerActivity.getResources().getString(R.string.RA_HomeScreen_Title);

        if(fragmentCount==0) {
            assertEquals(hamburgerItem.getTitle(), menuItem);
        }
        else{
            assertNotEquals(hamburgerItem.getTitle(), menuItem);
        }
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
    }
}