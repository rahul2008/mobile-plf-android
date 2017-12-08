/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import retrofit.client.OkClient;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OkClientFactory {

    @Inject
    public OkClientFactory() {
    }

    public OkClient create(@NonNull final OkHttpClient okHttpClient) {
        return new OkClient(okHttpClient);
    }
}
