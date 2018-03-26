package com.philips.platform.baseapp.screens.telehealthservices;

import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;

/**
 * Created by philips on 26/03/18.
 */

public class TeleHealthServicesStateDeepLinking extends TeleHealthServicesState {

    @Override
    protected THSMicroAppLaunchInput getTHSMicroLaunchInput() {
        return new THSMicroAppLaunchInput("",this,true);
    }
}
