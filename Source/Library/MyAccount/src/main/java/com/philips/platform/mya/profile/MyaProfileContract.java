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
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.TreeMap;

interface MyaProfileContract {

    interface View extends MyaBaseView {

        void showProfileItems(TreeMap<String,String> profileList);

        void setUserName(String userName);

        void showPassedFragment(Fragment fragment, FragmentLauncher fragmentLauncher);

    }

    interface Presenter extends MyaPresenterInterface<View> {

        void getProfileItems(AppInfraInterface appInfra);

        void setUserName(Bundle bundle);

        boolean handleOnClickProfileItem(String profileItem, Bundle bundle);
    }
}
