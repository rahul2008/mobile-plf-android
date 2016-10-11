/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorResponse {
    public int errorCode;

    @Nullable
    public static ErrorResponse create(final Response response) {
        ErrorResponse errorResponse = null;
        if (response.getBody() == null) {
            return null;
        }

        TypedByteArray typedString = (TypedByteArray) response.getBody();
        String responseString = new String(typedString.getBytes());
        Gson gson = new GsonBuilder().create();
        try {
            errorResponse = gson.fromJson(responseString, ErrorResponse.class);
        } catch (Exception e) {

        }
        return errorResponse;
    }
}
