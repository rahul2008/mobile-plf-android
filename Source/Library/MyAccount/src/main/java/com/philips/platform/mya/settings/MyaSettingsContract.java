/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.base.mvp.MyaBaseView;
import com.philips.platform.mya.base.mvp.MyaPresenterInterface;

import java.util.LinkedHashMap;

interface MyaSettingsContract {

    interface View extends MyaBaseView {
        void showSettingsItems(LinkedHashMap<String, SettingsModel> dataModelLinkedHashMap);
        void showDialog(String title, String message);
        void handleLogOut();
    }

    interface Presenter extends MyaPresenterInterface<View> {

        void getSettingItems(Context context, AppInfraInterface appInfra);

        void onClickRecyclerItem(Context context, String key, SettingsModel settingsModel);

        void logOut(Bundle bundle);

        boolean handleOnClickSettingsItem(String key);
    }
}
