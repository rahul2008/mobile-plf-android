/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;

import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.base.MyaBaseView;
import com.philips.platform.mya.base.MyaPresenterInterface;

import java.util.Map;

interface MyaProfileContract {

    interface View extends MyaBaseView {

        void showProfileItems(Map<String,String> profileList);

        void setUserName(String userName);

        void showPassedFragment(MyaBaseFragment fragment);
    }

    interface Presenter extends MyaPresenterInterface<View> {

        boolean handleOnClickProfileItem(String profileItem, Bundle bundle);

        void processUi(AppInfraInterface appInfra, Bundle arguments);
    }
}
