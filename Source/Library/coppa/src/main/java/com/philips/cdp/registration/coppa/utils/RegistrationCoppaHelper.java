
package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.coppa.event.UserRegistrationCoppaHelper;
import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.events.UserRegistrationHelper;

public class RegistrationCoppaHelper {


    private static RegistrationCoppaHelper mRegistrationHelper = null;


    private RegistrationCoppaHelper() {
    }


    public synchronized static RegistrationCoppaHelper getInstance() {
        if (mRegistrationHelper == null) {
            synchronized (RegistrationCoppaHelper.class) {
                if (mRegistrationHelper == null) {
                    mRegistrationHelper = new RegistrationCoppaHelper();
                }
            }

        }
        return mRegistrationHelper;
    }


    public void registerUserRegistrationListener(UserRegistrationCoppaListener userRegistrationListener) {
        UserRegistrationCoppaHelper.getInstance().registerEventNotification(userRegistrationListener);
        UserRegistrationHelper.getInstance().registerEventNotification(RegistrationCoppaFragment.getUserRegistrationListener());
    }

    public void unRegisterUserRegistrationListener(UserRegistrationCoppaListener userRegistrationListener) {
        UserRegistrationCoppaHelper.getInstance().unregisterEventNotification(userRegistrationListener);
        UserRegistrationHelper.getInstance().registerEventNotification(RegistrationCoppaFragment.getUserRegistrationListener());
    }

    public UserRegistrationCoppaHelper getUserRegistrationListener() {
        return UserRegistrationCoppaHelper.getInstance();
    }


    public static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
