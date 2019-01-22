package com.philips.uiddemo;

import android.app.Application;

import com.adobe.mobile.Config;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import net.openid.appauth.AuthState;

public class UIDDemoApplication extends Application {
    public AuthState authState=new AuthState();
    @Override
    public void onCreate() {
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.ULTRA_LIGHT, AccentRange.GROUP_BLUE));
        Config.setContext(this);
//        Analytics.
        super.onCreate();
    }

    public AuthState getAuthState() {
        return authState;
    }

}
