package com.philips.cdp.modularui.util;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.appframework.homescreen.HamburgerActivity;
import com.philips.cdp.appframework.introscreen.IntroductionScreenActivity;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310240027 on 6/29/2016.
 */
public class LaunchScreen {
    private static LaunchScreen instance = new LaunchScreen();

    private LaunchScreen(){}

    public static LaunchScreen getInstance(){
        if(null == instance){
            instance = new LaunchScreen();
        }
        return instance;
    }

    public void launchScreen(Context context,int stateID){
        Intent launchIntent = null;
        switch (ActivityMap.getInstance().getFromActivityMap(stateID)){
            case  UIConstants.UI_WELCOME_SCREEN:
                launchIntent = new Intent(context, IntroductionScreenActivity.class);
                break;
            case  UIConstants.UI_HAMBURGER_SCREEN:
                launchIntent = new Intent(context, HamburgerActivity.class);
                break;
            case  UIConstants.UI_USER_REGISTRATION_SCREEN:
                launchIntent = new Intent(context, RegistrationActivity.class);
                break;

        }
        context.startActivity(launchIntent);
    }
}
