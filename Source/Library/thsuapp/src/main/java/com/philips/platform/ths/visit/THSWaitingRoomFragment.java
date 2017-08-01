package com.philips.platform.ths.visit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarWithLabel;

import static android.app.Activity.RESULT_CANCELED;
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
    ImageView mProviderImageView;

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
        mProviderImageView = (ImageView)view.findViewById(R.id.details_providerImage);
        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_VIDEO_VISIT) {
            //  todo getPresenter().setResult(resultCode, data);
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
}
