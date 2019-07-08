package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface RefreshUSRTokenListener {
    void onRefreshTokenRequestSuccess(String refreshToken);

    void onRefreshTokenRequestFailed(Error error);
}
