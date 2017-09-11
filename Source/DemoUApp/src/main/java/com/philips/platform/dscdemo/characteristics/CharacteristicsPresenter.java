/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.characteristics;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.pojo.AppCharacteristics;
import com.philips.platform.dscdemo.pojo.AppUserCharacteristics;

import java.util.ArrayList;
import java.util.List;

class CharacteristicsPresenter {
    private DataServicesManager mDataServicesManager;
    private final DBRequestListener<Characteristics> dbRequestListener;

    CharacteristicsPresenter(DBRequestListener<Characteristics> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
        mDataServicesManager = DataServicesManager.getInstance();
    }

    boolean createOrUpdateCharacteristics(String userCharacteristics) {
        List<Characteristics> characteristicsList = new ArrayList<>();
        try {
            AppUserCharacteristics mAppUserCharacteristics = parseUserCharacteristics(userCharacteristics);
            if (mAppUserCharacteristics == null || mAppUserCharacteristics.getCharacteristics() == null)
                return false;
            for (int i = 0; i < mAppUserCharacteristics.getCharacteristics().size(); i++) {

                if (mAppUserCharacteristics.getCharacteristics().get(i) != null) {
                    String type = mAppUserCharacteristics.getCharacteristics().get(i).getType();
                    String value = mAppUserCharacteristics.getCharacteristics().get(i).getValue();
                    Characteristics characteristics = mDataServicesManager.createUserCharacteristics(type, value, null);
                    characteristicsList.add(characteristics);
                    saveUserCharacteristicsToLocalDBRecursively(characteristicsList, characteristics, mAppUserCharacteristics.getCharacteristics().get(i).getCharacteristics());
                }
            }
            mDataServicesManager.updateUserCharacteristics(characteristicsList, dbRequestListener);
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
            return null;
        }
    }

    private void saveUserCharacteristicsToLocalDBRecursively(List<Characteristics> parentCharacteristicsList, Characteristics parentCharacteristics, List<AppCharacteristics> appCharacteristicsList) {
        if (appCharacteristicsList != null && appCharacteristicsList.size() > 0) {
            for (int i = 0; i < appCharacteristicsList.size(); i++) {
                if (appCharacteristicsList.get(i) != null) {
                    String type = appCharacteristicsList.get(i).getType();
                    String value = appCharacteristicsList.get(i).getValue();
                    Characteristics childCharacteristics = mDataServicesManager.createUserCharacteristics(type, value, parentCharacteristics);
                    parentCharacteristicsList.add(childCharacteristics);
                    parentCharacteristics.setCharacteristicsDetail(childCharacteristics);
                    saveUserCharacteristicsToLocalDBRecursively(parentCharacteristicsList, childCharacteristics, appCharacteristicsList.get(i).getCharacteristics());
                }
            }
        }
    }
}
