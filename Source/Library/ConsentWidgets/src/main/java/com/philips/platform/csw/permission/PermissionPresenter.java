package com.philips.platform.csw.permission;

import android.content.Context;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.utils.CswLogger;

import java.util.List;
import java.util.Locale;


public class PermissionPresenter implements ConsentResponseListener, CreateConsentListener {

    private PermissionInterface permissionInterface;
    private Context context;
    public static final int version = 0;

    public static final String CONSENT_TYPE_MOMENT_SYNC = "momentsync";
    public static final String CONSENT_TYPE_MOMENT = "moment";

    public PermissionPresenter(
            PermissionInterface permissionInterface, Context context) {
        this.permissionInterface = permissionInterface;
        this.context = context;
    }

    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        ConsentAccessToolKit.getInstance().
                getStatusForConsentType(CONSENT_TYPE_MOMENT, version, this);
    }

    void createConsentStatus(boolean status) {
        permissionInterface.showProgressDialog();
        ConsentStatus consentStatus = status ? ConsentStatus.active : ConsentStatus.rejected;
        Consent consent = new Consent(Locale.US, consentStatus, "moment", 0);   // locale, type and version come from ConsentDefinition
        ConsentAccessToolKit.getInstance().createConsent(consent, this);
    }

    @Override
    public void onResponseSuccessConsent(List<Consent> responseData) {
        if (responseData != null && !responseData.isEmpty()) {
            Consent consent = responseData.get(0);
            permissionInterface.hideProgressDialog();
            permissionInterface.updateSwitchStatus(consent.getStatus().equals(ConsentStatus.active));
        } else {
            permissionInterface.hideProgressDialog();
            permissionInterface.updateSwitchStatus(false);
            CswLogger.d(" Consent : ", "no consent for type found on server");
        }
    }

    @Override
    public int onResponseFailureConsent(int consentError) {
        permissionInterface.hideProgressDialog();
        CswLogger.d(" Consent : ", "fail  :" + consentError);
        return consentError;
    }

    @Override
    public void onSuccess(int code) {
        CswLogger.d(" Create Consent: ", "Success : " + code);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public int onFailure(int errCode) {
        CswLogger.d(" Create Consent: ", "Failed : " + errCode);
        permissionInterface.hideProgressDialog();
        return errCode;
    }
}
