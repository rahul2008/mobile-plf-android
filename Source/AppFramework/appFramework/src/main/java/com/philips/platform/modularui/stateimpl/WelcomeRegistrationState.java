package com.philips.platform.modularui.stateimpl;

        import android.content.Context;
        import android.content.Intent;

        import com.philips.platform.appframework.introscreen.WelcomeActivity;
        import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 8/1/2016.
 */
public class WelcomeRegistrationState extends UIState {

    public WelcomeRegistrationState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    protected void navigate(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }
}
