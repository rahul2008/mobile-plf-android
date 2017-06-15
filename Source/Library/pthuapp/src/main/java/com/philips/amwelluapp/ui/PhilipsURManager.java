package com.philips.amwelluapp.ui;


import android.content.Context;

import com.philips.cdp.registration.User;

public class PhilipsURManager {

    private User user;
    PhilipsURManager(Context context){
        user = new User(context);
    }

    public String getUUID(){
        return user.getHsdpUUID();
    }

    public String getHSDPAccessToken(){
        return user.getHsdpAccessToken();
    }

    public String getUserName(){
       return user.getGivenName();
    }

    public boolean isUserLoggedIn(){
        return user.isUserSignIn();
    }

    public String getMobileNumber(){
        return user.getMobile();
    }
    public String getEmail(){
        return user.getEmail();
    }

    public String getJainRainAccessToken(){
        return user.getAccessToken();
    }

    public String getJainRainUUID(){
        return user.getJanrainUUID();
    }

}
