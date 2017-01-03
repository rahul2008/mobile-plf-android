package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

import javax.inject.Inject;

public class UserCharacteristicsConvertor {
    @NonNull
    private BaseAppDataCreator baseAppDataCreater;

    @Inject
    public UserCharacteristicsConvertor() {
        DataServicesManager manager = DataServicesManager.getInstance();
        this.baseAppDataCreater = manager.getDataCreater();
    }

    public UCoreUserCharacteristics convertToUCoreCharacteristics(List<CharacteristicsDetail> characteristic){
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();

        return null;
       /* public List<UCoreCharacteristicsDetail> convertToUCoreCharacteristicsDetails(@NonNull final Collection<? extends CharacteristicsDetail> characteristicsDetails) {
            List<UCoreCharacteristicsDetail> uCoreCharacteristicsDetails = new ArrayList<>();
            for (CharacteristicsDetail characteristicsDetail : characteristicsDetails) {

                UCoreCharacteristicsDetail uCoreCharacteristicsDetail = new UCoreCharacteristicsDetail(characteristicsDetail.getType(), characteristicsDetail.getValue());
                uCoreCharacteristicsDetails.add(uCoreCharacteristicsDetail);

            }
            return uCoreCharacteristicsDetails;
        }*/
    }
}