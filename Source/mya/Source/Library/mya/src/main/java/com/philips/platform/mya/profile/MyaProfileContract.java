/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.base.mvp.MyaBaseView;
import com.philips.platform.mya.base.mvp.MyaPresenterInterface;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import java.util.Map;

interface MyaProfileContract {

    interface View extends MyaBaseView {

        void showProfileItems(Map<String,String> profileList);

        void setUserName(String userName);

        void showPassedFragment(Fragment fragment);

    }

    interface Presenter extends MyaPresenterInterface<View> {

        void getProfileItems(AppInfraInterface appInfra);

        void setUserName(UserDataModelProvider userDataModelProvider);

        boolean handleOnClickProfileItem(String profileItem, Bundle bundle);
    }
}
