package com.philips.platform.ths.appointment;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;

public class THSProviderNotAvailablePresenter implements THSBasePresenter{
    THSBaseFragment mThsBaseFragment;

    THSProviderNotAvailablePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }
}
