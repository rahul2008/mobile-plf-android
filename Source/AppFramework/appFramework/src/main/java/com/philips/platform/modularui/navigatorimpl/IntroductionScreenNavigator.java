package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class IntroductionScreenNavigator implements UIBaseNavigator {
    @Override
    public void navigate(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }
}
