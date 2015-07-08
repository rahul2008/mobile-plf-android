
package com.philips.cl.di.reg.events;

import java.util.ArrayList;

import com.philips.cl.di.reg.listener.UserRegistrationListener;

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

	public void notifyonUserRegistrationCompleteEventOccurred() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserRegistrationComplete();
				}
			}
		}
	}

	public void notifyOnPrivacyPolicyClickEventOccurred() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onPrivacyPolicyClick();
				}
			}
		}
	}

	public void notifyOnTermsAndConditionClickEventOccurred() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onTermsAndConditionClick();
				}
			}
		}
	}
}
