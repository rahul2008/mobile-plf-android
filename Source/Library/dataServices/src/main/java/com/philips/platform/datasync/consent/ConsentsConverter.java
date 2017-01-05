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

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public ConsentsConverter() {
        DataServicesManager.mAppComponent.injectConsentsConverter(this);
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

   }