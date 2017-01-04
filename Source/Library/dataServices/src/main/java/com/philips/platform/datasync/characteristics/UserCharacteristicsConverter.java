/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class UserCharacteristicsConverter {
    private List<CharacteristicsDetail> mCharacteristicsDetailList;

    @Inject
    public UserCharacteristicsConverter() {
    }

    public Characteristics convertToCharacteristics(){
        return null;
    }

    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<Characteristics> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();

        for (int i = 0; i < characteristic.size(); i++) {
            mCharacteristicsDetailList = convertToCharacteristicDetail(characteristic.get(i).getCharacteristicsDetails());
            UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
            uCoreCharacteristics.setType(mCharacteristicsDetailList.get(i).getType());
            uCoreCharacteristics.setValue(mCharacteristicsDetailList.get(i).getValue());
            uCoreCharacteristics.setCharacteristics(convertToUCoreCharacteristics(convertToCharacteristicDetail(mCharacteristicsDetailList.get(i).getCharacteristicsDetail())));
            uCoreCharacteristicsList.add(uCoreCharacteristics);
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