package com.ecs.demouapp.ui.integration;

import org.json.JSONObject;

/**
 * Created by philips on 4/1/19.
 */

public interface ECSMockInterface {


     boolean isMockEnabled();

     JSONObject GetMockJson(String fileName);

     JSONObject GetProductCatalogResponse();

     JSONObject OAuthResponse();

}
