package com.philips.platform.ths.appointment;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;

public class THSPickTimePresenter implements THSBasePresenter {
    THSBaseFragment mThsBaseFragment;

    THSPickTimePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }
    @Override
    public void onEvent(int componentID) {

    }
}
