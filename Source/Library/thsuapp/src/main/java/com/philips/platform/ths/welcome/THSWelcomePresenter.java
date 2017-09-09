/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.net.ssl.HttpsURLConnection;

public class THSWelcomePresenter implements THSBasePresenter{
    private THSBaseFragment uiBaseView;

    THSWelcomePresenter(THSBaseFragment uiBaseView){
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.appointments) {
            uiBaseView.addFragment(new THSScheduledVisitsFragment(),THSScheduledVisitsFragment.TAG,null);
        } else if (componentID == R.id.visit_history) {
            uiBaseView.addFragment(new THSVisitHistoryFragment(),THSScheduledVisitsFragment.TAG,null);
        } else if (componentID == R.id.how_it_works) {
            uiBaseView.showToast("Coming Soon!!!");
        }else if(componentID == R.id.ths_start){
            launchPractice();
        }
    }

    private void launchPractice() {
        AmwellLog.d("Login","Consumer object received");
        final THSPracticeFragment fragment = new THSPracticeFragment();
        fragment.setFragmentLauncher(uiBaseView.getFragmentLauncher());
        uiBaseView.addFragment(fragment,THSPracticeFragment.TAG,null);
    }
}
