package com.philips.platform.catk.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.platform.catk.CatkInterface;
import com.philips.platform.catk.network.NetworkAbstractModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maqsood on 10/13/17.
 */

public class CreateConsentModelRequest extends NetworkAbstractModel {

    //This field has to remove later(URL should take from service discovery)
    private String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";
    private String applicationName;
    private String propositionName;
    private String consentStatus;
    private String locale;

    public CreateConsentModelRequest(String applicationName, String consentStatus, String propositionName, String locale, DataLoadListener dataLoadListener) {
        super(dataLoadListener);
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
    public Map<String, String> requestHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("api-version", "1");
        header.put("content-type", "application/json");
        header.put("authorization","bearer "+CatkInterface.getCatkComponent().getUser().getHsdpAccessToken());
        header.put("performerid",CatkInterface.getCatkComponent().getUser().getHsdpUUID());
        header.put("cache-control", "no-cache");
        return header;
    }

    @Override
    public String requestBody() {
        CreateConsentModel model = new CreateConsentModel();
        model.setResourceType("Consent");
        model.setLanguage(locale);
        model.setStatus(consentStatus);
        model.setSubject(CatkInterface.getCatkComponent().getUser().getHsdpUUID());
        model.setPolicyRule("urn:com.philips.consent:moment/" + CatkInterface.getCatkComponent().getUser().getCountryCode()
                + "/0/" + propositionName + "/" + applicationName);
        return getJsonString(model);
    }

    @Override
    public String getUrl() {
        return URL;
    }

    private String getJsonString(CreateConsentModel model) {
        Gson gson = new Gson();
        return gson.toJson(model);
    }
}