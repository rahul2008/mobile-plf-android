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

    public UCoreUserCharacteristics convertToUCoreCharacteristics(List<CharacteristicsDetail> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();

        return null;
    }
}