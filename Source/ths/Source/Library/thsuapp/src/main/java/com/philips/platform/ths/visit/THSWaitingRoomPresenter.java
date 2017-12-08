/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.visit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.ChatReport;
import com.americanwell.sdk.entity.visit.VisitEndReason;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.Map;


import static com.americanwell.sdk.entity.visit.VisitEndReason.PROVIDER_DECLINE;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_START_VISIT;
import static com.philips.platform.ths.uappclasses.THSCompletionProtocol.THSExitType.visitUnsuccessful;
import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_VIDEO_CALL;
import static com.philips.platform.ths.utility.THSConstants.VISIT_UNSUCCESSFUL;


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
            if (null != mTHSWaitingRoomFragment.mVisit && null != mTHSWaitingRoomFragment.mVisit.getAssignedProvider()) {
                mTHSWaitingRoomFragment.mProviderNameLabel.setText(mTHSWaitingRoomFragment.mVisit.getAssignedProvider().getFullName());
                mTHSWaitingRoomFragment.mProviderPracticeLabel.setText(mTHSWaitingRoomFragment.mVisit.getAssignedProvider().getPracticeInfo().getName());

                ///////////
                ProviderInfo providerInfo = mTHSWaitingRoomFragment.mVisit.getAssignedProvider();

                try {
                    THSManager.getInstance().getAwsdk(mTHSWaitingRoomFragment.getFragmentActivity()).
                            getPracticeProvidersManager().
                            newImageLoader(providerInfo,
                                    mTHSWaitingRoomFragment.mProviderImageView, ProviderImageSize.SMALL).placeholder
                            (mTHSWaitingRoomFragment.mProviderImageView.getResources().getDrawable(R.drawable.doctor_placeholder, mTHSWaitingRoomFragment.getActivity().getTheme())).
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


            THSManager.getInstance().startVisit(mTHSWaitingRoomFragment.getFragmentActivity(), mTHSWaitingRoomFragment.mVisit, null, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }catch (Exception e){
            mTHSWaitingRoomFragment.showError(mTHSWaitingRoomFragment.getString(R.string.something_went_wrong));
        }

    }

   /* void handleVisitFinish(Intent intent) {
        final Bundle visitExtras = intent.getBundleExtra(VISIT_FINISHED_EXTRAS);
        if (visitExtras != null) {

            int mResultcode = visitExtras.getInt(VISIT_RESULT_CODE);
            Visit visit = (Visit) visitExtras.getParcelable(VISIT);
            boolean isServerDisconnected = visitExtras.getBoolean(VISIT_STATUS_APP_SERVER_DISCONNECTED);
            boolean isVideoDisconnected = visitExtras.getBoolean(VISIT_STATUS_VIDEO_DISCONNECTED);
            boolean isProviderConnected = visitExtras.getBoolean(VISIT_STATUS_PROVIDER_CONNECTED);

            mTHSWaitingRoomFragment.mProgressBarWithLabel.setText(" Please wait, your visit is  wrapping up");
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY, visit);
            mTHSWaitingRoomFragment.addFragment(new THSVisitSummaryFragment(), THSVisitSummaryFragment.TAG, bundle, true);

        }
    }*/

    void cancelVisit() {
        try {
            THSManager.getInstance().cancelVisit(mTHSWaitingRoomFragment.getFragmentActivity(), mTHSWaitingRoomFragment.mVisit, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    void abondonCurrentVisit() {
        try {
            THSManager.getInstance().abandonCurrentVisit(mTHSWaitingRoomFragment.getFragmentActivity());
            mTHSWaitingRoomFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG, 0);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    void updatePatientAheadCount(int count) {
        if(count==0){
            mTHSWaitingRoomFragment.mProgressBarWithLabel.setText(null);
        }else if (count > 0) {
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
        mTHSWaitingRoomFragment.doTaggingUponStopWaiting();
        THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "completeWaitingInstantAppointment");
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_VIDEO_CALL, null, null);

    }

    @Override
    public void onStartVisitEnded(@NonNull VisitEndReason visitEndReason) {
        AmwellLog.v("call end", visitEndReason.toString());
        if (visitEndReason == PROVIDER_DECLINE) {
            mTHSWaitingRoomFragment.doTaggingUponStopWaiting();
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "videoVisitCancelledAtQueue");
            showVisitUnSuccess(true, true, false);

        }

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
        if (null != mTHSWaitingRoomFragment && mTHSWaitingRoomFragment.isFragmentAttached()) {
            mTHSWaitingRoomFragment.showError(mTHSWaitingRoomFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {

    }

    @Override
    public void onResponse(Void aVoid, SDKError sdkError) {
        if (null != mTHSWaitingRoomFragment && mTHSWaitingRoomFragment.isFragmentAttached()) {
            if (null != sdkError) {
                mTHSWaitingRoomFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_START_VISIT, sdkError));
                return;
            } else {
                // must  be cancel visit call back
                mTHSWaitingRoomFragment.doTaggingUponStopWaiting();
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "videoVisitCancelledAtQueue");
                THSManager.getInstance().resetTHSManagerData();
                abondonCurrentVisit();
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != mTHSWaitingRoomFragment && mTHSWaitingRoomFragment.isFragmentAttached()) {
            if (null != throwable && null != throwable.getMessage()) {
                mTHSWaitingRoomFragment.doTagging(ANALYTICS_START_VISIT, throwable.getMessage(), false);
                mTHSWaitingRoomFragment.showError(THSConstants.THS_GENERIC_SERVER_ERROR, true);
            }

        }
        abondonCurrentVisit();
    }


    void showVisitUnSuccess(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        if (null != mTHSWaitingRoomFragment && mTHSWaitingRoomFragment.isFragmentAttached()) {
            final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSWaitingRoomFragment.getFragmentActivity())
                    .setMessage(showLargeContent ? mTHSWaitingRoomFragment.getFragmentActivity().getResources().getString(R.string.visit_not_successful) : mTHSWaitingRoomFragment.getFragmentActivity().getResources().getString(R.string.visit_not_successful)).
                            setPositiveButton(" Ok ", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTHSWaitingRoomFragment.alertDialogFragment.dismiss();
                                    mTHSWaitingRoomFragment.exitFromAmWell(visitUnsuccessful);
                                }
                            });

            if (isWithTitle) {
                builder.setTitle("Error");

            }
            mTHSWaitingRoomFragment.alertDialogFragment = builder.setCancelable(false).create();
            mTHSWaitingRoomFragment.alertDialogFragment.show(mTHSWaitingRoomFragment.getFragmentManager(), VISIT_UNSUCCESSFUL);
        }

    }


}