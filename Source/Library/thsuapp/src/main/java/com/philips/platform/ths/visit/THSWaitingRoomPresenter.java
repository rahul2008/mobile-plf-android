package com.philips.platform.ths.visit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.ChatReport;
import com.americanwell.sdk.entity.visit.VisitEndReason;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;

/**
 * Created by philips on 7/26/17.
 */

public class THSWaitingRoomPresenter implements THSBasePresenter, THSStartVisitCallback {

THSWaitingRoomFragment mTHSWaitingRoomFragment;

    public THSWaitingRoomPresenter(THSWaitingRoomFragment mTHSWaitingRoomFragment) {
        this.mTHSWaitingRoomFragment = mTHSWaitingRoomFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

   void  startVisit(){
       try {
           THSManager.getInstance().startVisit(mTHSWaitingRoomFragment.getFragmentActivity(),this);
       } catch (AWSDKInstantiationException e) {
           e.printStackTrace();
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
    }

    @Override
    public void onStartVisitEnded(@NonNull VisitEndReason visitEndReason) {

    }

    @Override
    public void onPatientsAheadOfYouCountChanged(int i) {

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

    }

    @Override
    public void onFailure(Throwable throwable) {

    }


}
