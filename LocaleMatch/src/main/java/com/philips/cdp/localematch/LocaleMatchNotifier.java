package com.philips.cdp.localematch;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.philips.cdp.localematch.enums.LocaleMatchError;

public final class LocaleMatchNotifier {

	private Set<LocaleMatchListener> mLocaleMatchListeners = Collections.synchronizedSet(new HashSet<LocaleMatchListener>());

	private static LocaleMatchNotifier mNotifier = null;

	private LocaleMatchNotifier() {

	}

	public synchronized static LocaleMatchNotifier getIntance() {
		if (mNotifier == null) {
			mNotifier = new LocaleMatchNotifier();
		}
		return mNotifier;
	}

	public void registerForLocaleMatchChange(LocaleMatchListener listener) {
		mLocaleMatchListeners.add(listener);
	}

	public void unRegisterLocaleMatchChange(LocaleMatchListener listener) {
		mLocaleMatchListeners.remove(listener);

	}

	public void notifyLocaleMatchChange(String locale) {
		for (LocaleMatchListener listener : mLocaleMatchListeners) {
			listener.onLocaleMatchRefreshed(locale);
		}
	}
	
	public void notifyLocaleMatchChangeForSingleListener(LocaleMatchListener listener, String locale) {
			listener.onLocaleMatchRefreshed(locale);
	}
	
	public void notifyLocaleMatchError(LocaleMatchError error) {
		for (LocaleMatchListener listener : mLocaleMatchListeners) {
			listener.onErrorOccurredForLocaleMatch(error);
		}
	}

}
