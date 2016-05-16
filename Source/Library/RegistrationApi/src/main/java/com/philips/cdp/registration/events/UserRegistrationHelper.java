
package com.philips.cdp.registration.events;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRegistrationHelper {

    private static UserRegistrationHelper eventHelper;

    private List<UserRegistrationListener> userRegistrationListeners;

    private UserRegistrationHelper() {
        userRegistrationListeners = Collections.synchronizedList(new ArrayList<UserRegistrationListener>());
    }

    public static synchronized UserRegistrationHelper getInstance() {
        if (eventHelper == null) {
            eventHelper = new UserRegistrationHelper();
        }
        return eventHelper;
    }

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
