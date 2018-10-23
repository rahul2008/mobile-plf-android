/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;

public interface THSMedicationCallback {

    interface PTHGetMedicationCallback {
        void onGetMedicationReceived(THSMedication pTHMedication, SDKError sDKError);
        void onFailure(Throwable throwable);

    }


    interface PTHUpdateMedicationCallback {
        void onUpdateMedicationSent(Void pVoid, SDKError sDKError);
        void onFailure(Throwable throwable);

    }
}
