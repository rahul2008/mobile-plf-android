/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.consents.ConsentDetailType;

import java.util.ArrayList;
import java.util.List;

public class ConsentsPresenter {

    private final DBRequestListener dbRequestListener;

    ConsentsPresenter(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    protected boolean getConsentDetailStatus(ConsentDetail consentDetail) {

        if (consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
            return true;
        }
        return false;
    }

    public void updateConsent(List<ConsentDetail> consentDetails) {
        DataServicesManager.getInstance().updateConsentDetails(consentDetails, dbRequestListener);
    }

    public void saveDefaultConsentDetails() {
        DataServicesManager dataServicesManager = DataServicesManager.getInstance();
        List<ConsentDetail> consentDetails = new ArrayList<>();
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED, ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED, ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED, ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        dataServicesManager.saveConsentDetails(consentDetails, dbRequestListener);
    }
}
