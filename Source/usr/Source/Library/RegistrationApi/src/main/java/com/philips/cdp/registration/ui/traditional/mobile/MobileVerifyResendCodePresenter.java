package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.janrain.android.Jump;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import java.net.URL;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MobileVerifyResendCodePresenter implements NetworkStateListener {
    private String TAG = MobileVerifyResendCodePresenter.class.getSimpleName();
    private static final String VERIFICATION_SMS_CODE_SERVICE_ID = "userreg.urx.verificationsmscode";
    private static final String BASE_URL_CODE_SERVICE_ID = "userreg.janrain.api";

    private static final int RESEND_OTP_REQUEST_CODE = 101;
    private static final String ERROR_CODE = "errorCode";
    private static final String OTP_RESEND_SUCCESS = "0";
    private static final int CHANGE_NUMBER_REQUEST_CODE = 102;

    private static final String STAT = "stat";

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    private final MobileVerifyResendCodeContract mobileVerifyCodeContract;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileVerifyResendCodePresenter(MobileVerifyResendCodeFragment mobileVerifyCodeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }


    private void getURLFromServiceDiscoveryAndRequestVerificationCode(String mobileNumber) {
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(VERIFICATION_SMS_CODE_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                RLog.d(TAG, VERIFICATION_SMS_CODE_SERVICE_ID + " URL is " + url);
                URRequest urRequest = new URRequest(getSmsVerificationUrl(url.toString(), mobileNumber), null, "application/json; charset=UTF-8"
                        , response -> mobileVerifyCodeContract.onSuccessResponse(RESEND_OTP_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse);
                urRequest.makeRequest();
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                RLog.d(TAG, error.name() + "and error message is " + message);
                mobileVerifyCodeContract.enableResendButton();
            }
        });
    }

    void resendOTPRequest(final String mobileNumber) {
        getURLFromServiceDiscoveryAndRequestVerificationCode(mobileNumber);
    }

    @NonNull
    private String getSmsVerificationUrl(String verificationSmsCodeURL, String mobileNumber) {
        RLog.d(TAG, verificationSmsCodeURL + "?provider=" +
                "JANRAIN-CN&locale=zh_CN" + "&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber));
        return verificationSmsCodeURL + "?provider=" +
                "JANRAIN-CN&locale=zh_CN" + "&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber);
    }

    public void cleanUp() {
        compositeDisposable.clear();
    }

    void handleOnSuccess(int resultCode, String response) {
        if (resultCode == RESEND_OTP_REQUEST_CODE) {
            mobileVerifyCodeContract.hideProgressSpinner();
            handleResendSms(response);
        } else if (resultCode == CHANGE_NUMBER_REQUEST_CODE) {
            RLog.d("CHANGE_NUMBER_REQUEST_CODE", "CHANGE_NUMBER_REQUEST_CODE" + response);
            handlePhoneNumberChange(response);
        } else {
            mobileVerifyCodeContract.hideProgressSpinner();
        }
    }

    private void handlePhoneNumberChange(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            RLog.d("CHANGE_NUMBER_REQUEST_CODE", "CHANGE_NUMBER_REQUEST_STAT " + jsonObject.get(STAT));

            if (jsonObject.get(STAT).equals("ok")) {
                RLog.d("CHANGE_NUMBER_REQUEST_CODE", "CHANGE_NUMBER_REQUEST_CODE" + response);
                mobileVerifyCodeContract.refreshUser();
                //      mobileVerifyCodeContract.enableResendButton();
            } else {
                mobileVerifyCodeContract.hideProgressSpinner();

                mobileVerifyCodeContract.showNumberChangeTechincalError(jsonObject.getString("errorCode"));
            }
        } catch (Exception e) {
            mobileVerifyCodeContract.hideProgressSpinner();

            mobileVerifyCodeContract.showSmsSendFailedError();

        }
    }


    private void handleResendSms(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.getString(ERROR_CODE).equals(OTP_RESEND_SUCCESS)) {
                mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
            } else {
                mobileVerifyCodeContract.showNumberChangeTechincalError(jsonObject.getString("errorCode"));
            }
        } catch (Exception e) {
            mobileVerifyCodeContract.showSmsSendFailedError();
        }
    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER Netowrk *** network: " + isOnline);

        if (isOnline) {
            mobileVerifyCodeContract.netWorkStateOnlineUiHandle();
        } else {
            mobileVerifyCodeContract.netWorkStateOfflineUiHandle();
        }
    }


    @VisibleForTesting
    @Deprecated
    public void mockInjections(ServiceDiscoveryWrapper wrapper) {
        serviceDiscoveryWrapper = wrapper;
    }


    private void initServiceDiscoveryForUpdateMobilenumber(String mobilenumberURL) {
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(BASE_URL_CODE_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onSuccess(URL url) {
                RLog.d(TAG, BASE_URL_CODE_SERVICE_ID + " URL is " + url);
                URRequest urRequest = new URRequest(url + "/oauth/update_profile_native", getUpdateMobileNUmberURL(mobilenumberURL), null, response -> mobileVerifyCodeContract.onSuccessResponse(CHANGE_NUMBER_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse);
                urRequest.makeRequest();
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                RLog.d(TAG, error.name() + "and error message is " + message);
                mobileVerifyCodeContract.enableUpdateButton();
            }
        });
    }

    void updatePhoneNumber(String mobilenumberURL) {
        initServiceDiscoveryForUpdateMobilenumber(mobilenumberURL);
    }

    private String getUpdateMobileNUmberURL(String mobilenumber) {
        return "client_id=" + getClientId() + "&locale=zh-CN&response_type=token&form=mobileNumberForm&flow=standard&" +
                "flow_version=" + Jump.getCaptureFlowVersion() + "&token=" + getAccessToken() +
                "&mobileNumberConfirm=" + mobilenumber + "&mobileNumber=" + mobilenumber;
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private String getAccessToken() {
        return Jump.getSignedInUser() != null ? Jump.getSignedInUser()
                .getAccessToken() : null;
    }

}
