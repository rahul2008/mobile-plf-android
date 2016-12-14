package cdp.philips.com.mydemoapp.characteristics;

import android.content.Context;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.trackers.DataServicesManager;

import cdp.philips.com.mydemoapp.consents.ConsentDetailType;

/**
 * Created by sangamesh on 16/11/16.
 */

class CharacteristicsDialogPresenter {

    private final Context mContext;

    CharacteristicsDialogPresenter(Context mContext) {
        this.mContext = mContext;
    }

    protected void createUpdateCharacteristics() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        Characteristics characteristics = mDataServices.createCharacteristics();
        CharacteristicsDetail characteristicsDetails = mDataServices.createCharacteristicsDetails(characteristics, "User", "John", 0,null);
        CharacteristicsDetail characteristicsDetails1 = mDataServices.createCharacteristicsDetails(characteristics, "Mouth", "UpperTeeth", 0,characteristicsDetails);
        CharacteristicsDetail characteristicsDetails2 = mDataServices.createCharacteristicsDetails(characteristics, "BrokenTeeth", "1,2,3,4,5", 0,characteristicsDetails1);

        CharacteristicsDetail characteristicsDetails3 = mDataServices.createCharacteristicsDetails(characteristics, "Mouth", "LowerTeeth", 0,characteristicsDetails);
        CharacteristicsDetail characteristicsDetails4 = mDataServices.createCharacteristicsDetails(characteristics, "BrokenTeeth", "6,7,8,9", 0,characteristicsDetails3);


       /* "type": "BrokenTeeth",
                "value": "1,2,3,4,5",*/
        mDataServices.updateCharacteristics(characteristics);
    }

}
