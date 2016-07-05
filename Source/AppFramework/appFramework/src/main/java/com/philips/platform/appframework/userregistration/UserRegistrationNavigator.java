package com.philips.platform.appframework.userregistration;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class UserRegistrationNavigator implements UIBaseNavigator {
    @Override
    public void loadScreen(Context context) {
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }
}
