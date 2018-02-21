/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.visit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.americanwell.sdk.entity.visit.Visit;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarWithLabel;

import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;
import static com.philips.platform.ths.utility.THSConstants.THS_WAITING;

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
    Visit mVisit;
    static final long serialVersionUID = 1123L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_waiting_room, container, false);
        Bundle bundle = getArguments();
        mVisit = bundle.getParcelable(THS_VISIT_ARGUMENT_KEY);
        mTHSWaitingRoomPresenter = new THSWaitingRoomPresenter(this);
        mProviderNameLabel = (Label) view.findViewById(R.id.details_providerNameLabel);
        mProviderPracticeLabel = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mProgressBarWithLabel = (ProgressBarWithLabel) view.findViewById(R.id.ths_waiting_room_ProgressBarWithLabel);
        mCancelVisitButton = (Button) view.findViewById(R.id.ths_waiting_room_cancel_button);
        mCancelVisitButton.setOnClickListener(this);
        mProviderImageView = (CircularImageView) view.findViewById(R.id.details_providerImage);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        doTaggingUponStartWaiting();
        mTHSWaitingRoomPresenter.startVisit();
    }

    private void doTaggingUponStartWaiting() {

        if (THSManager.getInstance().getThsTagging() == null) {
            if (THSManager.getInstance().getLoggingInterface() != null) {
                AmwellLog.i("TagInterface", "Tagging interface is null");
            } else {
                Log.e(AmwellLog.LOG, "TagInterface and logging interface are null");
            }
            return;
        }

        THSTagUtils.doTrackPageWithInfo(THS_WAITING, null, null);

        THSManager.getInstance().getThsTagging().trackTimedActionEnd("totalPreparationTimePreVisit");
        THSTagUtils.doTrackActionWithInfo("totalPrepartationTimeEnd", null, null);

        THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "waitInLineInstantAppointment");

        THSManager.getInstance().getThsTagging().trackTimedActionStart("totalWaitingTimeInstantAppointment");
        THSTagUtils.doTrackActionWithInfo("waitingTimeStartForInstantAppointment", null, null);
    }

    protected void doTaggingUponStopWaiting() {
        THSManager.getInstance().getThsTagging().trackTimedActionEnd("totalWaitingTimeInstantAppointment");
        THSTagUtils.doTrackActionWithInfo("waitingTimeEndForInstantAppointment", null, null);
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

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001 && requestCode == REQUEST_VIDEO_VISIT) { // 1001 is Successfull video completion
            //  todo getPresenter().setResult(resultCode, data);
            Bundle bundle = new Bundle();
            bundle.putParcelable(THS_VISIT_ARGUMENT_KEY, mVisit);
            THSVisitSummaryFragment thsVisitSummaryFragment = new THSVisitSummaryFragment();
            addFragment(thsVisitSummaryFragment, THSVisitSummaryFragment.TAG, bundle, true);
        } else {
            // video call does not completed succesfully so sending back user is
            doTaggingUponStopWaiting();
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "videoVisitCancelledAtQueue");
            mTHSWaitingRoomPresenter.showVisitUnSuccess(true, true, false);
        }
    }


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
        if (v.getId() == R.id.ths_waiting_room_cancel_button) {
            // mTHSWaitingRoomPresenter.onEvent(R.id.ths_waiting_room_cancel_button);
            if (v.getId() == R.id.uid_dialog_negative_button) {
                alertDialogFragment.dismiss();
            } else if (v.getId() == R.id.uid_dialog_positive_button) {
                mTHSWaitingRoomPresenter.onEvent(R.id.uid_dialog_positive_button);

            } else {
                showCancelDialog();
            }
        }


    }

    @Override
    public boolean handleBackEvent() {
        showCancelDialog();
        return true;
    }

    private void showCancelDialog() {
        THSConfirmationDialogFragment tHSConfirmationDialogFragment = new THSConfirmationDialogFragment();
        tHSConfirmationDialogFragment.setPresenter(mTHSWaitingRoomPresenter);
        tHSConfirmationDialogFragment.show(getFragmentManager(), THSConfirmationDialogFragment.TAG);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //Fix for 115218
    }

}
