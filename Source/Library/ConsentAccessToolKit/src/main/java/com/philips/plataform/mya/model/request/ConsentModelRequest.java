package com.philips.plataform.mya.model.request;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.plataform.mya.model.network.NetworkAbstractModel;
import com.philips.plataform.mya.model.response.ConsentModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maqsood on 10/13/17.
 */

public class ConsentModelRequest extends NetworkAbstractModel {

    //This field has to remove later(URL should take from service discovery)
    private StringBuilder URL = new StringBuilder("https://hdc-css-mst.cloud.pcftest.com/consent/");
    private User mUser;
    private String mApplicationName;
    private String mPropositionName;

    public ConsentModelRequest(String applicationName, String propositionName, User user, DataLoadListener dataLoadListener) {
        super(user, dataLoadListener);
        mUser = user;
        mApplicationName = applicationName;
        mPropositionName = propositionName;
    }

    @Override
    public ConsentModel[] parseResponse(JsonArray response) {
        return new Gson().fromJson(response, ConsentModel[].class);
    }

    @Override
    public int getMethod() {
       return Request.Method.GET;
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
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        URL.append(mUser.getHsdpUUID()+"?applicationName="+mApplicationName+"&propositionName="+mPropositionName);
        return URL.toString();
    }
}
