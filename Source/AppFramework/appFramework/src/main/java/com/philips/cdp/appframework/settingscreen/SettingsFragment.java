/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.appframework.settingscreen;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.appframework.AppFrameworkBaseFragment;
import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.utility.Logger;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SettingsFragment is the Base Class of all existing fragments.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public class SettingsFragment extends AppFrameworkBaseFragment {

    private ListViewSettings mAdapter = null;
    private ListView mList = null;
    private static String TAG = SettingsFragment.class.getSimpleName();

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
//        formObjectList();

        /*
        * Use asList method of Arrays class to convert Java String array to ArrayList
        */
//        ArrayList<String> settingsItemList = new ArrayList<String>(Arrays.asList(settingsItemArray));

//        User userRegistration = new User(getActivity());
//
//        if(!userRegistration.isUserSignIn()){
//            settingsItemList = filterListForRegistration(settingsItemList);
//        }

        mAdapter = new ListViewSettings(getActivity(), settingScreenItemList, mLogoutHandler);

//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey("ListViewWithoutIcons")) {
//                mAdapter.setSavedBundle(savedInstanceState.getBundle("ListViewWithoutIcons"));
//            }
//        }

        mList.setAdapter(mAdapter);

//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
//                Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_LONG).show();
//            }
//        });

        return view;
    }

    private LogoutHandler mLogoutHandler = new LogoutHandler() {
        @Override
        public void onLogoutSuccess() {
            backstackFragment();
        }

        @Override
        public void onLogoutFailure(int i, String s) {

        }
    };

    private ArrayList<SettingScreenItem> filterSettingScreenItemList(ArrayList<SettingScreenItem> settingScreenItemList) {
        User user = new User(getActivity());

        if(user.isUserSignIn()){
            return settingScreenItemList;
        }

        ArrayList<SettingScreenItem> newSettingScreenItemList = new ArrayList<SettingScreenItem>();
        for(int i = 0; i < settingScreenItemList.size(); i++){
            if(!settingScreenItemList.get(i).userRegistrationRequired){
                newSettingScreenItemList.add(settingScreenItemList.get(i));
            }
        }
        return newSettingScreenItemList;
    }

//    private void formObjectList() {
//        String[] settingsItemArray = getActivity().getResources().getStringArray(R.array.settingsScreen_list);
//
//        for (int i = 0; i < settingsItemArray.length; i++) {
//            switch (i) {
//                case "abc":
//                    formSection(settingsItemArray[i], SettingScreenItemType);
//                    break;
//                case 1:
//                    formSection(settingsItemArray, i);
//                    break;
//                case 2:
//                    formNotificationSection(settingsItemArray, i);
//                    break;
//            }
//        }
//    }

    private SettingScreenItem formDataSection(String settingsItem, SettingScreenItemType type, boolean userRegistrationRequired) {
        SettingScreenItem settingScreenItem = new SettingScreenItem();
        settingScreenItem.title = Html.fromHtml(settingsItem);
        settingScreenItem.type = type;
        settingScreenItem.userRegistrationRequired = userRegistrationRequired;
        return settingScreenItem;
    }

//    private void formNotificationSection(String[] settingsItemArray, int i) {
//        SettingScreenItem settingScreenItem = new SettingScreenItem();
//        settingScreenItem.title = settingsItemArray[i];
//        settingScreenItem.type = SettingScreenItemType.HEADER;
//        settingScreenItem.UserRegistrationRequired = false;
//    }
//
//    private ArrayList<String> filterListForRegistration(ArrayList<String> settingsItemList) {
//        ArrayList<String> userRegistrationDependentList = new ArrayList<String>();
//        userRegistrationDependentList.add(getString(R.string.settings_list_item_notify));
//        userRegistrationDependentList.add(getString(R.string.settings_list_item_purchases));
//        userRegistrationDependentList.add(getString(R.string.settings_list_item_order_history));
//
//        settingsItemList.removeAll(userRegistrationDependentList);
//
//        return settingsItemList;
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBundle("ListViewWithoutIcons", mAdapter.getSavedBundle());
    }
}
