package com.philips.cdp.appframework.modularui;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.homescreen.HomeActivity;
import com.philips.cdp.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.cdp.registration.User;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIWelcomeScreenNavigation implements UIBaseNavigation {
    @Override
    public void onClick(int componentID,Context context) {
        switch (componentID){
            case R.id.start_registration_button:
                User user = new User(context);
                //AppFrameworkBaseActivity.setIntroScreenDonePressed();
                if (user.isUserSignIn()) {
                    context.startActivity(new Intent(context, HomeActivity.class));
                } else {
                    context.startActivity(new Intent(context, UserRegistrationActivity.class));
                }
                break;
        }
    }

    @Override
    public void onSwipe(int componentID,Context context) {

    }

    @Override
    public void onLongPress(int componentID,Context context) {

    }
}
