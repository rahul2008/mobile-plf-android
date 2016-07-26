package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class IntroductionScreenState extends UIState {
    public IntroductionScreenState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }
}
