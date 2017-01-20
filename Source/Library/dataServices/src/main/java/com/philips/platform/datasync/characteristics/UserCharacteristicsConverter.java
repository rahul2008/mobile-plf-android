/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class UserCharacteristicsConverter {

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public UserCharacteristicsConverter() {
        DataServicesManager.getInstance().getAppComponant().injectUserCharacteristicsConverter(this);
    }

    //DataCore data type To Application type
    public UserCharacteristics convertToCharacteristics(UCoreUserCharacteristics uCoreUserCharacteristics, @NonNull final String creatorId) {
        DSLog.d(DSLog.LOG, "Inder = Inside UC Converter");
        UserCharacteristics mUserCharacteristics = dataCreator.createCharacteristics(creatorId);
        for (int i = 0; i < uCoreUserCharacteristics.getCharacteristics().size(); i++) {
            String type = uCoreUserCharacteristics.getCharacteristics().get(i).getType();
            String value = uCoreUserCharacteristics.getCharacteristics().get(i).getValue();

            Characteristics characteristics = dataCreator.createCharacteristicsDetails(type, value, mUserCharacteristics);

            mUserCharacteristics.addCharacteristicsDetail(characteristics);

            convertUCoreCharacteristicsToCharacteristicsDetailRecursively(mUserCharacteristics, characteristics,
                    uCoreUserCharacteristics.getCharacteristics().get(i).getCharacteristics());

        }
        DSLog.d(DSLog.LOG, "Inder = Inside UC Converter mUserCharacteristics = " + mUserCharacteristics);
        return mUserCharacteristics;

    }

    private void convertUCoreCharacteristicsToCharacteristicsDetailRecursively(UserCharacteristics mUserCharacteristics, Characteristics parentCharacteristics, List<UCoreCharacteristics> characteristicsList) {
        if (characteristicsList.size() > 0) {
            for (int i = 0; i < characteristicsList.size(); i++) {
                String type = characteristicsList.get(i).getType();
                String value = characteristicsList.get(i).getValue();
                Characteristics childCharacteristics = dataCreator.createCharacteristicsDetails(type, value, mUserCharacteristics, parentCharacteristics);

                // parentCharacteristics.setCharacteristicsDetail(childCharacteristics);

                mUserCharacteristics.addCharacteristicsDetail(childCharacteristics);

                convertUCoreCharacteristicsToCharacteristicsDetailRecursively(mUserCharacteristics, childCharacteristics, characteristicsList.get(i).getCharacteristics());
            }
        }
    }

    //Application data type to DataCore type
    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<UserCharacteristics> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        List<Characteristics> mCharacteristicsList;
        if (characteristic != null) {
            for (int i = 0; i < characteristic.size(); i++) {
                mCharacteristicsList = convertToCharacteristicDetail(characteristic.get(i).getCharacteristicsDetails());
                if (mCharacteristicsList.size() > 0) {
                        UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                        uCoreCharacteristics.setType(mCharacteristicsList.get(i).getType());
                        uCoreCharacteristics.setValue(mCharacteristicsList.get(i).getValue());
                        uCoreCharacteristics.setCharacteristics(convertToUCoreCharacteristics(convertToCharacteristicDetail(mCharacteristicsList.get(i).getCharacteristicsDetail())));
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