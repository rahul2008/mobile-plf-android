package com.philips.platform.ths.visit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static android.app.Activity.RESULT_CANCELED;
import static com.philips.platform.ths.utility.THSConstants.REQUEST_VIDEO_VISIT;

/**
 * Created by philips on 7/26/17.
 */

public class THSWaitingRoomFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSWaitingRoomFragment.class.getSimpleName();

    private ActionBarListener actionBarListener;
    private RelativeLayout mProgressbarContainer;
    THSWaitingRoomPresenter mTHSWaitingRoomPresenter;

    Label mProviderNameLabel;
    Label mProviderPracticeLabel;
    Button mCancelVisitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_waiting_room, container, false);
        mTHSWaitingRoomPresenter = new THSWaitingRoomPresenter(this);
        mProviderNameLabel = (Label) view.findViewById(R.id.details_providerNameLabel);
        mProviderPracticeLabel = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.ths_waiting_room_progressbar_container);
        mCancelVisitButton = (Button) view.findViewById(R.id.ths_waiting_room_cancel_button);
        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        mTHSWaitingRoomPresenter.startVisit();
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ths_waiting_room_cancel_button);{
            mTHSWaitingRoomPresenter.onEvent(R.id.ths_waiting_room_cancel_button);
        }

    }
}
