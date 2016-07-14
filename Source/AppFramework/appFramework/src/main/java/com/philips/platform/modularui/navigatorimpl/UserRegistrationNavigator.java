package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class UserRegistrationNavigator implements UIBaseNavigator {

    @Override
    public void navigate(Context context) {
        context.startActivity(new Intent(context, UserRegistrationActivity.class));
    }
}
