package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.PIMError;

public interface RefetchUserDetailsListener {

    void onRefetchSuccess();

    void onRefetchFailure(PIMError error);

}
