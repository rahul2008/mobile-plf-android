/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.consents;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.List;

class ConsentPresenter {

    private final DBRequestListener<ConsentDetail> dbRequestListener;

    ConsentPresenter(DBRequestListener<ConsentDetail> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    boolean getConsentDetailStatus(ConsentDetail consentDetail) {
        return consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name());
    }

    void updateConsent(List<ConsentDetail> consentDetails) {
        DataServicesManager.getInstance().updateConsentDetails(consentDetails, dbRequestListener);
    }

    void saveDefaultConsentDetails() {
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
