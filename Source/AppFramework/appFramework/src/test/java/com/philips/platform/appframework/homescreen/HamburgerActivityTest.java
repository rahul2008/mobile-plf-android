/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cdp.registration.User;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.logout.URLogoutInterface;
import com.philips.platform.appframework.models.HamburgerMenuItem;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uid.view.widget.Label;
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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class HamburgerActivityTest {
    private HamburgerActivity hamburgerActivity = null;
    private Resources resource = null;
    private LinearLayout navigationView;
    private SideBar sideBar;
    private FrameLayout hamburgerClick = null;
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
        public void initDLS() {
            setTheme(R.style.Theme_Philips_BrightAqua_Gradient_NoActionBar);
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
        activityController=Robolectric.buildActivity(HamburgerMock.class);
        hamburgerActivity=activityController.create().start().get();
        navigationView = (LinearLayout) hamburgerActivity.findViewById(R.id.navigation_view);
        sideBar = (SideBar) hamburgerActivity.findViewById(R.id.sidebar_layout);

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

        sideBar.addDrawerListener(drawerToggle);
        hamburgerClick.performClick();
        sideBar.openDrawer(navigationView);
        assertTrue(sideBar.isDrawerVisible(navigationView));
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

        assertEquals(String.valueOf(R.drawable.ic_hamburger_icon), tag);
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

        assertEquals(String.valueOf(R.drawable.ic_hamburger_icon), tag);
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
        if(sideBar.isDrawerOpen(navigationView))
        {
            sideBar.closeDrawer(navigationView);
        }

        hamburgerActivity.onBackPressed();

        HamburgerMenuAdapter adapter = hamburgerActivity.getHamburgerAdapter();
        HamburgerMenuItem hamburgerItem = adapter.getMenuItem(fragmentCount);

        String menuItem = hamburgerActivity.getResources().getString(R.string.RA_HomeTab_Menu_Title);

        if(fragmentCount==0) {
            assertEquals(hamburgerItem.getTitle(), menuItem);
        }
        else{
            assertNotEquals(hamburgerItem.getTitle(), menuItem);
        }
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
    public void logoutClickWhenUserLoggedInTest() {
        LinearLayout logoutParent = (LinearLayout) hamburgerActivity.findViewById(R.id.hamburger_menu_footer_container);
        logoutParent.performClick();
        assertFalse(sideBar.isDrawerVisible(navigationView));
        verify(urLogoutInterface).performLogout(any(Context.class), any(User.class), anyBoolean(), anyBoolean());
    }
    @Test
    public void logoutClickWhenUserLoggedOutTest() {
        TestAppFrameworkApplication testAppFrameworkApplication = (TestAppFrameworkApplication) RuntimeEnvironment.application;
        when(testAppFrameworkApplication.getUserRegistrationState().getUserObject(any(Context.class)).isUserSignIn()).thenReturn(false);
        LinearLayout logoutParent = (LinearLayout) hamburgerActivity.findViewById(R.id.hamburger_menu_footer_container);
        logoutParent.performClick();
        assertFalse(sideBar.isDrawerVisible(navigationView));
        verify(hamburgerActivityPresenter, times(1)).onEvent(Constants.LOGIN_BUTTON_CLICK_CONSTANT);
    }
    @Test
    public void logoutResultFailureTest() {
        hamburgerActivity.onLogoutResultFailure(0, "Logout failure");
        assertEquals(ShadowToast.getTextOfLatestToast(), "Logout failure");
    }

    @Test
    public void logoutResultSuccessTest() {
        assertEquals(((Label) hamburgerActivity.findViewById(R.id.hamburger_log_out)).getText().toString(), hamburgerActivity.getString(R.string.RA_Settings_Logout));
        TestAppFrameworkApplication testAppFrameworkApplication = (TestAppFrameworkApplication) RuntimeEnvironment.application;
        when(testAppFrameworkApplication.getUserRegistrationState().getUserObject(any(Context.class)).isUserSignIn()).thenReturn(false);
        hamburgerActivity.onLogoutResultSuccess();
        assertEquals(((Label) hamburgerActivity.findViewById(R.id.hamburger_log_out)).getText().toString(), hamburgerActivity.getString(R.string.RA_Settings_Login));
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        hamburgerActivityPresenter = null;
        hamburgerActivity = null;
        activityController = null;
    }
}