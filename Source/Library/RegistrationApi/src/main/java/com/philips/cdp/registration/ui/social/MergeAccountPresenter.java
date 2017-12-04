package com.philips.cdp.registration.ui.social;


import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class MergeAccountPresenter implements TraditionalLoginHandler, NetworkStateListener {

    @Inject
    User mUser;


    private MergeAccountContract mergeAccountContract;

    public MergeAccountPresenter(MergeAccountContract mergeAccountContract,User mUser) {
        this.mUser=mUser;
        URInterface.getComponent().inject(this);
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
        if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
            mergeAccountContract.mergePasswordFailure();
            return;
        }
        mergeAccountContract.mergeFailure(userRegistrationFailureInfo.getErrorDescription());
    }


    public void mergeToTraditionalAccount(String mEmailId, String s, String mMergeToken) {
        mUser.mergeToTraditionalAccount(mEmailId, s, mMergeToken, this);
    }
}
