package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.android.volley.Request;
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
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class MobileVerifyResendCodePresenter implements NetworkStateListener {
    public static final String UPDATE_PROFILE_URL = "https://philips-cn-staging.capture.cn.janrain.com/oauth/update_profile_native";
    private String TAG = MobileVerifyResendCodePresenter.class.getSimpleName();
    private static final String VERIFICATION_SMS_CODE_SERVICE_ID = "userreg.urx.verificationsmscode";
    private static final int RESEND_OTP_REQUEST_CODE = 101;
    private static final String ERROR_CODE = "errorCode";
    private static final String OTP_RESEND_SUCCESS = "0";
    private static final int CHANGE_NUMBER_REQUEST_CODE = 102;
    private static final String ERROR_DESC = "error_description";
    private static final String ERROR_MSG = "message";
    private String mobileNumberStr;

    private static final String STAT = "stat";

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileVerifyResendCodeContract mobileVerifyCodeContract;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileVerifyResendCodePresenter(MobileVerifyResendCodeFragment mobileVerifyCodeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void resendOTPRequest(final String mobileNumber) {
        mobileVerifyCodeContract.disableResendButton();

        Single<String> serviceUrl = serviceDiscoveryWrapper.getServiceUrlWithCountryPreferenceSingle(VERIFICATION_SMS_CODE_SERVICE_ID).cache();

        final String string = serviceUrl.toString();
        RLog.d(TAG, "verifyClicked = " + string);
        RequestBody emptyBody = RequestBody.create(null, new byte[0]);
//        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, getSmsVerificationUrl(string, mobileNumber), emptyBody.toString(), mobileVerifyCodeContract::onSuccessResponse, mobileVerifyCodeContract::onErrorResponse, null, null, null);
//        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, getSmsVerificationUrl(string, mobileNumber), emptyBody.toString(), response -> mobileVerifyCodeContract.onSuccessResponse(RESEND_OTP_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse, null, null, null);
//        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(urRestClientStringRequest);

        URRequest urRequest = new URRequest(Request.Method.POST, getSmsVerificationUrl(string, mobileNumber), emptyBody.toString(), response -> mobileVerifyCodeContract.onSuccessResponse(RESEND_OTP_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse);
        urRequest.makeRequest();
//        compositeDisposable.add(serviceUrl
//                .subscribeOn(Schedulers.io())
//                .map(url -> createResendSMSIntent(url, mobileNumber))
//                .map(mobileVerifyCodeContract::startService)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<ComponentName>() {
//                    @Override
//                    public void onSuccess(ComponentName value) {
//                        /** NOP */
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mobileVerifyCodeContract.enableResendButton();
//                    }
//                }));
    }

//    private Intent createResendSMSIntent(String verificationSmsCodeURL, String mobileNumber) {
//        String url = getSmsVerificationUrl(verificationSmsCodeURL, mobileNumber);
//
//        RequestBody emptyBody = RequestBody.create(null, new byte[0]);
//        Intent httpServiceIntent = getHttpServiceIntent(url, emptyBody.toString(), RESEND_OTP_REQUEST_CODE);
//        return httpServiceIntent;
//    }

//    @NonNull
//    private Intent getHttpServiceIntent(String url, String value, int resendOtpRequestCode) {
//        HttpClientServiceReceiver receiver = mobileVerifyCodeContract.getClientServiceRecevier();
//        receiver.setListener(this);
//        Intent httpServiceIntent = mobileVerifyCodeContract.getServiceIntent();
//        httpServiceIntent.putExtra(HTTP_RECEIVER, receiver);
//        httpServiceIntent.putExtra(HTTP_BODY_CONTENT, value);
//        httpServiceIntent.putExtra(HTTP_URL_TO_BE_CALLED, url);
//        httpServiceIntent.putExtra(HTTP_SERVICE_REQUEST_CODE, resendOtpRequestCode);
//        return httpServiceIntent;
//    }

    @NonNull
    private String getSmsVerificationUrl(String verificationSmsCodeURL, String mobileNumber) {
        return verificationSmsCodeURL + "?provider=" +
                "JANRAIN-CN&locale=zh_CN" + "&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber);
    }

    public void cleanUp() {
        compositeDisposable.clear();
    }

//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        String response = resultData.getString(HTTP_SERVICE_RESPONSE);
//
//        if (response == null || response.isEmpty()) {
//            mobileVerifyCodeContract.hideProgressSpinner();
//            mobileVerifyCodeContract.showSmsSendFailedError();
//            return;
//        }
//
//        handleOnSuccess(resultCode, response);
//    }

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

    public void updatePhoneNumber(String mobilenumberURL, Context context) {
        mobileNumberStr = mobilenumberURL;
//        Intent smsActivationIntent = updatePhoneNumberIntent(mobilenumber,context);
//        mobileVerifyCodeContract.startService(smsActivationIntent);

//        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, "https://philips-cn-staging.capture.cn.janrain.com/oauth/update_profile_native", getUpdateMobileNUmberURL(mobilenumberURL), mobileVerifyCodeContract::onSuccessResponse, mobileVerifyCodeContract::onErrorResponse, null, null, null);
//        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, "https://philips-cn-staging.capture.cn.janrain.com/oauth/update_profile_native", getUpdateMobileNUmberURL(mobilenumberURL), response -> mobileVerifyCodeContract.onSuccessResponse(CHANGE_NUMBER_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse, null, null, null);
//        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(urRestClientStringRequest);

        URRequest urRequest = new URRequest(Request.Method.POST, UPDATE_PROFILE_URL, getUpdateMobileNUmberURL(mobilenumberURL), response -> mobileVerifyCodeContract.onSuccessResponse(CHANGE_NUMBER_REQUEST_CODE, response), mobileVerifyCodeContract::onErrorResponse);
        urRequest.makeRequest();
    }


//    private Intent updatePhoneNumberIntent(String mobilenumber, Context mContext) {
//
//        final String receiverKey="receiver";
//        final String bodyContentKey= "bodyContent";
//        final String urlKey= "url";
//
//        Intent httpServiceIntent = new Intent(mContext, HttpClientService.class);
//        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
//        receiver.setListener(this);
//
//        String bodyContent = getUpdateMobileNUmberURL(mobilenumber);
//        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER BODY *** : " + bodyContent);
//
//        httpServiceIntent.putExtra(receiverKey, receiver);
//        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
//        httpServiceIntent.putExtra(urlKey, "https://philips-cn-staging.capture.cn.janrain.com/oauth/update_profile_native");
//        httpServiceIntent.putExtra(HTTP_SERVICE_REQUEST_CODE, CHANGE_NUMBER_REQUEST_CODE);
//
//        return httpServiceIntent;
//    }

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
