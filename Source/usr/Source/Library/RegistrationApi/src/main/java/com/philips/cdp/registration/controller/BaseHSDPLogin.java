package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

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
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.login(emailorMobile, accessToken, Jump.getRefreshSecret(), new SocialLoginHandler() {

            @Override
            public void onLoginSuccess() {
                ThreadUtils.postInMainThread(mContext, () ->
                        mTraditionalLoginHandler.onLoginSuccess());
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                ThreadUtils.postInMainThread(mContext, () -> mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
            }
        });
    }


    public void hsdpLogin(String accessToken, String emailOrMobile, SocialProviderLoginHandler mSocialProviderLoginHandler) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.login(emailOrMobile, accessToken, Jump.getRefreshSecret(), new SocialLoginHandler() {

            @Override
            public void onLoginSuccess() {
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());

                RLog.d(TAG, "onSuccess : if : SocialLoginHandler : onLoginSuccess : is called");
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                RLog.d(TAG, "onSuccess : if : SocialLoginHandler : onLoginFailedWithError : is called");
            }
        });
    }

}
