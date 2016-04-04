
package com.philips.cdp.registration.coppa.event;

import android.app.Activity;

import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;

import java.util.ArrayList;

public class UserRegistrationCoppaHelper {

	private static UserRegistrationCoppaHelper eventHelper;

	private ArrayList<UserRegistrationCoppaListener> userRegistrationListeners;

	private UserRegistrationCoppaHelper() {
		userRegistrationListeners = new ArrayList<UserRegistrationCoppaListener>();
	}

	public static synchronized UserRegistrationCoppaHelper getInstance() {
		if (eventHelper == null) {
			eventHelper = new UserRegistrationCoppaHelper();
		}
		return eventHelper;
	}

	public void registerEventNotification(UserRegistrationCoppaListener observer) {
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

	public void unregisterEventNotification(UserRegistrationCoppaListener observer) {
		if (userRegistrationListeners != null && observer != null) {
			for (int i = 0; i < userRegistrationListeners.size(); i++) {
				UserRegistrationCoppaListener tmp = userRegistrationListeners.get(i);
				if (tmp.getClass() == observer.getClass()) {
					userRegistrationListeners.remove(tmp);
				}
			}
		}
	}

	public void notifyonUserRegistrationCompleteEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserRegistrationComplete(activity);
				}
			}
		}
	}

	public void notifyOnPrivacyPolicyClickEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onPrivacyPolicyClick(activity);
				}
			}
		}
	}

	public void notifyOnTermsAndConditionClickEventOccurred(Activity activity) {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onTermsAndConditionClick(activity);
				}
			}
		}
	}


	public void notifyOnUserLogoutSuccess() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserLogoutSuccess();
				}
			}
		}
	}

	public void notifyOnUserLogoutFailure() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserLogoutFailure();
				}
			}
		}
	}

	public void notifyOnLogoutSuccessWithInvalidAccessToken() {
		if (userRegistrationListeners != null) {
			for (UserRegistrationCoppaListener eventListener : userRegistrationListeners) {
				if (eventListener != null) {
					eventListener.onUserLogoutSuccessWithInvalidAccessToken();
				}
			}
		}
	}
}
