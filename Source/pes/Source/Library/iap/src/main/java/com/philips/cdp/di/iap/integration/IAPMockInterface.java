package com.philips.cdp.di.iap.integration;

import org.json.JSONObject;

/**
 * Created by philips on 4/1/19.
 */

public interface IAPMockInterface {


     boolean isMockEnabled();

     JSONObject GetMockJson(String fileName);

     JSONObject GetProductCatalogResponse();

     JSONObject OAuthResponse();

}
