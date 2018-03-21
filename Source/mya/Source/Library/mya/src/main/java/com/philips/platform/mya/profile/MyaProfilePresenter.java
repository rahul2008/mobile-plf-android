/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.os.Bundle;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter{

    private MyaProfileContract.View view;
    private UserDataInterface userDataInterface;
    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    private void setUserName(UserDataInterface userDataInterface) {
        if (userDataInterface != null) {
            ArrayList<String> list = new ArrayList<>();
            try {
                HashMap<String,Object> userMap = userDataInterface.getUserDetails(list);
                String givenName = (String) userMap.get(UserDetailConstants.GIVEN_NAME);
                String familyName = (String) userMap.get(UserDetailConstants.FAMILY_NAME);
                if (!TextUtils.isEmpty(givenName) && !TextUtils.isEmpty(familyName) && !familyName.equalsIgnoreCase("null")) {
                    view.setUserName(givenName.concat(" ").concat(familyName));
                } else if (!TextUtils.isEmpty(givenName)) {
                    view.setUserName(givenName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean handleOnClickProfileItem(String profileItem, Bundle bundle) {
        return false;
    }

    @Override
    public void getProfileItems(AppInfraInterface appInfra, Bundle arguments) {
        List<?> list = null;
        if (arguments != null) {
            userDataInterface = MyaHelper.getInstance().getUserDataInterface();
            setUserName(userDataInterface);
            list = MyaHelper.getInstance().getMyaLaunchInput().getProfileMenuList();
        }
        view.showProfileItems(getProfileList(list));
    }

    private TreeMap<String, String> getProfileList(List<?> list) {
        try {
            TreeMap<String, String> treeMap = new TreeMap<>();
            MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
            treeMap.put("MYA_My_details", view.getFragmentActivity().getString(R.string.MYA_My_details));
            if (list != null && list.size() != 0) {
                myaLocalizationHandler.getLocalisedList(view.getFragmentActivity(), list, treeMap);
            }
            return treeMap;
        } catch (IllegalArgumentException exception) {
            // TODO: Deepthi, use TLA while logging
            exception.getMessage();
        }
        return null;
    }
}
