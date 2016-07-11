package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;

import com.philips.platform.modularui.cocointerface.CoCoFactory;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class UserRegistrationNavigator implements UIBaseNavigator {
    UICoCoInterface uiCoCoUserReg;
    @Override
    public void navigate(Context context) {
        uiCoCoUserReg= CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        //context.startActivity(new Intent(context, RegistrationActivity.class));
        uiCoCoUserReg.loadPlugIn(context);
        uiCoCoUserReg.runCoCo(context);
        //context.startActivity(new Intent(context, RegistrationActivity.class));
    }
}
