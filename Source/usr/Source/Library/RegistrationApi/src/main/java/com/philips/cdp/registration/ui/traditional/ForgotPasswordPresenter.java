package com.philips.cdp.registration.ui.traditional;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.janrain.android.Jump;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.mobile.MobileForgotPassVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by philips on 22/06/17.
 */

public class ForgotPasswordPresenter implements NetworkStateListener, EventListener,
        ForgotPasswordHandler {

    private static String TAG = ForgotPasswordPresenter.class.getSimpleName();


    private final RegistrationHelper registrationHelper;


    private final EventHelper eventHelper;

    @Inject
    ServiceDiscoveryWrapper serviceDiscoveryWrapper;

    public static final String USER_REQUEST_PASSWORD_RESET_SMS_CODE =
            "/api/v1/user/requestPasswordResetSmsCode";

    public static final String USER_REQUEST_RESET_PASSWORD_REDIRECT_URI_SMS =
            "/c-w/user-registration/apps/reset-password.html";

    private final ForgotPasswordContract forgotPasswordContract;

    private final CompositeDisposable disposable = new CompositeDisposable();

    String verificationSmsCodeURL;

    String resetPasswordSmsRedirectUri;

    String userId;

    Context context;

    public ForgotPasswordPresenter(
            RegistrationHelper registrationHelper, EventHelper eventHelper, ForgotPasswordContract forgotPasswordContract, Context context) {
//        User user1 = user;
        this.registrationHelper = registrationHelper;
        this.eventHelper = eventHelper;
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.forgotPasswordContract = forgotPasswordContract;
        this.context = context;
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(RLog.NETWORK_STATE, "CreateAccoutFragment :onNetWorkStateReceived : " + isOnline);
        forgotPasswordContract.handleUiState(isOnline);
    }

    public void registerListener() {
        registrationHelper.registerNetworkStateListener(this);
        eventHelper
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
    }

    public void unRegisterListener() {
        registrationHelper.unRegisterNetworkListener(this);
        eventHelper.unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
    }

    @Override
    public void onEventReceived(String event) {
        RLog.d(RLog.EVENT_LISTENERS, "ResetPasswordFragment :onCounterEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            forgotPasswordContract.handleUiStatus();
        }
    }

    @Override
    public void onSendForgotPasswordSuccess() {
        forgotPasswordContract.handleSendForgotPasswordSuccess();
    }

    @Override
    public void onSendForgotPasswordFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        forgotPasswordContract.handleSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
    }

    void forgotPasswordRequest(String userId, User user) {
        user.forgotPassword(userId, this);
    }

//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        String response = resultData.getString("responseStr");
//        RLog.d("MobileVerifyCodeFragment ", "onReceiveResult Response Val = " + response);
//        forgotPasswordContract.hideForgotPasswordSpinner();
//        if (response == null) {
//            forgotPasswordContract.forgotPasswordErrorMessage(
//                    context.getResources().getString(R.string.reg_Invalid_PhoneNumber_ErrorMsg));
//            return;
//        } else {
//            handleResendSMSRespone(response);
//        }
//    }

    void handleResendSMSRespone(String response) {

        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";

        try {
            JSONObject jsonObject = new JSONObject(response);
            if ("0".equals(jsonObject.getString("errorCode").toString())) {
                forgotPasswordContract.trackAction(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
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
                constructMobileVerifyCodeFragment(mobileNumberKey, tokenKey, redirectUriKey, verificationSmsCodeURLKey, token);
            } else {
                forgotPasswordContract.trackAction(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.TECHNICAL_ERROR, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                String errorMsg = RegChinaUtil.getErrorMsgDescription(jsonObject.getString("errorCode").toString(), context);
                forgotPasswordContract.forgotPasswordErrorMessage(errorMsg);
                RLog.d("MobileVerifyCodeFragment ", " SMS Resend failure = " + response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructMobileVerifyCodeFragment(String mobileNumberKey, String tokenKey, String redirectUriKey, String verificationSmsCodeURLKey, String token) {
        MobileForgotPassVerifyCodeFragment mobileForgotPasswordVerifyCodeFragment = new MobileForgotPassVerifyCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mobileNumberKey, userId);
        bundle.putString(tokenKey, token);
        bundle.putString(redirectUriKey, getRedirectUri());
        bundle.putString(verificationSmsCodeURLKey, verificationSmsCodeURL);
        mobileForgotPasswordVerifyCodeFragment.setArguments(bundle);
        forgotPasswordContract.addFragment(mobileForgotPasswordVerifyCodeFragment);
    }

//    public Intent createResendSMSIntent(String url) {
//
//        final String receiverKey = "receiver";
//        final String bodyContentKey = "bodyContent";
//        final String urlKey = "url";
//
//        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + userId);
//        RLog.d("Configration : ", " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
//
//        Intent httpServiceIntent = new Intent(context, HttpClientService.class);
//        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
//        receiver.setListener(this);
//
//        String bodyContent = getBodyContent();
//
//        RLog.d("Configration : ", " envirr :" + getClientId() + getRedirectUri());
//        httpServiceIntent.putExtra(receiverKey, receiver);
//        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
//        httpServiceIntent.putExtra(urlKey, url);
//        return httpServiceIntent;
//    }

    private String getBodyContent() {
        return "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(userId) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri=" + getRedirectUri();
    }

    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST + Jump.getCaptureDomain());
    }

    private String getRedirectUri() {
        return resetPasswordSmsRedirectUri;
    }

    void initateCreateResendSMSIntent(String userId) {
        this.userId = userId;
        String smsServiceID = "userreg.urx.verificationsmscode";

        RLog.d(RLog.SERVICE_DISCOVERY, " Country :" + RegistrationHelper.getInstance().getCountryCode());
        disposable.add(serviceDiscoveryWrapper.getServiceUrlWithCountryPreferenceSingle(smsServiceID)
                .map(serviceUrl -> getBaseUrl(serviceUrl))
                .map(baseUrl -> {
                    resetPasswordSmsRedirectUri = baseUrl + USER_REQUEST_RESET_PASSWORD_REDIRECT_URI_SMS;
                    verificationSmsCodeURL = baseUrl + USER_REQUEST_PASSWORD_RESET_SMS_CODE;
                    return verificationSmsCodeURL;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String verificationUrl) {
                        // forgotPasswordContract.intiateService(verificationUrl);
                        URRequest urRequest = new URRequest(Request.Method.POST, verificationUrl, getBodyContent(), forgotPasswordContract::onSuccessResponse, forgotPasswordContract::onErrorResponse);
                        urRequest.makeRequest();
//                        URRestClientStringRequest urRestClientStringRequest = new URRestClientStringRequest(Request.Method.POST, verificationUrl, getBodyContent(), (String response) -> {
//                            forgotPasswordContract.onSuccessResponse(response);
//                        }, error -> {
//                            forgotPasswordContract.onErrorResponse(error);
//                        }, null, null, null);
//                        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(urRestClientStringRequest);
                    }

                    @Override
                    public void onError(Throwable e) {
                        forgotPasswordContract.hideForgotPasswordSpinner();
                        RLog.d(TAG, "ForgotPasswordPresenter = " + e.getMessage());
                        forgotPasswordContract.forgotPasswordErrorMessage(
                                context.getString(R.string.reg_Generic_Network_Error));
                    }
                }));
    }

    @NonNull
    private String getBaseUrl(String serviceUrl) {
        String urlSeprator = "://";
        URL url = null;
        try {
            url = new URL(serviceUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return (url.getProtocol() + urlSeprator + url.getHost());
    }

    public void clearDisposable() {
        disposable.clear();
    }
}
