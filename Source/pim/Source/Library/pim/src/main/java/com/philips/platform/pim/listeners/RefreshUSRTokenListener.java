package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface RefreshUSRTokenListener {
    void onRefreshTokenSuccess(String accessToken);

    void onRefreshTokenFailed(Error error);
}
