package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
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
        DataServicesManager.getInstance().getAppComponent().injectConsentsConverter(this);
    }

    @NonNull
    public List<ConsentDetail> convertToAppConsentDetails(@NonNull final List<UCoreConsentDetail> uCoreConsentDetails) {

        List<ConsentDetail> consentDetails =new ArrayList<>();
        for (UCoreConsentDetail uCoreConsentDetail : uCoreConsentDetails) {
            ConsentDetail consentDetail = dataCreator.createConsentDetail(uCoreConsentDetail.getName(), uCoreConsentDetail.getStatus(), uCoreConsentDetail.getDocumentVersion(), uCoreConsentDetail.getDeviceIdentificationNumber());
            if(consentDetail==null) return null;
            consentDetails.add(consentDetail);
        }

        return consentDetails;
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