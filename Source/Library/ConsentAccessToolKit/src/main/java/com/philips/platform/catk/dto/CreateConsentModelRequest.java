/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.dto;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.platform.catk.network.NetworkAbstractModel;

import java.util.List;

public class CreateConsentModelRequest extends NetworkAbstractModel {

    private String url;
    CreateConsentModel dto;

    public CreateConsentModelRequest(String url, CreateConsentModel dto, DataLoadListener dataLoadListener) {
        super(dataLoadListener);
        this.url = url;
        this.dto = dto;
    }

    @Override
    public List<GetConsentsModel> parseResponse(JsonArray response) {
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

    @Override
    public String getUrl() {
        return url;
    }

}