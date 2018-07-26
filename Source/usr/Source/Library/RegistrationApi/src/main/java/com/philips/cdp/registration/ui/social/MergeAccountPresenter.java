package com.philips.cdp.registration.ui.social;


import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import javax.inject.Inject;

public class MergeAccountPresenter implements SocialProviderLoginHandler, NetworkStateListener {

    @Inject
    User mUser;


    private MergeAccountContract mergeAccountContract;

    public MergeAccountPresenter(MergeAccountContract mergeAccountContract,User mUser) {
        this.mUser=mUser;
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mergeAccountContract = mergeAccountContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        mergeAccountContract.connectionStatus(isOnline);
        mergeAccountContract.mergeStatus(isOnline);
    }


    boolean getReceiveMarketingEmail() {
        return mUser.getReceiveMarketingEmail();
    }

    public String getLoginWithDetails() {
        if (FieldsValidator.isValidEmail(mUser.getEmail())) {
            return mUser.getEmail();
        }
        return mUser.getMobile();
    }

    @Override
    public void onLoginSuccess() {
        mergeAccountContract.mergeSuccess();
    }

    @Override
    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
//        if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
//            mergeAccountContract.mergePasswordFailure();
//            return;
//        }
        mergeAccountContract.mergeFailure(userRegistrationFailureInfo.getErrorDescription(),userRegistrationFailureInfo.getErrorCode());
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        //nope
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
    //nope
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
    //nope
    }

    @Override
    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
    //nope
    }


    public void mergeToTraditionalAccount(String mEmailId, String s, String mMergeToken) {
        mUser.mergeToTraditionalAccount(mEmailId, s, mMergeToken, this);
    }
}
