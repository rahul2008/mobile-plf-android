/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.util.mvp.MyaBaseView;
import com.philips.platform.mya.util.mvp.MyaPresenterInterface;

import java.util.TreeMap;

interface MyaProfileContract {

    interface View extends MyaBaseView {

        void showProfileItems(TreeMap<String,String> profileList);

        void setUserName(String userName);

    }

    interface Presenter extends MyaPresenterInterface<View> {
        void getProfileItems(Context context, AppInfraInterface appInfra);
    }
}
