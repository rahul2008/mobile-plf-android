/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.os.Bundle;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.philips.platform.mya.launcher.MyaInterface.MYA_LAUNCH_INPUT;
import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter {

    private MyaProfileContract.View view;

    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    private void setUserName(UserDataModelProvider userDataModelProvider) {
        if (userDataModelProvider != null) {
            UserDataModel userDataModel = (UserDataModel) userDataModelProvider.getData(DataModelType.USER);
            setUserModel(userDataModel);
        }
    }

    @Override
    public boolean handleOnClickProfileItem(String profileItem, Bundle bundle) {
        if (profileItem.equals(view.getContext().getString(R.string.MYA_My_details)) || profileItem.equalsIgnoreCase("MYA_My_details")) {
            MyaDetailsFragment myaDetailsFragment = getMyaDetailsFragment();
            myaDetailsFragment.setArguments(bundle);
            view.showPassedFragment(myaDetailsFragment);
            return true;
        }
        return false;
    }

    @Override
    public void processUi(AppInfraInterface appInfra, Bundle arguments) {
        List<?> list = null;
        if (arguments != null) {
            setUserName((UserDataModelProvider) arguments.getSerializable(USER_PLUGIN));
            MyaLaunchInput myaLaunchInput = (MyaLaunchInput) arguments.getSerializable(MYA_LAUNCH_INPUT);
            if (myaLaunchInput != null) {
                list = myaLaunchInput.getProfileConfigurableItems();
            }
        }
        if (list == null || list.isEmpty()) {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            String profileItems = "profile.menuItems";
            list = (ArrayList<?>) appInfra.getConfigInterface().getPropertyForKey
                    (profileItems, "mya", configError);
        }
        view.showProfileItems(getProfileList(list));
    }

    MyaDetailsFragment getMyaDetailsFragment() {
        return new MyaDetailsFragment();
    }

    private void setUserModel(UserDataModel userDataModel) {
        String givenName = userDataModel.getGivenName();
        String familyName = userDataModel.getFamilyName();
        if (!TextUtils.isEmpty(givenName) && !TextUtils.isEmpty(familyName) && !familyName.equalsIgnoreCase("null")) {
            view.setUserName(givenName.concat(" ").concat(familyName));
        } else if (!TextUtils.isEmpty(givenName)) {
            view.setUserName(givenName);
        }
    }

    private TreeMap<String, String> getProfileList(List<?> list) {
        try {
            TreeMap<String, String> treeMap = new TreeMap<>();
            MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
            if (list != null && list.size() != 0) {
                treeMap.put("MYA_My_details", view.getContext().getString(R.string.MYA_My_details));
                myaLocalizationHandler.getLocalisedList(view.getContext(), list, treeMap);
            } else {
                treeMap.put("MYA_My_details", view.getContext().getString(R.string.MYA_My_details));
            }
            return treeMap;
        } catch (IllegalArgumentException exception) {
            // TODO: Deepthi, use TLA while logging
            exception.getMessage();
        }
        return null;
    }

}
