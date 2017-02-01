/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.settingscreen;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.UIKitButton;
import com.philips.platform.GradleRunner;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.homescreen.TestAppFrameworkApplication;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GradleRunner.class)
@Config(constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class SettingsAdapterTest {
    private SettingsAdapter settingsAdapter = null;
//    private SettingsFragment settingsFragment = null;
//    private SettingsAdapter adapter = null;
    private HamburgerActivity hamburgerActivity = null;
    private ArrayList<SettingListItem> settingScreenItemList;
    private SettingsFragmentPresenter fragmentPresenter = null;
    private UserRegistrationSettingsState userRegistrationSettingsState = null;
    private UIKitButton btn_settings_logout = null;
    private boolean userRegIsLoggedIn = false;
    private int LOGIN_VIEW = 0;
    private int VERTICAL_SETTING_VIEW = 1;

    private SettingsView settingsView = new SettingsView() {
        @Override
        public void finishActivityAffinity() {

        }

        @Override
        public ActionBarListener getActionBarListener() {
            return null;
        }

        @Override
        public int getContainerId() {
            return 0;
        }

        @Override
        public FragmentActivity getFragmentActivity() {
            return null;
        }
    };

    @Before
    public void setup() {


        //mocking
//        SettingsFragment settingsFragment = mock(SettingsFragment.class);

        SettingsFragment settingsFragment = new SettingsFragment();

        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().get();
        fragmentPresenter = new SettingsFragmentPresenter(settingsView);
        userRegistrationSettingsState = new UserRegistrationSettingsState();
        settingScreenItemList = settingsFragment.filterSettingScreenItemList(buildSettingsScreenList());
        settingsAdapter = new SettingsAdapter(hamburgerActivity, settingScreenItemList,
                fragmentPresenter, userRegistrationSettingsState, userRegIsLoggedIn, false);
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(settingsAdapter.getCount(), settingScreenItemList.size());
    }

    @Test
    public void getItem() throws Exception {
        assertEquals(VERTICAL_SETTING_VIEW, settingsAdapter.getItemViewType(0));
    }

    @Test
    public void getItemLoginView() throws Exception {
        assertEquals(LOGIN_VIEW, settingsAdapter.getItemViewType(5));
    }

    @Test
    public void getItemId() throws Exception {
        assertEquals(0, settingsAdapter.getItemId(0));
    }

    @Test
    public void getViewTypeCount() throws Exception {
        assertEquals(2, settingsAdapter.getViewTypeCount());
    }

    @Test
    public void getViewLoginWhenRegistrationIsDone() throws Exception {
//        settingsAdapter.getView(5, null, null);
//        UIKitButton urButton = settingsAdapter.getUrButton();
//        String logoutText = hamburgerActivity.getResources().getString(R.string.settings_list_item_log_out);
//        assertEquals(logoutText, urButton.getText());
    }

    @Test
    public void getViewLoginWhenRegistrationNotDone() throws Exception {
//        userRegIsLoggedIn = true;
//        settingsAdapter = new SettingsAdapter(hamburgerActivity, settingScreenItemList,
//                fragmentPresenter, userRegistrationSettingsState, userRegIsLoggedIn, false);
//        settingsAdapter.getView(5, null, null);
//        UIKitButton urButton = settingsAdapter.getUrButton();
//        String logoutText = hamburgerActivity.getResources().getString(R.string.settings_list_item_login);
//        assertEquals(logoutText, urButton.getText());
    }

//    @Test
//    public void getViewNoLoginWhenRegistrationIsDone() throws Exception {
//        settingsAdapter.getView(0, null, null);
//        UIKitButton urButton = settingsAdapter.getUrButton();
//        String logoutText = hamburgerActivity.getResources().getString(R.string.settings_list_item_log_out);
//        assertEquals(logoutText, urButton.getText());
//    }

    @Test
    public void getViewNoLoginWhenRegistrationNotDone() throws Exception {
//        userRegIsLoggedIn = true;
//        SettingsAdapter settingsAdapterNew = new SettingsAdapter(hamburgerActivity, settingScreenItemList,
//                fragmentPresenter, userRegistrationSettingsState, userRegIsLoggedIn, false);
//        settingsAdapterNew.getView(0, null, null);
//        UIKitButton urButton = settingsAdapterNew.getUrButton();
//        String logoutText = hamburgerActivity.getResources().getString(R.string.settings_list_item_login);
//        assertEquals(logoutText, urButton.getText());
    }


    @Test
    public void isEnabled() throws Exception {

    }

    private ArrayList<SettingListItem> buildSettingsScreenList() {
        ArrayList<SettingListItem> settingScreenItemList = new ArrayList<SettingListItem>();
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_main), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_one), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_two), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_three), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_notify), SettingListItemType.NOTIFICATION, true));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_my_acc), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.settings_list_item_login), SettingListItemType.CONTENT, false));
        return settingScreenItemList;
    }

    private String getResourceString(int resId) {
        return hamburgerActivity.getApplicationContext().getString(resId);
    }

    /*
   * 'Android N' doesn't support single parameter in "Html.fromHtml". So adding the if..else condition and
   * suppressing "deprecation" for 'else' block.
   */
    @SuppressWarnings("deprecation")
    private SettingListItem formDataSection(String settingsItem, SettingListItemType type, boolean userRegistrationRequired) {
        SettingListItem settingScreenItem = new SettingListItem();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            settingScreenItem.title = Html.fromHtml(settingsItem, Html.FROM_HTML_MODE_LEGACY);
        }
        else{
            settingScreenItem.title = Html.fromHtml(settingsItem);
        }

        settingScreenItem.type = type;
        settingScreenItem.userRegistrationRequired = userRegistrationRequired;
        return settingScreenItem;
    }
}