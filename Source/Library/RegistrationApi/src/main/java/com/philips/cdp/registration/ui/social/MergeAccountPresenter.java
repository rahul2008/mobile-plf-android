package com.philips.cdp.registration.ui.social;


import com.philips.cdp.registration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import org.greenrobot.eventbus.*;

import javax.inject.*;

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
