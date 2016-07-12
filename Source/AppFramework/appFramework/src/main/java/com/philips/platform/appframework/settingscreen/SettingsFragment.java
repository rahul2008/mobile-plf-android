/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.appframework.settingscreen;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;

import java.util.ArrayList;

/**
 * SettingsFragment is the Base Class of all existing fragments.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public class SettingsFragment extends AppFrameworkBaseFragment {

    private static String TAG = SettingsFragment.class.getSimpleName();
    private ListViewSettings mAdapter = null;
    private ListView mList = null;
    private LogoutHandler mLogoutHandler = new LogoutHandler() {
        @Override
        public void onLogoutSuccess() {
            backstackFragment();
        }

        @Override
        public void onLogoutFailure(int i, String s) {

        }
    };

    private ArrayList<SettingScreenItem> buildSettingsScreenList() {
        ArrayList<SettingScreenItem> settingScreenItemList = new ArrayList<SettingScreenItem>();
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_main), SettingScreenItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_one), SettingScreenItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_two), SettingScreenItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_three), SettingScreenItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_notify), SettingScreenItemType.NOTIFICATION, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_purchases), SettingScreenItemType.HEADER, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_order_history), SettingScreenItemType.CONTENT, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_my_acc), SettingScreenItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_login), SettingScreenItemType.CONTENT, false));
        return settingScreenItemList;
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.settings_screen_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_settings, container, false);

        mList = (ListView) view.findViewById(R.id.listwithouticon);

        ArrayList<SettingScreenItem> settingScreenItemList = filterSettingScreenItemList(buildSettingsScreenList());
        mAdapter = new ListViewSettings(getActivity(), settingScreenItemList, mLogoutHandler);
        mList.setAdapter(mAdapter);

        return view;
    }

    private ArrayList<SettingScreenItem> filterSettingScreenItemList(ArrayList<SettingScreenItem> settingScreenItemList) {
        User user = new User(getActivity());

        if (user.isUserSignIn()) {
            return settingScreenItemList;
        }

        ArrayList<SettingScreenItem> newSettingScreenItemList = new ArrayList<SettingScreenItem>();
        for (int i = 0; i < settingScreenItemList.size(); i++) {
            if (!settingScreenItemList.get(i).userRegistrationRequired) {
                newSettingScreenItemList.add(settingScreenItemList.get(i));
            }
        }
        return newSettingScreenItemList;
    }

    private SettingScreenItem formDataSection(String settingsItem, SettingScreenItemType type, boolean userRegistrationRequired) {
        SettingScreenItem settingScreenItem = new SettingScreenItem();
        settingScreenItem.title = Html.fromHtml(settingsItem);
        settingScreenItem.type = type;
        settingScreenItem.userRegistrationRequired = userRegistrationRequired;
        return settingScreenItem;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
