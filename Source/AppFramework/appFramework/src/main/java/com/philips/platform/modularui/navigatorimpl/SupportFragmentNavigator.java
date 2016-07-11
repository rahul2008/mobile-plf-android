package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;

import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.cocointerface.CoCoFactory;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SupportFragmentNavigator implements UIBaseNavigator {
    UICoCoInterface uiCoCoConsumerCare;
    @Override
    public void navigate(Context context) {
        uiCoCoConsumerCare= CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_CONSUMER_CARE);
        uiCoCoConsumerCare.setActionbar((HomeActivity)context);
        uiCoCoConsumerCare.setFragActivity((HomeActivity)context);
        uiCoCoConsumerCare.loadPlugIn(context);
    }
}
