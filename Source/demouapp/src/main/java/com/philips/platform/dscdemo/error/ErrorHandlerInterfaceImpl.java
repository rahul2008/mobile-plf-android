package com.philips.platform.dscdemo.error;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

/**
 * Created by 310218660 on 12/20/2016.
 */

public class ErrorHandlerInterfaceImpl implements ErrorHandlingInterface{
    //Parse the error code from Retrofit and handle specific scenario
    @Override
    public void syncError(int error) {
        DSLog.e(DataServicesManager.TAG,"error = " + error);
    }

    @Override
    public void onServiceDiscoveryError(String error) {

    }
}
