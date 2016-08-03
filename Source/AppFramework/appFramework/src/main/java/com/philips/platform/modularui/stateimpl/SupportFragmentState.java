package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SupportFragmentState extends UIState {
    UICoCoInterface uiCoCoConsumerCare;

    public SupportFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        uiCoCoConsumerCare= CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_CONSUMER_CARE);
        uiCoCoConsumerCare.setActionbar((HomeActivity)context);
        uiCoCoConsumerCare.setFragActivity((HomeActivity)context);
        uiCoCoConsumerCare.loadPlugIn(context);
        uiCoCoConsumerCare.runCoCo(context);
    }
}
