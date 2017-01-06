package com.philips.platform.baseapp.screens.dataservices.error;

import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

/**
 * Created by 310218660 on 12/20/2016.
 */

public class ErrorHandlerInterfaceImpl implements ErrorHandlingInterface {
    @Override
    public void syncError(int error) {
        DSLog.e(DataServicesState.TAG,"error = " + error);
    }
}
