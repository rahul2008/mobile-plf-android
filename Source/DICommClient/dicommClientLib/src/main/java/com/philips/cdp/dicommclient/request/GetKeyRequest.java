/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetKeyRequest extends LocalRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;

    public GetKeyRequest(String applianceIpAddress, int protocolVersion, boolean isHttps, ResponseHandler responseHandler) {
        super(applianceIpAddress, protocolVersion, isHttps, SECURITY_PORTNAME, SECURITY_PRODUCTID, LocalRequestType.GET, new HashMap<String, Object>(), responseHandler, null);
    }

    @Override
    public Response execute() {
        Response response = super.execute();
        String responseData = response.getResponseMessage();

        JSONObject json;
        try {
            json = new JSONObject(responseData);
            String key = json.getString("key");
            return new Response(key, null, mResponseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(null, Error.REQUESTFAILED, mResponseHandler);
    }
}