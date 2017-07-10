package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;

/**
 * Created by philips on 6/28/17.
 */

public interface THSMedicationCallback {

    interface PTHGetMedicationCallback {
        void onGetMedicationReceived(THSMedication pTHMedication, SDKError sDKError );

    }



    interface PTHUpdateMedicationCallback {
        void onUpdateMedicationSent(Void pVoid, SDKError sDKError);

    }
}
