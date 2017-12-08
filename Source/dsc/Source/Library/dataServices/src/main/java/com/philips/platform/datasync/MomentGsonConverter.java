/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync;

import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentGsonConverter extends GsonConverter {

    @Inject
    public MomentGsonConverter(Gson gson) {
        super(gson);
    }
}