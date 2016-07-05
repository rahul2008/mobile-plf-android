package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class HomeActivityNavigator implements UIBaseNavigator {
    @Override
    public void loadScreen(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }
}
