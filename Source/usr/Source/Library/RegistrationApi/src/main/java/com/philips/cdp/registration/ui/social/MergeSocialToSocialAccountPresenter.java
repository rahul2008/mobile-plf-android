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

public class MergeSocialToSocialAccountPresenter implements NetworkStateListener, SocialProviderLoginHandler {

    private MergeSocialToSocialAccountContract mergeSocialToSocialAccountContract;

    @Inject
    User mUser;

    public MergeSocialToSocialAccountPresenter(MergeSocialToSocialAccountContract mergeSocialToSocialAccountContract,User mUser) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.mUser=mUser;
        this.mergeSocialToSocialAccountContract = mergeSocialToSocialAccountContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        mergeSocialToSocialAccountContract.connectionStatus(isOnline);
        mergeSocialToSocialAccountContract.mergeStatus(isOnline);
    }


    @Override
    public void onLoginSuccess() {
        mergeSocialToSocialAccountContract.mergeSuccess();
    }

    @Override
    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        mergeSocialToSocialAccountContract.mergeFailure(userRegistrationFailureInfo.getErrorDescription());
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        EventBus.getDefault().post(new LoginFailureNotification());
        mergeSocialToSocialAccountContract.mergeFailureIgnored();
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
        EventBus.getDefault().post(new LoginFailureNotification());
        mergeSocialToSocialAccountContract.mergeFailureIgnored();
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        mergeSocialToSocialAccountContract.mergeSuccess();
    }

    @Override
    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        mergeSocialToSocialAccountContract.mergeFailureIgnored();
    }

    public void logout() {
        mUser.logout(null);
    }

    public void loginUserUsingSocialProvider(String mConflictProvider, String mMergeToken) {
        mUser.loginUserUsingSocialProvider(mergeSocialToSocialAccountContract.getActivityContext(), mConflictProvider, this, mMergeToken);
    }

    public String getLoginWithDetails() {
        if (FieldsValidator.isValidEmail(mUser.getEmail())) {
            return mUser.getEmail();
        }
        return mUser.getMobile();
    }

    public boolean getReceiveMarketingEmail() {
        return mUser.getReceiveMarketingEmail();
    }
}
