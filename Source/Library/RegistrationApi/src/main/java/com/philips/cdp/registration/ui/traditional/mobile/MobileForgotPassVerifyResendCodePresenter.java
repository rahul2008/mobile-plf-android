package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;
import android.os.*;
import android.support.annotation.*;

import com.janrain.android.*;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.infra.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import org.json.*;

import javax.inject.*;

import io.reactivex.*;
import io.reactivex.android.schedulers.*;
import io.reactivex.disposables.*;
import io.reactivex.observers.*;
import io.reactivex.schedulers.*;

public class MobileForgotPassVerifyResendCodePresenter implements
        HttpClientServiceReceiver.Listener, NetworkStateListener {

    private static final String VERIFICATION_SMS_CODE_SERVICE_ID =
            "userreg.urx.verificationsmscode";
    private static final int RESEND_OTP_REQUEST_CODE = 101;
    private static final String ERROR_CODE = "errorCode";
    private static final String OTP_RESEND_SUCCESS = "0";
    private static final String ERROR_DESC = "error_description";
    private static final String ERROR_MSG = "message";

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

    public void resendOTPRequest(String serviceUrl,final String mobileNumber) {

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
        RLog.d("Configration : ", " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
        String url = verificationSmsCodeURL;

        Intent httpServiceIntent = mobileVerifyCodeContract.getServiceIntent();
        HttpClientServiceReceiver receiver = mobileVerifyCodeContract.getClientServiceRecevier();
        receiver.setListener(this);

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
        RLog.d("Configration : ", " envir :" + getClientId() + getRedirectUri());

        httpServiceIntent.putExtra(receiverKey, receiver);
        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
        httpServiceIntent.putExtra(urlKey, url);
        return httpServiceIntent;
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private String getRedirectUri() {

        return redirectUri;
    }

    String redirectUri;

    public void setRedirectUri(String uri){
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
                        AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR,
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

    //    mobileVerifyCodeContract.showSMSSentNotification();
        }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER Netowrk *** network: " + isOnline);

        if(isOnline) {
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
