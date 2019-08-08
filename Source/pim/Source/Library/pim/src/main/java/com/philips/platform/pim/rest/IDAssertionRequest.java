package com.philips.platform.pim.rest;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IDAssertionRequest implements PIMRequestInterface {

    private String endpoint;
    private String accessToken;
    private String TAG = IDAssertionRequest.class.getSimpleName();

    public IDAssertionRequest(String endpoint, String accessToken) {
        this.endpoint = endpoint;
        this.accessToken = accessToken;
    }

    @Override
    public String getUrl() {
        return endpoint;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/x-www-form-urlencoded");
        headers.put("Api-Key", "nYO1gXoy5J7AaHT8KPu2D9JxN2cZo77M8zdBD2iJ");
        headers.put("Api-Version", "1");
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String getBody() {
        JSONObject bodyJson = new JSONObject();
        JSONObject accessTokenJson = new JSONObject();
        try {
            accessTokenJson.put("accessToken", accessToken);
            bodyJson.put("data", accessTokenJson);
        } catch (JSONException e) {
            PIMSettingManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, TAG, "Json Exception : " + e.getMessage());
        }
        return bodyJson.toString();
    }

    @Override
    public int getMethodType() {
        return PIMRequest.Method.POST;
    }
}
