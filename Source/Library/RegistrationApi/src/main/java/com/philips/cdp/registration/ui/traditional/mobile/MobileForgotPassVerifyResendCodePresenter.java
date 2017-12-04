package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.janrain.android.Jump;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MobileForgotPassVerifyResendCodePresenter implements
        HttpClientServiceReceiver.Listener, NetworkStateListener {

    String redirectUri;

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    private final MobileForgotPassVerifyResendCodeContract mobileVerifyCodeContract;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MobileForgotPassVerifyResendCodePresenter(
            MobileForgotPassVerifyResendCodeFragment mobileVerifyCodeContract) {
        URInterface.getComponent().inject(this);
        this.mobileVerifyCodeContract = mobileVerifyCodeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void resendOTPRequest(String serviceUrl, final String mobileNumber) {

        compositeDisposable.add(Single.just(serviceUrl)
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

        final String receiverKey = "receiver";
        final String bodyContentKey = "bodyContent";
        final String urlKey = "url";

        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + mobileNumber);
        RLog.d("Configration : ", " envir :" +
                RegistrationConfiguration.getInstance().getRegistrationEnvironment());
        String url = verificationSmsCodeURL;

        Intent httpServiceIntent = mobileVerifyCodeContract.getServiceIntent();
        HttpClientServiceReceiver receiver = mobileVerifyCodeContract.getClientServiceRecevier();
        receiver.setListener(this);

        String bodyContent = getBodyContent(mobileNumber);
        RLog.d("Configration : ", " envir :" + getClientId() + getRedirectUri());

        httpServiceIntent.putExtra(receiverKey, receiver);
        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
        httpServiceIntent.putExtra(urlKey, url);
        return httpServiceIntent;
    }

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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String response = resultData.getString("responseStr");

        if (response == null) {
            mobileVerifyCodeContract.showSmsSendFailedError();
            mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
            return;
        } else {
            handleResendSMSRespone(response);
        }
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
        RLog.i("MobileVerifyCodeFragment ", " isAccountActivate is " + token + " -- " + response);

        mobileVerifyCodeContract.updateToken(token);
    }

    private void handleResendSMSRespone(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("errorCode").toString().equals("0")) {
                mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
                mobileVerifyCodeContract.trackMultipleActionsOnMobileSuccess();
                handleResendVerificationEmailSuccess();
            } else {
                mobileVerifyCodeContract.trackVerifyActionStatus(
                        AppTagingConstants.SEND_DATA, AppTagingConstants.TECHNICAL_ERROR,
                        AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                mobileVerifyCodeContract.enableResendButtonAndHideSpinner();
                RLog.i("MobileVerifyCodeFragment ", " SMS Resend failure = " + response);
                mobileVerifyCodeContract.showSMSSpecifedError(jsonObject.getString("errorCode").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
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
