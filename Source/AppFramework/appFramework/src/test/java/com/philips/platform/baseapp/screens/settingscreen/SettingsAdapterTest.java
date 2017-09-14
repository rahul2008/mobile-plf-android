/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.settingscreen;

import android.text.Html;

import com.philips.cdp.uikit.customviews.UIKitButton;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class SettingsAdapterTest {
    private SettingsAdapter settingsAdapter = null;
    private HamburgerActivity hamburgerActivity = null;
    private ArrayList<SettingListItem> settingScreenItemList;
    private SettingsFragmentPresenter fragmentPresenter = null;
    private UIKitButton btn_settings_logout = null;
    private boolean userRegIsLoggedIn = false;
    private int LOGIN_VIEW = 0;
    private int VERTICAL_SETTING_VIEW = 1;

    @Mock
    private SettingsView settingsView;

    @Mock
    UserRegistrationSettingsState userRegistrationSettingsState;

    @Before
    public void setup() {
        initMocks(this);
        SettingsFragment settingsFragment = new SettingsFragment();

        hamburgerActivity = Robolectric.buildActivity(TestActivity.class).create().get();
        fragmentPresenter = new SettingsFragmentPresenter(settingsView);

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
        assertEquals(LOGIN_VIEW, settingsAdapter.getItemViewType(3));
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
    public void getViewLoginWhenRegistrationNotDone() throws Exception {
        settingsAdapter.getView(3, null, null);
        UIKitButton urButton = settingsAdapter.getUrButton();
        String logoutText = hamburgerActivity.getResources().getString(R.string.RA_Settings_Login);
        assertEquals(logoutText, urButton.getText());
    }

    @Test
    public void getViewLoginWhenRegistrationDone() throws Exception {
        userRegIsLoggedIn = true;
        settingsAdapter = new SettingsAdapter(hamburgerActivity, settingScreenItemList,
                fragmentPresenter, userRegistrationSettingsState, userRegIsLoggedIn, false);
        settingsAdapter.getView(3, null, null);
        UIKitButton urButton = settingsAdapter.getUrButton();
        String logoutText = hamburgerActivity.getResources().getString(R.string.RA_Settings_Logout);
        assertEquals(logoutText, urButton.getText());
    }


    @Test
    public void getViewNonLoginWhenRegistrationNotDoneHeader() throws Exception {
        settingsAdapter.getView(0, null, null);
        String headerString = String.valueOf(SettingListItemType.HEADER);
        assertEquals(settingsAdapter.getViewHolderTag(), headerString);
    }

    @Test
    public void getViewNonLoginWhenRegistrationNotDoneContent() throws Exception {
        settingsAdapter.getView(1, null, null);
        String headerString = String.valueOf(SettingListItemType.CONTENT);
        assertEquals(settingsAdapter.getViewHolderTag(), headerString);
    }

    @Test
    public void getViewNonLoginWhenRegistrationNotDoneNotification() throws Exception {
        settingsAdapter.getView(2, null, null);
        String headerString = String.valueOf(SettingListItemType.NOTIFICATION);
        assertEquals(settingsAdapter.getViewHolderTag(), headerString);
    }

    @Test
    public void isEnabled() throws Exception {

    }

    private ArrayList<SettingListItem> buildSettingsScreenList() {
        ArrayList<SettingListItem> settingScreenItemList = new ArrayList<SettingListItem>();
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Settings_Menu_Title), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Vertical_App_Setting_A), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Setting_Philips_Promo_Title_Only), SettingListItemType.NOTIFICATION, false));
        settingScreenItemList.add(formDataSection(getResourceString(R.string.RA_Settings_Login), SettingListItemType.CONTENT, false));
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