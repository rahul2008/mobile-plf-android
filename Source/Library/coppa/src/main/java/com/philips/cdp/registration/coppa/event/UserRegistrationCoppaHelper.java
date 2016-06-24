
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.event;

import android.app.Activity;

import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;

import java.util.concurrent.CopyOnWriteArrayList;

public class UserRegistrationCoppaHelper {

    private static volatile UserRegistrationCoppaHelper eventHelper;

    private CopyOnWriteArrayList<UserRegistrationCoppaListener> userRegistrationListeners;

    private UserRegistrationCoppaHelper() {
        userRegistrationListeners = new CopyOnWriteArrayList<UserRegistrationCoppaListener>();
    }

    /**
     * {@code getInstance} method get instance of User Registration CoppaHelperr
     *
     * @return UserRegistrationCoppaHelper instance of event helper
     */
    public static synchronized UserRegistrationCoppaHelper getInstance() {
        if (eventHelper == null) {
            synchronized (UserRegistrationCoppaHelper.class) {
                if (eventHelper == null) {
                    eventHelper = new UserRegistrationCoppaHelper();
                }
            }
        }
        return eventHelper;
    }

    /**
     * {@code registerEventNotification} method get User Registration CoppaListener
     *
     * @param observer register Event Notification
     */
    public synchronized void registerEventNotification(UserRegistrationCoppaListener observer) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null && observer != null) {
                for (int i = 0; i < userRegistrationListeners.size(); i++) {
                    UserRegistrationCoppaListener tmp = userRegistrationListeners.get(i);
                    if (tmp.getClass() == observer.getClass()) {
                        userRegistrationListeners.remove(tmp);
                    }
                }
                userRegistrationListeners.add(observer);
            }
        }
    }

    /**
     * {@code unregisterEventNotification} method get User Registration CoppaListener
     *
     * @param observer unregister Event Notification
     */
    public synchronized void unregisterEventNotification(UserRegistrationCoppaListener observer) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null && observer != null) {
                for (int i = 0; i < userRegistrationListeners.size(); i++) {
                    UserRegistrationCoppaListener tmp = userRegistrationListeners.get(i);
                    if (tmp.getClass() == observer.getClass()) {
                        userRegistrationListeners.remove(tmp);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyonUserRegistrationCompleteEventOccurred} method get activity
     *
     * @param activity notify onUserRegistration Complete EventOccurred
     */
    public synchronized void notifyonUserRegistrationCompleteEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserRegistrationComplete(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnPrivacyPolicyClickEventOccurred} method get activity
     *
     * @param activity notify OnPrivacyPolicy Click EventOccurred
     */
    public synchronized void notifyOnPrivacyPolicyClickEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onPrivacyPolicyClick(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnTermsAndConditionClickEventOccurred} method get activity
     *
     * @param activity notify OnTermsAndCondition Click EventOccurred
     */
    public synchronized void notifyOnTermsAndConditionClickEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onTermsAndConditionClick(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnUserLogoutSuccess} method notify OnUser Logout Success
     */
    public synchronized void notifyOnUserLogoutSuccess() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutSuccess();
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnUserLogoutFailure} method notify OnUser Logout Failure
     */
    public synchronized void notifyOnUserLogoutFailure() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutFailure();
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnLogoutSuccessWithInvalidAccessToken} method notify OnLogout Success With Invalid AccessToken
     */
    public synchronized void notifyOnLogoutSuccessWithInvalidAccessToken() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutSuccessWithInvalidAccessToken();
                    }
                }
            }
        }
    }
}
