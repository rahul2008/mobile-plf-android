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
import android.widget.AdapterView;
import android.widget.ListView;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import java.util.ArrayList;

/**
 * Fragment is used for showing the account settings that verticals provide
 * In the current implementation this class has below features:
 *  1. Options for custom settings
 *  2. Option to launch IAP history (if logged in already )
 *  3. Option to logout of User Registration (if logged in already )
 *  4. Option to login and launch IAP (if not logged in )
 */
public class SettingsFragment extends AppFrameworkBaseFragment {

    private SettingsAdapter adapter = null;
    private ListView list = null;
    UIBasePresenter uiBasePresenter;
    public static final int logOutButton = 5555;
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private UserRegistrationState userRegistrationState;
    private LogoutHandler logoutHandler = new LogoutHandler() {
        @Override
        public void onLogoutSuccess() {
            uiBasePresenter = new SettingsFragmentPresenter();
            uiBasePresenter.onClick(logOutButton,getActivity());
            ((HomeActivity)getContext()).onGetCartCount(0);
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
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).updateActionBarIcon(false);
        ((HomeActivity)getActivity()).cartIconVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_settings_fragment, container, false);
        fragmentPresenter = new SettingsFragmentPresenter();
        list = (ListView) view.findViewById(R.id.listwithouticon);

        final ArrayList<SettingListItem> settingScreenItemList = filterSettingScreenItemList(buildSettingsScreenList());
        adapter = new SettingsAdapter(getActivity(), settingScreenItemList, logoutHandler, fragmentPresenter);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(settingScreenItemList.get(position).title.toString().equalsIgnoreCase(Html.fromHtml(getString(R.string.settings_list_item_order_history)).toString())){
                    fragmentPresenter.onClick(SettingsAdapter.iapHistoryLaunch, getActivity());
                }
            }
        });

        return view;
    }

    private ArrayList<SettingListItem> filterSettingScreenItemList(ArrayList<SettingListItem> settingScreenItemList) {
        userRegistrationState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        if (userRegistrationState.getUserObject(getActivity()).isUserSignIn()) {
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
