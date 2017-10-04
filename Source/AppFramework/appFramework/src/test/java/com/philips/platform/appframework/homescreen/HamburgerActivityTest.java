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
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class HamburgerActivityTest {
    private HamburgerActivity hamburgerActivity = null;
    private Resources resource = null;
    private NavigationView navigationView;
    private DrawerLayout philipsDrawerLayout;
    private FrameLayout hamburgerClick = null;
    private ActivityController<HamburgerMock> activityController;

//    private TestAppFrameworkApplication application = null;

    static class HamburgerMock extends HamburgerActivity {
        @Override
        public void initDLS() {
            setTheme(R.style.Theme_Philips_BrightAqua_Gradient_NoActionBar);
        }
    }
    @Before
    public void setup() {
        activityController=Robolectric.buildActivity(HamburgerMock.class);
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