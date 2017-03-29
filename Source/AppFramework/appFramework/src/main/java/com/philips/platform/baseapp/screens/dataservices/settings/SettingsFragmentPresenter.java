package com.philips.platform.baseapp.screens.dataservices.settings;

import android.content.Context;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * Created by sangamesh on 09/01/17.
 */

public class SettingsFragmentPresenter {

    private final Context mContext;
    private final DBRequestListener dbRequestListener;
    public SettingsFragmentPresenter(Context mContext, DBRequestListener dbRequestListener) {
        this.mContext = mContext;
        this.dbRequestListener = dbRequestListener;
    }

    public void updateSettings(Settings settings) {
        DataServicesManager.getInstance().updateUserSettings(settings, dbRequestListener);
    }


}
