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

    String createOrUpdateCharacteristics(String userCharacteristics) {
        List<Characteristics> characteristicsList = new ArrayList<>();
        String errorMessage;
        try {
            AppUserCharacteristics mAppUserCharacteristics = parseUserCharacteristics(userCharacteristics);
            errorMessage = isValidCharacteristic(mAppUserCharacteristics);
            if (errorMessage == null) {
                for (int i = 0; i < mAppUserCharacteristics.getCharacteristics().size(); i++) {
                    AppCharacteristics appCharacteristics = mAppUserCharacteristics.getCharacteristics().get(i);
                    errorMessage = createOrUpdateAppCharacteristics(appCharacteristics, characteristicsList);
                    if (errorMessage != null) { break; }
                }
                mDataServicesManager.updateUserCharacteristics(characteristicsList, dbRequestListener);
            }
        } catch (JsonParseException exception) {
            return "";
        }
        return errorMessage;
    }

    private String createOrUpdateAppCharacteristics(AppCharacteristics appCharacteristics, List<Characteristics> characteristicsList) {
        if (appCharacteristics != null) {
            String type = appCharacteristics.getType();
            String value = appCharacteristics.getValue();
            if (type == null || value == null) {
                return "type and value fields are mandatory in the json and it's case sensitive";
            }
            Characteristics characteristics = mDataServicesManager.createUserCharacteristics(type, value, null);
            characteristicsList.add(characteristics);
            saveUserCharacteristicsToLocalDBRecursively(characteristicsList, characteristics, appCharacteristics.getCharacteristics());
        }
        return null;
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

    private String isValidCharacteristic(AppUserCharacteristics mAppUserCharacteristics) {
        if (mAppUserCharacteristics == null) {
            return "The JSON you entered is invalid or too lengthy";
        } else if (mAppUserCharacteristics.getCharacteristics() == null) {
            return "'characteristics' is mandatory element";
        }

        return null;
    }
}
