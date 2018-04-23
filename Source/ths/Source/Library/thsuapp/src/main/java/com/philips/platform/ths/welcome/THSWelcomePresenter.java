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
import com.philips.platform.ths.appointment.THSAppointmentAndVisitHistoryList;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.faqs.THSFaqFragment;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.registration.dependantregistration.THSDependantHistoryFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.settings.THSCustomerSupportFragment;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_CONSUMER_DETAILS;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_INITIALIZATION;


class THSWelcomePresenter implements THSBasePresenter{
    private THSBaseFragment uiBaseView;

    THSWelcomePresenter(THSBaseFragment uiBaseView){
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        final Context context = uiBaseView.getContext();
        Bundle bundle = new Bundle();
        if (componentID == R.id.appointments) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_SCHEDULED_VISITS);
               // uiBaseView.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);

            uiBaseView.addFragment(new THSAppointmentAndVisitHistoryList(), THSAppointmentAndVisitHistoryList.TAG, null, false);
        }  else if (componentID == R.id.how_it_works) {
            uiBaseView.addFragment(new THSFaqFragment(), THSFaqFragment.TAG, null, false);
        } else if (componentID == R.id.ths_start) {
            bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_PRACTICES);
            if(THSManager.getInstance().getThsParentConsumer(context).getDependents()!=null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size()>0){
                uiBaseView.addFragment(new THSDependantHistoryFragment(),THSDependantHistoryFragment.TAG,bundle,false);
            }else {
                uiBaseView.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, false);
            }
        } else if(componentID == R.id.customer_support){
            uiBaseView.addFragment(new THSCustomerSupportFragment(), THSPracticeFragment.TAG, null, false);
        }else if(componentID == R.id.details){
            if(THSManager.getInstance().getThsParentConsumer(context).getDependents()!=null && THSManager.getInstance().getThsParentConsumer(context).getDependents().size()>0){
                bundle.putInt(THSConstants.THS_LAUNCH_INPUT,THSConstants.THS_EDIT_CONSUMER_DETAILS);
                uiBaseView.addFragment(new THSDependantHistoryFragment(),THSDependantHistoryFragment.TAG,bundle,false);
            }else {
                bundle.putBoolean(THSConstants.IS_LAUNCHED_FROM_EDIT_DETAILS,true);
                uiBaseView.addFragment(new THSRegistrationFragment(), THSPracticeFragment.TAG, bundle, false);
            }
        }
    }


}
