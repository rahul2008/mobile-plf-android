/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class UserCharacteristicsConverter {

    private final Characteristics mCharacteristics;
    @Inject
    BaseAppDataCreator dataCreator;

    DataServicesManager mDataServicesManager;

    @Inject
    public UserCharacteristicsConverter() {
        mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.mAppComponent.injectUserCharacteristicsConverter(this);
        mCharacteristics = mDataServicesManager.createCharacteristics();
    }

    //DataCore data type To Application type
    public Characteristics convertToCharacteristics(UCoreUserCharacteristics uCoreUserCharacteristics) {
        for (int i = 0; i < uCoreUserCharacteristics.getCharacteristics().size(); i++) {
            String type = uCoreUserCharacteristics.getCharacteristics().get(i).getType();
            String value = uCoreUserCharacteristics.getCharacteristics().get(i).getValue();
            CharacteristicsDetail characteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, 0, null);
            convertUCoreCharacteristicsToCharacteristicsDetailRecursively(characteristicsDetail,
                    uCoreUserCharacteristics.getCharacteristics().get(i).getCharacteristics());
        }
        mDataServicesManager.updateCharacteristics(mCharacteristics);
        return mCharacteristics;

    }

    private void convertUCoreCharacteristicsToCharacteristicsDetailRecursively(CharacteristicsDetail parentCharacteristicsDetail, List<UCoreCharacteristics> characteristicsList) {
        if (characteristicsList.size() > 0) {
            for (int i = 0; i < characteristicsList.size(); i++) {
                String type = characteristicsList.get(i).getType();
                String value = characteristicsList.get(i).getValue();
                CharacteristicsDetail childCharacteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, 0, parentCharacteristicsDetail);
                parentCharacteristicsDetail.setCharacteristicsDetail(childCharacteristicsDetail);
                convertUCoreCharacteristicsToCharacteristicsDetailRecursively(childCharacteristicsDetail, characteristicsList.get(i).getCharacteristics());
            }
        }
    }

    //Application data type to DataCore type
    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<Characteristics> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        List<CharacteristicsDetail> mCharacteristicsDetailList;
        if (characteristic != null) {
            for (int i = 0; i < characteristic.size(); i++) {
                mCharacteristicsDetailList = convertToCharacteristicDetail(characteristic.get(i).getCharacteristicsDetails());
                UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                uCoreCharacteristics.setType(mCharacteristicsDetailList.get(i).getType());
                uCoreCharacteristics.setValue(mCharacteristicsDetailList.get(i).getValue());
                uCoreCharacteristics.setCharacteristics(convertToUCoreCharacteristics(convertToCharacteristicDetail(mCharacteristicsDetailList.get(i).getCharacteristicsDetail())));
                uCoreCharacteristicsList.add(uCoreCharacteristics);
            }
        }
        uCoreUserCharacteristics.setCharacteristics(uCoreCharacteristicsList);
        return uCoreUserCharacteristics;
    }

    private List<UCoreCharacteristics> convertToUCoreCharacteristics(List<CharacteristicsDetail> characteristicsDetails) {
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristicsDetails.size() > 0) {
            for (int i = 0; i < characteristicsDetails.size(); i++) {
                List<CharacteristicsDetail> characteristicsDetailList = convertToCharacteristicDetail(characteristicsDetails.get(i).getCharacteristicsDetail());
                UCoreCharacteristics characteristicsDetail = new UCoreCharacteristics();
                characteristicsDetail.setType(characteristicsDetails.get(i).getType());
                characteristicsDetail.setValue(characteristicsDetails.get(i).getValue());
                characteristicsDetail.setCharacteristics(convertToUCoreCharacteristics(characteristicsDetailList));
                uCoreCharacteristicsList.add(characteristicsDetail);
            }
        }
        return uCoreCharacteristicsList;
    }

    private List<CharacteristicsDetail> convertToCharacteristicDetail(Collection<? extends CharacteristicsDetail> characteristicsDetails) {
        List<CharacteristicsDetail> characteristicsDetailList = new ArrayList<>();
        for (CharacteristicsDetail detail : characteristicsDetails) {
            characteristicsDetailList.add(detail);
        }
        return characteristicsDetailList;
    }
}