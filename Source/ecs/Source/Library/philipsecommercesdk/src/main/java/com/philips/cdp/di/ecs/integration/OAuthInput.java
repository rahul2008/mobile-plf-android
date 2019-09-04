package com.philips.cdp.di.ecs.integration;

public abstract class OAuthInput{


     public static final String CLIENT_ID = "mobile_android";
     public static final String CLIENT_SECRET = "secret";


     public abstract String getOAuthID();

     public GrantType getGrantType(){
          return GrantType.JANRAIN;
     }

     public String getClientID(){
          return CLIENT_ID;
     }

     public String getClientSecret(){
          return CLIENT_SECRET;
     }

}
