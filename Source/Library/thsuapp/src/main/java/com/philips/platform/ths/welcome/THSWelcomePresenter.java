/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.content.Context;
import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.faqs.THSFaqFragment;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.dependantregistration.THSDependantHistoryFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSConstants;
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
        final Context context = uiBaseView.getContext();
        Bundle bundle = new Bundle();
        if (componentID == R.id.appointments) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_SCHEDULED_VISITS);
            if(THSManager.getInstance().getThsParentConsumer(context).getDependents()!=null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size()>0){
                uiBaseView.addFragment(new THSDependantHistoryFragment(),THSDependantHistoryFragment.TAG,bundle,false);
            }else {
                uiBaseView.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
            }
        } else if (componentID == R.id.visit_history) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_VISITS_HISTORY);
            if(THSManager.getInstance().getThsParentConsumer(context).getDependents()!=null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size()>0){
                uiBaseView.addFragment(new THSDependantHistoryFragment(),THSDependantHistoryFragment.TAG,bundle,false);
            }else {
                uiBaseView.addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
            }
        } else if (componentID == R.id.how_it_works) {
            uiBaseView.addFragment(new THSFaqFragment(), THSFaqFragment.TAG, null, false);
        } else if (componentID == R.id.ths_start) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_PRACTICES);
            if(THSManager.getInstance().getThsParentConsumer(context).getDependents()!=null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size()>0){
                uiBaseView.addFragment(new THSDependantHistoryFragment(),THSDependantHistoryFragment.TAG,bundle,false);
            }else {
                uiBaseView.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, false);
            }
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
        uiBaseView.showToast(R.string.ths_se_server_error_toast_message);
    }

    @Override
    public void onReceiveConsumerObject(Consumer consumer, SDKError sdkError) {
        uiBaseView.hideProgressBar();
        if(sdkError == null) {
            ((THSWelcomeFragment) uiBaseView).updateView();
        }else if(null != sdkError.getSDKErrorReason()){
            uiBaseView.showError(THSSDKErrorFactory.getErrorType(sdkError.getSDKErrorReason()));
        }else {
            uiBaseView.showError(THSConstants.THS_GENERIC_SERVER_ERROR);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        uiBaseView.hideProgressBar();
        uiBaseView.showToast(R.string.ths_se_server_error_toast_message);
    }


    public void getStarted() {
        authenticateUser();
    }

    private void authenticateUser() {
        try {
            THSManager.getInstance().authenticateMutualAuthToken(uiBaseView.getContext(),this);
            //THSManager.getInstance().authenticate(uiBaseView.getContext(),"rohit.nihal@philips.com","Philips@123",null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfRefreshTokenWasTriedBefore() {
        if(isRefreshTokenRequestedBefore){
            isRefreshTokenRequestedBefore = false;
            uiBaseView.hideProgressBar();
            if(uiBaseView.getActivity()!=null) {
                uiBaseView.showError(uiBaseView.getString(R.string.ths_user_not_authenticated));
            }
            return true;
        }
        return false;
    }
}
