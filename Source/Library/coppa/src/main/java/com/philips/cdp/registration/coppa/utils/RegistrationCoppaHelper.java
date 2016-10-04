
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.coppa.event.UserRegistrationCoppaHelper;
import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;
import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaUIEventListener;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.events.UserRegistrationHelper;

public class RegistrationCoppaHelper {


    private static volatile RegistrationCoppaHelper mRegistrationHelper = null;


    private RegistrationCoppaHelper() {
    }


    public UserRegistrationCoppaUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationUIEventListener;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationCoppaUIEventListener userRegistrationUIEventListener) {
        this.userRegistrationUIEventListener = userRegistrationUIEventListener;
    }

    private UserRegistrationCoppaUIEventListener userRegistrationUIEventListener;



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


    public synchronized void registerUserRegistrationListener(UserRegistrationCoppaListener userRegistrationListener) {
        UserRegistrationCoppaHelper.getInstance().registerEventNotification(userRegistrationListener);
        UserRegistrationHelper.getInstance().registerEventNotification(RegistrationCoppaFragment.getUserRegistrationListener());
    }

    public synchronized void unRegisterUserRegistrationListener(UserRegistrationCoppaListener userRegistrationListener) {
        UserRegistrationCoppaHelper.getInstance().unregisterEventNotification(userRegistrationListener);
        UserRegistrationHelper.getInstance().unregisterEventNotification(RegistrationCoppaFragment.getUserRegistrationListener());
    }

    public synchronized UserRegistrationCoppaHelper getUserRegistrationListener() {
        return UserRegistrationCoppaHelper.getInstance();
    }


    public synchronized static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
