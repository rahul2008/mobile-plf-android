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
                List<AppCharacteristics> appCharacteristics = mAppUserCharacteristics.getCharacteristics();
                errorMessage = populateCharacteristics(appCharacteristics, characteristicsList, null);
                if (errorMessage != null) {
                    return errorMessage;
                }
                mDataServicesManager.updateUserCharacteristics(characteristicsList, dbRequestListener);
            }
        } catch (JsonParseException exception) {
            return "Error parsing JSON";
        }
        return errorMessage;
    }

    private String populateCharacteristics(List<AppCharacteristics> appCharacteristics, List<Characteristics> characteristicsList, Characteristics parentCharacteristics){
        if (appCharacteristics != null) {
            for (AppCharacteristics appCharacteristic : appCharacteristics) {
                String type = appCharacteristic.getType();
                String value = appCharacteristic.getValue();
                if (type == null || value == null) {
                    return "type and value fields are mandatory in the json and it's case sensitive";
                }
                Characteristics characteristic = mDataServicesManager.createUserCharacteristics(type, value, parentCharacteristics);
                easterEggForTesters(type, value, characteristic);
                List<AppCharacteristics> childCharacteristics = appCharacteristic.getCharacteristics();
                String errorMessage = populateCharacteristics(childCharacteristics, characteristicsList, characteristic);
                if (errorMessage != null) {
                    return errorMessage;
                }
                characteristicsList.add(characteristic);
                if (parentCharacteristics != null) {
                    parentCharacteristics.setCharacteristicsDetail(characteristic);
                }
            }
        }
        return null;
    }

    private void easterEggForTesters(String type, String value, Characteristics characteristic) {
        if ((type.equals("test") && value.equals("test")) || (type.equals("type") && value.equals("value"))) {
            characteristic.setType("Moron");
            characteristic.setValue("Is that all your mind can come up with?");
        }
    }

    @Nullable
    private AppUserCharacteristics parseUserCharacteristics(String userCharacteristics) {
        try {
            return new Gson().fromJson(userCharacteristics, AppUserCharacteristics.class);
        } catch (Exception ex) {
            return null;
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
