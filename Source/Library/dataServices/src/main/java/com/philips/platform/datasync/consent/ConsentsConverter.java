package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;


import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentsConverter {

    @NonNull
    private final BaseAppDataCreator dataCreator;

    @Inject
    public ConsentsConverter() {
        DataServicesManager manager = DataServicesManager.getInstance();
        this.dataCreator = manager.getDataCreater();
    }

    @NonNull
    public Consent convertToAppConsentDetails(@NonNull final List<UCoreConsentDetail> uCoreConsentDetails, @NonNull final String creatorId) {
        Consent consent = dataCreator.createConsent(creatorId);
        for (UCoreConsentDetail uCoreConsentDetail : uCoreConsentDetails) {
            //Needs to be modified later
            ConsentDetail consentDetail = dataCreator.createConsentDetail(uCoreConsentDetail.getName(), uCoreConsentDetail.getStatus(), uCoreConsentDetail.getDocumentVersion(), uCoreConsentDetail.getDeviceIdentificationNumber(),false, consent);
            consent.addConsentDetails(consentDetail);
        }

        return consent;
    }

    @NonNull
    public List<UCoreConsentDetail> convertToUCoreConsentDetails(@NonNull final Collection<? extends ConsentDetail> consentDetails) {
        List<UCoreConsentDetail> uCoreConsentDetailList = new ArrayList<>();
        for (ConsentDetail consentDetail : consentDetails) {
            String type = consentDetail.getType();

            if (type != null) {
                UCoreConsentDetail uCoreConsentDetail = new UCoreConsentDetail(type,
                        consentDetail.getStatus(), consentDetail.getVersion(), consentDetail.getDeviceIdentificationNumber());
                uCoreConsentDetailList.add(uCoreConsentDetail);
            }
        }
        return uCoreConsentDetailList;
    }

    public List<String> getDocumentVersionList() {
        //final ConsentDetailType[] values = ConsentDetailType.values();
        List<String> documentVersionList = new ArrayList<>(3);
       /* for (int index = 0; index < values.length; index++) {
            documentVersionList.add(Consent.DEFAULT_DOCUMENT_VERSION);
        }*/
        documentVersionList.add(Consent.DEFAULT_DOCUMENT_VERSION);
        documentVersionList.add(Consent.DEFAULT_DOCUMENT_VERSION);
        documentVersionList.add(Consent.DEFAULT_DOCUMENT_VERSION);
        return documentVersionList;
    }

    public List<String> getDeviceIdentificationNumberList() {
       // final ConsentDetailType[] values = ConsentDetailType.values();
        List<String> deviceIdentificationNumberList = new ArrayList<>(3);
        deviceIdentificationNumberList.add(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
        deviceIdentificationNumberList.add(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
        deviceIdentificationNumberList.add(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        /*for (int index = 0; index < values.length; index++) {
            if (ConsentDetailType.ROOM_TEMPERATURE == values[index] || ConsentDetailType.RELATIVE_HUMIDITY == values[index]) {
                deviceIdentificationNumberList.add(Consent.SMART_BABY_MONITOR);
            } else {
                deviceIdentificationNumberList.add(Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
            }
        }*/
        return deviceIdentificationNumberList;
    }
}