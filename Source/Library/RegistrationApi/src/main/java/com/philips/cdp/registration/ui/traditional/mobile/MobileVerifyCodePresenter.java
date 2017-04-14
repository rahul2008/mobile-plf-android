package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.janrain.android.Jump;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegChinaConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.philips.cdp.registration.HttpClientService.HTTP_BODY_CONTENT;
import static com.philips.cdp.registration.HttpClientService.HTTP_RECEIVER;
import static com.philips.cdp.registration.HttpClientService.HTTP_SERVICE_REQUEST_CODE;
import static com.philips.cdp.registration.HttpClientService.HTTP_SERVICE_RESPONSE;
import static com.philips.cdp.registration.HttpClientService.HTTP_URL_TO_BE_CALLED;
import static com.philips.cdp.registration.ui.utils.RegConstants.SUCCESS_STATE_RESPONSE;
import static com.philips.cdp.registration.ui.utils.RegConstants.SUCCESS_STATE_RESPONSE_OK;

public class MobileVerifyCodePresenter implements HttpClientServiceReceiver.Listener, NetworStateListener {

    private static final long RESEND_DISABLED_DURATION = 60 * 1000;
    private static final long INTERVAL = 1 * 1000;
    private static final String VERIFICATION_SMS_CODE_SERVICE_ID = "userreg.urx.verificationsmscode";
    private static final int SMS_ACTIVATION_REQUEST_CODE = 100;
    private static final int RESEND_OTP_REQUEST_CODE = 101;
    private static final String ERROR_CODE = "errorCode";
    private static final String OTP_RESEND_SUCCESS = "0";

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileVerifyCodeContract mobileVerifyCodeContract;

    private final CountDownTimer resendTimer = new CountDownTimer(RESEND_DISABLED_DURATION, INTERVAL) {
        @Override
        public void onTick(long timeLeft) {
            String timeRemaining = String.format("%02d", + timeLeft / 1000) + "s";
            mobileVerifyCodeContract.updateResendTimer(timeRemaining);
        }

        @Override
        public void onFinish() {
            mobileVerifyCodeContract.enableResendButton();
        }
    };

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileVerifyCodePresenter(MobileVerifyCodeContract mobileVerifyCodeContract) {
        URInterface.getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
    }

    public void startResendTimer() {
        resendTimer.start();
    }

    public void verifyMobileNumber(String uuid, String otp) {
        Intent smsActivationIntent = createSMSActivationIntent(uuid, otp);
        mobileVerifyCodeContract.startService(smsActivationIntent);
    }

    private Intent createSMSActivationIntent(String uuid, String otp) {
        String verifiedMobileNumber = FieldsValidator.getVerifiedMobileNumber(uuid, otp);
        String url = "https://"+ Jump.getCaptureDomain()+"/access/useVerificationCode";

        String bodyContent = "verification_code=" + verifiedMobileNumber;
        RLog.i("MobileVerifyCodeFragment ", "verification_code" + verifiedMobileNumber);
        Intent httpServiceIntent = getHttpServiceIntent(url, bodyContent, SMS_ACTIVATION_REQUEST_CODE);
        return httpServiceIntent;
    }

    public void resendOTPRequest(final String mobileNumber) {

        Single<String> serviceUrl = serviceDiscoveryWrapper.getServiceUrlWithCountryPreferenceSingle(VERIFICATION_SMS_CODE_SERVICE_ID).cache();

        compositeDisposable.add(serviceUrl
                .subscribeOn(Schedulers.io())
                .map(url -> createResendSMSIntent(url, mobileNumber))
                .map(mobileVerifyCodeContract::startService)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ComponentName>() {
                    @Override
                    public void onSuccess(ComponentName value) {
                        /** NOP */
                    }

                    @Override
                    public void onError(Throwable e) {
                        mobileVerifyCodeContract.enableResendButton();
                    }
                }));
    }

    private Intent createResendSMSIntent(String verificationSmsCodeURL, String mobileNumber) {
        String url = getSmsVerificationUrl(verificationSmsCodeURL, mobileNumber);

        RequestBody emptyBody = RequestBody.create(null, new byte[0]);
        Intent httpServiceIntent = getHttpServiceIntent(url, emptyBody.toString(), RESEND_OTP_REQUEST_CODE);
        return httpServiceIntent;
    }

    @NonNull
    private Intent getHttpServiceIntent(String url, String value, int resendOtpRequestCode) {
        HttpClientServiceReceiver receiver = mobileVerifyCodeContract.getClientServiceRecevier();
        receiver.setListener(this);
        Intent httpServiceIntent = mobileVerifyCodeContract.getServiceIntent();
        httpServiceIntent.putExtra(HTTP_RECEIVER, receiver);
        httpServiceIntent.putExtra(HTTP_BODY_CONTENT, value);
        httpServiceIntent.putExtra(HTTP_URL_TO_BE_CALLED, url);
        httpServiceIntent.putExtra(HTTP_SERVICE_REQUEST_CODE, resendOtpRequestCode);
        return httpServiceIntent;
    }

    @NonNull
    private String getSmsVerificationUrl(String verificationSmsCodeURL, String mobileNumber) {
        return verificationSmsCodeURL +"?provider=" +
                "JANRAIN-CN&locale=zh_CN" + "&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber);
    }

    public void cleanUp() {
        compositeDisposable.clear();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String response = resultData.getString(HTTP_SERVICE_RESPONSE);
        if (response == null || response.isEmpty()) {
            mobileVerifyCodeContract.showSmsSendFailedError();
            return;
        }

        if(resultCode == SMS_ACTIVATION_REQUEST_CODE) {
            handleActivation(response);
        }

        if(resultCode == RESEND_OTP_REQUEST_CODE) {
            handleResendSms(response);
            startResendTimer();
        }
    }

    private void handleResendSms(String response) {
        try {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString(ERROR_CODE).toString().equals(OTP_RESEND_SUCCESS)) {
            mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
        } else {
            String errorCodeString = jsonObject.getString(ERROR_CODE).toString();
            mobileVerifyCodeContract.showSmsResendTechincalError(errorCodeString);
        }
        } catch (JSONException e) {
            mobileVerifyCodeContract.showSmsResendTechincalError("50");
        }
    }

    private void handleActivation(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(SUCCESS_STATE_RESPONSE).equals(SUCCESS_STATE_RESPONSE_OK)) {
                mobileVerifyCodeContract.refreshUserOnSmsVerificationSuccess();
            } else {
                mobileVerifyCodeContract.hideProgressSpinner();
                mobileVerifyCodeContract.enableVerifyButton();
                smsActivationFailed(jsonObject);
            }
        } catch (JSONException e) {
            mobileVerifyCodeContract.smsVerificationResponseError();
        }
    }

    private void smsActivationFailed(JSONObject jsonObject) throws JSONException {
        if (isResponseCodeValid(jsonObject)) {
            mobileVerifyCodeContract.setOtpInvalidErrorMessage();
        } else {
            String errorMessage = jsonObject.getString("error_description");
            mobileVerifyCodeContract.setOtpErrorMessageFromJson(errorMessage);
        }
        mobileVerifyCodeContract.showOtpInvalidError();
        mobileVerifyCodeContract.enableVerifyButton();
    }

    private boolean isResponseCodeValid(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("code").toString().equals(String.valueOf(RegChinaConstants.URXInvalidVerificationCode));
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if(isOnline) {
            mobileVerifyCodeContract.enableVerifyButton();
            mobileVerifyCodeContract.hideErrorMessage();
        } else {
            mobileVerifyCodeContract.disableVerifyButton();
            mobileVerifyCodeContract.showNoNetworkErrorMessage();
        }
    }


    @VisibleForTesting
    @Deprecated
    public void mockInjections(ServiceDiscoveryWrapper wrapper) {
        serviceDiscoveryWrapper = wrapper;
    }
}
