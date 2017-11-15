package com.philips.platform.csw.permission;

import android.content.Context;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.response.ConsentStatus;
import com.philips.platform.csw.utils.CswLogger;

import java.util.List;


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
        ConsentAccessToolKit.getInstance().createConsent(consentStatus, this);
    }

    @Override
    public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
        if (responseData != null && !responseData.isEmpty()) {
            GetConsentsModel consentModel = responseData.get(0);
            permissionInterface.hideProgressDialog();
            permissionInterface.updateSwitchStatus(consentModel.getStatus().equals(ConsentStatus.active));
            CswLogger.d(" Consent : ", "getDateTime :" + consentModel.getDateTime());
            CswLogger.d(" Consent : ", "getLanguage :" + consentModel.getLanguage());
            CswLogger.d(" Consent : ", "status :" + consentModel.getStatus());
            CswLogger.d(" Consent : ", "policyRule :" + consentModel.getPolicyRule());
            CswLogger.d(" Consent : ", "Resource type :" + consentModel.getResourceType());
            CswLogger.d(" Consent : ", "subject  :" + consentModel.getSubject());
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
