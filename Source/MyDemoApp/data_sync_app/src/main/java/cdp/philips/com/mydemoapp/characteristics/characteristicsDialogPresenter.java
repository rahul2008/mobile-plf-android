package cdp.philips.com.mydemoapp.characteristics;

import android.content.Context;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.trackers.DataServicesManager;

import cdp.philips.com.mydemoapp.consents.ConsentDetailType;

/**
 * Created by sangamesh on 16/11/16.
 */

public class CharacteristicsDialogPresenter {

    private final Context mContext;

    CharacteristicsDialogPresenter(Context mContext) {
        this.mContext = mContext;
    }

    protected void createUpdateCharacteristics() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        Characteristics characteristics = mDataServices.createCharacteristics();
        mDataServices.createCharacteristicsDetails(characteristics,"User","John");
        mDataServices.createCharacteristicsDetails(characteristics,"Mouth","UpperTeeth");
        mDataServices.updateCharacteristics(characteristics);
    }

}
