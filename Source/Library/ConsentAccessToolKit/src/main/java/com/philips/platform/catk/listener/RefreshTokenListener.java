/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

/**
 * Created by philips on 10/23/17.
 */

public interface RefreshTokenListener {
    void onRefreshSuccess();
    void onRefreshFailed(int errCode);
}
