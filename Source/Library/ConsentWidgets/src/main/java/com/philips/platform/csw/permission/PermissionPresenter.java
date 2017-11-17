package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.csw.utils.CswLogger;

import java.util.List;
import java.util.Locale;


public class PermissionPresenter implements ConsentResponseListener {

    private PermissionInterface permissionInterface;
    private ConsentInteractor consentInteractor;
    public static final int version = 0;

    PermissionPresenter(
            PermissionInterface permissionInterface, ConsentInteractor consentInteractor) {
        this.permissionInterface = permissionInterface;
        this.consentInteractor = consentInteractor;
    }


    void getConsentStatus() {
        permissionInterface.showProgressDialog();
        consentInteractor.getConsents(this);
    }

//    void createConsentStatus(boolean status) {
//        permissionInterface.showProgressDialog();
//        ConsentStatus consentStatus = status ? ConsentStatus.active : ConsentStatus.rejected;
//        Consent consent = new Consent(Locale.US, consentStatus, "moment", 0);   // locale, type and version come from ConsentDefinition
//        ConsentAccessToolKit.getInstance().createConsent(consent, this);
//    }

    @Override
    public void onResponseSuccessConsent(List<Consent> responseData) {
        permissionInterface.onConsentsRetrieved(responseData);
        permissionInterface.hideProgressDialog();
    }

    @Override
    public int onResponseFailureConsent(int consentError) {
        permissionInterface.hideProgressDialog();
        return 0;
    }

//    @Override
//    public void onSuccess(int code) {
//        CswLogger.d(" Create Consent: ", "Success : " + code);
//        permissionInterface.hideProgressDialog();
//    }
//
//    @Override
//    public int onFailure(int errCode) {
//        CswLogger.d(" Create Consent: ", "Failed : " + errCode);
//        permissionInterface.hideProgressDialog();
//        return errCode;
//    }
}
