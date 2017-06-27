package com.philips.amwelluapp.registration;

import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;

public class PTHRegistrationDetailPresenter implements UIBasePresenter, PTHRegistrationDetailCallback<PTHConsumer, PTHSDKPasswordError>{
    @Override
    public void onEvent(int componentID) {

    }

    @Override
    public void onResponse(PTHConsumer pthConsumer, PTHSDKPasswordError pthsdkPasswordError) {

    }

    @Override
    public void onFailure(Throwable var1) {

    }
}
