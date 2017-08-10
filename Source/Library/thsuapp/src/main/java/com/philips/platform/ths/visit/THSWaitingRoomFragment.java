/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.visit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.visit.Visit;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarWithLabel;

import static android.app.Activity.RESULT_CANCELED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_FINISHED_EXTRAS;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_RESULT_CODE;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_APP_SERVER_DISCONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_PROVIDER_CONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_VIDEO_DISCONNECTED;
import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;

/**
 * Created by philips on 7/26/17.
 */

public class THSWaitingRoomFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSWaitingRoomFragment.class.getSimpleName();
    public static final String CANCEL_VISIT_ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";

    private ActionBarListener actionBarListener;
     ProgressBarWithLabel mProgressBarWithLabel;
    THSWaitingRoomPresenter mTHSWaitingRoomPresenter;
     AlertDialogFragment alertDialogFragment;

    Label mProviderNameLabel;
    Label mProviderPracticeLabel;
    Button mCancelVisitButton;
    CircularImageView mProviderImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_waiting_room, container, false);
        mTHSWaitingRoomPresenter = new THSWaitingRoomPresenter(this);
        mProviderNameLabel = (Label) view.findViewById(R.id.details_providerNameLabel);
        mProviderPracticeLabel = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mProgressBarWithLabel = (ProgressBarWithLabel) view.findViewById(R.id.ths_waiting_room_ProgressBarWithLabel);
        mCancelVisitButton = (Button) view.findViewById(R.id.ths_waiting_room_cancel_button);
        mCancelVisitButton.setOnClickListener(this);
        mProviderImageView = (CircularImageView)view.findViewById(R.id.details_providerImage);

        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("visitFinishedResult"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        actionBarListener = getActionBarListener();

        mTHSWaitingRoomPresenter.startVisit();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This needs to be taken care by proposition to avoid losing listener on rotation
        final AlertDialogFragment alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(CANCEL_VISIT_ALERT_DIALOG_TAG);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);
            alertDialogFragment.setNegativeButtonListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Waiting", true);
        }
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("visitFinishedResult"));
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            mTHSWaitingRoomPresenter.handleVisitFinish(intent);
            //Log.d("receiver", "Got message: " + message);

        }
    };

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }


     void showCancelDialog(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getFragmentActivity())
                .setMessage(showLargeContent ? "Your visit shall be cancelled" : "Your visit shall be cancelled").
                        setPositiveButton(" Yes ", this).
                        setNegativeButton(" No  ", this);
        if (isWithTitle) {
            builder.setTitle("Do you really want to cancel your visit");
            if (showIcon) {
                builder.setIcon(R.drawable.uid_ic_cross_icon);

            }
        }
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), CANCEL_VISIT_ALERT_DIALOG_TAG);
    }





    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ths_waiting_room_cancel_button);{
           // mTHSWaitingRoomPresenter.onEvent(R.id.ths_waiting_room_cancel_button);
            if (v.getId() == R.id.uid_alert_negative_button) {
                alertDialogFragment.dismiss();
            }else if (v.getId() == R.id.uid_alert_positive_button) {
                mTHSWaitingRoomPresenter.onEvent(R.id.uid_alert_positive_button);

            }else {
                showCancelDialog(true, true, true);
            }
        }


    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        //LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int y=10;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        int y=15;
    }
}
