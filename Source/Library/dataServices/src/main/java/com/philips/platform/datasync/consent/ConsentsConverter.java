package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;


import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
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
        DataServicesManager.getInstance().getAppComponant().injectConsentsConverter(this);
    }

    @NonNull
    public List<Consent> convertToAppConsentDetails(@NonNull final List<UCoreConsentDetail> uCoreConsentDetails) {

        List<Consent> consents =new ArrayList<>();
        for (UCoreConsentDetail uCoreConsentDetail : uCoreConsentDetails) {
            Consent consent = dataCreator.createConsent(uCoreConsentDetail.getName(), uCoreConsentDetail.getStatus(), uCoreConsentDetail.getDocumentVersion(), uCoreConsentDetail.getDeviceIdentificationNumber());
            consents.add(consent);
        }

        return consents;
    }

    @NonNull
    public List<UCoreConsentDetail> convertToUCoreConsentDetails(@NonNull final Collection<? extends Consent> consentDetails) {
        List<UCoreConsentDetail> uCoreConsentDetailList = new ArrayList<>();
        for (Consent consent : consentDetails) {
            String type = consent.getType();

            if (type != null) {
                UCoreConsentDetail uCoreConsentDetail = new UCoreConsentDetail(type,
                        consent.getStatus(), consent.getVersion(), consent.getDeviceIdentificationNumber());
                uCoreConsentDetailList.add(uCoreConsentDetail);
            }
        }
        return uCoreConsentDetailList;
    }

   }