/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.visit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.ChatReport;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.entity.visit.VisitEndReason;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.Map;

import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_FINISHED_EXTRAS;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_RESULT_CODE;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_APP_SERVER_DISCONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_PROVIDER_CONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_VIDEO_DISCONNECTED;
import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;
import static com.philips.platform.ths.utility.THSConstants.THS_VIDEO_CALL;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;

/**
 * Created by philips on 7/26/17.
 */

public class THSWaitingRoomPresenter implements THSBasePresenter, THSStartVisitCallback, THSCancelVisitCallBack.SDKCallback<Void, SDKError> {

    private THSWaitingRoomFragment mTHSWaitingRoomFragment;

    public THSWaitingRoomPresenter(THSWaitingRoomFragment mTHSWaitingRoomFragment) {
        this.mTHSWaitingRoomFragment = mTHSWaitingRoomFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_confirmation_dialog_primary_button) {
            mTHSWaitingRoomFragment.mProgressBarWithLabel.setText("Cancelling Visit");
            cancelVisit();

        }
    }

    void startVisit() {
        try {
            if (null !=mTHSWaitingRoomFragment.mVisit && null != mTHSWaitingRoomFragment.mVisit.getAssignedProvider()) {
                mTHSWaitingRoomFragment.mProviderNameLabel.setText(mTHSWaitingRoomFragment.mVisit.getAssignedProvider().getFullName());
                mTHSWaitingRoomFragment.mProviderPracticeLabel.setText(mTHSWaitingRoomFragment.mVisit.getAssignedProvider().getPracticeInfo().getName());

                ///////////
                ProviderInfo providerInfo = mTHSWaitingRoomFragment.mVisit.getAssignedProvider();

                    try {
                        THSManager.getInstance().getAwsdk(mTHSWaitingRoomFragment.getFragmentActivity()).
                                getPracticeProvidersManager().
                                newImageLoader(providerInfo,
                                        mTHSWaitingRoomFragment.mProviderImageView, ProviderImageSize.SMALL).placeholder
                                (mTHSWaitingRoomFragment.mProviderImageView.getResources().getDrawable(R.drawable.doctor_placeholder,mTHSWaitingRoomFragment.getActivity().getTheme())).
                                build().load();
                    } catch (AWSDKInstantiationException e) {
                        e.printStackTrace();
                    }

                ////////////
            }
            Integer patientWaitingCount = mTHSWaitingRoomFragment.mVisit.getPatientsAheadOfYou();
            if (null != patientWaitingCount && patientWaitingCount > 0) {

                mTHSWaitingRoomFragment.mProgressBarWithLabel.setText(patientWaitingCount + " patients waiting");
            }


            THSManager.getInstance().startVisit(mTHSWaitingRoomFragment.getFragmentActivity(),mTHSWaitingRoomFragment.mVisit, null,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    void handleVisitFinish(Intent intent){
        final Bundle visitExtras = intent.getBundleExtra(VISIT_FINISHED_EXTRAS);
        if (visitExtras != null) {

            int mResultcode = visitExtras.getInt(VISIT_RESULT_CODE);
            Visit visit = (Visit) visitExtras.getParcelable(VISIT);
            boolean isServerDisconnected = visitExtras.getBoolean(VISIT_STATUS_APP_SERVER_DISCONNECTED);
            boolean isVideoDisconnected = visitExtras.getBoolean(VISIT_STATUS_VIDEO_DISCONNECTED);
            boolean isProviderConnected = visitExtras.getBoolean(VISIT_STATUS_PROVIDER_CONNECTED);

            mTHSWaitingRoomFragment.mProgressBarWithLabel.setText(  " Please wait, your visit is  wrapping up");
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY,visit);
            bundle.putParcelable(THSConstants.THS_CONSUMER,mTHSWaitingRoomFragment.getConsumer());
            mTHSWaitingRoomFragment.addFragment(new THSVisitSummaryFragment(), THSVisitSummaryFragment.TAG,bundle, true);

        }
    }

    void cancelVisit() {
        try {
            THSManager.getInstance().cancelVisit(mTHSWaitingRoomFragment.getFragmentActivity(),mTHSWaitingRoomFragment.mVisit, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    void abondonCurrentVisit() {
        try {
            THSManager.getInstance().abondonCurrentVisit(mTHSWaitingRoomFragment.getFragmentActivity());
            mTHSWaitingRoomFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG,0);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    void updatePatientAheadCount(int count) {
        if (count > 0) {
            mTHSWaitingRoomFragment.mProgressBarWithLabel.setText(count + " patients waiting");
        }

    }

    @Override
    public void onProviderEntered(@NonNull Intent intent) {
        // set up ongoing notification
  /*      PendingIntent pendingIntent = PendingIntent.getActivity(mTHSWaitingRoomFragment.getFragmentActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mTHSWaitingRoomFragment.getFragmentActivity());
        builder.setSmallIcon(R.drawable.awsdk_ic_visit_camera_default)
                .setContentTitle("THS")
                .setContentText("provider name")
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);
        builder.build();*/
        // notificationManager.notify(ONGOING_NOTIFICATION_ID, builder.build());
        // start activity


       mTHSWaitingRoomFragment.startActivityForResult(intent, REQUEST_VIDEO_VISIT);
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_VIDEO_CALL,null,null);

    }

    @Override
    public void onStartVisitEnded(@NonNull VisitEndReason visitEndReason) {
        AmwellLog.v("call end",visitEndReason.toString());

    }

    @Override
    public void onPatientsAheadOfYouCountChanged(int i) {
        updatePatientAheadCount(i);
    }

    @Override
    public void onSuggestedTransfer() {

    }

    @Override
    public void onChat(@NonNull ChatReport chatReport) {

    }

    @Override
    public void onPollFailure(@NonNull Throwable throwable) {

    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {

    }

    @Override
    public void onResponse(Void aVoid, SDKError sdkError) {
        // must  be cancel visit call back
        abondonCurrentVisit();
    }

    @Override
    public void onFailure(Throwable throwable) {
        abondonCurrentVisit();
    }


}
