package com.philips.platform.ths.insurance;

import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;

/**
 * Created by philips on 7/10/17.
 */

public class THSConfirmationPresenter implements THSBasePresenter {
    THSBaseView uiBaseView;


    public THSConfirmationPresenter(THSBaseView uiBaseView) {
        this.uiBaseView = uiBaseView;
    }



    @Override
    public void onEvent(int componentID) {

    }


}
