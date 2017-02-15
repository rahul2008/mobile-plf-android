package com.philips.platform.baseapp.screens.dataservices.consents;

import android.content.Context;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

public class ConsentDialogPresenter {

    private final Context mContext;
    private final DBRequestListener dbRequestListener;

    ConsentDialogPresenter(Context mContext, DBRequestListener dbRequestListener) {
        this.mContext = mContext;
        this.dbRequestListener = dbRequestListener;
    }

    protected boolean getConsentDetailStatus(ConsentDetail consentDetail) {

        if (consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
            return true;
        }
        return false;
    }

    public void updateConsent(List<ConsentDetail> consentDetails) {
        DataServicesManager.getInstance().updateConsentDetails(consentDetails,dbRequestListener);
    }
}
