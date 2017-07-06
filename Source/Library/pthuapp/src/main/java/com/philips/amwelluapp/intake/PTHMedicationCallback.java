package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Medication;

import java.util.List;

/**
 * Created by philips on 6/28/17.
 */

public interface PTHMedicationCallback {

    interface PTHGetMedicationCallback {
        void onGetMedicationReceived(PTHMedication pTHMedication, SDKError sDKError );

    }



    interface PTHUpdateMedicationCallback {
        void onUpdateMedicationSent(Void pVoid, SDKError sDKError);

    }
}
