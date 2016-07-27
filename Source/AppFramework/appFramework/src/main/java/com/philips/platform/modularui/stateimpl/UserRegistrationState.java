package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class UserRegistrationState extends UIState {

    public UserRegistrationState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        // TODO: Launch UR as fragment from our activity
        context.startActivity(new Intent(context, UserRegistrationActivity.class));
    }
}
