package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;

import static com.philips.cdp.registration.ui.utils.ThreadUtils.postInMainThread;

public class BaseHSDPLogin {
    private static final String TAG = BaseHSDPLogin.class.getSimpleName();
    private Context mContext;

    public BaseHSDPLogin(Context mContext) {
        this.mContext = mContext;
    }

    public String getUserEmailOrMobile(User user) {
        String emailorMobile;
        if (FieldsValidator.isValidEmail(user.getEmail())) {
            emailorMobile = user.getEmail();
        } else {
            emailorMobile = user.getMobile();
        }
        return emailorMobile;
    }

    public void hsdpLogin(String accessToken, String emailorMobile, TraditionalLoginHandler mTraditionalLoginHandler) {
        RLog.d(TAG, "hsdpLogin : with TraditionalLoginHandler");
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.login(emailorMobile, accessToken, Jump.getRefreshSecret(), new SocialLoginHandler() {

            @Override
            public void onLoginSuccess() {
                RLog.d(TAG, "onLoginSuccess : with TraditionalLoginHandler");
                postInMainThread(mContext, mTraditionalLoginHandler::onLoginSuccess);
                UserRegistrationHelper.getInstance().notifyOnHSDPLoginSuccess();
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                RLog.d(TAG, "onLoginFailedWithError : with TraditionalLoginHandler : " + userRegistrationFailureInfo.getErrorCode());
                AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                postInMainThread(mContext, () -> mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                UserRegistrationHelper.getInstance().notifyOnHSDPLoginFailure(userRegistrationFailureInfo.getErrorCode());
            }
        });
    }


    public void hsdpLogin(String accessToken, String emailOrMobile, SocialProviderLoginHandler mSocialProviderLoginHandler) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        RLog.d(TAG, "hsdpLogin : with SocialProviderLoginHandler");
        hsdpUser.login(emailOrMobile, accessToken, Jump.getRefreshSecret(), new SocialLoginHandler() {


            @Override
            public void onLoginSuccess() {
                postInMainThread(mContext, mSocialProviderLoginHandler::onContinueSocialProviderLoginSuccess);
                UserRegistrationHelper.getInstance().notifyOnHSDPLoginSuccess();
                RLog.d(TAG, "onSuccess : if : SocialLoginHandler : onLoginSuccess : is called");
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                postInMainThread(mContext, () ->
                {
                    mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                });
                UserRegistrationHelper.getInstance().notifyOnHSDPLoginFailure(userRegistrationFailureInfo.getErrorCode());
                RLog.d(TAG, "onLoginFailedWithError : if : SocialLoginHandler : onLoginFailedWithError : is called :" + userRegistrationFailureInfo.getErrorCode());
            }
        });
    }

}
