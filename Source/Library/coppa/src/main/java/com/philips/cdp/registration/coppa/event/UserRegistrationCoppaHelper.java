
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
