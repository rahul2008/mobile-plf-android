/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSManager;

import javax.net.ssl.HttpsURLConnection;

class THSWelcomePresenter implements THSBasePresenter,
        THSLoginCallBack<THSAuthentication,THSSDKError>,THSGetConsumerObjectCallBack{
    private THSBaseFragment uiBaseView;
    boolean isRefreshTokenRequestedBefore;

    THSWelcomePresenter(THSBaseFragment uiBaseView){
        isRefreshTokenRequestedBefore = false;
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.appointments) {
            uiBaseView.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
        } else if (componentID == R.id.visit_history) {
            uiBaseView.addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
        } else if (componentID == R.id.how_it_works) {
            uiBaseView.showToast("Coming Soon!!!");
        } else if (componentID == R.id.ths_start) {
            uiBaseView.addFragment( new THSPracticeFragment(), THSPracticeFragment.TAG, null, false);
        }
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError sdkError) {

        if (sdkError.getSdkError() != null && sdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            if (checkIfRefreshTokenWasTriedBefore()) return;
            isRefreshTokenRequestedBefore = true;
            refreshToken();
            return;
        }

        try {
            if (thsAuthentication.needsToCompleteEnrollment()) {
                THSManager.getInstance().completeEnrollment(uiBaseView.getContext(), thsAuthentication, this);
            } else {
                THSManager.getInstance().getConsumerObject(uiBaseView.getFragmentActivity(), thsAuthentication.getAuthentication(), this);
            }
        } catch (AWSDKInstantiationException e) {

        }
    }

    private void refreshToken() {
        THSManager.getInstance().getUser(uiBaseView.getContext()).refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                authenticateUser();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int i) {
                uiBaseView.showToast("Refresh Signon failed with the following status code " + i + " please logout and login again");
                uiBaseView.hideProgressBar();
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {

            }
        });
    }

    @Override
    public void onLoginFailure(Throwable var1) {
        uiBaseView.hideProgressBar();
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        uiBaseView.hideProgressBar();
        ((THSWelcomeFragment)uiBaseView).updateView();
    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
    }


    public void getStarted() {
        authenticateUser();
    }

    private void authenticateUser() {
        try {
            THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfRefreshTokenWasTriedBefore() {
        if(isRefreshTokenRequestedBefore){
            isRefreshTokenRequestedBefore = false;
            uiBaseView.hideProgressBar();
            uiBaseView.showError(uiBaseView.getString(R.string.ths_user_not_authenticated));
            return true;
        }
        return false;
    }
}
