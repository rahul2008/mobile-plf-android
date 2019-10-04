package com.philips.cdp.di.mec.integration;

import org.json.JSONObject;

/**
 * Created by philips on 4/1/19.
 */

public interface MECMockInterface {


     boolean isMockEnabled();

     JSONObject GetMockJson(String fileName);

     JSONObject GetProductCatalogResponse();

     JSONObject OAuthResponse();

}
