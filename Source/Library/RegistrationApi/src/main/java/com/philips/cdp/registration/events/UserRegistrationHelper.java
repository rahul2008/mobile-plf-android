
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationListener;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class User Registration helper
 */
public class UserRegistrationHelper {

    /* User registration helper*/
    private static volatile UserRegistrationHelper eventHelper;

    /* User registration listeners */
    private CopyOnWriteArrayList<UserRegistrationListener> userRegistrationListeners;

    /**
     * Class constructor
     */
    private UserRegistrationHelper() {
        userRegistrationListeners = new CopyOnWriteArrayList<UserRegistrationListener>();
    }

    /**
     * {@code UserRegistrationHelper} method for User registration helper get instance
     * @return eventHelper UserRegistratinHelper object
     */
    public static synchronized UserRegistrationHelper getInstance() {
        if (eventHelper == null) {
            synchronized (UserRegistrationHelper.class) {
                if (eventHelper == null) {
                    eventHelper = new UserRegistrationHelper();
                }
            }

        }
        return eventHelper;
    }

    /**
     * {@code registerEventNotification} method to registration event notification
     * @param observer UserRegistrationListener object
     */
    public synchronized void registerEventNotification(UserRegistrationListener observer) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null && observer != null) {
                for (int i = 0; i < userRegistrationListeners.size(); i++) {
                    UserRegistrationListener tmp = userRegistrationListeners.get(i);
                    if (tmp.getClass() == observer.getClass()) {
                        userRegistrationListeners.remove(tmp);
                    }
                }
                userRegistrationListeners.add(observer);
            }
        }
    }

    /**
     * {@code unregisterEventNotification} method to unregister event notification
     * @param observer UserRegistrationListener object
     */
    public synchronized void unregisterEventNotification(UserRegistrationListener observer) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null && observer != null) {
                for (int i = 0; i < userRegistrationListeners.size(); i++) {
                    UserRegistrationListener tmp = userRegistrationListeners.get(i);
                    if (tmp.getClass() == observer.getClass()) {
                        userRegistrationListeners.remove(tmp);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyonUserRegistrationCompleteEventOccurred} method to Notify on user registration complete event occurred
     * @param activity
     */
    public synchronized void notifyonUserRegistrationCompleteEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserRegistrationComplete(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnPrivacyPolicyClickEventOccurred} method to notify on privace policy click event occurred
     * @param activity
     */
    public synchronized void notifyOnPrivacyPolicyClickEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onPrivacyPolicyClick(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnTermsAndConditionClickEventOccurred} method to notify on terms and condition click event occurred
     * @param activity
     */
    public synchronized void notifyOnTermsAndConditionClickEventOccurred(Activity activity) {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onTermsAndConditionClick(activity);
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnUserLogoutSuccess} method to notify on user logout success
     */
    public synchronized void notifyOnUserLogoutSuccess() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutSuccess();
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnUserLogoutFailure} method to notify on user logout failure
     */
    public synchronized void notifyOnUserLogoutFailure() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutFailure();
                    }
                }
            }
        }
    }

    /**
     * {@code notifyOnLogoutSuccessWithInvalidAccessToken} method to notify on logout success with invalid access token
     */
    public synchronized void notifyOnLogoutSuccessWithInvalidAccessToken() {
        synchronized (userRegistrationListeners) {
            if (userRegistrationListeners != null) {
                for (UserRegistrationListener eventListener : userRegistrationListeners) {
                    if (eventListener != null) {
                        eventListener.onUserLogoutSuccessWithInvalidAccessToken();
                    }
                }
            }
        }
    }
}
