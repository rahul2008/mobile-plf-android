package com.philips.platform.baseapp.screens.dataservices.consents;

import android.content.Context;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * Created by sangamesh on 16/11/16.
 */

public class ConsentDialogPresenter {

    private final Context mContext;

    ConsentDialogPresenter(Context mContext) {
        this.mContext = mContext;
    }

    protected boolean getConsentDetailStatus(ConsentDetail consentDetail) {

        return consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name());
    }

    protected void createSaveDefaultConsent() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        Consent consent = mDataServices.createConsent();
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER, true);
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,true);
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,true);
        mDataServices.save(consent);
    }
}
