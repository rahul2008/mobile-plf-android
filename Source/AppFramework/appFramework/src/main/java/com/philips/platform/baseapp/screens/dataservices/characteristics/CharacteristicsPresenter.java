package com.philips.platform.baseapp.screens.dataservices.characteristics;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.List;

import cdp.philips.com.mydemoapp.pojo.AppCharacteristics;
import cdp.philips.com.mydemoapp.pojo.AppUserCharacteristics;

/**
 * Created by indrajitkumar on 1/17/17.
 */

public class CharacteristicsPresenter {
    private DataServicesManager mDataServicesManager;
    private final DBRequestListener dbRequestListener;

    CharacteristicsPresenter(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
        mDataServicesManager = DataServicesManager.getInstance();
    }

    boolean createOrUpdateCharacteristics(String userCharacteristics) {
        try {
            AppUserCharacteristics mAppUserCharacteristics = parseUserCharacteristics(userCharacteristics);
            if (mAppUserCharacteristics == null || mAppUserCharacteristics.getCharacteristics() == null)
                return false;
            for (int i = 0; i < mAppUserCharacteristics.getCharacteristics().size(); i++) {
                String type = mAppUserCharacteristics.getCharacteristics().get(i).getType();
                String value = mAppUserCharacteristics.getCharacteristics().get(i).getValue();
                Characteristics characteristics = mDataServicesManager.createUserCharacteristics(type, value, null);
                saveUserCharacteristicsToLocalDBRecursively(characteristics, mAppUserCharacteristics.getCharacteristics().get(i).getCharacteristics());
            }
            mDataServicesManager.updateCharacteristics(dbRequestListener);
        } catch (JsonParseException exception) {
            return false;
        }
        return true;
    }

    @Nullable
    private AppUserCharacteristics parseUserCharacteristics(String userCharacteristics) {
        try {
            return new Gson().fromJson(userCharacteristics, AppUserCharacteristics.class);
        } catch (Exception ex) {
            DSLog.e(DSLog.LOG, "JSON Parser Exception =" + ex.getMessage());
            return null;
        }
    }

    private void saveUserCharacteristicsToLocalDBRecursively(Characteristics parentCharacteristics, List<AppCharacteristics> appCharacteristicsList) {
        if (appCharacteristicsList != null && appCharacteristicsList.size() > 0) {
            for (int i = 0; i < appCharacteristicsList.size(); i++) {
                String type = appCharacteristicsList.get(i).getType();
                String value = appCharacteristicsList.get(i).getValue();
                Characteristics childCharacteristics = mDataServicesManager.createUserCharacteristics(type, value, parentCharacteristics);
                parentCharacteristics.setCharacteristicsDetail(childCharacteristics);
                saveUserCharacteristicsToLocalDBRecursively(childCharacteristics, appCharacteristicsList.get(i).getCharacteristics());
            }
        }
    }
}
