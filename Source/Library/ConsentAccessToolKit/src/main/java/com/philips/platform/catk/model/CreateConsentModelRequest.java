/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.network.NetworkAbstractModel;

public class CreateConsentModelRequest extends NetworkAbstractModel {

    private String url;
    private String applicationName;
    private String propositionName;
    private String consentStatus;
    private String locale;

    public CreateConsentModelRequest(String url, String applicationName, String consentStatus, String propositionName, String locale, DataLoadListener dataLoadListener) {
        super(dataLoadListener);
        this.url = url;
        this.applicationName = applicationName;
        this.propositionName = propositionName;
        this.consentStatus = consentStatus;
        this.locale = locale;
    }

    @Override
    public GetConsentsModel[] parseResponse(JsonArray response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String requestBody() {
        CreateConsentModel model = new CreateConsentModel();
        model.setResourceType("Consent");
        model.setLanguage(locale);
        model.setStatus(consentStatus);
        model.setSubject(ConsentAccessToolKit.getInstance().getCatkComponent().getUser().getHsdpUUID());
        model.setPolicyRule("urn:com.philips.consent:moment/" + ConsentAccessToolKit.getInstance().getCatkComponent().getUser().getCountryCode()
                            + "/0/" + propositionName + "/" + applicationName);
        return getJsonString(model);
    }

    @Override
    public String getUrl() {
        return url;
    }

    private String getJsonString(CreateConsentModel model) {
        Gson gson = new Gson();
        return gson.toJson(model);
    }
}