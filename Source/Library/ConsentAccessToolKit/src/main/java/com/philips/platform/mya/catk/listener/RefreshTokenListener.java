package com.philips.platform.mya.catk.listener;

/**
 * Created by philips on 10/23/17.
 */

public interface RefreshTokenListener {
    void onRefreshSuccess();
    void onRefreshFailed(int errCode);
}
