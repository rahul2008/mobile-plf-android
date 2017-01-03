package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristic;
import com.philips.platform.core.trackers.DataServicesManager;

import javax.inject.Inject;

public class UserCharacteristicsConvertor {
    @NonNull
    private BaseAppDataCreator baseAppDataCreater;

    @Inject
    public UserCharacteristicsConvertor() {
        DataServicesManager manager = DataServicesManager.getInstance();
        this.baseAppDataCreater = manager.getDataCreater();
    }

    public UCoreUserCharacteristics convertToUCoreCharacteristics(Characteristic characteristic){
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();


        return null;
    }
}