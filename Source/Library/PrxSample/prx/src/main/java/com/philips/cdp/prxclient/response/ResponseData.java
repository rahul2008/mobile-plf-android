package com.philips.cdp.prxclient.response;

import org.json.JSONObject;

/**
 * Description : This class provides the URL
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */

public abstract class ResponseData {
    public abstract ResponseData parseJsonResponseData(JSONObject response);
}
