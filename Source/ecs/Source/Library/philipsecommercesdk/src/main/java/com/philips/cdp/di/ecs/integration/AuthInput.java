package com.philips.cdp.di.ecs.integration;

import com.philips.cdp.di.ecs.util.ECSConstant;

public abstract class AuthInput {

     public abstract String getJanRainID();

     public String getGrantType(){
          return ECSConstant.GRANT_TYPE;
     }

     public String getClientID(){
          return ECSConstant.CLIENT_ID;
     }

     public String getClientSecret(){
          return ECSConstant.CLIENT_SECRET;
     }
}
