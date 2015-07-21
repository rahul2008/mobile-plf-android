
package com.philips.cdp.registration.events;

import android.app.Activity;

import java.util.ArrayList;

import com.philips.cdp.registration.listener.UserRegistrationListener;

public class UserRegistrationHelper {

	private static UserRegistrationHelper eventHelper;

	private ArrayList<UserRegistrationListener> userRegistrationListeners;

	private UserRegistrationHelper() {
		userRegistrationListeners = new ArrayList<UserRegistrationListener>();
	}

	public static synchronized UserRegistrationHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new UserRegistrationHelper();
		}
		return eventHelper;
	}

	public void registerEventNotification(UserRegistrationListener observer) {
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

	public void unregisterEventNotification(UserRegistrationListener observer) {
		if (userRegistrationListeners != null && observer != null) {
			for (int i = 0; i < userRegistrationListeners.size(); i++) {
				UserRegistrationListener tmp = userRegistrationListeners.get(i);
				if (tmp.getClass() == observer.getClass()) {
					userRegistrationListeners.remove(tmp);
				}
			}
		}
	}

	public void notifyonUserRegistrationCompleteEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserRegistrationComplete(activity);
				}
			}
		}
	}

	public void notifyOnPrivacyPolicyClickEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onPrivacyPolicyClick(activity);
				}
			}
		}
	}

	public void notifyOnTermsAndConditionClickEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onTermsAndConditionClick(activity);
				}
			}
		}
	}
}
