package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.Map;

/**
 * Created by philips on 7/4/17.
 */

public class THSFollowUpPresenter implements PTHBasePresenter , PTHUpdateConsumerCallback<PTHConsumer , PTHSDKPasswordError>{
    PTHBaseView uiBaseView;

    public THSFollowUpPresenter(PTHBaseView uiBaseView) {
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        uiBaseView.addFragment(new THSNoppFragment(), THSNoppFragment.TAG,null );
    }


    protected void updateConsumer(String updatedPhoner) {


        try {
            PTHManager.getInstance().updateConsumer(uiBaseView.getFragmentActivity(), updatedPhoner,this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onUpdateConsumerResponse(PTHConsumer pthConsumer, PTHSDKPasswordError sdkPasswordError) {
        //update signleton PTHManager PTHConsumer member
        PTHManager.getInstance().setPTHConsumer(pthConsumer);
    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {

    }
}
