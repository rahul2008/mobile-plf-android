/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class UserCharacteristicsConverter {

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public UserCharacteristicsConverter() {
        DataServicesManager.getInstance().getAppComponent().injectUserCharacteristicsConverter(this);
    }

    //DataCore data type To Application type
    public List<Characteristics> convertToCharacteristics(UCoreUserCharacteristics uCoreUserCharacteristics, @NonNull final String creatorId) {

        List<Characteristics> characteristicsList = new ArrayList<>();

        if (uCoreUserCharacteristics != null && uCoreUserCharacteristics.getCharacteristics() != null) {
            for (int i = 0; i < uCoreUserCharacteristics.getCharacteristics().size(); i++) {
                String type = uCoreUserCharacteristics.getCharacteristics().get(i).getType();
                String value = uCoreUserCharacteristics.getCharacteristics().get(i).getValue();

                Characteristics characteristics = dataCreator.createCharacteristics(type, value);

                if (characteristics == null) return null;
                characteristicsList.add(characteristics);

                convertUCoreCharacteristicsToCharacteristicsDetailRecursively(characteristicsList, characteristics,
                        uCoreUserCharacteristics.getCharacteristics().get(i).getCharacteristics());

            }
        }

        return characteristicsList;

    }

    private void convertUCoreCharacteristicsToCharacteristicsDetailRecursively(List<Characteristics> characteristicsList, Characteristics parentCharacteristics, List<UCoreCharacteristics> uCoreCharacteristicsList) {
        if (uCoreCharacteristicsList.size() > 0) {
            for (int i = 0; i < uCoreCharacteristicsList.size(); i++) {
                String type = uCoreCharacteristicsList.get(i).getType();
                String value = uCoreCharacteristicsList.get(i).getValue();

                Characteristics childCharacteristics = dataCreator.createCharacteristics(type, value, parentCharacteristics);
                characteristicsList.add(childCharacteristics);
                convertUCoreCharacteristicsToCharacteristicsDetailRecursively(characteristicsList, childCharacteristics, uCoreCharacteristicsList.get(i).getCharacteristics());
            }
        }
    }

    //Application data type to DataCore type
    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<Characteristics> characteristicsList) {

        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();

        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        List<Characteristics> mCharacteristicsList;
        if (characteristicsList != null) {
            mCharacteristicsList = convertToCharacteristicDetail(characteristicsList);
            List<Characteristics> parentCharacteristicsList = new ArrayList<>();
            for (int i = 0; i < mCharacteristicsList.size(); i++) {
                if (mCharacteristicsList.get(i).getParent() == 0) {
                    parentCharacteristicsList.add(mCharacteristicsList.get(i)); 
                }
            }
            for (int i = 0; i < parentCharacteristicsList.size(); i++) {
                if (parentCharacteristicsList.size() > 0) {
                    UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                    uCoreCharacteristics.setType(parentCharacteristicsList.get(i).getType());
                    uCoreCharacteristics.setValue(parentCharacteristicsList.get(i).getValue());
                    uCoreCharacteristics.setCharacteristics(convertToUCoreCharacteristics(convertToCharacteristicDetail(parentCharacteristicsList.get(i).getCharacteristicsDetail())));
                    uCoreCharacteristicsList.add(uCoreCharacteristics);
                }
            }
        }
        uCoreUserCharacteristics.setCharacteristics(uCoreCharacteristicsList);
        return uCoreUserCharacteristics;
    }

    private List<UCoreCharacteristics> convertToUCoreCharacteristics(List<Characteristics> characteristicses) {
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristicses.size() > 0) {
            for (int i = 0; i < characteristicses.size(); i++) {
                List<Characteristics> characteristicsList = convertToCharacteristicDetail(characteristicses.get(i).getCharacteristicsDetail());
                UCoreCharacteristics characteristicsDetail = new UCoreCharacteristics();
                characteristicsDetail.setType(characteristicses.get(i).getType());
                characteristicsDetail.setValue(characteristicses.get(i).getValue());
                characteristicsDetail.setCharacteristics(convertToUCoreCharacteristics(characteristicsList));
                uCoreCharacteristicsList.add(characteristicsDetail);
            }
        }
        return uCoreCharacteristicsList;
    }

    private List<Characteristics> convertToCharacteristicDetail(Collection<? extends Characteristics> characteristicsDetails) {
        List<Characteristics> characteristicsList = new ArrayList<>();
        for (Characteristics detail : characteristicsDetails) {
            characteristicsList.add(detail);
        }
        return characteristicsList;
    }
}