package com.philips.cdp.di.ecs.integration;

public abstract class ECSOAuthProvider {


     public static final String CLIENT_ID = "mobile_android";
     public static final String CLIENT_SECRET = "secret";


     public abstract String getOAuthID();

     GrantType getGrantType(){
          return GrantType.JANRAIN;
     }

     public String getClientID(){
          return CLIENT_ID;
     }

     public String getClientSecret(){
          return CLIENT_SECRET;
     }

}
