package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface RefetchUserDetailsListener {

    void onRefetchSuccess();

    void onRefetchFailure(Error error);

}
