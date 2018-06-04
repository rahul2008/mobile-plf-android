/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.platform.mya.catk.dto.CreateConsentDto;
import com.philips.platform.mya.catk.dto.GetConsentDto;

import java.util.List;

class CreateConsentModelRequest extends NetworkAbstractModel {

    @NonNull private String url;
    @NonNull private CreateConsentDto dto;

    public CreateConsentModelRequest(@NonNull String url, @NonNull CreateConsentDto dto, DataLoadListener dataLoadListener) {
        super(dataLoadListener);
        this.url = url;
        this.dto = dto;
    }

    @Override
    public List<GetConsentDto> parseResponse(JsonArray response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String requestBody() {
        return new Gson().toJson(dto);
    }

    @NonNull
    @Override
    public String getUrl() {
        return url;
    }

}