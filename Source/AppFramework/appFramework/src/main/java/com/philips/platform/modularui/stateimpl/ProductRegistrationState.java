package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.cocointerface.UICoCoProdRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

public class ProductRegistrationState extends UIState {

    UICoCoProdRegImpl uiCoCoProdReg;
    public ProductRegistrationState(@UIStateDef int stateID){
        super(stateID);
    }
    @Override
    protected void navigate(Context context) {
        uiCoCoProdReg = (UICoCoProdRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_PRODUCT_REGISTRATION);
        uiCoCoProdReg.loadPlugIn(context);
        uiCoCoProdReg.setActionBar((HomeActivity)context);
        uiCoCoProdReg.setFragActivity((HomeActivity)context);
        uiCoCoProdReg.setFragmentContainer(R.id.frame_container);
        uiCoCoProdReg.runCoCo(context);
    }
}
