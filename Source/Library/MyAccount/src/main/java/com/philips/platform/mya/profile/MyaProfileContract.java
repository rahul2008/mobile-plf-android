/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.util.mvp.MyaBaseView;
import com.philips.platform.mya.util.mvp.MyaPresenterInterface;

import java.util.List;

interface MyaProfileContract {

    interface View extends MyaBaseView {

        void showProfileItems(List<String> profileList);

        void setUserName(String userName);

    }

    interface Presenter extends MyaPresenterInterface<View> {

        void getProfileItems(Context context, AppInfraInterface appInfra);

        List<String> getAppConfigProfileItems(Context context, AppConfigurationInterface appConfigurationManager);
    }
}
