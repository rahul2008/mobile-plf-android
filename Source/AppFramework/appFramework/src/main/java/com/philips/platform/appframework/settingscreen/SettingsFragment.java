/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.homescreen.HomeFragment;

import java.util.ArrayList;

public class SettingsFragment extends AppFrameworkBaseFragment {

    private static String TAG = SettingsFragment.class.getSimpleName();
    private SettingsAdapter mAdapter = null;
    private ListView mList = null;
    private HomeFragment mHomeFragment = null;

    private LogoutHandler mLogoutHandler = new LogoutHandler() {
        @Override
        public void onLogoutSuccess() {

            if(mHomeFragment == null) {
                mHomeFragment = new HomeFragment();
            }
            ((AppFrameworkBaseActivity)getActivity()).showFragment(mHomeFragment, mHomeFragment.getClass().getSimpleName());
        }

        @Override
        public void onLogoutFailure(int i, String s) {

        }
    };

    private ArrayList<SettingListItem> buildSettingsScreenList() {
        ArrayList<SettingListItem> settingScreenItemList = new ArrayList<SettingListItem>();
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_main), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_one), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_two), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_three), SettingListItemType.CONTENT, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_notify), SettingListItemType.NOTIFICATION, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_purchases), SettingListItemType.HEADER, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_order_history), SettingListItemType.CONTENT, true));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_my_acc), SettingListItemType.HEADER, false));
        settingScreenItemList.add(formDataSection(getString(R.string.settings_list_item_login), SettingListItemType.CONTENT, false));
        return settingScreenItemList;
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.settings_screen_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_settings_fragment, container, false);

        mList = (ListView) view.findViewById(R.id.listwithouticon);

        ArrayList<SettingListItem> settingScreenItemList = filterSettingScreenItemList(buildSettingsScreenList());
        mAdapter = new SettingsAdapter(getActivity(), settingScreenItemList, mLogoutHandler);
        mList.setAdapter(mAdapter);

        return view;
    }

    private ArrayList<SettingListItem> filterSettingScreenItemList(ArrayList<SettingListItem> settingScreenItemList) {
        User user = new User(getActivity());

        if (user.isUserSignIn()) {
            return settingScreenItemList;
        }

        ArrayList<SettingListItem> newSettingScreenItemList = new ArrayList<SettingListItem>();
        for (int i = 0; i < settingScreenItemList.size(); i++) {
            if (!settingScreenItemList.get(i).userRegistrationRequired) {
                newSettingScreenItemList.add(settingScreenItemList.get(i));
            }
        }
        return newSettingScreenItemList;
    }

    private SettingListItem formDataSection(String settingsItem, SettingListItemType type, boolean userRegistrationRequired) {
        SettingListItem settingScreenItem = new SettingListItem();
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
