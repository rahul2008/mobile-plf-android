package com.philips.platform.ths.intake;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

/**
 * Created by philips on 7/4/17.
 */

public class THSFollowUpPresenter implements THSBasePresenter, THSUpdateConsumerCallback<THSConsumer, THSSDKPasswordError> {
    THSBaseView uiBaseView;

    public THSFollowUpPresenter(THSBaseView uiBaseView) {
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        uiBaseView.addFragment(new THSNoppFragment(), THSNoppFragment.TAG,null );
    }


    protected void updateConsumer(String updatedPhoner) {


        try {
            THSManager.getInstance().updateConsumer(uiBaseView.getFragmentActivity(), updatedPhoner,this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onUpdateConsumerResponse(THSConsumer THSConsumer, THSSDKPasswordError sdkPasswordError) {
        //update signleton THSManager THSConsumer member
        THSManager.getInstance().setPTHConsumer(THSConsumer);
    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {

    }
}
