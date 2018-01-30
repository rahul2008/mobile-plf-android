package com.philips.cdp.registration.ui.traditional.mobile;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.android.volley.Request;
import com.janrain.android.Jump;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MobileForgotPassVerifyResendCodePresenter implements NetworkStateListener {

    private static final String TAG = MobileForgotPassVerifyResendCodePresenter.class.getSimpleName();
    private String redirectUri;

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileForgotPassVerifyResendCodeContract mobileVerifyCodeContract;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileForgotPassVerifyResendCodePresenter(
            MobileForgotPassVerifyResendCodeFragment mobileVerifyCodeContract) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void resendOTPRequest(String serviceUrl, final String mobileNumber) {
//        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, serviceUrl, getBodyContent(mobileNumber), (String response) -> {
//            mobileVerifyCodeContract.onSuccessResponse(response);
//        }, error -> {
//            mobileVerifyCodeContract.onErrorResponse(error);
//        }, null, null, null);
//        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(urRestClientStringRequest);

        URRequest urRequest = new URRequest(Request.Method.POST, serviceUrl, getBodyContent(mobileNumber), mobileVerifyCodeContract::onSuccessResponse, mobileVerifyCodeContract::onErrorResponse);
        urRequest.makeRequest();

//        compositeDisposable.add(Single.just(serviceUrl)
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
//
//        final String receiverKey = "receiver";
//        final String bodyContentKey = "bodyContent";
//        final String urlKey = "url";
//
//        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + mobileNumber);
//        RLog.d("Configration : ", " envir :" +
//                RegistrationConfiguration.getInstance().getRegistrationEnvironment());
//        String url = verificationSmsCodeURL;
//
//        Intent httpServiceIntent = mobileVerifyCodeContract.getServiceIntent();
//        HttpClientServiceReceiver receiver = mobileVerifyCodeContract.getClientServiceRecevier();
//        receiver.setListener(this);
//
//        String bodyContent = getBodyContent(mobileNumber);
//        RLog.d("Configration : ", " envir :" + getClientId() + getRedirectUri());
//
//        httpServiceIntent.putExtra(receiverKey, receiver);
//        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
//        httpServiceIntent.putExtra(urlKey, url);
//        return httpServiceIntent;
//    }

    @NonNull
    private String getBodyContent(String mobileNumber) {
        return "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST +
                Jump.getCaptureDomain());
    }

    private String getRedirectUri() {

        return redirectUri;
    }


    public void setRedirectUri(String uri) {
        redirectUri = uri;
    }


    public void cleanUp() {
        compositeDisposable.clear();
    }

//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        String response = resultData.getString("responseStr");
//
//        if (response == null) {
//            mobileVerifyCodeContract.showSmsSendFailedError();
//            mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
//            return;
//        } else {
//            handleResendSMSRespone(response);
//        }
//        updateToken(response);
//    }

    void updateToken(String response) {
        JSONObject json = null;
        String payload = null;
        String token = null;
        try {
            json = new JSONObject(response);
            payload = json.getString("payload");
            json = new JSONObject(payload);
            token = json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RLog.d("MobileVerifyCodeFragment ", " isAccountActivate is " + token + " -- " + response);

        mobileVerifyCodeContract.updateToken(token);
    }

    void handleResendSMSRespone(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("errorCode").equals("0")) {
                mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
                mobileVerifyCodeContract.trackMultipleActionsOnMobileSuccess();
                handleResendVerificationEmailSuccess();
            } else {
                mobileVerifyCodeContract.trackVerifyActionStatus(
                        AppTagingConstants.SEND_DATA, AppTagingConstants.TECHNICAL_ERROR,
                        AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
                RLog.d("MobileVerifyCodeFragment ", " SMS Resend failure = " + response);
                mobileVerifyCodeContract.showSMSSpecifedError(jsonObject.getString("errorCode"));
            }
        } catch (JSONException e) {
            RLog.d(TAG, " handleResendSMSRespone is " + e.getMessage());
        }


    }

    private void handleResendVerificationEmailSuccess() {
        mobileVerifyCodeContract.trackVerifyActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
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
}
