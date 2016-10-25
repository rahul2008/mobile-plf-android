/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core;

import android.content.SharedPreferences;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface BackendIdProvider {
    String getUserId();

    String getSubjectId();

    void injectSaredPrefs(SharedPreferences sharedPreferences);
}
