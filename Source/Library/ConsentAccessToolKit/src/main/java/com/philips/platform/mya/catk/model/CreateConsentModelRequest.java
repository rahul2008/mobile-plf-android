package com.philips.platform.mya.catk.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.mya.catk.network.NetworkAbstractModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maqsood on 10/13/17.
 */

public class CreateConsentModelRequest extends NetworkAbstractModel {

    //This field has to remove later(URL should take from service discovery)
    private String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";
    private User mUser;
    private String mApplicationName;
    private String mPropositionName;
    private String consentStatus;

    public CreateConsentModelRequest(String applicationName, String consentStatus,String propositionName, User user, DataLoadListener dataLoadListener) {
        super(user, dataLoadListener);
        mUser = user;
        mApplicationName = applicationName;
        mPropositionName = propositionName;
        this.consentStatus = consentStatus;
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("api-version", "1");
        params.put("content-type", "application/json");
        params.put("authorization","bearer "+mUser.getHsdpAccessToken());
        params.put("performerid",mUser.getHsdpUUID());
        params.put("cache-control", "no-cache");
        return params;
    }

    @Override
    public String requestBody() {
        CreateConsentModel model = new CreateConsentModel();
        model.setResourceType("Consent");
        model.setLanguage("af-ZA");
        model.setStatus(consentStatus);
        model.setSubject(mUser.getHsdpUUID());
        model.setPolicyRule("urn:com.philips.consent:moment/IN/0/OneBackendProp/OneBackend");
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