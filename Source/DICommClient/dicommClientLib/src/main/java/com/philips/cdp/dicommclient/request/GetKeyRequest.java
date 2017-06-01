/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.communication.LanRequest;
import com.philips.cdp2.commlib.lan.communication.LanRequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class GetKeyRequest extends LanRequest {

    private static final String SECURITY_PORTNAME = "security";
    private static final int SECURITY_PRODUCTID = 0;
    private static final String KEY_PROPERTY = "key";

    public GetKeyRequest(String applianceIpAddress, int protocolVersion, boolean isHttps, ResponseHandler responseHandler) {
        super(applianceIpAddress, protocolVersion, isHttps, SECURITY_PORTNAME, SECURITY_PRODUCTID, LanRequestType.GET, new HashMap<String, Object>(), responseHandler, null);
    }

    @Override
    public Response execute() {
        Response response = super.execute();
        String responseData = response.getResponseMessage();

        JSONObject json;
        try {
            json = new JSONObject(responseData);
            String key = json.getString(KEY_PROPERTY);
            return new Response(key, null, mResponseHandler);
        } catch (JSONException e) {
            DICommLog.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
    }
}
